package com.alchemistdigital.kissan.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.Login;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.GetAllSocietyAsyncTask;
import com.alchemistdigital.kissan.asynctask.GetEnquiryAsyncTask;
import com.alchemistdigital.kissan.asynctask.GetOrderAsyncTask;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.SetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.CommonVariables;
import com.alchemistdigital.kissan.utilities.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

/**
 * don't finish this activity. because network connection receiver is register with this activity.
 * if finish then it will not check the whether internet connection is working or not.
 */
public class AdminPanel extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tv_AdminEmail_navHeader,tv_AdminName_navHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(AdminPanel.this);
        int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
        String strUID = String.valueOf(uId);

        // get society data from server when obp has already created society.
        DatabaseHelper dbHelper = new DatabaseHelper(AdminPanel.this);
        int societyRowsCount = dbHelper.numberOfSocietyRows();
        int enquiryRowsCount = dbHelper.numberOfEnquiryRowsByUid();
        int orderRowsCount = dbHelper.numberOfOrderRowsByUserType("admin");
        dbHelper.closeDB();


        if ( isConnectingToInternet(AdminPanel.this) ) {

            // when app is uninstalled then this function get all data from server
            if (societyRowsCount <= 0) {
                new GetAllSocietyAsyncTask(AdminPanel.this).execute();
            }

            // when app is uninstalled then this function get all data from server
            if ( enquiryRowsCount <= 0 ){
                new GetEnquiryAsyncTask(AdminPanel.this,strUID).execute();
            }

            if ( orderRowsCount <= 0 ) {
                new GetOrderAsyncTask(AdminPanel.this,strUID,"admin").execute();
            }
        }


        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        registerReceiver(mHandleMessageReceiverAtAdmin, new IntentFilter(
                CommonVariables.DISPLAY_MESSAGE_ACTION));

        if (regId.equals("")) {
            // Registration is not present, Register now with GCM
            GCMRegistrar.register(getApplicationContext(), CommonVariables.SENDER_ID);
        }

        // set email-id and name to nav header view
        View headerLayout = navigationView.getHeaderView(0);

        String emailPreference = getPreference.getEmailPreference(getResources().getString(R.string.loginEmail));
        String namePreference = getPreference.getNamePreference(getResources().getString(R.string.loginName));

        tv_AdminEmail_navHeader = (TextView) headerLayout.findViewById(R.id.admin_email);
        tv_AdminName_navHeader  = (TextView) headerLayout.findViewById(R.id.admin_name);
        tv_AdminEmail_navHeader.setText(emailPreference);
        tv_AdminName_navHeader.setText(namePreference);

        this.registerReceiver(this.mConnReceiverAtAdmin,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * it check whether internet connection is working or not.
     */
    private BroadcastReceiver mConnReceiverAtAdmin = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if( isConnectingToInternet(context) ){
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();

//                DatabaseHelper dbHelper = new DatabaseHelper(context);
//                List<Offline> offlineData = dbHelper.getOfflineData();
//
//                for (int o = 0 ; o < offlineData.size() ; o++ ){
//                    System.out.println( offlineData.get(o) );
//                }

            } else {
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    };
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiverAtAdmin = new BroadcastReceiver() {
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
            unregisterReceiver(mHandleMessageReceiverAtAdmin);
            unregisterReceiver(mConnReceiverAtAdmin);
            GCMRegistrar.onDestroy(getApplicationContext());
        } catch (Exception e) {
            Log.e("UnRegisterReceiverError", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher_logo)
                .setTitle("Closing Kisan Cart")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AdminPanel.this.finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_panel, menu);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.id_admin_nav_viewEnquiry) {
            startActivity(new Intent(AdminPanel.this, View_Enquiry.class));
        } else if (id == R.id.id_admin_nav_newReply) {
            startActivity(new Intent(AdminPanel.this, New_Reply.class));
        } else if (id == R.id.id_admin_nav_viewOrder) {
            startActivity(new Intent(AdminPanel.this, View_Orders.class));
        } else if (id == R.id.id_admin_nav_createOBP) {
            // send program flow to OBP creation class(Activity)
            startActivity(new Intent(AdminPanel.this, Create_OBP.class));
        }else if (id == R.id.id_admin_nav_logout) {
            SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(AdminPanel.this);

            // it store false value of user for purpose of user is logging.
            setPreference.setLoginPreference(getResources().getString(R.string.boolean_login_sharedPref), "false");
            Intent intent = new Intent(AdminPanel.this, Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        }

        return true;
    }

    public void viewSQLiteDb(MenuItem item) {
        Intent dbmanager = new Intent(AdminPanel.this,AndroidDatabaseManager.class);
        startActivity(dbmanager);
    }
}
