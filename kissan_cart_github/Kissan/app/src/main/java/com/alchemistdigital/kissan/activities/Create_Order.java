package com.alchemistdigital.kissan.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.alchemistdigital.kissan.R;
import com.andexert.library.RippleView;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;

public class Create_Order extends AppCompatActivity {

    private EditText txt_utr,txt_item_name,txt_item_qty,txt_item_price,txt_item_total_amt;
    private String  str_utr,str_item_name,str_item_qty,str_item_price,str_item_total_amt;
    TextInputLayout utrInputLayout,item_nameInputLayout,item_qtyInputLayout,item_priceInputLayuot,item_total_amtInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_order_toolbar);
        setSupportActionBar(toolbar);

        txt_utr             = (EditText) findViewById(R.id.edittext_id_utr);
        txt_item_name       = (EditText) findViewById(R.id.edittext_id__item_name);
        txt_item_qty        = (EditText) findViewById(R.id.edittext_id__item_qty);
        txt_item_price      = (EditText) findViewById(R.id.edittext_id__item_price);
        txt_item_total_amt  = (EditText) findViewById(R.id.edittext_id__item_total_amt);

        utrInputLayout              = (TextInputLayout) findViewById(R.id.id_input_layout_utr);
        item_nameInputLayout        = (TextInputLayout) findViewById(R.id.id_input_layout__item_name);
        item_qtyInputLayout         = (TextInputLayout) findViewById(R.id.id_input_layout__item_qty);
        item_priceInputLayuot       = (TextInputLayout) findViewById(R.id.id_input_layout__item_price);
        item_total_amtInputLayout   = (TextInputLayout) findViewById(R.id.id_input_layout__item_total_amt);

        final RippleView rippleView = (RippleView)findViewById(R.id.btn_id_submit_order);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                str_utr             = txt_utr.getText().toString();
                str_item_name       = txt_item_name.getText().toString();
                str_item_qty        = txt_item_qty.getText().toString();
                str_item_price      = txt_item_price.getText().toString();
                str_item_total_amt  = txt_item_total_amt.getText().toString();

                Boolean boolUtr         = isEmptyString(str_utr);
                Boolean boolItemName    = isEmptyString(str_item_name);
                Boolean boolItemQty     = isEmptyString(str_item_qty);
                Boolean boolItemPrice   = isEmptyString(str_item_price);
                Boolean boolTotalAmt    = isEmptyString(str_item_total_amt);

                if (boolUtr) {
                    utrInputLayout.setErrorEnabled(false);
                } else {
                    utrInputLayout.setErrorEnabled(true);
                    utrInputLayout.setError("UTR field is empty.");
                }

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

                if (boolTotalAmt) {
                    item_total_amtInputLayout.setErrorEnabled(false);
                } else {
                    item_total_amtInputLayout.setErrorEnabled(true);
                    item_total_amtInputLayout.setError("total amount field is empty.");
                }


                if ( boolUtr && boolItemName && boolItemQty && boolItemPrice && boolTotalAmt ) {

                    // Check if Internet present
                    if ( !isConnectingToInternet(Create_Order.this) ) {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.network_error_message),Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        System.out.println(str_utr);
                        System.out.println(str_item_name);
                        System.out.println(str_item_qty);
                        System.out.println(str_item_price);
                        System.out.println(str_item_total_amt);
                    }
                }
            }
        });
    }
}
