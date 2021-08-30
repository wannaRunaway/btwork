package com.btkj.millingmachine.help;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityHelpDetailsBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import com.bumptech.glide.Glide;

public class HelpDetailsActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityHelpDetailsBinding binding;
    private ConfigModel configModel;
    private TimeCount timeCount;
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_details);
        binding.center.back.setOnClickListener(this);
        configModel = (ConfigModel) getIntent().getSerializableExtra("config");
        if (configModel == null) {
            configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(HelpDetailsActivity.this, "config", ""), ConfigModel.class);
        }
        Glide.with(this).load(configModel.getData().getWxPlatformUrl()).into(binding.gongzhonghao);
        Glide.with(this).load(configModel.getData().getWxMiniprogramUrl()).into(binding.xiaochengxu);
    }

    @Override
    public void initHeaderBottom() {
        Config config = configModel.getData();
        if (config != null){
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话："+config.getServiceCall());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(120000, 1000, binding.center.time, this);
        timeCount.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeCount.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void timeFinish() {
        finish();
    }
}
