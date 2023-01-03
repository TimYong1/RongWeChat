package cn.rong.wechat.activity;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cn.rong.wechat.R;
import cn.rong.wechat.adapter.AllMoudleAdadpter;
import cn.rong.wechat.moudle.MoudlesData;
import cn.rong.wechat.moudle.OnRecyclerItemClick;
import cn.rong.wechat.proxy.LiveBrodcaster;
import cn.rong.wechat.widget.VideoRendererView;
import cn.rong.wechat.widget.VideoViewManager;
import cn.rongcloud.rtc.api.RCRTCAudioMixer;
import cn.rongcloud.rtc.api.RCRTCConfig;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.RCRTCRemoteUser;
import cn.rongcloud.rtc.api.RCRTCRoom;
import cn.rongcloud.rtc.api.RCRTCRoomConfig;
import cn.rongcloud.rtc.api.RCRTCVideoStream;
import cn.rongcloud.rtc.api.callback.IRCRTCEngineEventListener;
import cn.rongcloud.rtc.api.callback.IRCRTCResultCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCResultDataCallback;
import cn.rongcloud.rtc.api.callback.IRCRTCStatusReportListener;
import cn.rongcloud.rtc.api.callback.IRCRTCVideoInputFrameListener;
import cn.rongcloud.rtc.api.report.RCRTCLiveAudioState;
import cn.rongcloud.rtc.api.report.StatusReport;
import cn.rongcloud.rtc.api.stream.RCRTCAudioStreamConfig;
import cn.rongcloud.rtc.api.stream.RCRTCCameraOutputStream;
import cn.rongcloud.rtc.api.stream.RCRTCInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCOutputStream;
import cn.rongcloud.rtc.api.stream.RCRTCVideoInputStream;
import cn.rongcloud.rtc.api.stream.RCRTCVideoView;
import cn.rongcloud.rtc.api.stream.view.RCRTCRendererEventsListener;
import cn.rongcloud.rtc.base.RCRTCAudioEventCode;
import cn.rongcloud.rtc.base.RCRTCConnectionState;
import cn.rongcloud.rtc.base.RCRTCLiveRole;
import cn.rongcloud.rtc.base.RCRTCMediaType;
import cn.rongcloud.rtc.base.RCRTCParamsType;
import cn.rongcloud.rtc.base.RCRTCRemoteVideoFrame;
import cn.rongcloud.rtc.base.RCRTCRoomType;
import cn.rongcloud.rtc.base.RCRTCVideoEventCode;
import cn.rongcloud.rtc.base.RTCErrorCode;

public class ChatLiveActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ChatLiveActivity";

    private RCRTCRoom mRoom;
    private String  roomid;
    private Button play_music, getLong,select;
    private String path;
    private int  mRole ;
    private TextView mMoreBtn;
    private VideoViewManager videoViewManager;
    private RecyclerView mAllMoudle;
    private AllMoudleAdadpter moudleAdadpter;
    private LiveBrodcaster liveBrodcaster;
    private RCRTCRoom rcrtcRoom;
    private File file1;
    private FrameLayout remote_video_view;
    private VideoRendererView rendererView;
   // private VideoRendererView rendererView1;

    private GLSurfaceView mGlSurfaceView;
    private int fd;
    private int width;
    // private JavaRenderer mRenderer;

    public static void start(Context context,int role,String roomid) {
        Intent intent = new Intent();
        intent.putExtra("liveRole",role);
        intent.putExtra("liveRoomid",roomid);
        intent.setClass(context, ChatLiveActivity.class);
        context.startActivity(intent);
    }

    private FrameLayout video_loca;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_chat_live;
    }

    @Override
    protected void initView() {
        video_loca = findViewById(R.id.video_loca);
        play_music = findViewById(R.id.play_music);
        getLong = findViewById(R.id.get_long);
        select = findViewById(R.id.select);
        mAllMoudle = findViewById(R.id.all_moudle);
        mMoreBtn = findViewById(R.id.btn_more);
        remote_video_view = findViewById(R.id.remote_video_view);
        MoudlesData moudlesData= new MoudlesData();
        moudleAdadpter = new AllMoudleAdadpter(moudlesData.getMoudlesData());
        mAllMoudle.setLayoutManager(new LinearLayoutManager(this));
        mAllMoudle.setAdapter(moudleAdadpter);
        play_music.setOnClickListener(this);
        getLong.setOnClickListener(this);
        select.setOnClickListener(this);
        mMoreBtn.setOnClickListener(this);
        mRole = getIntent().getIntExtra("liveRole",1);
        roomid = getIntent().getStringExtra("liveRoomid");
       LogUtils.e(TAG,"身份为"+mRole);
        video_loca.post(new Runnable() {
            @Override
            public void run() {
                width = video_loca.getWidth();
                videoViewManager = new VideoViewManager(video_loca,video_loca.getWidth(),video_loca.getHeight());
            }
        });
       // RCRTCEngine.getInstance().set
        moudleAdadpter.setOnItemClick(new OnRecyclerItemClick() {
            @Override
            public void onRecyclerViewClick(RecyclerView parent, View view, int postion) {
                if (postion==5){
                    RCRTCAudioStreamConfig audioStreamConfig=RCRTCAudioStreamConfig.Builder.create()
                            .setAGCCompression(60)
                            .setAGCTargetdbov(3)
                            .enableAGCLimiter(false)
                            .build();
                    RCRTCEngine.getInstance().getDefaultAudioStream().setAudioConfig(audioStreamConfig);
                }else if (postion == 2){
                  //  ToastUtils.showShort("跨房间连麦");
                    otherRoomreQuest();
                }
            }
        });


    }

    /**
     *申请跨房间连麦
     */
    private void otherRoomreQuest(){
        if(mRoom!=null){
            mRoom.getLocalUser().requestJoinOtherRoom("9999", "9992", false, "fdfd", new IRCRTCResultCallback() {
                @Override
                public void onSuccess() {
                   LogUtils.e("OtherRoom","邀请成功");
                }

                @Override
                public void onFailed(RTCErrorCode errorCode) {
                    LogUtils.e("OtherRoom","邀请失败");
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initData() {

        if (!checkOpenGLES30()) {
            ToastUtils.showLong("不支持3.0");
        }
//
        RCRTCConfig.Builder builder = RCRTCConfig.Builder.create();
      //  builder.setAudioSampleRate(16000);
        builder.enableEncoderTexture(false);

        RCRTCEngine.getInstance().init(this.getApplicationContext(), builder.build());
        RCRTCEngine.getInstance().registerStatusReportListener(ircrtcStatusReportListener);
        getPermission();
        RCRTCEngine.getInstance().registerEventListener(new IRCRTCEngineEventListener() {
            @Override
            public void onKicked(String roomId, RCRTCParamsType.RCRTCKickedReason kickedReason) {
                ToastUtils.showLong("您被踢出音视频房间" + roomId + "原因--" + kickedReason.name());
                Log.e(TAG, "您被踢出音视频房间" + roomId + "原因--" + kickedReason.name());

            }
        });

//        File file = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        LogUtils.e(file.getAbsolutePath());
//        file1 = file.listFiles()[0];
//        LogUtils.e("AAAAA", file1.getAbsolutePath());
//        File file5 = new File("/storage/emulated/0/Android/data/cn.rong.wechat/");
       TestHandle handle = new TestHandle();
        liveBrodcaster = new LiveBrodcaster(handle);
//        rendererView1 = new VideoRendererView(ChatLiveActivity.this);
//        initPlay();


        mGlSurfaceView = new GLSurfaceView(this);
        mGlSurfaceView.setEGLContextClientVersion(3); // 设置OpenGL版本号
        RCRTCEngine.getInstance().getDefaultAudioStream().setAudioQuality(RCRTCParamsType.AudioQuality.MUSIC_HIGH, RCRTCParamsType.AudioScenario.MUSIC_CHATROOM);
      //  mRenderer = new JavaRenderer(this);
//        mGlSurfaceView.setRenderer(mRenderer);
//        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 设置渲染模式为仅当手动执行requestRender时才绘制
//        // 实际场景中，可能是在相机预览回调或解码回调中调用，这里仅使用预设的yuv图片做示例
//        remote_video_view.addView(mGlSurfaceView);


    }

    private void drawYuv(ByteBuffer buffery,ByteBuffer bufferu,ByteBuffer bufferv,int width,int mPreviewHeight) {
        // 绘制的width必须是8的倍数，height必须是2的倍数，如果不是则需要对齐到8的倍数，否则渲染的结果不对

      //  mRenderer.setYuvData(buffery, bufferu,bufferv,width, mPreviewHeight);
//        byte[] i420 = FileUtil.getAssertData(this, "204x360_i420.yuv");
//        mRenderer.setYuvData(i420, 204, 360);
        mGlSurfaceView.requestRender(); // 手动触发渲染

    }

    private boolean checkOpenGLES30() {
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x30000);
    }


    private void getPermission() {
        PermissionUtils.permissionGroup(PermissionConstants.CAMERA,
                PermissionConstants.MICROPHONE,PermissionConstants.STORAGE).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                joinRoom();
            }

            @Override
            public void onDenied() {

            }
        }).request();
    }




    private void joinRoom() {
      //  RCRTCEngine.getInstance().getDefaultAudioStream().setAudioQuality(RCRTCParamsType.AudioQuality.SPEECH, RCRTCParamsType.AudioScenario.DEFAULT);
        RCRTCRoomConfig.Builder roomconifg = RCRTCRoomConfig.Builder.create();
        roomconifg.setRoomType(RCRTCRoomType.LIVE_AUDIO_VIDEO);
        roomconifg.setLiveRole(isBrodcaster()?RCRTCLiveRole.BROADCASTER:RCRTCLiveRole.AUDIENCE);
        RCRTCEngine.getInstance().joinRoom(roomid, roomconifg.build(), new IRCRTCResultDataCallback<RCRTCRoom>() {
            @Override
            public void onSuccess(RCRTCRoom data) {
                data.registerRoomListener(liveBrodcaster);
                mRoom = data;
                ToastUtils.showLong("加入房间成功");
                //       publishStream();
              //  subscribeStreams();
                if (isBrodcaster()){
                    publishStream();
                }else {
                    audiencesubscribe();
                }

            }

            @Override
            public void onFailed(RTCErrorCode errorCode) {
                ToastUtils.showLong("加入房间失败");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
       // RCRTCEngine.getInstance().unInit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // RCRTCEngine.getInstance().unInit();
    }

    private void publishStream() {

        RCRTCEngine.getInstance().getDefaultVideoStream().startCamera(null);
        mRoom.getLocalUser().publishDefaultStreams(new IRCRTCResultCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showLong("发布成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // setLocaVideoView();
                        subscribeStreams();
                    }
                });

            }

            @Override
            public void onFailed(RTCErrorCode errorCode) {
                ToastUtils.showLong("发布失败");
            }
        });
    }


    private void subscribeStreams() {
        List<RCRTCInputStream> streams = new ArrayList<>();
        List<RCRTCRemoteUser> remoteUsers = mRoom.getRemoteUsers();
        List<RCRTCOutputStream> outputStreams = new ArrayList<>();
        List<RCRTCInputStream> inputStreams = new ArrayList<>();
        for (RCRTCOutputStream outputStream : mRoom.getLocalUser().getStreams()){
            if (outputStream.getMediaType() == RCRTCMediaType.VIDEO){
                outputStreams.add(outputStream);
//                ((RCRTCCameraOutputStream)outputStream).setVideoFrameListener(new IRCRTCVideoOutputFrameListener() {
//                    @Override
//                    public RCRTCVideoFrame processVideoFrame(RCRTCVideoFrame rtcVideoFrame) {
//                        return null;
//                    }
//                });
            }
        }

        for (RCRTCRemoteUser remoteUser : remoteUsers) {
            for (RCRTCInputStream stream : remoteUser.getStreams()) {
                if (stream.getMediaType() == RCRTCMediaType.VIDEO){
                    inputStreams.add(stream);
//                    ((RCRTCVideoInputStream)stream).setVideoFrameListener(new IRCRTCVideoInputFrameListener() {
//                        @Override
//                        public void onFrame(RCRTCRemoteVideoFrame videoFrame) {
//                            setVideoFrame(videoFrame);
//
//                        }
//                    });
                }
                streams.add(stream);
            }
        }
       // remote_video_view.addView(rendererView1);
        LogUtils.e("onRenderFrame"+"222");
      //  updataUI(outputStreams,inputStreams);
        if (streams.size()==0){
           ToastUtils.showLong("房间内没有用户");
            return;
        }
        mRoom.getLocalUser().subscribeStreams(streams, new IRCRTCResultCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showLong("订阅远端成功");
            }

            @Override
            public void onFailed(RTCErrorCode errorCode) {
                ToastUtils.showLong("订阅失败");
            }
        });
    }

    private void setVideoFrame(RCRTCRemoteVideoFrame videoFrame){
        RCRTCRemoteVideoFrame.RTCBufferI420 buffer = (RCRTCRemoteVideoFrame.RTCBufferI420) videoFrame.getBuffer();
        LogUtils.e("videoFrame",buffer.getDataU());
        drawYuv(buffer.getDataY(),buffer.getDataU(),buffer.getDataV(),buffer.getWidth(),buffer.getHeight());

    }

    private void updataUI(List<RCRTCOutputStream> outputStreams ,List<RCRTCInputStream> inputStreams){
        List<RCRTCVideoView> videoViews = new ArrayList<>();
        for (RCRTCOutputStream outputStream : outputStreams) {
            RCRTCVideoView videoView = new RCRTCVideoView(this);
            ((RCRTCCameraOutputStream)outputStream).setVideoView(videoView);
            videoViews.add(videoView);
            videoView.setRendererEventsListener(new RCRTCRendererEventsListener() {
                @Override
                public void onFirstFrame() {

                }
            });
        }
        for (RCRTCInputStream inputStream : inputStreams) {
            RCRTCVideoView rcrtcVideoView = new RCRTCVideoView(this);
            ((RCRTCVideoStream)inputStream).setVideoView(rcrtcVideoView);
            videoViews.add(rcrtcVideoView);
        }
        if (videoViewManager!=null){
            videoViewManager.update((ArrayList<RCRTCVideoView>) videoViews);
        }

    }


    private void audiencesubscribe() {
        List<RCRTCInputStream>  streams = mRoom.getLiveStreams();
        RCRTCVideoView rcrtcVideoView = new RCRTCVideoView(this);
        for (RCRTCInputStream stream : streams) {
            if (stream.getMediaType() == RCRTCMediaType.VIDEO){
                ((RCRTCVideoInputStream)stream).setVideoView(rcrtcVideoView);
                break;
            }
        }
        mRoom.getLocalUser().subscribeStreams(streams, new IRCRTCResultCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showLong("订阅远端成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       video_loca.addView(rcrtcVideoView);
                    }
                });
            }

            @Override
            public void onFailed(RTCErrorCode errorCode) {

            }
        });
    }


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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_music:
                RCRTCEngine.getInstance().enableSpeaker(true);
                startMix();
                break;
            case R.id.get_long:
                ToastUtils.showLong("文件的总时长" + RCRTCAudioMixer.getInstance().getDurationMillis());
                break;
            case R.id.select:
                selectRole();
                break;
            case R.id.btn_more:
                if (mAllMoudle.getVisibility()==View.VISIBLE){
                    mAllMoudle.setVisibility(View.INVISIBLE);
                }else {
                    mAllMoudle.setVisibility(View.VISIBLE);
                }
                break;


        }
    }

    private void selectRole(){
//        ProgressDialog progressDialog = new ProgressDialog(ChatLiveActivity.this);
//        progressDialog.setTitle("");
//        progressDialog.setMessage("加载中");
//        progressDialog.setCancelable(false);
//        progressDialog.show();


         int mode = 0;
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("请选择音频模式");
        al.setCancelable(true);
        if (RCRTCEngine.getInstance().getDefaultAudioStream().getAudioScenario() == RCRTCParamsType.AudioScenario.MUSIC_CHATROOM){
            mode = 0;
        }else {
            mode = 1;
        }
        al.setSingleChoiceItems(new String[]{"音乐模式","开黑模式"}, mode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    ToastUtils.showShort("音乐");
                    RCRTCEngine.getInstance().getDefaultAudioStream().setAudioQuality(RCRTCParamsType.AudioQuality.MUSIC_HIGH, RCRTCParamsType.AudioScenario.MUSIC_CHATROOM);
                    RCRTCEngine.getInstance().getDefaultAudioStream().adjustRecordingVolume(100);
                }else {
                    ToastUtils.showShort("开黑");
                   // RCRTCEngine.getInstance().getDefaultAudioStream().setAudioQuality(RCRTCParamsType.AudioQuality.SPEECH, RCRTCParamsType.AudioScenario.GAMING_CHATROOM);
                    RCRTCEngine.getInstance().getDefaultAudioStream().adjustRecordingVolume(100);
                }
                dialog.dismiss();
            }
        });
        al.show();
    }



    private boolean isBrodcaster(){
        if (mRole==1){
            return true;
        }
        return false;
    }

   // {"ptid":"19689-2959","type":"audiorecord","status":"switchAudioRecord","sampleRate":48000,"channels":2,"audioSource":1}

  //  {"ptid":"19689-2959","type":"audiorecord","status":"switchGamingRecord","sampleRate":16000,"channels":1,"audioSource":7}

    IRCRTCStatusReportListener ircrtcStatusReportListener  = new IRCRTCStatusReportListener() {

        @Override
        public void onAudioInputLevel(String audioLevel) {
            super.onAudioInputLevel(audioLevel);
       //     LogUtils.e(TAG,"本端的音量为---"+audioLevel);
        }

        @Override
        public void onConnectionStats(StatusReport statusReport) {
            super.onConnectionStats(statusReport);
        }

        @Override
        public void reportLiveAudioStates(List<RCRTCLiveAudioState> audioStates) {
            super.reportLiveAudioStates(audioStates);
           /// Log.e(TAG,"当前音量为---"+audioStates.get(0).audioLevel);
        }
    };

    String url = "https://ks3-cn-beijing.ksyuncs.com/cloud-coach/1662703409965上低音号教材1-1-1伴奏.mp3";

     //String pathurl  = file1.getAbsolutePath();

    private void startMix() {
//        if (RCRTCEngine.getInstance().getDefaultAudioStream().getAudioScenario() == RCRTCParamsType.AudioScenario.GAMING_CHATROOM){
//            RCRTCEngine.getInstance().getDefaultAudioStream().setAudioQuality(RCRTCParamsType.AudioQuality.MUSIC_HIGH, RCRTCParamsType.AudioScenario.MUSIC_CHATROOM);
//        }
        RCRTCAudioMixer.getInstance().startMix(url, RCRTCAudioMixer.Mode.MIX, true, -1);
    }

class  TestHandle extends Handler{
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
    }
}


}