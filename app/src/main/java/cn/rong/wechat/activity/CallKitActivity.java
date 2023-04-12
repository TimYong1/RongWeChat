package cn.rong.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;

import cn.rong.wechat.R;
import cn.rong.wechat.utils.NotifyManagerUtils;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.stream.RCRTCVideoView;
import io.rong.callkit.RongCallKit;

public class CallKitActivity extends AppCompatActivity implements View.OnClickListener {
    private Button sendInvite;
    private TextInputEditText inputEditText;
    private FrameLayout video_loca;

    public static void start(Context context){
        context.startActivity(new Intent(context,CallKitActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_kit);
        initView();
    }

    private void initView() {
        sendInvite = findViewById(R.id.send_invite);
        inputEditText = findViewById(R.id.invite_userid);
        video_loca = findViewById(R.id.video_loca);
        sendInvite.setOnClickListener(this);
//        RCRTCEngine.getInstance().init(this,null);
//        RCRTCEngine.getInstance().getDefaultVideoStream().startCamera(null);
//        RCRTCVideoView view = new RCRTCVideoView(this);
//        RCRTCEngine.getInstance().getDefaultVideoStream().setVideoView(view);
//        video_loca.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_invite:
                //RCRTCEngine.getInstance().init(this,null);
               String inviteUserid = inputEditText.getText().toString();
//
               getPermission(inviteUserid);
             //  RongCallKit.startSingleCall(this,inviteUserid, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
                break;
        }
    }

    private void getPermission(String inviteUserid){
        PermissionUtils.permission(PermissionConstants.CAMERA,PermissionConstants.MICROPHONE).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                if (TextUtils.isEmpty(inviteUserid)){
                    ToastUtils.showShort("请输入正常的userid");
                    return;
                }
                RongCallKit.startSingleCall(CallKitActivity.this, inviteUserid, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);

//                if ( NotifyManagerUtils.isNotifyEnabled(CallKitActivity.this)){
//                    RongCallKit.startSingleCall(CallKitActivity.this, inviteUserid, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
//                }else {
//                    NotifyManagerUtils.openNotificationSettingsForApp(CallKitActivity.this);
//                }


            }

            @Override
            public void onDenied() {

            }
        }).request();
    }
}