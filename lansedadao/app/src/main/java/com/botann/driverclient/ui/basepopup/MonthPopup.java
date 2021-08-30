package com.botann.driverclient.ui.basepopup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.botann.driverclient.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * created by xuedi on 2019/5/14
 */
public class MonthPopup extends BasePopupWindow {
    public MonthPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        bindEvent();
    }

    public TextView tv1, tv2;

    private void bindEvent() {
        tv1 = findViewById(R.id.tv_month_first);
        tv2 = findViewById(R.id.tv_month_next);
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_month);
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
