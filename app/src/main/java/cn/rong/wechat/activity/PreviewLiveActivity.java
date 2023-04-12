package cn.rong.wechat.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.constraintlayout.utils.widget.MotionButton;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.textfield.TextInputEditText;

import cn.rong.wechat.R;
import cn.rong.wechat.http.GroupCallApi;
import cn.rong.wechat.http.RongCallApi;
import cn.rong.wechat.http.CallHelper;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.stream.RCRTCVideoView;
import cn.rongcloud.rtc.base.RCRTCLiveRole;
import cn.rongcloud.rtc.core.RendererCommon;

public class PreviewLiveActivity extends BaseActivity implements View.OnClickListener {
   private MotionButton brodcaster, audience,video_share;
   private TextInputEditText live_roomid;

   private FrameLayout view_video;
    public static void start(Context context){
        Intent intent = new Intent(context, PreviewLiveActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_priview_live;
    }

    @Override
    protected void initView() {
        brodcaster = findViewById(R.id.brodcaster);
        audience = findViewById(R.id.audience);
        live_roomid = findViewById(R.id.live_roomid);
        video_share = findViewById(R.id.video_share);
        view_video = findViewById(R.id.view_video);
        brodcaster.setOnClickListener(this);
        audience.setOnClickListener(this);
        video_share.setOnClickListener(this);
      //  RongConfigCenter.conversationConfig().rc_media_selector_contain_video = false;
    }

    @Override
    protected void initData() {
        RCRTCEngine.getInstance().unInit();
        RCRTCEngine.getInstance().init(this,null);
        RCRTCEngine.getInstance().getDefaultVideoStream().startCamera(null);
        RCRTCVideoView rcrtcVideoView = new RCRTCVideoView(this);
        rcrtcVideoView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        RCRTCEngine.getInstance().getDefaultVideoStream().setVideoView(rcrtcVideoView);
        view_video.addView(rcrtcVideoView);
        LogUtils.e("AAA","create");
        LogUtils.e("AAA","create");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.brodcaster:
              ChatLiveActivity.start(this, RCRTCLiveRole.BROADCASTER.getType(),live_roomid.getText().toString());
                break;
            case R.id.audience:
              //  ChatLiveActivity.start(this, RCRTCLiveRole.AUDIENCE.getType(),live_roomid.getText().toString());
                CallHelper.init(new GroupCallApi());
                CallHelper.obtain().startCall("sss");
                break;
            case R.id.video_share:
              //  VideoShareActivity.start(this, live_roomid.getText().toString());
                CallHelper.init(new RongCallApi());
                CallHelper.obtain().startCall("sss");
                break;
        }
    }
}