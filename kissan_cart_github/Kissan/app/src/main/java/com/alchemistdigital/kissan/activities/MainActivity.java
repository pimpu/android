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
import android.support.v7.widget.PopupMenu;
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
import com.alchemistdigital.kissan.asynctask.GetEnquiryAsyncTask;
import com.alchemistdigital.kissan.asynctask.GetOrderAsyncTask;
import com.alchemistdigital.kissan.asynctask.GetSocietyAsyncTask;
import com.alchemistdigital.kissan.asynctask.offlineAsyncTask.InsertOfflineEnquiryDataAsyncTask;
import com.alchemistdigital.kissan.asynctask.offlineAsyncTask.InsertOfflineOBPDataAsyncTask;
import com.alchemistdigital.kissan.asynctask.offlineAsyncTask.InsertOfflineOrderDataAsyncTask;
import com.alchemistdigital.kissan.asynctask.offlineAsyncTask.InsertOfflineSocietyDataAsyncTask;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.OBP;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Order;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.SetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.CommonVariables;
import com.alchemistdigital.kissan.utilities.WakeLocker;
import com.google.android.gcm.GCMRegistrar;

import java.util.List;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

/**
 * don't finish this activity. because network connection receiver is register with this activity.
 * if finish then it will not check the whether internet connection is working or not.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

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
        int orderRowsCount = dbHelper.numberOfOrderRowsByUserType("obp");
        dbHelper.closeDB();

        int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
        String strUID = String.valueOf(uId);

        if ( isConnectingToInternet(MainActivity.this) ) {

            // when app is uninstalled then this function get all data from server
            if (societyRowsCount <= 0) {
                new GetSocietyAsyncTask(MainActivity.this,strUID).execute();
            }

            // when app is uninstalled then this function get all data from server
            if ( enquiryRowsCount <= 0){
                new GetEnquiryAsyncTask(MainActivity.this,strUID).execute();
            }

            if ( orderRowsCount <= 0 ) {
                new GetOrderAsyncTask(MainActivity.this,strUID,"obp").execute();
            }
        }

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        registerReceiver(mHandleObpMessageReceiver,
                new IntentFilter(CommonVariables.DISPLAY_MESSAGE_ACTION));

        if (regId.equals("")) {
            // Registration is not present, Register now with GCM
            GCMRegistrar.register(getApplicationContext(), CommonVariables.SENDER_ID);
        }

        registerReceiver(mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    /**
     * Receiving push messages
     * */
    private BroadcastReceiver mHandleObpMessageReceiver = new BroadcastReceiver() {
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

    /**
     * it check whether internet connection is working or not.
     */
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if( isConnectingToInternet(context) ) {
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();

                DatabaseHelper dbHelper = new DatabaseHelper(context);
                List<Offline> offlineEnquiryData = dbHelper.getOfflineDataByTableName(DatabaseHelper.TABLE_ENQUIRY);
                List<Offline> offlineSocietyData = dbHelper.getOfflineDataByTableName(DatabaseHelper.TABLE_SOCIETY);
                List<Offline> offlineOrderData = dbHelper.getOfflineDataByTableName(DatabaseHelper.TABLE_ORDER);
                List<Offline> offlineOBPData = dbHelper.getOfflineDataByTableName(DatabaseHelper.TABLE_OBP);

                String jsonEnquiryArr = null;
                String jsonSocietyArr = null;
                String jsonOrderArr = null;
                String jsonOBPArr = null;

                for (int o = 0 ; o < offlineEnquiryData.size() ; o++ ) {

                    List<Enquiry> enquiryByID = dbHelper.getEnquiryByID(
                                                    offlineEnquiryData.get(o).getOffline_row_id(),
                                                    offlineEnquiryData.get(o).getOffline_row_action() );

                    jsonEnquiryArr = jsonEnquiryArr + enquiryByID.toString() ;
                }


                for (int p = 0 ; p < offlineSocietyData.size() ; p++ ) {
                    List<Society> offlineSocietyById = dbHelper.getOfflineSocietyById(
                            offlineSocietyData.get(p).getOffline_row_id(),
                            offlineSocietyData.get(p).getOffline_row_action());

                    jsonSocietyArr = jsonSocietyArr + offlineSocietyById.toString() ;
                }

                for (int b = 0 ; b < offlineOrderData.size() ; b++ ) {
                    List<Order> offlineOrderById = dbHelper.getOfflineOrderById(
                            offlineOrderData.get(b).getOffline_row_id(),
                            offlineOrderData.get(b).getOffline_row_action());

                    jsonOrderArr = jsonOrderArr + offlineOrderById.toString();
                }

                for (int q = 0 ; q < offlineOBPData.size() ; q++ ) {
                    List<OBP> offlineOBPById = dbHelper.getOfflineOBPById(
                            offlineOBPData.get(q).getOffline_row_id(),
                            offlineOBPData.get(q).getOffline_row_action());

                    jsonOBPArr = jsonOBPArr + offlineOBPById.toString() ;
                }

                if(jsonEnquiryArr != null) {
                    String jsonArrayEnquiryArr = jsonEnquiryArr.replace("null", "")
                                                                .replaceAll("\\]\\[", ",")
                                                                .replace(System.getProperty("line.separator"), " <br /> ");
                    new InsertOfflineEnquiryDataAsyncTask(MainActivity.this,jsonArrayEnquiryArr).execute();
                }

                if(jsonSocietyArr != null) {
                    String jsonArraySocietyArr = jsonSocietyArr.replace("null", "")
                                                                .replaceAll("\\]\\[", ",")
                                                                .replace(System.getProperty("line.separator"), " <br /> ");
                    new InsertOfflineSocietyDataAsyncTask(MainActivity.this,jsonArraySocietyArr).execute();
                }

                if( jsonOBPArr != null ){
                    String jsonArrayObpArr = jsonOBPArr.replace("null", "")
                            .replaceAll("\\]\\[", ",")
                            .replace(System.getProperty("line.separator"), " <br /> ");

                    GetSharedPreferenceHelper getPrefrence = new GetSharedPreferenceHelper(MainActivity.this);
                    String who = getPrefrence.getUserTypePreference(getResources().getString(R.string.userType));
                    String gId = null;
                    // check user is admin or obp
                    // on the bases of preference value.
                    if( who.equals("obp") ){
                        gId = "2";
                    }
                    else {
                        gId = "1";
                    }
//                    System.out.println(jsonArrayObpArr);
                    new InsertOfflineOBPDataAsyncTask(MainActivity.this,jsonArrayObpArr,gId).execute();
                }

                if( jsonOrderArr != null ) {
                    String jsonArrayOrderArr = jsonOrderArr.replace("null", "")
                            .replaceAll("\\]\\[", ",")
                            .replace(System.getProperty("line.separator"), " <br /> ");

                    System.out.println(jsonArrayOrderArr);
                    new InsertOfflineOrderDataAsyncTask(MainActivity.this,jsonArrayOrderArr).execute();
                }
                dbHelper.closeDB();

            } else {
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    };



    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleObpMessageReceiver);
            unregisterReceiver(mConnReceiver);
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
                    MainActivity.this.finish();
                }
            })
            .setNegativeButton("No", null)
            .show();
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

        /*if (id == R.id.nav_createEnquiry) {
            Intent intent = new Intent(MainActivity.this, Create_Enquiry.class);
            Bundle bundle = new Bundle();
            bundle.putString("callingClass", "mainActivity");
            intent.putExtras(bundle);
            startActivity(intent);

        } else*/ if (id == R.id.nav_viewEnquiry) {

            startActivity(new Intent(MainActivity.this, View_Enquiry.class));

        } /*else if (id == R.id.nav_createOrder) {

            Intent orderIntent = new Intent(MainActivity.this, Create_View_Orders.class);
            Bundle bundle = new Bundle();
            bundle.putString("referenceNo","0");
            orderIntent.putExtras(bundle);
            startActivity(orderIntent);

        }*/ else if (id == R.id.nav_newReply) {

            startActivity(new Intent(MainActivity.this, New_Reply.class));

        } /*else if (id == R.id.nav_createSociety) {

            startActivity(new Intent(MainActivity.this, Create_Society.class));

        }*/ else if(id == R.id.nav_viewOrder) {

            startActivity(new Intent(MainActivity.this, View_Orders.class));

        } else if(id == R.id.nav_viewSociety) {

            startActivity(new Intent(MainActivity.this, View_Society.class));

        } else if (id == R.id.nav_obp_logout) {

            SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(MainActivity.this);

            // it store false value of user for purpose of user is logging.
            setPreference.setLoginPreference(getResources().getString(R.string.boolean_login_sharedPref), "false");
            Intent intent = new Intent(MainActivity.this, Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        }

        return true;
    }

    public void viewSQLiteDbAtObp(MenuItem item) {
        Intent dbmanager = new Intent(MainActivity.this,AndroidDatabaseManager.class);
        startActivity(dbmanager);
    }

    public void openObpProfileMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(MainActivity.this);
        popup.inflate(R.menu.profile_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.obp_drawer_layout);
        Intent intent;
        Bundle extras;

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(MainActivity.this);
        int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        List<OBP> obpByUserId = dbHelper.getOBPByUserId(uId);
        dbHelper.closeDB();

        switch (item.getItemId()) {
            case R.id.action_admin_profile_details:

                drawer.closeDrawer(GravityCompat.START);

                // send program flow to OBP creation class(Activity)
                intent = new Intent(MainActivity.this, View_Obp_Details.class);
                extras = new Bundle();
                extras.putString("name", obpByUserId.get(0).getObp_name());
                extras.putString("store_name", obpByUserId.get(0).getObp_store_name());
                extras.putString("email_id", obpByUserId.get(0).getObp_email_id());
                extras.putString("contact", obpByUserId.get(0).getObp_contact_number());
                extras.putString("address", obpByUserId.get(0).getObp_address());
                extras.putInt("pincode", obpByUserId.get(0).getObp_pincode());
                extras.putString("city", obpByUserId.get(0).getObp_city());
                extras.putString("state", obpByUserId.get(0).getObp_state());
                extras.putString("country", obpByUserId.get(0).getObp_country());
                intent.putExtras(extras);
                startActivity(intent);

                return true;
            case R.id.action_admin_profile_edit:

                drawer.closeDrawer(GravityCompat.START);
                // send program flow to OBP creation class(Activity)
                intent = new Intent(MainActivity.this, Edit_Obp_Details.class);
                extras = new Bundle();
                extras.putString("name", obpByUserId.get(0).getObp_name());
                extras.putString("store_name", obpByUserId.get(0).getObp_store_name());
                extras.putString("email_id", obpByUserId.get(0).getObp_email_id());
                extras.putString("password", obpByUserId.get(0).getObp_email_passowrd());
                extras.putString("contact", obpByUserId.get(0).getObp_contact_number());
                extras.putString("address", obpByUserId.get(0).getObp_address());
                extras.putInt("pincode", obpByUserId.get(0).getObp_pincode());
                extras.putString("city", obpByUserId.get(0).getObp_city());
                extras.putString("state", obpByUserId.get(0).getObp_state());
                extras.putString("country", obpByUserId.get(0).getObp_country());
                extras.putInt("status", obpByUserId.get(0).getObp_status());
                extras.putInt("localid", obpByUserId.get(0).getObp_id());
                intent.putExtras(extras);
                startActivity(intent);

                return true;
            default:
                return false;
        }
    }
}
