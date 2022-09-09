package cn.rong.wechat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.rongcloud.rtc.api.RCRTCConfig;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.base.RCRTCParamsType;
import io.rong.imlib.RongIMClient;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        viewPager2 = findViewById(R.id.main_viewpager);
        bottomNavigationView = findViewById(R.id.main_navigation);
        BadgeDrawable chatBadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_chat);
        BadgeDrawable findBadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_find);
        chatBadge.setNumber(30);
        findBadge.setBounds(1,1,1,1);
    }
}