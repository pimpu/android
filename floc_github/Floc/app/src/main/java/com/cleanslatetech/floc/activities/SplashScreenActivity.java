package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cleanslatetech.floc.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }

    @Override
    protected void onResume() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    System.out.println("SplashScreenActivity(OnResume)");
                    finish();
                }
                catch (Exception e){
                    System.err.println("SplashScreenActivity(OnResume): "+e.getMessage());
                }
            }
        }, SPLASH_TIME_OUT);

        super.onResume();
    }
}
