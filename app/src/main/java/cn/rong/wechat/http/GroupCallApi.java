package cn.rong.wechat.http;

import com.blankj.utilcode.util.ToastUtils;

public class GroupCallApi implements ICallApi{
    @Override
    public void startCall(String userid) {
        ToastUtils.showShort("GRoup");
    }

    @Override
    public void asseptCall() {

    }
}
