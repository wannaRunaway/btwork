package com.kulun.energynet.utils;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * created by xuedi on 2019/5/23
 */
public class SharePref {
    public static final String FILE_NAME = "share_data";
    /**
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if(object instanceof String){
            edit.putString(key,(String)object);
        }else if(object instanceof Integer){
            edit.putInt(key,(Integer)object);
        }else if(object instanceof Boolean){
            edit.putBoolean(key,(Boolean)object);
        }else if(object instanceof Float){
            edit.putFloat(key,(Float)object);
        }else if(object instanceof Long){
            edit.putLong(key,(Long)object);
        }else{
            edit.putString(key,object.toString());
        }
        edit.apply();
    }
    /**
     * 根据默认值得到类型
     * @param context
     * @param key
     * @param defaultObject
     */
    public static Object get(Context context,String key,Object defaultObject){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        if(defaultObject instanceof String){
            return sharedPreferences.getString(key,(String) defaultObject);
        }else if(defaultObject instanceof Integer){
            return sharedPreferences.getInt(key,(Integer)defaultObject);
        }else if(defaultObject instanceof Boolean){
            return sharedPreferences.getBoolean(key,(Boolean)defaultObject);
        }else if(defaultObject instanceof Float){
            return sharedPreferences.getFloat(key,(Float)defaultObject);
        }else if(defaultObject instanceof Long){
            return sharedPreferences.getLong(key,(Long)defaultObject);
        }
        return  null;
    }
    /**
     * 清除所有数据
     * @param context
     */
    public static void clear(Context context){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
