package cn.rong.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.rong.wechat.R;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.RCRTCRemoteUser;
import cn.rongcloud.rtc.api.RCRTCRoom;
import cn.rongcloud.rtc.api.RCRTCRoomConfig;
import cn.rongcloud.rtc.api.RCRTCVideoStream;
import cn.rongcloud.rtc.api.callback.IRCRTCEngineEventListener;
import cn.rongcloud.rtc.api.callback.IRCRTCResultCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCResultDataCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCRoomEventsListener;
import cn.rongcloud.rtc.api.stream.RCRTCInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCVideoView;
import cn.rongcloud.rtc.base.RCRTCAudioEventCode;
import cn.rongcloud.rtc.base.RCRTCConnectionState;
import cn.rongcloud.rtc.base.RCRTCLiveRole;
import cn.rongcloud.rtc.base.RCRTCMediaType;
import cn.rongcloud.rtc.base.RCRTCParamsType;
import cn.rongcloud.rtc.base.RCRTCRoomType;
import cn.rongcloud.rtc.base.RCRTCVideoEventCode;
import cn.rongcloud.rtc.base.RTCErrorCode;
import cn.rongcloud.rtc.center.RCRoomEventListenerWrapper;
import io.rong.common.fwlog.FwLog;
import io.rong.imkit.utils.KitStorageUtils;

public class ChatLiveActivity extends BaseActivity {
    private static final String TAG = "ChatLiveActivity";

    private RCRTCRoom mRoom;

    public static void start(Context context){
        context.startActivity(new Intent(context,ChatLiveActivity.class));
    }

    private FrameLayout video_loca,video_remote;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_chat_live;
    }

    @Override
    protected void initView() {
        video_loca = findViewById(R.id.video_loca);
        video_remote= findViewById(R.id.video_remote);
    }

    @Override
    protected void initData() {
        RCRTCEngine.getInstance().init(this.getApplicationContext(),null);
        RCRTCEngine.getInstance().registerEventListener(ircrtcEngineEventListener);
        getPermission();
    }

    private void getPermission(){
        PermissionUtils.permissionGroup(PermissionConstants.CAMERA,PermissionConstants.STORAGE,
                PermissionConstants.MICROPHONE).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                Log.e(TAG,"去加房间");
                joinRoom();
            }

            @Override
            public void onDenied() {

            }
        }).request();
    }


    private void joinRoom(){
        RCRTCRoomConfig.Builder roomconifg = RCRTCRoomConfig.Builder.create();
        roomconifg.setRoomType(RCRTCRoomType.LIVE_AUDIO_VIDEO);
        roomconifg.setLiveRole(RCRTCLiveRole.BROADCASTER);

        RCRTCEngine.getInstance().joinRoom("999",roomconifg.build(), new IRCRTCResultDataCallback<RCRTCRoom>() {
            @Override
            public void onSuccess(RCRTCRoom data) {
                data.registerRoomListener(ircrtcRoomEventsListener);
                mRoom = data;
                ToastUtils.showLong("加入房间成功");
                publishStream();
            }

            @Override
            public void onFailed(RTCErrorCode errorCode) {

            }
        });
    }

    private void publishStream(){
        RCRTCEngine.getInstance().getDefaultVideoStream().startCamera(null);
        mRoom.getLocalUser().publishDefaultStreams(new IRCRTCResultCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showLong("发布成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setLocaVideoView();
                    }
                });

            }

            @Override
            public void onFailed(RTCErrorCode errorCode) {

            }
        });
    }

    private void setLocaVideoView(){
        RCRTCVideoView rcrtcVideoView = new RCRTCVideoView(this);
        RCRTCEngine.getInstance().getDefaultVideoStream().setVideoView(rcrtcVideoView);
        video_loca.addView(rcrtcVideoView);
    }


    private void sub(List<RCRTCInputStream> streams){
        List<RCRTCInputStream> streams1 = new ArrayList<>();
       // streams1 = mRoom.getRemoteUsers()
         mRoom.getLocalUser().subscribeStreams(streams, new IRCRTCResultCallback() {
             @Override
             public void onSuccess() {
                 ToastUtils.showLong("订阅远端成功");
                 RCRTCVideoView rcrtcVideoView = new RCRTCVideoView(ChatLiveActivity.this.getApplicationContext());
                 for (int i = 0; i < streams.size(); i++) {
                     if (streams.get(i).getMediaType() == RCRTCMediaType.VIDEO){
                         ((RCRTCVideoStream)streams.get(i)).setVideoView(rcrtcVideoView);
                         break;
                     }
                 }

                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         video_remote.addView(rcrtcVideoView);
                     }
                 });
             }

             @Override
             public void onFailed(RTCErrorCode errorCode) {

             }
         });
    }




    IRCRTCRoomEventsListener ircrtcRoomEventsListener = new IRCRTCRoomEventsListener() {
        @Override
        public void onRemoteUserPublishResource(RCRTCRemoteUser remoteUser, List<RCRTCInputStream> streams) {
            sub(streams);
        }

        @Override
        public void onRemoteUserMuteAudio(RCRTCRemoteUser remoteUser, RCRTCInputStream stream, boolean mute) {

        }

        @Override
        public void onRemoteUserMuteVideo(RCRTCRemoteUser remoteUser, RCRTCInputStream stream, boolean mute) {

        }

        @Override
        public void onRemoteUserUnpublishResource(RCRTCRemoteUser remoteUser, List<RCRTCInputStream> streams) {

        }

        @Override
        public void onUserJoined(RCRTCRemoteUser remoteUser) {

        }

        @Override
        public void onUserLeft(RCRTCRemoteUser remoteUser) {

        }

        @Override
        public void onUserOffline(RCRTCRemoteUser remoteUser) {

        }

        @Override
        public void onPublishLiveStreams(List<RCRTCInputStream> streams) {
            LogUtils.e(TAG,streams.toArray());
        }

        @Override
        public void onUnpublishLiveStreams(List<RCRTCInputStream> streams) {

        }
    };




    IRCRTCEngineEventListener ircrtcEngineEventListener = new IRCRTCEngineEventListener() {
        @Override
        public void onKicked(String roomId, RCRTCParamsType.RCRTCKickedReason kickedReason) {

        }

        @Override
        public void onLocalAudioEventNotify(RCRTCAudioEventCode event) {
            super.onLocalAudioEventNotify(event);
        }

        @Override
        public void onLocalVideoEventNotify(RCRTCVideoEventCode event) {
            super.onLocalVideoEventNotify(event);
        }

        @Override
        public void onConnectionStateChanged(RCRTCConnectionState state) {
            super.onConnectionStateChanged(state);
        }
    };
}