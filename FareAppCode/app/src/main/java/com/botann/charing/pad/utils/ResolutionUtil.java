package com.botann.charing.pad.utils;

import android.content.Context;

/**
 * Created by wcc on 2016/5/6.
 */
public class ResolutionUtil {
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
