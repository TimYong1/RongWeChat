package cn.rong.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import cn.rong.wechat.R;
import io.rong.callkit.BaseCallActivity;
import io.rong.calllib.RongCallSession;

public class MyCallActivity extends BaseCallActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_call);


    }

    @Override
    public void onCallOutgoing(RongCallSession callProfile, SurfaceView localVideo) {
        super.onCallOutgoing(callProfile, localVideo);



    }
}