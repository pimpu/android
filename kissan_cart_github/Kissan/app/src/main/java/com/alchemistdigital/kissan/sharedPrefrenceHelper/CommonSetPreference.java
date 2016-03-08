package com.alchemistdigital.kissan.sharedPrefrenceHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.alchemistdigital.kissan.R;

/**
 * Created by user on 3/1/2016.
 */
public class CommonSetPreference {
    Context context;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    public CommonSetPreference(Context context){
        sharedPreference = context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), 0);
        editor = sharedPreference.edit();
        this.context = context;
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void setBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setFloat(String key, Float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void setLong(String key, long value) {
        editor.putLong(key,value);
        editor.commit();
    }
}
