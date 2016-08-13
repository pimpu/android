package com.alchemistdigital.buxa.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.emailValidate;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;
import static com.alchemistdigital.buxa.utilities.Validations.phoneValiate;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinnerState;
    EditText txtCode, txtCompanyName, txtAddress, txtLandmark, txtCity, txtZipcode,
            txtPan, txtTin, txtContactName, txtMobile, txtEmailId, txtPwd, txtConformPwd;
    Button btnRegister;
    String strState;
    TextInputLayout companyName_InputLayout, address_InputLayout, landmark_InputLayout, city_InputLayout, zipcode_InputLayout,
            pan_InputLayout, tin_InputLayout, contactName_InputLayout, mobile_InputLayout, emailId_InputLayout,
            pwd_InputLayout, conformPwd_InputLayout;
    private final static SimpleDateFormat noFormatDateSdf = new SimpleDateFormat("ddMMyyhhmmss");
    // Progress Dialog Object
    ProgressDialog prgDialog;

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

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Registering ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        // sort state array list
        String[] statesArray = getResources().getStringArray(R.array.state);
        List<String> planetsList = Arrays.asList(statesArray);
        Collections.sort(planetsList);

        spinnerState = (Spinner) findViewById(R.id.spinner_id_state);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, planetsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapter);
        spinnerState.setOnItemSelectedListener(this);

        txtCode = (EditText) findViewById(R.id.company_code);
        Date date = new Date();
        txtCode.setText(getResources().getString(R.string.codeString, noFormatDateSdf.format(date)));

        txtCompanyName = (EditText) findViewById(R.id.company_name);
        txtAddress = (EditText) findViewById(R.id.company_address);
        txtLandmark = (EditText) findViewById(R.id.company_landmark);
        txtCity = (EditText) findViewById(R.id.company_city);
        txtZipcode = (EditText) findViewById(R.id.company_zipcode);
        txtPan = (EditText) findViewById(R.id.company_pan);
        txtTin = (EditText) findViewById(R.id.company_tin);
        txtContactName = (EditText) findViewById(R.id.contact_name);
        txtMobile = (EditText) findViewById(R.id.contact_mobile);
        txtEmailId = (EditText) findViewById(R.id.contact_email);
        txtPwd = (EditText) findViewById(R.id.contact_pwd);
        txtConformPwd = (EditText) findViewById(R.id.contact_conform_pwd);

        companyName_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_name);
        address_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_address);
        landmark_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_landmark);
        city_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_city);
        zipcode_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_zipcode);
        pan_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_pan);
        tin_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_company_tin);
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
                Boolean boolAdd = isEmptyString(txtAddress.getText().toString());
                Boolean boolLandmark = isEmptyString(txtLandmark.getText().toString());
                Boolean boolCity = isEmptyString(txtCity.getText().toString());
                Boolean booZipcode = isEmptyString(txtZipcode.getText().toString());
                Boolean boolPan = isEmptyString(txtPan.getText().toString());
                Boolean boolTin = isEmptyString(txtTin.getText().toString());
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

                if (boolAdd) {
                    address_InputLayout.setErrorEnabled(false);
                } else {
                    address_InputLayout.setErrorEnabled(true);
                    address_InputLayout.setError("Address field is empty.");
                }

                if (boolLandmark) {
                    landmark_InputLayout.setErrorEnabled(false);
                } else {
                    landmark_InputLayout.setErrorEnabled(true);
                    landmark_InputLayout.setError("Landmark field is empty.");
                }

                if (boolCity) {
                    city_InputLayout.setErrorEnabled(false);
                } else {
                    city_InputLayout.setErrorEnabled(true);
                    city_InputLayout.setError("City field is empty.");
                }

                if (booZipcode) {
                    zipcode_InputLayout.setErrorEnabled(false);
                } else {
                    zipcode_InputLayout.setErrorEnabled(true);
                    zipcode_InputLayout.setError("Zipcode field is empty.");
                }

                if (boolPan) {
                    pan_InputLayout.setErrorEnabled(false);
                } else {
                    pan_InputLayout.setErrorEnabled(true);
                    pan_InputLayout.setError("Pin field is empty.");
                }

                if (boolTin) {
                    tin_InputLayout.setErrorEnabled(false);
                } else {
                    tin_InputLayout.setErrorEnabled(true);
                    tin_InputLayout.setError("Tin field is empty.");
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

                if (boolCompanyName && boolAdd && boolLandmark && boolCity && booZipcode && boolPan
                        && boolTin && boolContactName && boolMob && boolEmail && boolPwd && boolConformPwd) {

                    if (txtPwd.getText().toString().equals(txtConformPwd.getText().toString()) &&
                            txtConformPwd.getText().toString().equals(txtPwd.getText().toString())) {

                        conformPwd_InputLayout.setErrorEnabled(false);

                        // Check if Internet present
                        if (!isConnectingToInternet(RegisterActivity.this)) {
                            // Internet Connection is not present
                            Snackbar.make(findViewById(R.id.id_xmlRegister), "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                                    .show();
                            // stop executing code by return
                            return;
                        } else {

                            /*System.out.println("Code: "+ txtCode.getText().toString() );
                            System.out.println("cmp name: "+ txtCompanyName.getText().toString() );
                            System.out.println("address: "+ txtAddress.getText().toString() );
                            System.out.println("landmark: "+ txtLandmark.getText().toString() );
                            System.out.println("city: "+ txtCity.getText().toString() );
                            System.out.println("zipcode: "+ txtZipcode.getText().toString() );
                            System.out.println("pan: "+ txtPan.getText().toString() );
                            System.out.println("tin: "+ txtTin.getText().toString() );
                            System.out.println("contact name: "+ txtContactName.getText().toString() );
                            System.out.println("mobile: "+ txtMobile.getText().toString() );
                            System.out.println("email: "+ txtEmailId.getText().toString() );
                            System.out.println("pwd: "+ txtPwd.getText().toString() );
                            System.out.println("confirm pwd: " + txtConformPwd.getText().toString());*/

                        /*new InsertCompanyRegisteration(
                                RegisterActivity.this,
                                txtCode.getText().toString(),
                                txtCompanyName.getText().toString(),
                                txtAddress.getText().toString(),
                                txtLandmark.getText().toString(),
                                txtCity.getText().toString(),
                                txtZipcode.getText().toString(),
                                txtPan.getText().toString(),
                                txtTin.getText().toString(),
                                txtContactName.getText().toString(),
                                txtMobile.getText().toString(),
                                txtEmailId.getText().toString(),
                                txtPwd.getText().toString(),
                                strState).execute();*/

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
        params.put("address", txtAddress.getText().toString());
        params.put("landmark", txtLandmark.getText().toString());
        params.put("country", "India");
        params.put("city", txtCity.getText().toString());
        params.put("zipcode", txtZipcode.getText().toString());
        params.put("pan", txtPan.getText().toString());
        params.put("tin", txtTin.getText().toString());
        params.put("uname", txtContactName.getText().toString());
        params.put("mobile", txtMobile.getText().toString());
        params.put("email", txtEmailId.getText().toString());
        params.put("password", txtPwd.getText().toString());
        params.put("state", strState);
        params.put("create_time", System.currentTimeMillis());

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
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(CommonVariables.COMPANY_REGISTER_SERVER_URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);
                    System.out.println(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    System.out.println("Requested resource not found");
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    System.out.println("Something went wrong at server end");
                }
                // When Http response code other than 404, 500
                else{
                    System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Login.class));
        finish();
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        String item = parent.getItemAtPosition(position).toString();

        if (spinner.getId() == R.id.spinner_id_state) {
            strState = item;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
