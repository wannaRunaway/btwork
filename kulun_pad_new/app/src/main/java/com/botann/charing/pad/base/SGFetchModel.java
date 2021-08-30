package com.botann.charing.pad.base;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liushanguo on 2018/4/17.
 */

public class SGFetchModel {
    public Integer code;
    public String  userInfo;
    public String content;
    public boolean success;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    public JSONObject getJsonObject() {
        if (jsonObject == null) {
            try {
                jsonObject = new JSONObject(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public JSONArray getJsonArray() {
        if (jsonArray == null) {
            try {
                jsonArray = new JSONArray(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    public String getSting (String key) {
        try {
            return getJsonObject().getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getInt (String key) {
        try {
            return getJsonObject().getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getString (JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SGFetchModel() {
    }

    public SGFetchModel(Integer code, String userInfo, String content, boolean success) {
        this.code = code;
        this.userInfo = userInfo;
        this.content = content;
        this.success = success;
    }

    public static <T> T parse(JsonObject jsonObject, Class clazz) {
        return (T)getGson().toJson(jsonObject,clazz);
    }

    public <T> T  modelOfContent(Class clazz) {
        return (T)getGson().fromJson(content,clazz);
    }
    public <T> T listModelOfContent(Type type) {
        return (T) getGson().fromJson(content, type);
    }

    public List listModel(String json, Class clazz) {
        if (json == null || json.equals("")){
            Log.d("btkj", "list为空，无法解析");
            return null;
        }
        //        T t = clazz.getComponentType();
//        return (T)getGson().fromJson(content,new TypeToken<List<T>>(){}.getType());
        List list = new ArrayList();
        try {
            JSONArray array = new JSONArray(json);
            for (int i=0;i<array.length();i++) {
                Object model = SGFetchModel.getGson().fromJson(array.getString(i),clazz);
                if (model == null) {
                    Log.e("列表存在null数据","");
                } else {
                    list.add(model);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
    public List listModelOfContent(Class clazz) {
        return listModel(content,clazz);
    }

    public Pager pagerOfContent(Class clazz) {
        Pager pager = new Pager();
        try {
            JSONObject object = getJsonObject();
            pager.total = object.getInt("total");
            List list = new ArrayList();
            JSONArray array = new JSONArray(object.getString("data"));
            for (int i=0;i<array.length();i++) {
                Object model = SGFetchModel.getGson().fromJson(array.getString(i),clazz);
                if (model == null) {
                    Log.e("列表存在null数据","");
                } else {
                    list.add(model);
                }
            }
            pager.list = list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pager;
    }



}
