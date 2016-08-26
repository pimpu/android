package com.alchemistdigital.buxa.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.emailValidate;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;
import static com.alchemistdigital.buxa.utilities.Validations.phoneValiate;

public class RegisterActivity extends AppCompatActivity {
    EditText txtCode, txtCompanyName, txtContactName, txtMobile, txtEmailId, txtPwd, txtConformPwd;
    Button btnRegister;
    TextInputLayout companyName_InputLayout, contactName_InputLayout, mobile_InputLayout, emailId_InputLayout,
            pwd_InputLayout, conformPwd_InputLayout;
    private final static SimpleDateFormat noFormatDateSdf = new SimpleDateFormat("ddMMyyhhmmss");
    // Progress Dialog Object
    ProgressDialog prgDialog;
    LinearLayout layout_noConnection;
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_registerform);
        setSupportActionBar(toolbar);

        // set back button on toolbar
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        // set click listener on back button of toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Buxa Registration");

        layout_noConnection = (LinearLayout) findViewById(R.id.id_noInternet_register);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Registering ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        txtCode = (EditText) findViewById(R.id.company_code);
        Date date = new Date();
        txtCode.setText(getResources().getString(R.string.codeString, noFormatDateSdf.format(date)));

        errorMessage = (TextView) findViewById(R.id.register_error_msg);
        txtCompanyName = (EditText) findViewById(R.id.company_name);
        txtContactName = (EditText) findViewById(R.id.contact_name);
        txtMobile = (EditText) findViewById(R.id.contact_mobile);
        txtEmailId = (EditText) findViewById(R.id.contact_email);
        txtPwd = (EditText) findViewById(R.id.contact_pwd);
        txtConformPwd = (EditText) findViewById(R.id.contact_conform_pwd);

        companyName_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_name);
        contactName_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_name);
        mobile_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_mobile);
        emailId_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_email);
        pwd_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_pwd);
        conformPwd_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_conform_pwd);

        btnRegister = (Button) findViewById(R.id.id_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean boolCompanyName = isEmptyString(txtCompanyName.getText().toString());
                Boolean boolContactName = isEmptyString(txtContactName.getText().toString());
                Boolean boolMob = phoneValiate(txtMobile.getText().toString());
                Boolean boolEmail = emailValidate(txtEmailId.getText().toString());
                Boolean boolPwd = isEmptyString(txtPwd.getText().toString());
                Boolean boolConformPwd = isEmptyString(txtConformPwd.getText().toString());

                if (boolCompanyName) {
                    companyName_InputLayout.setErrorEnabled(false);
                } else {
                    companyName_InputLayout.setErrorEnabled(true);
                    companyName_InputLayout.setError("Company name field is empty.");
                }

                if (boolContactName) {
                    contactName_InputLayout.setErrorEnabled(false);
                } else {
                    contactName_InputLayout.setErrorEnabled(true);
                    contactName_InputLayout.setError("Contact name field is empty.");
                }

                if (boolMob) {
                    mobile_InputLayout.setErrorEnabled(false);
                } else {
                    mobile_InputLayout.setErrorEnabled(true);
                    mobile_InputLayout.setError("Mobile field is wrong.");
                }

                if (boolEmail) {
                    emailId_InputLayout.setErrorEnabled(false);
                } else {
                    emailId_InputLayout.setErrorEnabled(true);
                    emailId_InputLayout.setError("Email field is wrong.");
                }

                if (boolPwd) {
                    pwd_InputLayout.setErrorEnabled(false);
                } else {
                    pwd_InputLayout.setErrorEnabled(true);
                    pwd_InputLayout.setError("Password field is empty.");
                }

                if (boolConformPwd) {
                    conformPwd_InputLayout.setErrorEnabled(false);
                } else {
                    conformPwd_InputLayout.setErrorEnabled(true);
                    conformPwd_InputLayout.setError("Confirm password field is empty.");
                }

                if (boolCompanyName && boolContactName && boolMob && boolEmail && boolPwd && boolConformPwd) {

                    if (txtPwd.getText().toString().equals(txtConformPwd.getText().toString()) &&
                            txtConformPwd.getText().toString().equals(txtPwd.getText().toString())) {

                        conformPwd_InputLayout.setErrorEnabled(false);

                        // Check if Internet present
                        if (!isConnectingToInternet(RegisterActivity.this)) {
                            layout_noConnection.setVisibility(View.VISIBLE);
                            errorMessage.setText(getResources().getString(R.string.strNoConnection));
                            // stop executing code by return
                            return;
                        } else {
                            layout_noConnection.setVisibility(View.GONE);
                            registerCompanyForBuxa();
                        }
                    } else {
                        conformPwd_InputLayout.setErrorEnabled(true);
                        conformPwd_InputLayout.setError("Confirm password doesn't match.");
                    }
                }
            }
        });

    }

    private void registerCompanyForBuxa() {
        // initialised parameter object for server call
        RequestParams params;
        params = new RequestParams();

        params.put("code", txtCode.getText().toString());
        params.put("company", txtCompanyName.getText().toString());
        params.put("uname", txtContactName.getText().toString());
        params.put("mobile", txtMobile.getText().toString());
        params.put("email", txtEmailId.getText().toString());
        params.put("password", txtPwd.getText().toString());
        params.put("create_time", ""+System.currentTimeMillis());

        invokeWS(params);
    }

    /**
     * Method that performs RESTful webservice invocations
     * @param params
     */
    private void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.COMPANY_REGISTER_SERVER_URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                try {
                    JSONObject json = new JSONObject(response);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    if (error) {
                        layout_noConnection.setVisibility(View.VISIBLE);
                        errorMessage.setText(json.getString(CommonVariables.TAG_MESSAGE));
                    } else {
                        layout_noConnection.setVisibility(View.GONE);
                        SetSharedPreference setSharedPreference = new SetSharedPreference(RegisterActivity.this);

                        // it store the Register true value of user for purpose of user is registered with this app.
                        setSharedPreference.setBooleanLogin(getResources().getString(R.string.boolean_login_sharedPref), "true");
                        setSharedPreference.setLoginId(getResources().getString(R.string.loginId), json.getInt("id"));
                        setSharedPreference.setApiKey(getResources().getString(R.string.apikey), json.getString("api_key"));
                        setSharedPreference.setLoginEmail(getResources().getString(R.string.loginEmail), json.getString("email"));
                        setSharedPreference.setLoginName(getResources().getString(R.string.loginName), json.getString("loginName"));
                        setSharedPreference.setCompanyName(getResources().getString(R.string.companyName), json.getString("companyName"));

                        // create shortcut when user Register with this app.
                        CommonUtilities.addShortcut(RegisterActivity.this);

                        Intent intent = new Intent(RegisterActivity.this, SelectServiceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        RegisterActivity.this.finish();
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

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartupActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
