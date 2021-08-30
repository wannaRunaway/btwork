package com.btkj.millingmachine.network;

import com.btkj.millingmachine.util.MD5;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by xuedi on 2019/4/18
 */
public class Customize {
    public static final String ACCESS_KEY = "b03840c11333428781f53f6e6d01448b";
    public static final String NONCE = generateDefaultUsersName(); //不小于10位
    public static final String SIGN = "sign";
    public static final String SECRET_KEY = "a30213180b7d406aa393472b685fdc2f";
    public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String getRequestParams(Map<String, String> map, RequestParams requestParams) {
        List<String> list = new ArrayList<>();
        Set<String> keySet = map.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = map.get(key);
            list.add(key + "=" + value);
            requestParams.add(key, String.valueOf(value));
        }
        String timestamp = getTimestamp();
        String nonce = Customize.NONCE;
        list.add("accessKey"+"="+ Customize.ACCESS_KEY);
        list.add("timestamp"+"="+timestamp);
        list.add("nonce"+"="+nonce);
        requestParams.add("accessKey", Customize.ACCESS_KEY);
        requestParams.add("timestamp", timestamp);
        requestParams.add("nonce", nonce);
        Collections.sort(list);
        list.add("secretKey"+"="+SECRET_KEY);
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()){
                stringBuilder.append("&");
            }
        }
        String encodeStr = MD5.encode(stringBuilder.toString());
        return encodeStr.toUpperCase();
    }
    public static String getTimestamp(){
        return String.valueOf(System.currentTimeMillis());
    }
    public static String generateDefaultUsersName() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            sb.append(ALL_CHAR.charAt((int) (ALL_CHAR.length() * Math.random())));
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return "ZY" + sb + df.format(date);
    }
}
