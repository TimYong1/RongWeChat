package cn.rong.wechat.http;

import com.blankj.utilcode.util.ToastUtils;

public class RongCallApi implements ICallApi{
    @Override
    public void startCall(String userid) {
        ToastUtils.showShort(userid);
    }

    @Override
    public void asseptCall() {

    }
}
