package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.FlocDescriptionActivity;
import com.cleanslatetech.floc.activities.WebviewActivity;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pimpu on 5/2/2017.
 */

public class ChannelRecyclerAdapter extends RecyclerView.Adapter<ChannelRecyclerAdapter.ViewHolder> {
    private Context context;
    private JSONArray jsonArrayLatestRecent;

    public ChannelRecyclerAdapter(Context context, JSONArray jsonArrayLatestRecent) {
        this.context = context;
        this.jsonArrayLatestRecent = jsonArrayLatestRecent;
    }

    @Override
    public ChannelRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.channel_grid_single,parent,false);

        // Return the ViewHolder
        return new ChannelRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChannelRecyclerAdapter.ViewHolder holder, int position){


        // Get the current color from the data set
        try {

            holder.bind(jsonArrayLatestRecent.getJSONObject(position), context);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount(){
        // Count the items
        return jsonArrayLatestRecent.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private AppCompatImageView imgview_bg;
        private AppCompatTextView tvEventName;

        ViewHolder(View v) {
            super(v);

            // Get the widget reference from the custom layout
            imgview_bg = (AppCompatImageView) v.findViewById(R.id.channelBg);
            tvEventName = (AppCompatTextView) v.findViewById(R.id.channelName);
        }

        public void bind(final JSONObject jsonObject, final Context context) {
            String eventPicture = null;
            try {
                eventPicture = jsonObject.getString("ChannelImage");
                String eventName = jsonObject.getString("ChannelName");

                tvEventName.setText(eventName);
                tvEventName.setSelected(true);

                Glide
                        .with(context)
                        .load( CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture)
                        .placeholder(R.drawable.textarea_gradient_bg)
                        .dontAnimate()
                        .into(imgview_bg);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            context.startActivity(new Intent( context, WebviewActivity.class)
                                    .putExtra("from", jsonObject.getString("ChannelName"))
                                    .putExtra("url", jsonObject.getString("ChannelLink") ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}