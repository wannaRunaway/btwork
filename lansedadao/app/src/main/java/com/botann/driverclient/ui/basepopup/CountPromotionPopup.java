package com.botann.driverclient.ui.basepopup;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.botann.driverclient.R;
import razerdp.basepopup.BasePopupWindow;

public class CountPromotionPopup extends BasePopupWindow {
    public CountPromotionPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        bindEvent();
    }
    /**
     * 设置点击事件
     */
    public TextView tv_carplatenum, tv_time, tv_totalmoney, tv_precaution_add, tv_buy,
             tv_warning, tv_user_info, tv_activity_time, tv1, tv2;
    public RelativeLayout re;
    private void bindEvent() {
        tv_carplatenum = findViewById(R.id.tv_carplatenum);
        tv_time = findViewById(R.id.tv_time);
        tv_totalmoney = findViewById(R.id.tv_totalmoney);
        tv_precaution_add = findViewById(R.id.tv_precaution_add);
        tv_buy = findViewById(R.id.tv_buy);
        re = findViewById(R.id.re);
        tv_warning = findViewById(R.id.tv_warning);
        tv_user_info = findViewById(R.id.tv_user_info);
        tv_activity_time = findViewById(R.id.tv_activity_time_region);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
    }
    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_count);
    }
    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 200);
    }
    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 200);
    }
}
