package com.kulun.kulunenergy.mine;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityMyActivityBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.modelnew.PromotionContent;
import com.kulun.kulunenergy.modelnew.Promotions;
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

public class MyActivityListActivity extends BaseActivity {

    private ActivityMyActivityBinding binding;
    private int pageNo = 1;
    private Myadapter myadapter;
    private List<Promotions> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_activity);
        binding.header.title.setText("活动列表");
        binding.header.left.setOnClickListener(view -> finish());
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
        Map<String, String> map = new HashMap<>();
        map.put("accountId", String.valueOf(User.getInstance().getAccountId(this)));
        map.put("pageNo", String.valueOf(pageNo));
        new MyRequest().req(API.URL_PROMOTIONS, map, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                if (!json.toString().equals("{}") || !jsonArray.toString().equals("[]")) {
                    PromotionContent promotionContent = GsonUtils.getInstance().fromJson(json, PromotionContent.class);
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(promotionContent.getData());
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
            View view = LayoutInflater.from(MyActivityListActivity.this).inflate(R.layout.adapter_activity, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Promotions data = list.get(position);
            holder.name.setText(data.getName());
            holder.time.setText(DateUtils.stampToYear(data.getStartTime()) + "至" + DateUtils.stampToYear(data.getEndTime()));
            holder.re.setOnClickListener(view -> {
                Intent intent = new Intent(MyActivityListActivity.this, MyActivityDetailActivity.class);
                intent.putExtra("data", list.get(position));
                startActivity(intent);
            });
            holder.text1.setText("费用:"+data.getPackagePrice());
            holder.text2.setText("");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, text1, text2;
        private RelativeLayout re;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            re = itemView.findViewById(R.id.re);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }
}
