package com.kulun.energynet.bill;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.ViewUtils;
import com.kulun.energynet.databinding.ActivityBillActivityBinding;
import com.kulun.energynet.databinding.ActivityBillRefundBinding;
import com.kulun.energynet.model.Bill;
import com.kulun.energynet.model.BillDetail;

import java.util.List;

public class BillRefundActivity extends AppCompatActivity {//退款账单详情
    private ActivityBillRefundBinding binding;
    private List<BillDetail> list;
    private Bill bill;
    //账单退款详情 返回status字段0退款中，1退款成功  2退款失败
    //  intentrefund.putExtra("status", json.get("status").getAsInt());
    //                                        intentrefund.putExtra("amount", json.get("amount").getAsDouble());
    private int status;
    private double amount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_bill_refund);
        binding.header.title.setText("账单详情");
        binding.header.left.setOnClickListener(v -> finish());
        list = (List<BillDetail>) getIntent().getSerializableExtra("data");
        bill = (Bill) getIntent().getSerializableExtra("bill");
        status = getIntent().getIntExtra("status", -1);
        amount = getIntent().getDoubleExtra("amount", 0);
        binding.money.setText(amount+"元");
        String s = "";
        switch (status){
            case 0:
                s="退款中";
                binding.type.setTextColor(getResources().getColor(R.color.yellow));
                break;
            case 1:
                s="退款成功";
                binding.type.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                s="退款失败";
                binding.type.setTextColor(getResources().getColor(R.color.red));
                break;
            default:
                break;
        }
        binding.type.setText(s);
        ViewUtils.addView(list,this,binding.line);
    }
}
