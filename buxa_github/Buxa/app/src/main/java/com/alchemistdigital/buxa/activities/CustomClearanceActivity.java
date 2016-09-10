package com.alchemistdigital.buxa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alchemistdigital.buxa.R;

import java.util.ArrayList;

public class CustomClearanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clearance);

        toolbarSetup();

        ArrayList<String> arrayServicesId = getIntent().getExtras().getStringArrayList("ServicesId");
        ArrayList<String> arrayServicesName = getIntent().getExtras().getStringArrayList("ServicesName");
        System.out.println("arrayServicesId: "+arrayServicesId);
        System.out.println("arrayServicesName: "+arrayServicesName);
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
}
