package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.FlocDescriptionActivity;
import com.cleanslatetech.floc.activities.HomePageActivity;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pimpu on 2/7/2017.
 */
public class HomeFlocAdapter extends BaseAdapter {
    private Context context;
    private JSONArray jaData;
    private int limit;

    public HomeFlocAdapter(Context context, JSONArray jaData, int limit) {
        this.context = context;
        this.jaData = jaData;
        this.limit = limit;

        System.out.println(jaData);
    }

    @Override
    public int getCount() {
        return limit;
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
            final JSONObject jsonObject = jaData.getJSONObject(position);
            String eventPicture = jsonObject.getString("EventPicture");
            String eventName = jsonObject.getString("EventName");

            holder.eventName.setText(eventName);
            holder.eventName.setSelected(true);
            Glide
                    .with(context)
                    .load( CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture)
                    .placeholder(R.drawable.textarea_gradient_bg)
                    .dontAnimate()
                    .into(holder.imgview_bg);

            holder.imgview_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentFlocDesc = new Intent(context, FlocDescriptionActivity.class);
                    intentFlocDesc.putExtra("floc_data", jsonObject.toString());
                    intentFlocDesc.putExtra("from", "Home");
                    context.startActivity(intentFlocDesc);
                }
            });

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