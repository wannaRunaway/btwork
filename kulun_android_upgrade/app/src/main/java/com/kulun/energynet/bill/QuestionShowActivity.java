package com.kulun.energynet.bill;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityShowActivityBinding;
import com.kulun.energynet.model.Bill;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;

import java.util.HashMap;

public class QuestionShowActivity extends AppCompatActivity {
    private ActivityShowActivityBinding binding;
    private Bill bill;
    private String site;
    private int exId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_activity);
        binding.header.title.setText("问题反馈");
        binding.header.left.setOnClickListener(v -> finish());
        bill = (Bill) getIntent().getSerializableExtra("bill");
        site = getIntent().getStringExtra("site");
        exId = getIntent().getIntExtra("exId", 0);
        binding.station.setText(site+"");
        binding.number.setText("订单号："+getIntent().getStringExtra("orderNo"));
        //{
        //    "bid": 2697877
        //}
        //{
        //    "code": 0,
        //    "data": {
        //        "StarNumber": 0,
        //        "Content": "",
        //        "TagList": "",
        //        "TagContent": ""
        //    }"content": "咯女",
        //        "picture": "202012181595712_1609326107_0.jpeg;202012181595712_1609326107_1.jpeg",
        //        "status": 0,
        //        "handleContent": ""
        //    }
        //}
        HashMap<String,String> map = new HashMap<>();
        map.put("exId", String.valueOf(exId));
        new MyRequest().myRequest(API.questionCheck, true, map, false, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (json.has("content")){
                    binding.question.setText("问题："+json.get("content").getAsString());
                    binding.imageview.setImageResource(R.mipmap.sign_processing);
                }
                if (json.has("handleContent")){
                    binding.fankui.setText("反馈:"+json.get("handleContent").getAsString());
                }
                if (json.has("status")){
                   if (json.get("status").getAsInt()!=0){
                       binding.imageview.setImageResource(R.mipmap.sign_success);
                   }
                }
            }
        });
    }
}
