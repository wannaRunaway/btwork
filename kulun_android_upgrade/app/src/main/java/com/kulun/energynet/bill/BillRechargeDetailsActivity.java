package com.kulun.energynet.bill;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.ViewUtils;
import com.kulun.energynet.databinding.ActivityBillRechargeDetailsBinding;
import com.kulun.energynet.model.Bill;
import com.kulun.energynet.model.BillDetail;

import java.util.List;

public class BillRechargeDetailsActivity extends AppCompatActivity {//充值账单详情
    private ActivityBillRechargeDetailsBinding binding;
    private List<BillDetail> list;
    private Bill bill;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_bill_recharge_details);
        binding.header.title.setText("账单详情");
        binding.header.left.setOnClickListener(v -> finish());
        list = (List<BillDetail>) getIntent().getSerializableExtra("data");
        bill = (Bill) getIntent().getSerializableExtra("bill");
        switch (bill.getPayType()) {//0微信,1支付宝,2现金
            case 0:
                binding.image.setImageResource(R.mipmap.recharge_type_wechat);
                binding.type.setText("微信充值");
                break;
            case 1:
                binding.image.setImageResource(R.mipmap.recharge_type_alipay);
                binding.type.setText("支付宝充值");
                break;
            case 2:
                binding.type.setText("现金充值");
                binding.image.setImageResource(R.mipmap.bill_recharge);
                break;
            default:break;
        }
        binding.money.setText(bill.getChange_balance()+"元");
        ViewUtils.addView(list,this,binding.line);
    }
//        try {
            //{"bnumber":"UUIDGLixQ20201229144511","changeType":2,"payType":2,"amount":100,"btime":"2020年12月29日 14:45:12"}}
            //changeType：1手机充值，2人工后台充值, 3pad充值
            //payType：0微信,1支付宝,2现金
//            binding.dindan.setText(bill.getBnumber());
//            binding.shijian.setText(bill.getBtime());
//            binding.money.setText("+"+bill.getAmount()+"元");
//            switch ()
//        }catch (Exception e){
//        }
}
