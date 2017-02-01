package com.alchemistdigital.buxa.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.asynctask.ForgotPwdAsynTask;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;

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

                // gcm successfully registered
                // now subscribe to `global` topic to receive app wide notifications
                FirebaseMessaging.getInstance().subscribeToTopic(CommonVariables.TOPIC_GLOBAL);

                Intent intentServicesActivity = new Intent(StartupActivity.this, WelcomeActivity.class);
                intentServicesActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intentServicesActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentServicesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
            System.err.println("UnRegisterReceiverError > " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartupActivity.this)
                .setCancelable(false)
                .setMessage("Do you want to Exit?")
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        StartupActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
