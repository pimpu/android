package info.alchemistdigital.e_carrier.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import info.alchemistdigital.e_carrier.R;
/**
 * Created by Tapan on 17-Oct-15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceSateate) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen
        String date = day+"/"+(++month)+"/"+year;
        Button dateButton = (Button)getActivity().findViewById(R.id.btn_date);
        dateButton.setText(date);
    }
}
