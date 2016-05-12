package com.alchemistdigital.ziko.utilities;

import android.content.Context;

/**
 * Created by user on 4/29/2016.
 */
public class CommonMethods {
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
