package cn.rong.wechat.anull;

public class App extends android.app.Application {

    private static App INSTANCE;

    @Override
    public void onCreate() {
        INSTANCE = this;
        super.onCreate();
    }

    public static App getApplication() {
        return INSTANCE;
    }
}
