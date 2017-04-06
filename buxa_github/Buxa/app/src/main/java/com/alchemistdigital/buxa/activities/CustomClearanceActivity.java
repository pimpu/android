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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.asynctask.InsertCustomClearanceAsyncTask;
import com.alchemistdigital.buxa.asynctask.InsertTransportationAsyncTask;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.CustomClearanceModel;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.GooglePlacesAutocompleteAdapter;
import com.alchemistdigital.buxa.utilities.ShipAreaVariableSingleton;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class CustomClearanceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView txtCCAddress, txtCommodity;
    EditText txtBookId, txtGrossWt, txtHSC;
    RadioGroup rgShipmentType, rgFCLStuffing;
    TextView hintAddress;
    ArrayList<String> arrayServicesId, arrayServicesName, availedServicesId, availedServicesName;
    String strShipmentType = "LCL", sPickupAddress, sBookId, strSelectedStuffing,
            strSelectedImpoExpo;
    TextInputLayout inputLayout_cc_address, inputLayout_commodity, inputLayout_gross_wt,
            inputLayout_hsc;
    DatabaseClass dbClass;
    TransportationModel transportDataModel;
    int iLoginId;
    String strCommodity;
    float fGrossWt;
    LinearLayout layout_cc_fcl_adddress;
    public static CustomClearanceModel customClearanceModel;

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
        sBookId = getIntent().getExtras().getString("bookId");

        if ( transportDataModel != null ) {
            strShipmentType = transportDataModel.getStrShipmentType();
            sPickupAddress = transportDataModel.getPickUp();
            sBookId = transportDataModel.getBookingId();
            strCommodity = transportDataModel.getStrCommodity();
            fGrossWt = transportDataModel.getGrossWeight();
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
            else if(newMessage.equals("gotoQuotationActivityFromCC")) {
                Intent intentActivity = new Intent(CustomClearanceActivity.this, QuotationActivity.class);
                intentActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentActivity.putExtra("customClearanceData", intent.getExtras().getParcelable("CCData"));
                intentActivity.putExtra("transportData", transportDataModel);
                startActivity(intentActivity);
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };



    private void init() {
        dbClass = new DatabaseClass(this);
        strSelectedImpoExpo = getIntent().getExtras().getString("selectedImpoExpo");

        layout_cc_fcl_adddress = (LinearLayout) findViewById(R.id.id_cc_fcl_addresses);

        inputLayout_cc_address = (TextInputLayout) findViewById(R.id.input_layout_cc_address);
        inputLayout_commodity = (TextInputLayout) findViewById(R.id.input_layout_commodity_cc);
        inputLayout_gross_wt = (TextInputLayout) findViewById(R.id.input_layout_gross_weight_cc);
        inputLayout_hsc = (TextInputLayout) findViewById(R.id.input_layout_hs_code);

        txtBookId = (EditText) findViewById(R.id.book_id_CC);
        txtGrossWt = (EditText) findViewById(R.id.id_gross_weight_cc);
        txtHSC = (EditText) findViewById(R.id.id_hs_code);
        rgShipmentType = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_CC);
        hintAddress = (TextView) findViewById(R.id.id_hint_CC_Address_label);
        txtCCAddress = (AutoCompleteTextView) findViewById(R.id.id_CC_adresses);
        txtCommodity = (AutoCompleteTextView) findViewById(R.id.id_commodity_cc);

        // initialised comodity autocomplete textfield from database
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        ArrayAdapter<CommodityModel> commodity_adapter = new ArrayAdapter<CommodityModel>(this, layoutItemId, dbClass.getCommodityData());
        txtCommodity.setAdapter(commodity_adapter);
        txtCommodity.setThreshold(1);
        /*txtCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iCommodityServerId = commodity_adapter.getItem(position).getServerId();
            }
        });*/

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        iLoginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));
        if(sBookId == null){

            txtBookId.setText( getResources().getString(R.string.codeString, DateHelper.getBookId(),iLoginId));
        }
        else {
            txtBookId.setText( sBookId );
        }

        rgShipmentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_cc:
                        rgFCLStuffing.setVisibility(View.GONE);
                        strShipmentType = "LCL";
                        layout_cc_fcl_adddress.setVisibility(View.GONE);
                        strSelectedStuffing = null;
                        break;

                    case R.id.rbFcl_cc:

                        layout_cc_fcl_adddress.setVisibility(View.VISIBLE);
                        rgFCLStuffing.setVisibility(View.VISIBLE);
                        hintAddress.setText("Stuffing Address");
                        strShipmentType = "FCL";

                        if ( ((RadioButton)(findViewById(R.id.rbFactoryStuff))).isChecked() ) {
                            hintAddress.setText(getResources().getString(R.string.strFactoryStuff)+" address");
                        }
                        else if ( ((RadioButton)(findViewById(R.id.rbDockStuff))).isChecked() ) {
                            hintAddress.setText(getResources().getString(R.string.strDockStuff)+" address");
                        }

                        // set address to location
                        txtCCAddress.setAdapter(new GooglePlacesAutocompleteAdapter(CustomClearanceActivity.this, R.layout.list_item));
                        txtCCAddress.setOnItemClickListener(CustomClearanceActivity.this);
                        txtCCAddress.setThreshold(1);
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
                            txtCCAddress.setText(sPickupAddress);
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

            txtCommodity.setText( strCommodity );
            txtCommodity.setClickable(false);
            txtCommodity.setCursorVisible(false);
            txtCommodity.setFocusable(false);
            txtCommodity.setFocusableInTouchMode(false);

            txtGrossWt.setText(""+fGrossWt);
            txtGrossWt.setClickable(false);
            txtGrossWt.setCursorVisible(false);
            txtGrossWt.setFocusable(false);
            txtGrossWt.setFocusableInTouchMode(false);
        }
    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_custom_clearance);
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
        getSupportActionBar().setTitle("Custom Clearance");
        getSupportActionBar().setSubtitle(ShipAreaVariableSingleton.getInstance().shipAreaName);
    }

    // set by autocompletetextfield
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void storeCustomClearanceEnquiry(View view) {
        String strCommodity = txtCommodity.getText().toString();

        Boolean boolAddress = isEmptyString(txtCCAddress.getText().toString());
        Boolean boolCommodity = isEmptyString(strCommodity) && (dbClass.getCommodityServerID(strCommodity) > 0);
        Boolean boolGrossWt = isEmptyString(txtGrossWt.getText().toString());
        Boolean boolHSC = isEmptyString(txtHSC.getText().toString());

        if (boolAddress) {
            inputLayout_cc_address.setErrorEnabled(false);
        } else {
            inputLayout_cc_address.setErrorEnabled(true);
            inputLayout_cc_address.setError(hintAddress.getText().toString()+" field is empty.");
        }

        if (boolCommodity) {
            inputLayout_commodity.setErrorEnabled(false);
        } else {
            inputLayout_commodity.setErrorEnabled(true);
            inputLayout_commodity.setError("Commodity field is empty or not in dropdown.");
        }

        if (boolGrossWt) {
            inputLayout_gross_wt.setErrorEnabled(false);
        } else {
            inputLayout_gross_wt.setErrorEnabled(true);
            inputLayout_gross_wt.setError("Gross weight field is empty.");
        }

        if (boolHSC) {
            inputLayout_hsc.setErrorEnabled(false);
        } else {
            inputLayout_hsc.setErrorEnabled(true);
            inputLayout_hsc.setError("HSC field is empty.");
        }

        if( strShipmentType.equals("FCL") && strSelectedStuffing == null  ) {
            Toast.makeText(getApplicationContext(), "Stuffing type of FCL should be checked", Toast.LENGTH_LONG).show();
            return;
        }

        if (boolCommodity && boolGrossWt && boolHSC) {

            if( strShipmentType.equals("FCL") && ! boolAddress ) {
                return;
            }

            int iAvail = 0;
            if( availedServicesName != null ) {
                iAvail = 1;
            }

            /*System.out.println("Booking id: "+txtBookId.getText().toString());
            System.out.println("CC Type: "+strSelectedImpoExpo);
            System.out.println("Commodity: "+txtCommodity.getText().toString());
            System.out.println("Grosss wt: "+txtGrossWt.getText().toString());
            System.out.println("HS code: "+txtHSC.getText().toString());
            System.out.println("Type of shipemnt: "+strShipmentType);
            System.out.println("Type of stuffing: "+strSelectedStuffing);
            System.out.println("Stuffing address: "+txtCCAddress.getText().toString());*/

            customClearanceModel = new CustomClearanceModel(
                    txtBookId.getText().toString(),
                    strSelectedImpoExpo,
                    strCommodity,
                    Float.parseFloat(txtGrossWt.getText().toString()),
                    Integer.parseInt(txtHSC.getText().toString()),
                    strShipmentType,
                    strSelectedStuffing,
                    txtCCAddress.getText().toString(),
                    iAvail,
                    1,
                    ""+DateHelper.convertToMillis(),
                    iLoginId
            );

            intentActions();

        }

    }

    private void intentActions() {
        Intent intentActivity = null;
        Boolean isIntentQuote =false;
        Boolean isTransService = false;

        if( availedServicesName != null ) {
            if(availedServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                intentActivity = new Intent(this, FreightForwardingActivity.class);
            }
            else {
                isIntentQuote = true;
            }

            // if users come in this activity from transport activity
            // isTransService = true
            // Then, beforing saving data of CC activity, Save transport data, then next CC activity.
            if(availedServicesName.contains(enumServices.TRANSPORTATION.toString())) {
                isTransService=true;
            }
        }
        else {
            if(arrayServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                intentActivity = new Intent(this, FreightForwardingActivity.class);
            }
            else {
                isIntentQuote = true;
            }

            // if users come in this activity from transport activity
            // isTransService = true
            // Then, beforing saving data of CC activity, Save transport data, then next CC activity.
            if(arrayServicesName.contains(enumServices.TRANSPORTATION.toString())) {
                isTransService=true;
            }
        }

        // hide soft keyboard
        View viewFocus = getCurrentFocus();
        if (viewFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }

        if(isIntentQuote){
            // get quotation of transportation from server
            // Check if Internet present
            if (!isConnectingToInternet(CustomClearanceActivity.this)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection),Toast.LENGTH_LONG).show();
                // stop executing code by return
                return;
            } else {
                if(isTransService) {
                    InsertTransportationAsyncTask.postTransportationData(
                            CustomClearanceActivity.this,
                            transportDataModel);
                }
                else {
                    InsertCustomClearanceAsyncTask.postCustomClearanceData(
                            CustomClearanceActivity.this,
                            customClearanceModel);
                }
            }
        }
        else {
            intentActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
            intentActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
            intentActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
            intentActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
            intentActivity.putExtra("customClearanceData", customClearanceModel);
            intentActivity.putExtra("transportData", transportDataModel);
            startActivity(intentActivity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);
    }

}
