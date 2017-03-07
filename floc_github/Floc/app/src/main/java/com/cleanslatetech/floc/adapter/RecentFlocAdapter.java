package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by pimpu on 2/7/2017.
 */
public class RecentFlocAdapter extends BaseAdapter {
    private Context context;
    private JSONArray jsonArrayLatestRecent;

    public RecentFlocAdapter(Context context, JSONArray jsonArrayLatestRecent) {
        this.context = context;
        this.jsonArrayLatestRecent = jsonArrayLatestRecent;
    }

    @Override
    public int getCount() {
        return jsonArrayLatestRecent.length();
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
        ViewRecentFlocHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.recentfloc_grid_single, null);
            holder = new ViewRecentFlocHolder();

            holder.imgview_bg = (AppCompatImageView) convertView.findViewById(R.id.recentFlocBg);
            holder.eventName = (AppCompatTextView) convertView.findViewById(R.id.tvRecentFlocName);

            convertView.setTag(holder);
        } else {
            holder = (ViewRecentFlocHolder) convertView.getTag();
        }

        try {
            String eventPicture = jsonArrayLatestRecent.getJSONObject(position).getString("EventPicture");
            String eventName = jsonArrayLatestRecent.getJSONObject(position).getString("EventName");

            holder.eventName.setText(eventName);
            holder.eventName.setSelected(true);
            Glide
                    .with(context)
                    .load( CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture)
                    .placeholder(R.drawable.textarea_gradient_bg)
                    .dontAnimate()
                    .into(holder.imgview_bg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    private static class ViewRecentFlocHolder {
        AppCompatImageView imgview_bg;
        AppCompatTextView eventName;
    }
}
