package com.alchemistdigital.kissan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Enquiry;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 3/8/2016.
 */
public class New_Reply_Enquiry_Adapter extends RecyclerView.Adapter<New_Reply_Enquiry_Adapter.EnquiryViewHolder> {

    List<Enquiry> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    View views;

    public New_Reply_Enquiry_Adapter(Context context, List<Enquiry> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public EnquiryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.new_reply_enquiry_details_row, parent, false);
        EnquiryViewHolder holder = new EnquiryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EnquiryViewHolder holder, int position) {
        Enquiry current = data.get(position);
        holder.tv_ref.setText("Ref no - "+current.getEnquiry_reference());
        holder.tv_scoiety.setText("Scoiety - "+current.getEnquiry_society());
        holder.tv_message.setText("Message - "+current.getEnquiry_message());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EnquiryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ref, tv_scoiety, tv_message;

        public EnquiryViewHolder(View itemView) {
            super(itemView);
            views = itemView;
            tv_ref = (TextView) itemView.findViewById(R.id.tv_id_newReply_eRef);
            tv_scoiety = (TextView) itemView.findViewById(R.id.tv_id_newReply_eSociety);
            tv_message = (TextView) itemView.findViewById(R.id.tv_id_newReply_eMessage);
        }
    }
}
