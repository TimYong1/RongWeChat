package cn.rong.wechat.jectpack;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Userinfo.class},version = 1)
public abstract class UserDataBase extends RoomDatabase {

  private static final String DATABASE_NAME = "userinfo_db";
  private static UserDataBase dataBaseIntentce;

    public static synchronized UserDataBase getDataBaseIntentce(Context context) {
        if (dataBaseIntentce==null){
            dataBaseIntentce = Room.databaseBuilder(context,UserDataBase.class,DATABASE_NAME).build();
        }
        return dataBaseIntentce;
    }

    public abstract UserInfoDao getUserinfoDao();
}
