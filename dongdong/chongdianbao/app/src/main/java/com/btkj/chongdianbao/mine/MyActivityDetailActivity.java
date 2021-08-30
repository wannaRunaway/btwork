package com.btkj.chongdianbao.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityDetailBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.ActivityModel;
import com.btkj.chongdianbao.utils.DateUtils;

/**
 * created by xuedi on 2019/9/6
 */
public class MyActivityDetailActivity extends BaseActivity {
    private ActivityModel.ContentBean contentBean;
    private ActivityDetailBinding binding;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        contentBean = (ActivityModel.ContentBean) getIntent().getSerializableExtra("data");
        binding.header.left.setOnClickListener(view -> finish());
        binding.header.title.setText("活动详情");
        binding.tvName.setText("活动名称："+contentBean.getName());
        binding.tvTime.setText("活动时间："+ DateUtils.stampToYear(contentBean.getStartTime())+"至"+DateUtils.stampToYear(contentBean.getEndTime()));
        binding.tvContent.setText(contentBean.getDetail());
    }
}
