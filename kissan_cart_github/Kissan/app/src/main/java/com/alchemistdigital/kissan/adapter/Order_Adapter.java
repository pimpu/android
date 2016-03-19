package com.alchemistdigital.kissan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Order;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 3/14/2016.
 */
public class Order_Adapter extends RecyclerView.Adapter<Order_Adapter.OrderViewHolder>  {
    List<Order> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    View views;

    public Order_Adapter(Context context, List<Order> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_details_row, parent, false);
        OrderViewHolder holder = new OrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order current = data.get(position);
        holder.tv_ref.setText(current.getOrder_reference());
        holder.tv_utr.setText(current.getOrder_utr());
        holder.tv_date.setText(current.getOrder_creted_at());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ref, tv_utr, tv_date;

        public OrderViewHolder(View itemView) {
            super(itemView);
            views = itemView;

            tv_ref = (TextView) itemView.findViewById(R.id.tv_id_inOrderView_reference);
            tv_utr = (TextView) itemView.findViewById(R.id.tv_id_inOrderView_utr);
            tv_date = (TextView) itemView.findViewById(R.id.tv_id_inOrderView_date);

        }
    }
}


