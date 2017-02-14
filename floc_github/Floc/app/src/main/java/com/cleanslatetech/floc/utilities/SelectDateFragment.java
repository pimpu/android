package com.cleanslatetech.floc.utilities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.CreateFlocActivity;
import com.cleanslatetech.floc.activities.PersonalProfileActivity;

import java.util.Calendar;

/**
 * Created by pimpu on 2/12/2017.
 */

public class SelectDateFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    int yy, mm, dd;
    String strTitle, from;
    Context context;

    public SelectDateFragment() {
    }

    @SuppressLint("ValidFragment")
    public SelectDateFragment(String from , String strTitle, int year, int month, int day) {
        this.yy = year;
        this.mm = month;
        this.dd = day;
        this.strTitle = strTitle;
        this.from = from;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( savedInstanceState != null ) {
            from = savedInstanceState.getString("From");
            strTitle = savedInstanceState.getString("dateFragmentTitle");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("From", from);
        outState.putString("dateFragmentTitle", strTitle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
        if(strTitle.equals("Select your birthday")) {
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }

        // Create a TextView programmatically.
        TextView tv = new TextView(getActivity());

        // Create a TextView programmatically
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 10, 10, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
        tv.setText(strTitle);
        tv.setTextColor(getResources().getColor(R.color.dark_blue));
        tv.setBackgroundColor(getResources().getColor(R.color.white));
        datePickerDialog.setCustomTitle(tv);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        if(from.equals("CreateFloc")) {
            if( ((CreateFlocActivity)context).interfaceOnDateSet != null) {
                ((CreateFlocActivity)context).interfaceOnDateSet.getDateSet(dd,(mm+1), yy, strTitle);
            }
        }else if(from.equals("PersonalProfile")) {
            if( ((PersonalProfileActivity)context).interfaceOnDateSet != null) {
                ((PersonalProfileActivity)context).interfaceOnDateSet.getDateSet(dd,(mm+1), yy, strTitle);
            }
        }
    }
}
