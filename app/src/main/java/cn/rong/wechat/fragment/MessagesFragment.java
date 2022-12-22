package cn.rong.wechat.fragment;

import android.widget.TextView;

import cn.rong.wechat.R;

public class MessagesFragment extends  BaseFragment{
    private TextView mButton;
    private String nameString;

    public MessagesFragment(){
    }

    public MessagesFragment(String name){
        this.nameString = name;
    }


    private static final String TAG = "MessagesFragment";

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_messages;
    }

    @Override
    protected void initView() {
        mButton = findViewById(R.id.test_button);
    }


    @Override
    protected void initData() {
        mButton.setText(nameString);
    }
}
