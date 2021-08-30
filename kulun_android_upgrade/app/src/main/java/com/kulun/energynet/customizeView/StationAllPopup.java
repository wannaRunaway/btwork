package com.kulun.energynet.customizeView;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kulun.energynet.R;

import razerdp.basepopup.BasePopupWindow;

public class StationAllPopup extends BasePopupWindow {

    public TextView name, address, phone, worktime;

    public StationAllPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        initView();
    }

    private void initView() {
        name = findViewById(R.id.tv_name);
        address = findViewById(R.id.tv_address);
        phone = findViewById(R.id.tv_phone);
        worktime = findViewById(R.id.tv_clock);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.stationall_map);
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