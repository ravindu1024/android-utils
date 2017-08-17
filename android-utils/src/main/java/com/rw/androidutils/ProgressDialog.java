package com.rw.androidutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Utilities
 * <p>
 * Created by ravindu on 25/11/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */

public class ProgressDialog
{
    private Context context;
    private AlertDialog dialog;

    private RelativeLayout mContainer;
    private TextView mRightText;
    private ProgressBar mProgress;

    private String mProgressMessage = "";
    private TextLocation location = TextLocation.Right;

    public enum TextLocation
    {
        Right, Bottom
    }

    public ProgressDialog(Context ctx)
    {
        context = ctx;

        init();
    }

    public ProgressDialog(Context ctx, String mProgressMessage)
    {
        context = ctx;
        this.mProgressMessage = mProgressMessage;

        init();

        setTextLocation();
    }

    private void init()
    {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("");
        b.setCancelable(false);

        @SuppressLint("InflateParams") View v = LayoutInflater.from(context).inflate(R.layout.commonutils_dialog_progress, null);

        mRightText = (TextView)v.findViewById(R.id.commonutils_dialog_progress_textView);
        mProgress = (ProgressBar)v.findViewById(R.id.commonutils_dialog_progress_bar);
        mContainer = (RelativeLayout)v.findViewById(R.id.commonutils_dialog_container);

        setBackgroundColor(Color.WHITE);

        mRightText.setText(mProgressMessage);

        b.setView(v);

        dialog = b.create();

    }

    public void setBackgroundColor(int color)
    {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.rounded_rect_white);
        background.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.ADD));

        mContainer.setBackground(background);
    }

    public void setColors(int progressColor, int backgroundColor, int textColor)
    {
        setProgressColor(progressColor);
        setBackgroundColor(backgroundColor);
        setTextColor(textColor);
    }

    public void setTextColor(int color)
    {
        mRightText.setTextColor(color);
    }

    public void setProgressColor(int color)
    {
        Drawable spinner = mProgress.getIndeterminateDrawable();
        spinner.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
    }

    public void setMessage(String message)
    {
        this.mProgressMessage = message;

        setTextLocation();
    }

    public void setMessage(String message, TextLocation location)
    {
        this.location = location;
        this.mProgressMessage = message;

        setTextLocation();
    }

    public void show()
    {
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(null);
    }


    private void setTextLocation()
    {
        mRightText.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams lpText = (RelativeLayout.LayoutParams) mRightText.getLayoutParams();
        lpText.removeRule(RelativeLayout.END_OF);
        lpText.removeRule(RelativeLayout.BELOW);
        lpText.topMargin = 0;
        lpText.leftMargin = 0;
        lpText.removeRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY);

        RelativeLayout.LayoutParams lpProg = (RelativeLayout.LayoutParams) mProgress.getLayoutParams();
        lpProg.removeRule(RelativeLayout.CENTER_HORIZONTAL);

        float density = context.getResources().getDisplayMetrics().density;

        if(location == TextLocation.Right)
        {
            lpText.addRule(RelativeLayout.END_OF, R.id.commonutils_dialog_progress_bar);
            lpText.leftMargin = (int) (5 * density);
            lpText.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, Gravity.LEFT);
        }
        else
        {
            lpText.addRule(RelativeLayout.BELOW, R.id.commonutils_dialog_progress_bar);
            lpText.topMargin = (int) (5 * density);
            lpText.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, Gravity.CENTER_HORIZONTAL);

            lpProg.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }

        mProgress.setLayoutParams(lpProg);
        mRightText.setLayoutParams(lpText);

        mRightText.setText(mProgressMessage);
    }
}
