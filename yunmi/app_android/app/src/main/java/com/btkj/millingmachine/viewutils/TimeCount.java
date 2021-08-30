package com.btkj.millingmachine.viewutils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;

/**
 * created by xuedi on 2019/5/31
 */
public class TimeCount extends CountDownTimer {
    private TextView textView;
    private TimeFinishInterface finishInterface;
    public TimeCount(long millisInFuture, long countDownInterval, TextView textView, TimeFinishInterface finishInterface) {
        super(millisInFuture, countDownInterval);
        if (textView != null) {
            this.textView = textView;
        }
        this.finishInterface = finishInterface;
    }

    @Override
    public void onTick(long l) {
        if (textView != null) {
            textView.setText(l / 1000 + "s");
        }
    }

    @Override
    public void onFinish() {
        Utils.log("倒计时结束");
        finishInterface.timeFinish();
    }
}
