package com.kulun.kulunenergy.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityDetailBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.modelnew.Promotions;
import com.kulun.kulunenergy.utils.DateUtils;

/**
 * created by xuedi on 2019/9/6
 */
public class MyActivityDetailActivity extends BaseActivity {
    private Promotions promotions;
    private ActivityDetailBinding binding;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        promotions = (Promotions) getIntent().getSerializableExtra("data");
        binding.header.left.setOnClickListener(view -> finish());
        binding.header.title.setText("活动详情");
        binding.tvName.setText(promotions.getName());
        binding.tvTime.setText(DateUtils.stampToYear(promotions.getStartTime())+"至"+DateUtils.stampToYear(promotions.getEndTime()));
//        binding.tvContent.setText(promotions.getDetail());
    }
}
