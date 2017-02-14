package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.utilities.InterfaceOnDateSet;
import com.cleanslatetech.floc.utilities.SelectDateFragment;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class PersonalProfileActivity extends BaseAppCompactActivity implements InterfaceOnDateSet {
    public InterfaceOnDateSet interfaceOnDateSet;

    private ArrayAdapter country_adapter;
    private AppCompatAutoCompleteTextView acCountry;
    private AppCompatTextView tvBirthday;
    private String selectedDate;
    private static final int FILE_SELECT_CODE = 100;
    private ImageView imgviewProfilePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);

        super.setToolBar(getResources().getString(R.string.my_profile));

        init();
    }

    private void init() {

        interfaceOnDateSet = this;

        tvBirthday = (AppCompatTextView) findViewById(R.id.id_btn_birthDate);
        imgviewProfilePic = (ImageView) findViewById(R.id.personal_pic);

        String email = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserEmail));
        System.out.println(email);
        ((AppCompatEditText)findViewById(R.id.personal_email)).setText(email);

        // initialised country autocomplete textfield
        acCountry = (AppCompatAutoCompleteTextView) findViewById(R.id.personal_country);
        ArrayList<String> stringArrayCountry = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.country)));
        country_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArrayCountry );
        acCountry.setAdapter(country_adapter);
        acCountry.setThreshold(1);

        acCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                country_adapter.getItem(position).toString();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClickDOB(View view) {
        Calendar calendar = Calendar.getInstance();

        int Year = 1980;
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);

        DialogFragment newFragment = new SelectDateFragment("PersonalProfile", "Select your birthday", Year, Month, Day);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void getDateSet(int dd, int mm, int yy, String title) {
        long dateMillis = DateHelper.convertToMillis( dd+"/"+mm+"/"+ yy + " 00:00 am");
        String day = DateHelper.getDay(dateMillis);
        String month = DateHelper.getMonth(dateMillis);
        String year = DateHelper.getYear(dateMillis);

        selectedDate = String.valueOf(dateMillis);
        String showDate = day + "-" + month + "-" + year;
        tvBirthday.setText(showDate);
        tvBirthday.setTextColor(getResources().getColor(R.color.white));
    }

    public void selectProfilePicture(View view) {
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
                        String path = getPath(PersonalProfileActivity.this, uri);
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        Bitmap bt=Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                        imgviewProfilePic.setImageBitmap(bt);
                        bt.recycle();

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
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
