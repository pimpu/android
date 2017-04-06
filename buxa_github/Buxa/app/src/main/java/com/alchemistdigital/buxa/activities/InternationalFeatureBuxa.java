package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alchemistdigital.buxa.R;

import java.util.ArrayList;

public class InternationalFeatureBuxa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international_feature_buxa);

        AppCompatCheckBox chkbox = (AppCompatCheckBox) findViewById(R.id.chk_proceed_to_next);
        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        public void run() {
                            ArrayList<String> ids = getIntent().getStringArrayListExtra("ServicesId");
                            ArrayList<String> names = getIntent().getStringArrayListExtra("ServicesName");

                            Intent intent = new Intent(InternationalFeatureBuxa.this, TransportQuotationActivity.class);
                            intent.putStringArrayListExtra("ServicesId", ids);
                            intent.putStringArrayListExtra("ServicesName", names);
                            startActivity(intent);
                            finish();

                        }
                    };

                    handler.postDelayed(runnable, 600);
                }
            }
        });
    }
}
