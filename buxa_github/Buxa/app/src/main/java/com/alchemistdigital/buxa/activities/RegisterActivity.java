package com.alchemistdigital.buxa.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.adapter.EmailListAdapter;
import com.alchemistdigital.buxa.asynctask.InsertInternationalDestinationPort;
import com.alchemistdigital.buxa.model.Email_Account_Item;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.emailValidate;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;
import static com.alchemistdigital.buxa.utilities.Validations.phoneValiate;

public class RegisterActivity extends AppCompatActivity {
    EditText txtCompanyName, txtContactName, txtMobile, txtPwd, txtConformPwd;
    Button btnRegister;
    TextInputLayout companyName_InputLayout, contactName_InputLayout, mobile_InputLayout,
            pwd_InputLayout, conformPwd_InputLayout;
    private final static SimpleDateFormat noFormatDateSdf = new SimpleDateFormat("ddMMyyhhmmss");
    // Progress Dialog Object
    ProgressDialog prgDialog;
    LinearLayout layout_noConnection;
    TextView errorMessage, tvRegisterEmail;
    private ArrayList<Email_Account_Item> list;
    private ListView listView;
    private EmailListAdapter listadaptor;
    private static final int PICK_ACCOUNT_REQUEST = 0;

    private static final String TAG = "PERMISSION: ";
    private static final int REQUEST_GET_ACCOUNT = 1;
    String[] PERMISSIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_registerform);
        setSupportActionBar(toolbar);

        // set back button on toolbar
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
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


        errorMessage = (TextView) findViewById(R.id.register_error_msg);
        txtCompanyName = (EditText) findViewById(R.id.company_name);
        txtContactName = (EditText) findViewById(R.id.contact_name);
        txtMobile = (EditText) findViewById(R.id.contact_mobile);
//        txtEmailId = (EditText) findViewById(R.id.contact_email);
        txtPwd = (EditText) findViewById(R.id.contact_pwd);
        txtConformPwd = (EditText) findViewById(R.id.contact_conform_pwd);

        tvRegisterEmail = (TextView) findViewById(R.id.contact_email);

        companyName_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_name);
        contactName_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_name);
        mobile_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_mobile);
//        emailId_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_email);
        pwd_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_pwd);
        conformPwd_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_contact_conform_pwd);

        registerReceiver(mHandleServerMessageReceiverInRegisterActivity, new IntentFilter(
                CommonVariables.DISPLAY_MESSAGE_ACTION));


        PERMISSIONS = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.VIBRATE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_GET_ACCOUNT);
        }
        else {
            // get emails addresses which are registered on mobile device
            list = CommonUtilities.getEmailsData(RegisterActivity.this);
            if( list.size() == 1 ){
                tvRegisterEmail.setText( list.get(0).getName() );
            }
        }



        btnRegister = (Button) findViewById(R.id.id_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean boolCompanyName = isEmptyString(txtCompanyName.getText().toString());
                Boolean boolContactName = isEmptyString(txtContactName.getText().toString());
                Boolean boolMob = phoneValiate(txtMobile.getText().toString());
//                Boolean boolEmail = emailValidate(txtEmailId.getText().toString());
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

                /*if (boolEmail) {
                    emailId_InputLayout.setErrorEnabled(false);
                } else {
                    emailId_InputLayout.setErrorEnabled(true);
                    emailId_InputLayout.setError("Email field is wrong.");
                }*/

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

                if (boolCompanyName && boolContactName && boolMob && boolPwd && boolConformPwd) {

                    if(tvRegisterEmail.getText().toString().equals("Click on me for Email")) {
                        Toast.makeText(getApplicationContext(), "Please, select contact email id.", Toast.LENGTH_LONG).show();
                        return;
                    }

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
                            View view = RegisterActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)RegisterActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
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

//        params.put("code", txtCode.getText().toString());
        params.put("company", txtCompanyName.getText().toString());
        params.put("uname", txtContactName.getText().toString());
        params.put("mobile", txtMobile.getText().toString());
        params.put("email", tvRegisterEmail.getText().toString());
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
        RestClient.post(CommonVariables.COMPANY_REGISTER_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.cancel();
                try {
//                    JSONObject json = new JSONObject(response);

                    Boolean error = response.getBoolean(CommonVariables.TAG_ERROR);
                    if (error) {
                        layout_noConnection.setVisibility(View.VISIBLE);
                        errorMessage.setText(response.getString(CommonVariables.TAG_MESSAGE));
                    } else {
                        layout_noConnection.setVisibility(View.GONE);
                        SetSharedPreference setSharedPreference = new SetSharedPreference(RegisterActivity.this);

                        setSharedPreference.setLoginId(getResources().getString(R.string.loginId), response.getInt("id"));
                        setSharedPreference.setApiKey(getResources().getString(R.string.apikey), response.getString("api_key"));
                        setSharedPreference.setLoginEmail(getResources().getString(R.string.loginEmail), response.getString("email"));
                        setSharedPreference.setLoginName(getResources().getString(R.string.loginName), response.getString("loginName"));
                        setSharedPreference.setCompanyName(getResources().getString(R.string.companyName), response.getString("companyName"));

                        // create shortcut when user Register with this app.
                        CommonUtilities.addShortcut(RegisterActivity.this);

                        DatabaseClass dbHelper = new DatabaseClass(RegisterActivity.this);
                        if (dbHelper.numberOfComodityRows() <= 0 ) {
                            setContentView(R.layout.activity_splash_screen);

                            new InsertInternationalDestinationPort(RegisterActivity.this).execute();

                        }
                        /*else {
                            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            RegisterActivity.this.finish();
                            startActivity(intent);
                        }*/

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleServerMessageReceiverInRegisterActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            // this message is come from GetAllPackageType when all default value from server get finished.
            if(newMessage.equals("allDefaultDataFetched")) {

                // gcm successfully registered
                // now subscribe to `global` topic to receive app wide notifications
                FirebaseMessaging.getInstance().subscribeToTopic(CommonVariables.TOPIC_GLOBAL);

                Intent intentServicesActivity = new Intent(RegisterActivity.this, WelcomeActivity.class);
                intentServicesActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                ((RegisterActivity)context).finish();
                startActivity(intentServicesActivity);
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleServerMessageReceiverInRegisterActivity);
        } catch (Exception e) {
            System.err.println("UnRegisterReceiverError > " + e.getMessage());
        }
        super.onDestroy();
    }

    public void getRegisteredEmail(View view) {
        if(list == null){
            Toast.makeText(getApplicationContext(), "Please, Enable contact permission for registering with Buxa.",Toast.LENGTH_LONG).show();
            return;
        }
        if (list.size() > 0) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
            dialogBuilder.setCancelable(true);
            View inflaterOfEmails = getLayoutInflater().inflate(R.layout.email_name_listview, null);
            dialogBuilder.setView(inflaterOfEmails);
            AlertDialog alertDialog = dialogBuilder.create();

            listView = (ListView) inflaterOfEmails.findViewById(R.id.idEmailName);
            listadaptor = new EmailListAdapter(RegisterActivity.this,
                    R.layout.email_account_name_view,
                    list,
                    tvRegisterEmail,
                    alertDialog);
            listView.setAdapter(listadaptor);

            alertDialog.show();
        } else {
            Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
            startActivityForResult(googlePicker, PICK_ACCOUNT_REQUEST);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PICK_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
            tvRegisterEmail.setText(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GET_ACCOUNT:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission has been denied by user");
                    Toast.makeText(getApplicationContext(), "Permission require for registering with Buxa.",Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Permission has been granted by user");

                    // get emails addresses which are registered on mobile device
                    list = CommonUtilities.getEmailsData(RegisterActivity.this);
                    if( list.size() == 1 ){
                        tvRegisterEmail.setText( list.get(0).getName() );
                    }
                }
                break;
        }
    }

}
