package com.cleanslatetech.floc.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cleanslatetech.floc.R;
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

public class SignupOptionActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private LoginButton btn_facebook_signIn;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of facebook sdk prior to create content view of activity.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_signup_option);

        googleSignInSetup();

        facebookSignInSetup();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupOptionActivity.this)
                .setCancelable(false)
                .setMessage("Do you want to Exit?")
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void googleSignInSetup() {
        LinearLayout layoutGoogle = (LinearLayout) findViewById(R.id.id_layout_google);
        ImageView imgGoogle = (ImageView) findViewById(R.id.id_img_google);
        AppCompatTextView tvGoogleText = (AppCompatTextView) findViewById(R.id.id_tv_signin_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
//        btn_google_signIn.setSize(SignInButton.SIZE_STANDARD);
//        btn_google_signIn.setColorScheme(SignInButton.COLOR_DARK);
//        btn_google_signIn.setScopes(gso.getScopeArray());

        layoutGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoogleSign();
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoogleSign();
            }
        });

        tvGoogleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoogleSign();
            }
        });

    }

    private void facebookSignInSetup() {
        LinearLayout layoutFb = (LinearLayout) findViewById(R.id.id_layout_fb);
        ImageView imgFb = (ImageView) findViewById(R.id.id_img_fb);
        AppCompatTextView tvFbText = (AppCompatTextView) findViewById(R.id.id_tv_signin_facebook);

        btn_facebook_signIn = (LoginButton) findViewById(R.id.btn_facebook_login);
        btn_facebook_signIn.setReadPermissions("public_profile");
        btn_facebook_signIn.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
        btn_facebook_signIn.registerCallback(callbackManager, new FacebookCallBackMethod(SignupOptionActivity.this) );

        layoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_facebook_signIn.performClick();
            }
        });

        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_facebook_signIn.performClick();
            }
        });

        tvFbText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_facebook_signIn.performClick();
            }
        });

    }

    public void btnGoogleSign() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(SignupOptionActivity.this, result);
        }
    }

    public void gotoRegisterActivity(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void gotoLoginActivity(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
