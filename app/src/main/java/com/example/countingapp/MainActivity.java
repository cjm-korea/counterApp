package com.example.countingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    // region initial
    final String intervalServiceTAG = "IntervalService";
    Intent sintent;
    Thread cThread = null;
    RadioGroup radioGroup;
    EditText editText;
    boolean isService = false;
    boolean isCnt = false;
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
                if(isService) {
                    stopService(new Intent(getApplicationContext(), MyService.class));
                    putIntent(sintent);
                    startService(sintent);
                }else{
                    isService = true;
                    Log.d(intervalServiceTAG, "Start Service");
                    sintent = new Intent(getApplicationContext(), MyService.class);
                    putIntent(sintent);
                    startService(sintent);
                }
            }
        });

        btnServiceOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isService = false;
                Log.d(intervalServiceTAG, "Stop Service");
                stopService(new Intent(getApplicationContext(), MyService.class));
            }
        });

        btnCountOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isService){
                    if(isCnt){
                        cThread.interrupt();
                        cThread = null;
                    }
                    isCnt = true;
                    cThread = new Thread(new CounterThread());
                    cThread.start();
                }
            }
        });
//
        btnCountOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCnt == true){
                    Log.d(intervalServiceTAG, "Stop Count");
                    cThread.interrupt();
                    cThread = null;
                }
            }
        });
        // endregion
    }

    private void putIntent(Intent intent) {
        sintent.putExtra("name", editText.getText().toString());
        sintent.putExtra("radioNum", getRadioNum());
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
    public class CounterThread implements Runnable {
        @Override
        public void run() {
            int cnt = 0;
            int interval = getRadioNum();
            while (true) {
                while(isService)
                    try {
                        Thread.sleep(interval);
                        Log.d(intervalServiceTAG, "Count = " + cnt + ", Interval = " + interval + "ms");
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