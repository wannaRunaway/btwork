package com.kulun.energynet.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivitySelectRegisterBinding;

/**
 * created by xuedi on 2019/12/23
 */
public class RegisterSelectActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySelectRegisterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_register);
        binding.imgBack.setOnClickListener(this);
        binding.imgChongdian.setOnClickListener(this);
        binding.imgHuandian.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_huandian:
                Intent intent = new Intent(RegisterSelectActivity.this, HuandianRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.img_chongdian:
                Intent intents = new Intent(RegisterSelectActivity.this, ChongdianRegisterActivity.class);
                startActivity(intents);
                break;
            default:
                break;
        }
    }
}
