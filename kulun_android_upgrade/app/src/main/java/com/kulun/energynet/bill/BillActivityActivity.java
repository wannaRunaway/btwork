package com.kulun.energynet.bill;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.ViewUtils;
import com.kulun.energynet.databinding.ActivityBillActivityBinding;
import com.kulun.energynet.model.Bill;
import com.kulun.energynet.model.BillDetail;

import java.util.List;

public class BillActivityActivity extends AppCompatActivity {
    private ActivityBillActivityBinding binding;
    private Bill bill;
    private List<BillDetail> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_activity);
        binding.header.left.setOnClickListener(v -> finish());
        binding.header.title.setText("账单详情");
        list = (List<BillDetail>) getIntent().getSerializableExtra("data");
        bill = (Bill) getIntent().getSerializableExtra("bill");
        binding.money.setText(bill.getChange_balance() + "元");
        ViewUtils.addView(list,this,binding.line);
    }
}
