package com.alchemistdigital.buxa.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.emailValidate;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class SplashScreen extends AppCompatActivity {
    RelativeLayout relativeLayout_loginPanel;
    EditText txtLogin, txtPassword;
    TextInputLayout loginEmail_InputLayout, loginPwd_InputLayout;
    ProgressDialog prgDialog;
    Button btnLogin;
    LinearLayout layout_noConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // made sharedprefrence
        GetSharedPreference getPrefrence = new GetSharedPreference(SplashScreen.this);

        // get boolean value of login sharedPreference for checking if user
        // if already logged in or not
        Boolean loginSharedPref =getPrefrence.getLoginPreference(getResources().getString(R.string.boolean_login_sharedPref));
        if( loginSharedPref ) {

            setContentView(R.layout.activity_splash_screen);

            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(2000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        SplashScreen.this.finish();
                    }
                }
            };
            timerThread.start();
        }
        else {
            setContentView(R.layout.activity_login);
            layout_noConnection = (LinearLayout) findViewById(R.id.id_noInternet_login);
            loginMethod();
        }

    }

    private void loginMethod() {
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(SplashScreen.this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Logging ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        relativeLayout_loginPanel = (RelativeLayout) findViewById(R.id.layout_loginPanel);
        txtLogin = (EditText) findViewById(R.id.login_email);
        txtPassword = (EditText) findViewById(R.id.login_password);
        loginEmail_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_login_email);
        loginPwd_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        Animation translateAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_logo_login);
        ImageView imageView = (ImageView) findViewById(R.id.id_buxaLogo_splashscreen);
        imageView.startAnimation(translateAnim);
        translateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relativeLayout_loginPanel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void goToRegisterPage(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public void doLogging(View view) {
        Boolean boolEmail = emailValidate(txtLogin.getText().toString());
        Boolean boolPwd = isEmptyString(txtPassword.getText().toString());

        if (boolEmail) {
            loginEmail_InputLayout.setErrorEnabled(false);
        } else {
            loginEmail_InputLayout.setErrorEnabled(true);
            loginEmail_InputLayout.setError("Email field is wrong.");
        }

        if (boolPwd) {
            loginPwd_InputLayout.setErrorEnabled(false);
        } else {
            loginPwd_InputLayout.setErrorEnabled(true);
            loginPwd_InputLayout.setError("Password field is empty.");
        }

        if ( boolEmail && boolPwd ) {
            // Check if Internet present
            if (!isConnectingToInternet(SplashScreen.this)) {
                layout_noConnection.setVisibility(View.VISIBLE);
                // stop executing code by return
                return;
            } else {
                layout_noConnection.setVisibility(View.GONE);
                LoginCompanyForBuxa();
            }
        }
    }

    private void LoginCompanyForBuxa() {
        RequestParams params;
        params = new RequestParams();

        params.put("email", txtLogin.getText().toString());
        params.put("password", txtPassword.getText().toString());

        invokeWS(params);
    }

    private void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(CommonVariables.COMPANY_LOGIN_SERVER_URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                try {
                    JSONObject json = new JSONObject(response);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    if(error) {
                        Toast.makeText(getApplicationContext(), json.getString(CommonVariables.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                    else {
                        SetSharedPreference setSharedPreference = new SetSharedPreference(SplashScreen.this);

                        // it store the Register true value of user for purpose of user is registered with this app.
                        setSharedPreference.setBooleanLogin(getResources().getString(R.string.boolean_login_sharedPref), true);
                        setSharedPreference.setLoginId(getResources().getString(R.string.loginId), json.getInt("id"));
                        setSharedPreference.setApiKey(getResources().getString(R.string.apikey), json.getString("api_key"));
                        setSharedPreference.setLoginEmail(getResources().getString(R.string.loginEmail), json.getString("email"));
                        setSharedPreference.setLoginName(getResources().getString(R.string.loginName), json.getString("loginName"));

                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        SplashScreen.this.finish();
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    System.out.println("Requested resource not found");
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
