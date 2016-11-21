package com.alchemistdigital.buxa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.NotificationUtils;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gcm.GCMRegistrar;
import com.google.firebase.messaging.FirebaseMessaging;

public class WelcomeActivity extends AppCompatActivity {
    TextView tv_welcome, tv_CompanyName, tv_UserName, tv_UserEmail;
    FloatingActionsMenu actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        actionButton = (FloatingActionsMenu) findViewById(R.id.right_labels);

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

        if(Build.MANUFACTURER.equals("Xiaomi") && CommonUtilities.isFirstTime(this)) {
            new AlertDialog.Builder(this)
                .setTitle("Notice")
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("Please, enable AutoStart option for Buxa app in Xiaomi phone Security.\nSecurity / Setting" +
                        " > Permission > Autostart > enable to buxa app.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Thanking you", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        }
    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String newMessage = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);

            // checking for type intent filter
            if (intent.getAction().equals(CommonVariables.REGISTRATION_COMPLETE)) {

                // gcm successfully registered
                // now subscribe to `global` topic to receive app wide notifications
                FirebaseMessaging.getInstance().subscribeToTopic(CommonVariables.TOPIC_GLOBAL);

            } else if (intent.getAction().equals(CommonVariables.PUSH_NOTIFICATION)) {
                // new push notification is received
                String message = intent.getStringExtra("message");
                Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
            }
        }
    };


    public void goToServiceActivity(View view) {
        startActivity(new Intent(this, SelectServiceActivity.class));
        // closed floating action menu which are open
        actionButton.collapse();
    }

    public void btnFeedbackClick(View view) {
        // closed floating action menu which are open
        actionButton.collapse();

        Toast.makeText(getApplicationContext(), "Work in progress...", Toast.LENGTH_LONG).show();
        // Todo : feedback form
//        startActivity(new Intent(WelcomeActivity.this, Feedback.class));
    }

    public void btnEnquiryClick(View view) {
        // closed floating action menu which are open
        startActivity(new Intent(this, EnquiriesActivity.class));
        actionButton.collapse();
//        Toast.makeText(getApplicationContext(), "Work in progress...", Toast.LENGTH_LONG).show();
    }

    public void btnLogout(View view) {
        // closed floating action menu which are open
        actionButton.collapse();

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
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandleMessageReceiver,
                new IntentFilter(CommonVariables.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandleMessageReceiver,
                new IntentFilter(CommonVariables.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandleMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
