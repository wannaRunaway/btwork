package com.botann.driverclient.utils;

import com.botann.driverclient.model.BindCar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 将list转化为json
     */
    public String listtoJson(ArrayList list){
        String json = gson.toJson(list);
        return json;
    }

    /**
     * 将json转化为list
     */
    public static List jsontoList(String json){
        List<BindCar> ls = gson.fromJson(json, new TypeToken<List<BindCar>>(){}.getType());
        return ls;
    }

    /**
     * 把json转化为一个类对象
     */
    public Object jsontoObject(String json, Class classes){
        Object oj = gson.fromJson(json, classes);
        return oj;
    }
}
