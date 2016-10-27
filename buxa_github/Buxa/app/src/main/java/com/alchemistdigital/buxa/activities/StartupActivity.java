package com.alchemistdigital.buxa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.WakeLocker;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        // create Database
        DatabaseClass databaseClass = new DatabaseClass(this);

        registerReceiver(mHandleServerMessageReceiverInStartUpActivity, new IntentFilter(
                CommonVariables.DISPLAY_MESSAGE_ACTION));


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

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleServerMessageReceiverInStartUpActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            // this message is come from GetAllPackageType when all default value from server get finished.
            if(newMessage.equals("allDefaultDataFetched")) {
                Intent intentServicesActivity = new Intent(StartupActivity.this, WelcomeActivity.class);
                intentServicesActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                ((StartupActivity)context).finish();
                startActivity(intentServicesActivity);
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleServerMessageReceiverInStartUpActivity);
        } catch (Exception e) {
            Log.e("UnRegisterReceiverError", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    public void btnForgetPwd(View view) {
        // Todo : forget password email coding
    }
}
