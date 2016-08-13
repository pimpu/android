package com.alchemistdigital.buxa.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 8/3/2016.
 */
public class SegoeLightFontCustomTextview extends TextView {
    public SegoeLightFontCustomTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SegoeLightFontCustomTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SegoeLightFontCustomTextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEUIL.TTF");
        setTypeface(tf ,1);
    }
}
