package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.BaseAppCompactActivity;
import com.cleanslatetech.floc.models.SubMenuModels;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.interfaces.InterfaceRightMenuClick;

import java.util.ArrayList;

/**
 * Created by pimpu on 2/15/2017.
 */
public class CustomSubmenuAdapter extends ArrayAdapter {
    private Context mContext;
    private PopupWindow subMenuPopup;
    private String menuName;
    private ArrayList<SubMenuModels> arraySubMenuName;
    String selectedText = null;
    public InterfaceRightMenuClick interfaceRightMenuClick;

    // View lookup cache
    private class SubMenuViewHolder {
        AppCompatTextView txtSubName;
    }

    public CustomSubmenuAdapter(String menuName, ArrayList<SubMenuModels> arraySubMenuName, Context mContext, PopupWindow subMenuPopup) {
        super(mContext, R.layout.submenu_row_item, arraySubMenuName);
        this.mContext=mContext;
        this.subMenuPopup=subMenuPopup;
        this.menuName=menuName;
        this.arraySubMenuName=arraySubMenuName;

        interfaceRightMenuClick = (BaseAppCompactActivity) mContext;

        // add header for submenu listview
        // without if, item repetedly aaded to 0th position
        if(!arraySubMenuName.get(0).getSubMenuName().equals(menuName))
            arraySubMenuName.add(0, new SubMenuModels(menuName));

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        final SubMenuViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new SubMenuViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.submenu_row_item, parent, false);
            viewHolder.txtSubName = (AppCompatTextView) convertView.findViewById(R.id.txtSubMenuName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SubMenuViewHolder) convertView.getTag();
        }

        viewHolder.txtSubName.setText(arraySubMenuName.get(position).getSubMenuName());

        final String previousSelectedTxt = new GetSharedPreference(mContext).getString(mContext.getResources().getString(R.string.shrdSelectedSubmenu));

        if(position !=0 && arraySubMenuName.get(position).getSubMenuName().equals(selectedText) ||
                arraySubMenuName.get(position).getSubMenuName().equals(previousSelectedTxt) ) {

            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.dark_blue));
        }
        else if(position !=0 && !arraySubMenuName.get(position).getSubMenuName().equals(selectedText)) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        if(position == 0) {
            viewHolder.txtSubName.setTextColor(mContext.getResources().getColor(R.color.darkGray));
            viewHolder.txtSubName.setTextSize(18);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubMenuViewHolder tag = (SubMenuViewHolder) v.getTag();

                selectedText = tag.txtSubName.getText().toString();
                new SetSharedPreference(mContext).setString(mContext.getResources().getString(R.string.shrdSelectedSubmenu), selectedText);
                subMenuPopup.dismiss();

                interfaceRightMenuClick.getSubmenuClick(selectedText);

            }
        });

        viewHolder.txtSubName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedText = viewHolder.txtSubName.getText().toString();
                new SetSharedPreference(mContext).setString(mContext.getResources().getString(R.string.shrdSelectedSubmenu), selectedText);
                subMenuPopup.dismiss();

                interfaceRightMenuClick.getSubmenuClick(selectedText);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return arraySubMenuName.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
