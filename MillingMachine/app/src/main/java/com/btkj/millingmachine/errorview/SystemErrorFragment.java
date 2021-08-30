package com.btkj.millingmachine.errorview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityErrorSystemBinding;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;

/**
 * created by xuedi on 2019/7/22
 */
public class SystemErrorFragment extends Fragment {
    private ActivityErrorSystemBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_error_system, null);
        binding = ActivityErrorSystemBinding.bind(view);
        initHeaderBottom();
        hideTimeBack();
        return view;
    }
    private void hideTimeBack() {
        binding.center.daojishi.setVisibility(View.INVISIBLE);
        binding.center.time.setVisibility(View.INVISIBLE);
        binding.center.back.setVisibility(View.INVISIBLE);
    }
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(getContext(), "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(getContext(), config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(getContext(), config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("服务电话：" + config.getServiceCall());
        }
    }
}
