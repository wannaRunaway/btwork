package com.botann.charing.pad.utils;

import android.content.Context;
import android.content.SharedPreferences;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liushanguo on 2017/6/12.
 */

public class SharedPreferencesUtil {

    private static final String GlobalDataKey = "data";

    public static void save(Context context,String key, String value){
        SharedPreferences pref = context.getSharedPreferences(GlobalDataKey,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(GlobalDataKey,MODE_PRIVATE);
    }
}
