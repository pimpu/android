package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.CustomSpinnerAdapter;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.interfaces.InterfaceOnDateSet;
import com.cleanslatetech.floc.interfaces.InterfaceOnTimeSet;
import com.cleanslatetech.floc.utilities.SelectDateFragment;
import com.cleanslatetech.floc.utilities.SelectTimeFragment;
import com.cleanslatetech.floc.utilities.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.cleanslatetech.floc.utilities.CommonUtilities.customToast;
import static com.cleanslatetech.floc.utilities.CommonUtilities.getFileName;
import static com.cleanslatetech.floc.utilities.CommonUtilities.getPath;

public class CreateFlocActivity extends BaseAppCompactActivity implements AdapterView.OnItemSelectedListener,
                                            InterfaceOnDateSet, InterfaceOnTimeSet {
    public InterfaceOnDateSet interfaceOnDateSet;
    public InterfaceOnTimeSet interfaceOnTimeSet;
    private CustomSpinnerAdapter adapterInterest, adapterPrice;
    private AppCompatSpinner spinnerCategory, spinnerPrice;
    private AppCompatTextView tvSelectedFile, tvStartDate, tvStartTime, tvEndDate, tvEndTime;
    private JSONArray categoryJsonArray;

    private static final int FILE_SELECT_CODE = 0;
    private int selectedCatId, selectedPriceType;
    private RelativeLayout layout_price;
    private String startDate, endDate, strStartHr, strEndHr, strStartMin, strEndMin, filePath;

    static CreateFlocActivity createFlocActivityInstance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_floc);

        super.setToolBar(getResources().getString(R.string.create_floc));

        createFlocActivityInstance = this;

        init();
    }

    public static CreateFlocActivity getInstance() {
        return createFlocActivityInstance;
    }

    private void init() {
        interfaceOnDateSet = this;
        interfaceOnTimeSet = this;

        layout_price = (RelativeLayout) findViewById(R.id.id_layout_amount);

        // populate interest category spinner
        spinnerCategory = (AppCompatSpinner) findViewById(R.id.id_spinner_category);
        ArrayList<String> stringArrayInterest = new ArrayList<String>();
        String categoryString = new GetSharedPreference(CreateFlocActivity.this).getString(getResources().getString(R.string.shrdAllCategoryList));
        try {
            categoryJsonArray = new JSONArray(categoryString);
            for (int j = 0 ; j < categoryJsonArray.length(); j++) {
                stringArrayInterest.add(categoryJsonArray.getJSONObject(j).getString("EventCategoryName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            selectedText.setTextColor(getResources().getColor(R.color.white));
        }

        AppCompatSpinner spinner = (AppCompatSpinner) parent;
        switch (spinner.getId()) {
            case R.id.id_spinner_category:
                try {
                    selectedCatId = categoryJsonArray.getJSONObject(position).getInt("EventCategoryId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.id_spinner_price_type:
                if(parent.getItemAtPosition(position).toString().equals("Free")) {
                    selectedPriceType = 1;
                    layout_price.setVisibility(View.GONE);
                }
                else {
                    selectedPriceType = 2;
                    layout_price.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void selectPicture(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a Picture to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            CommonUtilities.customToast(this, "Please install a File Manager.");
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
                        filePath = getPath(CreateFlocActivity.this, uri);
                        tvSelectedFile.setText(getFileName(filePath));
                        tvSelectedFile.setSelected(true);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

        String showDate = day + "-" + month + "-" + year;

        if(title.equals("Select Start Date")) {
            startDate = year+"-"+month+"-"+day;
            tvStartDate.setText(showDate);
        }else if(title.equals("Select End Date")) {
            endDate = yy+"-"+mm+"-"+dd;
            tvEndDate.setText(showDate);
        }
    }

    @Override
    public void getTimeSet(int hour, int minute, String title) {
        //Get the AM or PM for current time
        String aMpM = "AM";
        if(hour >11) {
            aMpM = "PM";
        }

        //Make the 24 hour time format to 12 hour time format
        int currentHour;
        if(hour>11) {
            currentHour = hour - 12;
        } else {
            currentHour = hour;
        }

        String formatMinute;
        if(minute < 10 ) {
            formatMinute = "0"+minute;
        }
        else {
            formatMinute = String.valueOf(minute);
        }

        String showTime = String.valueOf(currentHour) + ":" + formatMinute + " " + aMpM;

        if(title.equals("Select Start Time")) {
            tvStartTime.setText(showTime);
            strStartHr = String.valueOf(hour);
            strStartMin = String.valueOf(minute);

        } else if(title.equals("Select End Time")) {
            tvEndTime.setText(showTime);
            strEndHr = String.valueOf(hour);
            strEndMin = String.valueOf(minute);
        }
    }

    public void goToNextCrateFlocPage(View view) {
        AppCompatEditText txtName = ((AppCompatEditText)findViewById(R.id.floc_name));
        AppCompatEditText txtDesc = (AppCompatEditText) findViewById(R.id.floc_description);
        AppCompatEditText txtAmount = (AppCompatEditText) findViewById(R.id.id_price);

        Boolean flag = true;

        Boolean boolEventName = Validations.isEmptyString(txtName.getText().toString().trim());
        Boolean boolDesc = Validations.isEmptyString(txtDesc.getText().toString().trim());

        if(!boolEventName) {
            customToast(CreateFlocActivity.this, "Event name field is empty");
            flag = false;
        } else if(!boolDesc) {
            customToast(CreateFlocActivity.this, "Event description field is empty");
            flag = false;
        } else if( filePath == null ) {
            customToast(CreateFlocActivity.this, "Please, Select event picture");
            flag = false;
        } else if( startDate == null ) {
            customToast(CreateFlocActivity.this, "Please, Select start date");
            flag = false;
        } else if( strStartHr == null ) {
            customToast(CreateFlocActivity.this, "Please, Select start time");
            flag = false;
        } else if( endDate == null ) {
            customToast(CreateFlocActivity.this, "Please, Select end date");
            flag = false;
        } else if( strEndHr == null ) {
            customToast(CreateFlocActivity.this, "Please, Select end time");
            flag = false;
        } else if(selectedPriceType == 2 ) {
            Boolean boolPrice = Validations.isEmptyString(txtAmount.getText().toString().trim());
            if(!boolPrice) {
                customToast(CreateFlocActivity.this, "Please, Select event price");
                flag = false;
            }
        }

        if(!flag) {
            return;
        }

        JSONObject jsonObjCreate = new JSONObject();
        try {

            jsonObjCreate.put("name",txtName.getText().toString().trim());
            jsonObjCreate.put("catId",selectedCatId);
            jsonObjCreate.put("desc",txtDesc.getText().toString().trim());
            jsonObjCreate.put("filePath",filePath);
            jsonObjCreate.put("startDate",startDate);
            jsonObjCreate.put("startHr",strStartHr);
            jsonObjCreate.put("startMin",strStartMin);
            jsonObjCreate.put("endDate",endDate);
            jsonObjCreate.put("endHr",strEndHr);
            jsonObjCreate.put("endMin",strEndMin);
            jsonObjCreate.put("priceType",selectedPriceType);

            if(selectedPriceType == 2) {
                jsonObjCreate.put("price",txtAmount.getText().toString().trim());
            }
            else {
                jsonObjCreate.put("price","0");
            }

            Intent intent = new Intent(this, CreateFlocActivitySecond.class);
            intent.putExtra("flocData", jsonObjCreate.toString());
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
