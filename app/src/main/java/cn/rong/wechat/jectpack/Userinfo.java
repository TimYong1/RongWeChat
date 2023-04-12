package cn.rong.wechat.jectpack;


import androidx.annotation.InspectableProperty;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class Userinfo {

    @PrimaryKey(autoGenerate = true) // 主键自增长
    @ColumnInfo(name = "id" ,typeAffinity = ColumnInfo.INTEGER)
    private int id;

    @ColumnInfo(name = "userName",typeAffinity = ColumnInfo.TEXT)
    private String userName;

    @ColumnInfo(name = "userAge",typeAffinity = ColumnInfo.TEXT)
    private String userAge;

    public Userinfo(int id, String userName, String userAge) {
        this.id = id;
        this.userName = userName;
        this.userAge = userAge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userAge='" + userAge + '\'' +
                '}';
    }
}
