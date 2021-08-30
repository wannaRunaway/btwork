package com.kulun.energynet.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulun.energynet.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * created by xuedi on 2019/1/21
 */
public class PromotionPopup extends BasePopupWindow {
    public PromotionPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        bindEvent();
    }
    /**
     * 设置点击事件
     */
    public TextView now,next,monthnum,carplate,time,info,money, buy;
    public ImageView add, minus;
    public RelativeLayout re;
    private void bindEvent() {
        re = findViewById(R.id.re);
        add = findViewById(R.id.add);
        minus = findViewById(R.id.minus);
        now = findViewById(R.id.now);
        next = findViewById(R.id.next);
        monthnum = findViewById(R.id.num);
        carplate = findViewById(R.id.carplate);
        time = findViewById(R.id.time);
        info = findViewById(R.id.money_info);
        money = findViewById(R.id.money);
        buy = findViewById(R.id.buy);
    }
    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_normal);
    }
    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getShowAnimation();
    }
    @Override
    protected Animation onCreateDismissAnimation() {
        return getDismissAnimation();
    }
}
