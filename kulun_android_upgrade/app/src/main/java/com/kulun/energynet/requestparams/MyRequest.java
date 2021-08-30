package com.kulun.energynet.requestparams;

import android.app.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kulun.energynet.customizeView.HashmapToJson;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.utils.API;
//import com.kulun.energynet.utils.MyX509;
//import com.kulun.energynet.utils.MyX509;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * created by xuedi on 2019/9/18SSLSocketClient
 */
public class MyRequest {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient okhttpclient(Activity activity) {
        try {
            if (client == null) {
                client = new OkHttpClient.Builder().
                        sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                        .hostnameVerifier(new TrustAllHostnameVerifier()).addInterceptor(interceptor(activity))
                        .build();
            }
            return client;
        } catch (Exception e) {
            Utils.log("", "", "okhttp https连接问题");
            return null;
        }
    }


    private static class TrustAllCerts implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private Interceptor interceptor(Activity activity) {
        if (loggingInterceptor == null) {
            loggingInterceptor = new HttpLogInterceptor(activity);
        }
        return loggingInterceptor;
    }

    private OkHttpClient client;
    private Interceptor loggingInterceptor;

    public void myRequest(String url, boolean urlisPost, HashMap<String, String> map, boolean mapisString, Activity activity, PromptDialog promptDialog,
                          SmartRefreshLayout smartRefreshLayout, Response myresponse) {
        Request request = null;
        if (urlisPost) {
            RequestBody body;
            if (map == null) {
                body = RequestBody.create("", JSON);
            } else {
                if (mapisString) {
                    body = RequestBody.create(HashmapToJson.hashMapToJson(map), JSON);
                } else {
                    body = RequestBody.create(HashmapToJson.hashMapToJsonNoString(map), JSON);
                }
            }
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .post(body)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        }
        okhttpclient(activity).newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                try {
                    Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                    String json = new String(response.body().string());
                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                    Integer code = jsonObject.has("code") ? jsonObject.get("code").getAsInt() : -1;
                    String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "";
                    if (code == 5 || code == 4) {//4是过期，5是token不对，其他的错误暂时都是-1
                        if (Utils.getToken(activity) != null && !Utils.getToken(activity).equals("")) {
                            Utils.toLogin(activity);
                        }
                        UserLogin.getInstance().setToken("");
                        SharePref.put(activity, API.token, "");
                        return;
                    }
                    JsonObject data = null;
                    JsonArray jsonArray = null;
                    boolean isNull = false;
                    if (jsonObject.has("data")) {
                        if (jsonObject.get("data").isJsonObject()) {
                            data = jsonObject.get("data").getAsJsonObject();
                        } else if (jsonObject.get("data").isJsonArray()) {
                            jsonArray = jsonObject.get("data").getAsJsonArray();
                        } else {
                            isNull = true;
                        }
                    }
                    if (code == 0) {
                        JsonObject finalData = data;
                        JsonArray finalJsonArray = jsonArray;
                        boolean finalIsNull = isNull;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myresponse.response(finalData, finalJsonArray, finalIsNull);
                            }
                        });
                    } else {
                        if (!message.equals("缺少token") && !message.equals("未查询到优惠券")) {
                            Utils.snackbar(activity, message);
                        }
                    }
                } catch (Exception e) {
                    Utils.log(API.baseurl + url, "", "try catch exception" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                Utils.log(API.baseurl + url, "", "失败");
                Utils.snackbar(activity, API.net_error);
            }
        });
    }

    public void myRequestNoBack(String url, boolean urlisPost, HashMap<String, String> map, boolean mapisString, Activity activity, PromptDialog promptDialog,
                                SmartRefreshLayout smartRefreshLayout, ResponseCode myresponse) {
        Request request = null;
        if (urlisPost) {
            RequestBody body;
            if (map == null) {
                body = RequestBody.create("", JSON);
            } else {
                if (mapisString) {
                    body = RequestBody.create(HashmapToJson.hashMapToJson(map), JSON);
                } else {
                    body = RequestBody.create(HashmapToJson.hashMapToJsonNoString(map), JSON);
                }
            }
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .post(body)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        }
        okhttpclient(activity).newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                try {
                    Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                    String json = new String(response.body().string());
                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                    Integer code = jsonObject.has("code") ? jsonObject.get("code").getAsInt() : -1;
                    String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "";
                    if (code == 5 || code == 4) {//4是过期，5是token不对，其他的错误暂时都是-1
                        if (Utils.getToken(activity) != null && !Utils.getToken(activity).equals("")) {
                            Utils.toLogin(activity);
                        }
                        UserLogin.getInstance().setToken("");
                        SharePref.put(activity, API.token, "");
                        return;
                    }
                    JsonObject data = null;
                    JsonArray jsonArray = null;
                    boolean isNull = false;
                    if (jsonObject.has("data")) {
                        if (jsonObject.get("data").isJsonObject()) {
                            data = jsonObject.get("data").getAsJsonObject();
                        } else if (jsonObject.get("data").isJsonArray()) {
                            jsonArray = jsonObject.get("data").getAsJsonArray();
                        } else {
                            isNull = true;
                        }
                    }
                    if (code == -1) {
                        if (!message.equals("缺少token") && !message.equals("未查询到优惠券")) {
                            Utils.snackbar(activity, message);
                        }
                    }
                    JsonObject finalData = data;
                    JsonArray finalJsonArray = jsonArray;
                    boolean finalIsNull = isNull;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myresponse.response(finalData, finalJsonArray, finalIsNull, code, message);
                        }
                    });
                } catch (Exception e) {
                    Utils.log(API.baseurl + url, "", "try catch exception" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                Utils.log(API.baseurl + url, "", "失败");
                Utils.snackbar(activity, API.net_error);
            }
        });
    }

    public void mycodeRequest(String url, boolean urlisPost, HashMap<String, String> map, boolean mapisString, Activity activity, PromptDialog promptDialog,
                              SmartRefreshLayout smartRefreshLayout, ResponseCode myresponse) {
        Request request = null;
        if (urlisPost) {
            RequestBody body;
            if (map == null) {
                body = RequestBody.create("", JSON);
            } else {
                if (mapisString) {
                    body = RequestBody.create(HashmapToJson.hashMapToJson(map), JSON);
                } else {
                    body = RequestBody.create(HashmapToJson.hashMapToJsonNoString(map), JSON);
                }
            }
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .post(body)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        }
        okhttpclient(activity).newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                try {
                    Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                    String json = new String(response.body().string());
                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                    Integer code = jsonObject.has("code") ? jsonObject.get("code").getAsInt() : -1;
                    String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "";
                    if (code == 5 || code == 4) {//4是过期，5是token不对，其他的错误暂时都是-1
                        if (Utils.getToken(activity) != null && !Utils.getToken(activity).equals("")) {
                            Utils.toLogin(activity);
                        }
                        UserLogin.getInstance().setToken("");
                        SharePref.put(activity, API.token, "");
                        return;
                    }
                    JsonObject data = null;
                    JsonArray jsonArray = null;
                    boolean isNull = false;
                    if (jsonObject.has("data")) {
                        if (jsonObject.get("data").isJsonObject()) {
                            data = jsonObject.get("data").getAsJsonObject();
                        } else if (jsonObject.get("data").isJsonArray()) {
                            jsonArray = jsonObject.get("data").getAsJsonArray();
                        } else {
                            isNull = true;
                        }
                    }
                    if (code == 0 || code == 7) {
                        JsonObject finalData = data;
                        JsonArray finalJsonArray = jsonArray;
                        boolean finalIsNull = isNull;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myresponse.response(finalData, finalJsonArray, finalIsNull, code, message);
                            }
                        });
                    } else {
                        if (!message.equals("缺少token") && !message.equals("未查询到优惠券")) {
                            Utils.snackbar(activity, message);
                        }
                    }
                } catch (Exception e) {
                    Utils.log(API.baseurl + url, "", "try catch exception" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                Utils.log(API.baseurl + url, "", "失败");
                Utils.snackbar(activity, API.net_error);
            }
        });
    }

    public void spliceJson(String url, boolean urlisPost, String stringJson, Activity activity, PromptDialog promptDialog,
                           SmartRefreshLayout smartRefreshLayout, Response myresponse) {
        Request request = null;
        if (urlisPost) {
            RequestBody body;
            if (stringJson == null) {
                body = RequestBody.create("", JSON);
            } else {
                body = RequestBody.create(stringJson, JSON);
            }
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .post(body)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        } else {
            if (stringJson == null) {
                request = new Request.Builder()
                        .url(API.baseurl + url)
                        .header("ACCESS_TOKEN", Utils.getToken(activity))
                        .build();
            } else {
                request = new Request.Builder()
                        .url(API.baseurl + url + "?" + stringJson)
                        .header("ACCESS_TOKEN", Utils.getToken(activity))
                        .build();
            }
        }
        okhttpclient(activity).newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                try {
                    Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                    String json = new String(response.body().string());
                    try {
                        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                        Integer code = jsonObject.has("code") ? jsonObject.get("code").getAsInt() : -1;
                        String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "";
                        if (code == 5 || code == 4) {//4是过期，5是token不对，其他的错误暂时都是-1
                            if (Utils.getToken(activity) != null && !Utils.getToken(activity).equals("")) {
                                Utils.toLogin(activity);
                            }
                            UserLogin.getInstance().setToken("");
                            SharePref.put(activity, API.token, "");
                            return;
                        }
                        JsonObject data = null;
                        JsonArray jsonArray = null;
                        boolean isNull = false;
                        if (jsonObject.has("data")) {
                            if (jsonObject.get("data").isJsonObject()) {
                                data = jsonObject.get("data").getAsJsonObject();
                            } else if (jsonObject.get("data").isJsonArray()) {
                                jsonArray = jsonObject.get("data").getAsJsonArray();
                            } else {
                                isNull = true;
                            }
                        }
                        if (code == 0) {
                            JsonObject finalData = data;
                            JsonArray finalJsonArray = jsonArray;
                            boolean finalIsNull = isNull;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myresponse.response(finalData, finalJsonArray, finalIsNull);
                                }
                            });
                        } else {
                            if (!message.equals("缺少token")) {
                                Utils.snackbar(activity, message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Utils.log(API.baseurl + url, "", "try catch exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                Utils.log(API.baseurl + url, "", "失败");
                Utils.snackbar(activity, API.net_error);
            }
        });
    }

    public void pay(String url, boolean urlisPost, String stringJson, Activity activity, PromptDialog promptDialog,
                    SmartRefreshLayout smartRefreshLayout, ResponseString myresponse) {
        Request request = null;
        if (urlisPost) {
            RequestBody body;
            if (stringJson == null) {
                body = RequestBody.create("", JSON);
            } else {
                body = RequestBody.create(stringJson, JSON);
            }
            request = new Request.Builder()
                    .url(API.baseurl + url)
                    .post(body)
                    .header("ACCESS_TOKEN", Utils.getToken(activity))
                    .build();
        } else {
            if (stringJson == null) {
                request = new Request.Builder()
                        .url(API.baseurl + url)
                        .header("ACCESS_TOKEN", Utils.getToken(activity))
                        .build();
            } else {
                request = new Request.Builder()
                        .url(API.baseurl + url + "?" + stringJson)
                        .header("ACCESS_TOKEN", Utils.getToken(activity))
                        .build();
            }
        }
        okhttpclient(activity).newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                try {
                    Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                    String json = new String(response.body().string());
                    try {
                        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                        Integer code = jsonObject.has("code") ? jsonObject.get("code").getAsInt() : -1;
                        String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "";
                        if (code == 5 || code == 4) {//4是过期，5是token不对，其他的错误暂时都是-1
                            if (Utils.getToken(activity) != null && !Utils.getToken(activity).equals("")) {
                                Utils.toLogin(activity);
                            }
                            UserLogin.getInstance().setToken("");
                            SharePref.put(activity, API.token, "");
                            return;
                        }
                        String string = "";
                        JsonObject responseJson = null;
                        if (jsonObject.has("data")) {
                            if (jsonObject.get("data").isJsonObject()) {
                                responseJson = jsonObject.get("data").getAsJsonObject();
                            } else {
                                string = jsonObject.get("data").getAsString();
                            }
                        }
                        if (code == 0) {
                            String finalString = string;
                            JsonObject finalResponseJson = responseJson;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myresponse.response(finalString, finalResponseJson);
                                }
                            });
                        } else {
                            if (!message.equals("缺少token")) {
                                Utils.snackbar(activity, message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Utils.log(API.baseurl + url, "", "try catch exception:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Utils.refreandproUIdismiss(activity, promptDialog, smartRefreshLayout);
                Utils.log(API.baseurl + url, "", "失败");
                Utils.snackbar(activity, API.net_error);
            }
        });
    }
}
