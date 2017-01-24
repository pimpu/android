package com.cleanslatetech.floc.sharedprefrencehelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.cleanslatetech.floc.R;

/**
 * Created by pimpu on 1/20/2017.
 */

public class SetSharedPreference {
    Context context;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    public SetSharedPreference(Context context){
        sharedPreference =
                context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
        this.context = context;
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setInt(String key, int id) {
        editor.putInt(key, id);
        editor.commit();
    }

    public void setBoolean(String key, boolean val) {
        editor.putBoolean(key, val);
        editor.commit();
    }

}
