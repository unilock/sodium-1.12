package me.jellysquid.mods.sodium.mixin.features.texture_tracking;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public abstract class MixinSpriteBillboardParticle {
    @Shadow
    protected TextureAtlasSprite particleTexture;

    private boolean shouldTickSprite;

    @Inject(method = "setParticleTexture", at = @At("RETURN"))
    private void afterSetSprite(TextureAtlasSprite atlasSprite, CallbackInfo ci) {
        this.shouldTickSprite = atlasSprite != null && atlasSprite.hasAnimationMetadata();
    }

    @Inject(method = "renderParticle", at = @At("HEAD"))
    public void buildGeometry(BufferBuilder builder, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (this.shouldTickSprite) {
            SpriteUtil.markSpriteActive(this.particleTexture);
        }
    }
}