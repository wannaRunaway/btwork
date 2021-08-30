package com.kulun.kulunenergy.refund;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityRefundDetailBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.utils.OnClickEvent;
import com.kulun.kulunenergy.utils.Utils;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefundDetailActivity extends BaseActivity {
    ActivityRefundDetailBinding binding;
    private PicAdapter adapter;
    private List<String> list=new ArrayList<>();
    private int type;
    private int consumeId;
    private double realAmount;
    public static final int REQUSET_REFUND_CODE=50;
//    private StationModel.ContentBean.RefundBean refundBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding= DataBindingUtil.setContentView(RefundDetailActivity.this,R.layout.activity_refund_detail);
        if(getIntent()!=null){
            type=getIntent().getIntExtra("type",0);
            consumeId=getIntent().getIntExtra("consumeId",0);
            realAmount=getIntent().getDoubleExtra("realAmount",0.00);
//            refundBean= (StationModel.ContentBean.RefundBean) getIntent().getSerializableExtra("refundBean");
        }
        binding.header.title.setText("退款申请");
        binding.header.left.setOnClickListener(v -> finish());
        if(type==1){
            binding.tvCancel.setVisibility(View.GONE);
            binding.tvRetry.setVisibility(View.VISIBLE);
//            if(refundBean!=null&&!TextUtils.isEmpty(refundBean.getResult())){
//                binding.rlReason.setVisibility(View.VISIBLE);
//            }else {
//                binding.rlReason.setVisibility(View.GONE);
//            }
        }else if(type==2){
            binding.tvCancel.setVisibility(View.VISIBLE);
            binding.tvRetry.setVisibility(View.GONE);
            binding.rlReason.setVisibility(View.GONE);
        }
        init();
    }

    private void init() {
        binding.tvCancel.setOnClickListener(new OnClickEvent(500) {
            @Override
            public void singleClick(View v) {
                cancelRefund();
            }
        });
        binding.tvRetry.setOnClickListener(view->{
            Intent intent=new Intent(RefundDetailActivity.this,RefundActivity.class);
            intent.putExtra("consumeId",consumeId);
            intent.putExtra("realAmount",realAmount);
            startActivityForResult(intent,REQUSET_REFUND_CODE);
        });
        initRecyclerView();
    }

    private void cancelRefund() {
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
//        map.put("id",String.valueOf(refundBean.getId()));
        String url= API.BASE_URL+API.CANCELREFUND;
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, RefundDetailActivity.this, getApplicationContext(), true, json -> {
            Utils.snackbar(RefundDetailActivity.this,"退款已取消");
            EventBus.getDefault().post("refreshConsume");
            RefundDetailActivity.this.finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==REQUSET_REFUND_CODE){
            finish();
        }
    }

    private void initRecyclerView() {
        adapter=new PicAdapter(this,list, (view, position) -> {
            Intent intent=new Intent(RefundDetailActivity.this, ImagePreActivity.class);
            intent.putStringArrayListExtra("imgList", (ArrayList<String>) list);
            intent.putExtra("position",position);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        binding.rv.setLayoutManager(new GridLayoutManager(this,4));
        binding.rv.setAdapter(adapter);
        initData();
    }

    private void initData() {
//        if(refundBean!=null){
//            binding.tvAmount.setText(FormatUtil.format(refundBean.getValue()));
//            binding.tvReason.setText(String.format(getResources().getString(R.string.refundReason),refundBean.getReason()));
//            binding.tvFailreason.setText(refundBean.getResult());
//            list.clear();
//            for (int i = 0; i <refundBean.getImg_key().size() ; i++) {
//                if(!refundBean.getImg_key().get(i).contains("null")&&!refundBean.getImg_key().get(i).contains("NULL")){
//                    list.add(refundBean.getImg_key().get(i));
//                }
//            }
//            adapter.notifyDataSetChanged();
//        }
    }
}
