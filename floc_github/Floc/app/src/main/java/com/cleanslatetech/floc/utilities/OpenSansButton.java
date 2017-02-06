package com.cleanslatetech.floc.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.cleanslatetech.floc.R;

/**
 * Created by pimpu on 2/6/2017.
 */

public class OpenSansButton extends AppCompatButton {

    private final static int SEGOE_REGULAR = 0;

    /**
     * List of created typefaces for later reused.
     */
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(2);
    OnClickListener _wrappedOnClickListener;

    public OpenSansButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        init(context, attrs);
    }

    public OpenSansButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);

        int typefaceValue = values.getInt(R.styleable.TypefacedTextView_OpenSans, 0);
        values.recycle();

        setTypeface(obtaintTypeface(context, typefaceValue));
    }

    private Typeface obtaintTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = mTypefaces.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    private Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface;
        switch (typefaceValue) {

            case SEGOE_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                break;

            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }

}
