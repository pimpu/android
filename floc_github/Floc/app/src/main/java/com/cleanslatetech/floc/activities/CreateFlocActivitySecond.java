package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateFlocActivitySecond extends BaseAppCompactActivity implements AdapterView.OnItemSelectedListener{

    private AppCompatSpinner spinnerConcierge, spinnerEventMgmt;
    private CustomSpinnerAdapter adapterYesNo;
    private AppCompatAutoCompleteTextView acCountry;
    private ArrayAdapter<String> country_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_floc_second);

        super.setToolBar(getResources().getString(R.string.create_floc));

        init();
    }

    private void init() {
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
        ArrayList<String> stringArrayCountry = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.country)));
        country_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArrayCountry );
        acCountry.setAdapter(country_adapter);
        acCountry.setThreshold(1);

        acCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_adapter.getItem(position).toString();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
