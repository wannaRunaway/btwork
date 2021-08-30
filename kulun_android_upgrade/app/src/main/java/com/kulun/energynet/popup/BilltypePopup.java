package com.kulun.energynet.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.kulun.energynet.R;
import com.kulun.energynet.bill.TypePopclick;
import com.kulun.energynet.databinding.PopupBillBinding;

import razerdp.basepopup.BasePopupWindow;

public class BilltypePopup extends BasePopupWindow {
    private TypePopclick typePopclick;

    public BilltypePopup(Context context, TypePopclick typePopclick) {
        super(context);
        setPopupGravity(Gravity.TOP);
        this.typePopclick = typePopclick;
    }

    /**
     * 设置点击事件
     */
    public PopupBillBinding typebinding;

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.popup_bill);
        typebinding = PopupBillBinding.bind(view);
        typebinding.chongzhi.setOnClickListener(v -> {
            typePopclick.billpopclick("充值");
            dismiss();
        });
        typebinding.quanbu.setOnClickListener(v -> {
            typePopclick.billpopclick("全部");
            dismiss();
        });
        typebinding.xiaofei.setOnClickListener(v ->
        {
            typePopclick.billpopclick("消费");
            dismiss();
        });
        typebinding.tuikuan.setOnClickListener(v ->
        {
            typePopclick.billpopclick("退款");
            dismiss();
        });
        typebinding.huodong.setOnClickListener(v -> {
            typePopclick.billpopclick("活动");
            dismiss();
        });
        typebinding.header.title.setText("我的账单");
//        typebinding.money.setText(money+"");
        typebinding.header.left.setOnClickListener(v -> dismiss());
        return view;
    }

    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
//        return getTranslateVerticalAnimation(1f, 0, 200);
        return getShowAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
//        return getTranslateVerticalAnimation(0, 1f, 200);
        return getDismissAnimation();
    }
}
