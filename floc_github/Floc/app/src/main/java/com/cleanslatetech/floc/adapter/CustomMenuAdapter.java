package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.BaseAppCompactActivity;
import com.cleanslatetech.floc.models.MenuModel;
import com.cleanslatetech.floc.models.SubMenuModels;
import com.cleanslatetech.floc.sharedprefrencehelper.GetSharedPreference;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.interfaces.InterfaceRightMenuClick;

import java.util.ArrayList;

/**
 * Created by pimpu on 2/14/2017.
 */

public class CustomMenuAdapter extends ArrayAdapter {
    private String texta = null;
    private Context mContext;
    private PopupWindow popupWindow;
    private View appBarRight;
    private ArrayList<MenuModel> menuData;
    public InterfaceRightMenuClick interfaceRightMenuClick;

    // View lookup cache
    private class ViewHolder {
        AppCompatTextView txtName;
        ImageView subMenuOptionIcon;
    }

    public CustomMenuAdapter(ArrayList<MenuModel> data, Context context, PopupWindow popupWindow, View viewById) {
        super(context, R.layout.menu_row_item, data);
        this.mContext=context;
        this.popupWindow=popupWindow;
        this.appBarRight=viewById;
        this.menuData=data;

        interfaceRightMenuClick = (BaseAppCompactActivity) mContext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.menu_row_item, parent, false);
            viewHolder.txtName = (AppCompatTextView) convertView.findViewById(R.id.txtMenuName);
            viewHolder.subMenuOptionIcon = (ImageView) convertView.findViewById(R.id.subMenuOptionIcon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.txtName.setText(menuData.get(position).getMenuName());

        if(menuData.get(position).getArrayListSubMenuName() != null ) {
            viewHolder.subMenuOptionIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.subMenuOptionIcon.setVisibility(View.GONE);
        }

        final String previousSelectedTxt = new GetSharedPreference(mContext).getString(mContext.getResources().getString(R.string.shrdSelectedMenu));
        if( previousSelectedTxt != null && menuData.get(position).getMenuName().equals(previousSelectedTxt) ) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.dark_blue));
        }
        else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder tag = (ViewHolder) v.getTag();

                texta = tag.txtName.getText().toString();
                popupWindow.dismiss();
                // for single menu click
                interfaceRightMenuClick.getSubmenuClick(texta);

                new SetSharedPreference(mContext).setString(mContext.getResources().getString(R.string.shrdSelectedMenu), texta);
                if(menuData.get(position).getArrayListSubMenuName() != null ) {
                    createSubMenuPopup(menuData.get(position).getArrayListSubMenuName(), menuData.get(position).getMenuName());
                }

            }
        });

        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texta = viewHolder.txtName.getText().toString();
                popupWindow.dismiss();
                // for single menu click
                interfaceRightMenuClick.getSubmenuClick(texta);

                new SetSharedPreference(mContext).setString(mContext.getResources().getString(R.string.shrdSelectedMenu), texta);
                if(menuData.get(position).getArrayListSubMenuName() != null ) {
                    createSubMenuPopup(menuData.get(position).getArrayListSubMenuName(), menuData.get(position).getMenuName());
                }

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private void createSubMenuPopup(ArrayList<SubMenuModels> sumenuData, String menuname) {

        PopupWindow subMenuPopup = new PopupWindow(mContext);

        // the drop down list is a list view
        ListView listViewSub = new ListView(mContext);
        CustomSubmenuAdapter subMenuAdapter = new CustomSubmenuAdapter(menuname, sumenuData, mContext, subMenuPopup);

        // set our adapter and pass our pop up window contents
        listViewSub.setAdapter(subMenuAdapter);
        listViewSub.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        // some other visual settings for popup window
        subMenuPopup.setFocusable(true);
        subMenuPopup.setWidth(600);
        subMenuPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        subMenuPopup.setOutsideTouchable(true);

        // set the listview as popup content
        subMenuPopup.setContentView(listViewSub);

        subMenuPopup.showAsDropDown(appBarRight);
    }

    @Override
    public int getCount() {
        return menuData.size();
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
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
