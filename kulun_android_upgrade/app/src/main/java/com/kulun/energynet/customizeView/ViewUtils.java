package com.kulun.energynet.customizeView;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulun.energynet.R;
import com.kulun.energynet.model.BillDetail;

import java.util.List;

public class ViewUtils {
    public static void addView(List<BillDetail> list, Activity activity, ViewGroup view){
        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getName().equals("第三方凭证号") || list.get(i).getName().equals("套餐有效期")){
//                TextView textView = new TextView(activity);
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(50,20,50,20);
//                textView.setLayoutParams(params);
//                textView.setText(list.get(i).getName()+"");
//                textView.setTextColor(activity.getResources().getColor(R.color.black));
//                view.addView(textView);
//                TextView textView2 = new TextView(activity);//初始化第二个
//                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params2.setMargins(50,20,50,20);
//                params2.addRule(Gravity.RIGHT);
//                textView2.setLayoutParams(params);
//                textView2.setText(list.get(i).getValue()+"");
//                textView2.setTextColor(activity.getResources().getColor(R.color.black));
//                view.addView(textView2);
//            }else {
                RelativeLayout relativeLayout = new RelativeLayout(activity);
//                relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
                relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                relativeLayout.setPadding(0,10,0,10);
                TextView textView = new TextView(activity);
                textView.setId(R.id.textview);
                RelativeLayout.LayoutParams textviewparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textviewparams.setMargins(50, 20, 0, 20);
                textView.setLayoutParams(textviewparams);
                textView.setText(list.get(i).getName() + "");
                textView.setTextSize(12);
                textView.setTextColor(activity.getResources().getColor(R.color.black));
//                textView.setGravity(Gravity.);
                relativeLayout.addView(textView);
                //第二个
                RelativeLayout relativeLayout2 = new RelativeLayout(activity);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams2.addRule(RelativeLayout.RIGHT_OF, textView.getId());
                layoutParams2.setMargins(50,0,0,0);
                relativeLayout2.setLayoutParams(layoutParams2);

                TextView textView1 = new TextView(activity);
                textView1.setText(list.get(i).getValue() + "");
                RelativeLayout.LayoutParams textparams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textparams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                textparams1.setMargins(0, 20, 50, 10);
                textView1.setLayoutParams(textparams1);
                textView1.setTextSize(12);
                textView1.setTextColor(activity.getResources().getColor(R.color.black));
                relativeLayout2.addView(textView1);
                relativeLayout.addView(relativeLayout2);
                view.addView(relativeLayout);
//            }
        }
    }
}
