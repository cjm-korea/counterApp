package com.example.countingapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    final String intervalServiceTAG = "IntervalService";
    private Thread sThread;

    String name;
    int interval;
    boolean isService = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        name = intent.getExtras().getString("name");
        interval = intent.getExtras().getInt("radioNum");
        isService = true;

        if(!name.equals("")){
            // It should be destroy && Thread = null => reInitial?
            if(sThread == null){
                sThread = new Thread("My Thread"){
                    @Override
                    public void run(){
                        while(!Thread.currentThread().isInterrupted() && isService){
                                try{
                                    Thread.sleep(interval);
                                    Log.d(intervalServiceTAG, "Content = " + name + ", Interval = " + interval + "ms");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    this.interrupt();
                                }
                        }
                    }
                };
                sThread.start();
            }
        }else{
            // When name is null
//            if(!Thread.currentThread().isInterrupted() || Thread.currentThread().isAlive()){
            isService = false;
//            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isService = false;
    }

    public MyService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}