package cn.rong.wechat.anull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
//import cn.rongcloud.beauty.RCRTCBeautyEngine;
//import cn.rongcloud.quic.utils.QuicVersion;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.media.player.api.RCPlayerEngine;
//import cn.rongcloud.voicebeautifier.RCRTCVoiceBeautifierEngine;
//import com.example.anull.MockAppServer.GetTokenCallback;
import io.rong.calllib.RongCallClient;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ConnectionErrorCode;
import io.rong.imlib.RongIMClient.DatabaseOpenStatus;

public class MainActivity extends Base {

//    public static String APP_KEY = "c9kqb3rdkbb8j";
//    public static String APP_SECRET = "uTNrkYskbNC";
//    public static String NAV_SERVER = "https://nav-ucqa.rongcloud.net";
//    public static String MEDIA_SERVER = "https://rtc-media-ucbj2-01.rongcloud.net";
//    public static String API_SERVER = "http://api-ucqa.rongcloud.net";

    // 默认配置信息
    public static String APP_KEY = "e5t4ouvptkm2a";
    public static String APP_SECRET = "vB4FakXm8f68";
    public static String NAV_SERVER = "https://nav.cn.ronghub.com";
    public static String MEDIA_SERVER = "https://rtc-info.ronghub.com";
    public static String API_SERVER = "https://api.cn.ronghub.com";


    private TextView mTvVersion, imState;
    private EditText edit_userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mTvVersion = findViewById(R.id.tvVersion);
//        imState  = findViewById(R.id.imState);
//        edit_userId = findViewById(R.id.edit_userId);
//        setSDKVersion();
//        checkAudioVideoPermission();
    }

    private void setSDKVersion() {
        StringBuilder builder = new StringBuilder();
        builder.append("im：");
        builder.append(RongIMClient.getVersion());
        builder.append("，");

        builder.append("rtc：");
        builder.append(RCRTCEngine.getVersion());
        builder.append("，");

        builder.append("quic：");
       // builder.append(QuicVersion.getVersion());
        builder.append("，");

        builder.append("call：");
        builder.append(RongCallClient.getVersion());
        builder.append("，");

        builder.append("player：");
        builder.append(RCPlayerEngine.getVersion());
        builder.append("，");

        builder.append("faceBeauty：");
     //   builder.append(RCRTCBeautyEngine.getVersion());
        builder.append("，");

        builder.append("voiceBeauty：");
       // builder.append(RCRTCVoiceBeautifierEngine.getVersion());
        builder.append("，");

        mTvVersion.setText(builder.toString());
    }

    public void mainClick(View view) {
//        int id = view.getId();
//        if (id == R.id.btnLogin) {
//            imLogin();
//        } else if (id == R.id.btnCall) {
//            Intent intent = new Intent(MainActivity.this, CallActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.btnRTC) {
//            Intent intent = new Intent(MainActivity.this, RTCActivity.class);
//            startActivity(intent);
//        }
    }

//    private void imLogin() {
//        RongIMClient.getInstance().logout();
//        RongIMClient.getInstance().disconnect();
//        RongIMClient.setServerInfo(NAV_SERVER, "");
//        RongIMClient.init(MainActivity.this, APP_KEY, false);
//        MockAppServer.getToken(APP_KEY, APP_SECRET, edit_userId.getText().toString().trim(), API_SERVER, new GetTokenCallback() {
//            @Override
//            public void onGetTokenSuccess(String token) {
//                RongIMClient.connect(token, new ConnectCallback() {
//                    @Override
//                    public void onSuccess(String t) {
//                        showToast("登录成功");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imState.setText("登录成功");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(ConnectionErrorCode e) {
//                        showToast("登录失败："+e.getValue());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imState.setText("登录失败："+e.getValue());
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onDatabaseOpened(DatabaseOpenStatus code) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imState.setText("onDatabaseOpened");
//                            }
//                        });
//                    }
//                });
//            }
//
//            @Override
//            public void onGetTokenFailed(String err) {
//                showToast("获取token失败："+err);
//            }
//        });
//    }
}