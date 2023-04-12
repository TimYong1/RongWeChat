package cn.rong.wechat.proxy;

import android.os.Handler;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import cn.rongcloud.rtc.api.RCRTCRemoteUser;
import cn.rongcloud.rtc.api.callback.IRCRTCRoomEventsListener;
import cn.rongcloud.rtc.api.stream.RCRTCInputStream;
import cn.rongcloud.rtc.base.RCRTCLiveRole;

public class LiveBrodcaster extends IRCRTCRoomEventsListener {

    private static final String TAG = "LiveBrodcaster";
    public LiveBrodcaster(Handler handler){

    }


    @Override
    public void onRemoteUserPublishResource(RCRTCRemoteUser remoteUser, List<RCRTCInputStream> streams) {
        LogUtils.e(TAG,remoteUser.getUserId());
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
        ToastUtils.showShort("主播加入"+remoteUser.getUserId());
    }

    @Override
    public void onUserLeft(RCRTCRemoteUser remoteUser) {
        LogUtils.e("主播离开了",remoteUser.getUserId());
        ToastUtils.showShort("主播离开了"+remoteUser.getUserId());
    }

    @Override
    public void onUserOffline(RCRTCRemoteUser remoteUser) {
       LogUtils.e("主播离线了",remoteUser.getUserId());
        ToastUtils.showShort("主播离线了"+remoteUser.getUserId());
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

    @Override
    public void onCancelRequestOtherRoom(String inviterRoomId, String inviterUserId, String extra) {
        super.onCancelRequestOtherRoom(inviterRoomId, inviterUserId, extra);
        LogUtils.e("OtherRoom","onCancelRequestOtherRoom");
    }

    @Override
    public void onRequestJoinOtherRoom(String inviterRoomId, String inviterUserId, String extra) {
        super.onRequestJoinOtherRoom(inviterRoomId, inviterUserId, extra);
        LogUtils.e("OtherRoom","onRequestJoinOtherRoom");
    }

    @Override
    public void onResponseJoinOtherRoom(String inviterRoomId, String inviterUserId, String inviteeRoomId, String inviteeUserId, boolean agree, String extra) {
        super.onResponseJoinOtherRoom(inviterRoomId, inviterUserId, inviteeRoomId, inviteeUserId, agree, extra);
        LogUtils.e("OtherRoom","onResponseJoinOtherRoom");
    }

    @Override
    public void onFinishOtherRoom(String roomId, String userId) {
        super.onFinishOtherRoom(roomId, userId);
        LogUtils.e("OtherRoom","onFinishOtherRoom");
    }

//    @Override
//    public void onSwitchRole(String userId, RCRTCLiveRole role) {
//        super.onSwitchRole(userId, role);
//        LogUtils.e(TAG,"onSwitchRole"+userId);
//    }
}
