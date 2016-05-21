package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.UpdateSocietyAsyncTask;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;
import com.andexert.library.RippleView;

import java.util.Date;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Edit_Society_Details extends AppCompatActivity {

    private View editSocietyView;
    private EditText txt_society_name,txt_society_contact,txt_society_email,txt_society_address;
    private String  str_society_name,str_society_contact,str_society_email,str_society_address;
    TextInputLayout society_nameInputLayout,society_contactInputLayout,society_emailInputLayout,society_addressInputLayuot;
    private int societyId,societyServerId,status;
    private String oldSocietyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__society__details);

        editSocietyView = findViewById(R.id.id_EditScoietyView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_society_toolbar);
        setSupportActionBar(toolbar);

        txt_society_name        = (EditText) findViewById(R.id.edittext_id_edit_society_name);
        txt_society_contact     = (EditText) findViewById(R.id.edittext_id_edit_society_contact);
        txt_society_email       = (EditText) findViewById(R.id.edittext_id_edit_society_email);
        txt_society_address     = (EditText) findViewById(R.id.edittext_id_edit_society_address);

        society_nameInputLayout     = (TextInputLayout) findViewById(R.id.id_input_layout_edit_society_name);
        society_contactInputLayout  = (TextInputLayout) findViewById(R.id.id_input_layout_edit_society_contact);
        society_emailInputLayout    = (TextInputLayout) findViewById(R.id.id_input_layout_edit_society_email);
        society_addressInputLayuot  = (TextInputLayout) findViewById(R.id.id_input_layout_edit_society_address);

        Bundle extras = getIntent().getExtras();
        societyId = extras.getInt("societyId");
        societyServerId = extras.getInt("societyServerId");
        status = extras.getInt("societyStatus");

        DatabaseHelper dbhelper = new DatabaseHelper(Edit_Society_Details.this);
        Society societyById = dbhelper.getSocietyById(societyId);
        dbhelper.closeDB();

        oldSocietyName =  societyById.getSoc_name() ;

        txt_society_name.setText(societyById.getSoc_name());
        txt_society_contact.setText(societyById.getSoc_contact());
        txt_society_email.setText(societyById.getSoc_email());
        txt_society_address.setText(societyById.getSoc_adrs());


        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_edit_society);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_society_name    = txt_society_name.getText().toString();
                str_society_contact = txt_society_contact.getText().toString();
                str_society_email   = txt_society_email.getText().toString();
                str_society_address = txt_society_address.getText().toString();

                Boolean boolName    = isEmptyString(str_society_name);
                Boolean boolContact = phoneValiate(str_society_contact);
                Boolean boolEmail   = emailValidate(str_society_email);
                Boolean boolAddress = isEmptyString(str_society_address);

                if (boolName) {
                    society_nameInputLayout.setErrorEnabled(false);
                } else {
                    society_nameInputLayout.setErrorEnabled(true);
                    society_nameInputLayout.setError("Society name field is empty.");
                }

                if (boolContact) {
                    society_contactInputLayout.setErrorEnabled(false);
                } else {
                    society_contactInputLayout.setErrorEnabled(true);
                    society_contactInputLayout.setError("Society contact field is empty.");
                }

                if (boolEmail) {
                    society_emailInputLayout.setErrorEnabled(false);
                } else {
                    society_emailInputLayout.setErrorEnabled(true);
                    society_emailInputLayout.setError("You need to enter correct email-id");
                }

                if (boolAddress) {
                    society_addressInputLayuot.setErrorEnabled(false);
                } else {
                    society_addressInputLayuot.setErrorEnabled(true);
                    society_addressInputLayuot.setError("Society address field is empty.");
                }


                if ( boolName && boolContact && boolEmail && boolAddress ) {

                    // Check if Internet present
                    if (!isConnectingToInternet(Edit_Society_Details.this)) {

                        Society society = new Society();
                        society.setSoc_name(str_society_name);
                        society.setSoc_contact(str_society_contact);
                        society.setSoc_email(str_society_email);
                        society.setSoc_adrs(str_society_address);
                        society.setServerId(societyServerId);
                        society.setId(societyId);
                        society.setSoc_status(status);

                        DatabaseHelper dbhelper = new DatabaseHelper(Edit_Society_Details.this);

                        Offline offline = new Offline( dbhelper.TABLE_SOCIETY,
                                (int) societyId,
                                offlineActionModeEnum.UPDATE.toString(),
                                ""+new Date().getTime() );

                        // it check whether data with same row id with update action in offline table.
                        // if yes delete old one and create new entry in offline table.
                        if( dbhelper.numberOfOfflineRowsByRowIdAndUpdate(societyId) > 0 ) {
                            dbhelper.deleteOfflineTableDataByRowIdAndUpdate(String.valueOf(societyId) );
                        }
                        dbhelper.insertOffline(offline);

                        dbhelper.updateSociety(society);
                        dbhelper.closeDB();

                        Intent intent = new Intent(Edit_Society_Details.this,View_Society.class);
                        startActivity(intent);
                        finish();

                    } else {

                        new UpdateSocietyAsyncTask(Edit_Society_Details.this,
                                str_society_name,
                                str_society_contact,
                                str_society_email,
                                str_society_address,
                                oldSocietyName,
                                societyServerId,
                                status).execute();

                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Edit_Society_Details.this,View_Society.class);
        startActivity(intent);
        finish();
    }
}
