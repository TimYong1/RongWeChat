package cn.rong.wechat.anull;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.nio.ByteBuffer;


public class GLManager {

    private static final String TAG = GLManager.class.getSimpleName();
    private Handler mHandler;

    private GLManager() {
        HandlerThread handlerThread = new HandlerThread(GLManager.class.getSimpleName());
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    private static class holder {
        static GLManager tools = new GLManager();
    }

    public static GLManager getInstance() {
        return holder.tools;
    }

    public void createGLEnv(final Surface surface) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Render.getInstance().createGLContext();
                Render.getInstance().createSurfaceInternal(surface);
            }
        });
    }

    public void release() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Render.getInstance().release();
            }
        });
    }

    public void setSize(final int width, final int height) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Render.getInstance().setSize(width, height);
            }
        });
    }

    public void setYuvData(ByteBuffer datay, ByteBuffer datau, ByteBuffer datav, final int width, final int height) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Render.getInstance().drawYUV(datay,datau,datav, width, height);
            }
        });
    }
}
