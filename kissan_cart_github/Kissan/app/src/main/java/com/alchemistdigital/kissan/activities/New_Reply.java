package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.adapter.New_Reply_Enquiry_Adapter;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.utilities.RecyclerViewListener;

import java.util.List;

public class New_Reply extends AppCompatActivity {

    private RecyclerView enquiry_RecyclerView;
    public RecyclerView.Adapter enquiry_Adapter;
    public static List<Enquiry> data;
    TextView btn_view,btn_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__reply);

        Toolbar toolbar = (Toolbar) findViewById(R.id.new_reply_toolbar);
        setSupportActionBar(toolbar);

        View emptyView = findViewById(R.id.empty_reply_data);
        enquiry_RecyclerView = (RecyclerView)findViewById(R.id.new_reply_recycler);

        DatabaseHelper dbhelper = new DatabaseHelper(New_Reply.this);

        int len = dbhelper.numberOfEnquiryRowsByReplyto();
        if(len <= 0){
            emptyView.setVisibility(View.VISIBLE);
            enquiry_RecyclerView.setVisibility(View.GONE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            enquiry_RecyclerView.setVisibility(View.VISIBLE);
            data = dbhelper.getEnquiryByReplytoAndReplied();
            enquiryRecycler(data);
        }
        dbhelper.closeDB();
    }

    private void enquiryRecycler(List<Enquiry> enquiryData) {
        // Inflating view layout
        enquiry_Adapter = new New_Reply_Enquiry_Adapter(New_Reply.this, enquiryData );
        enquiry_RecyclerView.setAdapter(enquiry_Adapter);
        enquiry_RecyclerView.setLayoutManager(new LinearLayoutManager(New_Reply.this));

        enquiry_RecyclerView.addOnItemTouchListener(
            new RecyclerViewListener.RecyclerItemClickListener(getApplicationContext(), new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                    btn_view = (TextView) findViewById(R.id.tv_id_view_enquiry);
                    btn_reply = (TextView) findViewById(R.id.tv_id_new_reply);

                    btn_view.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent goToDetailEnquiry = new Intent(New_Reply.this,Enquiry_Detail.class);

                            Bundle extras = new Bundle();
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
                    });

                    btn_reply.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(New_Reply.this, Create_Enquiry.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("callingClass","newRelpy");
                            bundle.putString("ref",data.get(position).getEnquiry_reference());
                            bundle.putInt("enquiryId", data.get(position).getEnquiry_id());
                            intent.putExtras(bundle);
                            startActivity(intent);

                            finish();
                        }
                    });
                }
            })
        );
    }
}
