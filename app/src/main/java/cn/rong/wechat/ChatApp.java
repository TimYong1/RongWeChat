package cn.rong.wechat;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.rong.wechat.common.Config;
import cn.rongcloud.liveroom.api.RCLiveEngine;
import cn.rongcloud.liveroom.api.RCLiveMixType;
import io.rong.callkit.RongCallKit;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class ChatApp extends Application {
    private static final String TAG = "ChatApp";

    private static ChatApp INSTANCE;

    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
        RongCallKit.setMainPageActivityClass(new String[]{MainActivity.class.getName()});
        RongIM.init(this, Config.App_key);
    }

    public static ChatApp getApplication() {
        return INSTANCE;
    }
}
