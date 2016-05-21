package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.UpdateVendorAsyncTask;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Vendor;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;
import com.andexert.library.RippleView;

import java.util.Date;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Edit_Vendor_Details extends AppCompatActivity {

    private EditText txt_vendor_name,txt_vendor_contact,txt_vendor_email,txt_vendor_address;
    private String  str_vendor_name,str_vendor_contact,str_vendor_email,str_vendor_address;
    TextInputLayout vendor_nameInputLayout,vendor_contactInputLayout,vendor_emailInputLayout,vendor_addressInputLayuot;
//    String oldVendorName;
    Vendor vendor_entity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__vendor__details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_vendor_toolbar);
        setSupportActionBar(toolbar);

        txt_vendor_name        = (EditText) findViewById(R.id.edittext_id_edit_vendor_name);
        txt_vendor_contact     = (EditText) findViewById(R.id.edittext_id_edit_vendor_contact);
        txt_vendor_email       = (EditText) findViewById(R.id.edittext_id_edit_vendor_email);
        txt_vendor_address     = (EditText) findViewById(R.id.edittext_id_edit_vendor_address);

        vendor_nameInputLayout     = (TextInputLayout) findViewById(R.id.id_input_layout_edit_vendor_name);
        vendor_contactInputLayout  = (TextInputLayout) findViewById(R.id.id_input_layout_edit_vendor_contact);
        vendor_emailInputLayout    = (TextInputLayout) findViewById(R.id.id_input_layout_edit_vendor_email);
        vendor_addressInputLayuot  = (TextInputLayout) findViewById(R.id.id_input_layout_edit_vendor_address);

        vendor_entity = getIntent().getParcelableExtra("Vendor_Entity");
        str_vendor_name = vendor_entity.vendor_name;
        str_vendor_email = vendor_entity.vendor_email;
        str_vendor_contact = vendor_entity.vendor_contact;
        str_vendor_address = vendor_entity.vendor_Address;

//        oldVendorName =  str_vendor_name;

        txt_vendor_name.setText(str_vendor_name);
        txt_vendor_contact.setText(str_vendor_contact);
        txt_vendor_email.setText(str_vendor_email);
        txt_vendor_address.setText(str_vendor_address);

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_edit_vendor);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_vendor_name = txt_vendor_name.getText().toString();
                str_vendor_contact = txt_vendor_contact.getText().toString();
                str_vendor_email = txt_vendor_email.getText().toString();
                str_vendor_address = txt_vendor_address.getText().toString();

                Boolean boolName = isEmptyString(str_vendor_name);
                Boolean boolContact = phoneValiate(str_vendor_contact);
                Boolean boolEmail = emailValidate(str_vendor_email);
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


                if (boolName && boolContact && boolEmail && boolAddress) {

                    // Check if Internet present
                    if (!isConnectingToInternet(Edit_Vendor_Details.this)) {

                        Vendor vendor = new Vendor();
                        vendor.setVendor_name(str_vendor_name);
                        vendor.setVendor_contact(str_vendor_contact);
                        vendor.setVendor_email(str_vendor_email);
                        vendor.setVendor_Address(str_vendor_address);
                        vendor.setServerId(vendor_entity.serverId);
                        vendor.setId(vendor_entity.id);
                        vendor.setVendor_status(vendor_entity.vendor_status);

                        DatabaseHelper dbhelper = new DatabaseHelper(Edit_Vendor_Details.this);

                        Offline offline = new Offline(dbhelper.TABLE_VENDORS,
                                vendor_entity.id,
                                offlineActionModeEnum.UPDATE.toString(),
                                "" + new Date().getTime());

                        // it check whether data with same row id with update action in offline table.
                        // if yes delete old one and create new entry in offline table.
                        if (dbhelper.numberOfOfflineRowsByRowIdAndUpdate(vendor_entity.id) > 0) {
                            dbhelper.deleteOfflineTableDataByRowIdAndUpdate(String.valueOf(vendor_entity.id));
                        }
                        dbhelper.insertOffline(offline);

                        dbhelper.updateVendor(vendor);
                        dbhelper.closeDB();

                        onBackPressed();

                    } else {

                        new UpdateVendorAsyncTask(Edit_Vendor_Details.this,
                                str_vendor_name,
                                str_vendor_contact,
                                str_vendor_email,
                                str_vendor_address,
//                                oldVendorName,
                                vendor_entity.serverId,
                                vendor_entity.vendor_status,
                                vendor_entity.id).execute();

                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Edit_Vendor_Details.this,View_Vendor.class);
        startActivity(intent);
        finish();
    }
}
