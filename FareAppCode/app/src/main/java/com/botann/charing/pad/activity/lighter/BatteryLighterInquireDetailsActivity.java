package com.botann.charing.pad.activity.lighter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.ActivityBatteryLightInquireDetailsBinding;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.model.batterylighter.BatteryLighter;
import com.botann.charing.pad.model.batterylighter.BatteryLighterDetail;
import com.botann.charing.pad.utils.DateUtils;
import com.botann.charing.pad.utils.ToastUtil;

import java.util.ArrayList;

/**
 * created by xuedi on 2018/11/28
 */
public class BatteryLighterInquireDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private BatteryLighter batteryLighter;
    private ActivityBatteryLightInquireDetailsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        batteryLighter = (BatteryLighter) getIntent().getSerializableExtra("batteryLighter");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_battery_light_inquire_details);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        binding.btnLeft.setOnClickListener(this);
        binding.btnRight.setOnClickListener(this);
        /**
         * 本站移交不需要接收按钮
         */
        /**
         * 是否接收，状态、日期和右上角按钮显示
         */
        if (batteryLighter.getStatus() == 1) {
            binding.btnRight.setVisibility(View.INVISIBLE);
            binding.tvStatus.setText("状态：已接收");
            binding.tvAcceptTime.setVisibility(View.VISIBLE);
            binding.tvAcceptTime.setText("接收时间：" + DateUtils.stampToDate(batteryLighter.getReceiveTime()));
        } else {
            if (batteryLighter.isIsgive()) {
                binding.btnRight.setVisibility(View.INVISIBLE);
            } else {
                binding.btnRight.setVisibility(View.VISIBLE);
            }
            binding.tvStatus.setText("状态：未接收");
            binding.tvAcceptTime.setVisibility(View.INVISIBLE);
        }
        binding.tvMainTitle.setText("电池驳运查询(" + User.shared().getStation() + ")");
        binding.tvTransferName.setText("移交站点：" + batteryLighter.getTransferSiteName());
        binding.tvTransferTime.setText("移交时间：" + DateUtils.stampToDate(batteryLighter.getTransferTime()));
        binding.tvAcceptName.setText("接收站点：" + batteryLighter.getReceiveSiteName());
        binding.tvCarplate.setText("运输车辆：" + batteryLighter.getCarNumber());
        binding.tvRemarks.setText("备注：" + batteryLighter.getRemark());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new BatteryLighterInquireDetailsAdapter(getDetails(), BatteryLighterInquireDetailsActivity.this));
    }

    /**
     * 电池类型列表
     */
    private ArrayList<BatteryLighterDetail> getDetails() {
        ArrayList<BatteryLighterDetail> list = new ArrayList<>();
        String str[] = batteryLighter.getBatteryDetail().split(";");
        for (int i = 0; i < str.length; i++) {
            String string[] = str[i].split(",");
            if (Integer.parseInt(string[1]) > 0) {
                BatteryLighterDetail batteryLighterDetail = new BatteryLighterDetail();
                String type = "";
                switch (string[0]) {
                    case "1":
                        type = "10度横向";
                        break;
                    case "2":
                        type = "10度异型";
                        break;
                    case "3":
                        type = "12度横向";
                        break;
                    case "4":
                        type = "15度横向";
                        break;
                    case "5":
                        type = "15度异型";
                        break;
                    case "6":
                        type = "820专用";
                        break;
                    default:
                        break;
                }
                batteryLighterDetail.setType(type);
                batteryLighterDetail.setNum(string[1]);
                batteryLighterDetail.setElect(string[2]);
                list.add(batteryLighterDetail);
            }
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                accept();
                break;
            default:
                break;
        }
    }

    /**
     * 接受成功返回接口
     */
    private void accept() {
        URLParams urlParams = new URLParams();
        urlParams.put("receiveAdminUserId", User.shared().getId());
        urlParams.put("receiveAdminUserName", User.shared().getUsername());
        urlParams.put("id", batteryLighter.getId());
        SGHTTPManager.POST(API.URL_UPDATE_RECEIVER, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                Toast toast;
                if (isSuccess) {
                    toast = Toast.makeText(BatteryLighterInquireDetailsActivity.this, "提交成功", Toast.LENGTH_LONG);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else {
                    toast = Toast.makeText(BatteryLighterInquireDetailsActivity.this, userInfo, Toast.LENGTH_LONG);
                }
                ToastUtil.showMyToast(toast, 3000);
            }
        });
    }

    private class BatteryLighterInquireDetailsAdapter extends RecyclerView.Adapter<BatteryLighterInquireDetailsViewHolder> {
        private ArrayList<BatteryLighterDetail> list;
        private Context context;

        public BatteryLighterInquireDetailsAdapter(ArrayList<BatteryLighterDetail> details, Context context) {
            this.list = details;
            this.context = context;
        }

        @Override
        public BatteryLighterInquireDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_batterylighterinquiredetails, null);
            BatteryLighterInquireDetailsViewHolder holder = new BatteryLighterInquireDetailsViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(BatteryLighterInquireDetailsViewHolder holder, int position) {
            BatteryLighterDetail batteryLighterDetail = list.get(position);
            holder.type.setText(batteryLighterDetail.getType());
            holder.num.setText(batteryLighterDetail.getNum());
            holder.elect.setText(batteryLighterDetail.getElect().equals("0") ? "空电" : "满电");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class BatteryLighterInquireDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView type, num, elect;

        public BatteryLighterInquireDetailsViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            num = (TextView) itemView.findViewById(R.id.num);
            elect = (TextView) itemView.findViewById(R.id.elect);
        }
    }
}
