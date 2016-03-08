package com.alchemistdigital.kissan.utilities;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by user on 2/25/2016.
 */
public class Validations {

    public static boolean emailValidate(String target){
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isEmptyString(String target){
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return true;
        }
    }

    public static  boolean phoneValiate(String target){
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }

}
