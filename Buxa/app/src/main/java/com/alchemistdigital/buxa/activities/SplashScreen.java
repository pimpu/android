package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    // made sharedprefrence
                    GetSharedPreference getPrefrence = new GetSharedPreference(SplashScreen.this);

                    // get boolean value of login sharedPreference for checking if user
                    // if already logged in or not
                    String loginSharedPref =getPrefrence.getLoginPreference(getResources().getString(R.string.boolean_login_sharedPref));

                    Intent intent;

                    intent = new Intent(SplashScreen.this, MainActivity.class);

                    if( loginSharedPref.equals("true")) {
                    }
                    else {
                        intent = new Intent(SplashScreen.this, Login.class);
                    }

                    startActivity(intent);

                    finish();

                }
            }
        };
        timerThread.start();

    }
}
