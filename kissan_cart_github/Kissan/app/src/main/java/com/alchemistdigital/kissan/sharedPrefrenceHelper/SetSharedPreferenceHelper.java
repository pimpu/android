package com.alchemistdigital.kissan.sharedPrefrenceHelper;

import android.content.Context;

/**
 * Created by user on 3/1/2016.
 */
public class SetSharedPreferenceHelper extends CommonSetPreference{
    private Context context;

    public SetSharedPreferenceHelper(Context context) {
        super(context);
        this.context = context;
    }

    public void setLoginPreference(String text,String value){
        setString(text,value);
    }

    public void setUserTypePreference(String text,String value){
        setString(text, value);
    }

    public void setEmailPreference(String text, String value){
        setString(text, value);
    }

    public void setNamePreference(String text, String value){
        setString(text, value);
    }

    public void setUserIdPreference(String text, int value){
        setInt(text, value);
    }

    public void setFilePathPreference(String text, String value){
        setString(text, value);
    }

    public void setBoolScanPicture(String text,boolean value){
        setBoolean(text, value);
    }

    public void setAdminUserId(String text,int value){
        setInt(text,value);
    }
}
