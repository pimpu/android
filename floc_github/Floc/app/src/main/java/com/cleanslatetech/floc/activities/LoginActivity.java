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

public class LoginActivity extends AppCompatActivity{


    EditText txtLoginUser, txtLoginPwd;
    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;

        txtLoginUser = (EditText) findViewById(R.id.idLoginUserName);
        txtLoginPwd = (EditText) findViewById(R.id.idLoginUserPassword);
    }

    public void loginUser(View view) {
        // Check if Internet present
        if (!isConnectingToInternet(LoginActivity.this)) {
            CommonUtilities.customToast(LoginActivity.this, getResources().getString(R.string.strNoInternet));
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
