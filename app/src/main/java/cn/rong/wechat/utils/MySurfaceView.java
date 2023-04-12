package cn.rong.wechat.utils;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
   // private YUVRender mYUVRender;

    public MySurfaceView(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
       // mYUVRender = new YUVRender();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
       // mYUVRender.init(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //mYUVRender.release();
    }

    public void drawFrame(byte[] yuvData, int width, int height) {
      //  mYUVRender.render(yuvData, width, height);
    }
}

