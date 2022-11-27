package com.example.countingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Objects;

public class splash extends AppCompatActivity implements View.OnClickListener{
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash);

        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onStart(){
        super.onStart();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splash.this.beginMainActivity();
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        beginMainActivity();
    }

    private void beginMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}