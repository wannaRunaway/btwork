package com.kulun.kulunenergy.customizeView;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.kulun.kulunenergy.R;

import razerdp.basepopup.BasePopupWindow;

public class StationPopup extends BasePopupWindow {

    public TextView name,address,phone,worktime,distance;
    public StationPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        initView();
    }

    private void initView() {
        name = findViewById(R.id.tv_name);
        address = findViewById(R.id.tv_address);
        phone = findViewById(R.id.tv_phone);
        worktime = findViewById(R.id.tv_clock);
        distance = findViewById(R.id.tv_distance);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.station_map);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 200);
    }
    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 200);
    }
}
