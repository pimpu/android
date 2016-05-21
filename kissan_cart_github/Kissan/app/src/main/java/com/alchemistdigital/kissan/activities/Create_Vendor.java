package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.InsertVendorAsyncTask;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Vendor;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;
import com.andexert.library.RippleView;

import java.util.Date;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Create_Vendor extends AppCompatActivity {

    private EditText txt_vendor_name,txt_vendor_contact,txt_vendor_email,txt_vendor_address;
    private String  str_vendor_name,str_vendor_contact,str_vendor_email,str_vendor_address;
    TextInputLayout vendor_nameInputLayout,vendor_contactInputLayout,vendor_emailInputLayout,vendor_addressInputLayuot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__vendor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_vendor_toolbar);
        setSupportActionBar(toolbar);

        txt_vendor_name        = (EditText) findViewById(R.id.edittext_id_vendor_name);
        txt_vendor_contact     = (EditText) findViewById(R.id.edittext_id_vendor_contact);
        txt_vendor_email       = (EditText) findViewById(R.id.edittext_id_vendor_email);
        txt_vendor_address     = (EditText) findViewById(R.id.edittext_id_vendor_address);

        vendor_nameInputLayout         = (TextInputLayout) findViewById(R.id.id_input_layout_vendor_name);
        vendor_contactInputLayout      = (TextInputLayout) findViewById(R.id.id_input_layout_vendor_contact);
        vendor_emailInputLayout        = (TextInputLayout) findViewById(R.id.id_input_layout_vendor_email);
        vendor_addressInputLayuot      = (TextInputLayout) findViewById(R.id.id_input_layout_vendor_address);

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_submit_vendor);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_vendor_name        = txt_vendor_name.getText().toString();
                str_vendor_contact     = txt_vendor_contact.getText().toString();
                str_vendor_email       = txt_vendor_email.getText().toString();
                str_vendor_address     = txt_vendor_address.getText().toString();

                Boolean boolName    = isEmptyString(str_vendor_name);
                Boolean boolContact = phoneValiate(str_vendor_contact);
                Boolean boolEmail   = emailValidate(str_vendor_email);
                Boolean boolAddress = isEmptyString(str_vendor_address);

                if (boolName) {
                    vendor_nameInputLayout.setErrorEnabled(false);
                } else {
                    vendor_nameInputLayout.setErrorEnabled(true);
                    vendor_nameInputLayout.setError("Vendor name field is empty.");
                }

                if (boolContact) {
                    vendor_contactInputLayout.setErrorEnabled(false);
                } else {
                    vendor_contactInputLayout.setErrorEnabled(true);
                    vendor_contactInputLayout.setError("Vendor contact field is empty.");
                }

                if (boolEmail) {
                    vendor_emailInputLayout.setErrorEnabled(false);
                } else {
                    vendor_emailInputLayout.setErrorEnabled(true);
                    vendor_emailInputLayout.setError("You need to enter correct email-id");
                }

                if (boolAddress) {
                    vendor_addressInputLayuot.setErrorEnabled(false);
                } else {
                    vendor_addressInputLayuot.setErrorEnabled(true);
                    vendor_addressInputLayuot.setError("Vendor address field is empty.");
                }

                if ( boolName && boolContact && boolEmail && boolAddress ) {

                    // Check if Internet present
                    if (!isConnectingToInternet(Create_Vendor.this)) {

                        Vendor vendorObj = new Vendor(
                                0,
                                1,
                                str_vendor_name,
                                str_vendor_contact,
                                str_vendor_address,
                                str_vendor_email);

                        DatabaseHelper dbHelper = new DatabaseHelper(Create_Vendor.this);
                        if( dbHelper.isVendorPresent(str_vendor_email) > 0 ) {
                            Toast.makeText(getApplicationContext(), "This vendor's email-id is already exist.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            long vendorId = dbHelper.insertVendorData(vendorObj);

                            Offline offline = new Offline(
                                    dbHelper.TABLE_VENDORS,
                                    (int) vendorId,
                                    offlineActionModeEnum.INSERT.toString(),
                                    ""+new Date().getTime() );

                            dbHelper.insertOffline(offline);

                            dbHelper.closeDB();
                            onBackPressed();
                        }

                    } else {
                        DatabaseHelper dbHelper = new DatabaseHelper(Create_Vendor.this);
                        if( dbHelper.isVendorPresent(str_vendor_email) > 0 ) {
                            Toast.makeText(getApplicationContext(), "This vendor's email-id is already exist.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            new InsertVendorAsyncTask(Create_Vendor.this,str_vendor_name,str_vendor_contact,str_vendor_email,
                                    str_vendor_address).execute();
                        }

                    }
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Create_Vendor.this, View_Vendor.class));
        finish();
    }
}
