package me.jellysquid.mods.sodium.client.util;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class CustomFormats {

    public static final VertexFormat POSITION_COLOR_TEXTURE_LIGHT_NORMAL = new VertexFormat();

    static {
        POSITION_COLOR_TEXTURE_LIGHT_NORMAL.addElement(DefaultVertexFormats.POSITION_3F);
        POSITION_COLOR_TEXTURE_LIGHT_NORMAL.addElement(DefaultVertexFormats.COLOR_4UB);
        POSITION_COLOR_TEXTURE_LIGHT_NORMAL.addElement(DefaultVertexFormats.TEX_2F);
        POSITION_COLOR_TEXTURE_LIGHT_NORMAL.addElement(DefaultVertexFormats.TEX_2S);
        POSITION_COLOR_TEXTURE_LIGHT_NORMAL.addElement(DefaultVertexFormats.COLOR_4UB);
        POSITION_COLOR_TEXTURE_LIGHT_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        POSITION_COLOR_TEXTURE_LIGHT_NORMAL.addElement(DefaultVertexFormats.PADDING_1B);
    }

}
