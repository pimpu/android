package com.alchemistdigital.kissan.sharedPrefrenceHelper;

import android.content.Context;

/**
 * Created by user on 3/1/2016.
 */
public class GetSharedPreferenceHelper extends CommonGetPreference {
    Context context;
    public GetSharedPreferenceHelper(Context context) {
        super(context);
        this.context = context;
    }

    public String getLoginPreference(String text){
        return getString(text);
    }

    public String getUserTypePreference(String text){
        return getString(text);
    }

    public String getEmailPreference(String text){
        return getString(text);
    }

    public String getNamePreference(String text){
        return getString(text);
    }

    public int getUserIdPreference(String text){
        return getInt(text);
    }

    public String getFilePathPreference(String text){
        return getString(text);
    }

    public boolean getBoolScanPicture(String text){
        return getBoolean(text);
    }

    public int getAdminUserId(String text){
        return getInt(text);
    }
}
