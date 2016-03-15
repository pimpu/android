package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.adapter.ExpandableListItemAdapter;
import com.alchemistdigital.kissan.asynctask.GetOBPDetailsAtAdmin;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.Item;
import com.alchemistdigital.kissan.utilities.DateHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order_Detail extends AppCompatActivity {
    int id, userId;
    String referenceNo, utr, time;
    ExpandableListItemAdapter listAdapter;
    ExpandableListView expListView;
    TextView tv_ref, tv_utr;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.order_detail_toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        referenceNo = extras.getString("reference");
        utr = extras.getString("utr");
        userId = extras.getInt("userId");
        long date = Long.parseLong(extras.getString("creationTime"));
        time = DateHelper.convertToString(date);

        expListView = (ExpandableListView) findViewById(R.id.expandable_id_item);
        tv_ref = (TextView) findViewById(R.id.tv_id_order_details_reference);
        tv_utr = (TextView) findViewById(R.id.tv_id_order_details_utr);

        tv_ref.setText(referenceNo);
        tv_utr.setText(utr);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListItemAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

    }

    /**
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        DatabaseHelper dbhelper = new DatabaseHelper(Order_Detail.this);
        List<Item> itemsByRefno = dbhelper.getItemByRefno(referenceNo);

        for ( int i = 0 ; i < itemsByRefno.size() ; i++ ){

            listDataHeader.add(itemsByRefno.get(i).getItemName());
            List<String> lists = new ArrayList<String>();
            lists.add(""+itemsByRefno.get(i).getItemQuantity());
            lists.add(itemsByRefno.get(i).getItemPrice());
            lists.add(itemsByRefno.get(i).getItemTotalAmount());

            listDataChild.put(itemsByRefno.get(i).getItemName() , lists); // Header, Child data

        }
    }

    public void showSocietyDetails(View view) {
        DatabaseHelper dbhelper = new DatabaseHelper(Order_Detail.this);
        List<Enquiry> enquiryByUid_reference = dbhelper.getEnquiryByUid_Reference(userId, referenceNo);
        dbhelper.closeDB();

        // custom dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert_society_detail, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        TextView tv_societyName = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_society_name);
        TextView tv_societyContact = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_society_contact);
        TextView tv_societyEmail = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_society_email);
        TextView tv_societyAddress = (TextView) dialogView.findViewById(R.id.tv_id_inOrderDetails_society_address);
        ImageView closeDialog = (ImageView) dialogView.findViewById(R.id.closeSocietyDetailsAlert);

        tv_societyName.setText( enquiryByUid_reference.get(0).getEnquiry_society());
        tv_societyContact.setText( enquiryByUid_reference.get(0).getEnquiry_society_contact());
        tv_societyEmail.setText( enquiryByUid_reference.get(0).getEnquiry_society_email());
        tv_societyAddress.setText( enquiryByUid_reference.get(0).getEnquiry_society_address());


        final AlertDialog b = dialogBuilder.create();

        // if button is clicked, close the custom dialog
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        b.show();
    }

    public void showObpDetails(View view) {
        new GetOBPDetailsAtAdmin( Order_Detail.this,String.valueOf(userId) ).execute();
    }
}
