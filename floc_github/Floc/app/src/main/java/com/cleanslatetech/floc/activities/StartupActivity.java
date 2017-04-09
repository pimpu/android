package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pimpu on 3/31/2017.
 */

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();

        /*GetSharedPreference getSharedPreference = new GetSharedPreference(StartupActivity.this);

        boolean boolShowFeature = getSharedPreference.getBoolean(getResources().getString(R.string.isShowFeatureActivity));

        if( !boolShowFeature ) {
            startActivity(new Intent(this, FeaturingActivity.class));
            finish();
        } else {

            boolean isSignIn = getSharedPreference.getBoolean(getResources().getString(R.string.isAppSignIn));

            if(isSignIn) {
                handleIntentWhenSignIn(
                        this,
                        getSharedPreference.getString(getResources().getString(R.string.shrdLoginType)),
                        getSharedPreference.getString(getResources().getString(R.string.shrdUserName)),
                        getSharedPreference.getString(getResources().getString(R.string.shrdUserEmail)),
                        getSharedPreference.getInt(getResources().getString(R.string.shrdLoginId)) );
            }
            else {
                // intet for next activity
                handleIntentWhenSignOut(this);
            }
        }*/

        startActivity(new Intent(this, SplashScreenActivity.class));
        finish();
    }
}
