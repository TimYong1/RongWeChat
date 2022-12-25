package cn.rong.wechat.anull;

import android.text.TextUtils;
import android.widget.Toast;

/**
 * Toast 工具类
 *
 * @author yanke
 */
public class ToastUtil {

    public static void showToast(int resourceId) {
        showToast(resourceId, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resourceId, int duration) {
        showToast(App.getApplication().getResources().getString(resourceId), duration);
    }

    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public static void showToast(String message, int duration) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Toast.makeText(App.getApplication(), message, duration).show();
    }
}
