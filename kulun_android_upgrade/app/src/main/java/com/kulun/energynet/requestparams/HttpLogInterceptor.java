package com.kulun.energynet.requestparams;

import android.app.Activity;
import android.util.Log;

import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.SharePref;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * okhttp 拦截器
 */
public class HttpLogInterceptor implements Interceptor {
//    private static final String TAG = HttpLogInterceptor.class.getSimpleName();
    private final Charset UTF8 = Charset.forName("UTF-8");
    private Activity activity;
    public HttpLogInterceptor(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String body = null;
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            body = buffer.readString(charset);
        }

        Log.d("xuedi", "发送请求: method：" + request.method()
                + "\nurl：" + request.url()
                + "\n请求头：" + request.headers()
                + "\n请求参数: " + body);
        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        if (response.header("ACCESS_TOKEN")!=null) {
            UserLogin.getInstance().setToken(response.header("ACCESS_TOKEN"));
            SharePref.put(activity, API.token, response.header("ACCESS_TOKEN"));
        }
        ResponseBody responseBody = response.body();
        String rBody;
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        rBody = buffer.clone().readString(charset);

        Log.d("xuedi", "收到: method：" + request.method()+
                "\n响应header："+response.header("ACCESS_TOKEN")
                +"\n响应url:"+response.request().url()
                + "\n响应body: " + rBody);
        return response;
    }
}
