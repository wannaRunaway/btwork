package com.btkj.millingmachine.rechargequery;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityRechargeFinishBinding;
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
public class RechargeFinishActivity extends BaseActivity implements TimeFinishInterface {
//    private ImageView img_back, top_icon, bottom_icon;
//    private String amount, money;
//    private TextView tv_recharge, tv_balance;
    private TimeCount timeCount;
    private ActivityRechargeFinishBinding binding;
//    private TextView tv_time, tv_phone, tv_title;
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_recharge_finish);
        binding.center.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.toMain(RechargeFinishActivity.this);
            }
        });
        binding.chongzhi.setText(getIntent().getStringExtra("amount")+"");
        binding.yue.setText(getIntent().getStringExtra("money")+"");
//        amount = getIntent().getStringExtra("amount");
//        money = getIntent().getStringExtra("money");
//        img_back = findViewById(R.id.img_back);
//        img_back.setOnClickListener(this);
//        tv_recharge = findViewById(R.id.tv_recharge);
//        tv_balance = findViewById(R.id.tv_balance);
//        tv_recharge.setText("本次充值:"+amount+"元");
//        tv_balance.setText("当前余额:"+money+"元");
//        tv_time = findViewById(R.id.tv_time);
//        top_icon = findViewById(R.id.img_top_icon);
//        bottom_icon = findViewById(R.id.img_logo_bottom);
//        tv_phone = findViewById(R.id.tv_phone);
//        tv_title = findViewById(R.id.tv_title);
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
        timeCount = new TimeCount(60000, 1000, binding.center.time, this);
        timeCount.start();
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
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), bottom_icon);
//            tv_title.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话："+config.getServiceCall());
        }
    }
}
