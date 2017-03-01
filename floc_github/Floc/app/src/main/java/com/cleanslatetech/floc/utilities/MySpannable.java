package com.cleanslatetech.floc.utilities;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by pimpu on 2/17/2017.
 */

public class MySpannable extends ClickableSpan {

    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#f36d21"));
    }

    @Override
    public void onClick(View widget) {


    }
}