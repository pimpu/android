package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.asynctask.InsertEnquiryAsyncTask;
import com.alchemistdigital.kissan.model.Category;
import com.alchemistdigital.kissan.model.Enquiry;
import com.alchemistdigital.kissan.model.Offline;
import com.alchemistdigital.kissan.model.Product_Details;
import com.alchemistdigital.kissan.model.Product_Group;
import com.alchemistdigital.kissan.model.Society;
import com.alchemistdigital.kissan.model.Subcategory;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.alchemistdigital.kissan.utilities.offlineActionModeEnum;
import com.andexert.library.RippleView;

import java.util.ArrayList;
import java.util.Date;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.DateHelper.getDateToStoreInDb;
import static com.alchemistdigital.kissan.utilities.DateHelper.getRefStringDate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;

public class Create_Enquiry extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseHelper dbHelper;
    private EditText txt_ref_no, txt_product_qty;
    private String str_ref_no, str_product_qty;
    TextInputLayout refInputLayout, productQtyInputLayout;
    Spinner spinnerSociety, spinnerGroup, spinnerCategory, spinnerSubcategory, spinnerProduct;
    private ArrayList<Society> allSocieties;
    private ArrayAdapter<String> adapterSociety;
    private ArrayList<Product_Group> allGroup;
    private ArrayAdapter<Product_Group> adapterGroup;
    private ArrayList<Category> allCategory;
    private ArrayAdapter<Category> adapterCategory;
    private ArrayList<Subcategory> allSubcategory;
    private ArrayAdapter<Subcategory> adapterSubcategory;
    private ArrayList<Product_Details> allProductDetails;
    private ArrayAdapter<Product_Details> adapterProduct;
    String eId;
    int selectedSocietyIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DatabaseHelper(Create_Enquiry.this);
        int rowsCount = dbHelper.numberOfSocietyRowsByStatus();

        GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_Enquiry.this);
        String userTypePreference = getPreference.getUserTypePreference(getResources().getString(R.string.userType));

        // if society data not found then show unavailable view to screen.
        if (rowsCount <= 0 && userTypePreference.equals("obp")) {
            setContentView(R.layout.society_unavailable);
            Toolbar toolbar = (Toolbar) findViewById(R.id.create_enquiry_toolbar);
            setSupportActionBar(toolbar);
            return;
        }

        setContentView(R.layout.activity_create__enquiry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_enquiry_toolbar);
        setSupportActionBar(toolbar);

        // creating spinner from society table
        spinnerSociety = (Spinner) findViewById(R.id.spinner_id_societies);
        spinnerSociety.setOnItemSelectedListener(this);

        spinnerGroup = (Spinner) findViewById(R.id.spinner_id_group);
        spinnerGroup.setOnItemSelectedListener(this);

        spinnerCategory = (Spinner) findViewById(R.id.spinner_id_category);
        spinnerCategory.setOnItemSelectedListener(this);

        spinnerSubcategory = (Spinner) findViewById(R.id.spinner_id_subcategory);
        spinnerSubcategory.setOnItemSelectedListener(this);

        spinnerProduct = (Spinner) findViewById(R.id.spinner_id_product_name);
        spinnerProduct.setOnItemSelectedListener(this);

        // getting all rows of society
        allSocieties = dbHelper.getAllSocieties();
        // only get society names
        ArrayList<String> societyNames = new ArrayList<String>();
        for (int i = 0 ; i < allSocieties.size() ; i++ ) {
            societyNames.add(allSocieties.get(i).getSoc_name());
        }
        adapterSociety = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, societyNames );
        adapterSociety.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSociety.setAdapter(adapterSociety);
        adapterSociety.notifyDataSetChanged();


        // getting all rows of group
        allGroup = dbHelper.getAllGroup();
        // get only group name
        adapterGroup = new ArrayAdapter<Product_Group>(this, android.R.layout.simple_spinner_item, allGroup );
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapterGroup);
        adapterGroup.notifyDataSetChanged();


        // getting all rows of category filter by group id
        allCategory = dbHelper.getAllCategory(allGroup.get(0).getServerId());
        // get only category name
        adapterCategory = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, allCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
        adapterCategory.notifyDataSetChanged();

        // getting all rows of subcategory filter by group id and category id
        allSubcategory = dbHelper.getAllSubcategory(allGroup.get(0).getServerId(), allCategory.get(0).getServerId());
        // get only subcategory name
        adapterSubcategory = new ArrayAdapter<Subcategory>(this, android.R.layout.simple_spinner_item, allSubcategory);
        adapterSubcategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubcategory.setAdapter(adapterSubcategory);
        adapterSubcategory.notifyDataSetChanged();


        // getting all rows of product details filter by group id and category id and subcategory id
        allProductDetails = dbHelper.getAllProductdetails(allGroup.get(0).getServerId(),
                                                            allCategory.get(0).getServerId(),
                                                            allSubcategory.get(0).getServerId());
        // get only product details name
        adapterProduct = new ArrayAdapter<Product_Details>(this, android.R.layout.simple_spinner_item, allProductDetails);
        adapterProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();

        txt_ref_no = (EditText) findViewById(R.id.edittext_id_ref_no);
        txt_product_qty = (EditText) findViewById(R.id.edittext_id_product_quantity);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.getString("callingClass").equals("mainActivity")
                    || extras.getString("callingClass").equals("createSociety") ) {
                // this intent comes from main activity
                txt_ref_no.setText( getResources().getString(R.string.refString, getRefStringDate()) );
                eId = "0";
            }
            else if(extras.getString("callingClass").equals("newRelpy")) {
                // this intent comes from new reply with reference no
                txt_ref_no.setText( extras.getString("ref") );
                eId = String.valueOf(extras.getInt("enquiryId"));
            }
        }

        refInputLayout = (TextInputLayout) findViewById(R.id.id_input_layout_ref_no);
        productQtyInputLayout = (TextInputLayout) findViewById(R.id.id_input_layout_product_quantity);

        final RippleView rippleView = (RippleView) findViewById(R.id.btn_id_submit_enquiry);
        assert rippleView != null;
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_ref_no = txt_ref_no.getText().toString();
                str_product_qty = txt_product_qty.getText().toString();

                Boolean boolRef = isEmptyString(str_ref_no);
                Boolean boolProductQty = isEmptyString(str_product_qty);

                if (boolRef) {
                    refInputLayout.setErrorEnabled(false);
                } else {
                    refInputLayout.setErrorEnabled(true);
                    refInputLayout.setError("reference field is empty.");
                }

                if (boolProductQty) {
                    productQtyInputLayout.setErrorEnabled(false);
                } else {
                    productQtyInputLayout.setErrorEnabled(true);
                    productQtyInputLayout.setError("product quantity field is empty.");
                }

                if (boolRef && boolProductQty) {
                    int selectedSocietySpinnerPosition = spinnerSociety.getSelectedItemPosition();
                    int selectedSubcategorySpinnerPosition = spinnerSubcategory.getSelectedItemPosition();
                    int selectedProductSpinnerPosition = spinnerProduct.getSelectedItemPosition();

                    int selectedSocietyServerId = allSocieties.get(selectedSocietySpinnerPosition).getServerId();
                    int selectedSubcategoryServerId = allSubcategory.get(selectedSubcategorySpinnerPosition).getServerId();
                    int selectedProductServerId = allProductDetails.get(selectedProductSpinnerPosition).getServerId();

                    GetSharedPreferenceHelper getPreference = new GetSharedPreferenceHelper(Create_Enquiry.this);
                    int uId = getPreference.getUserIdPreference(getResources().getString(R.string.userId));
                    String strUID = String.valueOf(uId);
                    String userType = getPreference.getUserTypePreference(getResources().getString(R.string.userType));

                    // check if same enquiry present with this userid.
                    if ( dbHelper.checkEnquiryEntryPresent(uId,
                            selectedSocietyServerId,
                            selectedSubcategoryServerId,
                            selectedProductServerId,
                            str_ref_no) > 0) {
                        Toast.makeText(Create_Enquiry.this, "This enquiry is already exist.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    int gId;
                    int repToVal;
                    if (userType.equals("obp")) {
                        gId = 2;
                    } else {
                        gId = 1;
                    }

                    if ( eId.equals("0") ) {
                        repToVal = getPreference.getAdminUserId(getResources().getString(R.string.adminUserId));
                    } else {
                        repToVal = dbHelper.getUserIdByServerIdInEnquired(eId);
                    }

                    // Check if Internet present
                    if (!isConnectingToInternet(Create_Enquiry.this)) {

                        Enquiry enquiry = new Enquiry(0, getDateToStoreInDb(), str_ref_no, uId, gId,
                                repToVal, 0, selectedSocietyServerId, selectedSubcategoryServerId, selectedProductServerId,
                                str_product_qty,1);

                        long enquiryId = dbHelper.insertEnquiry(enquiry);

                        Offline offline = new Offline( dbHelper.TABLE_ENQUIRY,
                                (int) enquiryId,
                                offlineActionModeEnum.INSERT.toString(),
                                ""+new Date().getTime() );

                        dbHelper.insertOffline(offline);

                        if (!eId.equals("0")) {
                            int i = dbHelper.updateEnquiryReplied(eId,"1");

                            Offline offline1 = new Offline(dbHelper.TABLE_ENQUIRY,
                                    dbHelper.getEnquiryIdByEnquiryServerId(eId),
                                    offlineActionModeEnum.UPDATE.toString(),
                                    ""+new Date().getTime());
                            dbHelper.insertOffline(offline1);
                        }

                        onBackPressed();
                    } else {
                        new InsertEnquiryAsyncTask(
                                Create_Enquiry.this,
                                str_ref_no,
                                strUID,
                                gId,
                                repToVal,
                                selectedSocietyServerId,
                                selectedSubcategoryServerId,
                                selectedProductServerId,
                                str_product_qty,
                                userType,
                                eId ).execute();
                    }
                }
            }
        });

    }

    public void goToCreateSociety(View v) {
        Intent intent = new Intent(Create_Enquiry.this, Create_Society.class);
        Bundle bundle = new Bundle();
        bundle.putString("comesFrom","CreateEnquiry");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    int selectedGroupServerId = 0;
    int selectedCategoryServerId = 0;
    int selectedSubcategoryServerId = 0;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        String item = parent.getItemAtPosition(position).toString();

        if(spinner.getId() == R.id.spinner_id_societies){
            selectedSocietyIndex = position;
        }
        else if(spinner.getId() == R.id.spinner_id_group) {
            selectedGroupServerId = allGroup.get(position).getServerId();

            // rendered category spinner
            allCategory = dbHelper.getAllCategory(selectedGroupServerId);
            adapterCategory.clear();
            adapterCategory.addAll(allCategory);
            adapterCategory.notifyDataSetChanged();

            // get category server id
            selectedCategoryServerId = allCategory.get(0).getServerId();

            // rendered subcategory spinner
            allSubcategory = dbHelper.getAllSubcategory(selectedGroupServerId, selectedCategoryServerId);
            adapterSubcategory.clear();
            adapterSubcategory.addAll(allSubcategory);
            adapterSubcategory.notifyDataSetChanged();

            // get subcategory server id
            selectedSubcategoryServerId = allSubcategory.get(0).getServerId();

            allProductDetails = dbHelper.getAllProductdetails(selectedGroupServerId,
                    selectedCategoryServerId,
                    selectedSubcategoryServerId);
            adapterProduct.clear();
            adapterProduct.addAll(allProductDetails);
            adapterProduct.notifyDataSetChanged();
        }
        else if(spinner.getId() == R.id.spinner_id_category) {
            // get category server id
            selectedCategoryServerId = allCategory.get(position).getServerId();

            // rendered subcategory spinner
            allSubcategory = dbHelper.getAllSubcategory(selectedGroupServerId, selectedCategoryServerId);
            adapterSubcategory.clear();
            adapterSubcategory.addAll(allSubcategory);
            adapterSubcategory.notifyDataSetChanged();

            // get subcategory server id
            selectedSubcategoryServerId = allSubcategory.get(0).getServerId();

            allProductDetails = dbHelper.getAllProductdetails(selectedGroupServerId,
                    selectedCategoryServerId,
                    selectedSubcategoryServerId);
            adapterProduct.clear();
            adapterProduct.addAll(allProductDetails);
            adapterProduct.notifyDataSetChanged();

        }
        else if(spinner.getId() == R.id.spinner_id_subcategory) {
            // get subcategory server id
            selectedSubcategoryServerId = allSubcategory.get(position).getServerId();

            allProductDetails = dbHelper.getAllProductdetails(selectedGroupServerId,
                    selectedCategoryServerId,
                    selectedSubcategoryServerId);
            adapterProduct.clear();
            adapterProduct.addAll(allProductDetails);
            adapterProduct.notifyDataSetChanged();
        }
        else if(spinner.getId() == R.id.spinner_id_product_name) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Bundle extras = getIntent().getExtras();
        if(extras.getString("callingClass").equals("newRelpy")){
            startActivity(new Intent(Create_Enquiry.this,New_Reply.class));
            finish();
        }
        else if(extras.getString("callingClass").equals("mainActivity")){
            startActivity(new Intent(Create_Enquiry.this,View_Enquiry.class));
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.closeDB();
    }

    public void viewSocietyDetails(View view) {
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

        tv_societyName.setText(allSocieties.get(selectedSocietyIndex).getSoc_name());
        tv_societyContact.setText(allSocieties.get(selectedSocietyIndex).getSoc_contact());
        tv_societyEmail.setText(allSocieties.get(selectedSocietyIndex).getSoc_email());
        tv_societyAddress.setText(allSocieties.get(selectedSocietyIndex).getSoc_adrs());

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
}
