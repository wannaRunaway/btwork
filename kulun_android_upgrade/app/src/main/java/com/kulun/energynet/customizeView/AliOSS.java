package com.kulun.energynet.customizeView;

import android.app.Activity;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.kulun.energynet.main.MyApplication;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;

/**
 * created by xuedi on 2018/10/30
 */
public class AliOSS {
    private static OSS oss = null;

    public static OSS getOss(Activity activity) {
        if (oss == null) {
            oss = initOss(activity);
        }
        return oss;
    }

    public static OSSCredentialProvider initOSSCredentialProvider(Activity activity) {
        return new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                String ak = (String) SharePref.get(activity, API.ak, "");
                String sk = (String) SharePref.get(activity, API.sk, "");
                String token = (String) SharePref.get(activity, API.myosstoken, "");
                String expiration = (String) SharePref.get(activity, API.expiration, "");
                return new OSSFederationToken(ak, sk, token, expiration);
            }
        };
    }

    /**
     * 阿里sso存储初始化
     */
    private static OSS initOss(Activity activity) {
        String endpoint = Utils.getAliyunEndpoint(activity);
//        String stsServer = API.baseurl + API.URL_ALITOKEN;
        //推荐使用OSSAuthCredentialsProvider。token过期可以及时更新
//        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(API.baseurl+API.osstoken);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(MyApplication.getInstance().getApplicationContext(), endpoint, initOSSCredentialProvider(activity));
        OSSLog.enableLog();
        return oss;
    }
}
