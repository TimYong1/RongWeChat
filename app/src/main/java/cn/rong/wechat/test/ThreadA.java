package cn.rong.wechat.test;

import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class ThreadA extends Thread{
    private static final String TAG = "ThreadA";
    int test = 0;
    CountDownLatch countDownLatch;
    public ThreadA(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
        super.run();
        countDownLatch.countDown();
        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            test++;
            Log.e(TAG,"现在是---》"+test);
        }
    }
}
