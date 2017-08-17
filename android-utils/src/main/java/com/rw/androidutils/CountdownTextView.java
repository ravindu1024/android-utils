package com.rw.androidutils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

/**
 * Intavu
 * <p/>
 * Created by ravindu on 25/07/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */
public class CountdownTextView<T extends TextView> implements Animation.AnimationListener
{
    private RelativeLayout mLayout = null;
    private TextView mText = null;
    private AnimationSet mAnimationSet = null;
    private int mCount = 0;
    private ViewGroup mParentRootView = null;
    private OnCountdownCompletionListener mListener = null;
    private AlphaAnimation mRemoveAnimation = null;

    private boolean isAborted = false;



    public CountdownTextView(Context context, float maxScale, long animationTime, Class<T> type, int backgroundColor)
    {
        try
        {
            mText = type.getDeclaredConstructor(Context.class).newInstance(context);
        }
        catch (InstantiationException e)
        {
            Log.e("Countdown InstantiationException");
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            Log.e("Countdown textview IllegalAccessException");
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            Log.e("Countdown textview InvocationTargetException");
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            Log.e("Countdown textview NoSuchMethodException");
            e.printStackTrace();
        }

        mText.setTypeface(null, Typeface.BOLD);
        mLayout = new RelativeLayout(context);


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);





        mLayout.setBackgroundColor(backgroundColor);
        mText.setTextColor(Color.WHITE);
        mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 130);

        mText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mLayout.addView(mText, lp);

        ScaleAnimation sc = new ScaleAnimation(maxScale, 1f, maxScale, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sc.setDuration(animationTime);

        AlphaAnimation al = new AlphaAnimation(0.7f, 1.0f);
        al.setDuration(animationTime);

        mAnimationSet = new AnimationSet(true);
        mAnimationSet.addAnimation(sc);
        mAnimationSet.addAnimation(al);
        mAnimationSet.setFillEnabled(true);
        mAnimationSet.setFillAfter(true);
        mAnimationSet.setAnimationListener(this);

        mRemoveAnimation = new AlphaAnimation(1, 0);
        mRemoveAnimation.setDuration(200);
        mRemoveAnimation.setAnimationListener(this);
    }

    public void setCompletionListener(OnCountdownCompletionListener listener)
    {
        mListener = listener;
    }

    public void stop()
    {
        isAborted = true;
        mLayout.clearAnimation();

        if(mParentRootView != null)
        {
            mParentRootView.removeView(mLayout);
        }
    }

    public void start(ViewGroup root, int number)
    {
        isAborted = false;
        mCount = number;
        mParentRootView = root;

        root.removeView(mLayout);
        root.addView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        Utilities.executeScheduledTaskFixedDelay(1, 0, 1000, new Utilities.ScheduledTaskListener()
        {
            @Override
            public boolean onScheduledTask()
            {
                if(isAborted)
                    return false;   //forceful stop

                if (mCount == 0)
                {
                    mLayout.startAnimation(mRemoveAnimation);
                    return false;
                }
                else
                {

                    mText.setText(String.valueOf(mCount--));
                    mText.startAnimation(mAnimationSet);
                }
                return true;
            }
        }, true);
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        if (animation == mRemoveAnimation)
        {
            mParentRootView.removeView(mLayout);

            if (mListener != null)
                mListener.onCountdownComplete();

            mParentRootView = null;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {
    }

    public interface OnCountdownCompletionListener
    {
        void onCountdownComplete();
    }
}
