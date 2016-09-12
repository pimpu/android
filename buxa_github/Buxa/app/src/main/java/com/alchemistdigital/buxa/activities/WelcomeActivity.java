package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;

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
}
