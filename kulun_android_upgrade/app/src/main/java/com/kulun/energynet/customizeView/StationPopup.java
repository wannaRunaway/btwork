package com.kulun.energynet.customizeView;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kulun.energynet.R;
import com.kulun.energynet.model.StationInfo;

import razerdp.basepopup.BasePopupWindow;
public class StationPopup extends BasePopupWindow {

    public TextView name,address,phone,worktime,distance,paidui,kucun,time;
    public TextView delayappointcancel,delayappoint,appoint;
    public LinearLayout redelayDismiss;
    private StationInfo stationInfo;
    public StationPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        initView();
    }

    public void setStation(StationInfo station){
        this.stationInfo = station;
    }

    public StationInfo getStationInfo(){
        return stationInfo;
    }

    private void initView() {
        name = findViewById(R.id.tv_name);
        address = findViewById(R.id.tv_address);
        phone = findViewById(R.id.tv_phone);
        worktime = findViewById(R.id.tv_clock);
        distance = findViewById(R.id.tv_distance);
        paidui = findViewById(R.id.tv_paidui);
        kucun = findViewById(R.id.tv_kucun);
        delayappoint = findViewById(R.id.img_yuyue_delay);
        appoint = findViewById(R.id.img_yuyue_imm);
        delayappointcancel = findViewById(R.id.img_yuyue_cancel);
        redelayDismiss = findViewById(R.id.re_bottom);
        time = findViewById(R.id.tv_count_time);
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
