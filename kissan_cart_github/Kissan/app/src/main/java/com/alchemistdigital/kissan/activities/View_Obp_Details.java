package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.OBP;

public class View_Obp_Details extends AppCompatActivity {

    TextView tv_name, tv_storeName, tv_emailId, tv_contact, tv_address, tv_pincode, tv_city, tv_state, tv_country;
    String name, storeName, emailId, contact, address, pincode, city, state, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__obp__details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.obp_details_toolbar);
        setSupportActionBar(toolbar);

        OBP obp_entity = getIntent().getParcelableExtra("OBP_Entity");
        name = obp_entity.obp_name;
        storeName = obp_entity.obp_store_name;
        emailId = obp_entity.obp_email_id;
        contact = obp_entity.obp_contact_number;
        address = obp_entity.obp_address;
        pincode = String.valueOf(obp_entity.obp_pincode);
        city = obp_entity.obp_city;
        state = obp_entity.obp_state;
        country = obp_entity.obp_country;

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
