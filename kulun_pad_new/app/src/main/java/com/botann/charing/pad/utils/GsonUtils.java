package com.botann.charing.pad.utils;

import com.google.gson.Gson;

/**
 * created by xuedi on 2018/10/22
 * gson字符串的转化
 */
public class GsonUtils {
    private static Gson gson;
    public static Gson getInstance(){
        if (gson == null){
            synchronized (GsonUtils.class){
                if (gson == null){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}
