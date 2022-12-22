package cn.rong.wechat.proxy;

import android.content.Context;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

import io.rong.calllib.IRongCallListener;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;

public class CallProxy implements IRongCallListener {
    private FrameLayout mLocaVideo,mRemoteVideo;
    private FragmentActivity mContext;
    MaterialButton mAcceptButton;
    public CallProxy(){

    }

    public CallProxy(FragmentActivity context, FrameLayout locaVideo, FrameLayout remoteVideo, MaterialButton accept){
          this.mLocaVideo = locaVideo;
          this.mRemoteVideo = remoteVideo;
          this.mContext = context;
          this.mAcceptButton = accept;
    }


    @Override
    public void onCallIncoming(RongCallSession callSession, SurfaceView localVideo) {

    }

    @Override
    public void onCallOutgoing(RongCallSession callSession, SurfaceView localVideo) {
        addlocaview(localVideo);
        //callSession.getSessionId();
    }

    @Override
    public void onCallConnected(RongCallSession callSession, SurfaceView localVideo) {
         addlocaview(localVideo);
    }

    @Override
    public void onCallDisconnected(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
       removeViews();
       updataUi();
      ///  mContext.finish();
    }

    @Override
    public void onRemoteUserRinging(String userId) {

    }

    @Override
    public void onRemoteUserAccept(String userId, RongCallCommon.CallMediaType mediaType) {

    }

    @Override
    public void onRemoteUserJoined(String userId, RongCallCommon.CallMediaType mediaType, int userType, SurfaceView remoteVideo) {
       addRemoteView(remoteVideo);
    }

    @Override
    public void onRemoteUserInvited(String userId, RongCallCommon.CallMediaType mediaType) {

    }

    @Override
    public void onRemoteUserLeft(String userId, RongCallCommon.CallDisconnectedReason reason) {
        updataUi();

    }

    @Override
    public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {

    }

    @Override
    public void onError(RongCallCommon.CallErrorCode errorCode) {
        updataUi();

    }

    @Override
    public void onRemoteCameraDisabled(String userId, boolean disabled) {

    }

    @Override
    public void onRemoteMicrophoneDisabled(String userId, boolean disabled) {

    }

    @Override
    public void onNetworkReceiveLost(String userId, int lossRate) {

    }

    @Override
    public void onNetworkSendLost(int lossRate, int delay) {

    }

    @Override
    public void onFirstRemoteVideoFrame(String userId, int height, int width) {

    }

    @Override
    public void onFirstRemoteAudioFrame(String userId) {

    }


    @Override
    public void onAudioLevelSend(String audioLevel) {

    }

    @Override
    public void onAudioLevelReceive(HashMap<String, String> audioLevel) {

    }

    @Override
    public void onRemoteUserPublishVideoStream(String userId, String streamId, String tag, SurfaceView surfaceView) {

    }

    @Override
    public void onRemoteUserUnpublishVideoStream(String userId, String streamId, String tag) {

    }

    /**
     * 引用对象置空
     */

    public void clean(){
        mContext = null;
        mLocaVideo = null;
        mRemoteVideo = null;
        mAcceptButton = null;
    }

    /**
     * 添加本地视图进行渲染
     * @param localVideo
     */

    private void addlocaview(SurfaceView localVideo){
        if (localVideo!=null && mLocaVideo!=null){
            mLocaVideo.removeAllViews();
            mLocaVideo.addView(localVideo);
        }
    }

    /**
     * 添加远端的视图进行渲染
     * @param remoteVideo
     */
    private void addRemoteView(SurfaceView remoteVideo){
        if (remoteVideo!=null && mRemoteVideo!=null){
            mRemoteVideo.removeAllViews();
            mRemoteVideo.addView(remoteVideo);
        }
    }

    /**
     * 通话结束移除所有子view
     */
    private void removeViews(){
        if (mLocaVideo!=null &&mRemoteVideo!=null){
            mLocaVideo.removeAllViews();
            mRemoteVideo.removeAllViews();
            ToastUtils.showLong("通话结束");
        }
    }

    /**
     * 根据不同状态更改UI
     */
    private void updataUi(){
        if (mAcceptButton!=null){
            mAcceptButton.setText("接听");
        }
    }
}
