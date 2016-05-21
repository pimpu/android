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
import com.alchemistdigital.kissan.adapter.OBP_Adapter;
import com.alchemistdigital.kissan.asynctask.DeleteOBPAsyncTask;
import com.alchemistdigital.kissan.model.OBP;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.utilities.RecyclerViewListener;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;

import java.util.Date;
import java.util.List;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

public class View_Obp extends AppCompatActivity {

    private FloatingActionButton fabCreateOBP;
    public RecyclerView obp_RecyclerView;
    public RecyclerView.Adapter obp_Adapter;
    public static List<OBP> data;
    public View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__obp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_obp_toolbar);
        setSupportActionBar(toolbar);

        fabCreateOBP = (FloatingActionButton) findViewById(R.id.fab_create_obp);
        fabCreateOBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Obp.this, Create_OBP.class));
                finish();
            }
        });

        emptyView = findViewById(R.id.empty_obp_data);
        obp_RecyclerView = (RecyclerView) findViewById(R.id.obp_details_recycler);
        initOBPRecycler();

    }

    private void initOBPRecycler() {
        DatabaseHelper dbhelper = new DatabaseHelper(View_Obp.this);
        int len = dbhelper.isAnyOBPPresent();
        if (len <= 0) {
            emptyView.setVisibility(View.VISIBLE);
            obp_RecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            obp_RecyclerView.setVisibility(View.VISIBLE);
            data = dbhelper.getAllOBPExcludeAdmin();
            obpRecycler(data);
        }
        dbhelper.closeDB();
    }

    private void obpRecycler(final List<OBP> obpAllData) {
        // Inflating view layout
        obp_Adapter = new OBP_Adapter(View_Obp.this, obpAllData);
        obp_RecyclerView.setAdapter(obp_Adapter);
        obp_RecyclerView.setLayoutManager(new LinearLayoutManager(View_Obp.this));

        obp_RecyclerView.addOnItemTouchListener(
            new RecyclerViewListener.RecyclerItemClickListener(getApplicationContext(), new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                    // custom dialog
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(View_Obp.this);
                    LayoutInflater inflater = View_Obp.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.custom_alert_obp_option, null);
                    dialogBuilder.setView(dialogView);

                    final AlertDialog b = dialogBuilder.create();
                    b.show();

                    TextView viewDetails = (TextView) dialogView.findViewById(R.id.viewOBPDetail);
                    TextView delete = (TextView) dialogView.findViewById(R.id.deleteOBP);

                    viewDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            b.dismiss();

                            Intent intent = new Intent(View_Obp.this, View_Obp_Details.class);
                            intent.putExtra("OBP_Entity", data.get(position));
                            startActivity(intent);

                        }
                    });

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            b.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(View_Obp.this);
                            alertDialogBuilder.setMessage("Are you sure to delete this society?");

                            alertDialogBuilder.setPositiveButton(
                                    "yes",
                                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();

                                    OBP obpObj = new OBP();
                                    // Check if Internet present
                                    if (!isConnectingToInternet(View_Obp.this)) {

                                        obpObj.setUserID_serverId(data.get(position).getUserID_serverId());
                                        obpObj.setObp_id(data.get(position).getObp_id());

                                        DatabaseHelper dbhelper = new DatabaseHelper(View_Obp.this);

                                        // delete obp
                                        dbhelper.deleteObp(obpObj);

                                        // get number of available obp to show
                                        // if any obp not found to show
                                        // then view thw not found xml view.
                                        int len = dbhelper.isAnyOBPPresent();
                                        if (len <= 0) {
                                            emptyView.setVisibility(View.VISIBLE);
                                            obp_RecyclerView.setVisibility(View.GONE);
                                        }

                                        // offline data insert
                                        Offline offline = new Offline(dbhelper.TABLE_OBP,
                                                (int) data.get(position).getObp_id(),
                                                offlineActionModeEnum.UPDATE.toString(),
                                                "" + new Date().getTime());

                                        // it check whether data with same row id with update action in offline table.
                                        // if yes delete old one and create new entry in offline table.
                                        if (dbhelper.numberOfOfflineRowsByRowIdAndUpdate(
                                                data.get(position).getObp_id()) > 0) {
                                            dbhelper.deleteOfflineTableDataByRowIdAndUpdate(
                                                    String.valueOf(data.get(position).getObp_id()));
                                        }
                                        dbhelper.insertOffline(offline);

                                        dbhelper.closeDB();

                                        // reomove deleted obp from adapter
                                        obpAllData.remove(position);
                                        obp_Adapter.notifyItemRemoved(position);

                                    } else {

                                        new DeleteOBPAsyncTask(View_Obp.this, data.get(position), position).execute();

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
