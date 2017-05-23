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
import com.cleanslatetech.floc.interfaces.InterfaceOnClickText;

import java.util.ArrayList;

/**
 * Created by pimpu on 5/22/2017.
 */

public class DrawerMenuAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> dataName;
    private ArrayList<Integer> dataImage;
    private static LayoutInflater inflater=null;
    private InterfaceOnClickText interfaceOnClickText;

    public DrawerMenuAdapter(Activity activity, ArrayList<String> menuName, ArrayList<Integer> menuImage) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataImage = menuImage;
        dataName = menuName;

        interfaceOnClickText = (InterfaceOnClickText) activity;
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
        final String menuName = dataName.get(position);
        title.setText(menuName);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceOnClickText.onClickText(menuName);
            }
        });

        if ( dataImage.size() > 0 )
            thumb_image.setImageResource(dataImage.get(position));
        else
            thumb_image.setVisibility(View.GONE);

        return vi;
    }
}
