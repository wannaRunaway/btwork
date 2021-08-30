package com.kulun.kulunenergy.mine;

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
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityCouponBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.modelnew.CouponModel;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.DateUtils;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponActivity extends BaseActivity {
    private ActivityCouponBinding binding;
    private Myadapter myadapter;
    private int pageNo = 1;
    private List<CouponModel.DataBean> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon);
        binding.title.setText("优惠券");
        binding.left.setOnClickListener(view -> finish());
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

    //type[int]		默认0，可用优惠券；1，失效的优惠券
    //pageNo[int]
    //pageSize[int]
    private void loadData(boolean isRefresh) {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", String.valueOf(User.getInstance().getAccountId(this)));
        map.put("pageNo", String.valueOf(pageNo));
        new MyRequest().req(API.COUPON_AVA, map, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                if (!json.toString().equals("{}") || !jsonArray.toString().equals("[]")) {
                    CouponModel model = GsonUtils.getInstance().fromJson(json, CouponModel.class);
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(model.getData());
                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                    myadapter.notifyDataSetChanged();
                }
            }
        });
    }

    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CouponActivity.this).inflate(R.layout.adapter_coupon, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            CouponModel.DataBean data = list.get(position);
            holder.name.setText(data.getName());
            holder.money.setText(data.getAmount() + "元");
            holder.time.setText("· "+DateUtils.stampToYear(data.getReceiveBeginTime()) + "至" + DateUtils.stampToYear(data.getReceiveExpireTime()));
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
