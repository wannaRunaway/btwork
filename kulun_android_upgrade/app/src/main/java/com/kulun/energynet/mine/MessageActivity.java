package com.kulun.energynet.mine;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import android.view.View;

import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityMessageBinding;
import com.kulun.energynet.main.BaseActivity;

//import org.greenrobot.eventbus.EventBus;


public class MessageActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMessageBinding binding;
    private MessageSystemFragment messageSystemFragment;
    private MessagePushFragment messagePushFragment;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        binding.header.title.setText("消息通知");
        binding.header.left.setOnClickListener(view -> finish());
//        messagePushFragment = new MessagePushFragment();
//        messageSystemFragment = new MessageSystemFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.re,messagePushFragment).add(R.id.re,messageSystemFragment).commit();
        binding.tvPush.setOnClickListener(this);
        binding.tvSystem.setOnClickListener(this);
        binding.tvSystem.setTextColor(getResources().getColor(R.color.text_color));
        if (messagePushFragment == null) {
            messagePushFragment = new MessagePushFragment();
        }
        if (messageSystemFragment == null) {
            messageSystemFragment = new MessageSystemFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.re, messagePushFragment).add(R.id.re, messageSystemFragment).show(messageSystemFragment).hide(messagePushFragment).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_push:
                binding.tvPush.setTextColor(getResources().getColor(R.color.text_color));
                binding.tvSystem.setTextColor(getResources().getColor(R.color.black));
                if (messagePushFragment == null) {
                    messagePushFragment = new MessagePushFragment();
                }
                if (messageSystemFragment == null) {
                    messageSystemFragment = new MessageSystemFragment();
                }
                if (messageSystemFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(messagePushFragment).hide(messageSystemFragment).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.re, messagePushFragment).add(R.id.re, messageSystemFragment).show(messagePushFragment).hide(messageSystemFragment).commitAllowingStateLoss();
                }
                break;
            case R.id.tv_system:
                binding.tvPush.setTextColor(getResources().getColor(R.color.black));
                binding.tvSystem.setTextColor(getResources().getColor(R.color.text_color));
                if (messagePushFragment == null) {
                    messagePushFragment = new MessagePushFragment();
                }
                if (messageSystemFragment == null) {
                    messageSystemFragment = new MessageSystemFragment();
                }
                if (messageSystemFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(messageSystemFragment).hide(messagePushFragment).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.re, messagePushFragment).add(R.id.re, messageSystemFragment).show(messageSystemFragment).hide(messagePushFragment).commitAllowingStateLoss();
                }
                break;
            default:
                break;
        }
    }
}
