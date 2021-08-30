package com.btkj.chongdianbao.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityRechargeRecordBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.RechargeListModel;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.DateUtils;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeRecordActivity extends BaseActivity {
    private ActivityRechargeRecordBinding binding;
    private int pageNo = 1;
    private Myadapter myadapter;
    private List<RechargeListModel.ContentBean> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recharge_record);
        binding.header.title.setText("充值记录");
        binding.header.left.setOnClickListener(view -> finish());
        binding.recharge.setOnClickListener(view -> {
            Intent intent = new Intent(RechargeRecordActivity.this, RechargeActivity.class);
            startActivity(intent);
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myadapter = new Myadapter();
        binding.recyclerView.setAdapter(myadapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true);
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false);
            }
        });
        loadData(true);
    }

    //pageNo[int]	是	第几页，默认1
    //pageSize[int]	是	页/条，默认20
    private void loadData(boolean isRefresh) {
        String url = API.BASE_URL + API.RECHARGELIST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, RechargeRecordActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                RechargeListModel model = GsonUtils.getInstance().fromJson(json, RechargeListModel.class);
                if (isRefresh) {
                    list.clear();
                }
                list.addAll(model.getContent());
                binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                myadapter.notifyDataSetChanged();
            }
        },binding.smartRefresh);
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                finishLoad();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                RechargeListModel model = GsonUtils.getInstance().fromJson(json, RechargeListModel.class);
//                if (model.isSuccess()) {
//                    if (isRefresh) {
//                        list.clear();
//                    }
//                    list.addAll(model.getContent());
//                    binding.imageBack.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
//                    myadapter.notifyDataSetChanged();
//                } else {
//                    Utils.snackbar(getApplicationContext(), RechargeRecordActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                finishLoad();
//                Utils.log(url, requestParams, null);
//                Utils.snackbar(getApplicationContext(), RechargeRecordActivity.this, API.net_error);
//            }
//        });
    }

    private void finishLoad() {
        binding.smartRefresh.finishLoadMore();
        binding.smartRefresh.finishRefresh();
    }

    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RechargeRecordActivity.this).inflate(R.layout.adapter_money, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            RechargeListModel.ContentBean data = list.get(position);
            //0现金, 1支付宝, 2微信, 3银行卡
            switch (data.getPayWay()) {
                case 0:
                    holder.name.setText("现金");
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.recharge_record_cash), null,null,null);
                    break;
                case 1:
                    holder.name.setText("支付宝");
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.recharge_type_alipay), null,null,null);
                    break;
                case 2:
                    holder.name.setText("微信");
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.recharge_type_wechat), null,null,null);
                    break;
                case 3:
                    holder.name.setText("银行卡");
                    break;
                default:
                    break;
            }
            holder.name.setCompoundDrawablePadding(10);
            holder.money.setText(data.getBalance() + "元");
            holder.time.setText(DateUtils.stampToDate(data.getCreateTime()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, money;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            money = itemView.findViewById(R.id.tv_money);
        }
    }
}
