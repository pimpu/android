package com.alchemistdigital.buxa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alchemistdigital.buxa.R;

public class ServiceParameterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_parameter);
        Boolean strTrans = getIntent().getExtras().getBoolean("Trans");
        Boolean strCutomClr = getIntent().getExtras().getBoolean("Custom_clr");
        Boolean strFreight = getIntent().getExtras().getBoolean("Freight");

        System.out.println("Trans: " + strTrans);
        System.out.println("Custom clr: " + strCutomClr);
        System.out.println("Freight: " + strFreight);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
