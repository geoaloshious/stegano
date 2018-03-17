package com.example.geoaloshious.stegano;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class splash_screen extends AppCompatActivity
{
    TextView tv_steganopin;
    private static int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tv_steganopin=(TextView)findViewById(R.id.tv_steganopin);
        tv_steganopin.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i3 = new Intent(splash_screen.this, login.class);
                startActivity(i3);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}