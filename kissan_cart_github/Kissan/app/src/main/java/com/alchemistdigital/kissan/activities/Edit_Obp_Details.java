package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.UpdateObpAsyncTask;
import com.alchemistdigital.kissan.model.OBP;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.SetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;
import com.andexert.library.RippleView;

import java.util.Date;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Edit_Obp_Details extends AppCompatActivity {

    private EditText txt_obp_name,txt_obp_contact,txt_obp_email,txt_obp_pwd,txt_obp_address;
    private EditText txt_obp_store_name,txt_obp_pin,txt_obp_city,txt_obp_state,txt_obp_country;
    private String  str_obp_name,str_obp_contact,str_obp_email,str_obp_pwd,str_obp_address;
    private String  str_obp_store_name,str_obp_pin,str_obp_city,str_obp_state,str_obp_country;
    private int status, localId;
    TextInputLayout obp_nameInputLayout,obp_contactInputLayout,obp_emailInputLayout,obp_pwdInputLayuot,obp_addressInputLayuot;
    TextInputLayout obp_store_nameInputLayout,obp_pinInputLayout,obp_cityInputLayout,obp_stateInputLayuot,obp_countryInputLayuot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__obp__details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_obp_toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        str_obp_name = extras.getString("name");
        str_obp_pwd = extras.getString("password");
        str_obp_store_name = extras.getString("store_name");
        str_obp_email = extras.getString("email_id");
        str_obp_contact = extras.getString("contact");
        str_obp_address = extras.getString("address");
        str_obp_pin = String.valueOf(extras.getInt("pincode"));
        str_obp_city = extras.getString("city");
        str_obp_state = extras.getString("state");
        str_obp_country = extras.getString("country");
        status = extras.getInt("status");
        localId = extras.getInt("localid");

        txt_obp_name        = (EditText) findViewById(R.id.edittext_id_edit_obp_name);
        txt_obp_contact     = (EditText) findViewById(R.id.edittext_id_edit_obp_contact);
        txt_obp_email       = (EditText) findViewById(R.id.edittext_id_edit_obp_email);
        txt_obp_pwd         = (EditText) findViewById(R.id.edittext_id_edit_obp_pwd);
        txt_obp_address     = (EditText) findViewById(R.id.edittext_id_edit_obp_address);
        txt_obp_store_name  = (EditText) findViewById(R.id.edittext_id_edit_obp_store_name);
        txt_obp_pin         = (EditText) findViewById(R.id.edittext_id_edit_obp_pin);
        txt_obp_city        = (EditText) findViewById(R.id.edittext_id_edit_obp_city);
        txt_obp_state       = (EditText) findViewById(R.id.edittext_id_edit_obp_state);
        txt_obp_country     = (EditText) findViewById(R.id.edittext_id_edit_obp_country);

        txt_obp_name.setText(str_obp_name);
        txt_obp_contact.setText(str_obp_contact);
        txt_obp_email.setText(str_obp_email);
        txt_obp_pwd.setText(str_obp_pwd);
        txt_obp_address.setText(str_obp_address);
        txt_obp_store_name.setText(str_obp_store_name);
        txt_obp_pin.setText(str_obp_pin);
        txt_obp_city.setText(str_obp_city);
        txt_obp_state.setText(str_obp_state);
        txt_obp_country.setText(str_obp_country);

        obp_nameInputLayout         = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_name);
        obp_contactInputLayout      = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_contact);
        obp_emailInputLayout        = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_email);
        obp_pwdInputLayuot          = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_pwd);
        obp_addressInputLayuot      = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_address);
        obp_store_nameInputLayout   = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_store_name);
        obp_pinInputLayout          = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_pin);
        obp_cityInputLayout         = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_city);
        obp_stateInputLayuot        = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_state);
        obp_countryInputLayuot      = (TextInputLayout) findViewById(R.id.id_input_layout_edit_obp_country);

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_edit_obp);
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

                if (boolAddress) {
                    obp_addressInputLayuot.setErrorEnabled(false);
                } else {
                    obp_addressInputLayuot.setErrorEnabled(true);
                    obp_addressInputLayuot.setError("OBP address field is empty.");
                }

                if (boolPwd) {
                    obp_pwdInputLayuot.setErrorEnabled(false);
                } else {
                    obp_pwdInputLayuot.setErrorEnabled(true);
                    obp_pwdInputLayuot.setError("OBP password field is empty.");
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

                if (boolCity){
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

                    GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Edit_Obp_Details.this);
                    int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));

                    // Check if Internet present
                    if (!isConnectingToInternet(Edit_Obp_Details.this)) {

                        OBP obpObj = new OBP(uId,
                                str_obp_name,
                                str_obp_store_name,
                                str_obp_email,
                                str_obp_pwd,
                                str_obp_contact,
                                str_obp_address,
                                Integer.parseInt(str_obp_pin),
                                str_obp_city,
                                str_obp_state,
                                str_obp_country,
                                status);

                        DatabaseHelper dbHelper = new DatabaseHelper(Edit_Obp_Details.this);
                        dbHelper.updateObpData(obpObj);

                        Offline offline = new Offline( dbHelper.TABLE_OBP,
                                localId,
                                offlineActionModeEnum.UPDATE.toString(),
                                ""+new Date().getTime() );

                        // it check whether data with same row id with update action in offline table.
                        // if yes delete old one and create new entry in offline table.
                        if (dbHelper.numberOfOfflineRowsByRowIdAndUpdate(localId) > 0) {
                            dbHelper.deleteOfflineTableDataByRowIdAndUpdate(String.valueOf(localId));
                        }
                        dbHelper.insertOffline(offline);

                        dbHelper.closeDB();

                        SetSharedPreferenceHelper setPreference = new SetSharedPreferenceHelper(Edit_Obp_Details.this);
                        // user register email
                        setPreference.setEmailPreference(getResources().getString(R.string.loginEmail), str_obp_email);
                        // user register name
                        setPreference.setNamePreference(getResources().getString(R.string.loginName), str_obp_name);

                        finish();

                    } else {

                        new UpdateObpAsyncTask(
                                Edit_Obp_Details.this,
                                uId,
                                str_obp_name,
                                str_obp_store_name,
                                str_obp_email,
                                str_obp_pwd,
                                str_obp_contact,
                                str_obp_address,
                                str_obp_pin,
                                str_obp_city,
                                str_obp_state,
                                str_obp_country,
                                status).execute();

                    }
                }
            }
        });

    }
}
