package cn.rong.wechat.jectpack;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface UserInfoDao {

    @Insert
    public void insertUserInfo(Userinfo... userinfos);

    @Delete
    public void deleteUserInfo(Userinfo... userinfos);

    @Update
    public void updateUserInfo(Userinfo... userinfos);

//    @Query("select * from user_table where id=:id")
//    public void queryUserInfoById(int id);

    @Query("select * from user_table")
    public List<Userinfo> queryUserList();
}
