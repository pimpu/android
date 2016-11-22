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
import com.alchemistdigital.buxa.asynctask.FeedbackAsynTask;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class Feedback extends AppCompatActivity {
    TextView tvUserName, tvUserEmail;
    TextInputLayout inputLayout_feedback;
    EditText txtFeedback;
    private GetSharedPreference getSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbarSetup();


        tvUserEmail = (TextView) findViewById(R.id.feedbackUserEmail);
        tvUserName = (TextView) findViewById(R.id.feedbackUserName);
        inputLayout_feedback = (TextInputLayout) findViewById(R.id.input_layout_feedback);
        txtFeedback = (EditText) findViewById(R.id.id_feedback);

        getSharedPreference = new GetSharedPreference(Feedback.this);
        String loginEmail = getSharedPreference.getLoginEmail(getResources().getString(R.string.loginEmail));
        String loginName = getSharedPreference.getLoginName(getResources().getString(R.string.loginName));

        tvUserName.setText(loginName);
        tvUserEmail.setText(loginEmail);

    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_feedback);
        setSupportActionBar(toolbar);

        // set back button on toolbar
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        // set click listener on back button of toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_feedback));
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
            if (!isConnectingToInternet(Feedback.this)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection), Toast.LENGTH_LONG).show();
                // stop executing code by return
                return;
            }
            else {
                getSharedPreference = new GetSharedPreference(Feedback.this);
                int loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));
                String apiKeyHeader = getSharedPreference.getApiKey(getResources().getString(R.string.apikey));
                new FeedbackAsynTask(Feedback.this, loginId, apiKeyHeader, txtFeedback.getText().toString()).sendFeedback();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
