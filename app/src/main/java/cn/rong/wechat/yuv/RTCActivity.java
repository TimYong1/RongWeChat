package cn.rong.wechat.yuv;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;


import cn.rong.wechat.R;
import cn.rongcloud.rtc.api.RCRTCConfig.Builder;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.RCRTCRemoteUser;
import cn.rongcloud.rtc.api.RCRTCRoom;
import cn.rongcloud.rtc.api.RCRTCRoomConfig;
import cn.rongcloud.rtc.api.callback.IRCRTCAudioRouteListener;
import cn.rongcloud.rtc.api.callback.IRCRTCResultCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCResultDataCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCRoomEventsListener;
import cn.rongcloud.rtc.api.callback.IRCRTCVideoInputFrameListener;
import cn.rongcloud.rtc.api.stream.RCRTCInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCOutputStream;
import cn.rongcloud.rtc.api.stream.RCRTCVideoInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCVideoView;
import cn.rongcloud.rtc.audioroute.RCAudioRouteType;
import cn.rongcloud.rtc.base.RCRTCLiveRole;
import cn.rongcloud.rtc.base.RCRTCMediaType;
import cn.rongcloud.rtc.base.RCRTCRemoteVideoFrame;
import cn.rongcloud.rtc.base.RCRTCRoomType;
import cn.rongcloud.rtc.base.RTCErrorCode;
import cn.rongcloud.rtc.core.RendererCommon.ScalingType;
import io.rong.imlib.RongIMClient;
import java.util.ArrayList;
import java.util.List;

public class RTCActivity extends Base implements IRCRTCAudioRouteListener {

  public static void start(Context context){
      context.startActivity(new Intent(context,RTCActivity.class));
  }

    private FrameLayout localFrameLayout;
    private FrameLayout remoteFrameLayout;
    private EditText editTextRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtcactivity);
        localFrameLayout = findViewById(R.id.localFrameLayout);
        remoteFrameLayout = findViewById(R.id.remoteRTCFrameLayout);
        editTextRoomId = findViewById(R.id.editRoomId);
        TextView tvUserId = findViewById(R.id.tvUserId);
        tvUserId.setText(RongIMClient.getInstance().getCurrentUserId());
        create();
    }

    public void rtcClick(View view) {
        if (view.getId() == R.id.btnJoinRoom) {
            joinRoom();
        } else  if (view.getId() == R.id.btnLeaveRoom) {
            localFrameLayout.removeAllViews();
            RCRTCEngine.getInstance().leaveRoom(new IRCRTCResultCallback() {
                @Override
                public void onSuccess() {
                    RCRTCEngine.getInstance().unInit();
                }

                @Override
                public void onFailed(RTCErrorCode errorCode) {
                    RCRTCEngine.getInstance().unInit();
                }
            });
        } else if (view.getId() == R.id.btnFinish) {
            RCRTCEngine.getInstance().unInit();
           this.finish();
        }
    }



    private void joinRoom() {
        RCRTCEngine.getInstance().unInit();
        Builder configBuilder = Builder.create();
        configBuilder.enableEncoderTexture(false);
        RCRTCEngine.getInstance().init(RTCActivity.this.getApplicationContext(), configBuilder.build());
        List<RCRTCOutputStream> defaultAudioStream = new ArrayList<>();
        defaultAudioStream.add(RCRTCEngine.getInstance().getDefaultAudioStream());
        List<RCRTCOutputStream> defaultVideoStream = new ArrayList<>();
        defaultVideoStream.add(RCRTCEngine.getInstance().getDefaultVideoStream());
        if (RCRTCEngine.getInstance().getDefaultVideoStream()!=null){
            RCRTCEngine.getInstance().getDefaultVideoStream().startCamera(null);
            RCRTCVideoView videoView = new RCRTCVideoView(RTCActivity.this.getApplicationContext());
            RCRTCEngine.getInstance().getDefaultVideoStream().setVideoView(videoView);
            localFrameLayout.addView(videoView);
        }
        RCRTCRoomConfig.Builder roomconifg = RCRTCRoomConfig.Builder.create();
        roomconifg.setRoomType(RCRTCRoomType.LIVE_AUDIO_VIDEO);
        roomconifg.setLiveRole(RCRTCLiveRole.BROADCASTER);
        RCRTCEngine.getInstance().joinRoom(editTextRoomId.getText().toString().trim(), roomconifg.build(), new IRCRTCResultDataCallback<RCRTCRoom>() {
            @Override
            public void onSuccess(RCRTCRoom data) {
                showToast("加入房间成功");
                data.registerRoomListener(roomEventsListener);
                data.getLocalUser().publishStreams(defaultAudioStream, new IRCRTCResultCallback() {
                    @Override
                    public void onSuccess() {
                        showToast("发布音频成功");
                    }

                    @Override
                    public void onFailed(RTCErrorCode errorCode) {
                        showToast("发布音频失败："+errorCode.getValue());
                    }
                });

                data.getLocalUser().publishStreams(defaultVideoStream, new IRCRTCResultCallback() {
                    @Override
                    public void onSuccess() {
                        showToast("发布视频成功");
                    }

                    @Override
                    public void onFailed(RTCErrorCode errorCode) {
                        showToast("发布视频失败："+errorCode.getValue());
                    }
                });
                if (data.getRemoteUsers().size() > 0) {
                    List<RCRTCInputStream> streams = new ArrayList<>();
                    for (RCRTCRemoteUser remoteUser : data.getRemoteUsers()) {
                        streams.addAll(remoteUser.getStreams());
                    }
                    sub(streams);
                }
            }

            @Override
            public void onFailed(RTCErrorCode errorCode) {

            }
        });
    }

    public IRCRTCRoomEventsListener roomEventsListener = new IRCRTCRoomEventsListener() {
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
        }

        @Override
        public void onUnpublishLiveStreams(List<RCRTCInputStream> streams) {
        }

        @Override
        public void onFirstRemoteAudioFrame(String userId, String tag) {
            super.onFirstRemoteAudioFrame(userId, tag);
            showToast("收到 "+userId +" 音频首帧");
        }

        @Override
        public void onFirstRemoteVideoFrame(String userId, String tag, int width, int height) {
            super.onFirstRemoteVideoFrame(userId, tag, width, height);
            showToast("收到 "+userId +" 视频首帧");
        }
    };

    private void sub(List<RCRTCInputStream> streams) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RCRTCVideoView videoView = new RCRTCVideoView(RTCActivity.this.getApplicationContext());
                videoView.setScalingType(ScalingType.SCALE_ASPECT_FIT);
                for (RCRTCInputStream stream : streams) {
                    List<RCRTCInputStream> streamsTmp = new ArrayList<>();
                    streamsTmp.add(stream);
                    if (stream.getMediaType() == RCRTCMediaType.VIDEO) {
                        RCRTCVideoInputStream videoInputStream = (RCRTCVideoInputStream) stream;
                        videoInputStream.setVideoView(videoView);
                        videoInputStream.setVideoFrameListener(new IRCRTCVideoInputFrameListener() {
                            @Override
                            public void onFrame(RCRTCRemoteVideoFrame videoFrame) {
                                setVideoFrame(videoFrame);
                            }
                        });
                    }
                    RCRTCEngine.getInstance().getRoom().getLocalUser().subscribeStreams(streamsTmp, new IRCRTCResultCallback() {
                        @Override
                        public void onSuccess() {
                            showToast("订阅 "+stream.getMediaType().name()+" 成功");
                        }

                        @Override
                        public void onFailed(RTCErrorCode errorCode) {
                            showToast("订阅 "+stream.getMediaType().name()+" 失败："+errorCode);
                        }
                    });
                }
            }
        });
    }

    private void setVideoFrame(RCRTCRemoteVideoFrame videoFrame){
        RCRTCRemoteVideoFrame.RTCBufferI420 buffer = (RCRTCRemoteVideoFrame.RTCBufferI420) videoFrame.getBuffer();
     //   LogUtils.e("videoFrame",buffer.getHeight()+"height"+buffer.getWidth()+"widht");
        GLManager.getInstance().setYuvData(buffer.getDataY(),buffer.getDataU(),buffer.getDataV(),buffer.getWidth(),buffer.getHeight());
    }

    @Override
    public void onRouteChanged(RCAudioRouteType type) {
        showToast("onRouteChanged->type : "+type.name());
    }

    @Override
    public void onRouteSwitchFailed(RCAudioRouteType fromType, RCAudioRouteType toType) {
        showToast("onRouteSwitchFailed->fromType : "+fromType.name() +" ,toType : "+toType.name());
    }


    private void create() {
        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.getHolder().addCallback(callback);
        remoteFrameLayout.addView(surfaceView);
       // GLManager.getInstance().setSize(192, 240);
        //释放的时候调用
     //   GLManager.getInstance().release();
        //设置分辨率 一次就行

    }

    private Callback callback = new Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            GLManager.getInstance().createGLEnv(holder.getSurface());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };
}