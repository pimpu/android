package com.alchemistdigital.buxa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.DateHelper;

import java.util.ArrayList;
import java.util.Date;

public class QuotationActivity extends AppCompatActivity {
    TextView tvCompanyName, tvClientName, tvQuotationDate, tvQuotationSubject;
    ArrayList<String> arrayServicesId, arrayServicesName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");

        init();
    }

    private void init() {
        tvCompanyName = (TextView) findViewById(R.id.id_tv_companyName_quotation);
        tvClientName = (TextView) findViewById(R.id.id_tv_clientName_quotation);
        tvQuotationDate = (TextView) findViewById(R.id.id_tv_date_quotation);
        tvQuotationSubject = (TextView) findViewById(R.id.id_tv_quotation_subject);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        String companyName = getSharedPreference.getCompanyName(getResources().getString(R.string.companyName));
        String loginName = getSharedPreference.getLoginName(getResources().getString(R.string.loginName));

        tvCompanyName.setText(companyName);
        tvClientName.setText(loginName);
        tvQuotationDate.setText(DateHelper.convertToString_Quotation( new Date().getTime() ) );

        StringBuilder strSelectedServices;
        for (int i = 0 ; i < arrayServicesName.size() ; i++ ) {
            System.out.println(arrayServicesName.get(i));
        }


//        tvQuotationSubject.setText(arrayServicesName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
