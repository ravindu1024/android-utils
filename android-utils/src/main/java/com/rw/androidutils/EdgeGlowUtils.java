package com.rw.androidutils;

import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Utilities
 * <p>
 * Created by ravindu on 26/07/16.
 * Copyright © 2016 Vortilla. All rights reserved.
 */
public class EdgeGlowUtils
{
    public static void setEdgeGlowColor(ViewPager viewPager, int color)
    {
        try
        {
            Class<?> clazz = ViewPager.class;
            for (String name : new String[]{
                    "mLeftEdge", "mRightEdge"
            })
            {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                Object edge = field.get(viewPager); // android.support.v4.widget.EdgeEffectCompat
                Field fEdgeEffect = edge.getClass().getDeclaredField("mEdgeEffect");
                fEdgeEffect.setAccessible(true);
                setEdgeEffectColor((EdgeEffect) fEdgeEffect.get(edge), color);
            }
        }
        catch (Exception ignored)
        {
        }
    }


    private static void setEdgeEffectColor(EdgeEffect edgeEffect, int color)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                edgeEffect.setColor(color);
                return;
            }
            Field edgeField = EdgeEffect.class.getDeclaredField("mEdge");
            Field glowField = EdgeEffect.class.getDeclaredField("mGlow");
            edgeField.setAccessible(true);
            glowField.setAccessible(true);
            Drawable mEdge = (Drawable) edgeField.get(edgeEffect);
            Drawable mGlow = (Drawable) glowField.get(edgeEffect);
            mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            mEdge.setCallback(null); // free up any references
            mGlow.setCallback(null); // free up any references
        }
        catch (Exception ignored)
        {
        }
    }

    private static final Class<?> CLASS_SCROLL_VIEW = ScrollView.class;
    private static final Field SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    private static final Field SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static final Class<?> CLASS_LIST_VIEW = AbsListView.class;
    private static final Field LIST_VIEW_FIELD_EDGE_GLOW_TOP;
    private static final Field LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    static {
        Field edgeGlowTop = null, edgeGlowBottom = null;

        for (Field f : CLASS_SCROLL_VIEW.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    edgeGlowTop = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    edgeGlowBottom = f;
                    break;
            }
        }

        SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = edgeGlowTop;
        SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = edgeGlowBottom;

        for (Field f : CLASS_LIST_VIEW.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    edgeGlowTop = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    edgeGlowBottom = f;
                    break;
            }
        }

        LIST_VIEW_FIELD_EDGE_GLOW_TOP = edgeGlowTop;
        LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = edgeGlowBottom;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEdgeGlowColor(AbsListView listView, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                EdgeEffect ee;
                ee = (EdgeEffect) LIST_VIEW_FIELD_EDGE_GLOW_TOP.get(listView);
                ee.setColor(color);
                ee = (EdgeEffect) LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(listView);
                ee.setColor(color);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEdgeGlowColor(ScrollView scrollView, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                EdgeEffect ee;
                ee = (EdgeEffect) SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
                ee.setColor(color);
                ee = (EdgeEffect) SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
                ee.setColor(color);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
