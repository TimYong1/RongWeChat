package cn.rong.wechat.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;


public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setLayoutId()>0){
            setContentView(setLayoutId());
        }
        initView();
        initData();
    }

    protected abstract int setLayoutId();

    protected abstract void initView();

    protected abstract void initData();


    protected void showLoading(String content){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage(content);
        progressDialog.show();
    }

    protected void stopLoading(){
        if (progressDialog==null){
            LogUtils.e("需要先showloading，才能stop");
            return;
        }
        progressDialog.dismiss();
    }

}
