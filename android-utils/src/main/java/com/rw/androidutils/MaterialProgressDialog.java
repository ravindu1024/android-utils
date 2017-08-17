package com.rw.androidutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * Utilities
 * <p/>
 * Created by ravindu on 20/07/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */
public class MaterialProgressDialog
{
    private AlertDialog mDialog = null;
    private Context mContext = null;

    private String mDialogText = "";
    private int mProgressColor = 0;

    private TextView mTitle = null;
    private TextView mMessage = null;
    private ProgressWheel mProgress = null;
    private String mTitleText = "";

    public MaterialProgressDialog(Context context)
    {
        mContext = context;
        mProgressColor = ContextCompat.getColor(mContext, R.color.colorProgress);

        initDialog();
    }

    public MaterialProgressDialog(Context context, String message)
    {
        mContext = context;
        mDialogText = message;
        mProgressColor = ContextCompat.getColor(mContext, R.color.colorProgress);

        initDialog();
    }

    public MaterialProgressDialog(Context context, String message, int progressColor)
    {
        mContext = context;
        mDialogText = message;
        mProgressColor = progressColor;

        initDialog();
    }

    public void setMessage(String message)
    {
        mDialogText = message;
        mMessage.setText(message);
    }


    public void setTitle(String title)
    {
        mTitleText = title;
        mTitle.setText(title);
    }

    public void setProgressColor(int progressColor)
    {
        this.mProgressColor = progressColor;
        mProgress.setBarColor(mProgressColor);
    }

    public void setCancellable(boolean cancellable)
    {
        mDialog.setCancelable(cancellable);
    }


    public void show()
    {
        if(mTitleText.length() == 0)
            mTitle.setVisibility(View.GONE);

        mDialog.show();
    }

    public void dismiss()
    {
        mDialog.dismiss();
    }

    private void initDialog()
    {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.progressdialog, null);

        mProgress = (ProgressWheel) v.findViewById(R.id.dialog_progress_indicator);
        mTitle = (TextView) v.findViewById(R.id.progress_text_title);
        mMessage = (TextView) v.findViewById(R.id.progress_text_message);

        mProgress.setBarColor(mProgressColor);
        mMessage.setText(mDialogText);
        mTitle.setText(mTitleText);

        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
        b.setView(v);
        b.setCancelable(false);

        mDialog = b.create();

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


}
