package com.botann.driverclient.ui.activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityCouponBinding;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.fragment.AccountFragment;
import com.botann.driverclient.ui.fragment.ConsumeFragment;
import com.botann.driverclient.ui.fragment.CouponFragment;
import com.botann.driverclient.ui.fragment.CouponReceiveFragment;
import com.botann.driverclient.ui.fragment.RechargeFragment;
import com.botann.driverclient.ui.fragment.StationFragment;
import com.botann.driverclient.utils.Constants;

/**
 * Created by Orion on 2017/8/3.
 */
public class CouponActivity extends BaseActivity implements View.OnClickListener {
    private CouponFragment couponFragment;
    private CouponReceiveFragment couponReceiveFragment;
    private ActivityCouponBinding binding;
    private boolean iscoupon, ischongdian; //点击按钮true  点击红包false
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon);
        binding.couponBack.setOnClickListener(this);
        binding.canReceive.setOnClickListener(this);
        binding.canUse.setOnClickListener(this);
        iscoupon = getIntent().getBooleanExtra(API.coupon, true);
        ischongdian = getIntent().getBooleanExtra(API.chongdian, false);
        showFragment(iscoupon);
    }

    private void changeLine(boolean iscoupon){
        if (iscoupon){
            binding.lineReceive.setBackgroundColor(getResources().getColor(R.color.text_white));
            binding.lineUse.setBackgroundColor(getResources().getColor(R.color.transparent));
            binding.canUse.setTextColor(getResources().getColor(R.color.transparent));
            binding.canReceive.setTextColor(getResources().getColor(R.color.black));
        }else {
            binding.lineUse.setBackgroundColor(getResources().getColor(R.color.text_white));
            binding.lineReceive.setBackgroundColor(getResources().getColor(R.color.transparent));
            binding.canReceive.setTextColor(getResources().getColor(R.color.transparent));
            binding.canUse.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void hideAll(FragmentTransaction fragmentTransaction) {
        if (couponFragment != null) {
            fragmentTransaction.hide(couponFragment);
        }
        if (couponReceiveFragment != null) {
            fragmentTransaction.hide(couponReceiveFragment);
        }
    }

    private void showFragment(boolean iscoupon) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAll(fragmentTransaction);
        if (iscoupon){
            if (couponFragment == null) {
                couponFragment = new CouponFragment();
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(API.chongdian, ischongdian);
            couponFragment.setArguments(bundle);
            if (couponFragment.isAdded()) {
                fragmentTransaction.show(couponFragment).commitAllowingStateLoss();
            } else {
                fragmentTransaction.add(R.id.content, couponFragment).show(couponFragment).commitAllowingStateLoss();
            }
            changeLine(true);
        }else {
            if (couponReceiveFragment == null) {
                couponReceiveFragment = new CouponReceiveFragment();
            }
            if (couponReceiveFragment.isAdded()) {
                fragmentTransaction.show(couponReceiveFragment).commitAllowingStateLoss();
            } else {
                fragmentTransaction.add(R.id.content, couponReceiveFragment).show(couponReceiveFragment).commitAllowingStateLoss();
            }
            changeLine(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coupon_back:
                finish();
                break;
            case R.id.can_receive:
                showFragment(false);
                break;
            case R.id.can_use:
                showFragment(true);
                break;
            default:
                break;
        }
    }
}
