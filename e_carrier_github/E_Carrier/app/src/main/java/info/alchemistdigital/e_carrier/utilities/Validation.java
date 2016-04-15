package info.alchemistdigital.e_carrier.utilities;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by user on 12/19/2015.
 */
public class Validation {

    public static boolean emailValidate(String text){
        if (TextUtils.isEmpty(text)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
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
