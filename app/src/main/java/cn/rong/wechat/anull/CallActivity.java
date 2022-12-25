package cn.rong.wechat.anull;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.rongcloud.rtc.center.RCRoomImpl;
import io.rong.imlib.RongIMClient;

public class CallActivity extends Base {


    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_call);
//        editText = findViewById(R.id.editUserId);
//        TextView tvUserId = findViewById(R.id.tvUserId);
//        tvUserId.setText(RongIMClient.getInstance().getCurrentUserId());
//        RongCallKit.setMainPageActivityClass(new String[] {MainActivity.class.getName()});
    }

    public void callClick(View view) {
//        int id = view.getId();
//        if (id== R.id.btnStartCall) {
////            RongCallKit.startSingleCall(CallActivity.this, editText.getText().toString().trim(), CallMediaType.CALL_MEDIA_TYPE_VIDEO);
////            RongCallKit.startMultiCall(CallActivity.this, ConversationType.GROUP, editText.getText().toString().trim(),
////                CallMediaType.CALL_MEDIA_TYPE_VIDEO, null);
//            showToast("发起成功");
//        } else if (id== R.id.btnHangup) {
//        } else if (view.getId() == R.id.btnFinish) {
//            this.finish();
//        }
    }
}