package com.btkj.millingmachine.errorview;
import android.view.LayoutInflater;
import android.view.View;
import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityErrorSystemBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
/**
 * created by xuedi on 2019/8/30
 */
public class SystemErrorActivity extends BaseActivity {
    private ActivityErrorSystemBinding binding;
    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_error_system, null);
        binding = ActivityErrorSystemBinding.bind(view);
    }

    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.topIcon);
            }
            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.layoutBottom.tvPhone.setText("客服电话：" + config.getServiceCall());
        }
    }
}
