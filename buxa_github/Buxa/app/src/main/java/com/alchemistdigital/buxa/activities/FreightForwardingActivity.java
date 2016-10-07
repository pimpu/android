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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
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

import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class FreightForwardingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    LinearLayout layoutCommomTransFeild;
    ArrayAdapter<CommodityModel> commodity_adapter;
    ArrayAdapter<PackageTypeModel> packagingType_adapter;
    AutoCompleteTextView txtComodity, txtTypeOfPackaging, txtPickup, txtDrop, txtPolAdr, txtPodAdr;
    RadioGroup rgContainerSize, rgTypeOfShipment;
    DatabaseClass dbClass ;
    ArrayList<String> arrayServicesId, arrayServicesName, availedServicesId, availedServicesName;
    String strShipmentType = "LCL", bookId, strSelectedContainerSize = null;
    private EditText txtBookId, txtCBM, txtGrossWt, txt_noOfPack, txtDimenLen,
            txtDimenHeight, txtDimenWidth;
    TextInputLayout inputLayout_pickUp, inputLayout_drop, inputLayout_cubicMeter, inputLayout_grossWeight,
            inputLayout_packType, inputLayout_noOfPack, inputLayout_commodity,
            inputLayout_dimen_len, inputLayout_dimen_height, inputLayout_dimen_width,
            inputLayout_POLAddress, inputLayout_PODAddress;
    private int iSelectedCommodityId, iSelectedPackageType;
    private int loginId;
    private TransportationModel transportDataModel;
    private CustomClearanceModel customClearanceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight_forwarding);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        availedServicesId = getIntent().getStringArrayListExtra("availedServicesId");
        availedServicesName = getIntent().getStringArrayListExtra("availedServicesName");

        // shipmentType and bookId intent is comes from quotation activity
        if ( getIntent().getExtras().getString("shipmentType") != null ) {
            strShipmentType = getIntent().getExtras().getString("shipmentType");
        }
        bookId = getIntent().getExtras().getString("bookId");

        transportDataModel = getIntent().getExtras().getParcelable("transportData");
        customClearanceModel = getIntent().getExtras().getParcelable("customClearanceData");

        if ( transportDataModel  != null ) {
            strShipmentType = transportDataModel.getStrShipmentType();
            bookId = transportDataModel.getBookingId();
        }
        else if( customClearanceModel != null) {
            strShipmentType = customClearanceModel.getStrShipmentType();
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
            else if(newMessage.equals("gotoNextActivity_FF")) {
//                intentActions(intent);
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    private void intentActions(FreightForwardingModel freightForwardingModel) {
        Intent intentActivity = new Intent(this, QuotationActivity.class);

//        FreightForwardingModel freightForwardingModel = intent.getExtras().getParcelable("FFData");

        intentActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
        intentActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
        intentActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
        intentActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
        intentActivity.putExtra("freightForwardingModel", freightForwardingModel);
        intentActivity.putExtra("customClearanceData", customClearanceModel);
        intentActivity.putExtra("transportData", transportDataModel);
        startActivity(intentActivity);

    }

    private void init() {
        layoutCommomTransFeild = (LinearLayout) findViewById(R.id.id_commomTransportLayout);

        // initialised all Text Input Layout
        inputLayout_POLAddress = (TextInputLayout) findViewById(R.id.input_layout_POL);
        inputLayout_PODAddress = (TextInputLayout) findViewById(R.id.input_layout_POD);
        inputLayout_pickUp = (TextInputLayout) findViewById(R.id.input_layout_pick_up_ff);
        inputLayout_drop = (TextInputLayout) findViewById(R.id.input_layout_drop_ff);
        inputLayout_cubicMeter = (TextInputLayout) findViewById(R.id.input_layout_cubic_meter_measurement_ff);
        inputLayout_grossWeight = (TextInputLayout) findViewById(R.id.input_layout_gross_weight_ff);
        inputLayout_packType = (TextInputLayout) findViewById(R.id.input_layout_pack_type_ff);
        inputLayout_noOfPack = (TextInputLayout) findViewById(R.id.input_layout_no_of_package_ff);
        inputLayout_commodity = (TextInputLayout) findViewById(R.id.input_layout_commodity_ff);
        inputLayout_dimen_len = (TextInputLayout) findViewById(R.id.input_layout_dimensions_length_ff);
        inputLayout_dimen_height = (TextInputLayout) findViewById(R.id.input_layout_dimensions_height_ff);
        inputLayout_dimen_width = (TextInputLayout) findViewById(R.id.input_layout_dimensions_width_ff);

        // initialised all Edit Text
        txtBookId = (EditText) findViewById(R.id.book_id_ff);
        txtCBM = (EditText) findViewById(R.id.id_cubic_meter_measurement_ff);
        txtGrossWt = (EditText) findViewById(R.id.id_gross_weight_ff);
        txt_noOfPack = (EditText) findViewById(R.id.id_no_of_package_ff);
        txtDimenLen = (EditText) findViewById(R.id.id_dimensions_length_ff);
        txtDimenHeight = (EditText) findViewById(R.id.id_dimensions_height_ff);
        txtDimenWidth = (EditText) findViewById(R.id.id_dimensions_width_ff);

        // initialised all Auto Complete TextView
        txtComodity = (AutoCompleteTextView) findViewById(R.id.id_commodity_ff);
        txtTypeOfPackaging = (AutoCompleteTextView) findViewById(R.id.id_type_of_package_ff);
        txtPickup = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_pickup_ff);
        txtDrop = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_drop_ff);
        txtPolAdr = (AutoCompleteTextView) findViewById(R.id.id_POL_addresses);
        txtPodAdr = (AutoCompleteTextView) findViewById(R.id.id_POD_addresses);

        // initialised all Radio Group
        rgContainerSize = (RadioGroup) findViewById(R.id.radiogroup2040_ff);
        rgTypeOfShipment = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_ff);

        if(bookId == null){
            GetSharedPreference getSharedPreference = new GetSharedPreference(this);
            loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));
            txtBookId.setText( getResources().getString(R.string.codeString, DateHelper.getBookId(),loginId));
        }
        else {
            txtBookId.setText( bookId );
        }

        int layoutItemId = android.R.layout.simple_dropdown_item_1line;

        // initialised comodity autocomplete textfield from database
        commodity_adapter = new ArrayAdapter<CommodityModel>(this, layoutItemId, dbClass.getCommodityData() );
        txtComodity.setAdapter(commodity_adapter);
        txtComodity.setThreshold(1);
        txtComodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iSelectedCommodityId = commodity_adapter.getItem(position).getServerId();
            }
        });

        // initialised packaging type autocomplete textfield from database
        packagingType_adapter = new ArrayAdapter<PackageTypeModel>(this, layoutItemId, dbClass.getPackagingTypeData() );
        txtTypeOfPackaging.setAdapter(packagingType_adapter);
        txtTypeOfPackaging.setThreshold(1);
        txtTypeOfPackaging.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iSelectedPackageType = packagingType_adapter.getItem(position).getServerId();
            }
        });


        // set adapter to pickup location
        txtPickup.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPickup.setOnItemClickListener(this);
        txtPickup.setThreshold(1);

        // set adapter to drop location
        txtDrop.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtDrop.setOnItemClickListener(this);
        txtDrop.setThreshold(1);

        // set adapter to drop location
        txtPolAdr.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPolAdr.setOnItemClickListener(this);
        txtPolAdr.setThreshold(1);

        // set adapter to drop location
        txtPodAdr.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPodAdr.setOnItemClickListener(this);
        txtPodAdr.setThreshold(1);

        // click listener for shipment type radio group
        rgTypeOfShipment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_ff:
                        rgContainerSize.setVisibility(View.GONE);
                        inputLayout_cubicMeter.setVisibility(View.VISIBLE);
                        strShipmentType = "LCL";
                        strSelectedContainerSize = null;
                        rgContainerSize.clearCheck();
                        break;

                    case R.id.rbFcl_ff:
                        rgContainerSize.setVisibility(View.VISIBLE);
                        inputLayout_cubicMeter.setVisibility(View.GONE);
                        strShipmentType = "FCL";
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
                    case R.id.feet20_ff:
                        strSelectedContainerSize = "20";
                        txtCBM.setText("");
                        break;

                    case R.id.feet40_ff:
                        strSelectedContainerSize = "40";
                        txtCBM.setText("");
                        break;

                    case R.id.feet40HQfeet_ff:
                        strSelectedContainerSize = "40_HQ";
                        txtCBM.setText("");
                        break;
                }
            }
        });

        if( availedServicesName != null ||
                ((arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) ||
                arrayServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString())) &&
                arrayServicesId.size() > 0 )) {
            if(strShipmentType.equals("LCL")) {
                rgTypeOfShipment.check(R.id.rbLcl_ff);
            }
            else {
                rgTypeOfShipment.check(R.id.rbFcl_ff);
            }

            ((RadioButton)findViewById(R.id.rbLcl_ff)).setEnabled(false);
            ((RadioButton)findViewById(R.id.rbFcl_ff)).setEnabled(false);
        }

        if ( availedServicesName != null ||
                ( arrayServicesName.contains(enumServices.TRANSPORTATION.toString()) &&
                        arrayServicesId.size() > 0 ) ) {

            layoutCommomTransFeild.setVisibility(View.GONE);
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
        FreightForwardingModel freightForwardingModel = null;

        Boolean boolPOL = isEmptyString(txtPolAdr.getText().toString());
        Boolean boolPOD = isEmptyString(txtPodAdr.getText().toString());
        Boolean boolPickUp = isEmptyString(txtPickup.getText().toString());
        Boolean boolDrop = isEmptyString(txtDrop.getText().toString());
        Boolean boolCBM = isEmptyString(txtCBM.getText().toString());
        Boolean boolGrossWt = isEmptyString(txtGrossWt.getText().toString());
        Boolean boolTypeOfPack = isEmptyString(txtTypeOfPackaging.getText().toString());
        Boolean boolNoOfPack = isEmptyString(txt_noOfPack.getText().toString());
        Boolean boolCommodity = isEmptyString(txtComodity.getText().toString());
        Boolean boolDimenLen = isEmptyString(txtDimenLen.getText().toString());
        Boolean boolDimenHeight = isEmptyString(txtDimenHeight.getText().toString());
        Boolean boolDimenWeight = isEmptyString(txtDimenWidth.getText().toString());

        if (boolPOL) {
            inputLayout_POLAddress.setErrorEnabled(false);
        } else {
            inputLayout_POLAddress.setErrorEnabled(true);
            inputLayout_POLAddress.setError("Port of loading address field is empty.");
        }

        if (boolPOD) {
            inputLayout_PODAddress.setErrorEnabled(false);
        } else {
            inputLayout_PODAddress.setErrorEnabled(true);
            inputLayout_PODAddress.setError("Port of destination address field is empty.");
        }

        if (boolPickUp) {
            inputLayout_pickUp.setErrorEnabled(false);
        } else {
            inputLayout_pickUp.setErrorEnabled(true);
            inputLayout_pickUp.setError("Pick up address field is empty.");
        }

        if (boolDrop) {
            inputLayout_drop.setErrorEnabled(false);
        } else {
            inputLayout_drop.setErrorEnabled(true);
            inputLayout_drop.setError("Drop address field is empty.");
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
            if(strSelectedContainerSize == null && (layoutCommomTransFeild.getVisibility() == View.VISIBLE) ){
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
            inputLayout_packType.setError("Package type field is empty.");
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
            inputLayout_commodity.setError("Commodity field is empty.");
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

        if (layoutCommomTransFeild.getVisibility() == View.GONE && boolPOL && boolPOD) {
            int iAvail = 0;
            if( availedServicesName != null ) {
                iAvail = 1;
            }

            freightForwardingModel = new FreightForwardingModel(
                    txtBookId.getText().toString(),
                    txtPolAdr.getText().toString(),
                    txtPodAdr.getText().toString(),
                    iAvail,
                    1,
                    ""+DateHelper.convertToMillis(),
                    strShipmentType,
                    loginId,
                    dbClass.getShipmentTypeServerId(strShipmentType)
            );

            intentActions(freightForwardingModel);
        }
        else if ( boolPickUp && boolDrop && boolGrossWt && boolTypeOfPack && boolNoOfPack && boolDimenLen
                && boolDimenHeight && boolDimenWeight && boolCommodity && boolPOL && boolPOD ) {

            if ((inputLayout_cubicMeter.getVisibility() == View.VISIBLE && !boolCBM) ) {
                return;
            }
            else if( rgContainerSize.getVisibility() == View.VISIBLE && strSelectedContainerSize == null ) {
                return;
            }

            String measurment;
            if (inputLayout_cubicMeter.getVisibility() == View.VISIBLE) {
                measurment = txtCBM.getText().toString();
            }
            else {
                measurment = strSelectedContainerSize;
            }

            int iAvail = 0;
            if( availedServicesName != null ) {
                iAvail = 1;
            }

            freightForwardingModel = new FreightForwardingModel(
                    txtBookId.getText().toString(),
                    txtPolAdr.getText().toString(),
                    txtPodAdr.getText().toString(),
                    iAvail,
                    1,
                    ""+DateHelper.convertToMillis(),
                    strShipmentType,
                    loginId,
                    dbClass.getShipmentTypeServerId(strShipmentType)
            );

            intentActions(freightForwardingModel);

            /*// Check if Internet present
            if (!isConnectingToInternet(FreightForwardingActivity.this)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection),Toast.LENGTH_LONG).show();
                // stop executing code by return
                return;
            } else {
                InsertFreightForwardingAsyncTask.postFreightForwardingData(
                        FreightForwardingActivity.this,
                        freightForwardingModel);
            }*/

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
