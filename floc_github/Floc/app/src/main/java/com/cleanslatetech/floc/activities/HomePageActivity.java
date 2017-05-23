package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.ChannelRecyclerAdapter;
import com.cleanslatetech.floc.adapter.CustomSliderPagerAdapter;
import com.cleanslatetech.floc.adapter.DrawerMenuAdapter;
import com.cleanslatetech.floc.adapter.HomeFlocAdapter;
import com.cleanslatetech.floc.asynctask.GetMyProfile;
import com.cleanslatetech.floc.interfaces.InterfaceAllRecent_Current_Archive_Event;
import com.cleanslatetech.floc.interfaces.InterfaceOnClickText;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

public class HomePageActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        InterfaceAllRecent_Current_Archive_Event, InterfaceOnClickText {

    ViewPager mViewPager;
    private CustomSliderPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private int delay = 2500; //milliseconds
    private int page = 0;
    private Handler handler;
    Runnable runnable = new Runnable() {
        public void run() {
            if (mAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            mViewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    private ListView listDrawerMenu;
    public static JSONArray jsonArrayAllArchive, jsonArrayAllEvents, jsonArrayAllRecent, jsonArrayAllChannel;
    public static InterfaceAllRecent_Current_Archive_Event interfaceAllRecentAndCurrentEvent;

    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_splash_screen);


        // google setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        jsonArrayAllArchive = new JSONArray();
        jsonArrayAllEvents = new JSONArray();
        jsonArrayAllRecent = new JSONArray();
        jsonArrayAllChannel = new JSONArray();

        interfaceAllRecentAndCurrentEvent = this;

        String strActivityId = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdActivityId));
        if(strActivityId == null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("rate", 5);
                jsonObject.put("like", 1);
                jsonObject.put("review", 4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new SetSharedPreference(this).setString(getResources().getString(R.string.shrdActivityId), String.valueOf(jsonObject));
        }

        if(!hasPermissions(this, CommonVariables.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, CommonVariables.PERMISSIONS, CommonVariables.REQUEST_PERMISSION);

        } else {
            new GetMyProfile(this).getData();
        }
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonVariables.REQUEST_PERMISSION:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission has been denied by user");
//                    CommonUtilities.customToast(HomePageActivity.this, "Permission require for registering with Buxa.");

                    Snackbar.make(findViewById(R.id.activity_splash_screen), "Permission require for FLOCworld.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Action", null).show();
                } else {
                    new GetMyProfile(this).getData();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            TextView textView = new TextView(HomePageActivity.this);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setTextSize(17);
            textView.setLineSpacing(2, 1);
            textView.setText("Thanks for visiting! We're fine tuning your flocworld experience every time you drop by. See you soon!");

            //  alert dialog main layout
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(parms);

            layout.setGravity(Gravity.CLIP_VERTICAL);
            layout.setPadding(30, 30, 30, 2);

            // adding edittext and textview to alert dialog main layout
            layout.addView(textView);

            AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this)
                    .setCancelable(false)
                    .setView(layout)
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finish();
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

    @Override
    protected void onResume() {
        super.onResume();

        if (handler != null) {
            handler.postDelayed(runnable, delay);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
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
    }*/

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_more) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    @Override
    public void getAllEvents(JSONArray jsonArray) {
        jsonArrayAllEvents = jsonArray;
    }

    @Override
    public void getAllRecent(JSONArray jsonArray) {
        jsonArrayAllRecent = jsonArray;
    }

    @Override
    public void getAllChannle(JSONArray jsonArray) {
        jsonArrayAllChannel = jsonArray;
    }

    @Override
    public void getAllArchive(JSONArray paramJsonArray) {
        Uri URIdata = getIntent().getData();
        if( URIdata != null ) {
            List params = URIdata.getPathSegments();
            String param3 = params.get(2).toString();
            System.out.println(param3);
        }

        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        listDrawerMenu = (ListView) findViewById(R.id.list_drawer_menu);

        createPrimaryNav();

        // populating home page data ....
        jsonArrayAllArchive = paramJsonArray;

        GetSharedPreference getSharedPreference = new GetSharedPreference(HomePageActivity.this);

        LinearLayout llInterestedFloc = (LinearLayout) findViewById(R.id.id_home_selected_events);
        AppCompatTextView tvBtnMoreEvent = (AppCompatTextView) findViewById(R.id.tvBtnMoreEvent);

        tvBtnMoreEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, AllEventActivity.class));
            }
        });

        Set<String> strIntArrayInterestCategory = getSharedPreference.getStringSet(getResources().getString(R.string.shrdSelectedCategory));
        String strIntArrayAllCategory = getSharedPreference.getString(getResources().getString(R.string.shrdAllCategoryList));
        JSONArray joAllCategory = null;

        try {
            joAllCategory = new JSONArray(strIntArrayAllCategory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsonArrayAllEvents.length() <= 0) {
            findViewById(R.id.id_home_event_panel).setVisibility(View.GONE);
            return;
        }

        HashMap<Integer, JSONArray> hmapInterest = new HashMap<Integer, JSONArray>();

        for( int j = 0 ; j < jsonArrayAllEvents.length() ; j++  ) {
            try {
                int eventCategoryId = jsonArrayAllEvents.getJSONObject(j).getInt("EventCategory");

                JSONArray jsonArray = new JSONArray();

                if (strIntArrayInterestCategory.contains(eventCategoryId) ) {

                    boolean isEventCategory = hmapInterest.containsKey(eventCategoryId);

                    if(isEventCategory) {
                        jsonArray = hmapInterest.get(eventCategoryId);
                    }

                    jsonArray.put(jsonArrayAllEvents.getJSONObject(j));
                    hmapInterest.put(eventCategoryId, jsonArray);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for ( Integer key : hmapInterest.keySet() ) {
            String categoryName = "";

            for (int k = 0; k < joAllCategory.length(); k ++) {
                try {
                    if(key == joAllCategory.getJSONObject(k).getInt("EventCategoryId")) {
                        categoryName = joAllCategory.getJSONObject(k).getString("EventCategoryName");
                        joAllCategory.remove(k);
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflateBecauseLikeText = mInflater.inflate(R.layout.single_floc_layout, null, false);
            AppCompatTextView tvCategory = (AppCompatTextView) inflateBecauseLikeText.findViewById(R.id.id_dynamic_category_name);
            AppCompatTextView tvMoreCategory = (AppCompatTextView) inflateBecauseLikeText.findViewById(R.id.tvBtnMoreInterestFlocEvent);
            GridView gridLikesCategoryViseFloc = (GridView) inflateBecauseLikeText.findViewById(R.id.gridviewFloc);

            tvCategory.setText(categoryName);
            tvMoreCategory.setText("Much more "+categoryName);

            int length = hmapInterest.get(key).length();
            int len = ( length > 6 ) ? 6 : length;
            HomeFlocAdapter adapterRecent = new HomeFlocAdapter(this, hmapInterest.get(key), len);
            gridLikesCategoryViseFloc.setAdapter(adapterRecent);

            /*gridLikesCategoryViseFloc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent intentFlocDesc = new Intent(HomePageActivity.this, FlocDescriptionActivity.class);
                        intentFlocDesc.putExtra("floc_data", jsonArrayAllEvents.getJSONObject(position).toString());
                        intentFlocDesc.putExtra("from", "Home");
                        startActivity(intentFlocDesc);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });*/

            llInterestedFloc.addView(inflateBecauseLikeText);
        }

        RecyclerView llChannel = (RecyclerView) findViewById(R.id.id_home_channel_layout);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        llChannel.setLayoutManager(mLayoutManager);

        // Initialize a new Adapter for RecyclerView
        ChannelRecyclerAdapter adapterRecent = new ChannelRecyclerAdapter(this, jsonArrayAllChannel);
        llChannel.setAdapter(adapterRecent);
    }

    private void createPrimaryNav() {
        findViewById(R.id.nav_more_header).setVisibility(View.GONE);
        findViewById(R.id.nav_header).setVisibility(View.VISIBLE);

        final ArrayList<String> menuName = new ArrayList<>();
        menuName.add("Home");
        menuName.add("More");
        menuName.add(getResources().getString(R.string.setting));
        menuName.add(getResources().getString(R.string.action_logout));

        ArrayList<Integer> menuImage = new ArrayList<>();
        menuImage.add(R.drawable.ic_menu_camera);
        menuImage.add(R.drawable.ic_menu_gallery);
        menuImage.add(R.drawable.ic_menu_slideshow);
        menuImage.add(R.drawable.ic_menu_manage);

        // Getting adapter by passing xml data ArrayList
        DrawerMenuAdapter adapter = new DrawerMenuAdapter(this, menuName, menuImage);
        listDrawerMenu.setAdapter(adapter);

        // Click event for single list row
        listDrawerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleNavigationItemSelected(menuName.get(position));
            }
        });
    }

    private void createMoreNav() {
        findViewById(R.id.nav_more_header).setVisibility(View.VISIBLE);
        findViewById(R.id.nav_header).setVisibility(View.GONE);

        final ArrayList<String> menuName = new ArrayList<>();
        menuName.add(getResources().getString(R.string.about_us));
        menuName.add(getResources().getString(R.string.contact_us));
        menuName.add(getResources().getString(R.string.f_amp_q));
        menuName.add(getResources().getString(R.string.terms_amp_conditions));
        menuName.add(getResources().getString(R.string.privacy_policy));

        ArrayList<Integer> menuImage = new ArrayList<>();

        // Getting adapter by passing xml data ArrayList
        DrawerMenuAdapter adapter = new DrawerMenuAdapter(this, menuName, menuImage);
        listDrawerMenu.setAdapter(adapter);

        // Click event for single list row
        listDrawerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleNavigationItemSelected(menuName.get(position));
            }
        });

        findViewById(R.id.back_from_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPrimaryNav();
            }
        });
    }

    private void handleNavigationItemSelected(String menuItem) {
        if ( !menuItem.equals("More") ) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }

        Toast.makeText(HomePageActivity.this, menuItem, Toast.LENGTH_SHORT).show();

        if (menuItem.equals("More")) {
                ImageView imgBack = (ImageView) findViewById(R.id.back_from_more);
                imgBack.setImageResource(R.drawable.abc_ic_ab_back_material);

                createMoreNav();

        } else if(menuItem.equals(getResources().getString(R.string.setting))) {
            startActivity(new Intent(this, SettingActivity.class));

        } else if(menuItem.equals(getResources().getString(R.string.action_logout))) {

            String loginType = new GetSharedPreference(HomePageActivity.this)
                    .getString(getResources().getString(R.string.shrdLoginType));

            if(loginType.equals(getResources().getString(R.string.appLogin))) {
                // intet for next activity
                handleIntentWhenSignOut(HomePageActivity.this);
            }
            else if(loginType.equals(getResources().getString(R.string.facebookLogin))) {
                LoginManager.getInstance().logOut();

                // intet for next activity
                handleIntentWhenSignOut(HomePageActivity.this);
            }
            else if(loginType.equals(getResources().getString(R.string.googleLogin))) {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // intet for next activity
                                handleIntentWhenSignOut(HomePageActivity.this);
                            }
                        });
            }
        } else if( menuItem.equals(getResources().getString(R.string.about_us))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.about_us))
                    .putExtra("url", "http://floc.world/Home/About"));

        } else if( menuItem.equals(getResources().getString(R.string.contact_us))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.contact_us))
                    .putExtra("url", "http://floc.world/Home/Contact"));

        } else if( menuItem.equals(getResources().getString(R.string.f_amp_q))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.f_amp_q))
                    .putExtra("url", "http://floc.world/Home/FAQ"));

        } else if( menuItem.equals(getResources().getString(R.string.terms_amp_conditions))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.terms_amp_conditions))
                    .putExtra("url", "http://floc.world/terms.html"));

        } else if( menuItem.equals(getResources().getString(R.string.privacy_policy))) {

            startActivity(new Intent(getApplicationContext(), WebviewActivity.class)
                    .putExtra("from", getResources().getString(R.string.privacy_policy))
                    .putExtra("url", "http://floc.world/privacy-policy.html"));

        }

    }

    @Override
    public void onClickText(String menuItem) {
        handleNavigationItemSelected(menuItem);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
