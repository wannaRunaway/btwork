package com.kulun.kulunenergy.mine;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityReserveListBinding;
import com.kulun.kulunenergy.main.BaseActivity;

public class ReserveListActivity extends BaseActivity implements View.OnClickListener {
    private ActivityReserveListBinding binding;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_list);
        binding.header.left.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            default:
                break;
        }
    }
}
