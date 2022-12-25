package cn.rong.wechat.anull;

import static android.opengl.EGL14.EGL_OPENGL_ES2_BIT;

import android.graphics.Bitmap;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

public class GLEngine {

    private static class holder {
        static GLEngine tools = new GLEngine();
    }

    public static GLEngine getInstance() {
        return holder.tools;
    }

    public static final FloatBuffer FULL_RECTANGLE_BUFFER =
        createFloatBuffer(
            new float[] {
                -1.0f, -1.0f, // Bottom left.
                1.0f, -1.0f, // Bottom right.
                -1.0f, 1.0f, // Top left.
                1.0f, 1.0f, // Top right.
            });
    // Texture coordinates - (0, 0) is bottom-left and (1, 1) is top-right.
    public static final FloatBuffer FULL_RECTANGLE_TEXTURE_BUFFER =
        createFloatBuffer(
            new float[] {
                0.0f, 0.0f, // Bottom left.
                1.0f, 0.0f, // Bottom right.
                0.0f, 1.0f, // Top left.
                1.0f, 1.0f, // Top right.
            });

    public EGLDisplay getEglDisplay() {
        EGLDisplay eglDisplay = EGL14.EGL_NO_DISPLAY;

        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("EGL error " + EGL14.eglGetError());
        }
        return eglDisplay;
    }

    public EGLConfig getEglConfig(EGLDisplay eglDisplay) {
        int[] configAttributes = {
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig(
            eglDisplay, configAttributes, 0, configs, 0, configs.length, numConfigs, 0)) {
            throw new RuntimeException(
                "eglChooseConfig failed: 0x" + Integer.toHexString(EGL14.eglGetError()));
        }
        if (numConfigs[0] <= 0) {
            throw new RuntimeException("Unable to find any matching EGL config");
        }
        final EGLConfig eglConfig = configs[0];
        if (eglConfig == null) {
            throw new RuntimeException("eglChooseConfig returned null");
        }
        return eglConfig;
    }

    public EGLContext createEglContext(EGLDisplay eglDisplay, EGLConfig eglConfig) {
        int[] contextAttributes = {
            EGL14.EGL_CONTEXT_CLIENT_VERSION,
            2,
            EGL14.EGL_NONE};
        final EGLContext eglContext = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, contextAttributes, 0);
        return eglContext;
    }

    public void release(int program, int oesTextureId, EGLContext eglContext, EGLConfig eglConfig, EGLDisplay eglDisplay, EGLSurface eglSurface) {
        GLES20.glUseProgram(0);
        if (program != -1) {
            GLES20.glDeleteProgram(program);
            program = -1;
        }
        GLES20.glDeleteTextures(1, new int[] {oesTextureId}, 0);
        oesTextureId = 0;

        // Detach the current EGL context, so that it can be made current on another thread.
        EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        // release Surface
        if (eglSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglDestroySurface(eglDisplay, eglSurface);
            eglSurface = EGL14.EGL_NO_SURFACE;
        }

        EGL14.eglDestroyContext(eglDisplay, eglContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(eglDisplay);
        eglContext = EGL14.EGL_NO_CONTEXT;
        eglDisplay = EGL14.EGL_NO_DISPLAY;
        eglConfig = null;
    }

    public int createAndLinkProgram(String vertexSource, String fragmentSource) {
        int vertexShader = createShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = createShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);

            // we can delete shader after attach
            GLES20.glDeleteShader(vertexShader);
            GLES20.glDeleteShader(fragmentShader);

            GLES20.glLinkProgram(program);
            // 存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        checkNoGLES2Error("createAndLinkProgram");
        GLES20.glUseProgram(program);
        return program;
    }

    private int createShader(int shaderType, String sourceCode) {
        int shaderId = GLES20.glCreateShader(shaderType);
        if (shaderId != 0) {
            GLES20.glShaderSource(shaderId, sourceCode);
            GLES20.glCompileShader(shaderId);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                GLES20.glDeleteShader(shaderId);
                shaderId = 0;
            }
        }
        return shaderId;
    }

    public EGLSurface createSurfaceInternal(Surface surface, EGLContext eglContext, EGLDisplay eglDisplay, EGLConfig eglConfig) {
        EGLSurface eglSurface = EGL14.EGL_NO_SURFACE;
        int[] surfaceAttribs = {EGL14.EGL_NONE};
        eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, surface, surfaceAttribs, 0);

        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
        return eglSurface;
    }

    private static FloatBuffer createFloatBuffer(float[] coords) {
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(coords);
        fb.position(0);
        return fb;
    }


    public void glDisableVertexAttribArray(int index) {
        GLES20.glDisableVertexAttribArray(index);
    }

    public void glEnableClientState() {
        GLES10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    }


    public void matrixOrthoM(int width, int height) {
        float [] projectionMatrix = new float[16];
        //适配屏幕宽高等
        float aspectRatio= width > height ? (float)width / (float)height: (float)height / (float)width;
        if(width > height){
            Matrix.orthoM(projectionMatrix,0, -aspectRatio, aspectRatio,-1f,1f,-1f,1f);
        }else{
            Matrix.orthoM(projectionMatrix,0,-1f,1f, -aspectRatio, aspectRatio,-1f,1f);
        }
    }

    public void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
        GLES20.glUniformMatrix4fv(location, count, transpose, value, offset);
    }

    public void eglSwapBuffers(EGLDisplay eglDisplay, EGLSurface eglSurface) {
        if (eglSurface == EGL14.EGL_NO_SURFACE) {
            throw new RuntimeException("No EGLSurface - can't swap buffers");
        }
        EGL14.eglSwapBuffers(eglDisplay, eglSurface);
    }

    public void eglSwapBuffers(EGLDisplay eglDisplay, EGLSurface eglSurface, long timeStampNs) {
        EGLExt.eglPresentationTimeANDROID(eglDisplay, eglSurface, timeStampNs);
        eglSwapBuffers(eglDisplay, eglSurface);
    }

    public void glFlush() {
        GLES20.glFlush();
    }
    public void glFinish() {
        GLES20.glFinish();
    }

    public void glDrawArrays(int mode, int first, int count) {
        GLES20.glDrawArrays(mode, first, count);
    }

    public void checkNoGLES2Error(String msg) {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(msg + ": GLES20 error: " + error);
        }
    }

    public int loadTexture(Bitmap bmp) {
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }
    public int generateTexture(int target) {
        final int textureArray[] = new int[1];
        GLES20.glGenTextures(1, textureArray, 0);
        final int textureId = textureArray[0];
        GLES20.glBindTexture(target, textureId);
        GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(target, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLEngine.getInstance().checkNoGLES2Error("generateTexture");
        return textureId;
    }
    public void glGenFramebuffers( int n, int[] frameBuffers, int offset) {
        GLES20.glGenFramebuffers(n, frameBuffers, offset);
    }

    public void glActiveTexture(int texture) {
        GLES20.glActiveTexture(texture);
    }

    public void glEnableVertexAttribArray(int index) {
        GLES20.glEnableVertexAttribArray(index);
    }

    public void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, java.nio.Buffer ptr) {
        GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
    }

    public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, java.nio.Buffer pixels) {
        GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }

    public void glFramebufferTexture2D( int target, int attachment, int textarget, int texture, int level){
        GLES20.glFramebufferTexture2D(target, attachment, textarget, texture, level);
    }

    public void glBindTexture(int target, int texture) {
        GLES20.glBindTexture(target, texture);
    }
    public void glBindFramebuffer(int target, int framebuffer) {
        GLES20.glBindFramebuffer(target, framebuffer);
    }

    public void glViewport( int x, int y, int width, int height) {
        GLES20.glViewport(x, y, width, height);
    }

    public void glClear(int mask) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
