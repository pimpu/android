package com.alchemistdigital.buxa.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;

import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class Feedback extends AppCompatActivity {
    TextView tvUserName, tvUserEmail;
    TextInputLayout inputLayout_feedback;
    EditText txtFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_feedback);
        toolbar.setTitle(getResources().getString(R.string.title_activity_feedback));
        setSupportActionBar(toolbar);

        tvUserEmail = (TextView) findViewById(R.id.feedbackUserEmail);
        tvUserName = (TextView) findViewById(R.id.feedbackUserName);
        inputLayout_feedback = (TextInputLayout) findViewById(R.id.input_layout_feedback);
        txtFeedback = (EditText) findViewById(R.id.id_feedback);

        GetSharedPreference getSharedPreference = new GetSharedPreference(Feedback.this);
        String loginEmail = getSharedPreference.getLoginEmail(getResources().getString(R.string.loginEmail));
        String loginName = getSharedPreference.getLoginName(getResources().getString(R.string.loginName));

        tvUserName.setText(loginName);
        tvUserEmail.setText(loginEmail);
    }

    public void sentFeedback(View view) {
        Boolean boolFeedback = isEmptyString(txtFeedback.getText().toString());

        if (boolFeedback) {
            inputLayout_feedback.setErrorEnabled(false);
        } else {
            inputLayout_feedback.setErrorEnabled(true);
            inputLayout_feedback.setError("Feedback field is empty.");
        }

        if(boolFeedback){
            Toast.makeText(getApplicationContext(),"Thanks for your favorable feedback !",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
