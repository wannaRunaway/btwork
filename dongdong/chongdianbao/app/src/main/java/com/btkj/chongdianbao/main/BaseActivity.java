package com.btkj.chongdianbao.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import qiu.niorgai.StatusBarCompat;

/**
 * created by xuedi on 2019/7/31
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeToLightStatusBar(this);
        initView(savedInstanceState);
    }
    public static void changeToLightStatusBar(@NonNull Activity activity) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return;
            }
            if (activity == null) {
                return;
            }
            StatusBarCompat.setStatusBarColor(activity,Color.WHITE);
            Window window = activity.getWindow();
            if (window == null) {
                return;
            }

            View decorView = window.getDecorView();
            if (decorView == null) {
                return;
            }
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public abstract void initView(Bundle savedInstanceState);
}
