package cn.rong.wechat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.rong.wechat.R;
import cn.rong.wechat.proxy.LiveBrodcaster;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.RCRTCRoomConfig;
import cn.rongcloud.rtc.base.RCRTCLiveRole;
import io.rong.calllib.Role;

/**
 * Created by Kevin on 2016/11/20.
 * Blog:http://blog.csdn.net/student9128
 * Describe：the Next Activity
 */

public class NextActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, NextActivity.class);
        context.startActivity(starter);
    }
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Button test;
    private static final String TAG = "NextActivity";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        test = findViewById(R.id.test);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("点击了");
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                }else {
                    LogUtils.e("关闭着");
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }

            }
        });
        
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Log.d(TAG, "onDrawerSlide: ");

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                mDrawerLayout.setClickable(true);
                Log.d(TAG, "onDrawerOpened: ");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                Log.d(TAG, "onDrawerClosed: ");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d(TAG, "onDrawerStateChanged: ");

            }
        });



    }
}
