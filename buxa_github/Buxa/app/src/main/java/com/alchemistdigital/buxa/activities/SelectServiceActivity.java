package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.asynctask.GetAllCommodity;
import com.alchemistdigital.buxa.asynctask.GetAllCustomLoaction;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.CustomTypefaceSpan;

public class SelectServiceActivity extends AppCompatActivity {
    private CheckBox cbTrans, cbCustomClr, cbFreightForward;
    TextView tv_strSelectService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        // get all pre defined commodity from server.
        GetAllCommodity.getCommodities(this, CommonVariables.QUERY_COMMODITY_SERVER_URL);

        // initialise typeface
        Typeface seogoLight = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUIL.TTF");
        Typeface seogoReguler = Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.TTF");

        // initialise component
        tv_strSelectService = (TextView) findViewById(R.id.id_tv_strSelectService);

        cbTrans = (CheckBox) findViewById(R.id.rb_id_trans);
        cbTrans.setTypeface(seogoLight);
        cbCustomClr = (CheckBox) findViewById(R.id.rb_id_CustomClr);
        cbCustomClr.setTypeface(seogoLight);
        cbFreightForward = (CheckBox) findViewById(R.id.rb_id_freightForward);
        cbFreightForward.setTypeface(seogoLight);

        String firstWord = getResources().getString(R.string.strSelectService);
        String secondWord = getResources().getString(R.string.strServices);

        // Create a new spannable with the two strings
        Spannable spannable = new SpannableString(firstWord+" "+secondWord);

        // Set the custom typeface to span over a section of the spannable object
        spannable.setSpan( new CustomTypefaceSpan("", seogoLight), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("", seogoReguler), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the text of a textView with the spannable object
        tv_strSelectService.setText(spannable);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void gotoSelectServiceParameter(View view) {

        if(  cbTrans.isChecked() || cbCustomClr.isChecked()  || cbFreightForward.isChecked() ) {
            Intent intentForServiceParameterActivity = new Intent(this, ServiceParameterActivity.class);
            intentForServiceParameterActivity.putExtra("Trans", cbTrans.isChecked());
            intentForServiceParameterActivity.putExtra("Custom_clr", cbCustomClr.isChecked());
            intentForServiceParameterActivity.putExtra("Freight", cbFreightForward.isChecked());
            startActivity(intentForServiceParameterActivity);
        }
        else {
            Toast.makeText(this, "Please, select any one service. ",Toast.LENGTH_SHORT).show();
        }
    }
}
