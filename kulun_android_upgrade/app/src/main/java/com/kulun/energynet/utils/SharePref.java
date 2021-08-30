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
    public static final String ONLYONE = "only_one";

    /**
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        try {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            if (object instanceof String) {
                edit.putString(key, (String) object);
            } else if (object instanceof Integer) {
                edit.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                edit.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                edit.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                edit.putLong(key, (Long) object);
            } else {
                edit.putString(key, object.toString());
            }
            edit.apply();
        } catch (Exception e) {

        }
    }

    /**
     * @param context
     * @param key
     * @param object
     */
    public static void oneput(Context context, String key, Object object) {
        try {
            SharedPreferences sp = context.getSharedPreferences(ONLYONE, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            if (object instanceof String) {
                edit.putString(key, (String) object);
            } else if (object instanceof Integer) {
                edit.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                edit.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                edit.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                edit.putLong(key, (Long) object);
            } else {
                edit.putString(key, object.toString());
            }
            edit.apply();
        } catch (Exception e) {
        }
    }

    /**
     * 根据默认值得到类型
     *
     * @param context
     * @param key
     * @param defaultObject
     */
    public static Object oneget(Context context, String key, Object defaultObject) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ONLYONE, Context.MODE_PRIVATE);
            if (defaultObject instanceof String) {
                return sharedPreferences.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sharedPreferences.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sharedPreferences.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sharedPreferences.getLong(key, (Long) defaultObject);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据默认值得到类型
     *
     * @param context
     * @param key
     * @param defaultObject
     */
    public static Object get(Context context, String key, Object defaultObject) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            if (defaultObject instanceof String) {
                return sharedPreferences.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sharedPreferences.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sharedPreferences.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sharedPreferences.getLong(key, (Long) defaultObject);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 移除某个数据
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
        }catch (Exception e){
        }
    }

    /**
     * 查询某条记录是否存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean icContains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    private static class SharePrefencesCompat {
        private static Method sApplyMethod = null;

        static {
            try {
                sApplyMethod = findApplyMethod();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        private static Method findApplyMethod() throws NoSuchMethodException {
            Class clz = SharedPreferences.Editor.class;
            return clz.getMethod("apply");
        }

        public static void apply(SharedPreferences.Editor editor) throws InvocationTargetException, IllegalAccessException {
            if (sApplyMethod != null) {
                sApplyMethod.invoke(editor);//invoke通常用来执行一个方法
                return;
            }
            editor.commit();
        }
    }
}
