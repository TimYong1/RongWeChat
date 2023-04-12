package cn.rong.wechat.jectpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import cn.rong.wechat.R;

public class RoomActivity extends AppCompatActivity {

    private RecyclerView userList;
    private UserListAdapter mAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        userList = findViewById(R.id.user_recycler);
        UserInfoDao userInfoDao = UserDataBase.getDataBaseIntentce(this).getUserinfoDao();
        Userinfo userinfo = new Userinfo(1,"hah","18");
        Userinfo userinfo1 = new Userinfo(1,"hah","18");
        Userinfo userinfo2 = new Userinfo(1,"hah","18");
        userInfoDao.insertUserInfo(userinfo,userinfo1,userinfo2);
        userList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserListAdapter(userInfoDao.queryUserList());
        userList.setAdapter(mAdapter);
    }


}