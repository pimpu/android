package com.alchemistdigital.buxa.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.utilities.CommonVariables;

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

public class CustomClearanceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AutoCompleteTextView txtCCAddress;
    RadioGroup rgShipmentType, rgFCLStuffing;
    TextView hintAddress;
    ArrayList<String> arrayServicesId, arrayServicesName;
    String shipmentType, pickupAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clearance);

        arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        shipmentType = getIntent().getExtras().getString("shipmentType");
        pickupAddress = getIntent().getExtras().getString("pickupAddress");

        toolbarSetup();

        init();


    }

    private void init() {

        rgShipmentType = (RadioGroup) findViewById(R.id.radiogroupTypeOfShipment_CC);
        hintAddress = (TextView) findViewById(R.id.id_hint_CC_Address_label);
        hintAddress.setText("Vendor CFS Address");
        txtCCAddress = (AutoCompleteTextView) findViewById(R.id.id_CC_adresses);

        // set address to location
        txtCCAddress.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        txtCCAddress.setOnItemClickListener(this);

        rgShipmentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLcl_cc:
                        rgFCLStuffing.setVisibility(View.GONE);
                        hintAddress.setText("Vendor CFS Address");
                        break;

                    case R.id.rbFcl_cc:
                        rgFCLStuffing.setVisibility(View.VISIBLE);
                        hintAddress.setText("Stuffing Address");

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
                        if( arrayServicesName.contains("Transportation") && arrayServicesId.size() > 0 ) {
                            txtCCAddress.setText(pickupAddress);
                            txtCCAddress.setClickable(false);
                            txtCCAddress.setCursorVisible(false);
                            txtCCAddress.setFocusable(false);
                            txtCCAddress.setFocusableInTouchMode(false);
                        }

                        break;

                    case R.id.rbDockStuff:
                        hintAddress.setText(getResources().getString(R.string.strDockStuff)+" address");
                        if( arrayServicesName.contains("Transportation") && arrayServicesId.size() > 0 ) {
                            txtCCAddress.setText("");
                            // set address to location
                            txtCCAddress.setAdapter(new GooglePlacesAutocompleteAdapter(CustomClearanceActivity.this, R.layout.list_item));
                            txtCCAddress.setOnItemClickListener(CustomClearanceActivity.this);

                            txtCCAddress.setClickable(true);
                            txtCCAddress.setCursorVisible(true);
                            txtCCAddress.setFocusable(true);
                            txtCCAddress.setFocusableInTouchMode(true);
                        }
                        break;
                }
            }
        });

        // auto selection of field is did when user comes from Transportation page.
        if( arrayServicesName.contains("Transportation") && arrayServicesId.size() > 0 ) {
            if(shipmentType.equals("LCL")) {
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
            StringBuilder sb = new StringBuilder(CommonVariables.PLACES_API_BASE + CommonVariables.TYPE_AUTOCOMPLETE + CommonVariables.OUT_JSON);
            sb.append("?key=" + CommonVariables.API_KEY);
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
