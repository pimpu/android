package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;

public class View_Obp_Details extends AppCompatActivity {

    TextView tv_name, tv_storeName, tv_emailId, tv_contact, tv_address, tv_pincode, tv_city, tv_state, tv_country;
    String name, storeName, emailId, contact, address, pincode, city, state, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__obp__details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.obp_details_toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        storeName = extras.getString("store_name");
        emailId = extras.getString("email_id");
        contact = extras.getString("contact");
        address = extras.getString("address");
        pincode = String.valueOf(extras.getInt("pincode"));
        city = extras.getString("city");
        state = extras.getString("state");
        country = extras.getString("country");

        init();

        setText();
    }

    private void init() {

        tv_name = (TextView) findViewById(R.id.tv_id_obp_name);
        tv_storeName = (TextView) findViewById(R.id.tv_id_obp_storeName);
        tv_emailId = (TextView) findViewById(R.id.tv_id_obp_emailId);
        tv_contact = (TextView) findViewById(R.id.tv_id_obp_contact);
        tv_address = (TextView) findViewById(R.id.tv_id_obp_address);
        tv_pincode = (TextView) findViewById(R.id.tv_id_obp_pincode);
        tv_city = (TextView) findViewById(R.id.tv_id_obp_city);
        tv_state = (TextView) findViewById(R.id.tv_id_obp_state);
        tv_country = (TextView) findViewById(R.id.tv_id_obp_country);
    }

    private void setText() {
        tv_name.setText(name);
        tv_storeName.setText("Store: "+storeName);
        tv_emailId.setText("Email: "+emailId);
        tv_contact.setText("Contact: "+contact);
        tv_address.setText("Address: "+address);
        tv_pincode.setText("Pincode: "+pincode);
        tv_city.setText("City: "+city);
        tv_state.setText("State: "+state);
        tv_country.setText("Country: "+country);
    }
}
