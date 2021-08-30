package com.btkj.chongdianbao.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.btkj.chongdianbao.login.PasswordLoginActivity;
import com.btkj.chongdianbao.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * created by xuedi on 2019/9/18
 */
public class MyRequest {
    public void myrequest(String url, RequestParams requestParams, Activity activity,Context context,  boolean isshowfail, Myparams myparams){
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
                    if (code == 801){  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
                        User.getInstance().setYuyue(false);
                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
                        User.getInstance().setIsneedcheckIdcard(false);//退出后则不弹出身份认证提示
                        Utils.snackbar(context,activity,"请重新登录");
                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    }else {
                        if (!TextUtils.isEmpty(message)) {
                            Utils.snackbar(context, activity, message);
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
                    Utils.snackbar(context, activity, API.net_error);
                }
            }
        });
    }

    public void myshouyerequest(String url, RequestParams requestParams, Activity activity,Context context,  boolean isshowfail, Myparams myparams, PromptDialog promptDialog, SmartRefreshLayout smartRefreshLayout,boolean isnomessage){
        PersistentCookieStore cookieStore = new PersistentCookieStore(activity);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (promptDialog != null){
                    promptDialog.dismiss();
                }
                if (smartRefreshLayout != null){
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
                    if (code == 801){  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
                        User.getInstance().setYuyue(false);
                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
                        User.getInstance().setIsneedcheckIdcard(false);
//                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        activity.startActivity(intent);
//                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    }else {
                        if (!isnomessage && !User.getInstance().isIsneedlogin()) {
                            if (!TextUtils.isEmpty(message)) {
                                Utils.snackbar(context, activity, message);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (promptDialog != null){
                    promptDialog.dismiss();
                }
                if (smartRefreshLayout != null){
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                }
                Utils.log(url, requestParams, null);
                if (isshowfail) {
                    Utils.snackbar(context, activity, API.net_error);
                }
            }
        });
    }

    public void mysmartrequest(String url, RequestParams requestParams, Activity activity, Context context, boolean isshowfail, Myparams myparams, SmartRefreshLayout smartRefreshLayout){
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
                    if (code == 801){  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
                        User.getInstance().setYuyue(false);
                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
                        User.getInstance().setIsneedcheckIdcard(false);
                        Utils.snackbar(context,activity,"请重新登录");
                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    }else {
                        if (!TextUtils.isEmpty(message)) {
                            Utils.snackbar(context, activity,message);
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
                    Utils.snackbar(context, activity, API.net_error);
                }
            }
        });
    }

    public void mypromptrequest(String url, RequestParams requestParams, Activity activity, Context context, boolean isshowfail, Myparams myparams, PromptDialog promptDialog){
        PersistentCookieStore cookieStore = new PersistentCookieStore(activity);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (promptDialog != null){
                    promptDialog.dismiss();
                }
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getInt("code");
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("msg");
                    if (code == 801){  //其他有登陆情况
                        SharePref.put(activity, API.bindId, 0);
                        SharePref.put(activity, API.carId, 0);
                        SharePref.put(activity, API.plateNo, "");
                        SharePref.put(activity, API.carType, 0);
                        SharePref.put(activity, API.batterycount, 0);
                        User.getInstance().setYuyue(false);
                        User.getInstance().setDelay(false);
                        User.getInstance().setIsneedlogin(true);
                        User.getInstance().setIsneedcheckIdcard(false);
                        Utils.snackbar(context,activity,"请重新登录");
                        Intent intent = new Intent(activity, PasswordLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        return;
                    }
                    if (success) {
                        myparams.message(json);
                    }else {
                        if (!TextUtils.isEmpty(message)) {
                            Utils.snackbar(context, activity, message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (promptDialog != null){
                    promptDialog.dismiss();
                }
                Utils.log(url, requestParams, null);
                if (isshowfail) {
                    Utils.snackbar(context, activity,API.net_error);
                }
            }
        });
    }
}
