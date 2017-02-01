package com.cleanslatetech.floc.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.cleanslatetech.floc.R;

/**
 * Created by pimpu on 1/24/2017.
 */

public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;
    private final Context context;

    public TextDrawable(Context context, String text) {
        this.text = text;
        this.context = context;

        this.paint = new Paint();
        paint.setColor(this.context.getResources().getColor(R.color.darkYellow));
        paint.setTextSize(40f);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(6f, 0, 0, this.context.getResources().getColor(R.color.black_overlay));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(context.getResources().getColor(R.color.colorPrimaryDark));

        if(text.contains(" ")) {
            String[] splited = text.split("\\s+");
            int j = 0 ;
            for(int i = 0 ; i < splited.length ; i++) {
                canvas.drawText(splited[i], 140, 175+j, paint);
                j = j + 50;
            }
        }
        else {
            canvas.drawText(text, 140, 220, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
