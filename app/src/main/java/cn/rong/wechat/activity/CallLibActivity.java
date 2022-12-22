package cn.rong.wechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import cn.rong.wechat.R;
import cn.rong.wechat.fragment.TestFragment;

public class CallLibActivity extends AppCompatActivity {

    public static void start(Context context){
        context.startActivity(new Intent(context,CallLibActivity.class));
    }

    private FrameLayout fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        initView();

    }

    private void initView() {
        fragment = findViewById(R.id.fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,new TestFragment());
        fragmentTransaction.commit();
    }
}