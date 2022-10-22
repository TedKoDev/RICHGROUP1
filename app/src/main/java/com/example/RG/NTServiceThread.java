package com.example.RG;


import android.os.Handler;
import android.util.Log;

public class NTServiceThread extends Thread {
    Handler handler;
    String getText;
    boolean isRun = true;

    public NTServiceThread(Handler handler, String A) {
        Log.e("1st서비스쓰레드", " 1 ");
        this.handler = handler;
        this.getText = A;
        Log.e("1st서비스쓰레드", " 2 ");
    }

    public void stopForever() {
        synchronized (this) {
            Log.e("1st서비스쓰레드", " 3 ");
            this.isRun = false;
            Log.e("1st서비스쓰레드", " 4 ");
        }
        Log.e("1st서비스쓰레드", " 5 ");
    }

    public void run() {
        Log.e("1st서비스쓰레드", " 6 ");
        //반복적으로 수행할 작업을 한다.
        if (getText != null) {
//            while (isRun) {
            Log.e("1st서비스쓰레드", " 7 ");
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
            Log.e("1st서비스쓰레드", " 8 ");

            Log.e("1st서비스쓰레드", " 12 ");
//            }
        }
    }


}