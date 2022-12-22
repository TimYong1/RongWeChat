package cn.rong.wechat.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
import io.rong.imlib.model.Conversation;

public class TestFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextInputEditText mInviteUserid;
    private MaterialButton mStartButton,mAssept,mHungup;
    private FrameLayout mLocaVideo,mRemoteVideo;
    private RongCallSession rongCallSession;
    private CallProxy callProxy;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       RongCallClient.setReceivedCallListener(new RongReceivedCall(this));
//        ((ChatApp)getActivity().getApplication()).setOnHandlerListener(new ChatApp.HandlerListener() {
//            @Override
//            public void heandleMessage(Message msg) {
//                mAssept.setText("有人呼叫你，快接听吧");
//            }
//        });
    }

    private void initView(View view){
        mInviteUserid = view.findViewById(R.id.invite_userid);
        mStartButton = view.findViewById(R.id.button_start);
        mLocaVideo = view.findViewById(R.id.video_loca);
        mRemoteVideo = view.findViewById(R.id.video_remote);
        mAssept = view.findViewById(R.id.button_accept);
        mHungup = view.findViewById(R.id.button_hungup);
        mStartButton.setOnClickListener(this);
        mAssept.setOnClickListener(this);
        mHungup.setOnClickListener(this);
        callProxy = new CallProxy(getActivity(),mLocaVideo,mRemoteVideo,mAssept);
        RongCallClient.getInstance().setVoIPCallListener(callProxy);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id. button_start:
                onStartCall();
                break;
            case R.id.button_accept:
                accept();
                break;
            case R.id.button_hungup:
                hungUp();
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
    private void onStartCall(){
        // 被叫用户 Id
        String targetId = mInviteUserid.getText().toString();
        if (TextUtils.isEmpty(targetId)){
            ToastUtils.showShort("请输入要邀请的人userid");
            return;
        }
        List<String> userIds = new ArrayList<>();
        userIds.add(targetId);
        RongCallClient.getInstance().startCall(Conversation.ConversationType.PRIVATE, targetId,
                userIds, null, RongCallCommon.CallMediaType.VIDEO, null);
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
