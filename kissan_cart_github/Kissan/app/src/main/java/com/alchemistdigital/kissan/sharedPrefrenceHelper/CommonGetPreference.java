package com.alchemistdigital.kissan.sharedPrefrenceHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.alchemistdigital.kissan.R;

/**
 * Created by user on 3/1/2016.
 */
public class CommonGetPreference {
    private SharedPreferences sharedPreference;

    public CommonGetPreference(Context context){
        sharedPreference = context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), 0);
    }

    public String getString(String key){
        return sharedPreference.getString(key, "");
    }

    public int getInt(String key){
        return sharedPreference.getInt(key, 0);
    }

    public Boolean getBoolean(String key){
        return sharedPreference.getBoolean(key,false);
    }

    public Float getFloat(String key){
        return sharedPreference.getFloat(key, 0);
    }

    public long getLong(String key){
        return sharedPreference.getLong(key,0);
    }
}
