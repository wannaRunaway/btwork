package com.botann.charing.pad;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.botann.charing.pad.base.GreenDaoHelper;

/**
 * Created by mengchenyun on 2016/11/5.
 */

public class MainApp extends MultiDexApplication {

    public static MainApp instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        GreenDaoHelper.initGreenDao(this);
//        User.getInstance().setUserName(User.getInstance().getPersistUsername());
//        User.getInstance().setPassword(User.getInstance().getPersistPassword());
//        User.getInstance().setUserId(User.getInstance().getPersistUserId());
        registerActivityLifecycleCallbacks(new ActivityLifecycleAdapter() {
            @Override
            public void onActivityCreated(Activity a, Bundle savedInstanceState) {
                a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
    }

    public static MainApp getInstance() {
        return instance;
    }

    class ActivityLifecycleAdapter implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
