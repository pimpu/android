package com.cleanslatetech.floc.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.HandleExpandLayoutMethods;

public class MyProfileActivity extends BaseAppCompactActivity {
    AppCompatTextView tv_personal, tv_finance;
    View personalLayout, financeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        super.setToolBar(getResources().getString(R.string.my_profile));

        init();
    }

    private void init() {
        tv_personal = (AppCompatTextView) findViewById(R.id.openPersonaPanel);
        tv_finance = (AppCompatTextView) findViewById(R.id.openFinancePanel);

        personalLayout = findViewById(R.id.personal_layout);
        financeLayout = findViewById(R.id.finance_layout);

        personalLayout.setVisibility(View.GONE);
        financeLayout.setVisibility(View.GONE);
    }

    public void toggle_personal_contents(View view) {
        if(personalLayout.isShown()){
            HandleExpandLayoutMethods.slide_up(this, personalLayout);
            personalLayout.setVisibility(View.GONE);

            tv_personal.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.arrow_down_float,0);
        }
        else{
            personalLayout.setVisibility(View.VISIBLE);
            HandleExpandLayoutMethods.slide_down(this, personalLayout);
            tv_personal.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.arrow_up_float,0);
        }
    }

    public void toggle_finance_contents(View view) {
        if(financeLayout.isShown()){
            HandleExpandLayoutMethods.slide_up(this, financeLayout);
            financeLayout.setVisibility(View.GONE);

            tv_finance.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.arrow_down_float,0);
        }
        else{
            financeLayout.setVisibility(View.VISIBLE);
            HandleExpandLayoutMethods.slide_down(this, financeLayout);
            tv_finance.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.arrow_up_float,0);
        }
    }
}
