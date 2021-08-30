package com.btkj.millingmachine.rechargequery;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;

/**
 * created by xuedi on 2019/5/4
 */
public class RechargeFinishActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ImageView img_back, top_icon, bottom_icon;
    private String amount, money;
    private TextView tv_recharge, tv_balance;
    private TimeCount timeCount;
    private TextView tv_time, tv_phone, tv_title;
    @Override
    public void initView() {
        setContentView(R.layout.activity_recharge_finish);
        amount = getIntent().getStringExtra("amount");
        money = getIntent().getStringExtra("money");
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        tv_recharge = findViewById(R.id.tv_recharge);
        tv_balance = findViewById(R.id.tv_balance);
        tv_recharge.setText("本次充值:"+amount+"元");
        tv_balance.setText("当前余额:"+money+"元");
        tv_time = findViewById(R.id.tv_time);
        top_icon = findViewById(R.id.img_top_icon);
        bottom_icon = findViewById(R.id.img_logo_bottom);
        tv_phone = findViewById(R.id.tv_phone);
        tv_title = findViewById(R.id.tv_title);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeCount.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, tv_time, this);
        timeCount.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Utils.toMain(RechargeFinishActivity.this);
                break;
            default:
                break;
        }
    }

    @Override
    public void timeFinish() {
        Utils.toMain(this);
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null){
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), top_icon);
            }
            Utils.Glide(this, config.getLogoImgUrl(), bottom_icon);
            tv_title.setText(config.getLogoTitle());
            tv_phone.setText("客服电话："+config.getServiceCall());
        }
    }
}
