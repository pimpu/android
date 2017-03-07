package com.cleanslatetech.floc.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.LoginUserAsyncTask;
import com.cleanslatetech.floc.asynctask.OTPConformAsyncTask;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.FacebookCallBackMethod;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleGoogleSignInResult;
import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;
import static com.cleanslatetech.floc.utilities.Validations.isEmptyString;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    SignInButton btn_google_signIn;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private LoginButton btn_facebook_signIn;
    private CallbackManager callbackManager;
    EditText txtLoginUser, txtLoginPwd;
    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

        loginActivity = this;

        txtLoginUser = (EditText) findViewById(R.id.idLoginUserName);
        txtLoginPwd = (EditText) findViewById(R.id.idLoginUserPassword);

        googleSignInSetup();

        facebookSignInSetup();
    }

    private void facebookSignInSetup() {
        btn_facebook_signIn = (LoginButton) findViewById(R.id.btn_facebook_login);
        btn_facebook_signIn.setReadPermissions("public_profile");
        btn_facebook_signIn.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        btn_facebook_signIn.registerCallback(callbackManager, new FacebookCallBackMethod(LoginActivity.this) );
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_google_sign_login:
                btnGoogleSign();
                break;
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

    public void loginUser(View view) {
        // Check if Internet present
        if (!isConnectingToInternet(LoginActivity.this)) {
            CommonUtilities.customToast(getApplicationContext(), getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        } else {
            String user = txtLoginUser.getText().toString();
            String pwd = txtLoginPwd.getText().toString();

            Boolean boolUserName = isEmptyString(user);
            Boolean boolUserPwd = isEmptyString(pwd);

            if (boolUserName) {
                findViewById(R.id.errorLoginNameText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorLoginNameText).setVisibility(View.VISIBLE);
            }

            if (boolUserPwd) {
                findViewById(R.id.errorLoginPwdText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorLoginPwdText).setVisibility(View.VISIBLE);
            }

            if( boolUserName && boolUserPwd ) {
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                new LoginUserAsyncTask(LoginActivity.this, user, pwd).postData();
            }

        }
    }
}
