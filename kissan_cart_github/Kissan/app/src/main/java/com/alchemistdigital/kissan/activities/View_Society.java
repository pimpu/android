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
import com.alchemistdigital.kissan.adapter.Society_Adapter;
import com.alchemistdigital.kissan.asynctask.DeleteSocietyAsyncTask;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.RecyclerViewListener;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;

import java.util.Date;
import java.util.List;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

public class View_Society extends AppCompatActivity{

    private View displaySocietyView;
    public RecyclerView society_recyclerView;
    public RecyclerView.Adapter society_adapter;
    public static List<Society> data;
    public View emptyView;
    private FloatingActionButton fabCreateSociety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__society);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_society_toolbar);
        setSupportActionBar(toolbar);

        fabCreateSociety = (FloatingActionButton) findViewById(R.id.fab_create_society);
        fabCreateSociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(View_Society.this, Create_Society.class);
                Bundle bundle = new Bundle();
                bundle.putString("comesFrom","ViewSociety");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(View_Society.this);
        String who = getPreference.getUserTypePreference(getResources().getString(R.string.userType));
        // check user is admin or obp
        // on the bases of preference value.
        if( who.equals("obp") ) {
            fabCreateSociety.setVisibility(View.VISIBLE);
        }
        else {
            fabCreateSociety.setVisibility(View.GONE);
        }


        displaySocietyView = findViewById(R.id.id_displayScoietyView);

        emptyView = findViewById(R.id.empty_view_society_data);
        society_recyclerView = (RecyclerView)findViewById(R.id.society_details_recycler);

        DatabaseHelper dbhelper = new DatabaseHelper(View_Society.this);
        int len = dbhelper.numberOfSocietyRowsByStatus();

        if(len <= 0) {
            emptyView.setVisibility(View.VISIBLE);
            society_recyclerView.setVisibility(View.GONE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            society_recyclerView.setVisibility(View.VISIBLE);
            data = dbhelper.getAllSocieties();
            societyRecycler(data);
        }

    }

    private void societyRecycler(final List<Society> societyData) {
        // Inflating view layout
        society_adapter = new Society_Adapter(View_Society.this, societyData );
        society_recyclerView.setAdapter(society_adapter);
        society_recyclerView.setLayoutManager(new LinearLayoutManager(View_Society.this));

        society_recyclerView.addOnItemTouchListener(
            new RecyclerViewListener.RecyclerItemClickListener(
                    getApplicationContext(),
                    new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {

                // custom dialog
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(View_Society.this);
                LayoutInflater inflater = View_Society.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_alert_society_option, null);
                dialogBuilder.setView(dialogView);

                final AlertDialog b = dialogBuilder.create();
                b.show();

                TextView edit = (TextView) dialogView.findViewById(R.id.editSocietyDetail);
                TextView delete = (TextView) dialogView.findViewById(R.id.deleteSociety);

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();

                        Intent intent = new Intent(View_Society.this, Edit_Society_Details.class);
                        Bundle extras = new Bundle();
                        extras.putInt("societyId", data.get(position).getId());
                        extras.putInt("societyServerId", data.get(position).getServerId());
                        extras.putInt("societyStatus", data.get(position).getSoc_status());
                        intent.putExtras(extras);
                        startActivity(intent);
                        View_Society.this.finish();

                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        b.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(View_Society.this);
                        alertDialogBuilder.setMessage("Are you sure to delete this society?");

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();

                                Society societyObj = new Society();
                                // Check if Internet present
                                if (!isConnectingToInternet(View_Society.this)) {

                                    societyObj.setServerId(data.get(position).getServerId());
                                    societyObj.setId(data.get(position).getId());

                                    DatabaseHelper dbhelper = new DatabaseHelper(View_Society.this);

                                    // delete society
                                    dbhelper.deleteSociety(societyObj);

                                    // get number of available society to show
                                    // if any society not found to show
                                    // then view thw not found xml view.
                                    int len = dbhelper.numberOfSocietyRowsByStatus();
                                    if(len <= 0) {
                                        emptyView.setVisibility(View.VISIBLE);
                                        society_recyclerView.setVisibility(View.GONE);
                                    }

                                    // offline data insert
                                    Offline offline = new Offline( dbhelper.TABLE_SOCIETY,
                                            (int) data.get(position).getId(),
                                            offlineActionModeEnum.UPDATE.toString(),
                                            ""+new Date().getTime() );

                                    // it check whether data with same row id with update action in offline table.
                                    // if yes delete old one and create new entry in offline table.
                                    if( dbhelper.numberOfOfflineRowsByRowIdAndUpdate(
                                            data.get(position).getId() ) > 0 ) {
                                        dbhelper.deleteOfflineTableDataByRowIdAndUpdate(
                                                String.valueOf( data.get(position).getId() ));
                                    }
                                    dbhelper.insertOffline(offline);

                                    dbhelper.closeDB();

                                    // reomove deleted society from adapter
                                    societyData.remove(position);
                                    society_adapter.notifyItemRemoved(position);

                                } else {

                                    new DeleteSocietyAsyncTask(View_Society.this, data.get(position), position ).execute();

                                }
                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}
