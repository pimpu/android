package com.alchemistdigital.ziko.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alchemistdigital.ziko.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 5/3/2016.
 */
public class ImageLoaderAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    String file_path;
    private List<String> VideoValues;
    public int selectedPosition;
    HashMap<String, String> videoDuration;

    public ImageLoaderAdapter(Context context,
                              ArrayList<String> VideoValues,
                              String file_path,
                              HashMap<String, String> videoDuration) {
        inflater = LayoutInflater.from(context);
        this.VideoValues = VideoValues;
        this.file_path = file_path;
        this.context = context;
        this.videoDuration = videoDuration;
    }

    @Override
    public int getCount() {
        return VideoValues.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String fullFilePath = file_path + VideoValues.get(position);
        String parse = Uri.fromFile(new File(fullFilePath)).toString();
        String decodedUri = Uri.decode(parse);
        final ViewHolder holder;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.gridlayout, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.imageView = (ImageView) view.findViewById(R.id.grid_item_image);
            holder.gridLabel = (TextView) view.findViewById(R.id.grid_item_label);
            // set duration of video into textview.
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // set video duration text
        holder.gridLabel.setText( videoDuration.get(fullFilePath));

        Glide
            .with(context)
            .load( decodedUri )
            .thumbnail( 0.1f )
            .into(holder.imageView);

        if(position == selectedPosition){
            view.setBackgroundResource(R.drawable.grid_selected);
        } else {
            view.setBackgroundDrawable(null);
        }

        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView gridLabel;
    }
}

