package cn.rong.wechat.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentStateAdapter {

   List<Fragment> fragments = new ArrayList<>();
    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity,List<Fragment> fragmentList) {
        super(fragmentActivity);
        this.fragments = fragmentList;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
