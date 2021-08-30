package com.kulun.energynet.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.kulun.energynet.R;

import java.lang.ref.WeakReference;

public class TimerCountUtils extends CountDownTimer {
    WeakReference<TextView> mTextView;
    public TimerCountUtils(TextView textView,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = new WeakReference(textView);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(mTextView.get()==null){
            cancel();
            return;
        }
        mTextView.get().setClickable(false);
        mTextView.get().setText(millisUntilFinished/1000+"秒后可重新发送");
        mTextView.get().setBackgroundResource(R.drawable.get_sms_press);
    }

    @Override
    public void onFinish() {
        if(mTextView.get()==null){
            cancel();
            return;
        }
        mTextView.get().setClickable(true);
        mTextView.get().setText("重新获取验证码");
        mTextView.get().setBackgroundResource(R.drawable.get_sms);
    }

    public void cancelTime() {
        this.cancel();
    }
}
