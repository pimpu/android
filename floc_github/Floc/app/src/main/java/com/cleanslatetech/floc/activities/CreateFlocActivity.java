package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSpinnerAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.utilities.InterfaceOnDateSet;
import com.cleanslatetech.floc.utilities.InterfaceOnTimeSet;
import com.cleanslatetech.floc.utilities.SelectDateFragment;
import com.cleanslatetech.floc.utilities.SelectTimeFragment;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CreateFlocActivity extends BaseAppCompactActivity implements AdapterView.OnItemSelectedListener,
                                            InterfaceOnDateSet, InterfaceOnTimeSet {
    public InterfaceOnDateSet interfaceOnDateSet;
    public InterfaceOnTimeSet interfaceOnTimeSet;
    private CustomSpinnerAdapter adapterInterest, adapterPrice;
    private AppCompatSpinner spinnerCategory, spinnerPrice;
    private AppCompatTextView tvSelectedFile, tvStartDate, tvStartTime, tvEndDate, tvEndTime;

    private static final int FILE_SELECT_CODE = 0;
    private String selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_floc);

        super.setToolBar(getResources().getString(R.string.create_floc));

        init();
    }

    private void init() {
        interfaceOnDateSet = this;
        interfaceOnTimeSet = this;

        String uName = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserName));
        ((AppCompatEditText)findViewById(R.id.floc_name)).setText(uName);

        // populate interest category spinner
        spinnerCategory = (AppCompatSpinner) findViewById(R.id.id_spinner_category);
        ArrayList<String> stringArrayInterest = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.interests)));
        adapterInterest = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, stringArrayInterest);
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterInterest);
        spinnerCategory.setOnItemSelectedListener(this);

        // populate price type spinner
        spinnerPrice = (AppCompatSpinner) findViewById(R.id.id_spinner_price_type);
        ArrayList<String> stringArrayPrice = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.price_type)));
        adapterPrice = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, stringArrayPrice);
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(adapterPrice);
        spinnerPrice.setOnItemSelectedListener(this);

        tvSelectedFile = (AppCompatTextView) findViewById(R.id.tv_selected_file);
        tvStartDate = (AppCompatTextView) findViewById(R.id.id_btn_startDate);
        tvStartTime = (AppCompatTextView) findViewById(R.id.id_btn_startTime);
        tvEndDate = (AppCompatTextView) findViewById(R.id.id_btn_endDate);
        tvEndTime = (AppCompatTextView) findViewById(R.id.id_btn_endTime);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void selectPicture(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a Picture to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    // Get the path
                    try {
                        String path = getPath(CreateFlocActivity.this, uri);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data", MediaStore.Video.Media.TITLE };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
                if (cursor.moveToFirst()) {
                    tvSelectedFile.setText(cursor.getString(columnIndex));
                    tvSelectedFile.setSelected(true);
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            tvSelectedFile.setText(uri.getLastPathSegment());
            tvSelectedFile.setSelected(true);
            return uri.getPath();
        }

        return null;
    }

    public void selectStarDate(View view) {
        Calendar calendar = Calendar.getInstance();

        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);

        DialogFragment newFragment = new SelectDateFragment("CreateFloc", "Select Start Date", Year, Month, Day);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void selectStarTime(View view) {
        Calendar calendar = Calendar.getInstance();

        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);

        DialogFragment newFragment = new SelectTimeFragment("CreateFloc", "Select Start Time", h, m);
        newFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void selectEndDate(View view) {
        Calendar calendar = Calendar.getInstance();

        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);

        DialogFragment newFragment = new SelectDateFragment("CreateFloc", "Select End Date", Year, Month, Day);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void selectEndTime(View view) {
        Calendar calendar = Calendar.getInstance();

        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);

        DialogFragment newFragment = new SelectTimeFragment("CreateFloc", "Select End Time", h, m);
        newFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    @Override
    public void getDateSet(int dd, int mm, int yy, String title) {
        long dateMillis = DateHelper.convertToMillis( dd+"/"+mm+"/"+ yy + " 00:00 am");
        String day = DateHelper.getDay(dateMillis);
        String month = DateHelper.getMonth(dateMillis);
        String year = DateHelper.getYear(dateMillis);

        selectedDate = String.valueOf(dateMillis);
        String showDate = day + "-" + month + "-" + year;

        if(title.equals("Select Start Date")) {
            tvStartDate.setText(showDate);
        }else if(title.equals("Select End Date")) {
            tvEndDate.setText(showDate);
        }
    }

    @Override
    public void getTimeSet(int hour, int minute, String aMpM, String title) {
        String formatMinute;
        if(minute < 10 ) {
            formatMinute = "0"+minute;
        }
        else {
            formatMinute = String.valueOf(minute);
        }

        String showTime = String.valueOf(hour) + ":" + formatMinute + " " + aMpM;

        if(title.equals("Select Start Time")) {
            tvStartTime.setText(showTime);
        } else if(title.equals("Select End Time")) {
            tvEndTime.setText(showTime);
        }
    }

    public void goToNextCrateFlocPage(View view) {
        startActivity(new Intent(this, CreateFlocActivitySecond.class));
    }
}
