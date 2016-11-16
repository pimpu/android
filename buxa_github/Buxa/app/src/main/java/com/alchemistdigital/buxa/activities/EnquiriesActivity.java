package com.alchemistdigital.buxa.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.adapter.Enquiry_Adapter;
import com.alchemistdigital.buxa.asynctask.DownloadQuotationAsyncTask;
import com.alchemistdigital.buxa.asynctask.EnquiryAcceptAsyncTask;
import com.alchemistdigital.buxa.asynctask.EnquiryCancelAsyncTask;
import com.alchemistdigital.buxa.asynctask.InsertCustomClearanceAsyncTask;
import com.alchemistdigital.buxa.asynctask.InsertTransportationAsyncTask;
import com.alchemistdigital.buxa.model.ShipmentConformationModel;
import com.alchemistdigital.buxa.utilities.ItemClickListener;

import java.io.File;
import java.util.List;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;

public class EnquiriesActivity extends AppCompatActivity implements ItemClickListener {
    private static final String TAG = "PERMISSION: ";
    private RecyclerView enquiry_RecyclerView;
    public RecyclerView.Adapter enquiry_Adapter;
    private static List<ShipmentConformationModel> data;
    View emptyView;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiries);

        toolbarSetup();

        emptyView = findViewById(R.id.empty_enquiry_data);
        enquiry_RecyclerView = (RecyclerView)findViewById(R.id.enquiry_details_recycler);
        initEnquiryRecycler();

        int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the SD-CARD is required for this app to Download PDF.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");

                } else {

                    Log.i(TAG, "Permission has been granted by user");

                }
                return;
            }
        }
    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_enquiry);
        setSupportActionBar(toolbar);

        // set back button on toolbar
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        // set click listener on back button of toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("My Enquiries");
    }

    private void initEnquiryRecycler() {
        DatabaseClass dbhelper = new DatabaseClass(this);

        int len = dbhelper.numberOfEnquiryRowsByStatus();
        if(len <= 0){
            emptyView.setVisibility(View.VISIBLE);
            enquiry_RecyclerView.setVisibility(View.GONE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            enquiry_RecyclerView.setVisibility(View.VISIBLE);
            data = dbhelper.getShipmentConformationData();
            enquiryRecycler();
        }
    }

    private void enquiryRecycler() {
        // Inflating view layout
        enquiry_Adapter = new Enquiry_Adapter(this, data );
        enquiry_RecyclerView.setAdapter(enquiry_Adapter);
        enquiry_RecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTransportClick(int position) {
//        new DownloadQuotationAsyncTask().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");

        Intent gotoTransDetailsIntent = new Intent(EnquiriesActivity.this, TransportationDetailsActivity.class);
        gotoTransDetailsIntent.putExtra("bookingId", data.get(position).getBookingId());
        startActivity(gotoTransDetailsIntent);
    }

    @Override
    public void onCCClick(int position) {
        Intent gotoIntent = new Intent(EnquiriesActivity.this, CustomClrDetailsActivity.class);
        gotoIntent.putExtra("bookingId", data.get(position).getBookingId());
        startActivity(gotoIntent);
    }

    @Override
    public void onFFClick(int position) {
        Intent gotoIntent = new Intent(EnquiriesActivity.this, FreightFwdDetailsActivity.class);
        gotoIntent.putExtra("bookingId", data.get(position).getBookingId());
        startActivity(gotoIntent);
    }

    @Override
    public void onPdfViewClick(int position) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Buxa/" + "maven.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(EnquiriesActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAcceptwClick(int position) {
        // get quotation of transportation from server
        // Check if Internet present
        if (!isConnectingToInternet(EnquiriesActivity.this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection),Toast.LENGTH_LONG).show();
            // stop executing code by return
            return;
        } else {
            EnquiryAcceptAsyncTask.acceptAsyncTask(
                    EnquiriesActivity.this,
                    data.get(position).getBookingId());
        }
    }

    @Override
    public void onCancelClick(int position) {
        // get quotation of transportation from server
        // Check if Internet present
        if (!isConnectingToInternet(EnquiriesActivity.this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.strNoConnection),Toast.LENGTH_LONG).show();
            // stop executing code by return
            return;
        } else {
            EnquiryCancelAsyncTask.cancelAsyncTask(
                    EnquiriesActivity.this,
                    data.get(position).getBookingId());
        }
    }
}

