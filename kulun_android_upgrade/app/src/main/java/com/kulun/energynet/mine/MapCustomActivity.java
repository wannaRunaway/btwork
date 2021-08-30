package com.kulun.energynet.mine;

import android.os.Bundle;
import android.view.WindowManager;
import com.amap.api.navi.AmapRouteActivity;
//解决高德地图自动熄屏的问题
public class MapCustomActivity extends AmapRouteActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
