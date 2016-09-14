package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.utilities.GooglePlacesAutocompleteAdapter;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;

public class FreightForwardingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    LinearLayout layoutPortAddress;
    private ArrayList<String> arrayServicesId;
    private ArrayList<String> arrayServicesName;
    private String shipmentType = "LCL";
    private RadioGroup rgShipmentType;
    private AutoCompleteTextView txtPOLAddress, txtPODAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight_forwarding);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        if (getIntent().getExtras().getString("shipmentType") != null){
            shipmentType = getIntent().getExtras().getString("shipmentType");
        }

        toolbarSetup();

        init();
    }

    private void init() {
        layoutPortAddress = (LinearLayout) findViewById(R.id.layout_port_address);
        txtPOLAddress = (AutoCompleteTextView) findViewById(R.id.id_POL_adresses);
        txtPODAddress = (AutoCompleteTextView) findViewById(R.id.id_POD_adresses);

        // set google place library to autocomplete textview of port of loading
        txtPOLAddress.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPOLAddress.setOnItemClickListener(this);

        // set google place library to autocomplete textview of port of destination
        txtPODAddress.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPODAddress.setOnItemClickListener(this);

        // radiogroup for type of shipment
        rgShipmentType = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_FF);
        rgShipmentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_ff:
                        shipmentType = "LCL";
                        layoutPortAddress.setVisibility(View.GONE);
                        break;

                    case R.id.rbFcl_ff:
                        shipmentType = "FCL";
                        layoutPortAddress.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        if( (arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) ||
                arrayServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString()))
                && arrayServicesId.size() > 0 ) {
            if(shipmentType.equals("LCL")) {
                rgShipmentType.check(R.id.rbLcl_ff);
                layoutPortAddress.setVisibility(View.GONE);
            }
            else {
                rgShipmentType.check(R.id.rbFcl_ff);
                layoutPortAddress.setVisibility(View.VISIBLE);
            }

            ((RadioButton)findViewById(R.id.rbLcl_ff)).setEnabled(false);
            ((RadioButton)findViewById(R.id.rbFcl_ff)).setEnabled(false);
        }
    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_freight_forwarding);
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
        getSupportActionBar().setTitle("Freight Forwarding");
    }

    public void storeFreightForwardingEnquiry(View view) {
        Intent intentForFreightForardingActivity = new Intent(this, QuotationActivity.class);
        intentForFreightForardingActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
        intentForFreightForardingActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
        startActivity(intentForFreightForardingActivity);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
