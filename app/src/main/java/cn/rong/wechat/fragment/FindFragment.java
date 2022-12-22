package cn.rong.wechat.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.rong.wechat.R;
import cn.rong.wechat.activity.CallLibActivity;
import cn.rong.wechat.activity.PreviewLiveActivity;
import cn.rong.wechat.adapter.MessageAdapter;
import io.rong.imlib.IRongCoreCallback;
import io.rong.imlib.IRongCoreEnum;
import io.rong.imlib.chatroom.base.RongChatRoomClient;
import io.rong.imlib.model.ChatRoomMemberAction;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class FindFragment extends BaseFragment implements View.OnClickListener {
    private TextView name;
    private String nameString;
    private Button mLiveBtn,test,quite_chatroom,call,chatroomSend;
    private static final String TAG = "FindFragment";
    private RecyclerView messages_list;
    private MessageAdapter messageAdapter;
    private List<Message> messages = new ArrayList<>();
    public FindFragment(){

    }
    public FindFragment(String name){
        this.nameString = name;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_find;
    }
    @Override
    protected void initView() {
        name = findViewById(R.id.name);
        mLiveBtn = findViewById(R.id.live);
        test = findViewById(R.id.test);
        call = findViewById(R.id.call);
        quite_chatroom = findViewById(R.id.quite_chatroom);
        chatroomSend = findViewById(R.id.chat_room_send);
        messages_list = findViewById(R.id.messages_list);
        quite_chatroom.setOnClickListener(this);
        mLiveBtn.setOnClickListener(this);
        test.setOnClickListener(this);
        call.setOnClickListener(this);
        chatroomSend.setOnClickListener(this);
        messageAdapter = new MessageAdapter(messages);
        messages_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        messages_list.setAdapter(messageAdapter);
//        RongIM.addOnReceiveMessageListener(new RongIMClient.OnReceiveMessageWrapperListener() {
//            @Override
//            public boolean onReceived(Message message, int left, boolean hasPackage, boolean offline) {
//                LogUtils.e(TAG,"OnReceiveMessageWrapperListener","收到了"+message.getSenderUserId()+"发送的消息"+"消息内容为");
//                //ToastUtils.showLong("接收到消息"+message.getContent());
//                messages.add(message);
//                messageAdapter.notifyDataSetChanged();
//                messages_list.scrollToPosition(messages.size());
//                return false;
//            }
//        });
//        RCRTCEngine.getInstance().registerEventListener(new IRCRTCEngineEventListener() {
//            @Override
//            public void onKicked(String roomId, RCRTCParamsType.RCRTCKickedReason kickedReason) {
//
//            }
//        });
    }

    private void sendMessage(String s){
        String targetid = "999";
        Conversation.ConversationType conversationType = Conversation.ConversationType.CHATROOM;
        TextMessage textMessage = new TextMessage(s);
        Message message = Message.obtain(targetid,conversationType,textMessage);
//        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
//            @Override
//            public void onAttached(Message message) {
//
//            }
//
//            @Override
//            public void onSuccess(Message message) {
//                messages.add(message);
//                messageAdapter.notifyDataSetChanged();
//                messages_list.scrollToPosition(messages.size()-1);
//            }
//
//            @Override
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });
    }
    private int index = 0;
    private boolean sendmessageFlag = true;
    private synchronized void looperSendMessage(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (sendmessageFlag){
                    index++;
                    sendMessage("哈哈"+index);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


    private void startCall(){
        PermissionUtils.permissionGroup(PermissionConstants.CAMERA,PermissionConstants.MICROPHONE).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                CallLibActivity.start(getActivity());
            }

            @Override
            public void onDenied() {

            }
        }).request();
    }




    /**
     * 聊天室操作监听
     */
    RongChatRoomClient.ChatRoomAdvancedActionListener chatRoomAdvancedActionListener = new RongChatRoomClient.ChatRoomAdvancedActionListener() {
        @Override
        public void onJoining(String chatRoomId) {
            LogUtils.e(TAG,"ChatRoomAdvancedActionListener","正在加入聊天室"+chatRoomId);
        }

        @Override
        public void onJoined(String chatRoomId) {
            LogUtils.e(TAG,"ChatRoomAdvancedActionListener","加入聊天室成功"+chatRoomId);
        }

        @Override
        public void onReset(String chatRoomId) {
            LogUtils.e(TAG,"ChatRoomAdvancedActionListener","聊天室信息重置"+chatRoomId);
        }

        @Override
        public void onQuited(String chatRoomId) {
            LogUtils.e(TAG,"ChatRoomAdvancedActionListener","退出聊天室"+chatRoomId);
        }

        @Override
        public void onDestroyed(String chatRoomId, IRongCoreEnum.ChatRoomDestroyType type) {
            LogUtils.e(TAG,"ChatRoomAdvancedActionListener","聊天室销毁"+chatRoomId);
            ToastUtils.showLong("聊天室销毁"+chatRoomId);
        }

        @Override
        public void onError(String chatRoomId, IRongCoreEnum.CoreErrorCode code) {
            LogUtils.e(TAG,"ChatRoomAdvancedActionListener","加入聊天室失败"+chatRoomId);
        }
    };

    /**
     * 聊天室成员变化监听
     */
    RongChatRoomClient.ChatRoomMemberActionListener chatRoomMemberActionListener = (chatRoomMemberActions, roomId) -> {
        for (ChatRoomMemberAction chatRoomMemberAction : chatRoomMemberActions) {
            LogUtils.e(TAG,"ChatRoomMemberActionListener",chatRoomMemberAction.getUserId()+"加入聊天室"+roomId);
        }
    };

    @Override
    protected void initData() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.live:
               //  ChatLiveActivity.start(getActivity());
                PreviewLiveActivity.start(getActivity());
                //   startMix();
               // sendMessage();
                break;
            case R.id.test:
                joinChatRoom();
                break;
            case R.id.quite_chatroom:
                RongChatRoomClient.getInstance().quitChatRoom("999", new IRongCoreCallback.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showLong("退出聊天室成功");
                    }

                    @Override
                    public void onError(IRongCoreEnum.CoreErrorCode coreErrorCode) {
                        ToastUtils.showLong("退出聊天室失败");
                    }
                });
                break;
            case R.id.call:
              startCall();
                break;
            case R.id.chat_room_send:
                looperSendMessage();
                break;
        }
    }

    /**
     * kv 变化监听
     */
    RongChatRoomClient.KVStatusListener kvStatusListener = new RongChatRoomClient.KVStatusListener() {
        @Override
        public void onChatRoomKVSync(String roomId) {

        }

        @Override
        public void onChatRoomKVUpdate(String roomId, Map<String, String> chatRoomKvMap) {

        }

        @Override
        public void onChatRoomKVRemove(String roomId, Map<String, String> chatRoomKvMap) {

        }
    };

    /**
     * 获取单个key的vaule
     * @param roomid
     * @param key
     */
    private void getVaule(String roomid,String key){
        RongChatRoomClient.getInstance().getChatRoomEntry(roomid, key, new IRongCoreCallback.ResultCallback<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> stringStringMap) {

            }

            @Override
            public void onError(IRongCoreEnum.CoreErrorCode e) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void joinChatRoom(){
        RongChatRoomClient.setChatRoomMemberListener(chatRoomMemberActionListener);
        RongChatRoomClient.getInstance().addKVStatusListener(kvStatusListener);
        RongChatRoomClient.setChatRoomAdvancedActionListener(chatRoomAdvancedActionListener);
        RongChatRoomClient.getInstance().joinChatRoom("999", -1, new IRongCoreCallback.OperationCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showLong("加入聊天室成功");
            }

            @Override
            public void onError(IRongCoreEnum.CoreErrorCode coreErrorCode) {
                ToastUtils.showLong("加入聊天室失败"+coreErrorCode);
            }
        });
    }
}
