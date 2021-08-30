package com.btkj.chongdianbao.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityConsumeBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.StationModel;
import com.btkj.chongdianbao.refund.RefundActivity;
import com.btkj.chongdianbao.refund.RefundDetailActivity;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.DateUtils;
import com.btkj.chongdianbao.utils.FormatUtil;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.L;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumeActivity extends BaseActivity {
    private ActivityConsumeBinding binding;
    private int pageNo = 1;
    private Myadapter myadapter;
    private List<StationModel.ContentBean> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_consume);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        binding.header.title.setText("消费记录");
        binding.header.left.setOnClickListener(view -> finish());
        if (getIntent().getBooleanExtra(API.isfromScan, false)){
            Utils.snackbar(getApplicationContext(), ConsumeActivity.this, "换电成功");
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myadapter = new Myadapter();
        binding.recyclerView.setAdapter(myadapter);
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            pageNo = 1;
            loadData(true);
        });
        binding.smartRefresh.setOnLoadMoreListener(refreshLayout -> {
            pageNo = pageNo + 1;
            loadData(false);
        });
        loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String s){
        if(!TextUtils.isEmpty(s)&&s.equals("refreshConsume")){
            loadData(true);
        }
    }

    @Override
    protected void onDestroy() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    //stationId[int]
    //pageNo[int]
    //pageSize[int]
    private void loadData(boolean isRefresh) {
        String url = API.BASE_URL + API.EXCHANGELIST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, ConsumeActivity.this, getApplicationContext(), true, json -> {
            StationModel model = GsonUtils.getInstance().fromJson(json, StationModel.class);
            if (isRefresh) {
                list.clear();
            }
            list.addAll(model.getContent());
            binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
            myadapter.notifyDataSetChanged();
        }, binding.smartRefresh);
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                finishLoad();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                StationModel model = GsonUtils.getInstance().fromJson(json, StationModel.class);
//                if (model.isSuccess()) {
//                    if (isRefresh) {
//                        list.clear();
//                    }
//                    list.addAll(model.getContent());
//                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
//                    myadapter.notifyDataSetChanged();
//                } else {
//                    Utils.snackbar(getApplicationContext(), ConsumeActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                finishLoad();
//                Utils.log(url, requestParams, null);
//                Utils.snackbar(getApplicationContext(), ConsumeActivity.this, API.net_error);
//            }
//        });
    }

    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ConsumeActivity.this).inflate(R.layout.adapter_station_list, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            StationModel.ContentBean data = list.get(position);
            StationModel.ContentBean.RefundBean refundBean=data.getRefundBean();
            holder.order.setText("订单号："+data.getSerialNo());
            holder.time.setText(DateUtils.stampToDate(data.getCreateTime()));
            holder.carplate.setText("车牌号："+data.getPlateNo());
            holder.station.setText(data.getStationName());
            holder.battery.setText("换上电池："+data.getBatteryCount()+"x"+data.getBatteryType()+"度");
            holder.socTv.setText("换下SOC："+FormatUtil.socformat(data.getSoc()));
            if(data.getStatus()==1){
                holder.applyTv.setVisibility(View.VISIBLE);
                holder.refundingTv.setVisibility(View.GONE);
                holder.finishTv.setVisibility(View.GONE);
                holder.failTv.setVisibility(View.GONE);
                holder.content.setText("实际支付/优惠券/余额   "+ FormatUtil.format(data.getReal())+"/"+FormatUtil.format(data.getCoupon())+"/"+FormatUtil.format(data.getAfterBalance()));
            }else if(data.getStatus()==2){
                holder.refundingTv.setVisibility(View.VISIBLE);
                holder.finishTv.setVisibility(View.GONE);
                holder.failTv.setVisibility(View.GONE);
                holder.applyTv.setVisibility(View.GONE);
                holder.content.setText("实际支付/优惠券/余额   "+ FormatUtil.format(data.getReal())+"/"+FormatUtil.format(data.getCoupon())+"/"+FormatUtil.format(data.getAfterBalance()));
            }else if(data.getStatus()==3&&refundBean!=null){
                holder.applyTv.setVisibility(View.GONE);
                if(refundBean.getStatus()==1){
                    holder.refundingTv.setVisibility(View.GONE);
                    holder.finishTv.setVisibility(View.VISIBLE);
                    holder.failTv.setVisibility(View.GONE);
                    holder.content.setText("实际支付/优惠券/余额/退款   "+ FormatUtil.format(data.getReal())+"/"+FormatUtil.format(data.getCoupon())+"/"+FormatUtil.format(data.getAfterBalance())+"/"+FormatUtil.format(refundBean.getValue()));
                }else if(refundBean.getStatus()==2){
                    holder.refundingTv.setVisibility(View.GONE);
                    holder.finishTv.setVisibility(View.GONE);
                    holder.failTv.setVisibility(View.VISIBLE);
                    holder.content.setText("实际支付/优惠券/余额   "+ FormatUtil.format(data.getReal())+"/"+FormatUtil.format(data.getCoupon())+"/"+FormatUtil.format(data.getAfterBalance()));
                }
            }
            holder.applyTv.setOnClickListener(view->{
                Intent intent=new Intent(ConsumeActivity.this, RefundActivity.class);
                intent.putExtra("consumeId",data.getId());
                intent.putExtra("realAmount",data.getReal());
                startActivity(intent);
            });
            holder.failTv.setOnClickListener(view->{
                Intent intent=new Intent(ConsumeActivity.this, RefundDetailActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("consumeId",data.getId());
                intent.putExtra("realAmount",data.getReal());
                if(data.getRefundBean()!=null){
                    intent.putExtra("refundBean",data.getRefundBean());
                }
                startActivity(intent);
            });
            holder.refundingTv.setOnClickListener(view->{
                Intent intent=new Intent(ConsumeActivity.this, RefundDetailActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("consumeId",data.getId());
                intent.putExtra("realAmount",data.getReal());
                if(data.getRefundBean()!=null){
                    intent.putExtra("refundBean",data.getRefundBean());
                }
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView order, time, carplate, station, battery, content,applyTv,refundingTv,finishTv,failTv,socTv;
        MyViewHolder(View itemView) {
            super(itemView);
            order = itemView.findViewById(R.id.tv_order);
            time = itemView.findViewById(R.id.tv_time);
            carplate = itemView.findViewById(R.id.tv_car_plate);
            station = itemView.findViewById(R.id.tv_station);
            battery = itemView.findViewById(R.id.tv_battery);
            content = itemView.findViewById(R.id.tv_content);
            applyTv=itemView.findViewById(R.id.tv_apply);
            refundingTv=itemView.findViewById(R.id.tv_refunding);
            finishTv=itemView.findViewById(R.id.tv_refundfinish);
            failTv=itemView.findViewById(R.id.tv_refundfail);
            socTv=itemView.findViewById(R.id.tv_soc);
        }
    }
}
