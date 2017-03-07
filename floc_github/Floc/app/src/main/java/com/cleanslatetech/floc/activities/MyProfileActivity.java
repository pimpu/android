package com.cleanslatetech.floc.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.utilities.HandleExpandLayoutMethods;
import com.cleanslatetech.floc.utilities.InterfaceOnDateSet;
import com.cleanslatetech.floc.utilities.SelectDateFragment;

import java.util.Calendar;

public class MyProfileActivity extends BaseAppCompactActivity implements InterfaceOnDateSet {
    AppCompatTextView tv_personal, tv_finance, tv_dob;
    AppCompatEditText txtEmail;
    View personalLayout, financeLayout;
    public InterfaceOnDateSet interfaceOnDateSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        super.setToolBar(getResources().getString(R.string.my_profile));

        init();
    }

    @Override
    public void getDateSet(int dd, int mm, int yy, String title) {
        long dateMillis = DateHelper.convertToMillis( dd+"/"+mm+"/"+ yy + " 00:00 am");
        String day = DateHelper.getDay(dateMillis);
        String month = DateHelper.getMonth(dateMillis);
        String year = DateHelper.getYear(dateMillis);

        String showDate = day + "-" + month + "-" + year;
        tv_dob.setText(showDate);
    }

    private void init() {
        interfaceOnDateSet = this;

        tv_personal = (AppCompatTextView) findViewById(R.id.openPersonaPanel);
        tv_finance = (AppCompatTextView) findViewById(R.id.openFinancePanel);
        tv_dob = (AppCompatTextView) findViewById(R.id.id_btn_birthDate);
        txtEmail = (AppCompatEditText) findViewById(R.id.personal_email);


        personalLayout = findViewById(R.id.personal_layout);
        financeLayout = findViewById(R.id.finance_layout);

        personalLayout.setVisibility(View.GONE);
        financeLayout.setVisibility(View.GONE);

        txtEmail.setText(new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserEmail)));
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

    public void onClickDOB(View view) {
        Calendar calendar = Calendar.getInstance();

        int Year = 1980;
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);

        DialogFragment newFragment = new SelectDateFragment("MyProfileActivity", "Select your birthday", Year, Month, Day);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
}
