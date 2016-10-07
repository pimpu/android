package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;

public class WelcomeActivity extends AppCompatActivity {
    TextView tv_welcome, tv_CompanyName, tv_UserName, tv_UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tv_welcome = (TextView) findViewById(R.id.id_welcomeText_welcomeScreen);
        Typeface face1= Typeface.createFromAsset(getAssets(), "fonts/HELVETICA_CONDENSED_LIGHT_7.OTF");
        tv_welcome.setTypeface(face1);

        tv_CompanyName = (TextView) findViewById(R.id.id_tv_companyName);
        tv_UserName = (TextView) findViewById(R.id.id_tv_userName);
        tv_UserEmail = (TextView) findViewById(R.id.id_tv_userEmail);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        String companyName = getSharedPreference.getCompanyName(getResources().getString(R.string.companyName));
        String loginEmail = getSharedPreference.getLoginEmail(getResources().getString(R.string.loginEmail));
        String loginName = getSharedPreference.getLoginName(getResources().getString(R.string.loginName));

        tv_CompanyName.setText(companyName);
        tv_UserName.setText(loginName);
        tv_UserEmail.setText(loginEmail);
    }

    public void goToServiceActivity(View view) {
        startActivity(new Intent(this, SelectServiceActivity.class));
    }

    public void btnEnquiryClick(View view) {
//        startActivity(new Intent(this, EnquiriesActivity.class));
        Toast.makeText(getApplicationContext(), "Work in progress...", Toast.LENGTH_LONG).show();
    }

    public void btnLogout(View view) {
        SetSharedPreference setSharedPreference = new SetSharedPreference(this);
        // signout from app.
        setSharedPreference.setBooleanLogin(getString(R.string.boolean_login_sharedPref), "false");

        Intent intent = new Intent(this, StartupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);

    }
}
