package com.cleanslatetech.floc.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleGoogleSignInResult;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    SignInButton btn_google_signIn;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private ProgressDialog mProgressDialog;
    private LoginButton btn_facebook_signIn;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

        googleSignInSetup();

        facebookSignInSetup();

        System.out.println(CommonUtilities.isFacebookLoggedIn());
    }

    private void facebookSignInSetup() {
        btn_facebook_signIn = (LoginButton) findViewById(R.id.btn_facebook_login);
        btn_facebook_signIn.setReadPermissions("public_profile");
        btn_facebook_signIn.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        btn_facebook_signIn.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Get facebook data from login
                                Bundle bFacebookData = CommonUtilities.getFacebookData(object);
                                System.out.println("profile_pic: "+bFacebookData.getString("profile_pic"));
                                System.out.println("idFacebook: "+bFacebookData.getString("idFacebook"));
                                System.out.println("first_name: "+bFacebookData.getString("first_name"));
                                System.out.println("last_name: "+bFacebookData.getString("last_name"));
                                System.out.println("email: "+bFacebookData.getString("email"));
                                System.out.println("gender: "+bFacebookData.getString("gender"));

//                                startActivity(new Intent(LoginActivity.this, SelectInterestsActivity.class));
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        System.out.println("Facebook login cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("facebook Login: "+exception.getMessage());
                    }
                });
    }

    private void googleSignInSetup() {
        btn_google_signIn = (SignInButton) findViewById(R.id.btn_google_sign_login);
        btn_google_signIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btn_google_signIn.setSize(SignInButton.SIZE_STANDARD);
        btn_google_signIn.setColorScheme(SignInButton.COLOR_DARK);
        btn_google_signIn.setScopes(gso.getScopeArray());
    }

    public void gotoRegsiterActivity(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void btnGoogleSign() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void btnSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        System.out.println("btnSignOut: sign out");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_google_sign_login:
                btnGoogleSign();
                break;

            /*case R.id.btn_fb_sign_out:
                LoginManager.getInstance().logOut();
                break;*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(LoginActivity.this, result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            System.out.println("Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleGoogleSignInResult(LoginActivity.this, result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            CommonUtilities.showProgressDialog(LoginActivity.this);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    CommonUtilities.hideProgressDialog();
                    handleGoogleSignInResult(LoginActivity.this, googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

}
