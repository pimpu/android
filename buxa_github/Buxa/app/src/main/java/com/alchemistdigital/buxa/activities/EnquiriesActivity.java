package com.alchemistdigital.buxa.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.adapter.Enquiry_Adapter;
import com.alchemistdigital.buxa.asynctask.EnquiryAcceptAsyncTask;
import com.alchemistdigital.buxa.asynctask.EnquiryCancelAsyncTask;
import com.alchemistdigital.buxa.model.ShipmentConformationModel;
import com.alchemistdigital.buxa.utilities.CommonUtilities;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.ItemClickListener;
import com.alchemistdigital.buxa.utilities.WakeLocker;

import java.io.File;
import java.util.List;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;

public class EnquiriesActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView enquiry_RecyclerView;
    public RecyclerView.Adapter enquiry_Adapter;
    private static List<ShipmentConformationModel> data;
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiries);

        toolbarSetup();

        emptyView = findViewById(R.id.empty_enquiry_data);
        enquiry_RecyclerView = (RecyclerView)findViewById(R.id.enquiry_details_recycler);
        initEnquiryRecycler();

        if(!hasPermissions(this, CommonVariables.PERMISSIONS)){
            ActivityCompat.requestPermissions(this, CommonVariables.PERMISSIONS, CommonVariables.REQUEST_PERMISSION);
        }

        registerReceiver(AccpetCancelHandler, new IntentFilter("AcceptCancelIntent"));
    }

    private boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CommonVariables.REQUEST_PERMISSION:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission has been denied by user");
                    Toast.makeText(getApplicationContext(), "Permission require for registering with Buxa.",Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Permission has been granted by user");
                }
                break;
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

    public void initEnquiryRecycler() {
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
            enquiryRecycler(data);
        }
    }

    private void enquiryRecycler(List<ShipmentConformationModel> shipmentData) {
        // Inflating view layout
        enquiry_Adapter = new Enquiry_Adapter(this, shipmentData);
        enquiry_RecyclerView.setAdapter(enquiry_Adapter);
        enquiry_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTransportClick(int position) {

        Intent gotoTransDetailsIntent = new Intent(EnquiriesActivity.this, TransportationDetailsActivity.class);
        gotoTransDetailsIntent.putExtra("bookingId", data.get(position).getBookingId());
        gotoTransDetailsIntent.putExtra("shipmentArea", data.get(position).getShipArea());
        startActivity(gotoTransDetailsIntent);
    }

    @Override
    public void onCCClick(int position) {
        Intent gotoIntent = new Intent(EnquiriesActivity.this, CustomClrDetailsActivity.class);
        gotoIntent.putExtra("bookingId", data.get(position).getBookingId());
        gotoIntent.putExtra("shipmentArea", data.get(position).getShipArea());
        startActivity(gotoIntent);
    }

    @Override
    public void onFFClick(int position) {
        Intent gotoIntent = new Intent(EnquiriesActivity.this, FreightFwdDetailsActivity.class);
        gotoIntent.putExtra("bookingId", data.get(position).getBookingId());
        gotoIntent.putExtra("shipmentArea", data.get(position).getShipArea());
        startActivity(gotoIntent);
    }

    @Override
    public void onPdfViewClick(int position) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Buxa/" + data.get(position).getQuotaion());  // -> filename = maven.pdf
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
            new EnquiryAcceptAsyncTask(EnquiriesActivity.this, data.get(position).getBookingId() ).acceptAsyncTask();
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
            new EnquiryCancelAsyncTask(EnquiriesActivity.this,data.get(position).getBookingId()).cancelAsyncTask();
        }
    }

    BroadcastReceiver AccpetCancelHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonVariables.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            if (newMessage.equals("Accept") || newMessage.equals("Cancel")) {
                initEnquiryRecycler();
            }

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(AccpetCancelHandler);
        super.onDestroy();
    }
}

