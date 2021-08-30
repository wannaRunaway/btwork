package com.kulun.kulunenergy.mine;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.kulun.kulunenergy.databinding.ActivityMyCarBinding;
import com.kulun.kulunenergy.databinding.AdapterCarBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.modelnew.Car;
import com.kulun.kulunenergy.modelnew.CarModel;
import com.kulun.kulunenergy.requestparams.Response;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Myparams;
import com.kulun.kulunenergy.utils.SharePref;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCarActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMyCarBinding binding;
    private MyAdapter adapter;
    private int pageNo = 1;
    private List<Car> list = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_car);
        binding.header.left.setOnClickListener(this);
        binding.header.title.setText("我的绑定");
        binding.imgAddCar.setOnClickListener(this);
        adapter = new MyAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyCarActivity.this));
        binding.recyclerView.setAdapter(adapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String s) {
        if ("refreshmycar".equals(s)) {
            pageNo = 1;
            loadData(true);
        }
    }

    private void loadData(boolean isRefresh) {
        Map<String,String> map = new HashMap<>();
        map.put("accountId", String.valueOf(User.getInstance().getAccountId(this)));
        map.put("pageNo", String.valueOf(pageNo));
        new MyRequest().req(API.MYPLATELIST, map, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray) {
                if (!json.toString().equals("{}")) {
                    CarModel model = GsonUtils.getInstance().fromJson(json, CarModel.class);
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(model.getData());
                    adapter.notifyDataSetChanged();
                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
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
            case R.id.img_add_car:
                Intent intent = new Intent(MyCarActivity.this, AddCarActivity.class);
                intent.putExtra(API.islogin, false);
                startActivity(intent);
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyCarActivity.this).inflate(R.layout.adapter_car, parent, false);
            AdapterCarBinding adapterCarBinding = DataBindingUtil.bind(view);
            MyViewHolder holder = new MyViewHolder(view);
            holder.setBinding(adapterCarBinding);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder,int position) {
            MyViewHolder holder1 = holder;
            holder.getBinding().name.setText("车牌号码");
//            holder1.plateNom.setText("车牌号：" + list.get(position).getPlateNo());
//            holder1.carType.setText("车型：" + list.get(position).getCarTypeName());
//            holder1.re_group.setOnClickListener(view -> {
//                new AlertDialog.Builder(MyCarActivity.this).setMessage("您是否删除这辆车").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                        cancelCar(list.get(position).getBindId(), list.get(position).getPlateNo());
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create().show();
//            });
        }

        @Override
        public int getItemCount() {
            return list.size();
//            return 2;
        }
    }

    //bindId[int]	是	绑定ID
    private void cancelCar(int bindId, String plateNo) {
        String url = API.BASE_URL + API.DELETECAR;
        RequestParams requestParams = new RequestParams();
        Map<String,String> map = new HashMap<>();
        map.put("bindId", String.valueOf(bindId));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, MyCarActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                String plate = (String) SharePref.get(MyCarActivity.this, API.plateNo, "");
                if (plate.equals(plateNo)){
                    SharePref.put(MyCarActivity.this, API.plateNo, "");
                    SharePref.put(MyCarActivity.this, API.bindId, 0);
                    SharePref.put(MyCarActivity.this, API.carId, 0);
                    SharePref.put(MyCarActivity.this, API.carType, 0);
                    SharePref.put(MyCarActivity.this, API.batterycount, 0);
                }
                loadData(true);
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterCarBinding adapterCarBinding;
        public MyViewHolder(View view) {
            super(view);
//            this.adapterCarBinding = adapterCarBinding;
//            plateNom = itemView.findViewById(R.id.tv_car_plate);
//            carType = itemView.findViewById(R.id.tv_car_type);
//            re_group = itemView.findViewById(R.id.re_group);
        }
        public void setBinding(AdapterCarBinding adapterCarBinding){
            this.adapterCarBinding = adapterCarBinding;
        }
        public AdapterCarBinding getBinding(){
            return adapterCarBinding;
        }
    }
}
