package cn.rong.wechat.moudle;

import java.io.Serializable;

public class MoudlesDataBean implements Serializable {
    private String moudleName;
    private int postion;
    public MoudlesDataBean(){

    }

    public MoudlesDataBean(String moudleName,int postion){
        this.moudleName = moudleName;
        this.postion = postion;
    }

    public String getMoudleName() {
        return moudleName;
    }

    public void setMoudleName(String moudleName) {
        this.moudleName = moudleName;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }
}
