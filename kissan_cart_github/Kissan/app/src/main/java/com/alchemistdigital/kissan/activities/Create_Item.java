package com.alchemistdigital.kissan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.kissan.DBHelper.DatabaseHelper;
import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Item;
import com.andexert.library.RippleView;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;

public class Create_Item extends AppCompatActivity {
    TextView tv_ref;
    private EditText txt_item_name,txt_item_qty,txt_item_price,txt_item_total_amt;
    private String  str_item_name,str_item_qty,str_item_price,str_item_total_amt;
    TextInputLayout item_nameInputLayout,item_qtyInputLayout,item_priceInputLayuot,item_total_amtInputLayout;
    String refNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_order_toolbar);
        setSupportActionBar(toolbar);

        refNo = getIntent().getExtras().getString("refNo");

        tv_ref              = (TextView) findViewById(R.id.tv_id_InOrder_enquiry_reference);
        tv_ref.setText(refNo);

        txt_item_name       = (EditText) findViewById(R.id.edittext_id__item_name);
        txt_item_qty        = (EditText) findViewById(R.id.edittext_id__item_qty);
        txt_item_price      = (EditText) findViewById(R.id.edittext_id__item_price);
        txt_item_total_amt  = (EditText) findViewById(R.id.edittext_id__item_total_amt);


        item_nameInputLayout        = (TextInputLayout) findViewById(R.id.id_input_layout__item_name);
        item_qtyInputLayout         = (TextInputLayout) findViewById(R.id.id_input_layout__item_qty);
        item_priceInputLayuot       = (TextInputLayout) findViewById(R.id.id_input_layout__item_price);
        item_total_amtInputLayout   = (TextInputLayout) findViewById(R.id.id_input_layout__item_total_amt);



        txt_item_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                str_item_qty        = txt_item_qty.getText().toString();
                str_item_price      = txt_item_price.getText().toString();
                Boolean boolItemQty     = isEmptyString(str_item_qty);
                Boolean boolItemPrice   = isEmptyString(str_item_price);

                if ( boolItemQty && boolItemPrice ) {
                    int total = Integer.parseInt(str_item_qty) * Integer.parseInt(str_item_price);
                    txt_item_total_amt.setText(""+total);
                }
                else {
                    txt_item_total_amt.setText("0");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        txt_item_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                str_item_qty        = txt_item_qty.getText().toString();
                str_item_price      = txt_item_price.getText().toString();
                Boolean boolItemQty     = isEmptyString(str_item_qty);
                Boolean boolItemPrice   = isEmptyString(str_item_price);

                if ( boolItemQty && boolItemPrice ) {
                    int total = Integer.parseInt(str_item_qty) * Integer.parseInt(str_item_price);
                    txt_item_total_amt.setText(""+total);
                }
                else {
                    txt_item_total_amt.setText("0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_create_item);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_item_name       = txt_item_name.getText().toString();
                str_item_qty        = txt_item_qty.getText().toString();
                str_item_price      = txt_item_price.getText().toString();
                str_item_total_amt  = txt_item_total_amt.getText().toString();

                Boolean boolItemName    = isEmptyString(str_item_name);
                Boolean boolItemQty     = isEmptyString(str_item_qty);
                Boolean boolItemPrice   = isEmptyString(str_item_price);

                if (boolItemName) {
                    item_nameInputLayout.setErrorEnabled(false);
                } else {
                    item_nameInputLayout.setErrorEnabled(true);
                    item_nameInputLayout.setError("item name field is empty.");
                }

                if (boolItemQty) {
                    item_qtyInputLayout.setErrorEnabled(false);
                } else {
                    item_qtyInputLayout.setErrorEnabled(true);
                    item_qtyInputLayout.setError("quantity field is empty.");
                }

                if (boolItemPrice) {
                    item_priceInputLayuot.setErrorEnabled(false);
                } else {
                    item_priceInputLayuot.setErrorEnabled(true);
                    item_priceInputLayuot.setError("price field is empty.");
                }

                if ( !str_item_total_amt.equals("0") ) {
                    item_total_amtInputLayout.setErrorEnabled(false);
                } else {
                    item_total_amtInputLayout.setErrorEnabled(true);
                    item_total_amtInputLayout.setError("total amount is 0.");
                }


                if ( boolItemName && boolItemQty && boolItemPrice && !str_item_total_amt.equals("0") ) {

                    // Check if Internet present
                    if ( !isConnectingToInternet(Create_Item.this) ) {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.network_error_message),Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        /*System.out.println(str_item_name);
                        System.out.println(str_item_qty);
                        System.out.println(str_item_price);
                        System.out.println(str_item_total_amt);*/

                        DatabaseHelper dbHelper = new DatabaseHelper(Create_Item.this);
                        Item item = new Item(refNo,str_item_name,Integer.parseInt(str_item_qty), str_item_price, str_item_total_amt);
                        dbHelper.insertItem(item);
                        dbHelper.closeDB();

                        Intent intentToCO = new Intent(Create_Item.this,Create_View_Orders.class);
                        Bundle extra = new Bundle();
                        extra.putString("referenceNo",refNo);
                        intentToCO.putExtras(extra);
                        startActivity(intentToCO);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intentToCO = new Intent(Create_Item.this,Create_View_Orders.class);
        Bundle extra = new Bundle();
        extra.putString("referenceNo",refNo);
        intentToCO.putExtras(extra);
        startActivity(intentToCO);
        finish();
    }
}
