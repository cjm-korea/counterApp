package com.example.countingapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public static final String ACTION_START_COUNTING = "start count";
    public static final String ACTION_STOP_COUNTING = "stop count";

    final String intervalServiceTAG = "IntervalService";
    private Thread sThread;
    private Thread cThread = null;

    boolean isService = false;
    String name;
    int interval;
    int cnt = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_START_COUNTING);
        intentFilter.addAction(ACTION_STOP_COUNTING);
        registerReceiver(br, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        name = intent.getExtras().getString("name");
        interval = intent.getExtras().getInt("radioNum");

        if(!name.equals("")){
            // It should be destroy && Thread = null => reInitial?
            if(sThread == null){
                sThread = new Thread("My Thread"){
                    @Override
                    public void run(){
                        while(!Thread.currentThread().isInterrupted()){
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
                isService = true;
            }
        }else{
            if(!Thread.currentThread().isInterrupted() || Thread.currentThread().isAlive()){
                sThread.interrupt();
                sThread = null;
                isService = false;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(sThread != null && sThread.isAlive()){
            sThread.interrupt();
            sThread = null;
            isService = false;
            cnt = 0;
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_START_COUNTING) && isService){
                cThread = new Thread(new CounterThread());
                cThread.start();
            }else if(action.equals(ACTION_STOP_COUNTING) && isService){
                cThread.interrupt();
                isService = false;
                cThread = null;
                cnt = 0;
            }else{
                Log.d("check", "onReceive: ");
            }
        }
    };

    // region ThreadClass
    public class CounterThread implements Runnable {
        @Override
        public void run() {
            int cnt = 0;
            while (true) {
                while(isService)
                    try {
                        Thread.sleep(interval);
                        Log.d(intervalServiceTAG, "Counter = " + cnt + ", Interval = " + interval + "ms");
                        cnt++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
            }
        }
    }
    // endregion
}