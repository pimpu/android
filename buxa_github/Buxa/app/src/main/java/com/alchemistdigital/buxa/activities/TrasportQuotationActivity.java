package com.alchemistdigital.buxa.activities;

import android.content.Context;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.EdittextSegoeLightFont;

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

public class TrasportQuotationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView txtComodity, txtTypeOfPackaging, txtPickup, txtDrop;
    TextInputLayout CBM_InputLayout;
    public LinearLayout commodityLayout, shipmentTypeLayout, packageTypeLayout, noOfPackageLayout,
                dimensionLayout, pickupLayout, dropLayout;
    RadioGroup rgContainerSize, rgTypeOfShipment;
    EdittextSegoeLightFont txtCBM;
    DatabaseClass dbClass ;
    ArrayList<String> ids, names;


    //    -------------- place api -------------------
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCyjSSwYtYv4r84kESFyVz2m-edkKc0N54";

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
        txtCBM = (EdittextSegoeLightFont) findViewById(R.id.id_cubic_meter_measurement);

        rgTypeOfShipment = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_transport);
        rgTypeOfShipment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_transport:
                        rgContainerSize.setVisibility(View.GONE);
                        CBM_InputLayout.setVisibility(View.VISIBLE);
                        break;

                    case R.id.rbFcl_transport:
                        rgContainerSize.setVisibility(View.VISIBLE);
                        CBM_InputLayout.setVisibility(View.GONE);
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
        GetSharedPreference getPreference = new GetSharedPreference(this);
        System.out.println("selected files: "+getPreference.getSelectedImage(getResources().getString(R.string.strSelectedImage)));
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
            Intent intentForServiceParameterActivity = new Intent(this, CustomClearanceActivity.class);
            intentForServiceParameterActivity.putStringArrayListExtra("ServicesId",  ids);
            intentForServiceParameterActivity.putStringArrayListExtra("ServicesName", names);
            startActivity(intentForServiceParameterActivity);
        } else if(names.contains("Freight Forwarding")) {
            Toast.makeText(TrasportQuotationActivity.this, "Freight Forwarding", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(TrasportQuotationActivity.this, "Quotation Screen", Toast.LENGTH_SHORT).show();
        }
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:IN");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

//            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error processing Places API URL: " + e);
            return resultList;
        } catch (IOException e) {
            System.out.println("Error connecting to Places API: "+e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            System.out.println("Cannot process JSON results: "+e);
        }

        return resultList;
    }
}
