package com.botann.driverclient.network.api;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.botann.driverclient.model.User;
import com.botann.driverclient.ui.activity.login.LoginActivity;
import com.botann.driverclient.utils.MD5;
import com.botann.driverclient.utils.SystemUtil;
import com.botann.driverclient.utils.ToastUtil;
import com.botann.driverclient.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by Orion on 2017/7/11.
 */
public class GetLoginResponse {
    public static void getLoginResponse(final Activity mContext, final String phone, String password) {
        final String url = API.BASE_URL + API.URL_LOGIN;
        final RequestParams params = new RequestParams();
        params.add("phone",phone);
        params.add("password", MD5.encode(password));
        final AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try{
                    String json = new String(response);
                    Utils.log(url, params, json);
                    JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                    JsonObject data = obj.get("content").getAsJsonObject();
                    String token = data.get("token").getAsString();
                    final Integer accountId = data.get("accountId").getAsInt();
                    Integer code = obj.get("code").getAsInt();
//                    User user = new User();
                    if(data != null) {
                        User.getInstance().setToken(token);
                        User.getInstance().setAccountId(accountId);
                        User.getInstance().setPhone(phone);
                        if(code == 0){
                            LoginActivity.getInstance().doWithLoginResult(obj.get("msg").getAsString(), User.getInstance(), true);
                        }else{
                            LoginActivity.getInstance().doWithLoginResult(obj.get("msg").getAsString(), User.getInstance(), false);
                        }
                    }else{
                        LoginActivity.getInstance().doWithLoginResult(obj.get("msg").getAsString(), User.getInstance(), false);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPhoneInfo(mContext, accountId);
                        }
                    }, 2000);
                }catch(Exception e) {
                    String error = new String(response);
                    JsonObject errorObj = new JsonParser().parse(error).getAsJsonObject();
                    ToastUtil.showToast(mContext, errorObj.get("msg").getAsString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e(Utils.TAG, "Exception = " + e.toString());
                Toast.makeText(mContext, "连接到服务器失败！", Toast.LENGTH_SHORT).show();
            }

        });
    }
    //手机信息上传服务器

    /**
     * Integer accountId    司机账户ID
     * Integer phoneType IOS类型=1，安卓类型=2
     * String hard 手机硬件版本
     * String soft 蓝色大道app软件版本
     */
    public static void getPhoneInfo(Context context, int accountId) {
        final String url = API.BASE_URL + API.URL_PHONEINFO_UP;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(accountId));
        requestParams.add("phoneType", String.valueOf(2));
        requestParams.add("hard", SystemUtil.getDeviceBrand()+SystemUtil.getSystemModel()+"/"+SystemUtil.getSystemVersion());
        requestParams.add("soft", SystemUtil.getVersionName(context));
        Log.d(Utils.TAG, "url:"+url+"\n"+"params:"+requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
        httpClient.setCookieStore(persistentCookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(Utils.TAG, "手机信息上传成功");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Utils.TAG, "Exception = " + error.toString());
            }
        });
    }
}
