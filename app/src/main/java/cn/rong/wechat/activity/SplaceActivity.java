package cn.rong.wechat.activity;


import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.MotionButton;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import cn.rong.wechat.MainActivity;
import cn.rong.wechat.R;
import cn.rong.wechat.http.TimUtils;
import cn.rong.wechat.yuv.RTCActivity;

public class SplaceActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SplaceActivity";

    private TextView text_view,text_view_test;
    private MaterialButton mLoginButton;
    private TextInputEditText mUseridEt;
    private TimUtils timUtils;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splace;
    }

    @Override
    protected void initView() {
        text_view = findViewById(R.id.text_view);
        text_view_test = findViewById(R.id.text_view_test);
        mLoginButton = findViewById(R.id.login);
        mUseridEt = findViewById(R.id.userid);
        mLoginButton.setOnClickListener(this);
    }



    @Override
    protected void initData() {
        String mode = Build.MODEL;
        String BRAND = Build.BRAND;
        String carrier= android.os.Build.MANUFACTURER;
        LogUtils.e(mode+"----"+BRAND+"----"+carrier);
    }


    private void connect(){
        Random random = new Random();
        String userid = Integer.toString(random.nextInt(10000));
        LogUtils.d(TAG,userid);
        String useridet = mUseridEt.getText().toString();
        if (TextUtils.isEmpty(useridet)){
            ToastUtils.showLong("请输入userid");
            return;
        }
        showLoading("登录中...");
        timUtils = new TimUtils(useridet,"");
        timUtils.getToken(new TimUtils.LoginStatu() {
            @Override
            public void loginSuccess() {
                stopLoading();
                ToastUtils.showLong("连接成功");
                MainActivity.start(SplaceActivity.this);
            //    RouteUtils.registerActivity(RouteUtils.RongActivityType.ConversationActivity, ConverstatinActivity.class);
                finish();
            }

            @Override
            public void loginFail() {
                stopLoading();
                ToastUtils.showLong("连接失败");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                connect();
                break;
        }
    }
//    AudioManager audioManage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//    audioManage.


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timUtils != null) {
            timUtils = null;
        }
    }
}