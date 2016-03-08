package com.alchemistdigital.kissan.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.InsertEnquiryAsyncTask;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.SetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.CommonVariables;
import com.andexert.library.RippleView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.getFileName;
import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.DateHelper.getRefStringDate;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;
import static com.alchemistdigital.kissan.utilities.Validations.phoneValiate;

public class Create_Enquiry extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;
    private TextView tvFileName;
    private EditText txt_ref_no, txt_contact, txt_email, txt_address, txt_message;
    private String selectedFileName, str_ref_no, str_name, str_contact, str_email, str_address, str_message;
    TextInputLayout refInputLayout, contactInputLayout, emailInputLayout, addressInputLayuot, messageInputLayout;
    Spinner spinnerSociety;
    private ArrayAdapter<String> adapterSociety;
    private List<Society> allSocieties;
    private SetSharedPreferenceHelper setPreference;
    private View createEnquiryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(Create_Enquiry.this);
        int rowsCount = dbHelper.numberOfRows();
        dbHelper.closeDB();

        if (rowsCount <= 0) {
            setContentView(R.layout.society_unavailable);
            Toolbar toolbar = (Toolbar) findViewById(R.id.create_enquiry_toolbar);
            setSupportActionBar(toolbar);
            return;
        }

        setContentView(R.layout.activity_create__enquiry);

        createEnquiryView = findViewById(R.id.id_CreateEnquiryView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_enquiry_toolbar);
        setSupportActionBar(toolbar);

        setPreference   = new SetSharedPreferenceHelper(Create_Enquiry.this);
        setPreference.setBoolScanPicture(getResources().getString(R.string.bool_scan_image),false);

        // creating spinner from society table
        spinnerSociety = (Spinner) findViewById(R.id.spinner_id_societies);
        spinnerSociety.setOnItemSelectedListener(this);

        // getting all rows of society
        allSocieties = dbHelper.getAllSocieties();

        // only get society names
        ArrayList<String> societyNames = new ArrayList<String>();
        for (int i = 0 ; i < allSocieties.size() ; i++ ){
            societyNames.add(allSocieties.get(i).getSoc_name());
        }
        adapterSociety = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, societyNames );
        adapterSociety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSociety.setAdapter(adapterSociety);
        adapterSociety.notifyDataSetChanged();

        tvFileName = (TextView) findViewById(R.id.tv_id_UploadedImage);
        txt_ref_no = (EditText) findViewById(R.id.edittext_id_ref_no);
        txt_ref_no.setText("REF"+getRefStringDate());

        txt_contact = (EditText) findViewById(R.id.edittext_id_society_contact);
        txt_email = (EditText) findViewById(R.id.edittext_id_society_email);
        txt_address = (EditText) findViewById(R.id.edittext_id_society_address);
        txt_message = (EditText) findViewById(R.id.edittext_id_enquiry_message);

        refInputLayout = (TextInputLayout) findViewById(R.id.id_input_layout_ref_no);
        contactInputLayout = (TextInputLayout) findViewById(R.id.id_input_layout_society_contact);
        emailInputLayout = (TextInputLayout) findViewById(R.id.id_input_layout_society_email);
        addressInputLayuot = (TextInputLayout) findViewById(R.id.id_input_layout_society_address);
        messageInputLayout = (TextInputLayout) findViewById(R.id.id_input_layout_enquiry_message);

        // get society row by id for auto text in society detail's edittext.
        // for this method, sending the position of spinner data as parameter.
        // i.e. first position of spinner
        setSocietyDetailsEdittext(0);

        final RippleView rippleView = (RippleView) findViewById(R.id.btn_id_submit_enquiry);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_ref_no = txt_ref_no.getText().toString();
                str_contact = txt_contact.getText().toString();
                str_email = txt_email.getText().toString();
                str_address = txt_address.getText().toString();
                str_message = txt_message.getText().toString();

                Boolean boolFileName = isEmptyString(selectedFileName);
                Boolean boolRef = isEmptyString(str_ref_no);
                Boolean boolContact = phoneValiate(str_contact);
                Boolean boolEmail = emailValidate(str_email);
                Boolean boolAddress = isEmptyString(str_address);
                Boolean boolMessage = isEmptyString(str_message);

                if (!boolFileName) {
                    Toast.makeText(getApplicationContext(), "Please,choose file.", Toast.LENGTH_LONG).show();
                }

                if (boolRef) {
                    refInputLayout.setErrorEnabled(false);
                } else {
                    refInputLayout.setErrorEnabled(true);
                    refInputLayout.setError("reference field is empty.");
                }

                if (boolContact) {
                    contactInputLayout.setErrorEnabled(false);
                } else {
                    contactInputLayout.setErrorEnabled(true);
                    contactInputLayout.setError("contact field is empty.");
                }

                if (boolEmail) {
                    emailInputLayout.setErrorEnabled(false);
                } else {
                    emailInputLayout.setErrorEnabled(true);
                    emailInputLayout.setError("You need to enter correct email-id");
                }

                if (boolAddress) {
                    addressInputLayuot.setErrorEnabled(false);
                } else {
                    addressInputLayuot.setErrorEnabled(true);
                    addressInputLayuot.setError("address field is empty.");
                }

                if (boolMessage) {
                    messageInputLayout.setErrorEnabled(false);
                } else {
                    messageInputLayout.setErrorEnabled(true);
                    messageInputLayout.setError("message field is empty.");
                }


                if (boolRef && boolContact && boolEmail && boolAddress && boolMessage && boolFileName) {

                    // Check if Internet present
                    if (!isConnectingToInternet(Create_Enquiry.this)) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        /*System.out.println(str_ref_no);
                        System.out.println(str_name);
                        System.out.println(str_contact);
                        System.out.println(str_email);
                        System.out.println(str_address);
                        System.out.println(str_message);*/
                        String filepath = CommonVariables.SCAN_FILE_PATH+"/"+selectedFileName;

                        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_Enquiry.this);
                        int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
                        String strUID = String.valueOf(uId);
                        String userType = getPreference.getUserTypePreference(getResources().getString(R.string.userType));
                        String eId = "0";

                        // Check if Internet present
                        if (!isConnectingToInternet(Create_Enquiry.this)) {
                            // Internet Connection is not present
                            Snackbar.make(createEnquiryView, "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Retry", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            onCreate(null);
                                        }
                                    }).show();
                            // stop executing code by return
                            return;
                        } else {
                            new InsertEnquiryAsyncTask(Create_Enquiry.this,
                                    str_ref_no,
                                    str_name,
                                    str_contact,
                                    str_email,
                                    str_address,
                                    str_message,
                                    filepath,
                                    strUID,
                                    userType,
                                    eId).execute();
                        }
                    }
                }
            }
        });

    }

    private void setSocietyDetailsEdittext(int position) {
        Society societyByIdData = dbHelper.getSocietyById( allSocieties.get(position).getId() );
        txt_contact.setText(societyByIdData.getSoc_contact());
        txt_email.setText(societyByIdData.getSoc_email());
        txt_address.setText(societyByIdData.getSoc_adrs());
    }

    public void goToCreateSociety(View v) {
        startActivity(new Intent(Create_Enquiry.this, Create_Society.class));
        finish();
    }

    public void openFileChooser(View v) {
        // first check obp directory is exist
        // if exist then show option for selecting pic from gallery
        // if not exist then go to a scanner activity.
        File dir = new File(CommonVariables.SCAN_FILE_PATH);
        if(dir.exists() && dir.isDirectory()) {
            // do something here
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Select from gallery?");

            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    // set preference is false because when we do not get file path from
                    // shared preference in onResume() of this activity.
                    setPreference.setBoolScanPicture(getResources().getString(R.string.bool_scan_image),false);

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(CommonVariables.SCAN_FILE_PATH);
                    intent.setDataAndType(uri, "image/png");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                    // set preference is true so that when we get file path from
                    // shared preference in onResume() of this activity.
                    // because bitmap is create on scannerActivity class
                    setPreference.setBoolScanPicture(getResources().getString(R.string.bool_scan_image), true);
                    startActivity(new Intent(Create_Enquiry.this, scannerActivity.class));
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else {
            setPreference.setBoolScanPicture(getResources().getString(R.string.bool_scan_image), true);
            startActivity(new Intent(Create_Enquiry.this, scannerActivity.class));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                selectedFileName = getFileName(Create_Enquiry.this, filePath);
                tvFileName.setText(selectedFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        String item = parent.getItemAtPosition(position).toString();
        if(spinner.getId() == R.id.spinner_id_societies){
            System.out.println("Selected from society spinner: "+item);
            str_name = item;
            setSocietyDetailsEdittext(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_Enquiry.this);
        boolean boolScanPicture = getPreference.getBoolScanPicture(getResources().getString(R.string.bool_scan_image));
        if(boolScanPicture){
            Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
            String filePathPreference = getPreference.getFilePathPreference(getResources().getString(R.string.sharedPreference_filepath));
            if(filePathPreference != null){
//                filePathPreference = .../filename.png
                int cut = filePathPreference.lastIndexOf('/');
                if (cut != -1) {
                    selectedFileName = filePathPreference.substring(cut + 1);
//                    result = filename.png
                    tvFileName.setText(selectedFileName);
                }

            }
        }
    }
}
