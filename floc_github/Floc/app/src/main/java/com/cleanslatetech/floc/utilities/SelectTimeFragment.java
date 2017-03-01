package com.cleanslatetech.floc.utilities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.CreateFlocActivity;

/**
 * Created by pimpu on 2/12/2017.
 */

public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    int h, m;
    String strTitle, from;
    Context context;

    public SelectTimeFragment() {
    }

    @SuppressLint("ValidFragment")
    public SelectTimeFragment(String from , String strTitle, int hour, int minute) {
        this.h = hour;
        this.m = minute;
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
            strTitle = savedInstanceState.getString("timeFragmentTitle");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("From", from);
        outState.putString("timeFragmentTitle", strTitle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),this, h, m,
                DateFormat.is24HourFormat(getActivity()));

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
        timePickerDialog.setCustomTitle(tv);

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(from.equals("CreateFloc")) {
            if( ((CreateFlocActivity)context).interfaceOnTimeSet != null) {
                ((CreateFlocActivity)context).interfaceOnTimeSet.getTimeSet(hourOfDay, minute, strTitle);
            }
        }
    }
}
