package com.alchemistdigital.kissan.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.GetOnlineServerImageAsyncTask;
import com.alchemistdigital.kissan.utilities.CommonVariables;

import java.io.File;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

public class Enquiry_Detail extends AppCompatActivity {
    TextView tv_ref, tv_creationTime, tv_message, tv_society, tv_email, tv_contact, tv_address;
    String reference,creationTime,message,society,society_email,society_contact,society_address,
            document;
    ImageView imageviewSlip;
    View enquiryDetailView;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry__detail);

        enquiryDetailView = findViewById(R.id.id_enquiry_details_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.enquiry_details_toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("enq_id");
        reference = extras.getString("reference");
        creationTime = extras.getString("creationTime");
        message = extras.getString("message");
        society = extras.getString("society");
        society_email = extras.getString("society_email");
        society_contact = extras.getString("society_contact");
        society_address = extras.getString("society_address");
        document = extras.getString("document");

        init();

        setText();

    }

    private void init() {
        tv_ref = (TextView) findViewById(R.id.tv_id_enquiry_details_reference);
        tv_creationTime = (TextView) findViewById(R.id.tv_id_date);
        tv_message = (TextView) findViewById(R.id.tv_id_message);
        tv_society = (TextView) findViewById(R.id.tv_id_society_name);
        tv_email = (TextView) findViewById(R.id.tv_id_society_email);
        tv_contact = (TextView) findViewById(R.id.tv_id_society_contact);
        tv_address = (TextView) findViewById(R.id.tv_id_society_address);
        imageviewSlip = (ImageView) findViewById(R.id.imageview_id_slip);
    }

    private void setText() {
        tv_ref.setText(reference);
        tv_creationTime.setText(creationTime);
        tv_message.setText(message);
        tv_society.setText(society);
        tv_email.setText(society_email);
        tv_contact.setText(society_contact);
        tv_address.setText(society_address);

        DatabaseHelper dbHelper = new DatabaseHelper(Enquiry_Detail.this);

        File fileExist = new File(CommonVariables.SCAN_FILE_PATH +"/"+document );
        if( dbHelper.numberOfOfflineRowsByid(id) > 0
                || !isConnectingToInternet(Enquiry_Detail.this)
                || fileExist.exists() ) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(
                                    CommonVariables.SCAN_FILE_PATH +"/"+document,
                                    options);
            imageviewSlip.setImageBitmap(bitmap);

        } else {
            new GetOnlineServerImageAsyncTask(Enquiry_Detail.this,imageviewSlip)
                        .execute(CommonVariables.File_DOWNLOAD_URL + document);
        }

        dbHelper.closeDB();
    }
}
