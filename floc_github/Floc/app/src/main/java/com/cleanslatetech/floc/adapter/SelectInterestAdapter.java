package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleanslatetech.floc.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pimpu on 1/2/2017.
 */

public class SelectInterestAdapter extends BaseAdapter {
    public static ArrayList<Integer> iArraySelectedPositions;
    private Context context;
    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.movie, R.drawable.theater,R.drawable.festival,
            R.drawable.music, R.drawable.dance, R.drawable.literatures,
            R.drawable.food_drinks, R.drawable.sports, R.drawable.health_wellness
    };

    public String[] mThumbString = {
        "Movie", "Theater", "Festival", "Music", "Dance", "Literature", "Food\n&\nDrinks", "Sports", "Health\n&\nWellness"
    };

    public SelectInterestAdapter(Context context) {
        this.context = context;
        iArraySelectedPositions = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
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
        View gridview = convertView;
        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            gridview = inflater.inflate(R.layout.grid_single, null);
            holder = new ViewHolder();

            holder.imageViewThumbPic = (ImageView) gridview.findViewById(R.id.grid_image);
            holder.textView = (TextView) gridview.findViewById(R.id.grid_text);
            holder.imageviewChk = (ImageView) gridview.findViewById(R.id.idCheckboxinterest);

            holder.textView.setText(mThumbString[position]);
            holder.imageViewThumbPic.setImageResource(mThumbIds[position]);
            gridview.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(iArraySelectedPositions.contains(position)) {
            holder.imageviewChk.setVisibility(convertView.VISIBLE);
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            gridview.setBackgroundResource(R.drawable.grid_selected);
        } else {
            holder.imageviewChk.setVisibility(convertView.GONE);
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            gridview.setBackgroundDrawable(null);
        }

        return gridview;
    }

    static class ViewHolder {
        ImageView imageViewThumbPic;
        ImageView imageviewChk;
        TextView textView;
    }

}
