package me.jellysquid.mods.sodium.client.render;

import me.jellysquid.mods.sodium.client.util.math.MatrixStack;
import org.lwjgl.BufferUtils;
import repack.joml.Matrix4f;

import java.nio.FloatBuffer;

public class GameRendererContext {
    private static Matrix4f PROJECTION_MATRIX;

    public static void captureProjectionMatrix(org.lwjgl.util.vector.Matrix4f matrix) {
        PROJECTION_MATRIX = new Matrix4f(matrix.m00, matrix.m01, matrix.m02, matrix.m03, matrix.m10, matrix.m11, matrix.m12, matrix.m13, matrix.m20, matrix.m21, matrix.m22, matrix.m23, matrix.m30, matrix.m31, matrix.m32, matrix.m33);
    }

    private static final FloatBuffer bufModelViewProjection = BufferUtils.createFloatBuffer(16);

    /**
     * Obtains a model-view-projection matrix by multiplying the projection matrix with the model-view matrix
     * from {@param matrices}.
     *
     * The returned buffer is only valid for the lifetime of {@param stack}.
     *
     * @return A float-buffer on the stack containing the model-view-projection matrix in a format suitable for
     * uploading as uniform state
     */
    public static FloatBuffer getModelViewProjectionMatrix(MatrixStack.Entry matrices) {
        if (PROJECTION_MATRIX == null) {
            throw new IllegalStateException("Projection matrix has not been captured");
        }

        Matrix4f matrix = new Matrix4f(PROJECTION_MATRIX);
        matrix.mul(matrices.getModel());
        matrix.get(bufModelViewProjection);

        return bufModelViewProjection;
    }
}
