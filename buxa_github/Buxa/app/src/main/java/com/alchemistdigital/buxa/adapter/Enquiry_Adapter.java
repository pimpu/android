package com.alchemistdigital.buxa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.ShipmentConformationModel;
import com.alchemistdigital.buxa.utilities.DateHelper;
import com.alchemistdigital.buxa.utilities.ItemClickListener;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pimpu on 11/15/2016.
 */

public class Enquiry_Adapter extends RecyclerView.Adapter<Enquiry_Adapter.EnquiryViewHolder>  {
    List<ShipmentConformationModel> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    View views;
    Calendar c;
    String day;
    DateHelper dateHelper;
    ItemClickListener itemClickListener;

    public Enquiry_Adapter(Context context, List<ShipmentConformationModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        itemClickListener = (ItemClickListener)context;

        c=Calendar.getInstance();
        day = dateHelper.getDay(c.getTimeInMillis());
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public EnquiryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.enquiry_details_row, parent, false);
        EnquiryViewHolder holder = new EnquiryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EnquiryViewHolder holder, int position) {

        ShipmentConformationModel current = data.get(position);
        holder.tv_bookingid.setText(current.getBookingId());

        String date;
        int trans, cc, ff;

        if( day.equals(dateHelper.getDay(Long.parseLong(current.getCreatedAt())))) {
            date = dateHelper.convertToTodayEnquiryString(Long.parseLong(current.getCreatedAt()));
        }
        else {
            date = dateHelper.getDay(Long.parseLong(current.getCreatedAt()))+" "+dateHelper.getMonthString(Long.parseLong(current.getCreatedAt()));
        }
        holder.tv_date.setText(date);
        trans = current.getIsTrans();
        cc = current.getIsCC();
        ff = current.getIsFF();

        if( trans == 1) {
            holder.trans_layout.setVisibility(View.VISIBLE);
        }

        if( cc == 1) {
            holder.cc_layout.setVisibility(View.VISIBLE);
        }

        if( ff == 1) {
            holder.ff_layout.setVisibility(View.VISIBLE);
        }

        String status ="Status: ";
        if(current.getEnquiryStatus() == 1 ) {
            status += "enquiry submit";
        }
        else if(current.getEnquiryStatus() == 2 ) {
            status += "view by admin";
        }
        else if(current.getEnquiryStatus() == 3 ) {
            status += "email sent to you";
        }
        else if(current.getEnquiryStatus() == 4 ) {
            status += "accept";
//            holder.conform_btn_layout.setVisibility(View.GONE);
        }
        else if(current.getEnquiryStatus() == 5 ) {
            status += "cancel";
            holder.conform_btn_layout.setVisibility(View.GONE);
        }
        holder.tv_status.setText(status);

        String quotaion = current.getQuotaion();
        if (quotaion == null) {
            holder.tv_quotation.setVisibility(View.GONE);
        }
        else {
            holder.tv_quotation.setText("Quotation: "+ quotaion);
        }

        holder.tv_rates.setText("Rates: "+current.getRates()+" "+context.getResources().getString(R.string.Rs));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EnquiryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_bookingid, tv_date, tv_trans, tv_cc, tv_ff, tv_status, tv_quotation, tv_rates,
                tv_accept, tv_cancel;
        LinearLayout trans_layout, cc_layout, ff_layout;
        RelativeLayout conform_btn_layout;

        public EnquiryViewHolder(View itemView) {
            super(itemView);
            views = itemView;

            tv_bookingid = (TextView) itemView.findViewById(R.id.enquiry_row_bookigid);
            tv_date = (TextView) itemView.findViewById(R.id.enquiry_row_date);
            tv_trans = (TextView) itemView.findViewById(R.id.enquiry_row_trans);
            tv_cc = (TextView) itemView.findViewById(R.id.enquiry_row_cc);
            tv_ff = (TextView) itemView.findViewById(R.id.enquiry_row_ff);
            tv_quotation = (TextView) itemView.findViewById(R.id.enquiry_row_quotation);
            tv_rates = (TextView) itemView.findViewById(R.id.enquiry_row_rate);
            tv_status = (TextView) itemView.findViewById(R.id.enquiry_row_status);
            tv_accept = (TextView) itemView.findViewById(R.id.enquiry_row_accept);
            tv_cancel = (TextView) itemView.findViewById(R.id.enquiry_row_cancel);

            trans_layout = (LinearLayout) itemView.findViewById(R.id.trans_layout);
            cc_layout = (LinearLayout) itemView.findViewById(R.id.cc_layout);
            ff_layout = (LinearLayout) itemView.findViewById(R.id.ff_layout);

            conform_btn_layout = (RelativeLayout) itemView.findViewById(R.id.conformation_button_layout);

            trans_layout.setOnClickListener(this);
            cc_layout.setOnClickListener(this);
            ff_layout.setOnClickListener(this);
            tv_quotation.setOnClickListener(this);
            tv_accept.setOnClickListener(this);
            tv_cancel.setOnClickListener(this);

            tv_trans.setOnClickListener(this);
            tv_cc.setOnClickListener(this);
            tv_ff.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.trans_layout:
                    itemClickListener.onTransportClick(getAdapterPosition());
                    break;
                case R.id.enquiry_row_trans:
                    itemClickListener.onTransportClick(getAdapterPosition());
                    break;
                case R.id.cc_layout:
                    itemClickListener.onCCClick(getAdapterPosition());
                    break;
                case R.id.enquiry_row_cc:
                    itemClickListener.onCCClick(getAdapterPosition());
                    break;
                case R.id.ff_layout:
                    itemClickListener.onFFClick(getAdapterPosition());
                    break;
                case R.id.enquiry_row_ff:
                    itemClickListener.onFFClick(getAdapterPosition());
                    break;
                case R.id.enquiry_row_quotation:
                    itemClickListener.onPdfViewClick(getAdapterPosition());
                    break;
                case R.id.enquiry_row_accept:
                    itemClickListener.onAcceptwClick(getAdapterPosition());
                    break;
                case R.id.enquiry_row_cancel:
                    itemClickListener.onCancelClick(getAdapterPosition());
                    break;

            }
        }

    }

}
