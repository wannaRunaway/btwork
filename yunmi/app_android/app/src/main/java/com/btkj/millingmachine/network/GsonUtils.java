package com.btkj.millingmachine.network;
import com.google.gson.Gson;
import java.util.ArrayList;
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
