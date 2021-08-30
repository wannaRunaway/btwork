package com.botann.driverclient.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.botann.driverclient.model.User;

/**
 * Created by Orion on 2017/7/11.
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper sharedPreferencesHelper = null;

    //singleton mode Context as param
    public static SharedPreferencesHelper getInstance(Context context) {
        if(sharedPreferencesHelper == null) {
            synchronized (SharedPreferencesHelper.class) {
                if(sharedPreferencesHelper == null) {
                    sharedPreferencesHelper = new SharedPreferencesHelper();
                    sharedPreferencesHelper.setContext(context);
                    return sharedPreferencesHelper;
                }
            }
        }
        return sharedPreferencesHelper;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    //get Boolean type data
    public boolean getBoolean(String key, boolean defValue) {
        try {
            return getSP().getBoolean(key, defValue);
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
            return defValue;
        }
    }

    //save Boolean type data
    public void putBoolean(String key, boolean value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
        }
    }

    //get Long type data
    public long getLong(String key, long defValue) {
        try {
            return getSP().getLong(key, defValue);
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
            return defValue;
        }
    }

    //save Long type data
    public void putLong(String key, long value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putLong(key, value);
            editor.commit();
        } catch (NullPointerException exception) {
            Log.d("hcj", "" + exception);
        }
    }

    //get Int type data
    public int getInt(String key, int defaultValue) {
        try {
            return getSP().getInt(key, defaultValue);
        } catch (Exception e) {
            Log.d("hcj", "" + e);
            return defaultValue;

        }
    }

    //save Int type data
    public void putInt(String key, int value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putInt(key, value);
            editor.commit();
        } catch (Exception e) {
            Log.d("hcj", "" + e);
        }
    }

    //get String type data
    public String getString(String key, String defValue) {
        try {
            return getSP().getString(key, defValue);
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
            return defValue;
        }
    }

    //get String type data
    public String getAccountString(String key, String defValue) {
        try {
            return getAccountSP().getString(key, defValue);
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
            return defValue;
        }
    }

    //save String type data
    public void putString(String key, String value) {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.putString(key, value);
            editor.commit();
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
        }
    }

    //save String type data
    public void putAccountString(String key, String value) {
        try {
            SharedPreferences.Editor editor = getAccountSP().edit();
            editor.putString(key, value);
            editor.commit();
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
        }
    }

    //clear data
    public void clear() {
        try {
            SharedPreferences.Editor editor = getSP().edit();
            editor.clear();
            editor.commit();
        } catch (NullPointerException e) {
            Log.d("hcj", "" + e);
        }
    }

    //get SharedPreferences Object
    private SharedPreferences getSP() {
        return context.getSharedPreferences("sp", Context.MODE_PRIVATE);
    }

    //get SharedPreferences Object
    private SharedPreferences getAccountSP() {
        return context.getSharedPreferences(String.valueOf(User.getInstance().getAccountId()), Context.MODE_PRIVATE);
    }

    private SharedPreferences getSP(String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
