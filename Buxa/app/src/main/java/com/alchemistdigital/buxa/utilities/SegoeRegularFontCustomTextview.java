package com.alchemistdigital.buxa.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 8/2/2016.
 */
public class SegoeRegularFontCustomTextview extends TextView{
    public SegoeRegularFontCustomTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SegoeRegularFontCustomTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SegoeRegularFontCustomTextview(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEUI.TTF");
        setTypeface(tf ,1);
    }
}
