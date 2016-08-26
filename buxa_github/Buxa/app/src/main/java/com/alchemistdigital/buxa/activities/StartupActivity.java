package com.alchemistdigital.buxa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // made sharedprefrence
        GetSharedPreference getPrefrence = new GetSharedPreference(StartupActivity.this);

        // get boolean value of login sharedPreference for checking if user
        // if already logged in or not
        String loginSharedPref = getPrefrence.getLoginPreference(getResources().getString(R.string.boolean_login_sharedPref));
        System.out.println("is login: " + loginSharedPref);
        if( loginSharedPref != null  ) {
            if (loginSharedPref.equals("true") ) {
                SplashScreen splashFragment = new SplashScreen();
                fragmentTransaction.replace(android.R.id.content,splashFragment);
            }
            else {
                Login loginFragment = new Login();
                fragmentTransaction.replace(android.R.id.content, loginFragment);
            }
        }
        else {
            Login loginFragment = new Login();
            fragmentTransaction.replace(android.R.id.content, loginFragment);
        }
        fragmentTransaction.commit();
    }
}
