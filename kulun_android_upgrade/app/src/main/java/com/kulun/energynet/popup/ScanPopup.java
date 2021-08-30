package com.kulun.energynet.popup;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.TimeFinishInterface;
import com.kulun.energynet.main.MainActivity;
import com.kulun.energynet.model.BillDetail;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class ScanPopup extends BasePopupWindow {
//    private Activity activity;
//    private List<BillDetail> list;
    private View view;
    private TimeCount timeCount;
    public ScanPopup(Activity activity,Context context, List<BillDetail> list, String name, String fare, String appointmentNo, int recordId) {
        super(context);
//        this.activity = activity;
//        this.list = list;
        setPopupGravity(Gravity.BOTTOM);
        LinearLayout linearLayout = view.findViewById(R.id.line);
        TextView station = view.findViewById(R.id.station);
        TextView money = view.findViewById(R.id.money);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);

        timeCount = new TimeCount(30000, confirm, new TimeFinishInterface() {
            @Override
            public void timeFinish() {
                pay(appointmentNo, recordId, activity);
            }
        });
        timeCount.start();
        station.setText(name+"");
        money.setText(fare+"元");
        cancel.setOnClickListener(v -> {
            timeCount.cancel();
            dismiss();
        });
        confirm.setOnClickListener(v -> {
            timeCount.cancel();
            pay(appointmentNo, recordId, activity);
        });
        for (int i = 0; i < list.size(); i++) {
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            TextView textView = new TextView(getContext());
            RelativeLayout.LayoutParams textviewparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textviewparams.setMargins(50,20,0,20);
            textView.setLayoutParams(textviewparams);
            textView.setText(list.get(i).getName()+"");
            relativeLayout.addView(textView);
            //第二个
            TextView textView1 = new TextView(getContext());
            textView1.setText(list.get(i).getValue()+"");
            RelativeLayout.LayoutParams textparams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textparams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textparams1.setMargins(0,20,50,10);
            textView1.setLayoutParams(textparams1);
            relativeLayout.addView(textView1);
            linearLayout.addView(relativeLayout);
        }
    }

    private void pay(String appointmentNo, int recordId, Activity activity) {
        String json = JsonSplice.leftparent+JsonSplice.yin+"appointmentNo"+JsonSplice.yinandmao+JsonSplice.yin+appointmentNo+JsonSplice.yinanddou+
                JsonSplice.yin+"recordId"+JsonSplice.yinandmao+recordId+JsonSplice.rightparent;
        new MyRequest().spliceJson(API.scanpay, true, json, activity, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(activity, "支付成功");
                dismiss();
                ((MainActivity)activity).paysuccessLoad();
            }
        });
    }

//    private void bindEvent() {
//        tv_alipay = findViewById(R.id.tv_alipay);
//        tv_weixing = findViewById(R.id.tv_weixing);
//        tv_yue = findViewById(R.id.tv_yue);
//    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
         view = createPopupById(R.layout.popup_scan);
//         view = LayoutInflater.from(getContext()).inflate(R.layout.popup_scan, null);
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        textView.setLayoutParams(layoutParams);
//        TextView textView1 = new TextView(activity);
//        textView1.setText("测试事实");
//        textView1.setLayoutParams(layoutParams);
        return view;
    }

    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getShowAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDismissAnimation();
    }
    public class TimeCount extends CountDownTimer {
        private TimeFinishInterface finishInterface;
        WeakReference<TextView> mTextView;
        public TimeCount(long millisInFuture, TextView textView, TimeFinishInterface finishInterface) {
            super(millisInFuture, 1000);
            this.mTextView=new WeakReference<>(textView);
            this.finishInterface = finishInterface;
        }

        @Override
        public void onTick(long l) {
            if(mTextView.get()==null){
                this.cancel();
                return;
            }
            mTextView.get().setVisibility(View.VISIBLE);
            mTextView.get().setText("确认（"+ l/1000+")");

        }
        @Override
        public void onFinish() {
            mTextView.get().setText("确认");
            cancelTime();
            finishInterface.timeFinish();
            dismiss();
        }
        public void cancelTime(){
            if(this!=null){
                this.cancel();
            }
        }
    }
}
