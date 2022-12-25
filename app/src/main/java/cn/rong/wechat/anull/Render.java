package cn.rong.wechat.anull;

import static android.opengl.GLES20.GL_TEXTURE_2D;

import android.graphics.Matrix;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;
import java.nio.ByteBuffer;

public class Render {

    private EGLConfig eglConfig = null;
    private EGLDisplay eglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext eglContext = EGL14.EGL_NO_CONTEXT;
    private EGLSurface eglSurface;
    // YUV
    private ByteBuffer yBuffer, uBuffer, vBuffer;
    private byte[] bytesY, bytesU, bytesV;
    private int [] yuvTextures;
    private int texMatrixLocation;
    private int inPosLocation;
    private int inTcLocation;
    private final Matrix renderMatrix = new Matrix();


    ///
    private int mWidth;
    private int mHeight;
    private ShaderType currentShaderType = null;
    private int mProgram = 0;

    private static class holder {
        static Render tools = new Render();
    }

    public static Render getInstance() {
        return holder.tools;
    }

    public void setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void release() {
        GLEngine.getInstance().release(mProgram, 0, eglContext, eglConfig, eglDisplay, eglSurface);
    }

    public void createGLContext() {
        eglDisplay = GLEngine.getInstance().getEglDisplay();
        eglConfig = GLEngine.getInstance().getEglConfig(eglDisplay);
        eglContext = GLEngine.getInstance().createEglContext(eglDisplay, eglConfig);
    }

    public void createSurfaceInternal(Object surface) {
        eglSurface = GLEngine.getInstance().createSurfaceInternal((Surface) surface, eglContext, eglDisplay, eglConfig);
    }

    public void drawYUV(ByteBuffer datay, ByteBuffer datau, ByteBuffer datav,int width,int height) {
        long start = System.currentTimeMillis();
        if (yBuffer != null) {
            yBuffer.clear();
        }
        if (uBuffer != null) {
            uBuffer.clear();
        }
        if (vBuffer != null) {
            vBuffer.clear();
        }

        if (bytesY == null) {

        }
//        bytesY = new byte[width * height];
//        bytesU = new byte[width * height / 4];
//        bytesV = new byte[width * height / 4];
//
//        System.arraycopy(i420, 0, bytesY, 0, bytesY.length);
//        System.arraycopy(i420, bytesY.length, bytesU, 0, bytesU.length);
//        System.arraycopy(i420, bytesY.length + bytesU.length, bytesV, 0, bytesV.length);
        this.yBuffer = datay;
        this.uBuffer = datau;
        this.vBuffer = datav;
        this.mWidth = width;
        this.mHeight = height;

        if (yuvTextures == null) {
            yuvTextures = new int[3];
            for (int i = 0; i < 3; i++) {
                yuvTextures[i] = GLEngine.getInstance().generateTexture(GLES20.GL_TEXTURE_2D);
            }
        }

        for (int i = 0; i < 3; ++i) {
            GLEngine.getInstance().glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLEngine.getInstance().glBindTexture(GLES20.GL_TEXTURE_2D, yuvTextures[i]);
            if (i == 0) {
                GLEngine.getInstance().glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_LUMINANCE,
                    mWidth,
                    mHeight,
                    0,
                    GLES20.GL_LUMINANCE,
                    GLES20.GL_UNSIGNED_BYTE,
                    yBuffer);
            } else if (i == 1) {
                GLEngine.getInstance().glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE,
                    mWidth / 2, mHeight / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, uBuffer);
            } else if (i == 2) {
                GLEngine.getInstance().glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, mWidth / 2, mHeight / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, vBuffer);
            }
        }

        if (currentShaderType == null) {
            currentShaderType = ShaderType.YUV;

            mProgram = GLEngine.getInstance().createAndLinkProgram(YuvInfo.DEFAULT_VERTEX_SHADER_STRING,
                GlUtil.createFragmentShaderString(YuvInfo.FRAGMENT_SHADER, currentShaderType));

            int location_y_tex = GLES20.glGetUniformLocation(mProgram, "y_tex");
            GLES20.glUniform1i(location_y_tex, 0);

            int location_u_tex = GLES20.glGetUniformLocation(mProgram, "u_tex");
            GLES20.glUniform1i(location_u_tex, 1);

            int location_v_tex = GLES20.glGetUniformLocation(mProgram, "v_tex");
            GLES20.glUniform1i(location_v_tex, 2);

            texMatrixLocation = GLES20.glGetUniformLocation(mProgram, YuvInfo.TEXTURE_MATRIX_NAME);
            inPosLocation = GLES20.glGetAttribLocation(mProgram, YuvInfo.INPUT_VERTEX_COORDINATE_NAME);
            inTcLocation = GLES20.glGetAttribLocation(mProgram, YuvInfo.INPUT_TEXTURE_COORDINATE_NAME);
        }

        // Upload the vertex coordinates.
        GLEngine.getInstance().glEnableVertexAttribArray(inPosLocation);
        GLEngine.getInstance().glVertexAttribPointer(
            inPosLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            0,
            GLEngine.getInstance().FULL_RECTANGLE_BUFFER);

        // Upload the texture coordinates.
        GLES20.glEnableVertexAttribArray(inTcLocation);
        GLES20.glVertexAttribPointer(
            inTcLocation,
            2,
            GLES20.GL_FLOAT,
            false,
            0,
            GLEngine.getInstance().FULL_RECTANGLE_TEXTURE_BUFFER);

        renderMatrix.reset();
        renderMatrix.preTranslate(0.5f, 0.5f);
        renderMatrix.preScale(1f, -1f);
        renderMatrix.preRotate(0);
        renderMatrix.preTranslate(-0.5f, -0.5f);

        GLES20.glUniformMatrix4fv(
            texMatrixLocation,
            1,
            false,
            GlUtil.convertMatrixFromAndroidGraphicsMatrix(renderMatrix),
            0);


        GLES20.glViewport(0, 0, 720, 1360);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        EGL14.eglSwapBuffers(eglDisplay, eglSurface);

        for (int i = 0; i < 3; ++i) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GL_TEXTURE_2D, 0);
        }
        Log.d("bugtags", "time : " + (System.currentTimeMillis() - start));
    }
}
