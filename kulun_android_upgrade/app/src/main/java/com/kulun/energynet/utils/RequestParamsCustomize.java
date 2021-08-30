package com.kulun.energynet.utils;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by xuedi on 2019/4/18
 */
public class RequestParamsCustomize {
//    public static final String ACCESS_KEY = "dde9d65d2e6d3b42c28479edd529984a";
//    public static final String NONCE = StringUtil.generateDefaultUsersName(); //不小于10位
//    public static final String SIGN = "sign";
//    public static final String SECRET_KEY = "5fcc08df75eaab2bd86d7853cfead9ab";
//    public static String getRequestParams(Map<String, String> map, RequestParams requestParams) {
//        List<String> list = new ArrayList<>();
//        Set<String> keySet = map.keySet();
//        Iterator<String> it = keySet.iterator();
//        while (it.hasNext()) {
//            String key = it.next();
//            String value = map.get(key);
//            list.add(key + "=" + value);
//            requestParams.add(key, value);
//        }
//        String timestamp = getTimestamp();
//        String nonce = RequestParamsCustomize.NONCE;
//        list.add("accessKey"+"="+RequestParamsCustomize.ACCESS_KEY);
//        list.add("timestamp"+"="+timestamp);
//        list.add("nonce"+"="+nonce);
//        requestParams.add("accessKey", RequestParamsCustomize.ACCESS_KEY);
//        requestParams.add("timestamp", timestamp);
//        requestParams.add("nonce", nonce);
//        Collections.sort(list);
//        list.add("secretKey"+"="+SECRET_KEY);
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
//            stringBuilder.append(iterator.next());
//            if (iterator.hasNext()){
//                stringBuilder.append("&");
//            }
//        }
//        String encodeStr = MD5.encode(stringBuilder.toString());
//        return encodeStr.toUpperCase();
//    }
//
//    public static String getTimestamp(){
//        return String.valueOf(System.currentTimeMillis());
//    }
}
