package com.botann.charing.pad.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

//import com.botann.charing.pad.model.User;
import com.botann.charing.pad.MainApp;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.utils.SystemUtil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 基于OKHttp3
 * Created by 刘山国 on 2017/5/18.
 */

public class SGHTTPManager {

    private static OkHttpClient okHttpClient;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 单例
     * @return OkHttpClient
     */
    public static OkHttpClient instance(){
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60000, TimeUnit.SECONDS)
            .readTimeout(60000, TimeUnit.SECONDS)
            .build();
        }
        return okHttpClient;
    }

    private static String getUserAgent() {
        String userAgent = "";
//        APP版本
        String versionName = SystemUtil.getVersionName(MainApp.getInstance());
//        手机型号
        String systemModel = SystemUtil.getSystemModel();
//        系统版本
        String systemVersion = SystemUtil.getSystemVersion();
        String deviceBrand = SystemUtil.getDeviceBrand();
        userAgent = "Android/" + versionName + "/" + deviceBrand + "" + systemModel + "/" + systemVersion;
        Log.d("xuedi", "userAgent:"+userAgent);
        return userAgent;
    }


    /**
     * POST 异步网络请求 JSON
     * @param url           String                  接口url,以"/"开头
     * @param parameters    Map<String,Object>      请求参数
     * @param callBack      SGHttpCallBack          回调
     */
    public static void POST_JSON(String url,  Map<String,Object> parameters,final SGHttpCallBack callBack) {
        JSONObject jsonObject = null;
        if (parameters == null) {
            jsonObject = new JSONObject();
        } else {
            jsonObject = new JSONObject(parameters);
        }
        POST_JSON(url,jsonObject,callBack);
    }

    /**
     * POST_JSON 异步网络请求 JSON
     * @param url           String                  接口url,以"/"开头
     * @param parameters    JSONObject              请求参数
     * @param callBack      SGHttpCallBack          回调
     */
    public static void POST_JSON(String url, JSONObject parameters,final SGHttpCallBack callBack){

        //基本参数请求
//        FormBody.Builder build = new FormBody.Builder();
//        //Map<String,String> parameters
//        for (String key : parameters.keySet()) {
//            build.add(key,parameters.get(key));
//        }
//        RequestBody requestBody = build.build();

        //所有接口必带参数
        if (parameters == null) parameters = new JSONObject();
        try {
            parameters.put("userId", User.shared().getUserId());
            parameters.put("stationName",User.shared().getStation());
        } catch (JSONException e) {
            e.printStackTrace();
            callBack.onResponse(false,"没有用户id，用户登录失效",null);
            return;
        }

        Log.i("请求参数",parameters.toString());
        //JSON参数请求
        RequestBody requestBody = RequestBody.create(JSON, parameters.toString());
        Request request = new Request.Builder()
                .url(API.BASE_URL+url)
                .post(requestBody)
                .build();
        Call call = instance().newCall(request);
        request(call,callBack);
    }

    /***
     * POST 参数提交
     * @param url URL
     * @param parameters Map
     * @param callBack SGHttpCallBack
     */
    public static void POST(String url, Map<String,Object > parameters,final SGHttpCallBack callBack){
        //基本参数请求
        FormBody.Builder build = new FormBody.Builder();
        //Map<String,String> parameters
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                build.add(key,parameters.get(key).toString());
                stringBuilder.append(key+":"+parameters.get(key).toString()+",");
            }
        }
        build.add("userId",User.shared().getUserId().toString());
        stringBuilder.append("userId:"+User.shared().getUserId().toString()+"");
        build.add("stationName",User.shared().getStation());
        stringBuilder.append(", stationName:"+User.shared().getStation()+"}");
        Log.i("请求参数",stringBuilder.toString());
        RequestBody requestBody = build.build();
        Request request = new Request.Builder()
                .url(API.BASE_URL+url)
                .post(requestBody)
                .build();
        Call call = instance().newCall(request);
        request(call,callBack);

    }
    public static void POST(String url, URLParams params, final SGRequestCallBack callBack){
            //基本参数请求
            if (params == null) params = new URLParams();
            RequestBody requestBody = params.toBuilder().build();
            Request request = new Request.Builder()
//                    .url(API.NEW_BASE_URL+url)
                    .url(API.NEW_BASE_URL+url)
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", getUserAgent())
                    .post(requestBody)
                    .build();
            Call call = instance().newCall(request);
            Log.i("xuedi",API.NEW_BASE_URL+url);
            Log.i("xuedi",params.getParamsString());
            request(call,callBack);
    }


    /**
     * GET 异步网络请求
     * @param url 接口url,以"/"开头
     * @param parameters 请求参数
     * @param callBack 回调
     */
    public static void GET(String url, Map<String,Object> parameters, final  SGHttpCallBack callBack){
        String finalUrl = API.BASE_URL+url;
        parameters.put("userId", User.shared().getUserId());
        parameters.put("stationName",User.shared().getStation());
        if (parameters.size()>0) {
            StringBuffer stringBuffer = new StringBuffer(finalUrl+"?");
            for (String key : parameters.keySet()) {
                stringBuffer.append(key+"="+parameters.get(key)+"&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
            finalUrl = stringBuffer.toString();
        }
        Request.Builder requestBuilder = new Request.Builder().url(finalUrl);
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call call= instance().newCall(request);
        request(call,callBack);
}

    private static void request(Call mCall, final SGRequestCallBack callBack){
        if (mCall == null) {
            callBack.onResponse(false,"Call==null，请求失败",null);
            return;
        }
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(call.toString(),e.toString());
                defaultCallbackExecutor().execute(new Runnable(){
                    @Override public void run() {
                        Log.e("请求失败！","");
                        callBack.onResponse(false,"网络请求超时！",null);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
//                Log.i("请求接口", call.request().url().toString());
                Log.i("xuedi", body.toString());
                try {
                    final JSONObject jsonObject = new JSONObject(body);
                    final String message = jsonObject.getString("msg");
                    Integer code = jsonObject.getInt("code");
                    boolean success = false;
                    if (jsonObject.has("success")){
                        success = jsonObject.getBoolean("success");
                    }
                    final Boolean isSuccess = code==0;
                    final SGFetchModel fetchModel = new SGFetchModel(code,message,jsonObject.getString("content"), success);
                    defaultCallbackExecutor().execute(new Runnable(){
                        @Override public void run() {
                            callBack.onResponse(isSuccess,message,fetchModel);
                        }
                    });
                } catch (JSONException e) {
                    Log.e(call.toString(),"服务器返回数据格式错误！，响应非JSON\n"+e.toString());
                    defaultCallbackExecutor().execute(new Runnable(){
                        @Override public void run() {
                            callBack.onResponse(false,"服务器返回数据格式错误！",null);
                        }
                    });
//                    ToastUtil.showToast(this,"服务器返回数据格式错误！");
                }

            }
        });
    }

    /**
     * 私有方法，处理网络请求回调
     * @param mCall             Call
     * @param callBack          SGHttpCallBack
     */
    private static void request(Call mCall, final SGHttpCallBack callBack){
        if (mCall == null) {
            callBack.onResponse(false,"Call==null，请求失败",null);
            return;
        }
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(call.toString(),e.toString());
                defaultCallbackExecutor().execute(new Runnable(){
                    @Override public void run() {
                        callBack.onResponse(false,"",null);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
//                Log.i("请求接口", call.request().url().toString());
                Log.i("响应结果", body.toString());
                try {
                    final JSONObject jsonObject = new JSONObject(body);
                    final String message = jsonObject.getString("msg");
                    final Boolean isSuccess = jsonObject.getInt("code")==0;
                    defaultCallbackExecutor().execute(new Runnable(){
                        @Override public void run() {
                            callBack.onResponse(isSuccess,message,jsonObject);
                        }
                    });
                } catch (JSONException e) {
                    Log.e(call.toString(),"响应非JSON\n"+e.toString());
                    defaultCallbackExecutor().execute(new Runnable(){
                        @Override public void run() {
                            callBack.onResponse(false,"",null);
                        }
                    });

                }

            }
        });
    }

    /**
     * 专用网络回调接口
     */
    public interface SGHttpCallBack {
        void onResponse(Boolean isSuccess,String userInfo ,JSONObject jsonObject);
    }

    public interface SGRequestCallBack {
        void onResponse(Boolean isSuccess,String userInfo ,SGFetchModel fetchModel);
    }

    /**
     * 回调调主线程执行器
     * @return Executor
     */
    private static Executor executor;
    private static Executor defaultCallbackExecutor() {
        if (executor == null) executor = new MainThreadExecutor();
        return executor;
    }

    /**
     * 主线程执行器
     */
    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());
        @Override public void execute(Runnable r) {
            handler.post(r);
        }
    }
}
