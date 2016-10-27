package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.TransportServiceModel;
import com.alchemistdigital.buxa.utilities.CustomTypefaceSpan;
import com.alchemistdigital.buxa.utilities.enumServices;

import java.util.ArrayList;
import java.util.List;

public class SelectServiceActivity extends AppCompatActivity {
    TextView tv_strSelectService;
    LinearLayout layoutCheckbox;
    List<TransportServiceModel> transportServiceData;
    List<String> selchkboxlistId = new ArrayList<String>();
    List<String> selchkboxlistName = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_service);

        layoutCheckbox = (LinearLayout)findViewById(R.id.layoutServiceCheckbox);

        // initialise typeface
        Typeface seogoLight = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUIL.TTF");
        Typeface seogoReguler = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");

        // initialise component
        tv_strSelectService = (TextView) findViewById(R.id.id_tv_strSelectService);

        /*cbTrans = (CheckBox) findViewById(R.id.rb_id_trans);
        cbTrans.setTypeface(seogoLight);
        cbCustomClr = (CheckBox) findViewById(R.id.rb_id_CustomClr);
        cbCustomClr.setTypeface(seogoLight);
        cbFreightForward = (CheckBox) findViewById(R.id.rb_id_freightForward);
        cbFreightForward.setTypeface(seogoLight);*/

        String firstWord = getResources().getString(R.string.strSelectService);
        String secondWord = getResources().getString(R.string.strServices);

        // Create a new spannable with the two strings
        Spannable spannable = new SpannableString(firstWord+" "+secondWord);

        // Set the custom typeface to span over a section of the spannable object
        spannable.setSpan( new CustomTypefaceSpan("", seogoLight), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("", seogoReguler), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the text of a textView with the spannable object
        tv_strSelectService.setText(spannable);

        DatabaseClass dbClass = new DatabaseClass(this);
        transportServiceData = dbClass.getTransportServiceData();

        for (int i = 0 ; i < transportServiceData.size() ; i++ ) {
            CheckBox cb = new CheckBox(this);
            cb.setId(transportServiceData.get(i).getServerId());
            cb.setText(transportServiceData.get(i).getName());
            cb.setTextColor(getResources().getColor(R.color.backgroundColor));
            cb.setTextSize(getResources().getDimension(R.dimen.serviceCheckBoxTextSize));
            cb.setPadding(50, 0, 0, 0);
            cb.setTypeface(seogoLight);
            cb.setScaleX(1);
            cb.setScaleY(1);
            cb.setOnClickListener(getOnClickDoSomething(cb));
            layoutCheckbox.addView(cb);
        }

    }

    private View.OnClickListener getOnClickDoSomething(final CheckBox cb) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( cb.isChecked() ) {
                    selchkboxlistId.add(""+cb.getId());
                    selchkboxlistName.add(cb.getText().toString().trim());
                }
                else {
                    selchkboxlistId.remove(""+cb.getId() );
                    selchkboxlistName.remove(cb.getText().toString().trim());
                }

            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void gotoSelectServiceParameter(View view) {

        if(  selchkboxlistId.size() > 0 ) {
            if( selchkboxlistName.contains(enumServices.TRANSPORTATION.toString()) ) {
                Intent intentForServiceParameterActivity = new Intent(this, TransportQuotationActivity.class);
                intentForServiceParameterActivity.putStringArrayListExtra("ServicesId", (ArrayList<String>) selchkboxlistId);
                intentForServiceParameterActivity.putStringArrayListExtra("ServicesName", (ArrayList<String>) selchkboxlistName);
                startActivity(intentForServiceParameterActivity);
            } else if( selchkboxlistName.contains(enumServices.CUSTOM_CLEARANCE.toString()) ) {
                Intent intentForServiceParameterActivity = new Intent(this, CustomClearanceActivity.class);
                intentForServiceParameterActivity.putStringArrayListExtra("ServicesId", (ArrayList<String>) selchkboxlistId);
                intentForServiceParameterActivity.putStringArrayListExtra("ServicesName", (ArrayList<String>) selchkboxlistName);
                startActivity(intentForServiceParameterActivity);
            } else if(selchkboxlistName.contains(enumServices.FREIGHT_FORWARDING.toString())) {
                Intent intentForServiceParameterActivity = new Intent(this, FreightForwardingActivity.class);
                intentForServiceParameterActivity.putStringArrayListExtra("ServicesId", (ArrayList<String>) selchkboxlistId);
                intentForServiceParameterActivity.putStringArrayListExtra("ServicesName", (ArrayList<String>) selchkboxlistName);
                startActivity(intentForServiceParameterActivity);
            }

        }
        else {
            Toast.makeText(this, "Please, select any one service. ",Toast.LENGTH_SHORT).show();
        }
    }
}
