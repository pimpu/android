package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.adapter.Order_Adapter;
import com.alchemistdigital.kissan.model.Order;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.RecyclerViewListener;

import java.util.List;

public class View_Orders extends AppCompatActivity {

    private RecyclerView order_RecyclerView;
    public RecyclerView.Adapter order_Adapter;
    public static List<Order> data;
    private FloatingActionButton fabCreateOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__orders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_order_toolbar);
        setSupportActionBar(toolbar);

        fabCreateOrder = (FloatingActionButton) findViewById(R.id.fab_create_order);
        fabCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderIntent = new Intent(View_Orders.this, Create_View_Orders.class);
                Bundle bundle = new Bundle();
                bundle.putString("referenceNo","0");
                orderIntent.putExtras(bundle);
                startActivity(orderIntent);
            }
        });

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(View_Orders.this);
        String who = getPreference.getUserTypePreference(getResources().getString(R.string.userType));
        // check user is admin or obp
        // on the bases of preference value.
        if( who.equals("obp") ){
            fabCreateOrder.setVisibility(View.VISIBLE);
        }
        else {
            fabCreateOrder.setVisibility(View.GONE);
        }

        View emptyView = findViewById(R.id.empty_order_data);
        order_RecyclerView = (RecyclerView)findViewById(R.id.order_details_recycler);

        DatabaseHelper dbhelper = new DatabaseHelper(View_Orders.this);
        int len = dbhelper.numberOfOrderRows();
        if(len <= 0){
            emptyView.setVisibility(View.VISIBLE);
            order_RecyclerView.setVisibility(View.GONE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            order_RecyclerView.setVisibility(View.VISIBLE);
            data = dbhelper.getAllOrder();
            orderRecycler(data);
        }
        dbhelper.closeDB();

    }

    private void orderRecycler(List<Order> ordersData) {
        // Inflating view layout
        order_Adapter = new Order_Adapter(View_Orders.this, ordersData );
        order_RecyclerView.setAdapter(order_Adapter);
        order_RecyclerView.setLayoutManager(new LinearLayoutManager(View_Orders.this));

        order_RecyclerView.addOnItemTouchListener(
                new RecyclerViewListener.RecyclerItemClickListener(getApplicationContext(), new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
//                    int enquiryId = data.get(position).getEnquiry_id();
                        Intent goToDetailOrder = new Intent(View_Orders.this,Order_Detail.class);

                        Bundle extras = new Bundle();
                        extras.putInt("id", data.get(position).getOrder_id());
                        extras.putString("reference", data.get(position).getOrder_reference());
                        extras.putString("utr", data.get(position).getOrder_utr());
                        extras.putInt("userId", data.get(position).getUserId());
                        extras.putString("creationTime", data.get(position).getOrder_creted_at());

                        goToDetailOrder.putExtras(extras);
                        startActivity(goToDetailOrder);
                    }
                })
        );
    }
}
