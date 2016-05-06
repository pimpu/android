package com.alchemistdigital.ziko.sharedPrefrenceHelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.alchemistdigital.ziko.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 5/4/2016.
 */
public class SetSharedPreference {
    Context context;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    public SetSharedPreference(Context context){
        sharedPreference = context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        this.context = context;
    }

    public void setFileDirectoryName(ArrayList<String> lists) {
        Set<String> set = new HashSet<String>();
        set.addAll(lists);
        editor.putStringSet("mp4ContainsDirectory", set);
        editor.commit();
    }

    public void setVideoDuration(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
}
