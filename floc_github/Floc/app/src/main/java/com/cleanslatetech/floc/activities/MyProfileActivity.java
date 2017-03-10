package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.FileUploadAsyncTask;
import com.cleanslatetech.floc.models.MyProfileModel;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.utilities.HandleExpandLayoutMethods;
import com.cleanslatetech.floc.interfaces.InterfaceOnDateSet;
import com.cleanslatetech.floc.utilities.FormValidator;
import com.cleanslatetech.floc.utilities.SelectDateFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.cleanslatetech.floc.utilities.CommonUtilities.getPath;
import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class MyProfileActivity extends BaseAppCompactActivity implements InterfaceOnDateSet {
    private static final int FILE_SELECT_CODE = 101;
    AppCompatTextView tv_personal, tv_finance, tv_dob;
    AppCompatEditText txtEmail, txtFirstName, txtMiddleName, txtLastName, txtProfession, txtPincode,
                        txtCity, txtState, txtContact, txtUrl, txtBankName, txtBranch, txtIfsc, txtAc;
    AppCompatAutoCompleteTextView txtCountry;
    View personalLayout, financeLayout;
    public InterfaceOnDateSet interfaceOnDateSet;
    private String strDOB, filePath, selectedCountry, strMyProfile;
    private JSONObject joMyProfile;
    RadioButton rbMale, rbFemale;
    ImageView myImage;
    long lDateDob = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        super.setToolBar(getResources().getString(R.string.my_profile));

        init();
    }

    private void init() {
        interfaceOnDateSet = this;

        tv_personal = (AppCompatTextView) findViewById(R.id.openPersonaPanel);
        tv_finance = (AppCompatTextView) findViewById(R.id.openFinancePanel);
        tv_dob = (AppCompatTextView) findViewById(R.id.id_btn_birthDate);
        txtEmail = (AppCompatEditText) findViewById(R.id.personal_email);
        txtCountry = (AppCompatAutoCompleteTextView) findViewById(R.id.personal_country);
        txtFirstName = (AppCompatEditText) findViewById(R.id.personal_first_name);
        txtMiddleName = (AppCompatEditText) findViewById(R.id.personal_middle_name);
        txtLastName = (AppCompatEditText) findViewById(R.id.personal_last_name);
        txtProfession = (AppCompatEditText) findViewById(R.id.personal_profession);
        txtPincode = (AppCompatEditText) findViewById(R.id.personal_pincode);
        txtCity = (AppCompatEditText) findViewById(R.id.personal_city);
        txtState = (AppCompatEditText) findViewById(R.id.personal_state);
        txtContact = (AppCompatEditText) findViewById(R.id.personal_contact);
        txtUrl = (AppCompatEditText) findViewById(R.id.personal_url);
        txtBankName = (AppCompatEditText) findViewById(R.id.personal_bank_name);
        txtBranch = (AppCompatEditText) findViewById(R.id.personal_bank_branch);
        txtIfsc = (AppCompatEditText) findViewById(R.id.personal_bank_Ifsc_code);
        txtAc = (AppCompatEditText) findViewById(R.id.personal_bank_account_numner);
        myImage = (ImageView) findViewById(R.id.personal_pic);

        personalLayout = findViewById(R.id.personal_layout);
        financeLayout = findViewById(R.id.finance_layout);

        personalLayout.setVisibility(View.GONE);
        financeLayout.setVisibility(View.GONE);

        // initialised country autocomplete textfield
        txtCountry = (AppCompatAutoCompleteTextView) findViewById(R.id.personal_country);
        final ArrayList<String> stringArrayCountry = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.country)));
        final ArrayAdapter country_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArrayCountry);
        txtCountry.setAdapter(country_adapter);
        txtCountry.setThreshold(1);

        txtCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = (String) parent.getItemAtPosition(position);
            }
        });

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);

        strMyProfile = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdMyProfile));

        joMyProfile = new JSONObject();
        try {

            txtEmail.setText(new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserEmail)));

            if(strMyProfile != null) {

                joMyProfile = new JSONObject(strMyProfile);

                String firstName = joMyProfile.getString("FirstName");
                firstName = firstName.equals("null") ? "" : firstName;
                txtFirstName.setText(firstName);

                String lastName = joMyProfile.getString("LastName");
                lastName = lastName.equals("null") ? "" : lastName;
                txtLastName.setText(lastName);

                String middleName = joMyProfile.getString("MiddleName");
                middleName = middleName.equals("null") ? "" : middleName;
                txtMiddleName.setText(middleName);

                if(joMyProfile.getString("Gender").equals("Male")) {
                    rbMale.setChecked(true);
                } else if(joMyProfile.getString("Gender").equals("Female")) {
                    rbFemale.setChecked(true);
                }

                String contact = joMyProfile.getString("Contact");
                contact = contact.equals("null") ? "" : contact;
                txtContact.setText(contact);

                String profession = joMyProfile.getString("Profession");
                profession = profession.equals("null") ? "" : profession;
                txtProfession.setText(profession);


                String city = joMyProfile.getString("City");
                city = city.equals("null") ? "" : city;
                txtCity.setText(city);

                String state = joMyProfile.getString("State");
                state = state.equals("null") ? "" : state;
                txtState.setText(state);

                String country = joMyProfile.getString("Country");
                country = country.equals("null") ? "" : country;
                txtCountry.setText(country);

                String pinCode = joMyProfile.getString("PinCode");
                pinCode = pinCode.equals("null") ? "" : pinCode;
                txtPincode.setText(pinCode);

                Glide
                        .with(this)
                        .load( CommonVariables.EVENT_IMAGE_SERVER_URL + joMyProfile.getString("ProfilePic"))
                        .placeholder(R.drawable.textarea_gradient_bg)
                        .dontAnimate()
                        .into(myImage);

                lDateDob = DateHelper.dobConvertToMillis(joMyProfile.getString("BirthDate"));
                String day = DateHelper.getDay(lDateDob);
                String month = DateHelper.getMonth(lDateDob);
                String year = DateHelper.getYear(lDateDob);

                strDOB = year+"-"+month+"-"+day;
                String showDate = day + "-" + month + "-" + year;
                tv_dob.setText(showDate);
                tv_dob.setTextColor(getResources().getColor(R.color.white));

                String bankName = joMyProfile.getString("BankName");
                bankName = bankName.equals("null") ? "" : bankName;
                txtBankName.setText(bankName);

                String branch = joMyProfile.getString("Branch");
                branch = branch.equals("null") ? "" : branch;
                txtBranch.setText(branch);

                String ifsc = joMyProfile.getString("IFSC");
                ifsc = ifsc.equals("null") ? "" : ifsc;
                txtIfsc.setText(ifsc);

                String account = joMyProfile.getString("Account");
                account = account.equals("null") ? "" : account;
                txtAc.setText(account);

                String url = joMyProfile.getString("URL");
                url = url.equals("null") ? "" : url;
                txtUrl.setText(url);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        int Day, Month, Year;
        if(lDateDob != 0) {
            Day = Integer.parseInt(DateHelper.getDay(lDateDob));
            Month = Integer.parseInt(DateHelper.getMonth(lDateDob))-1;
            Year = Integer.parseInt(DateHelper.getYear(lDateDob));
        }
        else {

            Calendar calendar = Calendar.getInstance();

            Year = 1980;
            Month = calendar.get(Calendar.MONTH);
            Day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DialogFragment newFragment = new SelectDateFragment("MyProfileActivity", "Select your birthday", Year, Month, Day);
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void getDateSet(int dd, int mm, int yy, String title) {
        long dateMillis = DateHelper.convertToMillis( dd+"/"+mm+"/"+ yy + " 00:00 am");
        String day = DateHelper.getDay(dateMillis);
        String month = DateHelper.getMonth(dateMillis);
        String year = DateHelper.getYear(dateMillis);

        strDOB = year+"-"+month+"-"+day;
        String showDate = day + "-" + month + "-" + year;
        tv_dob.setText(showDate);
        tv_dob.setTextColor(getResources().getColor(R.color.white));
    }

    public void selectProfilePicture(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a Profile to Upload"),
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
                        filePath = getPath(MyProfileActivity.this, uri);
                        File imgFile = new  File(filePath);

                        if(imgFile.exists()){

                            Bitmap profileBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                            myImage.setImageBitmap(profileBitmap);

                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickUpdatePersonalInfo(View view) {
        // Check if Internet present
        if (!isConnectingToInternet(MyProfileActivity.this)) {
            CommonUtilities.customToast(MyProfileActivity.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        } else {

            String selectedGender = null;

            if(rbMale.isChecked()) {
                selectedGender = "Male";
            } else if(rbFemale.isChecked()) {
                selectedGender = "Female";
            }

            String strBank = null, strBranch = null, strIFSC = null, strAc = null;
            try {
                strBank = joMyProfile.getString("BankName").equals("null") ? "test" : joMyProfile.getString("BankName");
                strBranch = joMyProfile.getString("Branch").equals("null") ? "test" : joMyProfile.getString("Branch");
                strIFSC = joMyProfile.getString("IFSC").equals("null") ? "test" : joMyProfile.getString("IFSC");
                strAc = joMyProfile.getString("Account").equals("null") ? "test" : joMyProfile.getString("Account");

                MyProfileModel myProfileModel = new MyProfileModel(
                        new GetSharedPreference(MyProfileActivity.this).getInt(getResources().getString(R.string.shrdLoginId)),
                        new GetSharedPreference(MyProfileActivity.this).getString(getResources().getString(R.string.shrdUserName)),
                        txtFirstName.getText().toString(),
                        txtMiddleName.getText().toString(),
                        txtLastName.getText().toString(),
                        selectedGender,
                        txtContact.getText().toString(),
                        txtEmail.getText().toString(),
                        txtProfession.getText().toString(),
                        txtCity.getText().toString(),
                        txtState.getText().toString(),
                        selectedCountry,
                        txtPincode.getText().toString(),
                        filePath,
                        strDOB,
                        strBank,
                        strBranch,
                        strIFSC,
                        strAc,
                        txtUrl.getText().toString() );

                if( new FormValidator().validateField(myProfileModel, MyProfileActivity.this)) {

                    if(strMyProfile == null) {
                        myProfileModel.setBankName("");
                        myProfileModel.setBranch("");
                        myProfileModel.setIFSC("");
                        myProfileModel.setAccount("");
                    }

                    // hide soft keyboard when it is open
                    View view1 = getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }

                    new FileUploadAsyncTask(MyProfileActivity.this, myProfileModel, filePath, CommonVariables.POST_IMAGE_SERVER_URL).execute();

                }
            } catch (JSONException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public void onClickUpdatePersonalFinance(View view) {
        // Check if Internet present
        if (!isConnectingToInternet(MyProfileActivity.this)) {
            CommonUtilities.customToast(MyProfileActivity.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;
        } else {
            String strFirstName = null, strLastName = null, strMiddleName = null, strGender = null,
                    strContact = null, strEmail = null, strProfession = null, strCity = null,
                    strState = null, strCountry = null, strPin = null, strPic = null, strBirth = null, strUrl = null;

            try {
                strFirstName = joMyProfile.getString("FirstName").equals("null") ? "test" : joMyProfile.getString("FirstName");
                strMiddleName = joMyProfile.getString("MiddleName").equals("null") ? "test" : joMyProfile.getString("MiddleName");
                strLastName = joMyProfile.getString("LastName").equals("null") ? "test" : joMyProfile.getString("LastName");
                strGender = joMyProfile.getString("Gender").equals("null") ? "test" : joMyProfile.getString("Gender");
                strContact = joMyProfile.getString("Contact").equals("null") ? "test" : joMyProfile.getString("Contact");
                strEmail = joMyProfile.getString("EmailId").equals("null") ? "test" : joMyProfile.getString("EmailId");
                strProfession = joMyProfile.getString("Profession").equals("null") ? "test" : joMyProfile.getString("Profession");
                strCity = joMyProfile.getString("City").equals("null") ? "test" : joMyProfile.getString("City");
                strState = joMyProfile.getString("State").equals("null") ? "test" : joMyProfile.getString("State");
                strCountry = joMyProfile.getString("Country").equals("null") ? "test" : joMyProfile.getString("Country");
                strPin = joMyProfile.getString("PinCode").equals("null") ? "test" : joMyProfile.getString("PinCode");
                strPic = joMyProfile.getString("ProfilePic").equals("null") ? "test" : joMyProfile.getString("ProfilePic");
                strBirth = joMyProfile.getString("BirthDate").equals("null") ? "test" : joMyProfile.getString("BirthDate");
                strUrl = joMyProfile.getString("URL").equals("null") ? "test" : joMyProfile.getString("URL");

                MyProfileModel myProfileModel = new MyProfileModel(
                        new GetSharedPreference(MyProfileActivity.this).getInt(getResources().getString(R.string.shrdLoginId)),
                        new GetSharedPreference(MyProfileActivity.this).getString(getResources().getString(R.string.shrdUserName)),
                        strFirstName,
                        strMiddleName,
                        strLastName,
                        strGender,
                        strContact,
                        strEmail,
                        strProfession,
                        strCity,
                        strState,
                        strCountry,
                        strPin,
                        strPic,
                        strBirth,
                        txtBankName.getText().toString(),
                        txtBranch.getText().toString(),
                        txtIfsc.getText().toString(),
                        txtAc.getText().toString(),
                        strUrl );

                if( new FormValidator().validateField(myProfileModel, MyProfileActivity.this)) {

                    if(strMyProfile == null) {
                        myProfileModel.setFirstName("");
                        myProfileModel.setLastName("");
                        myProfileModel.setMiddleName("");
                        myProfileModel.setGender("");
                        myProfileModel.setContact("");
                        myProfileModel.setEmailId("");
                        myProfileModel.setProfession("");
                        myProfileModel.setCity("");
                        myProfileModel.setState("");
                        myProfileModel.setCountry("");
                        myProfileModel.setPinCode("");
                        myProfileModel.setProfilePic("");
                        myProfileModel.setBirthDate("");
                        myProfileModel.setURL("");
                    }

                    // hide soft keyboard when it is open
                    View view1 = getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }

                    new FileUploadAsyncTask(MyProfileActivity.this, myProfileModel, filePath, CommonVariables.POST_IMAGE_SERVER_URL).execute();
                }

            } catch (JSONException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}
