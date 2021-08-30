package com.btkj.millingmachine.util;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.rechargequery.AccountActivity;
import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

/**
 * created by xuedi on 2019/4/30
 */
public class Utils {
    public static String TAG = "xuedi";
    public static String TAGS = "xueserial";

    public static void toast(Context context, String message) {
        if (!message.isEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }

    public static void log(String msg) {
        if (!msg.isEmpty()) {
            Log.d(Utils.TAG, msg);
        }
    }

    //微信支付宝订单隔500ms查询一次，log日志显示过多
    public static void logserial(String msg) {
        if (!msg.isEmpty()) {
            Log.d(Utils.TAGS, msg);
        }
    }

    public static void toMain(Context context){
        /**
         * Intent message = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
         *         message.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
         *         MainApp.getInstance().getApplicationContext().startActivity(message);
         */
        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    //glide加载
    public static void Glide(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
