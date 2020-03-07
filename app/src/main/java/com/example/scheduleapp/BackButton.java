package com.example.scheduleapp;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class BackButton {
    private static BackButton instance;
    private long pressedTime = 0;

    public static BackButton getInstance(){
        if(instance == null)
            instance = new BackButton();
        return instance;
    }

    private BackButton(){}

    void onBtnPressed(Context context, Activity activity){
        if(pressedTime == 0){
            Toast.makeText(context,"한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        }
        else{
            long second = System.currentTimeMillis() - pressedTime;

            if(second>2000){
                pressedTime = 0;
            }
            else{
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                };

                timer.schedule(timerTask,100);
                activity.finish();
            }
        }
    }
}
