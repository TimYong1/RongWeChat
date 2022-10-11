package cn.rong.wechat.http;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * Time：2021/9/14
 * Author：Tim
 */
public class TimUtils {


    public static boolean isRLogin = false;
    private static UserInfo userInfo;
    private static loginStatu mLoginStatu;

    public static void getToken(String userId, loginStatu loginStatu) {
        mLoginStatu = loginStatu;
        TokenServer.getToken(userId, new TokenServer.tokenCallback() {
            @Override
            public void success(String token) {
                conRongServer(token);
            }

            @Override
            public void error() {
                mLoginStatu.loginFail();
            }
        });
    }

    public static void conRongServer(String token) {
        if (!TextUtils.isEmpty(token)) {
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
                }
            });
        } else {

        }
    }

    private static void sendMessage() {
        String content = "你好";
        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        // 构建消息
        TextMessage messageContent = TextMessage.obtain(content);
//        userInfo = new UserInfo(Config.hw, "我是小米", Uri.parse(""));
//        messageContent.setUserInfo(userInfo);
        //RongUserInfoManager.getInstance().refreshUserInfoCache(userInfo);
        Message message = Message.obtain("222", conversationType, messageContent);

        // 发送消息
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

    public interface loginStatu {
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
