package com.alchemistdigital.kissan.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.Login;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.GetEnquiryAsyncTask;
import com.alchemistdigital.kissan.asynctask.GetSocietyAsyncTask;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.SetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.CommonVariables;
import com.alchemistdigital.kissan.utilities.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper dbHelper;
    TextView tv_ObpEmail_navHeader,tv_ObpName_navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(MainActivity.this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.obp_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.obp_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set email-id and name to nav header view
        View headerLayout = navigationView.getHeaderView(0);

        String emailPreference = getPreference.getEmailPreference(getResources().getString(R.string.loginEmail));
        String namePreference = getPreference.getNamePreference(getResources().getString(R.string.loginName));

        tv_ObpEmail_navHeader = (TextView) headerLayout.findViewById(R.id.obp_email);
        tv_ObpName_navHeader  = (TextView) headerLayout.findViewById(R.id.obp_name);
        tv_ObpEmail_navHeader.setText(emailPreference);
        tv_ObpName_navHeader.setText(namePreference);


        // get society data from server when obp has already created society.
        dbHelper = new DatabaseHelper(MainActivity.this);
        int societyRowsCount = dbHelper.numberOfSocietyRows();
        int enquiryRowsCount = dbHelper.numberOfEnquiryRowsByUid();
        dbHelper.closeDB();

        int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
        String strUID = String.valueOf(uId);

        // when app is uninstalled then this function get all data from server
        if (societyRowsCount <= 0) {
            new GetSocietyAsyncTask(MainActivity.this,strUID).execute();
        }

        // when app is uninstalled then this function get all data from server
        if ( enquiryRowsCount <= 0){
            new GetEnquiryAsyncTask(MainActivity.this,strUID).execute();
        }

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
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

            System.out.println("on activity via gcm.");

            // Releasing wake lock
            WakeLocker.release();
        }
    };

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

    @Override
    public void onBackPressed() {
//        System.out.println(getSupportFragmentManager().getBackStackEntryCount() > 0);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 )
        {
            super.onBackPressed();
        }
        else
        {
            new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher_logo)
                .setTitle("Closing Kisan Cart")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(MainActivity.this);
                        String loginSharedPref = getPreference.getLoginPreference(getResources().getString(R.string.boolean_login_sharedPref));
                        if (loginSharedPref.equals("true")) {
                            finish();
                        }
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.obp_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_createEnquiry) {
            Intent intent = new Intent(MainActivity.this, Create_Enquiry.class);
            Bundle bundle = new Bundle();
            bundle.putString("callingClass","mainActivity");
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (id == R.id.nav_viewEnquiry) {

            startActivity(new Intent(MainActivity.this, View_Enquiry.class));

        } else if (id == R.id.nav_createOrder) {

            Intent orderIntent = new Intent(MainActivity.this, Create_View_Orders.class);
            Bundle bundle = new Bundle();
            bundle.putString("referenceNo","0");
            orderIntent.putExtras(bundle);
            startActivity(orderIntent);

        } else if (id == R.id.nav_newReply) {

            startActivity(new Intent(MainActivity.this, New_Reply.class));

        } else if (id == R.id.nav_createSociety) {

            startActivity(new Intent(MainActivity.this, Create_Society.class));

        } else if (id == R.id.nav_obp_logout) {

            SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(MainActivity.this);

            // it store false value of user for purpose of user is logging.
            setPreference.setLoginPreference(getResources().getString(R.string.boolean_login_sharedPref), "false");
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        }

        return true;
    }
}
