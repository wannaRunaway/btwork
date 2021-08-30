package com.kulun.kulunenergy.main;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulun.kulunenergy.customizeView.TimeFinishInterface;
import com.kulun.kulunenergy.utils.DateUtils;

import java.lang.ref.WeakReference;

/**
 * created by xuedi on 2019/5/31
 */
public class TimeCount extends CountDownTimer {
    private TimeFinishInterface finishInterface;
    WeakReference<TextView> mTextView;
    WeakReference<ImageView> mImageView;
    public TimeCount(long millisInFuture, TextView textView,ImageView imageView, TimeFinishInterface finishInterface) {
        super(millisInFuture, 1000);
        this.mTextView=new WeakReference<>(textView);
        this.mImageView=new WeakReference<>(imageView);
        this.finishInterface = finishInterface;
    }
    @Override
    public void onTick(long l) {
        if(mTextView.get()==null||mImageView.get()==null){
            this.cancel();
            return;
        }
        mTextView.get().setVisibility(View.VISIBLE);
        mTextView.get().setText("预约倒计时："+DateUtils.getTimeString((int) (l / 1000)));
        mImageView.get().setVisibility(View.GONE);

    }
    @Override
    public void onFinish() {
        mTextView.get().setText("预约超时！");
        mImageView.get().setVisibility(View.VISIBLE);
        finishInterface.timeFinish();

    }
    public void cancelTime(){
        if(this!=null){
            this.cancel();
        }
    }
}
