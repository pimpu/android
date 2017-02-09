package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomeActivity;

/**
 * Created by pimpu on 2/7/2017.
 */
public class EventFlocAdapter extends BaseAdapter {
    private Context context;
    private int[] images = new int[]{R.drawable.guitarist, R.drawable.guitarist, R.drawable.guitarist, R.drawable.guitarist};

    public EventFlocAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewEventHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.event_grid_single, null);
            holder = new ViewEventHolder();

            holder.imgview_bg = (AppCompatImageView) convertView.findViewById(R.id.eventBg);

            convertView.setTag(holder);
        } else {
            holder = (ViewEventHolder) convertView.getTag();
        }

        holder.imgview_bg.setBackgroundResource(images[position]);

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

    private static class ViewEventHolder {
        AppCompatImageView imgview_bg;
    }
}
