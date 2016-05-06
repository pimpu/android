package com.alchemistdigital.ziko.sharedPrefrenceHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.alchemistdigital.ziko.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by user on 5/4/2016.
 */
public class GetSharedPreference {
    private SharedPreferences sharedPreference;

    public GetSharedPreference(Context context){
        sharedPreference = context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), context.MODE_PRIVATE);
    }

    public ArrayList<String> getFileDirectoryName() {
        Set<String> set = sharedPreference.getStringSet("mp4ContainsDirectory", null);
        if(set == null) {
            return null;
        }
        return  new ArrayList<String>(set);
    }

    public String getVideoDuration(String key) {
        return sharedPreference.getString(key, null);
    }
}
