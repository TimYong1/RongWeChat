package cn.rong.wechat;

import android.app.Application;

import io.rong.imlib.RongIMClient;

public class ChatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RongIMClient.init(this,"0vnjpoad03mvz");
    }
}
