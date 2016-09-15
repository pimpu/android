package com.alchemistdigital.buxa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.GooglePlacesAutocompleteAdapter;
import com.alchemistdigital.buxa.utilities.enumServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CustomClearanceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView txtCCAddress;
    RadioGroup rgShipmentType, rgFCLStuffing;
    TextView hintAddress;
    ArrayList<String> arrayServicesId, arrayServicesName, availedServicesId, availedServicesName;
    String shipmentType = "LCL", pickupAddress, bookId;
    private EditText txtBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clearance);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        availedServicesId = getIntent().getStringArrayListExtra("availedServicesId");
        availedServicesName = getIntent().getStringArrayListExtra("availedServicesName");

        if ( getIntent().getExtras().getString("shipmentType") != null ) {
            shipmentType = getIntent().getExtras().getString("shipmentType");
        }
        pickupAddress = getIntent().getExtras().getString("pickupAddress");
        bookId = getIntent().getExtras().getString("bookId");

        toolbarSetup();

        init();

        // this intent fire when back button of QuotationActivity pressed
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity_from_quotation_activity"));
    }

    BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity_from_quotation_activity")) {
                finish();
            }
        }
    };

    private void init() {
        txtBookId = (EditText) findViewById(R.id.book_id_CC);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        if(bookId == null){
            int loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));

            txtBookId.setText( getResources().getString(R.string.codeString, DateHelper.getBookId(),loginId));
        }
        else {
            txtBookId.setText( bookId );
        }

        rgShipmentType = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_CC);
        hintAddress = (TextView) findViewById(R.id.id_hint_CC_Address_label);
        hintAddress.setText("Vendor CFS Address");
        txtCCAddress = (AutoCompleteTextView) findViewById(R.id.id_CC_adresses);

        // set address to location
        txtCCAddress.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtCCAddress.setOnItemClickListener(this);

        rgShipmentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_cc:
                        rgFCLStuffing.setVisibility(View.GONE);
                        hintAddress.setText("Vendor CFS Address");
                        shipmentType = "LCL";
                        break;

                    case R.id.rbFcl_cc:
                        rgFCLStuffing.setVisibility(View.VISIBLE);
                        hintAddress.setText("Stuffing Address");
                        shipmentType = "FCL";

                        if ( ((RadioButton)(findViewById(R.id.rbFactoryStuff))).isChecked() ) {
                            hintAddress.setText(getResources().getString(R.string.strFactoryStuff)+" address");
                        }
                        else if ( ((RadioButton)(findViewById(R.id.rbDockStuff))).isChecked() ) {
                            hintAddress.setText(getResources().getString(R.string.strDockStuff)+" address");
                        }
                        break;
                }
            }
        });

        rgFCLStuffing = (RadioGroup) findViewById(R.id.radiogroup_FCL_Stuffing_CC);
        rgFCLStuffing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFactoryStuff:
                        hintAddress.setText(getResources().getString(R.string.strFactoryStuff)+" address");
                        if( arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) && arrayServicesId.size() > 0 ) {
                            txtCCAddress.setText(pickupAddress);
                            txtCCAddress.setClickable(false);
                            txtCCAddress.setCursorVisible(false);
                            txtCCAddress.setFocusable(false);
                            txtCCAddress.setFocusableInTouchMode(false);
                        }

                        break;

                    case R.id.rbDockStuff:
                        hintAddress.setText(getResources().getString(R.string.strDockStuff)+" address");
                        if( arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) && arrayServicesId.size() > 0 ) {
                            txtCCAddress.setText("");
                            // set address to location
                            txtCCAddress.setAdapter(new GooglePlacesAutocompleteAdapter(CustomClearanceActivity.this, R.layout.list_item));
                            txtCCAddress.setOnItemClickListener(CustomClearanceActivity.this);

                            txtCCAddress.setClickable(true);
                            txtCCAddress.setCursorVisible(true);
                            txtCCAddress.setFocusable(true);
                            txtCCAddress.setFocusableInTouchMode(true);
                        }
                        break;
                }
            }
        });

        // auto selection of field is did when user comes from Transportation page.
        if( availedServicesName != null || (arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) && arrayServicesId.size() > 0) ) {
            if(shipmentType.equals("LCL")) {
                rgFCLStuffing.setVisibility(View.GONE);
                rgShipmentType.check(R.id.rbLcl_cc);
                hintAddress.setText("Vendor CFS Address");
            }
            else {
                rgFCLStuffing.setVisibility(View.VISIBLE);
                rgShipmentType.check(R.id.rbFcl_cc);
                hintAddress.setText("Stuffing Address");
            }
            ((RadioButton)findViewById(R.id.rbLcl_cc)).setEnabled(false);
            ((RadioButton)findViewById(R.id.rbFcl_cc)).setEnabled(false);

        }
    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_custom_clearance);
        setSupportActionBar(toolbar);

        // set back button on toolbar
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        // set click listener on back button of toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Custom Clearance");
    }

    // set by autocompletetextfield
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void storeCustomClearanceEnquiry(View view) {
        if( availedServicesName != null ) {

            if(availedServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                Intent intentForFreightForardingActivity = new Intent(this, FreightForwardingActivity.class);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentForFreightForardingActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentForFreightForardingActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentForFreightForardingActivity.putExtra("shipmentType", shipmentType);
                intentForFreightForardingActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForFreightForardingActivity);
            }
            else {
                Intent intentForQuoteActivity = new Intent(this, QuotationActivity.class);
                intentForQuoteActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentForQuoteActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentForQuoteActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentForQuoteActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentForQuoteActivity.putExtra("shipmentType", shipmentType);
                intentForQuoteActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForQuoteActivity);
            }

        }
        else {

            if(arrayServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                Intent intentForFreightForardingActivity = new Intent(this, FreightForwardingActivity.class);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentForFreightForardingActivity.putExtra("shipmentType", shipmentType);
                intentForFreightForardingActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForFreightForardingActivity);
            }
            else {
                Intent intentForQuoteActivity = new Intent(this, QuotationActivity.class);
                intentForQuoteActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentForQuoteActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentForQuoteActivity.putExtra("shipmentType", shipmentType);
                intentForQuoteActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForQuoteActivity);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);
    }
}
