package cn.rong.wechat;

import static android.media.AudioFormat.ENCODING_PCM_8BIT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import cn.rong.wechat.adapter.MainPagerAdapter;
import cn.rong.wechat.fragment.ContactsFragment;
import cn.rong.wechat.fragment.FindFragment;
import cn.rong.wechat.fragment.MessagesFragment;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class MainActivity extends FragmentActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    MainPagerAdapter mainPagerAdapter;
    List<Fragment> fragments = new ArrayList<>();
    private static final String TAG = "MainActivity";
    public static void start(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    //    Listener for handling reselection events on navigation items.
      //  isItemActiveIndicatorEnabled()


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //sendMessage();
    }

    /**
     * 默认给9992发条消息，方便测试
     */
    private void sendMessage(){
        String targetid = "9992";
        Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
        TextMessage textMessage = new TextMessage("哈哈");
        Message message = Message.obtain(targetid,conversationType,textMessage);
        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                ToastUtils.showLong("发送成功");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void initView() {
        viewPager2 = findViewById(R.id.main_viewpager);
        bottomNavigationView = findViewById(R.id.main_navigation);
       // bottomNavigationView.setItemActiveIndicatorEnabled(false);
       // bottomNavigationView.isItemHorizontalTranslationEnabled();
        BadgeDrawable chatBadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_chat);
        BadgeDrawable findBadge = bottomNavigationView.getOrCreateBadge(R.id.navigation_find);
        chatBadge.setNumber(30);
        findBadge.setBounds(1, 1, 1, 1);
        fragments.add(new FindFragment("发现"));
        fragments.add(new ContactsFragment());
        fragments.add(new ContactsFragment("通讯录"));
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