/*package me.jellysquid.mods.sodium.mixin.features.entity.smooth_lighting;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.model.light.EntityLighter;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.entity.EntityLightSampler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public abstract class MixinEntityRenderer<T extends Entity> implements EntityLightSampler<T> {
    @Shadow
    protected abstract int getBlockLight(T entity, BlockPos blockPos);

    @Shadow(remap = false)
    protected abstract int func_239381_b_(T entity, BlockPos blockPos);

    @Unique
    private float partialTicks;

    @Inject(method = "renderEntityStatic", at = @At("HEAD"))
    public void catchPartialTick(Entity entity, float partialTicks, boolean bl, CallbackInfo ci) {
        this.partialTicks = partialTicks;
    }

    @Redirect(method = "renderEntityStatic", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBrightnessForRender()I"))
    private int sodium$getBrightnessForRender(Entity self) {
        if (Minecraft.getMinecraft().gameSettings.ambientOcclusion == SodiumGameOptions.LightingQuality.HIGH.getVanilla()) {
            return EntityLighter.getBlendedLight(this, self, partialTicks);
        }

        return self.getBrightnessForRender();
    }

    @Inject(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z", shift = At.Shift.AFTER), cancellable = true)
    private void preShouldRender(Entity entity, ICamera camera, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        SodiumWorldRenderer renderer = SodiumWorldRenderer.getInstanceNullable();

        if (renderer == null) {
            return;
        }

        // If the entity isn't culled already by other means, try to perform a second pass
        if (cir.getReturnValue() && !renderer.isEntityVisible(entity)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public int bridge$getBlockLight(T entity, BlockPos pos) {
        return this.getBlockLight(entity, pos);
    }

    @Override
    public int bridge$getSkyLight(T entity, BlockPos pos) {
        return this.func_239381_b_(entity, pos);
    }
}*/
