package com.alchemistdigital.buxa.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.buxa.R;

/**
 * Created by user on 8/3/2016.
 */
public class SegoeFontTextView extends TextView implements View.OnClickListener {

    OnClickListener _wrappedOnClickListener;

    private final static int SEGOE_LIGHT = 0;
    private final static int SEGOE_REGULAR = 1;
    /**
     * List of created typefaces for later reused.
     */
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(2);

    public SegoeFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnClickListener(this);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        init(context, attrs);
    }

    public SegoeFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        if (_wrappedOnClickListener != null)
            _wrappedOnClickListener.onClick(view);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        _wrappedOnClickListener = l;
    }
}
