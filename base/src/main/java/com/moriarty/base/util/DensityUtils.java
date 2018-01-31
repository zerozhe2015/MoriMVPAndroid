package com.moriarty.base.util;

import android.content.Context;
import android.util.TypedValue;

import com.moriarty.base.BaseApplication;


/**
 * 常用单位转换的辅助类
 *
 * @author zhy
 */
public class DensityUtils {
    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics()) + 0.5);
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics()) + 0.5);
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        return dp2px(BaseApplication.getApplication(), dpVal);
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        return sp2px(BaseApplication.getApplication(), spVal);
    }

    /**
     * px转dp
     */
    public static float px2dp(float pxVal) {
        return px2dp(BaseApplication.getApplication(), pxVal);
    }

    /**
     * px转sp
     */
    public static float px2sp(float pxVal) {
        return px2sp(BaseApplication.getApplication(), pxVal);
    }

}
