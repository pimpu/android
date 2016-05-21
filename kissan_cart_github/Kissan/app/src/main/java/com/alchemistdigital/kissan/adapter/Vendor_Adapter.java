package com.alchemistdigital.kissan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Vendor;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 5/20/2016.
 */
public class Vendor_Adapter extends RecyclerView.Adapter<Vendor_Adapter.VendorViewHolder>{

    List<Vendor> vendorAllData = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    View views;

    public Vendor_Adapter(Context context, List<Vendor> vendorAllData) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.vendorAllData = vendorAllData;
    }

    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vendor_details_row, parent, false);
        VendorViewHolder holder = new VendorViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VendorViewHolder holder, int position) {
        Vendor current = vendorAllData.get(position);
        holder.tv_name.setText(current.getVendor_name());
        holder.tv_contact.setText("( "+current.getVendor_contact()+" )");
    }

    @Override
    public int getItemCount() {
        return vendorAllData.size();
    }

    class VendorViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name, tv_contact;

        public VendorViewHolder(View itemView) {
            super(itemView);
            views = itemView;

            tv_name = (TextView) itemView.findViewById(R.id.tv_id_inVendorDetails_VendorName);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_id_inVendorDetails_VendorContact);
        }
    }
}
