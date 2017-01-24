package com.cleanslatetech.floc.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.OTPConformAsyncTask;
import com.cleanslatetech.floc.asynctask.RegisterUserAsyncTask;
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
import static com.cleanslatetech.floc.utilities.Validations.emailValidate;
import static com.cleanslatetech.floc.utilities.Validations.isEmptyString;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 006;
    private SignInButton btn_google_signIn;
    private GoogleApiClient mGoogleApiClient;
    private LoginButton btn_facebook_signIn;
    private CallbackManager callbackManager;
    private EditText txtUserName, txtUserEmail, txtPassword, txtConformPwd;
    public static Activity registerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_register);

        registerActivity = this;

        init();

        googleSignInSetup();

        facebookSignInSetup();
    }

    private void init() {
            txtUserName = (EditText) findViewById(R.id.idRegisterUserName);
            txtUserEmail = (EditText) findViewById(R.id.idRegisterUserEmail);
            txtPassword = (EditText) findViewById(R.id.idRegisterUserPassword);
            txtConformPwd = (EditText) findViewById(R.id.idRegisterUserConfirmPassword);
    }

    private void googleSignInSetup() {
        btn_google_signIn = (SignInButton) findViewById(R.id.btn_google_sign_register);
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

    private void facebookSignInSetup() {
        btn_facebook_signIn = (LoginButton) findViewById(R.id.btn_facebook_register);
        btn_facebook_signIn.setReadPermissions("public_profile");
        btn_facebook_signIn.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        btn_facebook_signIn.registerCallback(callbackManager, new FacebookCallBackMethod(RegisterActivity.this) );
    }

    public void btnGoogleSign() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_google_sign_register:
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
            handleGoogleSignInResult(RegisterActivity.this, result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        System.out.println("onConnectionFailed:" + connectionResult);
    }

    public void registerUser(View view) {

        // Check if Internet present
        if (!isConnectingToInternet(RegisterActivity.this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoInternet),Toast.LENGTH_LONG).show();
            // stop executing code by return
            return;
        } else {
            boolean hasError = false;

            String name = txtUserName.getText().toString();
            String email = txtUserEmail.getText().toString();
            String pwd = txtPassword.getText().toString();
            String cnfrmPwd = txtConformPwd.getText().toString();

            Boolean boolUserName = isEmptyString(name);
            Boolean boolEmail = emailValidate(email);
            Boolean boolUserPwd = isEmptyString(pwd);
            Boolean boolUserCnfrmPwd = isEmptyString(cnfrmPwd);

            if (boolUserName) {
                findViewById(R.id.errorNameText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorNameText).setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (boolEmail) {
                findViewById(R.id.errorEmailText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorEmailText).setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (boolUserPwd) {
                findViewById(R.id.errorPwdText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorPwdText).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.errorPwdText)).setText(getResources().getString(R.string.password_field_is_empty));
                hasError = true;
            }

            if (boolUserCnfrmPwd) {
                findViewById(R.id.errorCnfrmPwdText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorCnfrmPwdText).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.errorCnfrmPwdText)).setText(getResources().getString(R.string.confirm_password_field_is_empty));
                hasError = true;
            }

            if( boolUserPwd && pwd.length() < 6 ) {
                findViewById(R.id.errorPwdText).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.errorPwdText)).setText(getResources().getString(R.string.password_length_error_message));
                hasError = true;
            }
            else if( boolUserPwd && pwd.equals(pwd.toLowerCase()) ) {
                findViewById(R.id.errorPwdText).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.errorPwdText)).setText(getResources().getString(R.string.password_hasUpperCase_error_message));
                hasError = true;
            }

            if( boolUserPwd && boolUserCnfrmPwd ) {

                if( cnfrmPwd.equals(pwd)  ) {
                    findViewById(R.id.errorCnfrmPwdText).setVisibility(View.GONE);
                }
                else {
                    findViewById(R.id.errorCnfrmPwdText).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.errorCnfrmPwdText)).setText(getResources().getString(R.string.confirm_password_missmatch));

                    hasError = true;
                }
            }

            if( !hasError ) {
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                new OTPConformAsyncTask(RegisterActivity.this, name, email, pwd, cnfrmPwd).postData();
            }

        }
    }
}
