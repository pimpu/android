package com.alchemistdigital.buxa.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by user on 8/26/2016.
 */
public class AutoCompleteSegoeLightFont extends AutoCompleteTextView {
    public AutoCompleteSegoeLightFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AutoCompleteSegoeLightFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoCompleteSegoeLightFont(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEUIL.TTF");
        setTypeface(tf, 1);
    }
}
