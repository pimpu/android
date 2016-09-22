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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.asynctask.InsertCustomClearanceAsyncTask;
import com.alchemistdigital.buxa.model.CustomClearanceModel;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.GooglePlacesAutocompleteAdapter;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class CustomClearanceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView txtCCAddress;
    RadioGroup rgShipmentType, rgFCLStuffing;
    TextView hintAddress;
    ArrayList<String> arrayServicesId, arrayServicesName, availedServicesId, availedServicesName;
    String strShipmentType = "LCL", pickupAddress, bookId, strSelectedStuffing;
    private EditText txtBookId;
    TextInputLayout inputLayout_cc_address;
    DatabaseClass dbClass;
    TransportationModel transportDataModel;
    int loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clearance);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        availedServicesId = getIntent().getStringArrayListExtra("availedServicesId");
        availedServicesName = getIntent().getStringArrayListExtra("availedServicesName");
        transportDataModel = getIntent().getExtras().getParcelable("transportData");

        // shipmentType and bookId intent is comes from quotation activity
        if ( getIntent().getExtras().getString("shipmentType") != null ) {
            strShipmentType = getIntent().getExtras().getString("shipmentType");
        }
        bookId = getIntent().getExtras().getString("bookId");

        if ( transportDataModel != null ) {
            strShipmentType = transportDataModel.getStrShipmentType();
            pickupAddress = transportDataModel.getPickUp();
            bookId = transportDataModel.getBookingId();
        }

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
            else if(newMessage.equals("gotoNextActivity_CC")) {
                intentActions(intent);
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    private void intentActions(Intent intent) {
        Intent intentActivity;
        if( availedServicesName != null ) {
            if(availedServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                intentActivity = new Intent(this, FreightForwardingActivity.class);
            }
            else {
                intentActivity = new Intent(this, QuotationActivity.class);
            }
        }
        else {
            if(arrayServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                intentActivity = new Intent(this, FreightForwardingActivity.class);
            }
            else {
                intentActivity = new Intent(this, QuotationActivity.class);
            }
        }

        CustomClearanceModel customClearanceModel = intent.getExtras().getParcelable("CCData");

        intentActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
        intentActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
        intentActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
        intentActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
        intentActivity.putExtra("customClearanceData", customClearanceModel);
        intentActivity.putExtra("transportData", transportDataModel);
        startActivity(intentActivity);
    }

    private void init() {
        dbClass = new DatabaseClass(this);

        inputLayout_cc_address = (TextInputLayout) findViewById(R.id.input_layout_cc_address);

        txtBookId = (EditText) findViewById(R.id.book_id_CC);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        if(bookId == null){
            loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));

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
        txtCCAddress.setThreshold(1);

        rgShipmentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_cc:
                        rgFCLStuffing.setVisibility(View.GONE);
                        hintAddress.setText("Vendor CFS Address");
                        strShipmentType = "LCL";
                        strSelectedStuffing = null;
                        break;

                    case R.id.rbFcl_cc:
                        rgFCLStuffing.setVisibility(View.VISIBLE);
                        hintAddress.setText("Stuffing Address");
                        strShipmentType = "FCL";

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
                        if( (availedServicesName != null && (availedServicesName.contains(enumServices.TRANSPORTATION.toString()) && availedServicesName.size() > 0) ) ||
                                (arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) && arrayServicesId.size() > 0) ) {
                            txtCCAddress.setText(pickupAddress);
                            txtCCAddress.setClickable(false);
                            txtCCAddress.setCursorVisible(false);
                            txtCCAddress.setFocusable(false);
                            txtCCAddress.setFocusableInTouchMode(false);
                        }
                        strSelectedStuffing = getResources().getString(R.string.strFactoryStuff);
                        break;

                    case R.id.rbDockStuff:
                        hintAddress.setText(getResources().getString(R.string.strDockStuff)+" address");
                        if( (availedServicesName != null && (availedServicesName.contains(enumServices.TRANSPORTATION.toString()) && availedServicesName.size() > 0) ) ||
                                (arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) && arrayServicesId.size() > 0) ) {
                            txtCCAddress.setText("");
                            // set address to location
                            txtCCAddress.setAdapter(new GooglePlacesAutocompleteAdapter(CustomClearanceActivity.this, R.layout.list_item));
                            txtCCAddress.setOnItemClickListener(CustomClearanceActivity.this);

                            txtCCAddress.setClickable(true);
                            txtCCAddress.setCursorVisible(true);
                            txtCCAddress.setFocusable(true);
                            txtCCAddress.setFocusableInTouchMode(true);
                        }
                        strSelectedStuffing = getResources().getString(R.string.strDockStuff);
                        break;
                }
            }
        });

        // auto selection of field is did when user comes from Transportation page.
        if( availedServicesName != null || (arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) && arrayServicesId.size() > 0) ) {
            if(strShipmentType.equals("LCL")) {
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
        Boolean boolAddress = isEmptyString(txtCCAddress.getText().toString());

        if (boolAddress) {
            inputLayout_cc_address.setErrorEnabled(false);
        } else {
            inputLayout_cc_address.setErrorEnabled(true);
            inputLayout_cc_address.setError(hintAddress.getText().toString()+" field is empty.");
        }

        if( strShipmentType.equals("FCL") && strSelectedStuffing == null ) {
            Toast.makeText(getApplicationContext(), "Stuffing type of FCL should be checked", Toast.LENGTH_LONG).show();
            return;
        }

        if (boolAddress) {
            String stuffingType;
            if( strShipmentType.equals("LCL") ) {
                stuffingType = "Vendor CFS";
            }
            else {
                stuffingType = strSelectedStuffing;
            }

            int iAvail = 0;
            if( availedServicesName != null ) {
                iAvail = 1;
            }

            CustomClearanceModel customClearanceModel = new CustomClearanceModel(
                    txtBookId.getText().toString(),
                    stuffingType,
                    txtCCAddress.getText().toString(),
                    iAvail,
                    1,
                    ""+DateHelper.convertToMillis(),
                    strShipmentType,
                    loginId,
                    dbClass.getShipmentTypeServerId(strShipmentType)
            );

            // get quotation of transportation from server

            // Check if Internet present
            if (!isConnectingToInternet(CustomClearanceActivity.this)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection),Toast.LENGTH_LONG).show();
                // stop executing code by return
                return;
            } else {
                InsertCustomClearanceAsyncTask.postCustomClearanceData(
                        CustomClearanceActivity.this,
                        customClearanceModel);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);
    }
}
