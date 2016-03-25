package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.adapter.Enquiry_Adapter;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.utilities.RecyclerViewListener;

import java.util.List;

public class View_Enquiry extends AppCompatActivity {

    private RecyclerView enquiry_RecyclerView;
    public RecyclerView.Adapter enquiry_Adapter;
    public static List<Enquiry> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__enquiry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_enquiry_toolbar);
        setSupportActionBar(toolbar);

        View emptyView = findViewById(R.id.empty_enquiry_data);
        enquiry_RecyclerView = (RecyclerView)findViewById(R.id.enquiry_details_recycler);

        DatabaseHelper dbhelper = new DatabaseHelper(View_Enquiry.this);
        int len = dbhelper.numberOfEnquiryRowsByUidAndStatus();
        if(len <= 0){
            emptyView.setVisibility(View.VISIBLE);
            enquiry_RecyclerView.setVisibility(View.GONE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            enquiry_RecyclerView.setVisibility(View.VISIBLE);
            data = dbhelper.getEnquiryByUid();
            enquiryRecycler(data);
        }
        dbhelper.closeDB();

    }

    private void enquiryRecycler(List<Enquiry> enquiryByuid) {
        // Inflating view layout
        enquiry_Adapter = new Enquiry_Adapter(View_Enquiry.this, enquiryByuid );
        enquiry_RecyclerView.setAdapter(enquiry_Adapter);
        enquiry_RecyclerView.setLayoutManager(new LinearLayoutManager(View_Enquiry.this));

        enquiry_RecyclerView.addOnItemTouchListener(
            new RecyclerViewListener.RecyclerItemClickListener(getApplicationContext(), new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
//                    int enquiryId = data.get(position).getEnquiry_id();
                    Intent goToDetailEnquiry = new Intent(View_Enquiry.this,Enquiry_Detail.class);

                    Bundle extras = new Bundle();
                    extras.putInt("enq_id",data.get(position).getEnquiry_id());
                    extras.putString("reference", data.get(position).getEnquiry_reference());
                    extras.putString("creationTime",data.get(position).getCreted_at());
                    extras.putString("society",data.get(position).getEnquiry_society());
                    extras.putString("society_address",data.get(position).getEnquiry_society_address());
                    extras.putString("message",data.get(position).getEnquiry_message());
                    extras.putString("document",data.get(position).getEnquiry_document());
                    extras.putString("society_contact",data.get(position).getEnquiry_society_contact());
                    extras.putString("society_email",data.get(position).getEnquiry_society_email());

                    goToDetailEnquiry.putExtras(extras);
                    startActivity(goToDetailEnquiry);
                }
            })
        );

    }

}
