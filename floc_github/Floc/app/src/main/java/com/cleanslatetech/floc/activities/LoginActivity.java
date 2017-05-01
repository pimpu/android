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

public class LoginActivity extends AppCompatActivity {

    // TODO: search bar by category, city, date
    // TODO: change the repsonse format of My booking Floc array in right menu FLOCworld option
    // TODO: image in chat box
    // TODO: save profile image when login with social
    // TODO: Deep linking in share
    // TODO: You tube type share layout
    // TODO: Notification
    // TODO: Image customization
    // TODO: sort image using GPS location
    // TODO: Edit Interest Option in right menu
    // TODO: change plus minus btn into checkbox while selecting interest
    // TODO: change text "start flocking" button and "the above 5 are for registration process"
    // TODO: remove single task from web view activity. not open webview again when other webview option open
    // TODO: alternate solution for moving text

    EditText txtLoginEmail, txtLoginPwd;
    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;

        txtLoginEmail = (EditText) findViewById(R.id.idLoginUserEmail);
        txtLoginPwd = (EditText) findViewById(R.id.idLoginUserPassword);
    }

    public void loginUser(View view) {
        // Check if Internet present
        if (!isConnectingToInternet(LoginActivity.this)) {
            CommonUtilities.customToast(LoginActivity.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        } else {
            String email = txtLoginEmail.getText().toString();
            String pwd = txtLoginPwd.getText().toString();

            Boolean boolUserEmail = isEmptyString(email);
            Boolean boolUserPwd = isEmptyString(pwd);

            if (boolUserEmail) {
                findViewById(R.id.errorLoginEmailText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorLoginEmailText).setVisibility(View.VISIBLE);
            }

            if (boolUserPwd) {
                findViewById(R.id.errorLoginPwdText).setVisibility(View.GONE);
            } else {
                findViewById(R.id.errorLoginPwdText).setVisibility(View.VISIBLE);
            }

            if( boolUserEmail && boolUserPwd ) {
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                new LoginUserAsyncTask(LoginActivity.this, email, pwd).postData();
            }
        }
    }
}
