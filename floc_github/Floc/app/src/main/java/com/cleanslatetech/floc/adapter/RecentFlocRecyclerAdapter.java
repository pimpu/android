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
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pimpu on 2/14/2017.
 */
public class RecentFlocRecyclerAdapter extends RecyclerView.Adapter<RecentFlocRecyclerAdapter.ViewHolder> {
    private Context context;
    private JSONArray jaData;
    private String from;

    public RecentFlocRecyclerAdapter(Context context, JSONArray jaData, String from) {
        this.context = context;
        this.jaData = jaData;
        this.from = from;
    }

    @Override
    public RecentFlocRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(context).inflate(R.layout.all_events_row,parent,false);

        // Return the ViewHolder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){


        // Get the current color from the data set
        try {

            holder.bind(jaData.getJSONObject(position), context, holder, from);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount(){
        // Count the items
        return jaData.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private AppCompatImageView imgview_bg;
        private AppCompatTextView tvEventName;

        ViewHolder(View v) {
            super(v);

            // Get the widget reference from the custom layout
            imgview_bg = (AppCompatImageView) v.findViewById(R.id.id_all_event_row_bg);
            tvEventName = (AppCompatTextView) v.findViewById(R.id.id_all_event_row_name);
        }

        public void bind(final JSONObject jsonObject, final Context context, ViewHolder holder, final String from) {
            String eventPicture = null;
            try {
                eventPicture = jsonObject.getString("EventPicture");
                String eventName = jsonObject.getString("EventName");

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
                        Intent intentFlocDesc = new Intent(context, FlocDescriptionActivity.class);
                        intentFlocDesc.putExtra("floc_data", jsonObject.toString());
                        intentFlocDesc.putExtra("from", from);
                        context.startActivity(intentFlocDesc);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
