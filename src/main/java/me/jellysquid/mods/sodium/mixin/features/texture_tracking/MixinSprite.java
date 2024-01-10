package me.jellysquid.mods.sodium.mixin.features.texture_tracking;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.render.texture.SpriteExtended;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TextureAtlasSprite.class)
public abstract class MixinSprite implements SpriteExtended {
    private boolean forceNextUpdate;

    @Shadow
    protected List<int[][]> framesTextureData;

    @Shadow
    protected int originX;

    @Shadow
    protected int originY;

    @Shadow
    protected int width;

    @Shadow
    protected int height;

    @Shadow
    protected int tickCounter;

    @Shadow
    private AnimationMetadataSection animationMetadata;

    @Shadow
    protected int frameCounter;

    @Shadow
    public abstract int getFrameCount();

    @Shadow
    protected int[][] interpolatedFrameData;

    @Shadow
    protected abstract void updateAnimationInterpolated();

    /**
     * @author JellySquid
     * @reason Allow conditional texture updating
     */
    @Overwrite
    public void updateAnimation() {
        this.tickCounter++;

        boolean onDemand = SodiumClientMod.options().advanced.animateOnlyVisibleTextures;

        if (!onDemand || this.forceNextUpdate) {
            this.uploadTexture();
        } else {
            // Check and update the frame index anyway to avoid getting out of sync
            if (this.tickCounter >= this.animationMetadata.getFrameTime()) {
                int frameCount = this.animationMetadata.getFrameCount() == 0 ? this.getFrameCount() : this.animationMetadata.getFrameCount();
                this.frameCounter = (this.frameCounter + 1) % frameCount;
                this.tickCounter = 0;
            }
        }
    }

    private void uploadTexture() {
        if (this.tickCounter >= this.animationMetadata.getFrameTime()) {
            int prevFrameIndex = this.animationMetadata.getFrameIndex(this.frameCounter);
            int frameCount = this.animationMetadata.getFrameCount() == 0 ? this.getFrameCount() : this.animationMetadata.getFrameCount();

            this.frameCounter = (this.frameCounter + 1) % frameCount;
            this.tickCounter = 0;

            int frameIndex = this.animationMetadata.getFrameIndex(this.frameCounter);

            if (prevFrameIndex != frameIndex && frameIndex >= 0 && frameIndex < this.getFrameCount()) {
                TextureUtil.uploadTextureMipmap(this.framesTextureData.get(frameIndex), this.width, this.height, this.originX, this.originY, false, false);
            }
        } else if (this.interpolatedFrameData != null) {
            this.updateAnimationInterpolated();
        }

        this.forceNextUpdate = false;
    }

    @Override
    public void markActive() {
        this.forceNextUpdate = true;
    }
}
