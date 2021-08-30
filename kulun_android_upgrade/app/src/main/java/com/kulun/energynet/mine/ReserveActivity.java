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
import com.kulun.energynet.databinding.ActivityReserveBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.Reserver;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.requestparams.MyRequest;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ReserveActivity extends BaseActivity implements View.OnClickListener {
    private ActivityReserveBinding binding;
    private List<Reserver> list = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve);
        binding.header.left.setOnClickListener(this);
        binding.header.title.setText("我的预约");
        adapter = new MyAdapter(list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ReserveActivity.this));
        binding.recyclerView.setAdapter(adapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        loadData();
    }

    private void loadData() {
        new MyRequest().myRequest(API.appointlist, false, null, false, this, null, binding.smartRefresh,
                new Response() {
                    @Override
                    public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                        //0预约中，1取消预约，2预约超时，3换电完成
                        if (jsonArray!= null) {
                            list.clear();
                            list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Reserver>>() {
                            }.getType()));
                            adapter.notifyDataSetChanged();
                            binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                        }else {
                            binding.image.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Reserver> adapterList;
        public MyAdapter(List<Reserver> list) {
            adapterList = list;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ReserveActivity.this).inflate(R.layout.adapter_reserver, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Reserver contentBean = list.get(position);
            String status = "";
            switch (contentBean.getStatus()) {//0预约中，1取消预约，2预约超时，3换电完成
                case 0:
                    status = "预约中";
                    break;
                case 1:
                    status = "取消预约";
                    break;
                case 2:
                    status = "预约超时";
                    break;
                case 3:
                    status = "换电完成";
                    break;
                default:
                    break;
            }
            if (status.equals("换电完成") || status.equals("预约中")) {
                holder.tv_status.setTextColor(getResources().getColor(R.color.black));
                holder.tv_time.setTextColor(getResources().getColor(R.color.black));
                holder.tv_station.setTextColor(getResources().getColor(R.color.black));
                holder.tv_plate.setTextColor(getResources().getColor(R.color.black));
            }else {
                holder.tv_status.setTextColor(getResources().getColor(R.color.reserverunfinish));
                holder.tv_time.setTextColor(getResources().getColor(R.color.reserverunfinish));
                holder.tv_station.setTextColor(getResources().getColor(R.color.reserverunfinish));
                holder.tv_plate.setTextColor(getResources().getColor(R.color.reserverunfinish));
            }
            holder.tv_status.setText(status);
            holder.tv_station.setText(contentBean.getSite());
            holder.tv_plate.setText("车牌号："+contentBean.getPlate());
            holder.tv_time.setText("预约时间："+contentBean.getTime());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_station, tv_time, tv_status, tv_plate;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_station = itemView.findViewById(R.id.tv_station);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_plate = itemView.findViewById(R.id.carplate);
        }
    }
}
