package com.botann.charing.pad.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by liushanguo on 2017/8/8.
 */

public class SGDeviceManager {



    /**
     * dp
     * @param context Context
     * @return dp
     */
    public static float SCREEN_WIDTH(Context context){
        WindowManager wm =  (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels / metric.density;
    }
//
//    public static float SCREEN_HEIGHT(Context context){
//        WindowManager wm =  (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//
//        DisplayMetrics metric = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(metric);
//        return
//    }

}
