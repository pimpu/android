package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.GetSocietyPerOBP;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.obp_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.obp_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get society data from server when obp has already created society.
        dbHelper = new DatabaseHelper(MainActivity.this);
        int rowsCount = dbHelper.numberOfRows();
        dbHelper.closeDB();
        if (rowsCount <= 0) {
            GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(MainActivity.this);
            int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
            String strUID = String.valueOf(uId);
            new GetSocietyPerOBP(MainActivity.this,strUID).execute();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.obp_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            // send program flow to enquiry creation class(Activity)
            startActivity(new Intent(MainActivity.this, Create_Enquiry.class));
        } else if (id == R.id.nav_viewEnquiry) {
            startActivity(new Intent(MainActivity.this, View_Enquiry.class));
        } else if (id == R.id.nav_createOrder) {
            // send program flow to order creation class(Activity)
            startActivity(new Intent(MainActivity.this, Create_Order.class));
        } else if (id == R.id.nav_newReply) {

        } else if (id == R.id.nav_createSociety) {
            // send program flow to Society creation class(Activity)
            startActivity(new Intent(MainActivity.this, Create_Society.class));
        } else if (id == R.id.nav_obp_logout) {
            // send program flow to Society creation class(Activity)
            startActivity(new Intent(MainActivity.this, Create_Society.class));
        }

        return true;
    }
}
