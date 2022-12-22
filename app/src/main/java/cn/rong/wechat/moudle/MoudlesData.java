package cn.rong.wechat.moudle;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class MoudlesData{

    public MoudlesData(){

    }

    public List<MoudlesDataBean> getMoudlesData(){
        List<MoudlesDataBean> moudles = new ArrayList<>();
        moudles.add(new MoudlesDataBean("混音",0));
        moudles.add(new MoudlesDataBean("同房间连麦",1));
        moudles.add(new MoudlesDataBean("跨房间连麦",2));
        moudles.add(new MoudlesDataBean("CDN",3));
        moudles.add(new MoudlesDataBean("播放器",4));
        moudles.add(new MoudlesDataBean("悬浮通知栏",5));
        return moudles;

    }
}
