package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.InsertOBPAsyncTask;
import com.andexert.library.RippleView;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Create_OBP extends AppCompatActivity {

    private EditText txt_obp_name,txt_obp_contact,txt_obp_email,txt_obp_pwd,txt_obp_address;
    private EditText txt_obp_store_name,txt_obp_pin,txt_obp_city,txt_obp_state,txt_obp_country;
    private String  str_obp_name,str_obp_contact,str_obp_email,str_obp_pwd,str_obp_address;
    private String  str_obp_store_name,str_obp_pin,str_obp_city,str_obp_state,str_obp_country;
    TextInputLayout obp_nameInputLayout,obp_contactInputLayout,obp_emailInputLayout,obp_pwdInputLayuot,obp_addressInputLayuot;
    TextInputLayout obp_store_nameInputLayout,obp_pinInputLayout,obp_cityInputLayout,obp_stateInputLayuot,obp_countryInputLayuot;
    private View createObpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__obp);

        createObpView = findViewById(R.id.id_CreateOBPView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_obp_toolbar);
        setSupportActionBar(toolbar);

        txt_obp_name        = (EditText) findViewById(R.id.edittext_id_obp_name);
        txt_obp_contact     = (EditText) findViewById(R.id.edittext_id_obp_contact);
        txt_obp_email       = (EditText) findViewById(R.id.edittext_id_obp_email);
        txt_obp_pwd         = (EditText) findViewById(R.id.edittext_id_obp_pwd);
        txt_obp_address     = (EditText) findViewById(R.id.edittext_id_obp_address);
        txt_obp_store_name  = (EditText) findViewById(R.id.edittext_id_obp_store_name);
        txt_obp_pin         = (EditText) findViewById(R.id.edittext_id_obp_store_pin);
        txt_obp_city        = (EditText) findViewById(R.id.edittext_id_obp_store_city);
        txt_obp_state       = (EditText) findViewById(R.id.edittext_id_obp_store_state);
        txt_obp_country     = (EditText) findViewById(R.id.edittext_id_obp_store_country);

        obp_nameInputLayout         = (TextInputLayout) findViewById(R.id.id_input_layout_obp_name);
        obp_contactInputLayout      = (TextInputLayout) findViewById(R.id.id_input_layout_obp_contact);
        obp_emailInputLayout        = (TextInputLayout) findViewById(R.id.id_input_layout_obp_email);
        obp_pwdInputLayuot          = (TextInputLayout) findViewById(R.id.id_input_layout_obp_pwd);
        obp_addressInputLayuot      = (TextInputLayout) findViewById(R.id.id_input_layout_obp_address);
        obp_store_nameInputLayout   = (TextInputLayout) findViewById(R.id.id_input_layout_obp_store_name);
        obp_pinInputLayout          = (TextInputLayout) findViewById(R.id.id_input_layout_obp_store_pin);
        obp_cityInputLayout         = (TextInputLayout) findViewById(R.id.id_input_layout_obp_store_city);
        obp_stateInputLayuot        = (TextInputLayout) findViewById(R.id.id_input_layout_obp_store_state);
        obp_countryInputLayuot      = (TextInputLayout) findViewById(R.id.id_input_layout_obp_store_country);

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_submit_obp);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_obp_name        = txt_obp_name.getText().toString();
                str_obp_contact     = txt_obp_contact.getText().toString();
                str_obp_email       = txt_obp_email.getText().toString();
                str_obp_pwd         = txt_obp_pwd.getText().toString();
                str_obp_address     = txt_obp_address.getText().toString();
                str_obp_store_name  = txt_obp_store_name.getText().toString();
                str_obp_pin         = txt_obp_pin.getText().toString();
                str_obp_city        = txt_obp_city.getText().toString();
                str_obp_state       = txt_obp_state.getText().toString();
                str_obp_country     = txt_obp_country.getText().toString();

                Boolean boolName    = isEmptyString(str_obp_name);
                Boolean boolContact = phoneValiate(str_obp_contact);
                Boolean boolEmail   = emailValidate(str_obp_email);
                Boolean boolPwd     = isEmptyString(str_obp_pwd);
                Boolean boolAddress = isEmptyString(str_obp_address);
                Boolean boolStore   = isEmptyString(str_obp_store_name);
                Boolean boolPin     = isEmptyString(str_obp_pin);
                Boolean boolCity    = isEmptyString(str_obp_city);
                Boolean boolState   = isEmptyString(str_obp_state);
                Boolean boolCountry = isEmptyString(str_obp_country);

                if (boolName) {
                    obp_nameInputLayout.setErrorEnabled(false);
                } else {
                    obp_nameInputLayout.setErrorEnabled(true);
                    obp_nameInputLayout.setError("OBP name field is empty.");
                }

                if (boolContact) {
                    obp_contactInputLayout.setErrorEnabled(false);
                } else {
                    obp_contactInputLayout.setErrorEnabled(true);
                    obp_contactInputLayout.setError("OBP contact field is empty.");
                }

                if (boolEmail) {
                    obp_emailInputLayout.setErrorEnabled(false);
                } else {
                    obp_emailInputLayout.setErrorEnabled(true);
                    obp_emailInputLayout.setError("You need to enter correct email-id");
                }

                if (boolPwd) {
                    obp_pwdInputLayuot.setErrorEnabled(false);
                } else {
                    obp_pwdInputLayuot.setErrorEnabled(true);
                    obp_pwdInputLayuot.setError("OBP password field is empty.");
                }

                if (boolAddress) {
                    obp_addressInputLayuot.setErrorEnabled(false);
                } else {
                    obp_addressInputLayuot.setErrorEnabled(true);
                    obp_addressInputLayuot.setError("OBP address field is empty.");
                }

                if (boolStore) {
                    obp_store_nameInputLayout.setErrorEnabled(false);
                } else {
                    obp_store_nameInputLayout.setErrorEnabled(true);
                    obp_store_nameInputLayout.setError("Store name field is empty.");
                }

                if (boolPin) {
                    obp_pinInputLayout.setErrorEnabled(false);
                } else {
                    obp_pinInputLayout.setErrorEnabled(true);
                    obp_pinInputLayout.setError("Store pincode field is empty.");
                }

                if (boolCity) {
                    obp_cityInputLayout.setErrorEnabled(false);
                } else {
                    obp_cityInputLayout.setErrorEnabled(true);
                    obp_cityInputLayout.setError("Store city field is empty.");
                }

                if (boolState) {
                    obp_stateInputLayuot.setErrorEnabled(false);
                } else {
                    obp_stateInputLayuot.setErrorEnabled(true);
                    obp_stateInputLayuot.setError("Store state field is empty.");
                }

                if (boolCountry) {
                    obp_countryInputLayuot.setErrorEnabled(false);
                } else {
                    obp_countryInputLayuot.setErrorEnabled(true);
                    obp_countryInputLayuot.setError("Store country field is empty.");
                }

                if ( boolName && boolContact && boolEmail && boolPwd && boolAddress && boolStore
                        && boolPin && boolCity && boolState && boolCountry) {

                    // Check if Internet present
                    if (!isConnectingToInternet(Create_OBP.this)) {
                        // Internet Connection is not present
                        Snackbar.make(createObpView, "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onCreate(null);
                                    }
                                }).show();
                        // stop executing code by return
                        return;
                    } else {
                        new InsertOBPAsyncTask(Create_OBP.this,str_obp_name,str_obp_contact,str_obp_email,
                                str_obp_pwd,str_obp_address,str_obp_store_name,str_obp_pin,str_obp_city,
                                str_obp_state,str_obp_country).execute();
                    }
                }
            }
        });
    }
}
