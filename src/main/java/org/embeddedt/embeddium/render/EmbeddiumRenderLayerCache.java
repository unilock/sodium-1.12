package org.embeddedt.embeddium.render;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.util.EnumUtil;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.StampedLock;

/**
 * Implements a caching layer over Forge's predicate logic in {@link BlockRenderLayer}. There is quite a bit
 * of overhead involved in dealing with the arbitrary predicates, so we cache a list of render layers
 * for each state (lazily), and just return that list. The StampedLock we use is not free, but it's
 * much more efficient than Forge's synchronization-based approach.
 */
public class EmbeddiumRenderLayerCache {
    private static final boolean DISABLE_CACHE = Boolean.getBoolean("embeddium.disableRenderLayerCache");
    private static final Reference2ReferenceOpenHashMap<BlockRenderLayer, ImmutableList<BlockRenderLayer>> SINGLE_LAYERS = new Reference2ReferenceOpenHashMap<>();
    private static final Reference2ReferenceOpenHashMap<IBlockProperties, ImmutableList<BlockRenderLayer>> LAYERS_BY_STATE = new Reference2ReferenceOpenHashMap<>();
    private static final StampedLock lock = new StampedLock();

    private static <H extends IBlockProperties> ImmutableList<BlockRenderLayer> findExisting(H state) {
        long stamp = lock.readLock();

        try {
            return LAYERS_BY_STATE.get(state);
        } finally {
            lock.unlock(stamp);
        }
    }

    /**
     * Retrieve the list of render layers for the given block/fluid state.
     * @param state a BlockState or FluidState
     * @return a list of render layers that the block/fluid state should be rendered on
     */
    public static <H extends IBlockProperties>  List<BlockRenderLayer> forState(H state) {
        if(DISABLE_CACHE) {
            return generateList(state);
        }

        ImmutableList<BlockRenderLayer> list = findExisting(state);

        if(list == null) {
            list = createList(state);
        }

        return list;
    }

    private static <H extends IBlockProperties> List<BlockRenderLayer> generateList(H state) {
        List<BlockRenderLayer> foundLayers = new ArrayList<>(2);
        if(state instanceof IBlockState) {
            IBlockState blockState = (IBlockState) state;
            for(BlockRenderLayer layer : EnumUtil.LAYERS) {
                if(blockState.getBlock().canRenderInLayer(blockState, layer)) {
                    foundLayers.add(layer);
                }
            }
        } else {
            throw new IllegalArgumentException("Unexpected type of state received: " + state.getClass().getName());
        }

        return foundLayers;
    }

    private static <H extends IBlockProperties> ImmutableList<BlockRenderLayer> createList(H state) {
        List<BlockRenderLayer> foundLayers = generateList(state);

        ImmutableList<BlockRenderLayer> layerList;

        // Deduplicate simple lists
        if(foundLayers.isEmpty()) {
            layerList = ImmutableList.of();
        } else if(foundLayers.size() == 1) {
            layerList = SINGLE_LAYERS.get(foundLayers.get(0));
            Objects.requireNonNull(layerList);
        } else {
            layerList = ImmutableList.copyOf(foundLayers);
        }

        long stamp = lock.writeLock();
        try {
            LAYERS_BY_STATE.put(state, layerList);
        } finally {
            lock.unlock(stamp);
        }

        return layerList;
    }

    /**
     * Invalidate the cached mapping of states to render layers to force the data to be queried
     * from Forge again.
     */
    public static void invalidate() {
        long stamp = lock.writeLock();
        try {
            LAYERS_BY_STATE.clear();
        } finally {
            lock.unlock(stamp);
        }
    }

    static {
        for(BlockRenderLayer layer : EnumUtil.LAYERS) {
            SINGLE_LAYERS.put(layer, ImmutableList.of(layer));
        }
        if(DISABLE_CACHE) {
            SodiumClientMod.logger().warn("Render layer cache is disabled, performance will be affected.");
        }
    }
}
