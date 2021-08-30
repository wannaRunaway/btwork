package com.btkj.chongdianbao.application;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastAliPayStyle;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        ToastUtils.initStyle(new ToastAliPayStyle(this));
    }
}
