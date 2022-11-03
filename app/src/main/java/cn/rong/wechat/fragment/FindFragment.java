package cn.rong.wechat.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.rong.wechat.R;
import cn.rong.wechat.activity.ChatLiveActivity;

public class FindFragment extends BaseFragment{
    private TextView name;
    private String nameString;
    private Button mLiveBtn;
    private static final String TAG = "ContactsFragment";
    public FindFragment(String name){
        this.nameString = name;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_find;
    }
    @Override
    protected void initView() {
        name = findViewById(R.id.name);
        mLiveBtn = findViewById(R.id.live);
        mLiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatLiveActivity.start(getActivity());
            }
        });
    }

    @Override
    protected void initData() {
       // name.setText(nameString);
    }





}
