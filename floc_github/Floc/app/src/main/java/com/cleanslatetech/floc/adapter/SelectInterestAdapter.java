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

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pimpu on 1/2/2017.
 */

public class SelectInterestAdapter extends BaseAdapter {
    public static ArrayList<Integer> iArraySelectedPositions;
    private Context context;
    private JSONArray getCategory;

    public SelectInterestAdapter(Context context, JSONArray getCategory) {
        this.context = context;
        this.getCategory = getCategory;
        System.out.println(getCategory);
        iArraySelectedPositions = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        return getCategory.length();
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

            try {
                holder.textView.setText(getCategory.getJSONObject(position).getString("EventCategoryName"));
                String categoryPic = getCategory.getJSONObject(position).getString("CategoryPic");

                Glide
                    .with(context)
                    .load( CommonVariables.INTEREST_CATEGORY_SERVER_URL + categoryPic)
                    .centerCrop()
                    .placeholder(R.drawable.progress)
                    .crossFade()
                    .into(holder.imageViewThumbPic);

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
