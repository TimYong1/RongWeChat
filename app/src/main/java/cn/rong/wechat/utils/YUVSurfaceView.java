package cn.rong.wechat.utils;

import static cn.rong.wechat.yuv.GlUtil.createProgram;

import android.content.Context;
import android.opengl.GLES20;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class YUVSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String VERTEX_SHADER =
            "attribute vec4 vPosition;\n" +
                    "attribute vec2 vTexCoord;\n" +
                    "varying vec2 texCoord;\n" +
                    "void main() {\n" +
                    "  texCoord = vTexCoord;\n" +
                    "  gl_Position = vPosition;\n" +
                    "}\n";
    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "uniform sampler2D y_texture;\n" +
                    "uniform sampler2D uv_texture;\n" +
                    "varying vec2 texCoord;\n" +
                    "void main() {\n" +
                    "  float y = texture2D(y_texture, texCoord).r;\n" +
                    "  float u = texture2D(uv_texture, texCoord).r - 0.5;\n" +
                    "  float v = texture2D(uv_texture, texCoord).g - 0.5;\n" +
                    "  vec3 rgb = vec3(y + 1.403 * v, y - 0.344 * u - 0.714 * v, y + 1.77 * u);\n" +
                    "  gl_FragColor = vec4(rgb, 1.0);\n" +
                    "}\n";

    private SurfaceHolder mHolder;
    private int mProgram;
    private int mPositionHandle;
    private int mTexCoordHandle;
    private int[] mTextureIds;
    private ByteBuffer mYBuffer;
    private ByteBuffer mUVBuffer;
    private float[] mVertices = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };
    private float[] mTexCoords = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    public YUVSurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mProgram = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoord");
        mTextureIds = new int[2];
        GLES20.glGenTextures(2, mTextureIds, 0);
        for (int i = 0; i < 2; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureIds[i]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        GLES20.glDeleteTextures(2, mTextureIds, 0);
    }

    public void render(byte[] yuv, int width, int height) {
        if (yuv == null || yuv.length != width * height * 3 / 2) {
            return;
        }
        int y_size = width * height;
        if (mYBuffer == null || mYBuffer.capacity() != y_size) {
            mYBuffer = ByteBuffer.allocate(y_size);
            mYBuffer.order(ByteOrder.nativeOrder());
        }
        mYBuffer.clear();
        mYBuffer.put(yuv, 0, y_size);
        mYBuffer.position(0);
        int uv_size = y_size / 4;
        if (mUVBuffer == null || mUVBuffer.capacity() != uv_size) {
            mUVBuffer = ByteBuffer.allocate(uv_size);
            mUVBuffer.order(ByteOrder.nativeOrder());
        }
        mUVBuffer.clear();
        mUVBuffer.put(yuv, y_size, uv_size);
        mUVBuffer.position(0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, FloatBuffer.wrap(mVertices));
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, FloatBuffer.wrap(mTexCoords));
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureIds[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, mYBuffer);
       // GLES20.glUniform1i(mYHandle, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureIds[1]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0, GLES20.GL_LUMINANCE_ALPHA, width / 2, height / 2, 0, GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, mUVBuffer);
    //    GLES20.glUniform1i(mUVHandle, 1);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glFinish();
    }
}



