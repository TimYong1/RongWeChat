package cn.rong.wechat.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.constraintlayout.utils.widget.MotionButton;

import com.google.android.material.textfield.TextInputEditText;

import cn.rong.wechat.R;
import cn.rong.wechat.activity.viewmoudle.PreviewLiveViewMoudle;
import cn.rong.wechat.anull.MainActivity;
import cn.rong.wechat.anull.RTCActivity;
import cn.rongcloud.rtc.api.RCRTCConfig;
import cn.rongcloud.rtc.base.RCRTCLiveRole;

public class PreviewLiveActivity extends BaseActivity implements View.OnClickListener {
   private MotionButton brodcaster, audience;
   private TextInputEditText live_roomid;
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
        brodcaster.setOnClickListener(this);
        audience.setOnClickListener(this);
      //  RongConfigCenter.conversationConfig().rc_media_selector_contain_video = false;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.brodcaster:
                Intent intent = new Intent(this, RTCActivity.class);
                startActivity(intent);
             //  ChatLiveActivity.start(this, RCRTCLiveRole.BROADCASTER.getType(),live_roomid.getText().toString());
                break;
            case R.id.audience:
                ChatLiveActivity.start(this, RCRTCLiveRole.AUDIENCE.getType(),live_roomid.getText().toString());
                break;
        }
    }
}