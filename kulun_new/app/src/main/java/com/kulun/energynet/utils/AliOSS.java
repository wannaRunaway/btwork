package com.kulun.energynet.utils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.kulun.energynet.MainApp;
import com.kulun.energynet.network.api.API;

/**
 * created by xuedi on 2018/10/30
 */
public class AliOSS {
    private static OSS oss = null;
    public static OSS getOss(){
        if (oss == null){
            oss = initOss();
        }
        return oss;
    }

    /**
     * 阿里sso存储初始化
     */
    private static OSS initOss() {
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        String stsServer = API.BASE_URL + API.URL_ALITOKEN;
        //推荐使用OSSAuthCredentialsProvider。token过期可以及时更新
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(MainApp.getInstance().getApplicationContext(), endpoint, credentialProvider);
        OSSLog.enableLog();
        return oss;
    }
}
