package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.InsertSocietyAsyncTask;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.andexert.library.RippleView;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Create_Society extends AppCompatActivity{

    private EditText txt_society_name,txt_society_contact,txt_society_email,txt_society_address;
    private String  str_society_name,str_society_contact,str_society_email,str_society_address;
    TextInputLayout society_nameInputLayout,society_contactInputLayout,society_emailInputLayout,society_addressInputLayuot;
    private View createSocietyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__society);

        createSocietyView = findViewById(R.id.id_CreateScoietyView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_society_toolbar);
        setSupportActionBar(toolbar);

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

                    // Check if Internet present
                    if (!isConnectingToInternet(Create_Society.this)) {
                        // Internet Connection is not present
                        Snackbar.make(createSocietyView, "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onCreate(null);
                                    }
                                }).show();
                        // stop executing code by return
                        return;
                    } else {
                        /*System.out.println(str_society_name);
                        System.out.println(str_society_contact);
                        System.out.println(str_society_email);
                        System.out.println(str_society_address);*/
                        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_Society.this);
                        int userId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));

                        new InsertSocietyAsyncTask(Create_Society.this,str_society_name,str_society_contact,str_society_email,str_society_address,userId).execute();
                    }
                }
            }
        });
    }
}
