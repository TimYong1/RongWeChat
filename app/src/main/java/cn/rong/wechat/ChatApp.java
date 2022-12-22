package cn.rong.wechat;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.rong.wechat.common.Config;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;

public class ChatApp extends Application {
    private static final String TAG = "ChatApp";

    @Override
    public void onCreate() {
        super.onCreate();
        RongIMClient.init(this, Config.App_key);
    }
}
