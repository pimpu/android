package com.alchemistdigital.kissan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.kissan.R;
import com.alchemistdigital.kissan.model.Item;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 3/11/2016.
 */
public class ItemsListAdapter extends BaseAdapter {
    List<Item> data = Collections.emptyList();
    Context context;
    private static LayoutInflater inflater=null;
    public ItemsListAdapter(Context context, List<Item> itemByRefno) {
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = itemByRefno;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView tv_name,tv_total;

        View rowView;
        rowView = inflater.inflate(R.layout.item_listview, null);

        tv_name=(TextView) rowView.findViewById(R.id.tv_id_itemName);
        tv_total=(TextView) rowView.findViewById(R.id.tv_id_itemTotal);

        tv_name.setText(data.get(position).getItemName());
        tv_total.setText("( "+data.get(position).getItemTotalAmount()+" "+context.getResources().getString(R.string.Rs)+" )");

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + data.get(position).getItemName(), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
