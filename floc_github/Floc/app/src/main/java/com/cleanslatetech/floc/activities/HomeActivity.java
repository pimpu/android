package com.cleanslatetech.floc.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.EventFlocAdapter;
import com.cleanslatetech.floc.adapter.InterestAdapter;
import com.cleanslatetech.floc.adapter.RecentFlocAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

public class HomeActivity extends BaseAppCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout linearLayoutSelectInterest;
    private SlidingSplashView sliderLayout;
    private AppCompatTextView tvBtnSaveInterest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setSlideOrInterestGrid();

        initRecentFlocGridview();
        initEventGridview();
    }

    private void setSlideOrInterestGrid() {
        linearLayoutSelectInterest = (LinearLayout) findViewById(R.id.selectIntrest_layout);
        sliderLayout = (SlidingSplashView) findViewById(R.id.slider_layout);

        boolean isAvailInterest = new GetSharedPreference(this).getBoolean(getResources().getString(R.string.shrdIsInterestSelected));

        if( !isAvailInterest ) {
            sliderLayout.setVisibility(View.GONE);
            linearLayoutSelectInterest.setVisibility(View.VISIBLE);
            interestGridview();
        }

    }

    private void interestGridview() {
        tvBtnSaveInterest = (AppCompatTextView) findViewById(R.id.tvBtnSaveInterest);
        GridView gridview = (GridView) findViewById(R.id.gridviewInterest);
        final InterestAdapter adapterInterest = new InterestAdapter(this);
        gridview.setAdapter(adapterInterest);

        tvBtnSaveInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(adapterInterest.iArraySelectedPositions);
                new SetSharedPreference(HomeActivity.this).setBoolean(getResources().getString(R.string.shrdIsInterestSelected),true);
                sliderLayout.setVisibility(View.VISIBLE);
                linearLayoutSelectInterest.setVisibility(View.GONE);
            }
        });
    }

    public void changeState_saveInterest(int counter) {
        if(counter == 0) {
            tvBtnSaveInterest.setVisibility(View.VISIBLE);
        }
        else {
            tvBtnSaveInterest.setVisibility(View.GONE);
        }
    }

    public void initRecentFlocGridview() {
        AppCompatTextView tvBtnMoreRecentFloc = (AppCompatTextView) findViewById(R.id.tvBtnMoreRecentFloc);
        GridView gridviewRecentFloc = (GridView) findViewById(R.id.gridviewRecentFloc);
        RecentFlocAdapter adapterRecent = new RecentFlocAdapter(this);
        gridviewRecentFloc.setAdapter(adapterRecent);
    }

     public void initEventGridview() {
        AppCompatTextView tvBtnMoreEvent = (AppCompatTextView) findViewById(R.id.tvBtnMoreEvent);
        GridView gridviewEvent = (GridView) findViewById(R.id.gridviewEvent);
        EventFlocAdapter adapterRecent = new EventFlocAdapter(this);
        gridviewEvent.setAdapter(adapterRecent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            String loginType = new GetSharedPreference(HomeActivity.this)
                    .getString(getResources().getString(R.string.shrdLoginType));

            if(loginType.equals(getResources().getString(R.string.appLogin))) {
                // intet for next activity
                handleIntentWhenSignOut(HomeActivity.this, false);
            }
            else if(loginType.equals(getResources().getString(R.string.facebookLogin))) {
                LoginManager.getInstance().logOut();

                // intet for next activity
                handleIntentWhenSignOut(HomeActivity.this, false);
            }
            else if(loginType.equals(getResources().getString(R.string.googleLogin))) {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // intet for next activity
                                handleIntentWhenSignOut(HomeActivity.this, false);
                            }
                        });
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
