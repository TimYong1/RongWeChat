package cn.rong.wechat.http;


import android.webkit.WebSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import cn.rong.wechat.ChatApp;
import cn.rong.wechat.common.Config;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenServer {
   static final String TAG  = "TokenServer";

    public static void getToken(final String userId, final tokenCallback tokenCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {

                final int nonce = new Random().nextInt(100000);
                final long timestamp = System.currentTimeMillis();
                final String signature =  sha1(Config.App_secret + nonce + timestamp);
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId",userId)
                        .add("name","Tim")
                        .add("portraitUri","")
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Config.GET_TOKEN)
                        .addHeader("Timestamp", String.valueOf(timestamp))
                        .addHeader("Nonce", String.valueOf(nonce))
                        .addHeader("Signature", signature)
                        .addHeader("App-Key", Config.App_key)
                        .addHeader("User-Agent", WebSettings.getDefaultUserAgent(ChatApp.getApplication()))
                        .post(requestBody)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.code()==200){
                        JSONObject jsonObject = new JSONObject(response.body().string());
                      String  token = jsonObject.optString("token");
                        tokenCallback.success(token);
                    }else {
                        tokenCallback.error();
                    }
                 //   Log.e(TAG,response.body().string());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    tokenCallback.error();
                }
            }
        }).start();
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfByte = (b >>> 4) & 0x0F;
            int twoHalfs = 0;
            do {
                buf.append(halfByte <= 9 ? (char) ('0' + halfByte) : (char) ('a' + halfByte - 10));
                halfByte = b & 0x0F;
            } while (twoHalfs++ < 1);
        }
        return buf.toString();
    }

    private static String sha1(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    public interface tokenCallback{
        void success(String token);
        void error();
    }
}
