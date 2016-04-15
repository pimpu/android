package info.alchemistdigital.e_carrier.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import info.alchemistdigital.e_carrier.AuthenticationScreen;
import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.asynctask.LogoutAsyncTask;
import info.alchemistdigital.e_carrier.services.GetLatLongService;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.ConnectionDetector;
import info.alchemistdigital.e_carrier.utilities.WakeLocker;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener  {
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    static  MainActivity main_activity;
    View mainLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayoutView = findViewById(R.id.drawer_layout);

        IntentFilter filterRefreshUpdate = new IntentFilter();
        filterRefreshUpdate.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filterRefreshUpdate.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(networkHandlerReceiver,filterRefreshUpdate);

        main_activity=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                CommonUtilities.DISPLAY_MESSAGE_ACTION));

        if (regId.equals("")) {
            // Registration is not present, Register now with GCM
            GCMRegistrar.register(getApplicationContext(), CommonUtilities.SENDER_ID);
        }

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        // display the first navigation drawer view on app launch
        displayView(0);

    }

    private final BroadcastReceiver networkHandlerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectionDetector cd=new ConnectionDetector(MainActivity.this);
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                Snackbar.make(mainLayoutView, "No internet connection !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // stop executing code by return
                return;
            }
        }
    };

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            boolean networkStatus = intent.getExtras().getBoolean(getResources().getString(R.string.networkStatus));

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            if(newMessage.equals("success")){
                finish();
                startActivity(getIntent());
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    public static MainActivity getInstance(){
        return   main_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        AppCompatActivity compact_activity = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                stopService(new Intent(MainActivity.this, GetLatLongService.class));
                break;

            case 1:

                fragment = null;
                compact_activity = new Profile_Edit();
                title = getString(R.string.title_profile);
                break;

            /* case 2:
                fragment = null;
                fragmentActivity = new MapsTrackingActivity();
                title = getString(R.string.title_track);
                break;

            case 3:
                *//*fragment = new MessagesFragment();
                title = getString(R.string.title_contact);*//*
                break;*/

            case 2:
                SharedPreferences sharedPreferenceLogin=getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                int userId = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId),0);

                ConnectionDetector cd=new ConnectionDetector(MainActivity.this);
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    Snackbar.make(mainLayoutView, "No internet connection !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    // stop executing code by return
                    return;
                }
                else{
                    new extendClassLogoutAsyncTask(MainActivity.this,userId).execute();
                }

            default:
                break;
        }


        if (fragment != null) {

//            if(title.equals(getString(R.string.title_book))){
//                startService(new Intent(MainActivity.this, GetLatLongService.class));
//            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle("");
        }
        else if (compact_activity != null){
            startActivity(new Intent(MainActivity.this,Profile_Edit.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
            unregisterReceiver(networkHandlerReceiver);
            GCMRegistrar.onDestroy(getApplicationContext());
            stopService(new Intent( MainActivity.this,GetLatLongService.class));
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
                    .setIcon(R.drawable.logo)
                    .setTitle("Closing E-Carrier")
                    .setMessage("Are you sure you want to close this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferenceLogin=getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                            String loginSharedPref = sharedPreferenceLogin.getString(getResources().getString(R.string.boolean_login_sharedPref), "");
                            if(loginSharedPref.equals("true")  )
                            {
                                finish();
                            }
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    // this did because finish() not found in LogoutAsyncTask.
    private class extendClassLogoutAsyncTask extends LogoutAsyncTask {

        public extendClassLogoutAsyncTask(Context applicationContext, int userId) {
            super(applicationContext, userId);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // check log cat fro response

            try {
                Log.d("Logout Response", result.toString());

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")){
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);

                // check for success tag
                String message = json.getString(CommonUtilities.TAG_MESSAGE);
                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                if(success == 0){

                    SharedPreferences sharedPreferenceLogin=getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                    SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
                    editor.putString(getResources().getString(R.string.boolean_login_sharedPref), "false");
                    editor.commit();

                    // start location service
                    stopService(new Intent(MainActivity.this, GetLatLongService.class));

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Intent gotoLogin = new Intent(getApplicationContext(),AuthenticationScreen.class);
                    gotoLogin.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    gotoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(gotoLogin);
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}