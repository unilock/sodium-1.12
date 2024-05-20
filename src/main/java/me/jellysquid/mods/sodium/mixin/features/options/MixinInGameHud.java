package me.jellysquid.mods.sodium.mixin.features.options;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraftforge.client.GuiIngameForge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * note that both {@link net.minecraft.client.gui.GuiIngame} and {@link net.minecraftforge.client.GuiIngameForge}
 * implements client GUI rendering, and mixins for GuiIngame can be totally useless since GuiIngame might be intentionally
 * ignored and barely invoked
 */
@Mixin(GuiIngameForge.class)
public class MixinInGameHud {
    @Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isFancyGraphicsEnabled()Z"))
    private boolean vintagium$redirectVignette() {
        return SodiumClientMod.options().quality.enableVignette;
    }
}
