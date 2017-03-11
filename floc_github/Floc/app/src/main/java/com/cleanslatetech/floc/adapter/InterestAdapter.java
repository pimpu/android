package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pimpu on 2/7/2017.
 */
public class InterestAdapter extends BaseAdapter {
    private JSONArray categories;
    private Context context;
    public List<Integer> iArraySelectedPositions;
    private int iCounter=5;

    public InterestAdapter(Context context, JSONArray categories) {
        this.categories = categories;
        this.context = context;
        iArraySelectedPositions = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        return categories.length();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewInterestHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.interest_grid_single, null);
            holder = new ViewInterestHolder();

            holder.tvInterestName = (AppCompatTextView) convertView.findViewById(R.id.tv_interest_name);
            holder.imgBtn_interest = (AppCompatImageButton) convertView.findViewById(R.id.imgBtn_selectInterest);
            holder.imgBtn_interest.setSelected(true);

            convertView.setTag(holder);
        } else {
            holder = (ViewInterestHolder) convertView.getTag();
        }

        try {
            final int eventCategoryId = categories.getJSONObject(position).getInt("EventCategoryId");

            holder.tvInterestName.setText(categories.getJSONObject(position).getString("EventCategoryName"));
            holder.imgBtn_interest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View p = (View) v.getParent();
                    ViewInterestHolder holder1 = (ViewInterestHolder) p.getTag();

                    if( iArraySelectedPositions.contains(eventCategoryId) && (iCounter >= 0 ) ) {
                        // set text colour
                        holder1.tvInterestName.setTextColor(context.getResources().getColor(R.color.white));

                        iArraySelectedPositions.remove(iArraySelectedPositions.indexOf(eventCategoryId));
                        iCounter++;

                        // animations
                        holder1.imgBtn_interest.setImageResource(R.drawable.animated_plus );
                        Drawable drawable = holder1.imgBtn_interest.getDrawable();
                        if (drawable instanceof Animatable) {
                            ((Animatable) drawable).start();
                        }
                    }
                    else if(iCounter <= 5 && iCounter > 0 ) {
                        // set text colour
                        holder1.tvInterestName.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                        iCounter--;
                        iArraySelectedPositions.add(eventCategoryId);

                        // animations
                        holder1.imgBtn_interest.setImageResource(R.drawable.animated_minus );
                        Drawable drawable = holder1.imgBtn_interest.getDrawable();
                        if (drawable instanceof Animatable) {
                            ((Animatable) drawable).start();
                        }
                    }

                    ((HomeActivity)context).changeState_saveInterest(iCounter);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private static class ViewInterestHolder {
        AppCompatTextView tvInterestName;
        AppCompatImageButton imgBtn_interest;
    }
}