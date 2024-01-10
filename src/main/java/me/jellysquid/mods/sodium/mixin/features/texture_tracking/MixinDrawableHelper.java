package me.jellysquid.mods.sodium.mixin.features.texture_tracking;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinDrawableHelper {
    @Inject(method = "drawTexturedModalRect(IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;II)V", at = @At("HEAD"))
    public void onHeadDrawSprite(int x, int y, TextureAtlasSprite sprite, int width, int height, CallbackInfo ci) {
        SpriteUtil.markSpriteActive(sprite);
    }
}
