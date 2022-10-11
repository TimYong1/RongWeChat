package cn.rong.wechat;

import android.util.Log;

public class TestThread extends Thread{
    public   int num = 0;
    private static final String TAG = "TestThread";
    @Override
    public void run() {
        super.run();
        while (!Thread.currentThread().isInterrupted()){
//            try {
//              //  Thread.sleep(1000);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            num++;
            setNum(num);
        }
    }

    private void setNum(int numtt){
        num = numtt;
        Log.e(TAG,"这是第几个元算"+num);

    }

    public int getNum(){
       //
        return num;
    }
}
