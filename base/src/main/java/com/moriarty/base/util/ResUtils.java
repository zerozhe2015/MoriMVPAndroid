package com.moriarty.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.moriarty.base.BaseApplication;


public class ResUtils {


    /**
     * 返回drawable string
     *
     * @param resId
     * @return
     */
    public static Drawable getDrawableRes(int resId) {
        Context context = BaseApplication.getApplication();

        return context.getResources().getDrawable(resId);
    }

    public static Drawable getDrawableRes(Context context, int resId) {

        return context.getResources().getDrawable(resId);
    }

    /**
     * 返回resource string
     *
     * @param resId
     * @return
     */
    public static String getStringRes(int resId) {
        Context context = BaseApplication.getApplication();
        return context.getString(resId);
    }

    public static String getStringRes(Context context, int resId) {
        return context.getString(resId);
    }

    public static String getStringRes(int resId, Object... formatArgs) {
        Context context = BaseApplication.getApplication();
        return context.getString(resId, formatArgs);
    }

    public static String getStringRes(Context context, int resId, Object... formatArgs) {
        return context.getString(resId, formatArgs);
    }

    public static int getDimensionPixelOffset(int resId) {
        Context context = BaseApplication.getApplication();
        return context.getResources().getDimensionPixelOffset(resId);
    }

    public static int getDimensionPixelOffset(Context context, int resId) {
        return context.getResources().getDimensionPixelOffset(resId);
    }

    public static int getDimensionPixelSize(int resId) {
        Context context = BaseApplication.getApplication();
        return context.getResources().getDimensionPixelSize(resId);
    }

    public static int getDimensionPixelSize(Context context, int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }


    public static int getColorRes(int resId) {
        Context context = BaseApplication.getApplication();
        return context.getResources().getColor(resId);
    }

    public static int getColorRes(Context context, int resId) {
        return context.getResources().getColor(resId);
    }



}
