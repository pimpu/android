package com.alchemistdigital.kissan.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.utilities.RecyclerViewListener;

import java.util.List;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;

public class View_Society extends AppCompatActivity{

    private View displaySocietyView;
    private RecyclerView society_recyclerView;
    public RecyclerView.Adapter society_adapter;
    public static List<Society> data;
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__society);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_society_toolbar);
        setSupportActionBar(toolbar);

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
            new RecyclerViewListener.RecyclerItemClickListener(getApplicationContext(), new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
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


                                        // Check if Internet present
                                        if (!isConnectingToInternet(View_Society.this)) {
                                            // Internet Connection is not present
                                            Snackbar.make(displaySocietyView, "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                                                    .setAction("Retry", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            onCreate(null);
                                                        }
                                                    }).show();
                                            // stop executing code by return
                                            return;

                                        } else {

                                            new DeleteSocietyAsyncTask(View_Society.this, data.get(position).getId() ).execute();

                                            DatabaseHelper dbhelper = new DatabaseHelper(View_Society.this);
                                            dbhelper.deleteSociety(data.get(position).getId());
                                            dbhelper.closeDB();

                                            societyData.remove(position);
                                            society_adapter.notifyItemRemoved(position);

                                            DatabaseHelper dbHelper = new DatabaseHelper(View_Society.this);
                                            int len = dbHelper.numberOfSocietyRowsByStatus();
                                            dbHelper.closeDB();

                                            if(len <= 0) {
                                                emptyView.setVisibility(View.VISIBLE);
                                                society_recyclerView.setVisibility(View.GONE);
                                            }

                                        }


                                    }
                                }

                        );

                            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }

                            );

                            AlertDialog alertDialog = alertDialogBuilder.create();

                            alertDialog.show();

                        }
                    });
                }
            })
        );
    }

}
