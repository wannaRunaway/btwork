package com.btkj.millingmachine.selftcult;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivitySelfCultBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.SoundPlayUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;

/**
 * created by xuedi on 2019/4/28
 */
public class SelfcultActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivitySelfCultBinding binding;
    private TimeCount timeCount;
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_self_cult);
        binding.center.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.account1.setOnClickListener(this);
        binding.account2.setOnClickListener(this);
        binding.account3.setOnClickListener(this);
        binding.account4.setOnClickListener(this);
        binding.account5.setOnClickListener(this);
        SoundPlayUtils.play(1);
    }

    //初始化头部和底部
    ConfigModel configModel;
    public void initHeaderBottom() {
        configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null){
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottomer.imgLogoBottom);
//            binding.layoutBottomer.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话："+config.getServiceCall());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(60000, 1000, binding.center.time, this);
        timeCount.start();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("price", configModel.getData().getRicePrice());
        switch (view.getId()) {
            case R.id.account1:
                intent.putExtra("weight", 1*500);
                break;
            case R.id.account2:
                intent.putExtra("weight", 2*500);
                break;
            case R.id.account3:
                intent.putExtra("weight", 3*500);
                break;
            case R.id.account4:
                intent.putExtra("weight", 4*500);
                break;
            case R.id.account5:
                intent.putExtra("weight", 5*500);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    public void timeFinish() {
        Utils.toMain(this);
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
}
