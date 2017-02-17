package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by pimpu on 1/31/2017.
 */
public class AllFlocRecyclerAdapter extends RecyclerView.Adapter<AllFlocRecyclerAdapter.EventViewHolder>{
    private  JSONArray getEventsArray;
    private LayoutInflater inflater;
    private Context context;

    public AllFlocRecyclerAdapter(Context context, JSONArray getEvents) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        getEventsArray = getEvents;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.floc_row_layout, parent, false);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        try {
            String eventName = getEventsArray.getJSONObject(position).getString("EventName");
            holder.tvEventName.setText(eventName);
            holder.tvEventArea.setText( getEventsArray.getJSONObject(position).getString("EventArea") );
            String eventPicture = getEventsArray.getJSONObject(position).getString("EventPicture");

            Glide
                    .with(context)
                    .load( CommonVariables.EVENT_IMAGE_SERVER_URL + eventPicture)
                    .placeholder(R.drawable.textarea_gradient_bg)
                    .dontAnimate()
                    .into(holder.imgvwEventPic);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return getEventsArray.length();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvEventArea;
        AppCompatImageView imgvwEventPic;

        public EventViewHolder(View itemView) {
            super(itemView);

            tvEventName = (TextView) itemView.findViewById(R.id.row_floc_name);
            tvEventArea = (TextView) itemView.findViewById(R.id.row_floc_area);
            imgvwEventPic = (AppCompatImageView) itemView.findViewById(R.id.floc_image);

        }
    }
}
