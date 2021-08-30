package com.btkj.chongdianbao.utils;
import com.google.gson.Gson;

/**
 * created by xuedi on 2018/10/17
 * gson解析
 */
public class GsonUtils {
    private static Gson gson;
    public static Gson getInstance(){
        if (null == gson){
            synchronized (GsonUtils.class){
                if (null == gson){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}
