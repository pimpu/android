package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Vendor;

public class View_Vendor_Details extends AppCompatActivity {

    TextView tv_name, tv_emailId, tv_contact, tv_address;
    String name, emailId, contact, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__vendor__details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.vendor_details_toolbar);
        setSupportActionBar(toolbar);

        Vendor vendor_entity = getIntent().getParcelableExtra("Vendor_Entity");
        name = vendor_entity.vendor_name;
        emailId = vendor_entity.vendor_email;
        contact = vendor_entity.vendor_contact;
        address = vendor_entity.vendor_Address;

        init();

        setText();
    }

    private void init() {

        tv_name = (TextView) findViewById(R.id.tv_id_vendor_name);
        tv_emailId = (TextView) findViewById(R.id.tv_id_vendor_emailId);
        tv_contact = (TextView) findViewById(R.id.tv_id_vendor_contact);
        tv_address = (TextView) findViewById(R.id.tv_id_vendor_address);
    }

    private void setText() {
        tv_name.setText(name);
        tv_emailId.setText("Email: "+emailId);
        tv_contact.setText("Contact: "+contact);
        tv_address.setText("Address: "+address);
    }
}
