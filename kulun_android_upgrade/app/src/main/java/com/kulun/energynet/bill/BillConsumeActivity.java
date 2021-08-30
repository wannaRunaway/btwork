package com.kulun.energynet.bill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.ViewUtils;
import com.kulun.energynet.databinding.ActivityBillConsumeBinding;
import com.kulun.energynet.model.Bill;
import com.kulun.energynet.model.BillDetail;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.Utils;

import java.util.List;

public class BillConsumeActivity extends AppCompatActivity implements View.OnClickListener {//消费账单详情
    private ActivityBillConsumeBinding binding;
    private List<BillDetail> list;
    private Bill bill;
    private boolean questioned, commented;
    private int siteid;
    private String site;
    private int exId;
    private String orderNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bill_consume);
        binding.header.title.setText("账单详情");
        binding.header.left.setOnClickListener(v -> finish());
        binding.zifeiyiwen.setOnClickListener(this);
        binding.pingjia.setOnClickListener(this);
        list = (List<BillDetail>) getIntent().getSerializableExtra("data");
        bill = (Bill) getIntent().getSerializableExtra("bill");
        siteid = getIntent().getIntExtra("siteid", 0);
        orderNo = getIntent().getStringExtra("orderNo");
        site = getIntent().getStringExtra("site");
        exId = getIntent().getIntExtra("exId", 0);
        questioned = getIntent().getBooleanExtra(API.questioned, false);
        commented = getIntent().getBooleanExtra(API.commented, false);
        binding.money.setText(bill.getChange_balance()+"元");
        ViewUtils.addView(list,this,binding.line);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String json = "bid=" + bill.getBid() + "&cType=" + bill.getcType();
        new MyRequest().spliceJson(API.billdetail, false, json, BillConsumeActivity.this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (json.has("questioned")){
                    questioned = json.get("questioned").getAsBoolean();
                }
                if (json.has("commented")){
                    commented = json.get("commented").getAsBoolean();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zifeiyiwen:
                zifeiyiwen();
                break;
            case R.id.pingjia:
                pingjia();
                break;
            default:
                break;
        }
    }

    private void pingjia() {//评价
        if (!commented) {
            Intent intent = new Intent(this, EvaluateActivity.class);
//            intent.putExtra("site",site);
            intent.putExtra("siteid",siteid);
            intent.putExtra("bill", bill);
            intent.putExtra("exId", exId);
            intent.putExtra("site", site);
            intent.putExtra("orderNo", orderNo);
            startActivity(intent);
//            finish();
        }else {
            Utils.snackbar(BillConsumeActivity.this, "您已经评价过了");
        }
    }

    private void zifeiyiwen() {//资费质疑
        if (!questioned) {//疑问
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("bill", bill);
            intent.putExtra("siteid",siteid);
            intent.putExtra("exId", exId);
            intent.putExtra("site", site);
            intent.putExtra("orderNo", orderNo);
            startActivity(intent);
//            finish();
        }else {
            Intent intent = new Intent(this, QuestionShowActivity.class);
            intent.putExtra("bill",bill);
            intent.putExtra("site",site);
            intent.putExtra("exId", exId);
            intent.putExtra("orderNo", orderNo);
            startActivity(intent);
        }
    }
}
