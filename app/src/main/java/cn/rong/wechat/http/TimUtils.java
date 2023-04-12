package cn.rong.wechat.http;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.StringTokenizer;

import cn.rong.wechat.R;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Time：2021/9/14
 * Author：Tim
 */
public class TimUtils {


    private boolean isRLogin = false;
    private LoginStatu mLoginStatu;
    private String userid,mToken;
    public TimUtils(){

    }

    public TimUtils(String userId, String token){
        this.userid = userId;
        this.mToken = token;
    }

    public void getToken(LoginStatu loginStatu) {
        mLoginStatu = loginStatu;
        if (TextUtils.isEmpty(mToken)){
            TokenServer.getToken(userid, new TokenServer.tokenCallback() {
                @Override
                public void success(String token) {
                    LogUtils.e("token是---"+token);
                    conRongServer(token);
                }

                @Override
                public void error() {
                    mLoginStatu.loginFail();
                }
            });
        }else {
            conRongServer(mToken);
        }
    }

    private void conRongServer(String token) {

//        //要设置channel 的优先级
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(R.drawable.ic_launcher+"","xxx", NotificationManager.IMPORTANCE_HIGH);
//            channel.enableLights(true);
//        }
       //是否在桌面icon右上角展示小红点channel.setLightColor(Color.GREEN); //小红点颜色channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知manager.createNotificationChannel(channel);
//       builder.setChannelId("001");}

      //  String token11 = "0DXyAhAh8COPPjG/mqOaExvBa5RwKCuMzBcGhPWG0Mg=@1rhg.sg.rongnav.com;1rhg.sg.rongcfg.com";
       // String token11 = "0DXyAhAh8COPPjG/mqOaExvBa5RwKCuM98vC95qA+Wg=@1rhg.sg.rongnav.com;1rhg.sg.rongcfg.com";
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String s) {
                    isRLogin = true;
                 //   sendMessage();
                    mLoginStatu.loginSuccess();
                }

                @Override
                public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                    //  ToastUtils.showLong("融云连接失败");
                    mLoginStatu.loginFail();
                    if (connectionErrorCode.equals(RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT)) {
                        //从 APP 服务获取新 token，并重连
                    } else {
                        //无法连接 IM 服务器，请根据相应的错误码作出对应处理
                    }
                }

                @Override
                public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                    mLoginStatu.loginFail();
                }
            });
    }

    private static void sendMessage() {
        String content = "你好";
        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        TextMessage messageContent = TextMessage.obtain(content);
        Message message = Message.obtain("222", conversationType, messageContent);
        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public interface LoginStatu {
        void loginSuccess();

        void loginFail();
    }


    private void dialogShow(Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("测试数据");
        alertDialog.setTitle("提示");
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showLong("取消");
            }
        });
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showLong("确定");
            }
        });
        alertDialog.show();
    }
}
