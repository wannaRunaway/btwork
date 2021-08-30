package com.kulun.energynet.ui.basepopup;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.inter.PromotionCarSelect;
import com.kulun.energynet.inter.PromotionsInterface;
import com.kulun.energynet.model.promotions.Promotions;
import com.kulun.energynet.ui.fragment.CarSelectorDialogFragment;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.DateUtils;

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
    public RadioButton rb_thismonth, rb_nextmonth;
    public TextView tv_carplatenum, tv_monthnum, tv_time, tv_totalmoney, tv_precaution_add, tv_buy, tv_money
            , tv_warning, tv_user_info, tv_activity_time;
    public ImageView img_add, img_minus;
    public RelativeLayout re;
    private void bindEvent() {
        img_add = findViewById(R.id.img_add);
        img_minus = findViewById(R.id.img_minus);
        tv_carplatenum = findViewById(R.id.tv_carplatenum);
        tv_monthnum = findViewById(R.id.tv_monthnum);
        tv_time = findViewById(R.id.tv_time);
        tv_totalmoney = findViewById(R.id.tv_totalmoney);
        tv_precaution_add = findViewById(R.id.tv_precaution_add);
        tv_buy = findViewById(R.id.tv_buy);
        tv_money = findViewById(R.id.tv_money);
        rb_thismonth = findViewById(R.id.rb_thismonth);
        rb_nextmonth = findViewById(R.id.rb_nextmonth);
        re = findViewById(R.id.re);
        tv_warning = findViewById(R.id.tv_warning);
        tv_user_info = findViewById(R.id.tv_user_info);
        tv_activity_time = findViewById(R.id.tv_activity_time_region);
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
        return getTranslateVerticalAnimation(1f, 0, 200);
    }
    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 200);
    }
}
