package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSpinnerAdapter;
import com.cleanslatetech.floc.asynctask.FileUploadAsyncTask;
import com.cleanslatetech.floc.models.EventsModel;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.cleanslatetech.floc.utilities.CommonUtilities.customToast;
import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class CreateFlocActivitySecond extends BaseAppCompactActivity implements AdapterView.OnItemSelectedListener{

    private AppCompatSpinner spinnerConcierge, spinnerEventMgmt;
    private CustomSpinnerAdapter adapterYesNo;
    private AppCompatAutoCompleteTextView acCountry;
    private ArrayAdapter<String> country_adapter;
    String flocFirstData;
    private String selectedConcierge, selectedEventMgmt, selectedCountry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_floc_second);

        super.setToolBar(getResources().getString(R.string.create_floc));

        init();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void init() {

        flocFirstData = getIntent().getExtras().getString("flocData");

        ArrayList<String> stringArrayYesNo = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.yes_no)));
        adapterYesNo = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, stringArrayYesNo);
        adapterYesNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // populate concierge service spinner
        spinnerConcierge = (AppCompatSpinner) findViewById(R.id.id_spinner_concierge_services);
        spinnerConcierge.setAdapter(adapterYesNo);
        spinnerConcierge.setOnItemSelectedListener(this);

        // populate event managment services spinner
        spinnerEventMgmt = (AppCompatSpinner) findViewById(R.id.id_spinner_event_mgmt_services);
        spinnerEventMgmt.setAdapter(adapterYesNo);
        spinnerEventMgmt.setOnItemSelectedListener(this);

        // initialised country autocomplete textfield
        acCountry = (AppCompatAutoCompleteTextView) findViewById(R.id.id_country);
        final ArrayList<String> stringArrayCountry = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.country)));
        final ArrayList<String> stringArrayCountryCode = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.country_code)));
        country_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArrayCountry );
        acCountry.setAdapter(country_adapter);
        acCountry.setThreshold(1);

        acCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectedCountry = country_adapter.getItem(position).toString();
                String selected = (String) parent.getItemAtPosition(position);
                int pos = stringArrayCountry.indexOf(selected);
                selectedCountry = stringArrayCountryCode.get(pos).toString();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(getResources().getColor(R.color.white));
        }

        AppCompatSpinner spinner = (AppCompatSpinner) parent;
        switch (spinner.getId()) {
            case R.id.id_spinner_concierge_services:
                    selectedConcierge = parent.getItemAtPosition(position).toString();
                break;

            case R.id.id_spinner_event_mgmt_services:
                    selectedEventMgmt = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void btnCreateFloc(View view) {
        // Check if Internet present
        if (!isConnectingToInternet(CreateFlocActivitySecond.this)) {
            CommonUtilities.customToast(CreateFlocActivitySecond.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        }


        try {

            AppCompatEditText txtMemberCount = (AppCompatEditText) findViewById(R.id.id_member_count);
            AppCompatEditText txtWebsite = (AppCompatEditText) findViewById(R.id.id_website_url);
            AppCompatEditText txtCity = (AppCompatEditText) findViewById(R.id.id_city);
            AppCompatEditText txtArea = (AppCompatEditText) findViewById(R.id.id_area);
            AppCompatEditText txtAddress = (AppCompatEditText) findViewById(R.id.id_address);
            AppCompatEditText txtState = (AppCompatEditText) findViewById(R.id.id_state);

            Boolean flag = true;

            Boolean boolMember = Validations.isEmptyString(txtMemberCount.getText().toString().trim());
            Boolean boolWebsite = Validations.isEmptyString(txtWebsite.getText().toString().trim());
            Boolean boolCity = Validations.isEmptyString(txtCity.getText().toString().trim());
            Boolean boolArea = Validations.isEmptyString(txtArea.getText().toString().trim());
            Boolean boolAddress = Validations.isEmptyString(txtAddress.getText().toString().trim());
            Boolean boolState = Validations.isEmptyString(txtState.getText().toString().trim());

            if(!boolMember) {
                customToast(CreateFlocActivitySecond.this, "Member count is empty");
                flag = false;
            } else if(!boolWebsite) {
                customToast(CreateFlocActivitySecond.this, "Website/URL field is empty");
                flag = false;
            } else if(!boolCity) {
                customToast(CreateFlocActivitySecond.this, "City field is empty");
                flag = false;
            } else if(!boolArea) {
                customToast(CreateFlocActivitySecond.this, "Area field is empty");
                flag = false;
            } else if(!boolAddress) {
                customToast(CreateFlocActivitySecond.this, "Address field is empty");
                flag = false;
            } else if(!boolState) {
                customToast(CreateFlocActivitySecond.this, "State field is empty");
                flag = false;
            } else if( selectedCountry == null ) {
                customToast(CreateFlocActivitySecond.this, "Country field is empty");
                flag = false;
            }

            if(!flag) {
                return;
            }

            JSONObject jsonObjFlocData = new JSONObject(flocFirstData);

            /*System.out.println("pic: "+ jsonObjFlocData.getString("filePath"));
            System.out.println("name: "+jsonObjFlocData.getString("name"));
            System.out.println("catId: "+jsonObjFlocData.getInt("catId"));
            System.out.println("desc: "+jsonObjFlocData.getString("desc"));
            System.out.println("startDate: "+jsonObjFlocData.getString("startDate"));
            System.out.println("startHr: "+jsonObjFlocData.getString("startHr"));
            System.out.println("startMin: "+jsonObjFlocData.getString("startMin"));
            System.out.println("endDate: "+jsonObjFlocData.getString("endDate"));
            System.out.println("endHr: "+jsonObjFlocData.getString("endHr"));
            System.out.println("endMin: "+jsonObjFlocData.getString("endMin"));
            System.out.println("priceType: "+jsonObjFlocData.getInt("priceType"));
            System.out.println("price: "+jsonObjFlocData.getString("price"));

            System.out.println(Integer.parseInt(txtMemberCount.getText().toString()));
            System.out.println("Website: "+txtWebsite.getText().toString());
            System.out.println("txtCity: "+txtCity.getText().toString());
            System.out.println("txtArea: "+txtArea.getText().toString());
            System.out.println("txtAddress: "+txtAddress.getText().toString());
            System.out.println("txtState: "+txtState.getText().toString());
            System.out.println("selectedConcierge: "+selectedConcierge);
            System.out.println("selectedEventMgmt: "+selectedEventMgmt);
            System.out.println("selectedCountry: "+selectedCountry);*/

            EventsModel eventsModel =
                    new EventsModel(
                            0,
                            new GetSharedPreference(CreateFlocActivitySecond.this).getInt(getResources().getString(R.string.shrdLoginId)),
                            jsonObjFlocData.getString("name"),
                            jsonObjFlocData.getInt("catId"),
                            jsonObjFlocData.getString("desc"),
                            jsonObjFlocData.getString("filePath"),
                            jsonObjFlocData.getString("startDate"),
                            jsonObjFlocData.getString("startHr"),
                            jsonObjFlocData.getString("startMin"),
                            jsonObjFlocData.getString("endDate"),
                            jsonObjFlocData.getString("endHr"),
                            jsonObjFlocData.getString("endMin"),
                            jsonObjFlocData.getInt("priceType"),
                            jsonObjFlocData.getString("price"),
                            Integer.parseInt(txtMemberCount.getText().toString()),
                            txtCity.getText().toString(),
                            txtArea.getText().toString(),
                            txtAddress.getText().toString(),
                            txtState.getText().toString(),
                            selectedCountry,
                            txtWebsite.getText().toString(),
                            "abc",
                            "Created",
                            selectedEventMgmt,
                            selectedConcierge,
                            0,
                            false );

            View view1 = getCurrentFocus();
            if (view1 != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            }

            new FileUploadAsyncTask(CreateFlocActivitySecond.this, eventsModel, jsonObjFlocData.getString("filePath"), CommonVariables.POST_IMAGE_SERVER_URL).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
