package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;

public class MainActivity extends AppCompatActivity {

    TextView tv_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_welcome = (TextView) findViewById(R.id.id_welcomeText);
        Typeface face1= Typeface.createFromAsset(getAssets(), "fonts/HELVETICA_CONDENSED_LIGHT_7.OTF");
        tv_welcome.setTypeface(face1);
    }

    public void goToSelectService(View view) {
        startActivity(new Intent(this, WelcomeActivity.class ));
    }

    public void btnNoClick(View view) {
        Toast.makeText(this,"The work is in process. ",Toast.LENGTH_SHORT).show();
    }
}
