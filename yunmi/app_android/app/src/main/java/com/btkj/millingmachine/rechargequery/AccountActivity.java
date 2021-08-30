package com.btkj.millingmachine.rechargequery;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityAccountBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.model.VipInquire.VipInquireModel;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/5/4
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityAccountBinding binding;
    private TimeCount timeCount;
    private VipInquireModel vipInquireModel;
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        binding.layoutBack.imgBack.setOnClickListener(this);
        vipInquireModel = (VipInquireModel) getIntent().getSerializableExtra("data");
        binding.tvName.setText("卡主姓名:"+vipInquireModel.getData().getName());
        binding.tvPhone.setText("手机号码:"+vipInquireModel.getData().getPhone());
        binding.tvBalance.setText("卡内余额:"+vipInquireModel.getData().getBalance());
        binding.tvDiscount.setText("尊享折扣:"+vipInquireModel.getData().getDiscount());
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, binding.includeHeader.tvTime, this);
        timeCount.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Utils.toMain(AccountActivity.this);
                break;
            default:
                break;
        }
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
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.includeHeader.imgTopIcon);
            }
            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.layoutBottom.tvPhone.setText("客服电话："+config.getServiceCall());
        }
    }
}
