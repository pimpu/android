package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.FacebookCallBackMethod;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleGoogleSignInResult;
import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignIn;
import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

public class SplashScreenActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static int SPLASH_TIME_OUT = 2000;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_splash_screen);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onResume() {


        super.onResume();
    }

    @Override
    public void onStart() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try {

                    GetSharedPreference getSharedPreference = new GetSharedPreference(SplashScreenActivity.this);
                    String loginType = getSharedPreference.getString(getResources().getString(R.string.shrdLoginType));

                    if (loginType.equals(getResources().getString(R.string.appLogin))) {

                        boolean isSignIn = getSharedPreference.getBoolean(getResources().getString(R.string.isAppSignIn));
                        System.out.println("Splash: "+isSignIn);
                        if(isSignIn) {
                            handleIntentWhenSignIn(
                                    SplashScreenActivity.this,
                                    loginType,
                                    true,
                                    new GetSharedPreference(SplashScreenActivity.this).getString(getResources().getString(R.string.shrdUserName)),
                                    new GetSharedPreference(SplashScreenActivity.this).getString(getResources().getString(R.string.shrdUserEmail)),
                                    new GetSharedPreference(SplashScreenActivity.this).getInt(getResources().getString(R.string.shrdLoginId)) );
                        }
                        else {
                            // intet for next activity
                            handleIntentWhenSignOut(SplashScreenActivity.this, false);
                        }
                    }
                    else if(loginType.equals(getResources().getString(R.string.facebookLogin))) {

                        AccessToken facebookLoggedIn = CommonUtilities.isFacebookLoggedIn();
                        if(facebookLoggedIn != null ) {
                            new FacebookCallBackMethod(SplashScreenActivity.this).getFacebookProfileData(facebookLoggedIn);
                        }
                        else {
                            // intet for next activity
                            handleIntentWhenSignOut(SplashScreenActivity.this, false);
                        }
                    }
                    else if(loginType.equals(getResources().getString(R.string.googleLogin))) {

                        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                        if (opr.isDone()) {
                            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                            // and the GoogleSignInResult will be available instantly.
                            System.out.println("Got cached sign-in");
                            GoogleSignInResult result = opr.get();
                            handleGoogleSignInResult(SplashScreenActivity.this, result);
                        } else {
                            // If the user has not previously signed in on this device or the sign-in has expired,
                            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                            // single sign-on will occur in this branch.
                            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                                @Override
                                public void onResult(GoogleSignInResult googleSignInResult) {
                                    handleGoogleSignInResult(SplashScreenActivity.this, googleSignInResult);
                                }
                            });
                        }
                    }
                }
                catch (Exception e){
                    // intet for next activity
                    handleIntentWhenSignOut(SplashScreenActivity.this,false);
                    System.err.println("SplashScreenActivity(Catch): "+e.getMessage());
                }
            }
        }, SPLASH_TIME_OUT);

        super.onStart();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("onConnectionFailed:" + connectionResult);
    }
}
