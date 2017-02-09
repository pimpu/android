package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.TextDrawable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pimpu on 1/2/2017.
 */

public class SelectInterestAdapter extends BaseAdapter {
    public static List<Integer> iArraySelectedPositions;
    private Context context;
    private JSONArray getCategory;

    public SelectInterestAdapter(Context context, JSONArray getCategory) {
        this.context = context;
        this.getCategory = getCategory;
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
        int eventCategoryId = 0;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            eventCategoryId = getCategory.getJSONObject(position).getInt("EventCategoryId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(convertView == null) {
            gridview = inflater.inflate(R.layout.grid_single, null);
            holder = new ViewHolder();

            holder.imageViewThumbPic = (ImageView) gridview.findViewById(R.id.grid_image);
            holder.imageviewChk = (ImageView) gridview.findViewById(R.id.idCheckboxinterest);
            holder.outsideImgView = (ImageView) gridview.findViewById(R.id.outside_imageview);

            try {
                String categoryPic = getCategory.getJSONObject(position).getString("CategoryPic");
                String interestText = getCategory.getJSONObject(position).getString("EventCategoryName");

                Glide
                    .with(context)
                    .load( CommonVariables.INTEREST_CATEGORY_SERVER_URL + categoryPic)
                    .centerCrop()
                    .placeholder(new TextDrawable(context, interestText))
                    .dontAnimate()
                    .into(holder.imageViewThumbPic);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            gridview.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(iArraySelectedPositions.contains(eventCategoryId)) {
            holder.imageviewChk.setVisibility(convertView.VISIBLE);
            holder.outsideImgView.setVisibility(convertView.VISIBLE);
//            gridview.setBackgroundResource(R.drawable.grid_selected);
        } else {
            holder.imageviewChk.setVisibility(convertView.GONE);
            holder.outsideImgView.setVisibility(convertView.GONE);
//            gridview.setBackgroundDrawable(null);
        }

        return gridview;
    }

    private static class ViewHolder {
        ImageView imageViewThumbPic;
        ImageView imageviewChk;
        ImageView outsideImgView;
    }

}
