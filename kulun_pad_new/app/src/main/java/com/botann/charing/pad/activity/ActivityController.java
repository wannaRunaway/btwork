package com.botann.charing.pad.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liushanguo on 2017/8/11.
 */

public class ActivityController {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }


    public static void toFirstActivity(){
        for (int i=activities.size()-1;i>0;i--){
            Activity activity = activities.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
            if (i==1) break;
        }
    }


    public static void finish(Integer count) {
        for (int i=activities.size()-1;i>0;i--){
            Activity activity = activities.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
                count--;
            }
            if (count==0) break;
        }
    }


    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
