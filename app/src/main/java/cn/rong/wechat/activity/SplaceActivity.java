package cn.rong.wechat.activity;


import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;
import java.util.Random;

import cn.rong.wechat.MainActivity;
import cn.rong.wechat.R;
import cn.rong.wechat.http.TimUtils;
import cn.rongcloud.fubeautifier.RCRTCFUBeautifierEngine;
import io.rong.imlib.IRongCoreCallback;
import io.rong.imlib.IRongCoreEnum;
import io.rong.imlib.RongCoreClient;
import io.rong.imlib.model.TagInfo;

public class SplaceActivity extends BaseActivity {

    private static final String TAG = "SplaceActivity";

    private TextView text_view,text_view_test;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splace;
    }

    @Override
    protected void initView() {
        //RCRTCFu
        text_view = findViewById(R.id.text_view);
        text_view_test = findViewById(R.id.text_view_test);
    }



    @Override
    protected void initData() {
        connect();
    }


    private void connect(){
        //String token = "0j/jG9C/8eWKFAnRb3umcQF/99m7BQCC3Nj0aHjxvwVC792IIG+ztKuXzWAZr2VMAtFr2TbycSg7XHAeoc2jEA==@9ugq.cn.rongnav.com;9ugq.cn.rongcfg.com";
        Random random = new Random();
        String userid = Integer.toString(random.nextInt(10000));
        LogUtils.d(TAG,userid);
        TimUtils.getToken(userid,"", new TimUtils.loginStatu() {
            @Override
            public void loginSuccess() {
                ToastUtils.showLong("连接成功");
                MainActivity.start(SplaceActivity.this);
            }

            @Override
            public void loginFail() {
                ToastUtils.showLong("连接失败");
            }
        });
    }
}