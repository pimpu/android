package com.alchemistdigital.kissan.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;

public class Enquiry_Detail extends AppCompatActivity {
    TextView tv_ref, tv_creationTime, tv_message, tv_society, tv_email, tv_contact, tv_address;
    ImageView imgViewSlip;
    String reference,creationTime,message,society,society_email,society_contact,society_address,
            document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry__detail);

        Bundle extras = getIntent().getExtras();
        reference = extras.getString("reference");
        creationTime = extras.getString("creationTime");
        message = extras.getString("message");
        society = extras.getString("society");
        society_email = extras.getString("society_email");
        society_contact = extras.getString("society_contact");
        society_address = extras.getString("society_address");
        document = extras.getString("document");

        init();

        setText();

    }

    private void init() {
        tv_ref = (TextView) findViewById(R.id.tv_id_enquiry_details_reference);
        tv_creationTime = (TextView) findViewById(R.id.tv_id_date);
        tv_message = (TextView) findViewById(R.id.tv_id_eMessage);
        tv_society = (TextView) findViewById(R.id.tv_id_society_name);
        tv_email = (TextView) findViewById(R.id.tv_id_society_email);
        tv_contact = (TextView) findViewById(R.id.tv_id_society_contact);
        tv_address = (TextView) findViewById(R.id.tv_id_society_address);
        imgViewSlip = (ImageView) findViewById(R.id.imageview_id_slip);
    }

    private void setText() {
        tv_ref.setText(reference);
        tv_creationTime.setText(creationTime);
        tv_message.setText(message);
        tv_society.setText(society);
        tv_email.setText(society_email);
        tv_contact.setText(society_contact);
        tv_address.setText(society_address);
        imgViewSlip.setImageURI(Uri.parse(""+document) );
    }
}
