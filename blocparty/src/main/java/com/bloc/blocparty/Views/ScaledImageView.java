package com.bloc.blocparty.Views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *  Class makes images stretch to fit the screen
 */
public class ScaledImageView extends ImageView {

    public ScaledImageView(Context context) {
        super(context);
    }

    public ScaledImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaledImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Drawable drawable = getDrawable();

        if (drawable != null && drawable.getIntrinsicWidth() > 0) {

            int width =  MeasureSpec.getSize(widthMeasureSpec);
            int height = width * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();

            setMeasuredDimension(width, height);

        }
        else {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

}
