package cn.rong.wechat;

import android.app.Application;

import cn.rong.wechat.common.Config;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class ChatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this, Config.App_key);
       // RongIM.init(this, "p5tvi9dspkl94");
    }
}
