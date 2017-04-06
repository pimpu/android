package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cleanslatetech.floc.R;

/**
 * Created by pimpu on 4/3/2017.
 */

public class CustomSliderPagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private int[] mResources;

    public CustomSliderPagerAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = new int[]{R.drawable.slider_1, R.drawable.slider_3, R.drawable.slider_4, R.drawable.slider_5};
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.slider_pager_item,container,false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);
           /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(950, 950);
            imageView.setLayoutParams(layoutParams);*/
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
