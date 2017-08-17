package com.rw.androidutils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;


/**
 * EmergencyResponse
 * <p>
 * Created by ravindu on 13/09/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */
@SuppressWarnings("WeakerAccess")
public abstract class CustomAlertDialog
{
    private int mTheme = -1;
    protected AlertDialog mDialog = null;
    protected Context mContext = null;
    private boolean mCancelable = true;
    private boolean mIsInitialized = false;
    private String mTitle = null;

    protected abstract View onCreateView(LayoutInflater inflater);

    public CustomAlertDialog(Context ctx, boolean cancelable)
    {
        mContext = ctx;
        mCancelable = cancelable;
    }

    public CustomAlertDialog(Context ctx, boolean cancelable, @StyleRes int theme)
    {
        mContext = ctx;
        mCancelable = cancelable;
        mTheme = theme;
    }

    public CustomAlertDialog(Context ctx, boolean cancelable, @Nullable String title)
    {
        mContext = ctx;
        mCancelable = cancelable;
        mTitle = title;
    }

    public CustomAlertDialog initialize()
    {
        init(mCancelable);

        mIsInitialized = true;
        return this;
    }

    private void init(boolean cancelable)
    {
        AlertDialog.Builder builder;

        if(mTheme != -1)
            builder = new AlertDialog.Builder(mContext, mTheme);
        else
            builder = new AlertDialog.Builder(mContext);

        builder.setCancelable(cancelable);

        builder.setTitle(mTitle);

        builder.setView(onCreateView(LayoutInflater.from(mContext)));

        mDialog = builder.create();
    }

    public void show()
    {
        if(!mIsInitialized)
            throw new RuntimeException("Dialog not initialized. Please call initialize() before show()");
        mDialog.show();
    }

    public void show(int width)
    {
        mDialog.show();
        setAlertDialogWidth(mDialog, width);
    }

    public void dismiss()
    {
        mDialog.dismiss();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener)
    {
        if(mDialog != null)
            mDialog.setOnDismissListener(dismissListener);
    }

    private void setAlertDialogWidth(AlertDialog dialog, int width)
    {
        if(dialog.isShowing())
        {
            if(dialog.getWindow() != null)
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = width;
            dialog.getWindow().setAttributes(lp);
        }
        else
            throw new UnsupportedOperationException("Dialog needs to be displayed before setting the width");
    }
}
