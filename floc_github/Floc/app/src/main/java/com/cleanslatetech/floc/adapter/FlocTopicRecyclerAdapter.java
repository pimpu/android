package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.FlocDescTopicsActivity;
import com.cleanslatetech.floc.activities.FlocDescriptionActivity;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by pimpu on 3/4/2017.
 */
public class FlocTopicRecyclerAdapter extends RecyclerView.Adapter<FlocTopicRecyclerAdapter.ViewHolder> {
    private  JSONArray arrayData = new JSONArray();
    private LayoutInflater inflater;
    private Context context;
    private String topicsType;

    public FlocTopicRecyclerAdapter(Context context, JSONArray arrayData, String topicsType) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrayData = arrayData;
        this.topicsType = topicsType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.floc_topic_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            if( topicsType.equals("likes") || topicsType.equals("reviews") ) {

                String profilePic = arrayData.getJSONObject(position).getString("ProfilePic");
                String userName = arrayData.getJSONObject(position).getString("UserName");
                String subTxt = arrayData.getJSONObject(position).getString("Review");

                holder.tvName.setText(userName);

                if( !subTxt.equals("NULL") ) {
                    holder.tvSubTopic.setText(subTxt);
                }

                if( !profilePic.equals("null") ) {

                    Glide
                            .with(context)
                            .load( CommonVariables.EVENT_IMAGE_SERVER_URL + profilePic)
                            .placeholder(R.drawable.textarea_gradient_bg)
                            .dontAnimate()
                            .into(holder.imgvwPic);
                }

            } else {
                String profilePic = arrayData.getJSONObject(position).getString("UserPic");
                String userName = arrayData.getJSONObject(position).getString("UserName");
                String subTxt = arrayData.getJSONObject(position).getString("UserEmailId");

                holder.tvName.setText(userName);
                holder.tvSubTopic.setText(subTxt);

                if( !profilePic.equals("null") ) {

                    Glide
                            .with(context)
                            .load( CommonVariables.EVENT_IMAGE_SERVER_URL + profilePic)
                            .placeholder(R.drawable.textarea_gradient_bg)
                            .dontAnimate()
                            .into(holder.imgvwPic);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSubTopic;
        AppCompatImageView imgvwPic;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.floc_topic_username);
            tvSubTopic = (TextView) itemView.findViewById(R.id.floc_topic_subtopic);
            imgvwPic = (AppCompatImageView) itemView.findViewById(R.id.floc_topic_profle);
        }
    }
}
