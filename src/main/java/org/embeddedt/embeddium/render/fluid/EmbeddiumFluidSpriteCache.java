package org.embeddedt.embeddium.render.fluid;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class EmbeddiumFluidSpriteCache {
    // Cache the sprites array to avoid reallocating it on every call
    private final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];
    private final Object2ObjectOpenHashMap<ResourceLocation, TextureAtlasSprite> spriteCache = new Object2ObjectOpenHashMap<>();

    private TextureAtlasSprite getTexture(ResourceLocation identifier) {
        TextureAtlasSprite sprite = spriteCache.get(identifier);

        if (sprite == null) {
            sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(identifier.toString());;
            spriteCache.put(identifier, sprite);
        }

        return sprite;
    }

    public TextureAtlasSprite[] getSprites(Fluid fluid) {
        sprites[0] = getTexture(fluid.getStill());
        sprites[1] = getTexture(fluid.getFlowing());
        ResourceLocation overlay = fluid.getOverlay();
        sprites[2] = overlay != null ? getTexture(overlay) : null;
        return sprites;
    }
}
