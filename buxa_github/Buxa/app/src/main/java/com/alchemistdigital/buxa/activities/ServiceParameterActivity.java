package com.alchemistdigital.buxa.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.CustomClearanceLocation;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;

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
import java.util.Arrays;
import java.util.List;

public class ServiceParameterActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView txtComodity, txtTypeOfPackaging, txtPickup, txtDrop, txtCustomClearanceLocation;

    public LinearLayout commodityLayout, shipmentTermLayout, packageTypeLayout, noOfPackageLayout,
                dimensionLayout, pickupLayout, dropLayout, LRCopyLayout, IECLayout, ADCodeLayout,
                customeClearanceLocationLayout, isFirstTimeCC;

    DatabaseClass dbClass ;
    ArrayList<String> ids, names;
    Boolean isAvail = false;

    //    -------------- place api -------------------
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCyjSSwYtYv4r84kESFyVz2m-edkKc0N54";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_parameter);

        toolbarSetup();

        init();

        ids = getIntent().getStringArrayListExtra("ServicesId");
        names = getIntent().getStringArrayListExtra("ServicesName");
        dbClass = new DatabaseClass(this);

        for (int j = 0 ; j < ids.size() ; j++ ) {
            switch (names.get(j)) {
                case "Transportation" :
                    transportation();
                    break;

                case "Freight Forwarding" :
                    break;

                case "Custom Clearance" :
                    customClearance();
                    break;
            }
        }

    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_selectServiceParameter);
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
        // getSupportActionBar().setTitle(getIntent().getStringExtra("callingActivity"));
    }

    private void init() {
        commodityLayout = (LinearLayout) findViewById(R.id.layout_commodity);
        shipmentTermLayout = (LinearLayout) findViewById(R.id.layout_shipmentTerm);
        packageTypeLayout = (LinearLayout) findViewById(R.id.layout_PackageType);
        noOfPackageLayout = (LinearLayout) findViewById(R.id.layout_NoOfPackage);
        dimensionLayout = (LinearLayout) findViewById(R.id.layout_dimension);
        pickupLayout = (LinearLayout) findViewById(R.id.layout_pickup);
        dropLayout = (LinearLayout) findViewById(R.id.layout_drop);
        LRCopyLayout = (LinearLayout) findViewById(R.id.layout_LRCopy);
        IECLayout = (LinearLayout) findViewById(R.id.layout_IEC);
        ADCodeLayout = (LinearLayout) findViewById(R.id.layout_ADCode);
        customeClearanceLocationLayout = (LinearLayout) findViewById(R.id.layout_CustomeClearanceLocation);
        isFirstTimeCC = (LinearLayout) findViewById(R.id.layout_isFirstTimeInCustomeClearance);

        txtComodity = (AutoCompleteTextView) findViewById(R.id.id_commodity);
        txtTypeOfPackaging = (AutoCompleteTextView) findViewById(R.id.id_type_of_package);
        txtPickup = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_pickup);
        txtDrop = (AutoCompleteTextView) findViewById(R.id.id_autoComplete_drop);
        txtCustomClearanceLocation = (AutoCompleteTextView) findViewById(R.id.id_custome_clearance);
    }

    private void transportation() {
        commodityLayout.setVisibility(View.VISIBLE);
        dimensionLayout.setVisibility(View.VISIBLE);
        shipmentTermLayout.setVisibility(View.VISIBLE);
        noOfPackageLayout.setVisibility(View.VISIBLE);
        packageTypeLayout.setVisibility(View.VISIBLE);
        pickupLayout.setVisibility(View.VISIBLE);
        dropLayout.setVisibility(View.VISIBLE);
        LRCopyLayout.setVisibility(View.VISIBLE);

        // initialised comodity autocomplete textfield from database
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        ArrayAdapter<CommodityModel> adapter = new ArrayAdapter<CommodityModel>(this, layoutItemId, dbClass.getCommodityData() );
        txtComodity.setAdapter(adapter);
        txtComodity.setThreshold(1);

        // set adapter to pickup location
        txtPickup.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtPickup.setOnItemClickListener(this);

        // set adapter to drop location
        txtDrop.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtDrop.setOnItemClickListener(this);

    }

    private void customClearance() {
        IECLayout.setVisibility(View.VISIBLE);
        ADCodeLayout.setVisibility(View.VISIBLE);
        customeClearanceLocationLayout.setVisibility(View.VISIBLE);
        isFirstTimeCC.setVisibility(View.VISIBLE);
        if(!names.contains("Transportation")) {
            shipmentTermLayout.setVisibility(View.VISIBLE);
        }

        // initialised custom clearance location autocomplete textfield from database
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        ArrayAdapter<CustomClearanceLocation> adapter = new ArrayAdapter<CustomClearanceLocation>(this, layoutItemId, dbClass.getCustomClearanceLocationData() );
        txtCustomClearanceLocation.setAdapter(adapter);
        txtCustomClearanceLocation.setThreshold(1);
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

    public void gotoFileUpload(View view) {
        Intent callCustomImageGallery = new Intent(this, CustomImageGalleryActivity.class);
        callCustomImageGallery.putExtra("callingActivity","Select LR Copy");
        startActivity(callCustomImageGallery);
    }

    public void storeTransportEnquiry(View view) {
        // before user agree with avail option
        if( !isAvail ) {
            // show avil dialog box when user select only transport services
            if(names.contains("Transportation") && names.size() == 1 ) {

                new AlertDialog.Builder(ServiceParameterActivity.this)
                        .setMessage(getResources().getString(R.string.strInTransportMode))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                isAvail = true;
                                customClearance();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Hello,",Toast.LENGTH_SHORT).show();
            }
        }
        // after user agree with avail option
        else {
            Toast.makeText(getApplicationContext(),"after avail option selected.,",Toast.LENGTH_SHORT).show();
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
