package cn.rong.wechat.proxy;

import android.os.Handler;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import cn.rongcloud.rtc.api.RCRTCRemoteUser;
import cn.rongcloud.rtc.api.callback.IRCRTCRoomEventsListener;
import cn.rongcloud.rtc.api.stream.RCRTCInputStream;

public class LiveBrodcaster extends IRCRTCRoomEventsListener {

    public LiveBrodcaster(Handler handler){

    }


    @Override
    public void onRemoteUserPublishResource(RCRTCRemoteUser remoteUser, List<RCRTCInputStream> streams) {

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
}
