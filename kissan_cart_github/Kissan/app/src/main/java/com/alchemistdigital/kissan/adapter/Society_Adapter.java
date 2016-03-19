package com.alchemistdigital.kissan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Society;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 3/18/2016.
 */
public class Society_Adapter extends RecyclerView.Adapter<Society_Adapter.SocietyViewHolder> {

    List<Society> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    View views;

    public Society_Adapter(Context context, List<Society> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public SocietyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.society_details_row, parent, false);

        SocietyViewHolder holder = new SocietyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SocietyViewHolder holder, int position) {
        Society current = data.get(position);
        holder.tv_scoiety_name.setText(current.getSoc_name());
        holder.tv_contact.setText( " ("+current.getSoc_contact() + ")");
        holder.tv_email.setText(current.getSoc_email());
        holder.tv_address.setText(current.getSoc_adrs());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SocietyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_scoiety_name, tv_contact, tv_email, tv_address;

        public SocietyViewHolder(View itemView) {
            super(itemView);
            views = itemView;

            tv_scoiety_name = (TextView) itemView.findViewById(R.id.tv_id_inScoietyView_name);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_id_inScoietyView_contact);
            tv_email = (TextView) itemView.findViewById(R.id.tv_id_inScoietyView_email);
            tv_address = (TextView) itemView.findViewById(R.id.tv_id_inScoietyView_address);

        }
    }

}
