package com.kulun.energynet.customizeView;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Vibrator;

import com.kulun.energynet.login.PasswordLoginActivity;
import com.kulun.energynet.main.MainActivity;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.Utils;

//摇一摇监听器
//public class ShakeListener implements SensorEventListener {
//    /**
//     * 检测的时间间隔
//     */
//    static final int UPDATE_INTERVAL = 100;
//    /**
//     * 上一次检测的时间
//     */
//    long mLastUpdateTime;
//    /**
//     * 上一次检测时，加速度在x、y、z方向上的分量，用于和当前加速度比较求差。
//     */
//    float mLastX, mLastY, mLastZ;
//
//    /**
//     * 摇晃检测阈值，决定了对摇晃的敏感程度，越小越敏感。
//     */
//    public int shakeThreshold = 4000;
//    private Activity activity;
//    private Vibrator vibrator;
//
//    public ShakeListener(Activity mainActivity, Vibrator vibrator) {
//        this.activity = mainActivity;
//        this.vibrator = vibrator;
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        long currentTime = System.currentTimeMillis();
//        long diffTime = currentTime - mLastUpdateTime;
//        if (diffTime < UPDATE_INTERVAL) {
//            return;
//        }
//        mLastUpdateTime = currentTime;
//        float x = event.values[0];
//        float y = event.values[1];
//        float z = event.values[2];
//        float deltaX = x - mLastX;
//        float deltaY = y - mLastY;
//        float deltaZ = z - mLastZ;
//        mLastX = x;
//        mLastY = y;
//        mLastZ = z;
//        float delta = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / diffTime * 10000);
//        // 当加速度的差值大于指定的阈值，认为这是一个摇晃
//        if (delta > shakeThreshold) {
//            vibrator.vibrate(200);
//            Utils.log("","",x+"==="+y+"==="+z+"==="+delta);
////            Utils.toLogin(activity);
//            Intent intent = new Intent(activity, PasswordLoginActivity.class);
//            intent.putExtra("url",API.baseurl);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//            activity.finish();
//
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//}
