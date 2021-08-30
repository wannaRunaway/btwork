package com.btkj.chongdianbao.utils;
import android.app.Activity;
import android.content.Context;
import com.btkj.chongdianbao.model.Informodel;
import com.btkj.chongdianbao.model.User;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;
/**
 * created by xuedi on 2019/9/2
 */
public class BaseRequest {
    public static void getInfo(Activity activity, Context applicatContext){
        String url = API.BASE_URL + API.INFO;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myshouyerequest(url, requestParams, activity, applicatContext, false, new Myparams() {
            @Override
            public void message(String json) {
                Informodel informodel = GsonUtils.getInstance().fromJson(json, Informodel.class);
                User.getInstance().setId(informodel.getContent().getId());
                User.getInstance().setName(informodel.getContent().getName());
                User.getInstance().setPhone(informodel.getContent().getPhone());
                User.getInstance().setAccountNo(informodel.getContent().getAccountNo());
                User.getInstance().setBalance(informodel.getContent().getBalance());
                User.getInstance().setNoReaded(informodel.getContent().getNoReaded());
            }
        },null,null,true);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                Informodel informodel = GsonUtils.getInstance().fromJson(json, Informodel.class);
//                if (informodel.isSuccess()){
//                    User.getInstance().setId(informodel.getContent().getId());
//                    User.getInstance().setName(informodel.getContent().getName());
//                    User.getInstance().setPhone(informodel.getContent().getPhone());
//                    User.getInstance().setAccountNo(informodel.getContent().getAccountNo());
//                    User.getInstance().setBalance(informodel.getContent().getBalance());
//                    User.getInstance().setNoReaded(informodel.getContent().getNoReaded());
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Utils.log(url, requestParams ,null);
//            }
//        });
    }
}
