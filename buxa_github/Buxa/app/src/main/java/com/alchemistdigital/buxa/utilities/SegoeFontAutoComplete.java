package com.alchemistdigital.buxa.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.AutoCompleteTextView;

import com.alchemistdigital.buxa.R;

/**
 * Created by user on 8/26/2016.
 */
public class SegoeFontAutoComplete extends AutoCompleteTextView {
    private final static int SEGOE_LIGHT = 0;
    private final static int SEGOE_REGULAR = 1;

    /**
     * List of created typefaces for later reused.
     */
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(2);

    public SegoeFontAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        init(context, attrs);
    }

    public SegoeFontAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);

        int typefaceValue = values.getInt(R.styleable.TypefacedTextView_Segoe, 0);
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
            case SEGOE_LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SEGOEUIL.TTF");
                break;

            case SEGOE_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SEGOEUI.TTF");
                break;

            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }

}
