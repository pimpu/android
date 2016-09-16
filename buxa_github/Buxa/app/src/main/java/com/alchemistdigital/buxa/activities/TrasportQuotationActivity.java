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

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.SegoeFontEdittext;
import com.alchemistdigital.buxa.utilities.GooglePlacesAutocompleteAdapter;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;

import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;

public class TrasportQuotationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayAdapter<CommodityModel> commodity_adapter;
    ArrayAdapter<PackageTypeModel> packagingType_adapter;
    AutoCompleteTextView txtComodity, txtTypeOfPackaging, txtPickup, txtDrop;
    private LinearLayout commodityLayout, shipmentTypeLayout, packageTypeLayout, noOfPackageLayout,
                dimensionLayout, pickupLayout, dropLayout;
    RadioGroup rgContainerSize, rgTypeOfShipment;
    DatabaseClass dbClass ;
    ArrayList<String> ids, names, availedServicesId, availedServicesName;
    String strSelectedShipmentType = "LCL", bookId, strSelectedContainerSize = null;
    private EditText txtBookId, txtCBM, txtGrossWt, txt_noOfPack, txtDimenLen,
                    txtDimenHeight, txtDimenWeight;
    TextInputLayout inputLayout_pickUp, inputLayout_drop, inputLayout_cubicMeter, inputLayout_grossWeight,
                    inputLayout_packType, inputLayout_noOfPack, inputLayout_commodity,
                    inputLayout_dimen_len, inputLayout_dimen_height, inputLayout_dimen_weight;
    private int iSelectedCommodityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasport_quotation);

        ids = getIntent().getStringArrayListExtra("ServicesId");
        names = getIntent().getStringArrayListExtra("ServicesName");
        availedServicesId = getIntent().getStringArrayListExtra("availedServicesId");
        availedServicesName = getIntent().getStringArrayListExtra("availedServicesName");
        if ( getIntent().getExtras().getString("shipmentType") != null ) {
            strSelectedShipmentType = getIntent().getExtras().getString("shipmentType");
        }
        bookId = getIntent().getExtras().getString("bookId");
        dbClass = new DatabaseClass(this);

        toolbarSetup();

        init();

        transportation();

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

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_transportQuotation);
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
        getSupportActionBar().setTitle("Transportation");
    }

    private void init() {
        // initialised all linear layout
        /*commodityLayout = (LinearLayout) findViewById(R.id.layout_commodity);
        shipmentTypeLayout = (LinearLayout) findViewById(R.id.layout_shipmentType);
        packageTypeLayout = (LinearLayout) findViewById(R.id.layout_PackageType);
        noOfPackageLayout = (LinearLayout) findViewById(R.id.layout_NoOfPackage);
        dimensionLayout = (LinearLayout) findViewById(R.id.layout_dimension);
        pickupLayout = (LinearLayout) findViewById(R.id.layout_pickup);
        dropLayout = (LinearLayout) findViewById(R.id.layout_drop);*/

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
        inputLayout_dimen_weight = (TextInputLayout) findViewById(R.id.input_layout_dimensions_weight);

        // initialised all Edit Text
        txtBookId = (EditText) findViewById(R.id.book_id_trans);
        txtCBM = (EditText) findViewById(R.id.id_cubic_meter_measurement);
        txtGrossWt = (EditText) findViewById(R.id.id_gross_weight);
        txt_noOfPack = (EditText) findViewById(R.id.id_no_of_package);
        txtDimenLen = (EditText) findViewById(R.id.id_dimensions_length);
        txtDimenHeight = (EditText) findViewById(R.id.id_dimensions_height);
        txtDimenWeight = (EditText) findViewById(R.id.id_dimensions_weight);

        // initialised all Auto Complete TextView
        txtComodity = (AutoCompleteTextView) findViewById(R.id.id_commodity);
        txtTypeOfPackaging = (AutoCompleteTextView) findViewById(R.id.id_type_of_package);
        txtPickup = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_pickup);
        txtDrop = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_drop);

        // initialised all Radio Group
        rgContainerSize = (RadioGroup) findViewById(R.id.radiogroup2040);
        rgTypeOfShipment = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_transport);

        if(bookId == null){
            GetSharedPreference getSharedPreference = new GetSharedPreference(this);
            int loginId = getSharedPreference.getLoginId(getResources().getString(R.string.loginId));
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
                        break;

                    case R.id.rbFcl_transport:
                        rgContainerSize.setVisibility(View.VISIBLE);
                        inputLayout_cubicMeter.setVisibility(View.GONE);
                        strSelectedShipmentType = "FCL";
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
                        strSelectedContainerSize = getResources().getString(R.string.str20feet);
                        break;

                    case R.id.feet40:
                        strSelectedContainerSize = getResources().getString(R.string.str40feet);
                        break;

                    case R.id.feet40HQfeet:
                        strSelectedContainerSize = getResources().getString(R.string.str40HQfeet);
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


        // set adapter to pickup location
        txtPickup.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPickup.setOnItemClickListener(this);
        txtPickup.setThreshold(1);

        // set adapter to drop location
        txtDrop.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtDrop.setOnItemClickListener(this);
        txtDrop.setThreshold(1);

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

        Boolean boolPickUp = isEmptyString(txtPickup.getText().toString());
        Boolean boolDrop = isEmptyString(txtDrop.getText().toString());
        Boolean boolCBM = isEmptyString(txtCBM.getText().toString());
        Boolean boolGrossWt = isEmptyString(txtGrossWt.getText().toString());
        Boolean boolTypeOfPack = isEmptyString(txtTypeOfPackaging.getText().toString());
        Boolean boolNoOfPack = isEmptyString(txt_noOfPack.getText().toString());
        Boolean boolCommodity = isEmptyString(txtComodity.getText().toString());
        Boolean boolDimenLen = isEmptyString(txtDimenLen.getText().toString());
        Boolean boolDimenHeight = isEmptyString(txtDimenHeight.getText().toString());
        Boolean boolDimenWeight = isEmptyString(txtDimenWeight.getText().toString());

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
            inputLayout_dimen_weight.setErrorEnabled(false);
        } else {
            inputLayout_dimen_weight.setErrorEnabled(true);
            inputLayout_dimen_weight.setError("weight.");
        }

        if (boolPickUp && boolDrop && boolGrossWt && boolTypeOfPack && boolNoOfPack && boolDimenLen
                && boolDimenHeight && boolDimenWeight && boolCommodity) {

            if (inputLayout_cubicMeter.getVisibility() == View.VISIBLE && !boolCBM ) {
                return;
            }

        }
        /*if( availedServicesName != null ) {

            if( availedServicesName.contains(enumServices.CUSTOM_CLEARANCE.toString()) ) {
                Intent intentForCustomClearanceActivity = new Intent(this, CustomClearanceActivity.class);
                intentForCustomClearanceActivity.putStringArrayListExtra("ServicesId",  ids);
                intentForCustomClearanceActivity.putStringArrayListExtra("ServicesName", names);
                intentForCustomClearanceActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentForCustomClearanceActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentForCustomClearanceActivity.putExtra("shipmentType", strSelectedShipmentType);
                intentForCustomClearanceActivity.putExtra("pickupAddress", txtPickup.getText().toString().trim());
                intentForCustomClearanceActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForCustomClearanceActivity);
            } else if(availedServicesName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                Intent intentForFreightForardingActivity = new Intent(this, FreightForwardingActivity.class);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesId",  ids);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesName", names);
                intentForFreightForardingActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentForFreightForardingActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentForFreightForardingActivity.putExtra("shipmentType", strSelectedShipmentType);
                intentForFreightForardingActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForFreightForardingActivity);
            }
            else {
                Intent intentForQuoteActivity = new Intent(this, QuotationActivity.class);
                intentForQuoteActivity.putStringArrayListExtra("ServicesId",  ids);
                intentForQuoteActivity.putStringArrayListExtra("ServicesName", names);
                intentForQuoteActivity.putStringArrayListExtra("availedServicesId", availedServicesId);
                intentForQuoteActivity.putStringArrayListExtra("availedServicesName", availedServicesName);
                intentForQuoteActivity.putExtra("shipmentType", strSelectedShipmentType);
                intentForQuoteActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForQuoteActivity);
            }

        }
        else {

            if( names.contains(enumServices.CUSTOM_CLEARANCE.toString()) ) {
                Intent intentForCustomClearanceActivity = new Intent(this, CustomClearanceActivity.class);
                intentForCustomClearanceActivity.putStringArrayListExtra("ServicesId",  ids);
                intentForCustomClearanceActivity.putStringArrayListExtra("ServicesName", names);
                intentForCustomClearanceActivity.putExtra("shipmentType", strSelectedShipmentType);
                intentForCustomClearanceActivity.putExtra("pickupAddress", txtPickup.getText().toString().trim());
                intentForCustomClearanceActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForCustomClearanceActivity);
            } else if(names.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                Intent intentForFreightForardingActivity = new Intent(this, FreightForwardingActivity.class);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesId",  ids);
                intentForFreightForardingActivity.putStringArrayListExtra("ServicesName", names);
                intentForFreightForardingActivity.putExtra("shipmentType", strSelectedShipmentType);
                intentForFreightForardingActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForFreightForardingActivity);
            }
            else {
                Intent intentForQuoteActivity = new Intent(this, QuotationActivity.class);
                intentForQuoteActivity.putStringArrayListExtra("ServicesId",  ids);
                intentForQuoteActivity.putStringArrayListExtra("ServicesName", names);
                intentForQuoteActivity.putExtra("shipmentType", strSelectedShipmentType);
                intentForQuoteActivity.putExtra("bookId", txtBookId.getText().toString().trim());
                startActivity(intentForQuoteActivity);
            }

        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
