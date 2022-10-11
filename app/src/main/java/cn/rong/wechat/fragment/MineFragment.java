package cn.rong.wechat.fragment;

import android.widget.TextView;

import cn.rong.wechat.R;

public class MineFragment extends BaseFragment{
    private TextView name;
    private String nameString;
    private static final String TAG = "ContactsFragment";
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
