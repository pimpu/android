package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.adapter.ItemsListAdapter;
import com.alchemistdigital.kissan.asynctask.InsertOrderAsyncTask;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.Item;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Order;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;
import com.andexert.library.RippleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.DateHelper.getDateToStoreInDb;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;

public class Create_View_Orders extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText txt_utr;
    String  str_enquiry_refno,str_utr;
    TextInputLayout utrInputLayout;
    Spinner spinnerRefNo;
    private List<Enquiry> getReferences;
    private ArrayAdapter<String> adapterRefNo;
    ListView itemListView;
    List<Item> itemByRefno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__view__orders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_view_order_toolbar);
        setSupportActionBar(toolbar);
//        actionBarSetup();

        View emptyView = findViewById(R.id.empty_ref_no);
        View orderView = findViewById(R.id.orderView);

        DatabaseHelper dbHelper = new DatabaseHelper(Create_View_Orders.this);
        getReferences = dbHelper.getReferenceNumberForOrder();

        if(getReferences.size() <= 0){
            emptyView.setVisibility(View.VISIBLE);
            orderView.setVisibility(View.GONE);
            return;
        }
        else {
            emptyView.setVisibility(View.GONE);
        }

        itemListView = (ListView) findViewById(R.id.itemListView);

        txt_utr = (EditText) findViewById(R.id.edittext_id_utr);
        utrInputLayout = (TextInputLayout) findViewById(R.id.id_input_layout_utr);


        spinnerRefNo = (Spinner) findViewById(R.id.spinner_id_ref_number);
        spinnerRefNo.setOnItemSelectedListener(this);

        // getting all rows of society
        if(getReferences.size() > 0){
            str_enquiry_refno = getReferences.get(0).getEnquiry_reference();
        }

        // only get society names
        ArrayList<String> enquiryRefeNo = new ArrayList<String>();
        for (int i = 0 ; i < getReferences.size() ; i++ ){
            enquiryRefeNo.add(getReferences.get(i).getEnquiry_reference());
        }

        // make reference spinner
        adapterRefNo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, enquiryRefeNo );
        adapterRefNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRefNo.setAdapter(adapterRefNo);
        adapterRefNo.notifyDataSetChanged();

        // intent come from main activity with 0 reference number.
        // And also come from Create_Item activity with Database reference number.
        // set reference number to spinner which intent is come from Create_Item activity.
        String referenceNo = getIntent().getExtras().getString("referenceNo");
        if( !referenceNo.equals("0") ) {
            spinnerRefNo.setSelection(adapterRefNo.getPosition(referenceNo));
            txt_utr.setText(getIntent().getExtras().getString("UTRNo"));
        }

        // make item list view
        makeItemListView(str_enquiry_refno);

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_submit_order);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                DatabaseHelper dbHelper = new DatabaseHelper(Create_View_Orders.this);

                // get item of selected reference no
                int itemsCount = dbHelper.numberOfItemRowsByRefNo(str_enquiry_refno);

                str_utr         = txt_utr.getText().toString();
                Boolean boolUtr = isEmptyString(str_utr);
                if (boolUtr) {

                    utrInputLayout.setErrorEnabled(false);
                    // Check if Internet present

                    if( itemsCount <= 0) {
                        Toast.makeText(Create_View_Orders.this,getResources().getString(R.string.emptyItemErrorMsg),Toast.LENGTH_LONG).show();
                    } else if ( !isConnectingToInternet(Create_View_Orders.this) ) {

                        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_View_Orders.this);
                        int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));

                        Order order = new Order(uId, str_enquiry_refno,
                                str_utr, getDateToStoreInDb(), 1);

                        DatabaseHelper dbhelper = new DatabaseHelper(Create_View_Orders.this);
                        long orderId = dbhelper.insertOrder(order);

                        Offline offline = new Offline( dbHelper.TABLE_ORDER,
                                (int) orderId,
                                offlineActionModeEnum.INSERT.toString(),
                                ""+new Date().getTime() );
                        dbHelper.insertOffline(offline);

                        dbhelper.closeDB();

                        startActivity(new Intent(Create_View_Orders.this, View_Orders.class));
                        finish();

                    }
                    else {

                        JSONArray jsonArr = new JSONArray();
                        for (Item pn : itemByRefno ) {
                            JSONObject pnObj = new JSONObject();
                            try {
                                pnObj.put("itemName", pn.getItemName());
                                pnObj.put("itemQuantity", pn.getItemQuantity());
                                pnObj.put("itemPrice", pn.getItemPrice());
                                pnObj.put("itemTotalAmount", pn.getItemTotalAmount());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArr.put(pnObj);
                        }
                        System.out.println(str_utr);
                        System.out.println(str_enquiry_refno);
                        System.out.println(jsonArr);

                        // get userId from shared preference.
                        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_View_Orders.this);
                        int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
                        new InsertOrderAsyncTask(Create_View_Orders.this,
                                                    str_enquiry_refno,
                                                    str_utr,
                                                    String.valueOf(jsonArr),
                                                    String.valueOf(uId)
                                                    ).execute();

                    }
                }
                else {
                    utrInputLayout.setErrorEnabled(true);
                    utrInputLayout.setError("UTR field is empty.");
                }
                dbHelper.closeDB();
            }
        });

        dbHelper.closeDB();
    }

    public void makeItemListView(String refNo) {
        View viewById = findViewById(R.id.relativelayout_id_itemView);
        DatabaseHelper dbHelper = new DatabaseHelper(Create_View_Orders.this);

        // get item of selected reference no
        int itemsCount = dbHelper.numberOfItemRowsByRefNo(refNo);

        if(itemsCount > 0) {
            viewById.setVisibility(View.VISIBLE);
            itemByRefno = dbHelper.getItemByRefno(str_enquiry_refno);
            itemListView.setAdapter(new ItemsListAdapter(Create_View_Orders.this, itemByRefno));
        }
        else {
            viewById.setVisibility(View.GONE);
        }
        dbHelper.closeDB();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        String item = parent.getItemAtPosition(position).toString();
        if(spinner.getId() == R.id.spinner_id_ref_number){
//            System.out.println("Selected from reference spinner: "+item);
            str_enquiry_refno = item;
            makeItemListView(str_enquiry_refno);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Sets the Action Bar for new Android versions.
     */
    /*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar ab = getActionBar();
            ab.setTitle("My Title");
            ab.setSubtitle("sub-title");
        }
    }*/

    public void createNewItem(View view) {
        if( txt_utr.getText().toString().isEmpty() ){
            Toast.makeText(Create_View_Orders.this,"UTR filed is empty",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(Create_View_Orders.this,Create_Item.class);
        Bundle extra = new Bundle();
        extra.putString("refNo",str_enquiry_refno);
        extra.putString("utrNo",txt_utr.getText().toString());
        intent.putExtras(extra);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Create_View_Orders.this, View_Orders.class));
        finish();
    }
}
