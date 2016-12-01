package com.alchemistdigital.buxa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.asynctask.InsertInternationalDestinationPort;
import com.alchemistdigital.buxa.asynctask.RegisterCompanyAsyncTask;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;

public class EmailConformationActivity extends AppCompatActivity {
    EditText txtFirstOTP, txtSecondOTP, txtThirdOTP, txtFourthOTP;
    private String company, uname, mobile, email, password, create_time, otp;
    TextView tvEmailText, tvErrConfrm;
    LinearLayout layout_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_conformation);

        txtFirstOTP = (EditText) findViewById(R.id.firstOTP);
        txtSecondOTP = (EditText) findViewById(R.id.secondOTP);
        txtThirdOTP = (EditText) findViewById(R.id.thirdOTP);
        txtFourthOTP = (EditText) findViewById(R.id.fourthOTP);

        tvEmailText = (TextView) findViewById(R.id.id_email_cnfm);
        tvErrConfrm = (TextView) findViewById(R.id.cnfrm_error_msg);

        layout_msg = (LinearLayout) findViewById(R.id.id_noInternet_cnfrm);

        company = getIntent().getExtras().getString("company");
        uname = getIntent().getExtras().getString("uname");
        mobile = getIntent().getExtras().getString("mobile");
        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        create_time = getIntent().getExtras().getString("create_time");
        otp = getIntent().getExtras().getString("otp");

        tvEmailText.setText(email);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        txtFirstOTP.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(txtFirstOTP.getText().toString().length()== 1 ) {
                    txtSecondOTP.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

        });

        txtSecondOTP.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(txtSecondOTP.getText().toString().length()== 1 ) {
                    txtThirdOTP.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

        });

        txtThirdOTP.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(txtThirdOTP.getText().toString().length()== 1 ) {
                    txtFourthOTP.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

        });

        registerReceiver(mHandleServerMessageReceiverInRegisterActivity, new IntentFilter(
                CommonVariables.DISPLAY_MESSAGE_ACTION));
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

                Intent intentServicesActivity = new Intent(EmailConformationActivity.this, WelcomeActivity.class);
                intentServicesActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intentServicesActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentServicesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((EmailConformationActivity)context).finish();
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

    public void btnConformOTP(View view) {
        String fOtp = txtFirstOTP.getText().toString();
        String sOtp = txtSecondOTP.getText().toString();
        String tOtp = txtThirdOTP.getText().toString();
        String foOtp = txtFourthOTP.getText().toString();

        // OTP match
        if(otp.equals(fOtp+sOtp+tOtp+foOtp)) {

            // Check if Internet present
            if (!isConnectingToInternet(EmailConformationActivity.this)) {
                layout_msg.setVisibility(View.VISIBLE);
                tvErrConfrm.setText(getResources().getString(R.string.strNoConnection));
                // stop executing code by return
                return;
            } else {
                layout_msg.setVisibility(View.GONE);
                View view1 = EmailConformationActivity.this.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)EmailConformationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }

                new RegisterCompanyAsyncTask(
                        EmailConformationActivity.this,
                        company,
                        uname,
                        mobile,
                        email,
                        password,
                        create_time,
                        layout_msg,
                        tvErrConfrm)
                .registerCompany();

            }

        }
        else {
            layout_msg.setVisibility(View.VISIBLE);
            tvErrConfrm.setText("OTP did not match");
        }
    }

}
