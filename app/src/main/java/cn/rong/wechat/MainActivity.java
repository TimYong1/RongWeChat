package cn.rong.wechat;

import static android.media.AudioFormat.ENCODING_PCM_8BIT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import cn.rong.wechat.adapter.MainPagerAdapter;
import cn.rong.wechat.fragment.ContactsFragment;
import cn.rong.wechat.fragment.FindFragment;
import cn.rong.wechat.fragment.MessagesFragment;
import cn.rongcloud.rtc.api.RCRTCConfig;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.base.RCRTCParamsType;
import cn.rongcloud.rtc.media.player.api.RCRTCMediaPlayer;
import io.rong.imlib.RongIMClient;

public class MainActivity extends FragmentActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    MainPagerAdapter mainPagerAdapter;
    List<Fragment> fragments = new ArrayList<>();
    private static final String TAG = "MainActivity";
    RCRTCMediaPlayer rcrtcMediaPlayer;
    public static void start(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        rcrtcMediaPlayer = new RCRTCMediaPlayer();
        rcrtcMediaPlayer.open("");
    }

    private void initView() {
        viewPager2 = findViewById(R.id.main_viewpager);
        bottomNavigationView = findViewById(R.id.main_navigation);
        BadgeDrawable chatBadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_chat);
        BadgeDrawable findBadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_find);
        chatBadge.setNumber(30);
        findBadge.setBounds(1, 1, 1, 1);
        fragments.add(new MessagesFragment("消息"));
        fragments.add(new ContactsFragment("通讯录"));
        fragments.add(new FindFragment("发现"));
        fragments.add(new MessagesFragment("我的"));
        mainPagerAdapter = new MainPagerAdapter(this, fragments);
        viewPager2.setAdapter(mainPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_chat);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_contacts);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_find);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_mine);
                        break;
                }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chat:
                        viewPager2.setCurrentItem(0, false);
                        break;
                    case R.id.navigation_contacts:
                        viewPager2.setCurrentItem(1, false);
                        break;
                    case R.id.navigation_find:
                        viewPager2.setCurrentItem(2, false);
                        break;
                    case R.id.navigation_mine:
                        viewPager2.setCurrentItem(3, false);
                        break;
                }
                return true;
            }
        });
    }

}