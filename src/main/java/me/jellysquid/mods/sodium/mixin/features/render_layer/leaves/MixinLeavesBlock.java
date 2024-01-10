package me.jellysquid.mods.sodium.mixin.features.render_layer.leaves;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockLeaves.class)
public class MixinLeavesBlock extends Block {

    @Shadow
    protected boolean leavesFancy;

    public MixinLeavesBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return SodiumClientMod.options().quality.leavesQuality.isFancy(leavesFancy) ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (SodiumClientMod.options().quality.leavesQuality.isFancy(Minecraft.getMinecraft().gameSettings.fancyGraphics)) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        } else {
            return blockAccess.getBlockState(pos.offset(side)).getBlock() instanceof BlockLeaves || super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }
}