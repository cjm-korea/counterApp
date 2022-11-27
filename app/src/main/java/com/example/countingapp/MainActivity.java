package com.example.countingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    // region initial
    RadioGroup radioGroup;
    EditText editText;
    final String intervalServiceTAG = "IntervalService";
    Thread serviceThread = new Thread(new ServiceThread());
    Thread cntThread = new Thread(new CounterThread());
    Boolean serviceState = false;
    Boolean cntState = false;
    Boolean processState = false;
    // endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // region initial xml
        Button btnServiceOn = findViewById(R.id.buttonServiceOn);
        Button btnServiceOff = findViewById(R.id.buttonServiceOff);
        Button btnCountOn = findViewById(R.id.buttonCountOn);
        Button btnCountOff = findViewById(R.id.buttonCountOff);
        editText = findViewById(R.id.editTextContent);
        radioGroup = findViewById(R.id.radioGroup);
        // endregion

        // region btn clicked
        btnServiceOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")) {
                    Log.d(intervalServiceTAG, "Start Service");
                    processState = true;
                    serviceState = false;
                    serviceThread.interrupt();
                    serviceState = true;
                } else {
                    Log.d(intervalServiceTAG, "Start Service");
//                    If initial state -> new Thread(new ServiceThread()) is can't resume Thread
//                    This state is thread.state == null, so nullPointerException
//                    if(serviceThread.isAlive() || serviceThread.isInterrupted()){
//                        serviceThread.interrupt();
//                        serviceThread = null;
//                        serviceThread = new Thread(new ServiceThread());
//                    }
//                    Finally there are runnable thread is stacked?
//                    Not Flag, use isInterrupt or isAlive => It's not Work!
//                    나는 뭣도 아니지만 improve is need to .isAlive, .isInterrupt for state check
                    if (serviceState) {
                        serviceThread.interrupt();
                        serviceThread = null;
                        serviceThread = new Thread(new ServiceThread());
                    }
                    processState = true;
                    serviceState = true;
                    serviceThread.start();
                }
            }
        });

        btnServiceOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processState = false;
                serviceState = false;
                Log.d(intervalServiceTAG, "Stop Service");
                serviceThread.interrupt();
//                Actually it need to readyState when btnOn is clicked, check for new initial thread
                serviceState = true;
            }
        });

        btnCountOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(processState){
                    if (cntState) {
                        cntThread.interrupt();
                        cntThread = null;
                        cntThread = new Thread(new CounterThread());
                    }
                    cntState = true;
                    cntThread.start();
                }
            }
        });

        btnCountOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cntState = false;
                cntThread.interrupt();
                cntState = true;
            }
        });
        // endregion
    }

    private int getRadioNum() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButton100:
                return 100;
            case R.id.radioButton300:
                return 300;
            case R.id.radioButton500:
                return 500;
            default:
                return 1000;
        }
    }

    // region ThreadClass
    public class ServiceThread implements Runnable {
        @Override
        public void run() {
            int interval = getRadioNum();
            String name = editText.getText().toString();
            while (true) {
//                Flag for check state
                while (serviceState) {
                    try {
                        Thread.sleep(interval);
                        Log.d(intervalServiceTAG, "Content = " + name + ", Interval = " + interval + "ms");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    public class CounterThread implements Runnable {
        @Override
        public void run() {
            int cnt = 0;
            int interval = getRadioNum();
            while (true) {
                while (cntState && processState) {
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
    }
    // endregion
}