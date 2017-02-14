package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.BaseAppCompactActivity;

import java.util.ArrayList;

/**
 * Created by pimpu on 2/14/2017.
 */

public class CustomMenuAdapter extends ArrayAdapter {
    String texta = null;
    private ArrayList<BaseAppCompactActivity.DataModel> dataSet;
    Context mContext;
    PopupWindow popupWindow;

    // View lookup cache
    private class ViewHolder {
        AppCompatTextView txtName;
    }

    public CustomMenuAdapter(ArrayList<BaseAppCompactActivity.DataModel> data, Context context, PopupWindow popupWindow) {
        super(context, R.layout.menu_row_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.popupWindow=popupWindow;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BaseAppCompactActivity.DataModel dataModel = (BaseAppCompactActivity.DataModel) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.menu_row_item, parent, false);
            viewHolder.txtName = (AppCompatTextView) convertView.findViewById(R.id.txtMenuName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(dataModel.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder tag = (ViewHolder) v.getTag();
                texta = tag.txtName.getText().toString();
                popupWindow.dismiss();
            }
        });

        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texta = viewHolder.txtName.getText().toString();
                popupWindow.dismiss();
            }
        });

        if(dataModel.getName().equals(texta)) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.dark_blue));
        }
        else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
