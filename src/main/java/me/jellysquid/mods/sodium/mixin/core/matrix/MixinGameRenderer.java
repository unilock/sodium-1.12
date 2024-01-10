package me.jellysquid.mods.sodium.mixin.core.matrix;

import me.jellysquid.mods.sodium.client.render.GameRendererContext;
import net.minecraft.client.shader.ShaderGroup;
import org.lwjgl.util.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderGroup.class)
public class MixinGameRenderer {

    @Shadow
    private Matrix4f projectionMatrix;

    @Inject(method = "resetProjectionMatrix", at = @At("HEAD"))
    public void captureProjectionMatrix(CallbackInfo ci) {
        GameRendererContext.captureProjectionMatrix(projectionMatrix);
    }
}
