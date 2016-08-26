package com.alchemistdigital.buxa.sharedprefrencehelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.alchemistdigital.buxa.R;

/**
 * Created by user on 8/16/2016.
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

    public void setBooleanLogin(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setLoginId(String key, int id) {
        editor.putInt(key, id);
        editor.commit();
    }

    public void setApiKey(String key, String api_key) {
        editor.putString(key, api_key);
        editor.commit();
    }

    public void setLoginEmail(String key, String email) {
        editor.putString(key, email);
        editor.commit();
    }

    public void setLoginName(String key, String loginName) {
        editor.putString(key, loginName);
        editor.commit();

    }

    public void setCompanyName(String key, String companyName) {
        editor.putString(key, companyName);
        editor.commit();
    }
}
