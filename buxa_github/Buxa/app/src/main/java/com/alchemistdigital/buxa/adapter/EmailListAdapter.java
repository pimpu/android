package com.alchemistdigital.buxa.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.Email_Account_Item;

import java.util.List;

/**
 * Created by Pimpu on 10/27/2016.
 */
public class EmailListAdapter extends ArrayAdapter<Email_Account_Item> {

    private List<Email_Account_Item> appsList = null;
    private Context context;
    private TextView txtRegisterEmail;
    private AlertDialog alertDialog;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.email_account_name_view, null);
        }

        final Email_Account_Item data = appsList.get(position);
        if (null != data) {

            TextView appName = (TextView) view.findViewById(R.id.idAcEmail);

            appName.setText(data.getName());

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    txtRegisterEmail.setText(data.getName().trim());

                    alertDialog.dismiss();
                }
            });
        }
        return view;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Email_Account_Item getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public EmailListAdapter(Context context, int textViewResourceId, List<Email_Account_Item> appsList, TextView txtRegisterEmail, AlertDialog alertDialog) {
        super(context, textViewResourceId, appsList);
        this.context            = context;
        this.appsList           = appsList;
        this.txtRegisterEmail   = txtRegisterEmail;
        this.alertDialog        = alertDialog;
    }

}
