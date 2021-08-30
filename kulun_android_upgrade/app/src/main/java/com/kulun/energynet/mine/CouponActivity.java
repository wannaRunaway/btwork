package com.kulun.energynet.mine;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityCouponBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.Coupon;
import com.kulun.energynet.requestparams.ResponseCode;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.DateUtils;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.requestparams.MyRequest;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CouponActivity extends BaseActivity {
    private ActivityCouponBinding binding;
    private Myadapter myadapter;
    private int pageNo = 0;
    private List<Coupon> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon);
        binding.header.title.setText("优惠券");
        binding.header.left.setOnClickListener(view -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myadapter = new Myadapter(list);
        binding.recyclerView.setAdapter(myadapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 0;
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

    //type[int]		默认0，可用优惠券；1，失效的优惠券
    //pageNo[int]
    //pageSize[int]
    private void loadData(boolean isRefresh) {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(pageNo));
        new MyRequest().myRequestNoBack(API.couponlist, true, map, false, this, null, binding.smartRefresh, new ResponseCode() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull, int code, String message) {
                if (code != 0) {
                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                }
                if (jsonArray != null) {
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Coupon>>() {
                    }.getType()));
                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                    myadapter.notifyDataSetChanged();
                }
            }
        });
    }

    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Coupon> adapterlist;

        public Myadapter(List<Coupon> list) {
            adapterlist = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CouponActivity.this).inflate(R.layout.adapter_coupon, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Coupon data = list.get(position);
            holder.name.setText(data.getCouponName());
            holder.money.setText(data.getAmount() + "元");
            holder.text.setText("· 剩余" + (data.getAmount() - data.getUsed()) + "元未使用");
            holder.time.setText("· " + DateUtils.timeToDate(data.getBeginDate()) + "至" + DateUtils.timeToDate(data.getExpireDate()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, money, text;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            money = itemView.findViewById(R.id.tv_money);
            text = itemView.findViewById(R.id.text);
        }
    }
}
