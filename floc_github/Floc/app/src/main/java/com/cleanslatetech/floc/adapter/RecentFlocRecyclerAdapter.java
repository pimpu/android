package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonVariables;

/**
 * Created by pimpu on 2/14/2017.
 */
public class RecentFlocRecyclerAdapter extends RecyclerView.Adapter<RecentFlocRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private String[] images = new String[]{ "ET00044536.jpg", "ET00040772.jpg", "ET00044680.jpg", "ET00044480.jpg"};

    public RecentFlocRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecentFlocRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_recent_floc,parent,false);

        // Return the ViewHolder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        // Get the current color from the data set
//        String color = mDataSet.get(position);

        Glide
                .with(mContext)
                .load( CommonVariables.EVENT_IMAGE_SERVER_URL + images[position])
                .placeholder(R.drawable.textarea_gradient_bg)
                .dontAnimate()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount(){
        // Count the items
        return images.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatImageView mImageView;

        ViewHolder(View v) {
            super(v);
            // Get the widget reference from the custom layout
            mImageView = (AppCompatImageView) v.findViewById(R.id.individualFlocBg);
        }
    }

}
