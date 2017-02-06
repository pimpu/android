package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.TextDrawable;

import org.json.JSONException;

/**
 * Created by pimpu on 2/7/2017.
 */
public class InterestAdapter extends BaseAdapter {
    private String[] stringArray;
    private Context context;

    public InterestAdapter(Context context) {
        stringArray = context.getResources().getStringArray(R.array.interests);
        this.context = context;
    }

    @Override
    public int getCount() {
        return stringArray.length;
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
        final ViewInterestHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            gridview = inflater.inflate(R.layout.interest_grid_single, null);
            holder = new ViewInterestHolder();

            holder.tvInterestName = (AppCompatTextView) gridview.findViewById(R.id.tv_interest_name);
            holder.imgBtn_interest = (AppCompatImageButton) gridview.findViewById(R.id.imgBtn_selectInterest);
            holder.imgBtn_interest.setSelected(true);

            holder.tvInterestName.setText(stringArray[position]);

            holder.imgBtn_interest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.imgBtn_interest.setSelected(!holder.imgBtn_interest.isSelected());
                    holder.imgBtn_interest.setImageResource(holder.imgBtn_interest.isSelected() ? R.drawable.animated_plus : R.drawable.animated_minus);
                    Drawable drawable = holder.imgBtn_interest.getDrawable();
                    if (drawable instanceof Animatable) {
                        ((Animatable) drawable).start();
                    }


                }
            });


            gridview.setTag(holder);

        } else {
            holder = (ViewInterestHolder) convertView.getTag();
        }

        /*if(iArraySelectedPositions.contains(eventCategoryId)) {
            holder.imageviewChk.setVisibility(convertView.VISIBLE);
            holder.outsideImgView.setVisibility(convertView.VISIBLE);
//            gridview.setBackgroundResource(R.drawable.grid_selected);
        } else {
            holder.imageviewChk.setVisibility(convertView.GONE);
            holder.outsideImgView.setVisibility(convertView.GONE);
//            gridview.setBackgroundDrawable(null);
        }*/

        return gridview;
    }

    static class ViewInterestHolder {
        AppCompatTextView tvInterestName;
        AppCompatImageButton imgBtn_interest;
    }
}
