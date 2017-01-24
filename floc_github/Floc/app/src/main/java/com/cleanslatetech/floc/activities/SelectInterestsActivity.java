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
import com.cleanslatetech.floc.asynctask.GetInterestCategoryAsyncTask;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

public class SelectInterestsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public static ImageView gotoNext;
    public static Toolbar mActionBarToolbar;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_select_interests);

        // google setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mActionBarToolbar = (Toolbar) findViewById(R.id.id_toolbar_selectinterest);
        setSupportActionBar(mActionBarToolbar);

//        tvSelectInterestText = (TextView) findViewById(R.id.id_tv_interest_text);
        gotoNext = (ImageView) findViewById(R.id.submitInterest);
        GridView selectInterestGridview = (GridView) findViewById(R.id.id_gridview_selectInterest);

        new GetInterestCategoryAsyncTask(SelectInterestsActivity.this, selectInterestGridview);
    }

    public void gotoNext(View view) {
        Toast.makeText(getApplicationContext(),"Hello", Toast.LENGTH_SHORT).show();
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
            String loginType = new GetSharedPreference(SelectInterestsActivity.this)
                    .getString(getResources().getString(R.string.shrdLoginType));

            if(loginType.equals(getResources().getString(R.string.appLogin))) {
                // intet for next activity
                handleIntentWhenSignOut(SelectInterestsActivity.this, false);
            }
            else if(loginType.equals(getResources().getString(R.string.facebookLogin))) {
                LoginManager.getInstance().logOut();

                // intet for next activity
                handleIntentWhenSignOut(SelectInterestsActivity.this, false);
            }
            else if(loginType.equals(getResources().getString(R.string.googleLogin))) {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // intet for next activity
                                handleIntentWhenSignOut(SelectInterestsActivity.this, false);
                            }
                        });
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}