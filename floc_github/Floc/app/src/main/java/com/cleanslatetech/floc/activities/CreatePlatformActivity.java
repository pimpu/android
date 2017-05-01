package com.cleanslatetech.floc.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.FileUploadAsyncTask;
import com.cleanslatetech.floc.asynctask.InsertMyProfileAsyncTask;
import com.cleanslatetech.floc.models.ChannelModel;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.DateHelper;
import com.cleanslatetech.floc.utilities.FormValidator;

import java.net.URISyntaxException;
import java.util.Date;

import static com.cleanslatetech.floc.utilities.CommonUtilities.getFileName;
import static com.cleanslatetech.floc.utilities.CommonUtilities.getPath;
import static com.cleanslatetech.floc.utilities.CommonUtilities.isConnectingToInternet;

public class CreatePlatformActivity extends BaseAppCompactActivity {

    private AppCompatTextView tvSelectedFile, tvChannelOwner;
    private static final int FILE_SELECT_CODE = 103;
    private String filePath;
    private AppCompatEditText txtChannelName, txtChannelPwd, txtChannelDesc;
    RadioButton rbPublic, rbPrivate, rbEcommerce, rbChannel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_platform);

        super.setToolBar(getResources().getString(R.string.build_a_platform));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void init() {
        tvSelectedFile = (AppCompatTextView) findViewById(R.id.tv_selected_channel_file);
        tvChannelOwner = (AppCompatTextView) findViewById(R.id.id_owner_name);

        txtChannelName = (AppCompatEditText) findViewById(R.id.id_channel_name);
        txtChannelPwd = (AppCompatEditText) findViewById(R.id.id_platform_pwd);
        txtChannelDesc = (AppCompatEditText) findViewById(R.id.id_channel_desc);

        rbPublic = (RadioButton) findViewById(R.id.rb_channel_public);
        rbPrivate = (RadioButton) findViewById(R.id.rb_channel_private);
        rbEcommerce = (RadioButton) findViewById(R.id.rb_platform_ecommerce);
        rbChannel = (RadioButton) findViewById(R.id.rb_platform_channel);

        String email = new GetSharedPreference(this).getString(getResources().getString(R.string.shrdUserEmail));
        tvChannelOwner.setText(email);
    }

    public void selectChannelPicture(View view) {
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
                        filePath = getPath(CreatePlatformActivity.this, uri);
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


    public void onClickCreatePlatform(View view) {
        if (!isConnectingToInternet(CreatePlatformActivity.this)) {
            CommonUtilities.customToast(CreatePlatformActivity.this, getResources().getString(R.string.strNoInternet));
            // stop executing code by return
            return;

        } else {
            String selectedChannelType = null;
            String selectedPlatformType = null;

            if(rbPublic.isChecked()) {
                selectedChannelType = "Public";
            } else if(rbPrivate.isChecked()) {
                selectedChannelType = "Private";
            }

            if(rbEcommerce.isChecked()) {
                selectedPlatformType = "Ecommerce";

            } else if(rbChannel.isChecked()) {
                selectedPlatformType = "Channel";
            }

            ChannelModel channelModel = new ChannelModel(
                    txtChannelName.getText().toString().trim(),
                    tvChannelOwner.getText().toString().trim(),
                    txtChannelDesc.getText().toString().trim(),
                    selectedPlatformType,
                    selectedChannelType,
                    txtChannelPwd.getText().toString().trim(),
                    0,
                    filePath
            );

            try {
                if( new FormValidator().validateField(channelModel, CreatePlatformActivity.this)) {
                    // hide soft keyboard when it is open
                    View view1 = getCurrentFocus();
                    if (view1 != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }

                    long dateMillis = new Date().getTime();
                    String day = DateHelper.getDay(dateMillis);
                    String month = DateHelper.getMonth(dateMillis);
                    String year = DateHelper.getYear(dateMillis);
                    String startDate = year + "-" + month + "-" + day;

                    channelModel.setCreateDate(startDate);

                    new FileUploadAsyncTask(CreatePlatformActivity.this, channelModel, filePath, CommonVariables.POST_CHANNEL_IMAGE_SERVER_URL).execute();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
