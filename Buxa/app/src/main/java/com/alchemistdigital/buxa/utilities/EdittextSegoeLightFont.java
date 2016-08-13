package com.alchemistdigital.buxa.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

/**
 * Created by user on 8/10/2016.
 */
public class EdittextSegoeLightFont extends TextInputEditText {
    public EdittextSegoeLightFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EdittextSegoeLightFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EdittextSegoeLightFont(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEUIL.TTF");
        setTypeface(tf ,1);
    }
}
