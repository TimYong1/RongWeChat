package cn.rong.wechat.activity;

import cn.rongcloud.rtc.api.RCRTCEngine;
import io.rong.callkit.BaseCallActivity;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;

public class CallActivity extends BaseCallActivity {

    @Override
    public void onCallDisconnected(RongCallSession callProfile, RongCallCommon.CallDisconnectedReason reason) {
        super.onCallDisconnected(callProfile, reason);
    }
}
