package cn.rong.wechat.anull;

/*
 *  Copyright 2015 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */


import android.content.res.Resources;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Some OpenGL static utility functions.
 */
public class GlUtil {
    private GlUtil() {
    }

    private static final String TAG = "GlUtil";

    public static FloatBuffer createSquareVtx() {
        final float vtx[] = {
                // XYZ, UV
                -1f, 1f, 0f, 0f, 1f,
                -1f, -1f, 0f, 0f, 0f,
                1f, 1f, 0f, 1f, 1f,
                1f, -1f, 0f, 1f, 0f,
        };
        ByteBuffer bb = ByteBuffer.allocateDirect(4 * vtx.length);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(vtx);
        fb.position(0);
        return fb;
    }


    public static FloatBuffer createVertexBufferImage() {  //顶点镜像
        final float vtx[] = {
                // XYZ
                1f, 1f, 0f,
                1f, -1f, 0f,
                -1f, 1f, 0f,
                -1f, -1f, 0f,
        };

        ByteBuffer bb = ByteBuffer.allocateDirect(4 * vtx.length);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(vtx);
        fb.position(0);
        return fb;
    }

    public static FloatBuffer createVertexBuffer() {
        final float vtx[] = {
                // XYZ
                -1f, 1f, 0f,
                -1f, -1f, 0f,
                1f, 1f, 0f,
                1f, -1f, 0f,
        };

        ByteBuffer bb = ByteBuffer.allocateDirect(4 * vtx.length);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(vtx);
        fb.position(0);
        return fb;
    }

    public static FloatBuffer createTexCoordBuffer() {
        final float vtx[] = {
                // UV
                0f, 1f,
                0f, 0f,
                1f, 1f,
                1f, 0f,
        };
        ByteBuffer bb = ByteBuffer.allocateDirect(4 * vtx.length);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(vtx);
        fb.position(0);
        return fb;
    }

    public static float[] createIdentityMtx() {
        float[] m = new float[16];
        Matrix.setIdentityM(m, 0);
        return m;
    }

    public static int createProgram(String vertexSource, String fragmentSource) {
        int vs = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        int fs = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vs);
        GLES20.glAttachShader(program, fs);
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    public static int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        //
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        //
        return shader;
    }

    public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException("glGetError encountered (see log)");
        }
    }

    public static void checkEglError(String op) {
        int error;
        while ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            throw new RuntimeException("eglGetError encountered (see log)");
        }
    }

    public static byte[] NV21ToI420p(int width, int height, byte[] src) {
        byte[] dst = new byte[src.length];
        int size = width * height;
        System.arraycopy(src, 0, dst, 0, size);
        int k = 0;
        for (int i = 0; i < size / 2; i += 2) {
            dst[size + k] = src[size + i + 1];
            dst[size + size / 4 + k] = src[size + i];
            ++k;
        }
        return dst;
    }

    private static int convertYUVtoARGB(int y, int u, int v) {
        int r,g,b;
        r = y + (int)(1.402f*u);
        g = y - (int)(0.344f*v + 0.714f*u);
        b = y + (int)(1.772f*v);
        r = r>255? 255 : r<0 ? 0 : r;
        g = g>255? 255 : g<0 ? 0 : g;
        b = b>255? 255 : b<0 ? 0 : b;
        return 0xff000000 | (r<<16) | (g<<8) | b;
    }

    public static String loadFromAssets(String fileName, Resources resources) {
        String result = null;
        try {
            InputStream is = resources.getAssets().open(fileName);
            int length = is.available();
            byte[] data = new byte[length];
            is.read(data);
            is.close();
            result = new String(data, "UTF-8");
            result.replace("\\r\\n", "\\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String createFragmentShaderString(String genericFragmentSource, ShaderType shaderType) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (shaderType == ShaderType.OES) {
            stringBuilder.append("#extension GL_OES_EGL_image_external : require\n");
        }
        stringBuilder.append("precision mediump float;\n");
        stringBuilder.append("varying vec2 tc;\n");

        if (shaderType == ShaderType.YUV) {
            stringBuilder.append("uniform sampler2D y_tex;\n");
            stringBuilder.append("uniform sampler2D u_tex;\n");
            stringBuilder.append("uniform sampler2D v_tex;\n");

            // Add separate function for sampling texture.
            // yuv_to_rgb_mat is inverse of the matrix defined in YuvConverter.
            stringBuilder.append("vec4 sample(vec2 p) {\n");
            stringBuilder.append("  float y = texture2D(y_tex, p).r * 1.16438;\n");
            stringBuilder.append("  float u = texture2D(u_tex, p).r;\n");
            stringBuilder.append("  float v = texture2D(v_tex, p).r;\n");
            stringBuilder.append("  return vec4(y + 1.59603 * v - 0.874202,\n");
            stringBuilder.append("    y - 0.391762 * u - 0.812968 * v + 0.531668,\n");
            stringBuilder.append("    y + 2.01723 * u - 1.08563, 1);\n");
            stringBuilder.append("}\n");
            stringBuilder.append(genericFragmentSource);
        } else {
            final String samplerName =
                shaderType == ShaderType.OES ? "samplerExternalOES" : "sampler2D";
            stringBuilder.append("uniform ").append(samplerName).append(" tex;\n");

            // Update the sampling function in-place.
            stringBuilder.append(genericFragmentSource.replace("sample(", "texture2D(tex, "));
        }

        return stringBuilder.toString();
    }

    /** Converts a float[16] matrix array to android.graphics.Matrix. */
    public static android.graphics.Matrix convertMatrixToAndroidGraphicsMatrix(float[] matrix4x4) {
        // clang-format off
        float[] values = {
            matrix4x4[0 * 4 + 0], matrix4x4[1 * 4 + 0], matrix4x4[3 * 4 + 0],
            matrix4x4[0 * 4 + 1], matrix4x4[1 * 4 + 1], matrix4x4[3 * 4 + 1],
            matrix4x4[0 * 4 + 3], matrix4x4[1 * 4 + 3], matrix4x4[3 * 4 + 3],
        };
        // clang-format on

        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setValues(values);
        return matrix;
    }

    public static float[] convertMatrixFromAndroidGraphicsMatrix(android.graphics.Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);

        // The android.graphics.Matrix looks like this:
        // [x1 y1 w1]
        // [x2 y2 w2]
        // [x3 y3 w3]
        // We want to contruct a matrix that looks like this:
        // [x1 y1  0 w1]
        // [x2 y2  0 w2]
        // [ 0  0  1  0]
        // [x3 y3  0 w3]
        // Since it is stored in column-major order, it looks like this:
        // [x1 x2 0 x3
        //  y1 y2 0 y3
        //   0  0 1  0
        //  w1 w2 0 w3]
        // clang-format off
        float[] matrix4x4 = {
            values[0 * 3 + 0],
            values[1 * 3 + 0],
            0,
            values[2 * 3 + 0],
            values[0 * 3 + 1],
            values[1 * 3 + 1],
            0,
            values[2 * 3 + 1],
            0,
            0,
            1,
            0,
            values[0 * 3 + 2],
            values[1 * 3 + 2],
            0,
            values[2 * 3 + 2],
        };
        // clang-format on
        return matrix4x4;
    }
}
