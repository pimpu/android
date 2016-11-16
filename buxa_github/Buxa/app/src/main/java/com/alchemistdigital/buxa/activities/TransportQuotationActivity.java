package com.alchemistdigital.buxa.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.asynctask.InsertTransportationAsyncTask;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.CustomClearanceModel;
import com.alchemistdigital.buxa.model.FreightForwardingModel;
import com.alchemistdigital.buxa.model.PackageTypeModel;
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

public class TransportQuotationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    LinearLayout layuotCommonFreight;
    ArrayAdapter<CommodityModel> commodity_adapter;
    ArrayAdapter<PackageTypeModel> packagingType_adapter;
    AutoCompleteTextView txtComodity, txtTypeOfPackaging, txtPickup, txtDrop, txtManualPickup, txtManualDrop;
    RadioGroup rgContainerSize, rgTypeOfShipment;
    DatabaseClass dbClass ;
    ArrayList<String> ids, names, availedServicesId, availedServicesName;
    String strSelectedShipmentType = "LCL", bookId, strSelectedContainerSize = null;
    private EditText txtBookId, txtCBM, txtGrossWt, txt_noOfPack, txtDimenLen,
                    txtDimenHeight, txtDimenWidth, txtPickupLandmark, txtPickupPincode,
                    txtDropLandmark, txtDropPincode;
    TextInputLayout inputLayout_pickUp, inputLayout_drop, inputLayout_cubicMeter, inputLayout_grossWeight,
                    inputLayout_packType, inputLayout_noOfPack, inputLayout_commodity,
                    inputLayout_dimen_len, inputLayout_dimen_height, inputLayout_dimen_width,
                    inputLayout_manual_pickUp, inputLayout_pick_landmark, inputLayout_pickUp_pincode,
                    inputLayout_manual_drop, inputLayout_drop_landmark, inputLayout_drop_pincode;
//    private int iSelectedCommodityId, iSelectedPackageType;
    private int loginId;
    private LinearLayout layout_pickup, layout_manual_pickup, id_not_find_pick_google_text, id_backTo_pick_google_text;
    private LinearLayout layout_drop, layout_manual_drop, id_not_find_drop_google_text, id_backTo_drop_google_text;
    private FreightForwardingModel freightForwardingModel;
    private CustomClearanceModel customClearanceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasport_quotation);

        ids = getIntent().getStringArrayListExtra("ServicesId");
        names = getIntent().getStringArrayListExtra("ServicesName");
        availedServicesId = getIntent().getStringArrayListExtra("availedServicesId");
        availedServicesName = getIntent().getStringArrayListExtra("availedServicesName");
        freightForwardingModel = getIntent().getExtras().getParcelable("freightForwardingModel");
        customClearanceModel = getIntent().getExtras().getParcelable("customClearanceData");

        if ( getIntent().getExtras().getString("shipmentType") != null ) {
            strSelectedShipmentType = getIntent().getExtras().getString("shipmentType");
        }
        bookId = getIntent().getExtras().getString("bookId");
        dbClass = new DatabaseClass(this);

        toolbarSetup();

        init();

        transportation();

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
            else if(newMessage.equals("gotoQuotationActivityFromTrans")) {

                Intent intentActivity = new Intent(TransportQuotationActivity.this, QuotationActivity.class);
                intentActivity.putStringArrayListExtra("ServicesId",  ids);
                intentActivity.putStringArrayListExtra("ServicesName", names);
                intentActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentActivity.putExtra("transportData",intent.getExtras().getParcelable("transportData"));

                startActivity(intentActivity);
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_transportQuotation);
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
        getSupportActionBar().setTitle("Transportation");
    }

    private void init() {
        layuotCommonFreight = (LinearLayout) findViewById(R.id.id_commomFreightLayout);

        // initialised all Linear Layout
        layout_pickup = (LinearLayout) findViewById(R.id.layout_pickup);
        layout_manual_pickup = (LinearLayout) findViewById(R.id.layout_manual_pickup);
        id_not_find_pick_google_text = (LinearLayout) findViewById(R.id.id_not_find_pick_google_text);
        id_backTo_pick_google_text = (LinearLayout) findViewById(R.id.id_backTo_pick_google_text);
        layout_drop = (LinearLayout) findViewById(R.id.layout_drop);
        layout_manual_drop = (LinearLayout) findViewById(R.id.layout_manual_drop);
        id_not_find_drop_google_text = (LinearLayout) findViewById(R.id.id_not_find_drop_google_text);
        id_backTo_drop_google_text = (LinearLayout) findViewById(R.id.id_backTo_drop_google_text);

        // initialised all Text Input Layout
        inputLayout_pickUp = (TextInputLayout) findViewById(R.id.input_layout_pick_up);
        inputLayout_drop = (TextInputLayout) findViewById(R.id.input_layout_drop);
        inputLayout_cubicMeter = (TextInputLayout) findViewById(R.id.input_layout_cubic_meter_measurement);
        inputLayout_grossWeight = (TextInputLayout) findViewById(R.id.input_layout_gross_weight);
        inputLayout_packType = (TextInputLayout) findViewById(R.id.input_layout_pack_type);
        inputLayout_noOfPack = (TextInputLayout) findViewById(R.id.input_layout_no_of_package);
        inputLayout_commodity = (TextInputLayout) findViewById(R.id.input_layout_commodity);
        inputLayout_dimen_len = (TextInputLayout) findViewById(R.id.input_layout_dimensions_length);
        inputLayout_dimen_height = (TextInputLayout) findViewById(R.id.input_layout_dimensions_height);
        inputLayout_dimen_width = (TextInputLayout) findViewById(R.id.input_layout_dimensions_width);
        inputLayout_manual_pickUp = (TextInputLayout) findViewById(R.id.input_layout_manual_pick);
        inputLayout_pick_landmark = (TextInputLayout) findViewById(R.id.input_layout_pick_landmark);
        inputLayout_pickUp_pincode = (TextInputLayout) findViewById(R.id.input_layout_pick_pincode);
        inputLayout_manual_drop = (TextInputLayout) findViewById(R.id.input_layout_manual_drop);
        inputLayout_drop_landmark = (TextInputLayout) findViewById(R.id.input_layout_drop_landmark);
        inputLayout_drop_pincode = (TextInputLayout) findViewById(R.id.input_layout_drop_pincode);

        // initialised all Edit Text
        txtBookId = (EditText) findViewById(R.id.book_id_trans);
        txtCBM = (EditText) findViewById(R.id.id_cubic_meter_measurement);
        txtGrossWt = (EditText) findViewById(R.id.id_gross_weight);
        txt_noOfPack = (EditText) findViewById(R.id.id_no_of_package);
        txtDimenLen = (EditText) findViewById(R.id.id_dimensions_length);
        txtDimenHeight = (EditText) findViewById(R.id.id_dimensions_height);
        txtDimenWidth = (EditText) findViewById(R.id.id_dimensions_width);
        txtPickupLandmark = (EditText) findViewById(R.id.id_pick_landmark);
        txtPickupPincode = (EditText) findViewById(R.id.id_pick_pincode);
        txtDropLandmark = (EditText) findViewById(R.id.id_drop_landmark);
        txtDropPincode = (EditText) findViewById(R.id.id_drop_pincode);

        // initialised all Auto Complete TextView
        txtComodity = (AutoCompleteTextView) findViewById(R.id.id_commodity);
        txtTypeOfPackaging = (AutoCompleteTextView) findViewById(R.id.id_type_of_package);
        txtPickup = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_pickup);
        txtDrop = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_drop);
        txtManualPickup = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_manual_pickup);
        txtManualDrop = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_manual_drop);

        // initialised all Radio Group
        rgContainerSize = (RadioGroup) findViewById(R.id.radiogroup2040);
        rgTypeOfShipment = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_transport);

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));
        if(bookId == null){
            txtBookId.setText( getResources().getString(R.string.codeString, DateHelper.getBookId(),loginId));
        }
        else {
            txtBookId.setText( bookId );
        }

        // click listener for shipment type radio group
        rgTypeOfShipment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_transport:
                        rgContainerSize.setVisibility(View.GONE);
                        inputLayout_cubicMeter.setVisibility(View.VISIBLE);
                        strSelectedShipmentType = "LCL";
                        strSelectedContainerSize = null;
                        rgContainerSize.clearCheck();
                        break;

                    case R.id.rbFcl_transport:
                        rgContainerSize.setVisibility(View.VISIBLE);
                        inputLayout_cubicMeter.setVisibility(View.GONE);
                        strSelectedShipmentType = "FCL";
                        strSelectedContainerSize = null;
                        txtCBM.setText("");
                        break;
                }
            }
        });

        // click listener for container size radio group
        rgContainerSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.feet20:
                        strSelectedContainerSize = "20";
                        txtCBM.setText("");
                        break;

                    case R.id.feet40:
                        strSelectedContainerSize = "40";
                        txtCBM.setText("");
                        break;

                    case R.id.feet40HQfeet:
                        strSelectedContainerSize = "40_HQ";
                        txtCBM.setText("");
                        break;
                }
            }
        });

        // autoselect and disable shipment type radio button
        if( availedServicesName != null ) {

            if(strSelectedShipmentType.equals("LCL")) {
                rgTypeOfShipment.check(R.id.rbLcl_transport);
            }
            else {
                rgTypeOfShipment.check(R.id.rbFcl_transport);
            }
            ((RadioButton)findViewById(R.id.rbLcl_transport)).setEnabled(false);
            ((RadioButton)findViewById(R.id.rbFcl_transport)).setEnabled(false);

        }

    }

    private void transportation() {
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;

        // initialised comodity autocomplete textfield from database
        commodity_adapter = new ArrayAdapter<CommodityModel>(this, layoutItemId, dbClass.getCommodityData() );
        txtComodity.setAdapter(commodity_adapter);
        txtComodity.setThreshold(1);
        /*txtComodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iSelectedCommodityId = commodity_adapter.getItem(position).getServerId();
            }
        });*/

        // initialised packaging type autocomplete textfield from database
        packagingType_adapter = new ArrayAdapter<PackageTypeModel>(this, layoutItemId, dbClass.getPackagingTypeData() );
        txtTypeOfPackaging.setAdapter(packagingType_adapter);
        txtTypeOfPackaging.setThreshold(1);
        /*txtTypeOfPackaging.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iSelectedPackageType = packagingType_adapter.getItem(position).getServerId();
            }
        });*/

        // set adapter to pickup location
        txtPickup.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPickup.setOnItemClickListener(this);
        txtPickup.setThreshold(1);

        // set adapter to drop location
        txtDrop.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtDrop.setOnItemClickListener(this);
        txtDrop.setThreshold(1);

        if ( availedServicesName != null ) {
            if( !availedServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())  ) {
                layuotCommonFreight.setVisibility(View.GONE);
            }

            if( !availedServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString())) {
                if( customClearanceModel.getStrShipmentType().equals("FCL") &&
                        customClearanceModel.getStuffingType().equals(getResources().getString(R.string.strFactoryStuff))) {

                    txtPickup.setText(customClearanceModel.getStuffingAddress());
                    txtPickup.setClickable(false);
                    txtPickup.setCursorVisible(false);
                    txtPickup.setFocusable(false);
                    txtPickup.setFocusableInTouchMode(false);
                }
                txtComodity.setText(customClearanceModel.getStrCommodity());
                txtComodity.setClickable(false);
                txtComodity.setCursorVisible(false);
                txtComodity.setFocusable(false);
                txtComodity.setFocusableInTouchMode(false);

                txtGrossWt.setText(""+customClearanceModel.getGrossWeight());
                txtGrossWt.setClickable(false);
                txtGrossWt.setCursorVisible(false);
                txtGrossWt.setFocusable(false);
                txtGrossWt.setFocusableInTouchMode(false);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.db_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AndroidDatabaseManager.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void storeTransportEnquiry(View view) {

        String strPackType = txtTypeOfPackaging.getText().toString();
        String strCommodity = txtComodity.getText().toString();

        Boolean boolPickUp = isEmptyString(txtPickup.getText().toString());
        Boolean boolDrop = isEmptyString(txtDrop.getText().toString());
        Boolean boolCBM = isEmptyString(txtCBM.getText().toString());
        Boolean boolGrossWt = isEmptyString(txtGrossWt.getText().toString());
        Boolean boolTypeOfPack = isEmptyString(strPackType) && (dbClass.getServerIdByPackagingType(strPackType) > 0);
        Boolean boolNoOfPack = isEmptyString(txt_noOfPack.getText().toString());
        Boolean boolCommodity = isEmptyString(strCommodity) && (dbClass.getCommodityServerID(strCommodity) > 0);
        Boolean boolDimenLen = isEmptyString(txtDimenLen.getText().toString());
        Boolean boolDimenHeight = isEmptyString(txtDimenHeight.getText().toString());
        Boolean boolDimenWeight = isEmptyString(txtDimenWidth.getText().toString());
        Boolean boolManualPickUp = isEmptyString(txtManualPickup.getText().toString());
        Boolean boolPickupLandmark = isEmptyString(txtPickupLandmark.getText().toString());
        Boolean boolPickupPincode = isEmptyString(txtPickupPincode.getText().toString());
        Boolean boolManualDrop = isEmptyString(txtManualDrop.getText().toString());
        Boolean boolDropLandmark = isEmptyString(txtDropLandmark.getText().toString());
        Boolean boolDropPincode = isEmptyString(txtDropPincode.getText().toString());

        if (boolPickUp && (layout_pickup.getVisibility() == View.VISIBLE)) {
            inputLayout_pickUp.setErrorEnabled(false);
        } else {
            inputLayout_pickUp.setErrorEnabled(true);
            inputLayout_pickUp.setError("Pick up address field is empty.");
        }

        if (boolDrop && (layout_drop.getVisibility() == View.VISIBLE)) {
            inputLayout_drop.setErrorEnabled(false);
        } else {
            inputLayout_drop.setErrorEnabled(true);
            inputLayout_drop.setError("Drop address field is empty.");
        }

        if( layout_manual_pickup.getVisibility() == View.VISIBLE) {

            if( boolManualPickUp ) {
                inputLayout_manual_pickUp.setErrorEnabled(false);
            } else {
                inputLayout_manual_pickUp.setErrorEnabled(true);
                inputLayout_manual_pickUp.setError("Pick up address field is empty.");
            }

            if( boolPickupLandmark ) {
                inputLayout_pick_landmark.setErrorEnabled(false);
            } else {
                inputLayout_pick_landmark.setErrorEnabled(true);
                inputLayout_pick_landmark.setError("Pick up landmark field is empty.");
            }

            if( boolPickupPincode )  {
                inputLayout_pickUp_pincode.setErrorEnabled(false);
            } else {
                inputLayout_pickUp_pincode.setErrorEnabled(true);
                inputLayout_pickUp_pincode.setError("Pick up pincode field is empty.");
            }
        }

        if( layout_manual_drop.getVisibility() == View.VISIBLE) {

            if( boolManualDrop ) {
                inputLayout_manual_drop.setErrorEnabled(false);
            } else {
                inputLayout_manual_drop.setErrorEnabled(true);
                inputLayout_manual_drop.setError("Drop address field is empty.");
            }

            if( boolDropLandmark ) {
                inputLayout_drop_landmark.setErrorEnabled(false);
            } else {
                inputLayout_drop_landmark.setErrorEnabled(true);
                inputLayout_drop_landmark.setError("Drop landmark field is empty.");
            }

            if( boolDropPincode )  {
                inputLayout_drop_pincode.setErrorEnabled(false);
            } else {
                inputLayout_drop_pincode.setErrorEnabled(true);
                inputLayout_drop_pincode.setError("drop pincode field is empty.");
            }
        }

        if (inputLayout_cubicMeter.getVisibility() == View.VISIBLE) {
            if (boolCBM) {
                inputLayout_cubicMeter.setErrorEnabled(false);
            } else {
                inputLayout_cubicMeter.setErrorEnabled(true);
                inputLayout_cubicMeter.setError("Cubic meter measurement field is empty.");
            }
        }
        else {
            if(strSelectedContainerSize == null && (layuotCommonFreight.getVisibility() == View.VISIBLE)){
                Toast.makeText(getApplicationContext(), "Container size of FCL should be checked.",Toast.LENGTH_LONG).show();
            }
        }

        if (boolGrossWt) {
            inputLayout_grossWeight.setErrorEnabled(false);
        } else {
            inputLayout_grossWeight.setErrorEnabled(true);
            inputLayout_grossWeight.setError("Gross weight field is empty.");
        }

        if (boolTypeOfPack) {
            inputLayout_packType.setErrorEnabled(false);
        } else {
            inputLayout_packType.setErrorEnabled(true);
            inputLayout_packType.setError("Package type field is empty or not in dropdown menu.");
        }

        if (boolNoOfPack) {
            inputLayout_noOfPack.setErrorEnabled(false);
        } else {
            inputLayout_noOfPack.setErrorEnabled(true);
            inputLayout_noOfPack.setError("Package number field is empty.");
        }

        if (boolCommodity) {
            inputLayout_commodity.setErrorEnabled(false);
        } else {
            inputLayout_commodity.setErrorEnabled(true);
            inputLayout_commodity.setError("Commodity field is empty or not in dropdown menu.");
        }

        if (boolDimenLen) {
            inputLayout_dimen_len.setErrorEnabled(false);
        } else {
            inputLayout_dimen_len.setErrorEnabled(true);
            inputLayout_dimen_len.setError("length.");
        }

        if (boolDimenHeight) {
            inputLayout_dimen_height.setErrorEnabled(false);
        } else {
            inputLayout_dimen_height.setErrorEnabled(true);
            inputLayout_dimen_height.setError("height.");
        }

        if (boolDimenWeight) {
            inputLayout_dimen_width.setErrorEnabled(false);
        } else {
            inputLayout_dimen_width.setErrorEnabled(true);
            inputLayout_dimen_width.setError("width.");
        }

        if ( boolDimenLen && boolDimenHeight && boolDimenWeight ) {

            String measurment, strPickUp, strDrop;
            if ( !boolPickUp && (layout_pickup.getVisibility() == View.VISIBLE)) {
                return;
            }

            if ( !boolDrop && (layout_drop.getVisibility() == View.VISIBLE)) {
                return;
            }

            if ( layout_manual_pickup.getVisibility() == View.VISIBLE ) {
                if( !boolManualPickUp && !boolPickupLandmark && !boolPickupPincode ) {
                    return;
                }
            }

            if ( layout_manual_drop.getVisibility() == View.VISIBLE ) {
                if( !boolManualDrop && !boolDropLandmark && !boolDropPincode ) {
                    return;
                }
            }

            if ( boolPickUp && (layout_pickup.getVisibility() == View.VISIBLE)) {
                strPickUp = txtPickup.getText().toString();
            }
            else {
                strPickUp = txtManualPickup.getText().toString()+", "+txtPickupLandmark.getText().toString()+", Pincode-"+
                        txtPickupPincode.getText().toString();
            }

            if ( boolDrop && (layout_drop.getVisibility() == View.VISIBLE)) {
                strDrop = txtDrop.getText().toString();
            }
            else {
                strDrop = txtManualDrop.getText().toString()+", "+txtDropLandmark.getText().toString()+", Pincode-"+
                        txtDropPincode.getText().toString();
            }

            int iAvail = 0;
            if( availedServicesName != null ) {
                iAvail = 1;
            }

            if (layuotCommonFreight.getVisibility() == View.VISIBLE) {

                if( boolGrossWt && boolTypeOfPack && boolNoOfPack && boolCommodity) {

                    if ((inputLayout_cubicMeter.getVisibility() == View.VISIBLE && !boolCBM) ) {
                        return;
                    }
                    else if( rgContainerSize.getVisibility() == View.VISIBLE && strSelectedContainerSize == null ) {
                        return;
                    }

                    if (inputLayout_cubicMeter.getVisibility() == View.VISIBLE) {
                        measurment = txtCBM.getText().toString();
                    }
                    else {
                        measurment = strSelectedContainerSize;
                    }

                    TransportationModel transportationModel = new TransportationModel(
                            txtBookId.getText().toString(),
                            strPickUp,
                            strDrop,
                            strSelectedShipmentType,
                            measurment,
                            Float.parseFloat(txtGrossWt.getText().toString()),
                            strPackType,
                            Integer.parseInt(txt_noOfPack.getText().toString()),
                            strCommodity,
                            Integer.parseInt(txtDimenLen.getText().toString()),
                            Integer.parseInt(txtDimenHeight.getText().toString()),
                            Integer.parseInt(txtDimenWidth.getText().toString()),
                            iAvail,
                            1,
                            ""+DateHelper.convertToMillis(),
                            loginId
                    );

                    intentActions(transportationModel);
                }

            } else {

                TransportationModel transportationModel = new TransportationModel(
                        txtBookId.getText().toString(),
                        strPickUp,
                        strDrop,
                        freightForwardingModel.getStrShipmentType(),
                        freightForwardingModel.getMeasurement(),
                        freightForwardingModel.getGrossWeight(),
                        freightForwardingModel.getStrPackType(),
                        freightForwardingModel.getNoOfPack(),
                        freightForwardingModel.getStrCommodity(),
                        Integer.parseInt(txtDimenLen.getText().toString()),
                        Integer.parseInt(txtDimenHeight.getText().toString()),
                        Integer.parseInt(txtDimenWidth.getText().toString()),
                        iAvail,
                        1,
                        ""+DateHelper.convertToMillis(),
                        loginId
                );

                intentActions(transportationModel);
            }

            /*System.out.println("Booked id: "+txtBookId.getText().toString());
            System.out.println("Pick up: "+strPickUp);
            System.out.println("Drop: "+strDrop);
            System.out.println("Type of shipment: "+strSelectedShipmentType);
            System.out.println("Measurement: "+measurment);
            System.out.println("gross weight: "+txtGrossWt.getText().toString());
            System.out.println("Type of pack: "+txtTypeOfPackaging.getText().toString());
            System.out.println("No of pack: "+txt_noOfPack.getText().toString());
            System.out.println("Commodity: "+txtComodity.getText().toString());
            System.out.println("Length: "+txtDimenLen.getText().toString());
            System.out.println("Height: "+txtDimenHeight.getText().toString());
            System.out.println("Weight: "+txtDimenWidth.getText().toString());*/

        }
    }

    private void intentActions(TransportationModel transportationModel) {

        Intent intentActivity = null;
        Boolean isIntentQuote=false;

        if( availedServicesName != null ) {
            if( availedServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString()) ) {
                intentActivity = new Intent(TransportQuotationActivity.this, CustomClearanceActivity.class);
            }
            else if(availedServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                intentActivity = new Intent(TransportQuotationActivity.this, FreightForwardingActivity.class);
            }
            else {
//                intentActivity = new Intent(TransportQuotationActivity.this, QuotationActivity.class);
                isIntentQuote=true;
            }
        }
        else {
            if( names.contains(enumServices.CUSTOM_CLEARANCE.toString()) ) {
                intentActivity = new Intent(TransportQuotationActivity.this, CustomClearanceActivity.class);
            }
            else if(names.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                intentActivity = new Intent(TransportQuotationActivity.this, FreightForwardingActivity.class);
            }
            else {
//                intentActivity = new Intent(TransportQuotationActivity.this, QuotationActivity.class);
                isIntentQuote=true;
            }
        }

        if(isIntentQuote) {
            // Check if Internet present
            if (!isConnectingToInternet(TransportQuotationActivity.this)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection),Toast.LENGTH_LONG).show();
                // stop executing code by return
                return;
            } else {
                InsertTransportationAsyncTask.postTransportationData(
                        TransportQuotationActivity.this,
                        transportationModel);
            }
        }
        else {
            intentActivity.putStringArrayListExtra("ServicesId",  ids);
            intentActivity.putStringArrayListExtra("ServicesName", names);
            intentActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
            intentActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
            intentActivity.putExtra("transportData",transportationModel);
            intentActivity.putExtra("customClearanceData",customClearanceModel);

            startActivity(intentActivity);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void togglePickUpAddressFiled(View view) {
        layout_pickup.setVisibility(View.GONE);
        id_not_find_pick_google_text.setVisibility(View.GONE);
        layout_manual_pickup.setVisibility(View.VISIBLE);
        id_backTo_pick_google_text.setVisibility(View.VISIBLE);
    }

    public void back_google_pick_address(View view) {
        layout_pickup.setVisibility(View.VISIBLE);
        id_not_find_pick_google_text.setVisibility(View.VISIBLE);
        layout_manual_pickup.setVisibility(View.GONE);
        id_backTo_pick_google_text.setVisibility(View.GONE);
    }

    public void toggleDropAddressFiled(View view) {
        layout_drop.setVisibility(View.GONE);
        id_not_find_drop_google_text.setVisibility(View.GONE);
        layout_manual_drop.setVisibility(View.VISIBLE);
        id_backTo_drop_google_text.setVisibility(View.VISIBLE);
    }

    public void back_google_drop_address(View view) {
        layout_drop.setVisibility(View.VISIBLE);
        id_not_find_drop_google_text.setVisibility(View.VISIBLE);
        layout_manual_drop.setVisibility(View.GONE);
        id_backTo_drop_google_text.setVisibility(View.GONE);
    }
}
