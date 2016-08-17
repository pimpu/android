package com.alchemistdigital.buxa.sharedprefrencehelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.alchemistdigital.buxa.R;

/**
 * Created by user on 8/2/2016.
 */
public class GetSharedPreference {
    Context context;
    private SharedPreferences sharedPreference ;

    public GetSharedPreference(Context context) {
        this.context = context;
        sharedPreference =
                context.getSharedPreferences(context.getResources().getString(R.string.sharedPrefrence), 0);
    }

    public Boolean getLoginPreference(String text){
        return sharedPreference.getBoolean(text,false);
    }
}
