package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.LoginUserAsyncTask;
import com.cleanslatetech.floc.asynctask.RegisterSocialAsyncTask;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;
import static com.cleanslatetech.floc.utilities.Validations.isEmptyString;

public class UserNameFeedActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_user_name_feed);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onBackPressed() {
        if(getIntent().getExtras().getString("provider").equals("Facebook")) {
            LoginManager.getInstance().logOut();
        }
        else if(getIntent().getExtras().getString("provider").equals("Google")) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        }
                    });
        }
        startActivity(new Intent(this, LoginActivity.class));
        super.onBackPressed();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

    public void onClickNameFeed(View view) {
        if (!isConnectingToInternet(UserNameFeedActivity.this)) {
            CommonUtilities.customToast(UserNameFeedActivity.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        } else {
            AppCompatEditText txtUserName = (AppCompatEditText) findViewById(R.id.idFeedUserName);
            String user = txtUserName.getText().toString();

            Boolean boolUserName = isEmptyString(user);
            if (boolUserName) {
                findViewById(R.id.errorUserFeedNameError).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorUserFeedNameError).setVisibility(View.VISIBLE);
            }

            if( boolUserName ) {
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                new RegisterSocialAsyncTask(
                        UserNameFeedActivity.this,
                        user,
                        getIntent().getExtras().getString("email"),
                        getIntent().getExtras().getString("provider"),
                        getIntent().getExtras().getString("providerKey") ).postData();
            }

        }
    }
}
