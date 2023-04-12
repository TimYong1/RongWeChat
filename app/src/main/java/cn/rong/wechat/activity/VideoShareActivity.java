package cn.rong.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.rong.wechat.R;
import cn.rongcloud.rtc.api.RCRTCConfig;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.RCRTCMixConfig;
import cn.rongcloud.rtc.api.RCRTCRemoteUser;
import cn.rongcloud.rtc.api.RCRTCRoom;
import cn.rongcloud.rtc.api.RCRTCRoomConfig;
import cn.rongcloud.rtc.api.callback.IRCRTCResultCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCResultDataCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCRoomEventsListener;
import cn.rongcloud.rtc.api.stream.RCRTCInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCLiveInfo;
import cn.rongcloud.rtc.api.stream.RCRTCVideoInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCVideoStreamConfig;
import cn.rongcloud.rtc.api.stream.RCRTCVideoView;
import cn.rongcloud.rtc.base.RCRTCLiveRole;
import cn.rongcloud.rtc.base.RCRTCMediaType;
import cn.rongcloud.rtc.base.RCRTCParamsType;
import cn.rongcloud.rtc.base.RCRTCRoomType;
import cn.rongcloud.rtc.base.RCRTCStream;
import cn.rongcloud.rtc.base.RCRTCStreamType;
import cn.rongcloud.rtc.base.RTCErrorCode;

public class VideoShareActivity extends BaseActivity implements View.OnClickListener {

    private String roomid;
    private Button share_btn, sub_btn, pub_btn;
    private FrameLayout share_frame,loca_frame;
    private RCRTCRoom mRoom;

    public static void start(Context context, String roomid) {
        Intent starter = new Intent(context, VideoShareActivity.class);
        starter.putExtra("liveRoomid", roomid);
        context.startActivity(starter);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_video_share;
    }

    @Override
    protected void initView() {
        share_btn = findViewById(R.id.share_btn);
        sub_btn = findViewById(R.id.sub_btn);
        pub_btn = findViewById(R.id.pub_btn);
        share_frame = findViewById(R.id.share_frame);
        loca_frame = findViewById(R.id.loca_frame);
        share_btn.setOnClickListener(this);
        sub_btn.setOnClickListener(this);
        pub_btn.setOnClickListener(this);
    }

    /**
     * 创建自定义合流布局配置
     * @param
     * @param
     * @return
     */


    @Override
    protected void initData() {
        if (mRoom==null){
            RCRTCEngine.getInstance().init(this, RCRTCConfig.Builder.create().build());
            roomid = getIntent().getStringExtra("liveRoomid");
            RCRTCRoomConfig.Builder builder = RCRTCRoomConfig.Builder.create()
                    .setRoomType(RCRTCRoomType.MEETING)
                    .setLiveRole(RCRTCLiveRole.BROADCASTER);
            RCRTCEngine.getInstance().joinRoom(roomid,builder.build(), new IRCRTCResultDataCallback<RCRTCRoom>() {
                @Override
                public void onSuccess(RCRTCRoom data) {
                    ToastUtils.showShort("加入房间成功");
                   // setConfig();
                    mRoom = data;
                    data.registerRoomListener(ircrtcRoomEventsListener);
                }

                @Override
                public void onFailed(RTCErrorCode errorCode) {
                    ToastUtils.showShort("加入房间失败");
                }
            });
        }
    }

    private IRCRTCRoomEventsListener ircrtcRoomEventsListener = new IRCRTCRoomEventsListener() {
        @Override
        public void onRemoteUserPublishResource(RCRTCRemoteUser remoteUser, List<RCRTCInputStream> streams) {
//
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

        }

        @Override
        public void onUnpublishLiveStreams(List<RCRTCInputStream> streams) {

        }

        @Override
        public void onLeaveRoom(int i) {

        }
    };

    private void setConfig() {
        RCRTCVideoStreamConfig.Builder builder = RCRTCVideoStreamConfig.Builder.create();
            // 设置帧率
        builder.setVideoFps(RCRTCParamsType.RCRTCVideoFps.Fps_15);
           // 设置分辨率
        builder.setVideoResolution(RCRTCParamsType.RCRTCVideoResolution.RESOLUTION_720_1280);
          // 设置最大码率
        builder.setMaxRate(2500);
       // RCRTCEngine.getInstance().getScreenShareVideoStream().setVideoConfig(builder.build());
    }
    public void startScreenShare() {

//        RCRTCEngine.getInstance().getScreenShareVideoStream()
//                .startCaptureScreen(
//                        new IRCRTCResultCallback() {
//                            @Override
//                            public void onSuccess() {
//                                    mRoom.getLocalUser().publishStream(
//                                            RCRTCEngine.getInstance().getScreenShareVideoStream(),
//                                            new IRCRTCResultCallback() {
//                                                @Override
//                                                public void onSuccess() {
//                                                    ToastUtils.showShort("发布成功");
//                                                    runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            RCRTCVideoView rcrtcVideoView = new RCRTCVideoView(VideoShareActivity.this);
//                                                            RCRTCEngine.getInstance().getScreenShareVideoStream().setVideoView(rcrtcVideoView);
//                                                            share_frame.addView(rcrtcVideoView);
//                                                        }
//                                                    });
//
//                                                }
//
//                                                @Override
//                                                public void onFailed(RTCErrorCode errorCode) {
//                                                    ToastUtils.showShort("发布失败");
//                                                }
//                                            });
//
//                            }
//
//                            @Override
//                            public void onFailed(RTCErrorCode errorCode) {
//
//                            }
//                        });
    }


    private void subShareVideo(){
        List<RCRTCInputStream> rcrtcInputStreams = new ArrayList<>();
        for (RCRTCRemoteUser remoteUser : mRoom.getRemoteUsers()) {
            for (RCRTCInputStream inputStream : remoteUser.getStreams()) {
                if (inputStream.getMediaType() == RCRTCMediaType.VIDEO) {
//                    if (inputStream.getTag().equals(RCRTCStream.TAG_SCREEN_SHARE)){
//                        rcrtcInputStreams.add(inputStream);
//                    }
                }
            }
        }
            if (rcrtcInputStreams.size()==0){
                ToastUtils.showShort("没有流");
                return;
            }
            mRoom.getLocalUser().subscribeStreams(rcrtcInputStreams, new IRCRTCResultCallback() {
                @Override
                public void onSuccess() {
                    ToastUtils.showShort("订阅成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RCRTCVideoView rcrtcVideoView = new RCRTCVideoView(VideoShareActivity.this);
                            ((RCRTCVideoInputStream)rcrtcInputStreams.get(0)).setVideoView(rcrtcVideoView);
                            share_frame.addView(rcrtcVideoView);
                        }
                    });
                }

                @Override
                public void onFailed(RTCErrorCode errorCode) {
                    ToastUtils.showShort("订阅失败");
                }
            });
    }

//    private void stopShare(){
//        RCRTCEngine.getInstance().getScreenShareVideoStream().stopCaptureScreen();
//    }

    private void pubShareStream(){
//        mRoom.getLocalUser().publishLiveStream(
//                RCRTCEngine.getInstance().getScreenShareVideoStream(),
//                new IRCRTCResultDataCallback<RCRTCLiveInfo>() {
//                    @Override
//                    public void onSuccess(RCRTCLiveInfo data) {
//                        //todo 发布成功
//                        ToastUtils.showShort("发布成功");
//                    }
//
//                    @Override
//                    public void onFailed(RTCErrorCode errorCode) {
//                        //todo 发布失败
//                        ToastUtils.showShort("发布失败");
//                    }
//                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn:
                //startShare();
                startScreenShare();
                break;
            case R.id.sub_btn:
                subShareVideo();
                break;
            case R.id.pub_btn:
                pubShareStream();
                break;
        }
    }
}