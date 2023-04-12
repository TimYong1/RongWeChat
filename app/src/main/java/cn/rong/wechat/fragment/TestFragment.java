package cn.rong.wechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.rong.wechat.ChatApp;
import cn.rong.wechat.R;
import cn.rong.wechat.proxy.CallProxy;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class TestFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TestFragment";
    private View view;
    private TextInputEditText mInviteUserid;
    private MaterialButton mStartButton,mAssept,mHungup,button_sperk;
    private FrameLayout mLocaVideo,mRemoteVideo;
    private RongCallSession rongCallSession;
    private CallProxy callProxy;
    private RadioGroup radioGroup;
    private Button button1,button2,button3;

    public static TestFragment newInstance() {

        Bundle args = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       RongCallClient.setReceivedCallListener(new RongReceivedCall(this));
    }

    private void initView(View view){
        mInviteUserid = view.findViewById(R.id.invite_userid);
        mStartButton = view.findViewById(R.id.button_start);
        mLocaVideo = view.findViewById(R.id.video_loca);
        mRemoteVideo = view.findViewById(R.id.video_remote);
        mAssept = view.findViewById(R.id.button_accept);
        mHungup = view.findViewById(R.id.button_hungup);
        button_sperk = view.findViewById(R.id.button_sperk);
        mStartButton.setOnClickListener(this);
        mAssept.setOnClickListener(this);
        mHungup.setOnClickListener(this);
        callProxy = new CallProxy(getActivity(),mLocaVideo,mRemoteVideo,mAssept);
        RongCallClient.getInstance().setVoIPCallListener(callProxy);
        button1 = view.findViewById(R.id.radio_group1);
        button2 = view.findViewById(R.id.radio_group2);
        button3 = view.findViewById(R.id.radio_group3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button_sperk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id. button_start:
                onStartCall(RongCallCommon.CallMediaType.VIDEO);
                break;
            case R.id.button_accept:
                accept();
                break;
            case R.id.button_hungup:
                hungUp();
                break;
            case R.id.radio_group1:
               getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
                break;
            case R.id.radio_group2:
                getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

                break;
            case R.id.radio_group3:
                AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamMute(AudioManager.ADJUST_MUTE,true);
               // getActivity().setVolumeControlStream(AudioManager.STREAM_ALARM);
               // getActivity().setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

                break;
            case R.id.button_sperk:
              //  RongCallClient.getInstance().setEnableSpeakerphone(true);
                onStartCall(RongCallCommon.CallMediaType.AUDIO);
                break;

        }
    }



    public static class RongReceivedCall implements IRongReceivedCallListener{
        WeakReference<TestFragment> weakReference ;

        RongReceivedCall(TestFragment fragment){
            weakReference = new WeakReference(fragment);
        }

        @Override
        public void onReceivedCall(RongCallSession callSession) {
//            RCRTCAudioRouteManager.getInstance().init(weakReference.get().getActivity());
            weakReference.get().rongCallSession = callSession;
            weakReference.get().mAssept.setText("有人呼叫你，快接听吧");
        }

        @Override
        public void onCheckPermission(RongCallSession callSession) {

        }
    }

    /**
     * 拨打电话
     */
    private void onStartCall(RongCallCommon.CallMediaType callMediaType){
        LogUtils.e("连接状态"+RongIMClient.getInstance().getCurrentConnectionStatus());
        // 被叫用户 Id
        String mInviteUserid1 = mInviteUserid.getText().toString();
        if (TextUtils.isEmpty(mInviteUserid1)){
            ToastUtils.showShort("userid不能为空");
            return;
        }
        List<String> userIds = new ArrayList<>();
        userIds.add(mInviteUserid1);
        RongCallClient.getInstance().startCall(Conversation.ConversationType.PRIVATE, mInviteUserid1,
                userIds, null, callMediaType, null);
    }


    /**
     * 接听
     */
    private void accept(){
        if (RongCallClient.getInstance()!=null){
            RongCallClient.getInstance().acceptCall(RongCallClient.getInstance().getCallSession().getCallId());
        }
    }

    /**
     * 挂断电话
     */
    private void hungUp(){
        if (RongCallClient.getInstance()!=null){
            RongCallClient.getInstance().hangUpCall();
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test,container,false);
        initView(view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (RongCallClient.getInstance()!=null){
            RongCallClient.getInstance().hangUpCall();
        }
       // ((ChatApp)getActivity().getApplication()).setOnHandlerListener(null);
        callProxy.clean();
    }
}
