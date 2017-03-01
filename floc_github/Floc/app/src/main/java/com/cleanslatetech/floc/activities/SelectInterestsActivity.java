package com.cleanslatetech.floc.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class SelectInterestsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public static ImageView gotoNext;
    public static Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interests);

        mActionBarToolbar = (Toolbar) findViewById(R.id.id_toolbar_selectinterest);
        setSupportActionBar(mActionBarToolbar);

//        tvSelectInterestText = (TextView) findViewById(R.id.id_tv_interest_text);
        gotoNext = (ImageView) findViewById(R.id.submitInterest);
        GridView selectInterestGridview = (GridView) findViewById(R.id.id_gridview_selectInterest);

//        new GetInterestCategoryAsyncTask(SelectInterestsActivity.this, selectInterestGridview);
    }

    public void gotoNext(View view) {

        // Check if Internet present
        if (!isConnectingToInternet(SelectInterestsActivity.this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoInternet),Toast.LENGTH_LONG).show();
            // stop executing code by return
            return;
        } else {
//            new SetInterestAsyncTask( SelectInterestsActivity.this, iArraySelectedPositions ).postData();
//            startActivity(new Intent(getApplicationContext(), AllEventsActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
