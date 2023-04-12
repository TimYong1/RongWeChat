package cn.rong.wechat.fragment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import cn.rong.wechat.R;

public class MineFragment extends BaseFragment{
    private TextView name;
    private String nameString;
    private static final String TAG = "ContactsFragment";
    private AppCompatButton room_button;
    public MineFragment(){

    }

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public MineFragment(String name){
        this.nameString = name;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }
    @Override
    protected void initView() {
        name = findViewById(R.id.name);
    }

    @Override
    protected void initData() {
        name.setText(nameString);
    }




}
