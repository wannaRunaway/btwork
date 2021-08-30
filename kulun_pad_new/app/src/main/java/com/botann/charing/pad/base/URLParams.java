package com.botann.charing.pad.base;

import java.util.HashMap;
import java.util.Map;

import com.botann.charing.pad.model.User;
import okhttp3.FormBody;

/**
 * Created by liushanguo on 2018/4/17.
 */

public class URLParams{
    private Map<String,Object> params;
    private String paramsString;

    public String getParamsString() {
        return paramsString;
    }

    public URLParams() {
        params = new HashMap<String ,Object>();
    }

    public URLParams(Map<String, Object> params) {
        this.params = params;
    }
    public URLParams(String key, Object value) {
        params = new HashMap<>();
        put(key,value);
    }

    public void put(String key, Object value) {
        this.params.put(key,value);
    }


    public FormBody.Builder toBuilder() {
        FormBody.Builder build = new FormBody.Builder();
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("请求参数：\n");
        stringBuilder.append("{\t\n");
        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                String param = (params.get(key) instanceof String) ? (String) params.get(key) : params.get(key).toString();
                build.add(key, param);
                stringBuilder.append("\t"+key+"  :  "+param+",\n");
            }
        }
        String adminIdKey = "adminId";
        build.add(adminIdKey, User.shared().getId().toString());
        stringBuilder.append("\t"+adminIdKey+"  :  "+User.shared().getId().toString()+",\n");
        stringBuilder.append("}\t\n");
        paramsString = stringBuilder.toString();
        return build;
    }
}
