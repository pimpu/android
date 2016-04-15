package info.alchemistdigital.e_carrier.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

import info.alchemistdigital.e_carrier.R;

import static info.alchemistdigital.e_carrier.utilities.DateHelper.getHours;
import static info.alchemistdigital.e_carrier.utilities.DateHelper.getMinute;

/**
 * Created by server on 10/23/2015.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        long dateMillis = System.currentTimeMillis();

        int hour = Integer.valueOf(getHours(dateMillis));
        int minute = Integer.valueOf(getMinute(dateMillis));

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,DateFormat.is24HourFormat( getActivity() ) );
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        String aMpM = "AM";
        if(hourOfDay >11)
        {
            aMpM = "PM";
        }

        //Make the 24 hour time format to 12 hour time format
        int currentHour;
        if(hourOfDay>11)
        {
            currentHour = hourOfDay - 12;
        }
        else
        {
            currentHour = hourOfDay;
        }

        String time=null;
        if(minute < 10 ){
            time=String.valueOf(currentHour)+":0"+String.valueOf(minute)+" "+aMpM;
        }
        else {
            time=String.valueOf(currentHour)+":"+String.valueOf(minute)+" "+aMpM;
        }
        Button TimeButton = (Button)getActivity().findViewById(R.id.btn_time);
        TimeButton.setText(time);
    }
}
