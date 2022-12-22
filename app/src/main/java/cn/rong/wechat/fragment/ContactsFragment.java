package cn.rong.wechat.fragment;

import android.util.Log;
import android.widget.TextView;

import cn.rong.wechat.R;

public class ContactsFragment extends BaseFragment{
    private TextView name;
    private String nameString;
    private static final String TAG = "ContactsFragment";
    public  ContactsFragment(){

    }
    public  ContactsFragment(String name){
        this.nameString = name;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_contacts;
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
