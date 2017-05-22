package com.cleanslatetech.floc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.HomePageActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pimpu on 5/22/2017.
 */

public class DrawerMenuAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> dataName;
    private ArrayList<Integer> dataImage;
    private static LayoutInflater inflater=null;

    public DrawerMenuAdapter(Activity a, ArrayList<String> menuName, ArrayList<Integer> menuImage) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataImage = menuImage;
        dataName = menuName;
    }

    public int getCount() {
        return dataName.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.drawer_menulist_row, null);

        AppCompatTextView title = (AppCompatTextView)vi.findViewById(R.id.drawer_menu_name); // title
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.drawer_menu_image); // thumb image

        // Setting all values in listview
        title.setText(dataName.get(position));
        thumb_image.setImageResource(dataImage.get(position));
        return vi;
    }
}
