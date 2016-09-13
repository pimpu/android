package com.alchemistdigital.buxa.activities;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.utilities.SegoeFontEdittext;
import com.alchemistdigital.buxa.utilities.GooglePlacesAutocompleteAdapter;

import java.util.ArrayList;

public class TrasportQuotationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView txtComodity, txtTypeOfPackaging, txtPickup, txtDrop;
    TextInputLayout CBM_InputLayout;
    public LinearLayout commodityLayout, shipmentTypeLayout, packageTypeLayout, noOfPackageLayout,
                dimensionLayout, pickupLayout, dropLayout;
    RadioGroup rgContainerSize, rgTypeOfShipment;
    SegoeFontEdittext txtCBM;
    DatabaseClass dbClass ;
    ArrayList<String> ids, names;
    String strSelectedShipmentType = "LCL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasport_quotation);

        toolbarSetup();

        init();

        ids = getIntent().getStringArrayListExtra("ServicesId");
        names = getIntent().getStringArrayListExtra("ServicesName");
        dbClass = new DatabaseClass(this);

        transportation();

    }

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
        getSupportActionBar().setTitle("Transportation Quotation");
    }

    private void init() {
        commodityLayout = (LinearLayout) findViewById(R.id.layout_commodity);
        shipmentTypeLayout = (LinearLayout) findViewById(R.id.layout_shipmentType);
        packageTypeLayout = (LinearLayout) findViewById(R.id.layout_PackageType);
        noOfPackageLayout = (LinearLayout) findViewById(R.id.layout_NoOfPackage);
        dimensionLayout = (LinearLayout) findViewById(R.id.layout_dimension);
        pickupLayout = (LinearLayout) findViewById(R.id.layout_pickup);
        dropLayout = (LinearLayout) findViewById(R.id.layout_drop);

        CBM_InputLayout = (TextInputLayout) findViewById(R.id.input_layout_cubic_meter_measurement);

        txtComodity = (AutoCompleteTextView) findViewById(R.id.id_commodity);
        txtTypeOfPackaging = (AutoCompleteTextView) findViewById(R.id.id_type_of_package);
        txtPickup = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_pickup);
        txtDrop = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_drop);
        rgContainerSize = (RadioGroup) findViewById(R.id.radiogroup2040);
        txtCBM = (SegoeFontEdittext) findViewById(R.id.id_cubic_meter_measurement);

        rgTypeOfShipment = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_transport);
        rgTypeOfShipment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_transport:
                        rgContainerSize.setVisibility(View.GONE);
                        CBM_InputLayout.setVisibility(View.VISIBLE);
                        strSelectedShipmentType = "LCL";
                        break;

                    case R.id.rbFcl_transport:
                        rgContainerSize.setVisibility(View.VISIBLE);
                        CBM_InputLayout.setVisibility(View.GONE);
                        strSelectedShipmentType = "FCL";
                        break;
                }
            }
        });

    }

    private void transportation() {
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;

        // initialised comodity autocomplete textfield from database
        ArrayAdapter<CommodityModel> commodity_adapter = new ArrayAdapter<CommodityModel>(this, layoutItemId, dbClass.getCommodityData() );
        txtComodity.setAdapter(commodity_adapter);
        txtComodity.setThreshold(1);

        // initialised packaging type autocomplete textfield from database
        ArrayAdapter<PackageTypeModel> packagingType_adapter = new ArrayAdapter<PackageTypeModel>(this, layoutItemId, dbClass.getPackagingTypeData() );
        txtTypeOfPackaging.setAdapter(packagingType_adapter);
        txtTypeOfPackaging.setThreshold(1);


        // set adapter to pickup location
        txtPickup.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPickup.setOnItemClickListener(this);

        // set adapter to drop location
        txtDrop.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtDrop.setOnItemClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if( names.contains("Custom Clearance") ) {
            Intent intentForCustomClearanceActivity = new Intent(this, CustomClearanceActivity.class);
            intentForCustomClearanceActivity.putStringArrayListExtra("ServicesId",  ids);
            intentForCustomClearanceActivity.putStringArrayListExtra("ServicesName", names);
            intentForCustomClearanceActivity.putExtra("shipmentType", strSelectedShipmentType);
            intentForCustomClearanceActivity.putExtra("pickupAddress", txtPickup.getText().toString().trim());
            startActivity(intentForCustomClearanceActivity);
        } else if(names.contains("Freight Forwarding")) {
            Intent intentForFreightForardingActivity = new Intent(this, FreightForwardingActivity.class);
            intentForFreightForardingActivity.putStringArrayListExtra("ServicesId",  ids);
            intentForFreightForardingActivity.putStringArrayListExtra("ServicesName", names);
            intentForFreightForardingActivity.putExtra("shipmentType", strSelectedShipmentType);
            startActivity(intentForFreightForardingActivity);
        }
        else {
            Intent intentForFreightForardingActivity = new Intent(this, QuotationActivity.class);
            intentForFreightForardingActivity.putStringArrayListExtra("ServicesId",  ids);
            intentForFreightForardingActivity.putStringArrayListExtra("ServicesName", names);
            startActivity(intentForFreightForardingActivity);
        }
    }
}
