package com.example.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class Stay_home_stay_safe extends AppCompatActivity {

    private static int SPLASH_SCREEN=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_home_stay_safe);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {

               moveTaskToBack(true);
               android.os.Process.killProcess(android.os.Process.myPid());
               System.exit(1);
           }
       },SPLASH_SCREEN);
    }
}
