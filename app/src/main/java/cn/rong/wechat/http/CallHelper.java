package cn.rong.wechat.http;

public class CallHelper implements ICallApi{


    private static CallHelper instance;
    public static CallHelper obtain() {
        synchronized (CallHelper.class) {
            if (instance == null) {
                instance = new CallHelper();
            }
        }
        return instance;
    }
    private CallHelper() {
    }
    private static ICallApi mCallApi;
    public static void init(ICallApi callApi) {
        mCallApi = callApi;
    }

    @Override
    public void startCall(String userid) {
        mCallApi.startCall(userid);
    }

    @Override
    public void asseptCall() {
     mCallApi.asseptCall();
    }
}
