package com.alchemistdigital.kissan.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.adapter.Vendor_Adapter;
import com.alchemistdigital.kissan.asynctask.DeleteVendorAsyncTask;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Vendor;
import com.alchemistdigital.kissan.utilities.RecyclerViewListener;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;

import java.util.Date;
import java.util.List;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

public class View_Vendor extends AppCompatActivity {

    private FloatingActionButton fabCreateVendor;
    public View emptyView;
    public RecyclerView vendor_RecyclerView;
    public RecyclerView.Adapter vendorAdapter;
    public static List<Vendor> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__vendor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_vendor_toolbar);
        setSupportActionBar(toolbar);

        fabCreateVendor = (FloatingActionButton) findViewById(R.id.fab_create_vendors);
        fabCreateVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Vendor.this, Create_Vendor.class));
                finish();
            }
        });

        emptyView = findViewById(R.id.empty_vendor_data);
        vendor_RecyclerView = (RecyclerView) findViewById(R.id.vendor_details_recycler);
        initVendorRecycler();
    }

    private void initVendorRecycler() {
        DatabaseHelper dbhelper = new DatabaseHelper(View_Vendor.this);
        int len = dbhelper.isAnyVendorPresent();
        if (len <= 0) {
            emptyView.setVisibility(View.VISIBLE);
            vendor_RecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            vendor_RecyclerView.setVisibility(View.VISIBLE);
            data = dbhelper.getAllVendors();
            vendorRecycler(data);
        }
        dbhelper.closeDB();
    }

    private void vendorRecycler(final List<Vendor> vendorAllData) {
        vendorAdapter = new Vendor_Adapter(View_Vendor.this, vendorAllData);
        vendor_RecyclerView.setAdapter(vendorAdapter);
        vendor_RecyclerView.setLayoutManager(new LinearLayoutManager(View_Vendor.this));

        vendor_RecyclerView.addOnItemTouchListener(
                new RecyclerViewListener.RecyclerItemClickListener(getApplicationContext(), new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        // custom dialog
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(View_Vendor.this);
                        LayoutInflater inflater = View_Vendor.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.custom_alert_vendor_option, null);
                        dialogBuilder.setView(dialogView);

                        final AlertDialog b = dialogBuilder.create();
                        b.show();

                        TextView viewDetails = (TextView) dialogView.findViewById(R.id.viewVendorDetail);
                        TextView editDetails = (TextView) dialogView.findViewById(R.id.editVendorDetail);
                        TextView delete = (TextView) dialogView.findViewById(R.id.deleteVendor);

                        viewDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                b.dismiss();

                                Intent intent = new Intent(View_Vendor.this, View_Vendor_Details.class);
                                intent.putExtra("Vendor_Entity", data.get(position));
                                startActivity(intent);

                            }
                        });

                        editDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                b.dismiss();

                                Intent intent = new Intent(View_Vendor.this, Edit_Vendor_Details.class);
                                intent.putExtra("Vendor_Entity",data.get(position));
                                startActivity(intent);
                                View_Vendor.this.finish();

                            }
                        });

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                b.dismiss();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(View_Vendor.this);
                                alertDialogBuilder.setMessage("Are you sure to delete this vendor ?");

                                alertDialogBuilder.setPositiveButton(
                                        "yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int arg1) {
                                                dialog.dismiss();

                                                // Check if Internet present
                                                if (!isConnectingToInternet(View_Vendor.this)) {

                                                    DatabaseHelper dbhelper = new DatabaseHelper(View_Vendor.this);

                                                    // delete obp
                                                    dbhelper.deleteVendor(vendorAllData.get(position));

                                                    // get number of available vendor to show
                                                    // if any vendor not found to show
                                                    // then view thw not found xml view.
                                                    int len = dbhelper.isAnyVendorPresent();
                                                    if (len <= 0) {
                                                        emptyView.setVisibility(View.VISIBLE);
                                                        vendor_RecyclerView.setVisibility(View.GONE);
                                                    }

                                                    // offline data insert
                                                    Offline offline = new Offline(dbhelper.TABLE_VENDORS,
                                                            data.get(position).getId(),
                                                            offlineActionModeEnum.UPDATE.toString(),
                                                            "" + new Date().getTime());

                                                    // it check whether data with same row id with update action in offline table.
                                                    // if yes delete old one and create new entry in offline table.
                                                    if (dbhelper.numberOfOfflineRowsByRowIdAndUpdate(
                                                            data.get(position).getId()) > 0) {
                                                        dbhelper.deleteOfflineTableDataByRowIdAndUpdate(
                                                                String.valueOf(data.get(position).getId()));
                                                    }
                                                    dbhelper.insertOffline(offline);

                                                    dbhelper.closeDB();

                                                    // reomove deleted obp from adapter
                                                    vendorAllData.remove(position);
                                                    vendorAdapter.notifyItemRemoved(position);

                                                } else {
                                                    new DeleteVendorAsyncTask(View_Vendor.this, data.get(position), position ).execute();
                                                }

                                            }
                                        });

                                alertDialogBuilder.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                            }
                        });
                    }
                })
        );
    }

}
