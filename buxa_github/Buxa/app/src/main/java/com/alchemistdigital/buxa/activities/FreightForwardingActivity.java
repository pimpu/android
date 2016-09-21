package com.alchemistdigital.buxa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CustomClearanceModel;
import com.alchemistdigital.buxa.model.FreightForwardingModel;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.GooglePlacesAutocompleteAdapter;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;

import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class FreightForwardingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    LinearLayout layoutPortAddress;
    private ArrayList<String> arrayServicesId, arrayServicesName, availedServicesId, availedServicesName;
    private String shipmentType = "LCL";
    private RadioGroup rgShipmentType;
    private AutoCompleteTextView txtPOLAddress, txtPODAddress;
    private String bookId;
    private EditText txtBookId;
    TextInputLayout inputLayout_pol, inputLayout_pod;
    private DatabaseClass dbClass;
    TransportationModel transportDataModel;
    CustomClearanceModel customClearanceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight_forwarding);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        availedServicesId = getIntent().getStringArrayListExtra("availedServicesId");
        availedServicesName = getIntent().getStringArrayListExtra("availedServicesName");

        transportDataModel = getIntent().getExtras().getParcelable("transportData");
        customClearanceModel = getIntent().getExtras().getParcelable("customClearanceData");

        if ( transportDataModel  != null ) {
            shipmentType = transportDataModel.getStrShipmentType();
            bookId = transportDataModel.getBookingId();
        }
        else if( customClearanceModel != null) {
            shipmentType = customClearanceModel.getStrShipmentType();
            bookId = customClearanceModel.getBookingId();
        }

        dbClass = new DatabaseClass(this);

        toolbarSetup();

        init();

        // this intent fire when back button of QuotationActivity pressed
        registerReceiver(broadcast_reciever, new IntentFilter(CommonVariables.DISPLAY_MESSAGE_ACTION));
    }

    BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            if (newMessage.equals("finishingActivity")) {
                finish();
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    private void init() {
        inputLayout_pol = (TextInputLayout) findViewById(R.id.input_layout_POL);
        inputLayout_pod = (TextInputLayout) findViewById(R.id.input_layout_POD);
        layoutPortAddress = (LinearLayout) findViewById(R.id.layout_port_address);

        txtBookId = (EditText) findViewById(R.id.book_id_FF);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        if(bookId == null){
            int loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));

            txtBookId.setText( getResources().getString(R.string.codeString, DateHelper.getBookId(),loginId));
        }
        else {
            txtBookId.setText( bookId );
        }

        txtPOLAddress = (AutoCompleteTextView) findViewById(R.id.id_POL_adresses);
        txtPODAddress = (AutoCompleteTextView) findViewById(R.id.id_POD_adresses);

        // set google place library to autocomplete textview of port of loading
        txtPOLAddress.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPOLAddress.setOnItemClickListener(this);
        txtPOLAddress.setThreshold(1);

        // set google place library to autocomplete textview of port of destination
        txtPODAddress.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPODAddress.setOnItemClickListener(this);
        txtPODAddress.setThreshold(1);

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

        if( availedServicesName != null || ((arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) ||
                arrayServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString()))
                && arrayServicesId.size() > 0 )) {
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
        Boolean boolPOL = isEmptyString(txtPOLAddress.getText().toString());
        Boolean boolPOD = isEmptyString(txtPODAddress.getText().toString());

        if (shipmentType.equals("FCL")) {

            if (boolPOL) {
                inputLayout_pol.setErrorEnabled(false);
            } else {
                inputLayout_pol.setErrorEnabled(true);
                inputLayout_pol.setError("POL address field is empty.");
            }

            if (boolPOD) {
                inputLayout_pod.setErrorEnabled(false);
            } else {
                inputLayout_pod.setErrorEnabled(true);
                inputLayout_pod.setError("POD address field is empty.");
            }
        }

        if( shipmentType.equals("FCL") && boolPOL && boolPOD  ) {

            if( availedServicesName != null ) {

                FreightForwardingModel freightForwardingModel = new FreightForwardingModel(
                        txtBookId.getText().toString(),
                        txtPOLAddress.getText().toString(),
                        txtPODAddress.getText().toString(),
                        1,
                        1,
                        ""+DateHelper.convertToMillis(),
                        shipmentType
                );

                Intent intentForQuoteActivity = new Intent(this, QuotationActivity.class);
                intentForQuoteActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentForQuoteActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentForQuoteActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentForQuoteActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentForQuoteActivity.putExtra("freightForwardingModel", freightForwardingModel);
                intentForQuoteActivity.putExtra("customClearanceData", customClearanceModel);
                intentForQuoteActivity.putExtra("transportData", transportDataModel);
                startActivity(intentForQuoteActivity);
            }
            else {

                FreightForwardingModel freightForwardingModel = new FreightForwardingModel(
                        txtBookId.getText().toString(),
                        txtPOLAddress.getText().toString(),
                        txtPODAddress.getText().toString(),
                        0,
                        1,
                        ""+DateHelper.convertToMillis(),
                        shipmentType
                );

                Intent intentForQuoteActivity = new Intent(this, QuotationActivity.class);
                intentForQuoteActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentForQuoteActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentForQuoteActivity.putExtra("freightForwardingModel", freightForwardingModel);
                intentForQuoteActivity.putExtra("customClearanceData", customClearanceModel);
                intentForQuoteActivity.putExtra("transportData", transportDataModel);
                startActivity(intentForQuoteActivity);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);
    }
}
