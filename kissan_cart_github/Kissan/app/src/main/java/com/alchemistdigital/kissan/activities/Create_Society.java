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
import com.alchemistdigital.kissan.asynctask.InsertSocietyAsyncTask;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;
import com.andexert.library.RippleView;

import java.util.Date;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Create_Society extends AppCompatActivity{

    private EditText txt_society_name,txt_society_contact,txt_society_email,txt_society_address;
    private String  str_society_name,str_society_contact,str_society_email,str_society_address;
    TextInputLayout society_nameInputLayout,society_contactInputLayout,society_emailInputLayout,society_addressInputLayuot;
    private View createSocietyView;
    String comesFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__society);

        createSocietyView = findViewById(R.id.id_CreateScoietyView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_society_toolbar);
        setSupportActionBar(toolbar);

        // this activity is called from create enquiry activity or view society activity
        comesFrom = getIntent().getExtras().getString("comesFrom");

        txt_society_name        = (EditText) findViewById(R.id.edittext_id_create_society_name);
        txt_society_contact     = (EditText) findViewById(R.id.edittext_id_create_society_contact);
        txt_society_email       = (EditText) findViewById(R.id.edittext_id_create_society_email);
        txt_society_address     = (EditText) findViewById(R.id.edittext_id_create_society_address);

        society_nameInputLayout     = (TextInputLayout) findViewById(R.id.id_input_layout_create_society_name);
        society_contactInputLayout  = (TextInputLayout) findViewById(R.id.id_input_layout_create_society_contact);
        society_emailInputLayout    = (TextInputLayout) findViewById(R.id.id_input_layout_create_society_email);
        society_addressInputLayuot  = (TextInputLayout) findViewById(R.id.id_input_layout_create_society_address);

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_submit_society);
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

                    GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_Society.this);
                    int userId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));

                    // Check if Internet present
                    if (!isConnectingToInternet(Create_Society.this)) {

                        DatabaseHelper dbHelper = new DatabaseHelper(Create_Society.this);

                        Society society = new Society(0, userId, str_society_name, str_society_contact,
                                str_society_email, str_society_address, 1);
                        long societyId = dbHelper.insertSociety(society);

                        Offline offline = new Offline( dbHelper.TABLE_SOCIETY,
                                (int) societyId,
                                offlineActionModeEnum.INSERT.toString(),
                                ""+new Date().getTime() );
                        dbHelper.insertOffline(offline);

                        dbHelper.closeDB();

                        onBackPressed();
                    } else {
                        new InsertSocietyAsyncTask(Create_Society.this,str_society_name,str_society_contact,str_society_email,str_society_address,userId,comesFrom).execute();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = null;
        if (comesFrom.equals("CreateEnquiry")){
            intent = new Intent( Create_Society.this , Create_Enquiry.class);
            Bundle bundle = new Bundle();
            bundle.putString("callingClass","createSociety");
            intent.putExtras(bundle);
        }
        else if (comesFrom.equals("ViewSociety")) {
            intent = new Intent( Create_Society.this , View_Society.class);
        }
        startActivity(intent);
        finish();
    }
}
