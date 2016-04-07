package com.alchemistdigital.kissan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.OBP;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 4/5/2016.
 */
public class OBP_Adapter extends RecyclerView.Adapter<OBP_Adapter.OBPViewHolder> {

    List<OBP> obpAllData = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    View views;

    public OBP_Adapter(Context context, List<OBP> obpAllData) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.obpAllData = obpAllData;
    }

    public void delete(int position) {
        obpAllData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public OBPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.obp_details_row, parent, false);
        OBPViewHolder holder = new OBPViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OBPViewHolder holder, int position) {
        OBP current = obpAllData.get(position);
        holder.tv_name.setText("Name - "+current.getObp_name());
        holder.tv_email.setText("( "+current.getObp_email_id()+" )");
        holder.tv_store.setText("Store name - "+current.getObp_store_name());
    }

    @Override
    public int getItemCount() {
        return obpAllData.size();
    }

    class OBPViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_email, tv_store;

        public OBPViewHolder(View itemView) {
            super(itemView);
            views = itemView;

            tv_name = (TextView) itemView.findViewById(R.id.tv_id_inOBPDetails_obp_name);
            tv_email = (TextView) itemView.findViewById(R.id.tv_id_inOBPDetails_obp_email);
            tv_store = (TextView) itemView.findViewById(R.id.tv_id_inOBPDetails_obp_store);

        }
    }
}
