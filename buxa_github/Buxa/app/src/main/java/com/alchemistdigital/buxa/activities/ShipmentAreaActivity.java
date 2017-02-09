package com.alchemistdigital.buxa.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.TransportServiceModel;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.CustomTypefaceSpan;
import com.alchemistdigital.buxa.utilities.ShipAreaVariableSingleton;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;
import java.util.List;

public class ShipmentAreaActivity extends AppCompatActivity {
    TextView tv_strSelectService;
    RadioButton rbInternational, rbDomestic;
    RadioGroup rgShipmentArea;
    List<String> selchkboxlistId = new ArrayList<String>();
    List<String> selchkboxlistName = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_area);

        // initialise typeface
        Typeface seogoLight = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUIL.TTF");
        Typeface seogoReguler = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");

        // initialise component
        tv_strSelectService = (TextView) findViewById(R.id.id_tv_strSelectService_shipArea);
        rbInternational = (RadioButton) findViewById(R.id.rbInternational);
        rbDomestic = (RadioButton) findViewById(R.id.rbDomestic);
        rgShipmentArea = (RadioGroup) findViewById(R.id.radioGroupShipmentArea);

        String firstWord = getResources().getString(R.string.strSelectService);
        String secondWord = getResources().getString(R.string.strServices);

        // Create a new spannable with the two strings
        Spannable spannable = new SpannableString(firstWord+" "+secondWord);

        // Set the custom typeface to span over a section of the spannable object
        spannable.setSpan( new CustomTypefaceSpan("", seogoLight), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("", seogoReguler), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the text of a textView with the spannable object
        tv_strSelectService.setText(spannable);
        rbInternational.setTypeface(seogoReguler);
        rbDomestic.setTypeface(seogoReguler);

        if(!hasPermissions(this, CommonVariables.PERMISSIONS)){
            ActivityCompat.requestPermissions(this, CommonVariables.PERMISSIONS, CommonVariables.REQUEST_PERMISSION);
        }

    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonVariables.REQUEST_PERMISSION:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission has been denied by user");
                    Toast.makeText(getApplicationContext(), "Permission require for registering with Buxa.",Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Permission has been granted by user");
                }
                break;
        }
    }

    public void gotoSelectShipmentArea(View view) {
        int selectedId=rgShipmentArea.getCheckedRadioButtonId();
        RadioButton radioShipAreaButton = (RadioButton) findViewById(selectedId);
        if(radioShipAreaButton != null) {
            String selectedText = radioShipAreaButton.getText().toString().trim();

            // set shipment area value(International.Domestic) in global variable of singleton class
            ShipAreaVariableSingleton.getInstance().shipAreaName = "";

            selchkboxlistId.clear();
            selchkboxlistName.clear();

            DatabaseClass dbClass = new DatabaseClass(ShipmentAreaActivity.this);
            List<TransportServiceModel> transportServiceData = dbClass.getTransportServiceData();

            if(selectedText.equals(getResources().getString(R.string.international))) {

                for (int i = 0 ; i < transportServiceData.size() ; i++ ) {
                    selchkboxlistId.add(""+transportServiceData.get(i).getServerId());
                    selchkboxlistName.add(transportServiceData.get(i).getName());
                }
            }
            else {
                for (int i = 0 ; i < transportServiceData.size() ; i++ ) {
                    if( transportServiceData.get(i).getName().equals(enumServices.TRANSPORTATION.toString()) ) {
                        selchkboxlistId.add(""+transportServiceData.get(i).getServerId());
                        selchkboxlistName.add(transportServiceData.get(i).getName());
                    }
                    break;
                }
            }

            Intent intentForServiceParameterActivity = new Intent(ShipmentAreaActivity.this, TransportQuotationActivity.class);
            intentForServiceParameterActivity.putStringArrayListExtra("ServicesId", (ArrayList<String>) selchkboxlistId);
            intentForServiceParameterActivity.putStringArrayListExtra("ServicesName", (ArrayList<String>) selchkboxlistName);
            startActivity(intentForServiceParameterActivity);

            // set shipment area value(International.Domestic) in global variable of singleton class
            ShipAreaVariableSingleton.getInstance().shipAreaName = selectedText;
        }
        else {
            Toast.makeText(this, "Please, select any one service. ",Toast.LENGTH_SHORT).show();
        }
    }
}
