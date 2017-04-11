package com.cleanslatetech.floc.sharedprefrencehelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.cleanslatetech.floc.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pimpu on 1/20/2017.
 */

public class GetSharedPreference {
    Context context;
    private SharedPreferences sharedPreference ;

    public GetSharedPreference(Context context) {
        this.context = context;
        sharedPreference =
                context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), Context.MODE_PRIVATE);
    }

    public String getString(String text) {
        return sharedPreference.getString(text,null);
    }

    public int getInt(String text) {
        return sharedPreference.getInt(text,0);
    }

    public boolean getBoolean(String text) {
        return sharedPreference.getBoolean(text,false);
    }

    public Set<String> getStringSet(String text) {
        return sharedPreference.getStringSet(text, null);
    }
}
