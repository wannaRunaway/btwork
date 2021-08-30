package com.kulun.energynet.network.api;

import android.content.Context;
import android.util.Log;

import com.kulun.energynet.UpdateManager;
import com.kulun.energynet.utils.ToastUtil;
import com.kulun.energynet.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by wcc on 2016/9/10.
 */
public class GetVersion {

    public static void getVersion(final Context mContext) {
        final String url = API.BASE_URL+API.URL_VERSION_INFO;
        final AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        client.setCookieStore(myCookieStore);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Utils.log(url, new RequestParams(), json);
                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                if ( !obj.get("content").isJsonNull()) {
                    JsonObject data = obj.get("content").getAsJsonObject();
                    Integer versionCode = data.get("versionCode").getAsInt();
                    String versionName = data.get("versionName").getAsString();
                    String apkPath = data.get("apkPath").getAsString();
                    UpdateManager manager = new UpdateManager(mContext, versionCode, versionName,
                            apkPath);
                    manager.checkUpdate();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                if (responseBody != null) {
                    String json = new String(responseBody);
                    Utils.log(url, new RequestParams(), json);
                    ToastUtil.showToast(mContext, "检查更新失败, 无法访问服务器");
                }
            }

        });

    }

}
