package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignIn;
import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

/**
 * Created by pimpu on 3/31/2017.
 */

public class StartupActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();

        // This method will be executed once the timer is over
        // Start your app main activity

        GetSharedPreference getSharedPreference = new GetSharedPreference(StartupActivity.this);
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
    }
}
