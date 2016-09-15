package com.alchemistdigital.buxa.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;
import java.util.Date;

public class QuotationActivity extends AppCompatActivity {
    TextView tvCompanyName, tvClientName, tvQuotationDate, tvQuotationSubject, tvBookingNo;
    ArrayList<String> arrayServicesId, arrayServicesName, availedServicesId, availedServicesName;
    ArrayList<String> arrayComparingNameArray = new ArrayList<String>();
    ArrayList<String> arrayComparingIdArray = new ArrayList<String>();
    String strAvailServiceOption = "Do you avail with ";
    private String bookId, shipmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        availedServicesId = getIntent().getStringArrayListExtra("availedServicesId");
        availedServicesName = getIntent().getStringArrayListExtra("availedServicesName");

        shipmentType = getIntent().getExtras().getString("shipmentType");
        bookId = getIntent().getExtras().getString("bookId");

        init();

        initQuoteTable();
    }

    private void initQuoteTable() {

    }

    private void init() {
        tvCompanyName = (TextView) findViewById(R.id.id_tv_companyName_quotation);
        tvClientName = (TextView) findViewById(R.id.id_tv_clientName_quotation);
        tvBookingNo = (TextView) findViewById(R.id.id_tv_bookId);
        tvQuotationDate = (TextView) findViewById(R.id.id_tv_date_quotation);
        tvQuotationSubject = (TextView) findViewById(R.id.id_tv_quotation_subject);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        String companyName = getSharedPreference.getCompanyName(getResources().getString(R.string.companyName));
        String loginName = getSharedPreference.getLoginName(getResources().getString(R.string.loginName));

        tvCompanyName.setText(companyName);
        tvClientName.setText(loginName);
        tvBookingNo.setText(bookId);
        tvQuotationDate.setText(DateHelper.convertToString_Quotation( new Date().getTime() ) );

        // this array is use for comparing with arrayServicesName
        // and remove duplicate entry from this array
        arrayComparingNameArray.add(enumServices.TRANSPORTATION.toString());
        arrayComparingNameArray.add(enumServices.CUSTOM_CLEARANCE.toString());
        arrayComparingNameArray.add(enumServices.FREIGHT_FORWARDING.toString());

        // remove duplicate contain from arrayComparingArray
        arrayComparingNameArray.removeAll(arrayServicesName);
        DatabaseClass databaseClass = new DatabaseClass(this);
        for (int y = 0 ; y < arrayComparingNameArray.size() ; y++ ) {

            arrayComparingIdArray.add(""+databaseClass.getTransportServiceServerId(arrayComparingNameArray.get(y)) );

            if ( (arrayComparingNameArray.size() - 1) == y ) {
                strAvailServiceOption = strAvailServiceOption + arrayComparingNameArray.get(y) + ". ";
            }
            else {
                strAvailServiceOption = strAvailServiceOption + arrayComparingNameArray.get(y) + " & ";
            }
        }

        String strSelectedServices="Quotation for ";
        for (int i = 0 ; i < arrayServicesName.size() ; i++ ) {
            if ( (arrayServicesName.size() - 1) == i ) {
                strSelectedServices = strSelectedServices + arrayServicesName.get(i) + ". ";
            }
            else {
                strSelectedServices = strSelectedServices + arrayServicesName.get(i) + ", ";
            }
        }
        tvQuotationSubject.setText(strSelectedServices);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("finish_activity_from_quotation_activity");
        sendBroadcast(intent);

        if ( availedServicesName == null && arrayComparingNameArray.size() > 0) {


            new AlertDialog.Builder(QuotationActivity.this)
                    .setMessage(strAvailServiceOption)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if( arrayComparingNameArray.contains(enumServices.TRANSPORTATION.toString()) ) {

                                Intent intentTransportActivity = new Intent(QuotationActivity.this, TrasportQuotationActivity.class);
                                intentTransportActivity.putStringArrayListExtra("availedServicesId", arrayComparingIdArray);
                                intentTransportActivity.putStringArrayListExtra("availedServicesName", arrayComparingNameArray);
                                intentTransportActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                                intentTransportActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                                intentTransportActivity.putExtra("shipmentType", shipmentType);
                                intentTransportActivity.putExtra("bookId", bookId);
                                startActivity(intentTransportActivity);

                            } else if( arrayComparingNameArray.contains(enumServices.CUSTOM_CLEARANCE.toString()) ) {

                                Intent intentCCActivity = new Intent(QuotationActivity.this, CustomClearanceActivity.class);
                                intentCCActivity.putStringArrayListExtra("availedServicesId", arrayComparingIdArray);
                                intentCCActivity.putStringArrayListExtra("availedServicesName", arrayComparingNameArray);
                                intentCCActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                                intentCCActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                                intentCCActivity.putExtra("shipmentType", shipmentType);
                                intentCCActivity.putExtra("bookId", bookId);
                                startActivity(intentCCActivity);

                            } else if(arrayComparingNameArray.contains(enumServices.FREIGHT_FORWARDING.toString())) {

                                Intent intentFFActivity = new Intent(QuotationActivity.this, FreightForwardingActivity.class);
                                intentFFActivity.putStringArrayListExtra("availedServicesId", arrayComparingIdArray);
                                intentFFActivity.putStringArrayListExtra("availedServicesName", arrayComparingNameArray);
                                intentFFActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                                intentFFActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                                intentFFActivity.putExtra("shipmentType", shipmentType);
                                intentFFActivity.putExtra("bookId", bookId);
                                startActivity(intentFFActivity);

                            }
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
        else {
            super.onBackPressed();
        }
    }
}
