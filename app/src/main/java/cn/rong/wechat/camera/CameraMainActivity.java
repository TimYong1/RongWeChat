package cn.rong.wechat.camera;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import cn.rong.wechat.R;

public class CameraMainActivity extends AppCompatActivity implements View.OnClickListener {
    public static void start(Context context){
        context.startActivity(new Intent(context,CameraMainActivity.class));
    }

    public static final String TAG = "GLCamera_MainActivity";

    private static final int PERMISSIONS_REQUEST_CAMERA = 1;

    private final MainHandler mMainHandler = new MainHandler(this);

    Camera2Wrapper camera2;
    MyGLSurfaceView mGLSurfaceView;

    WindowManager wm;
    int mWinWidth;
    int mWinHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main_layout);
        mGLSurfaceView = findViewById(R.id.cameraPreview);
        mGLSurfaceView.setPreviewSize(1280,720);

        wm = this.getWindowManager();
        mWinWidth = wm.getDefaultDisplay().getWidth();
        
        ViewGroup.LayoutParams lp = mGLSurfaceView.getLayoutParams();
        lp.width = mWinWidth;
        lp.height =mWinWidth * 9/16;
        mGLSurfaceView.setLayoutParams(lp);

        findViewById(R.id.btnChangeSizeToSmall).setOnClickListener(this);
        findViewById(R.id.btnChangeSizeToFullScreen).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "APP now do not have a camera permission.Need to requestPermission now!");
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
            return false;
        } else {
            Log.w(TAG, "APP already have camera permission.");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions,
                                            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "onRequestPermissionsResult: Camera permission granted! ");
                mMainHandler.notifyDeviceReady();
            }
        } else {
            Log.w(TAG, "onRequestPermissionsResult: Camera permission grant deny!");
        }
    }



    private static class MainHandler extends Handler {
        private static final int MSG_CAMERA_ALL_READY = 111;

        private final WeakReference<CameraMainActivity> mWeakMain;

        public MainHandler(CameraMainActivity main) {
            mWeakMain = new WeakReference<>(main);
        }

        public void notifyDeviceReady(){
            sendMessage(obtainMessage(MSG_CAMERA_ALL_READY,0));
        }

        @Override
        public void handleMessage(Message msg) {
            CameraMainActivity main = mWeakMain.get();
            if (main == null) {
                return;
            }

            switch (msg.what) {
                case MSG_CAMERA_ALL_READY:
                    main.startCameraPreview();
                    break;
                default:
                    throw new RuntimeException("Unknown message " + msg.what);
            }
        }
    }


    private void startCameraPreview(){
        Log.w(TAG,"ready to go");
        if(null == camera2) {
            camera2 = new Camera2Wrapper(getApplicationContext(), 0, "front camera",1280,720);
        }
        camera2.setGLSurfaceView(mGLSurfaceView);
        camera2.openCamera();
    }

    private void startCamera(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermissions()){
                //
                Toast.makeText(getApplicationContext(),"The camera is authorized",Toast.LENGTH_SHORT).show();
                startCameraPreview();
            }
        }else{
            startCameraPreview();
        }
    }

    private void stopCamera(){
        camera2.releaseCamera();
        camera2 = null;
    }


    @Override
    public void onClick(View view) {
        ViewGroup.LayoutParams lp = mGLSurfaceView.getLayoutParams();
        switch (view.getId()){
            case R.id.btnChangeSizeToSmall:
                lp.width = 1;
                lp.height = 1;
                mGLSurfaceView.setLayoutParams(lp);

                stopCamera();
                break;

            case R.id.btnChangeSizeToFullScreen:
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mGLSurfaceView.setLayoutParams(lp);

                startCamera();
                break;
            default:

                break;
        }
    }
}