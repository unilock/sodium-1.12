package me.jellysquid.mods.sodium.mixin.features.options;

import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngame.class)
public class MixinInGameHud {
    @Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isFancyGraphicsEnabled()Z"))
    private boolean redirectFancyGraphicsVignette() {
        // return SodiumClientMod.options().quality.enableVignette;
        return GuiIngameForge.renderVignette;
    }
}
