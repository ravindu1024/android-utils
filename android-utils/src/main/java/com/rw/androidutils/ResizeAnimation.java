package com.rw.androidutils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * OpDocs
 * <p>
 * Created by ravindu on 31/08/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */
public class ResizeAnimation extends Animation
{

    private int startWidth;
    private int deltaWidth; // distance between start and end height
    private int startHeight;
    private int deltaHeight;

    private int originalWidth = 0;
    private int originalHeight = 0;
    private View view;

    private boolean fillEnabled = false;


    public ResizeAnimation(View v, int startW, int endW, int startH, int endH)
    {
        view = v;
        startWidth = startW;
        deltaWidth = endW - startW;

        startHeight = startH;
        deltaHeight = endH - startH;

        originalHeight = v.getHeight();
        originalWidth = v.getWidth();
    }

    @Override
    public void setFillEnabled(boolean enabled)
    {
        fillEnabled = enabled;
        super.setFillEnabled(enabled);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        if(interpolatedTime == 1.0 && !fillEnabled)
        {
            view.getLayoutParams().height = originalHeight;
            view.getLayoutParams().width = originalWidth;
        }
        else
        {

            if(deltaHeight != 0)
                view.getLayoutParams().height = (int) (startHeight + deltaHeight * interpolatedTime);
            if(deltaWidth != 0)
                view.getLayoutParams().width = (int) (startWidth + deltaWidth * interpolatedTime);

        }

        view.requestLayout();
    }
}
