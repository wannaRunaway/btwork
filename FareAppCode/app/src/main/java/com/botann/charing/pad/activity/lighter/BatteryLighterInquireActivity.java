package com.botann.charing.pad.activity.lighter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.ActivityBatteryLightInquireBinding;
import com.botann.charing.pad.model.ExchangeSite;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * created by xuedi on 2018/11/28
 * 电池驳运查询
 */
public class BatteryLighterInquireActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityBatteryLightInquireBinding binding;
    private AcceptFragment acceptFragment;
    private GiveFragment giveFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_battery_light_inquire);
        binding.btnLeft.setOnClickListener(this);
        binding.tvMainTitle.setText("电池驳运查询(" + User.shared().getStation() + ")");
        binding.tvAccept.setOnClickListener(this);
        binding.tvGive.setOnClickListener(this);
        initTopView();
        initAcceptFragment();
    }

    /**
     * Acceptfragment的初始化
     */
    private void initAcceptFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (acceptFragment == null) {
            acceptFragment = new AcceptFragment();
            fragmentTransaction.add(R.id.frame, acceptFragment);
        }
        if (giveFragment != null) {
            fragmentTransaction.hide(giveFragment);
        }
        fragmentTransaction.show(acceptFragment).commitAllowingStateLoss();
    }

    /*
     giveFragment的初始化
     */
    private void initGiveFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (giveFragment == null) {
            giveFragment = new GiveFragment();
            Bundle bundle = new Bundle();
            bundle.putString("station", getIntent().getStringExtra("station"));
            giveFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frame, giveFragment);
        }
        if (acceptFragment != null) {
            fragmentTransaction.hide(acceptFragment);
        }
        fragmentTransaction.show(giveFragment).commitAllowingStateLoss();
    }

    /**
     * 初始化顶层view
     */
    private void initTopView() {
        binding.tvAccept.setBackgroundResource(R.color.light_top);
        binding.tvGive.setBackgroundResource(R.color.white);
        binding.tvAccept.setTextColor(getResources().getColor(R.color.white));
        binding.tvGive.setTextColor(getResources().getColor(R.color.grape_dark));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_accept:
                initTopView();
                initAcceptFragment();
                break;
            case R.id.tv_give:
                binding.tvAccept.setBackgroundResource(R.color.white);
                binding.tvGive.setBackgroundResource(R.color.light_top);
                binding.tvAccept.setTextColor(getResources().getColor(R.color.grape_dark));
                binding.tvGive.setTextColor(getResources().getColor(R.color.white));
                initGiveFragment();
                break;
            default:
                break;
        }
    }
}
