package com.kulun.kulunenergy.requestparams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kulun.kulunenergy.login.PasswordLoginActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.utils.SharePref;
import com.kulun.kulunenergy.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * created by xuedi on 2019/9/18
 */
public class MyRequest {
    public void req(String url, Map<String, String> map, Activity activity, PromptDialog promptDialog, SmartRefreshLayout smartRefreshLayout, Response response) {
        RequestParams requestParams = new RequestParams();
        for (String key : map.keySet()) {
            requestParams.add(key, map.get(key));
        }
        PersistentCookieStore cookieStore = new PersistentCookieStore(activity);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(API.BASE_URL + url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
                String json = new String(responseBody);
                Utils.log(API.BASE_URL + url, requestParams, json);
                try {
                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                    Integer code = jsonObject.has("code") ? jsonObject.get("code").getAsInt() : 0;
                    boolean success = jsonObject.has("success") ? jsonObject.get("success").getAsBoolean() : false;
                    String message = jsonObject.has("msg") ? jsonObject.get("msg").getAsString() : "";
                    if (jsonObject.has("content")) {
                        JsonObject data = null;
                        JsonArray jsonArray = null;
                        if (jsonObject.get("content").isJsonObject()) {
                            data = jsonObject.get("content").getAsJsonObject();
                            if (data.has("accountId")) {
                                Integer accountId = data.get("accountId").getAsInt();
                                User.getInstance().setAccountId(accountId);
                                SharePref.put(activity, API.accountId, accountId);
                            }
                            if (data.has("token")) {
                                String token = data.get("token").getAsString();
                                User.getInstance().setToken(token);
                                SharePref.put(activity, API.token, token);
                            }
                        } else if (jsonObject.get("content").isJsonArray()) {
                            jsonArray = jsonObject.get("content").getAsJsonArray();
                        }
//                        User.getInstance().setPhone(phone);
//                        if (code == 0) {
//                            GetMessageResponse.getPostMessage(LoginActivity.this, user.getAccountId(), 1, 20);
//                            createNewAccount(etPhone.getText().toString(), etPassword.getText().toString(), user);
//                        }
//                    if (code == 801){  //其他有登陆情况
//                        SharePref.put(activity, API.bindId, 0);
//                        SharePref.put(activity, API.carId, 0);
//                        SharePref.put(activity, API.plateNo, "");
//                        SharePref.put(activity, API.carType, 0);
//                        SharePref.put(activity, API.batterycount, 0);
//                        User.getInstance().setYuyue(false);
//                        User.getInstance().setDelay(false);
//                        User.getInstance().setIsneedlogin(true);
//                        User.getInstance().setIsneedcheckIdcard(false);
//                        return;
//                    }
                        if (success) {
                            if (data != null || jsonArray != null) {
                                response.response(data, jsonArray);
                            }
                        } else {
                            Utils.snackbar(activity, message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
                Utils.log(API.BASE_URL + url, requestParams, null);
                Utils.snackbar(activity, API.net_error);
                JsonObject jsonObject = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                response.response(jsonObject, jsonArray);
            }
        });
    }

//    private void createNewAccount(String phone, String password, User user) {
////        Constants.accountId = user.getAccountId();
//        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//        editor.putString("phone", phone);
//        editor.putString("password", password);
//        editor.putInt("accountId", user.getAccountId());
//        editor.putString("token", user.getToken());
//        editor.commit();
//    }

    public void myrequest(String url, RequestParams requestParams, Activity activity, Context context, boolean isshowfail, Myparams myparams) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(activity);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getInt("code");
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("msg");
                    if (code == 801) {  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
//                        User.getInstance().setYuyue(false);
//                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
//                        User.getInstance().setIsneedcheckIdcard(false);//退出后则不弹出身份认证提示
                        Utils.snackbar(activity, "请重新登录");
                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    } else {
                        if (!TextUtils.isEmpty(message)) {
                            Utils.snackbar(activity, message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log(url, requestParams, null);
                if (isshowfail) {
                    Utils.snackbar(activity, API.net_error);
                }
            }
        });
    }

    public void myshouyerequest(String url, RequestParams requestParams, Activity activity, Context context, boolean isshowfail, Myparams myparams, PromptDialog promptDialog, SmartRefreshLayout smartRefreshLayout, boolean isnomessage) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(activity);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getInt("code");
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("msg");
                    if (code == 801) {  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
//                        User.getInstance().setYuyue(false);
//                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
//                        User.getInstance().setIsneedcheckIdcard(false);
//                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        activity.startActivity(intent);
//                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    } else {
                        if (!isnomessage && !User.getInstance().isIsneedlogin()) {
                            if (!TextUtils.isEmpty(message)) {
                                Utils.snackbar(activity, message);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
                Utils.log(url, requestParams, null);
                if (isshowfail) {
                    Utils.snackbar(activity, API.net_error);
                }
            }
        });
    }

    public void mysmartrequest(String url, RequestParams requestParams, Activity activity, Context context, boolean isshowfail, Myparams myparams, SmartRefreshLayout smartRefreshLayout) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(activity);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getInt("code");
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("msg");
                    if (code == 801) {  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
//                        User.getInstance().setYuyue(false);
//                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
//                        User.getInstance().setIsneedcheckIdcard(false);
                        Utils.snackbar(activity, "请重新登录");
                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    } else {
                        if (!TextUtils.isEmpty(message)) {
                            Utils.snackbar(activity, message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                Utils.log(url, requestParams, null);
                if (isshowfail) {
                    Utils.snackbar(activity, API.net_error);
                }
            }
        });
    }

    public void mypromptrequest(String url, RequestParams requestParams, Activity activity, Context context, boolean isshowfail, Myparams myparams, PromptDialog promptDialog) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(activity);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getInt("code");
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("msg");
                    if (code == 801) {  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
//                        User.getInstance().setYuyue(false);
//                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
//                        User.getInstance().setIsneedcheckIdcard(false);
                        Utils.snackbar(activity, "请重新登录");
                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    } else {
                        if (!TextUtils.isEmpty(message)) {
                            Utils.snackbar(activity, message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                }
                Utils.log(url, requestParams, null);
                if (isshowfail) {
                    Utils.snackbar(activity, API.net_error);
                }
            }
        });
    }
}
