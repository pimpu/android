package com.cleanslatetech.floc.utilities;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cleanslatetech.floc.R;

/**
 * Created by pimpu on 2/21/2017.
 */
public class HandleExpandLayoutMethods {
    public static void slide_down(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
}
