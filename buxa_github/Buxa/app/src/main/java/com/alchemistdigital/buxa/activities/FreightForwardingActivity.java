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
import android.widget.Spinner;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.adapter.CustomSpinnerAdapter;
import com.alchemistdigital.buxa.asynctask.InsertCustomClearanceAsyncTask;
import com.alchemistdigital.buxa.asynctask.InsertFreightForwardingAsyncTask;
import com.alchemistdigital.buxa.asynctask.InsertTransportationAsyncTask;
import com.alchemistdigital.buxa.model.CFSAddressModel;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.CustomClearanceModel;
import com.alchemistdigital.buxa.model.FreightForwardingModel;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.ShipAreaVariableSingleton;
import com.alchemistdigital.buxa.utilities.WakeLocker;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;
import java.util.Arrays;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class FreightForwardingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private LinearLayout layoutCommomTransFeild;
    private ArrayAdapter<CommodityModel> commodity_adapter;
    private ArrayAdapter<PackageTypeModel> packagingType_adapter;
    public AutoCompleteTextView txtComodity, txtTypeOfPackaging, txtDestinationDeliveryAdr, txtPortOfCountry;
    private RadioGroup rgContainerSize, rgTypeOfShipment;
    private DatabaseClass dbClass ;
    private ArrayList<String> arrayServicesId, arrayServicesName, availedServicesId, availedServicesName;
    private String strShipmentType = "LCL", bookId, strSelectedContainerSize = null, selectedIncoterm;
    private EditText txtBookId, txtCBM, txtGrossWt, txt_noOfPack;
    public TextInputLayout inputLayout_destinationDeliveryAdr, inputLayout_cubicMeter, inputLayout_grossWeight,
            inputLayout_packType, inputLayout_noOfPack, inputLayout_commodity, inputLayout_poc;
    public int iSelectedCommodityId, iSelectedPackageType, iSelectedCfsAdr;
    private int loginId, layoutItemId;
    private TransportationModel transportDataModel;
    public static CustomClearanceModel customClearanceModel_ff;
    public ArrayAdapter<CFSAddressModel> cfs_adapter;
    private Spinner spinnerIncoterm, spinnerPOD, spinnerPOL; // spinnerPOC
    private Boolean boolIsDDAVisible=false;
    private CustomSpinnerAdapter adapterIncoterm, adapterPOL, adapterPortOfDestination;  // adapterPortOfCountry,

    public static Boolean isCCService = false;
    public static FreightForwardingModel freightForwardingModel;
    private ArrayAdapter<String> adapterPortOfCountry;

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
        customClearanceModel_ff = getIntent().getExtras().getParcelable("customClearanceData");

        if ( transportDataModel  != null ) {
            strShipmentType = transportDataModel.getStrShipmentType();
            bookId = transportDataModel.getBookingId();
        }
        else if( customClearanceModel_ff != null) {
            strShipmentType = customClearanceModel_ff.getStrShipmentType();
            bookId = customClearanceModel_ff.getBookingId();
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
            else if(newMessage.equals("gotoQuotationActivityFromFF")) {
                Intent intentActivity = new Intent(FreightForwardingActivity.this, QuotationActivity.class);

                intentActivity.putStringArrayListExtra("ServicesId",  arrayServicesId);
                intentActivity.putStringArrayListExtra("ServicesName", arrayServicesName);
                intentActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentActivity.putExtra("freightForwardingModel", intent.getExtras().getParcelable("FFData"));
                intentActivity.putExtra("customClearanceData", customClearanceModel_ff);
                intentActivity.putExtra("transportData", transportDataModel);
                startActivity(intentActivity);

            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    private void init() {
        layoutCommomTransFeild = (LinearLayout) findViewById(R.id.id_commomTransportLayout);

        // initialised all Text Input Layout
//        inputLayout_CfsAddress = (TextInputLayout) findViewById(R.id.input_layout_cfs);
        inputLayout_destinationDeliveryAdr = (TextInputLayout) findViewById(R.id.input_layout_destination_delivery_adr);
        inputLayout_cubicMeter = (TextInputLayout) findViewById(R.id.input_layout_cubic_meter_measurement_ff);
        inputLayout_grossWeight = (TextInputLayout) findViewById(R.id.input_layout_gross_weight_ff);
        inputLayout_packType = (TextInputLayout) findViewById(R.id.input_layout_pack_type_ff);
        inputLayout_noOfPack = (TextInputLayout) findViewById(R.id.input_layout_no_of_package_ff);
        inputLayout_commodity = (TextInputLayout) findViewById(R.id.input_layout_commodity_ff);
        inputLayout_poc = (TextInputLayout) findViewById(R.id.input_layout_POC);

        // initialised all Edit Text
        txtBookId = (EditText) findViewById(R.id.book_id_ff);
        txtCBM = (EditText) findViewById(R.id.id_cubic_meter_measurement_ff);
        txtGrossWt = (EditText) findViewById(R.id.id_gross_weight_ff);
        txt_noOfPack = (EditText) findViewById(R.id.id_no_of_package_ff);

        // initialised all Auto Complete TextView
        txtComodity = (AutoCompleteTextView) findViewById(R.id.id_commodity_ff);
        txtTypeOfPackaging = (AutoCompleteTextView) findViewById(R.id.id_type_of_package_ff);
//        txtCfsAdr = (AutoCompleteTextView) findViewById(R.id.id_cfs_address);
        txtDestinationDeliveryAdr = (AutoCompleteTextView) findViewById(R.id.id_destination_delivery_adr);
        txtPortOfCountry = (AutoCompleteTextView) findViewById(R.id.id_POC);

        // Incoterm Spinner
        spinnerIncoterm = (Spinner) findViewById(R.id.id_spinner_incoterm);
//        spinnerPOC = (Spinner) findViewById(R.id.id_spinner_poc);
        spinnerPOD = (Spinner) findViewById(R.id.id_spinner_pod);
        spinnerPOL = (Spinner) findViewById(R.id.id_spinner_pol);

        // initialised all Radio Group
        rgContainerSize = (RadioGroup) findViewById(R.id.radiogroup2040_ff);
        rgTypeOfShipment = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_ff);

        // populate port of loading spinner
        ArrayList<String> stringArrayPOL = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.loadingPort)));
        adapterPOL = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, stringArrayPOL);
        adapterPOL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPOL.setAdapter(adapterPOL);

        // populate incoterm adapter spinner
        ArrayList<String> stringArrayIncoterm = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.incoterms)));
        adapterIncoterm = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, stringArrayIncoterm);
        adapterIncoterm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIncoterm.setAdapter(adapterIncoterm);

        // click event on incoterm spinner
        // if selected spinner item is DAP and DDP
        // then show destination delivery address EditText field
        spinnerIncoterm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIncoterm = spinnerIncoterm.getSelectedItem().toString();
                String strDap = getResources().getString(R.string.dap);
                String strDdp = getResources().getString(R.string.ddp);

                if( selectedIncoterm.equals(strDap) || selectedIncoterm.equals(strDdp) ){
                    inputLayout_destinationDeliveryAdr.setVisibility(View.VISIBLE);
                    boolIsDDAVisible = true;
                }
                else {
                    inputLayout_destinationDeliveryAdr.setVisibility(View.GONE);
                    boolIsDDAVisible = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetSharedPreference getSharedPreference = new GetSharedPreference(this);
        loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));
        if(bookId == null){
            txtBookId.setText( getResources().getString(R.string.codeString, DateHelper.getBookId(),loginId));
        }
        else {
            txtBookId.setText( bookId );
        }

        layoutItemId = android.R.layout.simple_dropdown_item_1line;

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

        // initialised cfs address autocomplete textfield from database
        /*cfs_adapter = new ArrayAdapter<CFSAddressModel>(this, layoutItemId, dbClass.getCfsData() );
        txtCfsAdr.setAdapter(cfs_adapter);
        txtCfsAdr.setThreshold(1);
        txtCfsAdr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iSelectedCfsAdr = cfs_adapter.getItem(position).getServerId();
            }
        });*/

        // port of international country spinner
        adapterPortOfCountry = new ArrayAdapter<String>(this, layoutItemId, dbClass.getPortOfCountry());
        System.out.println(dbClass.getPortOfCountry());
        txtPortOfCountry.setAdapter(adapterPortOfCountry);
        txtPortOfCountry.setThreshold(1);
        txtPortOfCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // port of international destination spinner
                populatePODSpinner();
            }
        });

        /*spinnerPOC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // port of international country spinner
                populatePODSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


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

        if(arrayServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString())) {
            System.out.println(customClearanceModel_ff.getStrCommodity());
            txtComodity.setText(customClearanceModel_ff.getStrCommodity());
            txtComodity.setClickable(false);
            txtComodity.setCursorVisible(false);
            txtComodity.setFocusable(false);
            txtComodity.setFocusableInTouchMode(false);

            txtGrossWt.setText(""+customClearanceModel_ff.getGrossWeight());
            txtGrossWt.setClickable(false);
            txtGrossWt.setCursorVisible(false);
            txtGrossWt.setFocusable(false);
            txtGrossWt.setFocusableInTouchMode(false);
        }
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
                inputLayout_destinationDeliveryAdr.setErrorEnabled(false);
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

    /**
     * get international destination port with respect to international country
     */
    private void populatePODSpinner() {
        // port of international destination spinner
        adapterPortOfDestination = new CustomSpinnerAdapter(this,
                android.R.layout.simple_spinner_item,
                dbClass.getPortOfDestination( txtPortOfCountry.getText().toString() ));
        adapterPortOfDestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPOD.setAdapter(adapterPortOfDestination);

    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_freight_forwarding);
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
        getSupportActionBar().setTitle("Freight Forwarding");
        getSupportActionBar().setSubtitle(ShipAreaVariableSingleton.getInstance().shipAreaName);
    }

    public void storeFreightForwardingEnquiry(View view) {
        String strCommodity = txtComodity.getText().toString();
        String strTypeOfPack = txtTypeOfPackaging.getText().toString();

        Boolean boolPOC = isEmptyString(txtPortOfCountry.getText().toString());
        Boolean boolDDAdr = isEmptyString(txtDestinationDeliveryAdr.getText().toString());
        Boolean boolCBM = isEmptyString(txtCBM.getText().toString());
        Boolean boolGrossWt = isEmptyString(txtGrossWt.getText().toString());
        Boolean boolTypeOfPack = isEmptyString(strTypeOfPack) && (dbClass.getServerIdByPackagingType(strTypeOfPack) > 0);
        Boolean boolNoOfPack = isEmptyString(txt_noOfPack.getText().toString());
        Boolean boolCommodity = isEmptyString(strCommodity) && (dbClass.getCommodityServerID(strCommodity) > 0);

        if (boolDDAdr) {
            inputLayout_destinationDeliveryAdr.setErrorEnabled(false);
        } else {
            inputLayout_destinationDeliveryAdr.setErrorEnabled(true);
            inputLayout_destinationDeliveryAdr.setError(getResources().getString(R.string.hint_destination_delivery_adr)+
                                                " field is empty.");
        }

        if (boolPOC) {
            inputLayout_poc.setErrorEnabled(false);
        } else {
            inputLayout_poc.setErrorEnabled(true);
            inputLayout_poc.setError("Port of country field is empty.");
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

        int iAvail = 0;
        if( availedServicesName != null ) {
            iAvail = 1;
        }

        if (layoutCommomTransFeild.getVisibility() == View.GONE ) {

            if( boolIsDDAVisible && !boolDDAdr ) {
                return;
            }

            if( !boolPOC ) {
                return;
            }

            /*System.out.println("booking id: "+txtBookId.getText().toString());
            System.out.println("POL: "+spinnerPOL.getSelectedItem().toString());
            System.out.println("POC: "+spinnerPOC.getSelectedItem().toString());
            System.out.println("POD: "+spinnerPOD.getSelectedItem().toString());
            System.out.println("Incoterm: "+spinnerIncoterm.getSelectedItem().toString());
            System.out.println("DDA: "+txtDestinationDeliveryAdr.getText().toString());*/

            freightForwardingModel = new FreightForwardingModel(
                    txtBookId.getText().toString(),
                    spinnerPOL.getSelectedItem().toString(),
                    txtPortOfCountry.getText().toString(),
                    spinnerPOD.getSelectedItem().toString(),
                    spinnerIncoterm.getSelectedItem().toString(),
                    txtDestinationDeliveryAdr.getText().toString(),
                    strShipmentType,
                    transportDataModel.getMeasurement(),
                    transportDataModel.getGrossWeight(),
                    transportDataModel.getStrPackType(),
                    transportDataModel.getNoOfPack(),
                    transportDataModel.getStrCommodity(),
                    iAvail,
                    1,
                    ""+DateHelper.convertToMillis(),
                    loginId
            );

            intentActions();
        }
        else if ( boolGrossWt && boolTypeOfPack && boolNoOfPack && boolCommodity && boolPOC ) {

            if( boolIsDDAVisible && !boolDDAdr ) {
                return;
            }
            else if ((inputLayout_cubicMeter.getVisibility() == View.VISIBLE && !boolCBM) ) {
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

            freightForwardingModel = new FreightForwardingModel(
                    txtBookId.getText().toString(),
                    spinnerPOL.getSelectedItem().toString(),
                    txtPortOfCountry.getText().toString(),
                    spinnerPOD.getSelectedItem().toString(),
                    spinnerIncoterm.getSelectedItem().toString(),
                    txtDestinationDeliveryAdr.getText().toString(),
                    strShipmentType,
                    measurment,
                    Float.parseFloat(txtGrossWt.getText().toString()),
                    strTypeOfPack,
                    Integer.parseInt(txt_noOfPack.getText().toString()),
                    strCommodity,
                    iAvail,
                    1,
                    ""+DateHelper.convertToMillis(),
                    loginId
            );

            intentActions();
        }
    }

    private void intentActions() {
        Boolean isTransService = false;

        if( availedServicesName != null ) {
            if(availedServicesName.contains(enumServices.TRANSPORTATION.toString())) {
                isTransService=true;
            }

            if(availedServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString())) {
                isCCService = true;
            }
        }
        else {
            if(arrayServicesName.contains(enumServices.TRANSPORTATION.toString())) {
                isTransService=true;
            }

            if(arrayServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString())) {
                isCCService = true;
            }
        }

        // hide soft keyboard
        View viewFocus = getCurrentFocus();
        if (viewFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }

        // Check if Internet present
        if (!isConnectingToInternet(FreightForwardingActivity.this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection),Toast.LENGTH_LONG).show();
            // stop executing code by return
            return;
        } else {
            if(isTransService) {
                InsertTransportationAsyncTask.postTransportationData(
                        FreightForwardingActivity.this,
                        transportDataModel);
            }
            else if(isCCService) {
                InsertCustomClearanceAsyncTask.postCustomClearanceData(
                        FreightForwardingActivity.this,
                        customClearanceModel_ff);
            }
            else {
                InsertFreightForwardingAsyncTask.postFreightForwardingData(
                        FreightForwardingActivity.this,
                        freightForwardingModel);
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
