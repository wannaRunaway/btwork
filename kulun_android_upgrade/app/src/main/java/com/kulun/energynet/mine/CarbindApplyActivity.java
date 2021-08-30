package com.kulun.energynet.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityCarBindApplyBinding;
import com.kulun.energynet.databinding.AdapterCarBindBinding;
import com.kulun.energynet.model.CarApply;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.GsonUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class CarbindApplyActivity extends AppCompatActivity {
    private ActivityCarBindApplyBinding activityCarBindApplyBinding;
    private MyAdapter adapter;
    private List<CarApply> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCarBindApplyBinding = DataBindingUtil.setContentView(this, R.layout.activity_car_bind_apply);
        activityCarBindApplyBinding.title.left.setOnClickListener(v -> finish());
        activityCarBindApplyBinding.title.title.setText("绑定申请");
        adapter = new MyAdapter(list);
        activityCarBindApplyBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityCarBindApplyBinding.recyclerView.setAdapter(adapter);
        activityCarBindApplyBinding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                load();
            }
        });
        load();
    }

    private void load(){
        new MyRequest().myRequest(API.carapplylist, false, null, false, this, null,
                activityCarBindApplyBinding.smartRefresh, new Response() {
                    @Override
                    public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                        if (isNull){
                            activityCarBindApplyBinding.image.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                        }
                        if (jsonArray != null) {
                            list.clear();
                            list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<CarApply>>() {
                            }.getType()));
                            activityCarBindApplyBinding.image.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<CarApply> list;

        public MyAdapter(List<CarApply> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CarbindApplyActivity.this).inflate(R.layout.adapter_car_bind, null);
            AdapterCarBindBinding binding = AdapterCarBindBinding.bind(view);
            MyViewHolder holder = new MyViewHolder(view);
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            CarApply carApply = list.get(position);
            holder.getBinding().plate.setText("车牌号:"+carApply.getPlate()+"   "+carApply.getCar_type());
            holder.getBinding().time.setText(carApply.getApply_time());
            String status = "";//status=0申请中status=1通过status=2失败
            switch (carApply.getStatus()) {
                case 0:
                    status = "审核中";
                    holder.getBinding().status.setTextColor(getResources().getColor(R.color.black));
                    holder.getBinding().view.setVisibility(View.GONE);
                    holder.getBinding().reason.setVisibility(View.GONE);
                    break;
                case 1:
                    status = "通过";
                    holder.getBinding().status.setTextColor(getResources().getColor(R.color.black));
                    holder.getBinding().view.setVisibility(View.GONE);
                    holder.getBinding().reason.setVisibility(View.GONE);
                    break;
                case 2:
                    holder.getBinding().status.setTextColor(getResources().getColor(R.color.red));
                    if (TextUtils.isEmpty(carApply.getReason())){//reason为空则不显示
                        holder.getBinding().view.setVisibility(View.GONE);
                        holder.getBinding().reason.setVisibility(View.GONE);
                    }else {
                        holder.getBinding().view.setVisibility(View.VISIBLE);
                        holder.getBinding().reason.setVisibility(View.VISIBLE);
                        holder.getBinding().reason.setText(carApply.getReason());
                    }
                    status = "未通过";
                    break;
                default:
                    break;
            }
            holder.getBinding().status.setText(status);
            String typestatus = "";
            switch (carApply.getApply_type()){//0新增绑定 1换车解绑  2离职解绑
                case 0:
                    typestatus = "新增绑定";
                    break;
                case 1:
                    typestatus = "换车解绑";
                    break;
                case 2:
                    typestatus = "解绑";
                    break;
                default:
                    break;
            }
            holder.getBinding().statusType.setText(typestatus);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterCarBindBinding binding;

        public AdapterCarBindBinding getBinding() {
            return binding;
        }

        public void setBinding(AdapterCarBindBinding binding) {
            this.binding = binding;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
