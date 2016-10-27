package com.alchemistdigital.buxa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

public class WelcomeActivity extends AppCompatActivity {
    TextView tv_welcome, tv_CompanyName, tv_UserName, tv_UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tv_welcome = (TextView) findViewById(R.id.id_welcomeText_welcomeScreen);
        Typeface face1= Typeface.createFromAsset(getAssets(), "fonts/HELVETICA_CONDENSED_LIGHT_7.OTF");
        tv_welcome.setTypeface(face1);

        tv_CompanyName = (TextView) findViewById(R.id.id_tv_companyName);
        tv_UserName = (TextView) findViewById(R.id.id_tv_userName);
        tv_UserEmail = (TextView) findViewById(R.id.id_tv_userEmail);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        String companyName = getSharedPreference.getCompanyName(getResources().getString(R.string.companyName));
        String loginEmail = getSharedPreference.getLoginEmail(getResources().getString(R.string.loginEmail));
        String loginName = getSharedPreference.getLoginName(getResources().getString(R.string.loginName));

        tv_CompanyName.setText(companyName);
        tv_UserName.setText(loginName);
        tv_UserEmail.setText(loginEmail);

        // register this user with Buxa app on gcm
        // gcm generate registration id
        // registration id store in user table of Buxa database
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        String regId = GCMRegistrar.getRegistrationId(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                CommonVariables.DISPLAY_MESSAGE_ACTION));

        if (regId.equals("")) {
            // Registration is not present, Register now with GCM
            GCMRegistrar.register(getApplicationContext(), CommonVariables.SENDER_ID);
        }
    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            if(newMessage.equals("success")) {

            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };


    public void goToServiceActivity(View view) {
        startActivity(new Intent(this, SelectServiceActivity.class));
    }

    public void btnFeedbackClick(View view) {
        Toast.makeText(getApplicationContext(), "Work in progress...", Toast.LENGTH_LONG).show();
        // Todo : feedback form
    }

    public void btnEnquiryClick(View view) {
//        startActivity(new Intent(this, EnquiriesActivity.class));
        Toast.makeText(getApplicationContext(), "Work in progress...", Toast.LENGTH_LONG).show();
        // Todo : my enaquiry page form
    }

    public void btnLogout(View view) {
        SetSharedPreference setSharedPreference = new SetSharedPreference(this);
        // signout from app.
        setSharedPreference.setBooleanLogin(getString(R.string.boolean_login_sharedPref), "false");

        Intent intent = new Intent(this, StartupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(getApplicationContext());
        } catch (Exception e) {
            Log.e("UnRegisterReceiverError", "> " + e.getMessage());
        }
        super.onDestroy();
    }
}
