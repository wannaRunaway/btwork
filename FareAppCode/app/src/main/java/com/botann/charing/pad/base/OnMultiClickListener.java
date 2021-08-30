package com.botann.charing.pad.base;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.botann.charing.pad.MainApp;
import com.botann.charing.pad.utils.ToastUtil;

/**
 * created by xuedi on 2018/12/25
 */
public abstract class OnMultiClickListener implements View.OnClickListener{
    private static final int MIN_CLICK_DELAY_TIME = 5000;
    private long lastClickTime = 0;
//    public abstract void onMultiClick(View v);
//    public abstract void onNoneClick(View view);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(v);
        }else {
            onNoneClick(v);
        }
    }

    protected void onMultiClick(View v){

    }

    protected void onNoneClick(View v){

    }
}

