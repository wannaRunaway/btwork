package com.kulun.energynet.main;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulun.energynet.customizeView.TimeFinishInterface;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.DateUtils;

import java.lang.ref.WeakReference;

/**
 * created by xuedi on 2019/5/31
 */
public class TimeCount extends CountDownTimer {
    private TimeFinishInterface finishInterface;
    WeakReference<TextView> mTextView;
    public TimeCount(long millisInFuture, TextView textView, TimeFinishInterface finishInterface) {
        super(millisInFuture, 1000);
        this.mTextView=new WeakReference<>(textView);
//        this.mImageView=new WeakReference<>(imageView);
        this.finishInterface = finishInterface;
    }

    @Override
    public void onTick(long l) {
        if(mTextView.get()==null){
            this.cancel();
            return;
        }
//        mTextView.get().setVisibility(View.VISIBLE);
        mTextView.get().setText("距离预约超时还剩：："+DateUtils.getTimeString((int) (l / 1000)));
//        mImageView.get().setVisibility(View.GONE);

    }
    @Override
    public void onFinish() {
        mTextView.get().setText("预约超时！");
//        mImageView.get().setVisibility(View.VISIBLE);
        finishInterface.timeFinish();

    }
    public void cancelTime(){
        if(this!=null){
            this.cancel();
        }
    }
}
