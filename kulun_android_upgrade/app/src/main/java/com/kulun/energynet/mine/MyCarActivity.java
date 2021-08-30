package com.kulun.energynet.mine;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityMyCarBinding;
import com.kulun.energynet.databinding.AdapterCarBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.Car;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.BaseDialog;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.JsonSplice;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;

public class MyCarActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMyCarBinding binding;
    private MyAdapter adapter;
    private List<Car> list = new ArrayList<>();
    private PromptDialog promptDialog;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_car);
        binding.header.left.setOnClickListener(this);
        binding.header.title.setText("我的车");
        binding.header.right.setText("绑定申请");
        binding.imgAddCar.setOnClickListener(this);
        adapter = new MyAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyCarActivity.this));
        binding.recyclerView.setAdapter(adapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
        binding.header.right.setOnClickListener(this);
        promptDialog = new PromptDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        new MyRequest().myRequest(API.getmycarlist, false, null, false, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (jsonArray != null) {
                    list.clear();
                    list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Car>>() {
                    }.getType()));
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
                loadinfo();
                break;
            case R.id.right:
                Intent intent1 = new Intent(MyCarActivity.this, CarbindApplyActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void loadinfo() {
        new MyRequest().myRequest(API.INFO, false, null, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (json == null || isNull) {
                    uploadInfo();
                    return;
                }
                UserLogin userLogin = GsonUtils.getInstance().fromJson(json, UserLogin.class);
                if (userLogin != null) {
                    if (!TextUtils.isEmpty(userLogin.getName()) && !TextUtils.isEmpty(userLogin.getIdentity())) {
                        AndPermission.with(MyCarActivity.this)
                                .runtime()
                                .permission(Permission.CAMERA)
                                .onGranted(permissions -> {
                                    Intent intent = new Intent(MyCarActivity.this, AddCarActivity.class);
                                    startActivity(intent);
                                })
                                .onDenied(permissions -> {
                                    // Storage permission are not allowed.
                                })
                                .start();
                    } else {
                        uploadInfo();
                    }
                } else {
                    uploadInfo();
                }
            }
        });
    }

    private void uploadInfo() {
        BaseDialog.showDialog(MyCarActivity.this, "请您上传个人信息", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCarActivity.this, PersonalActivity.class);
                intent.putExtra(API.register, false);
                startActivity(intent);
            }
        });
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
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //bind_status=0解绑bind_status=1绑定中bind_status=2解绑中bind_status=3锁定
            Car car = list.get(position);
            holder.getBinding().name.setText(car.getPlate_number());
            holder.getBinding().t1.setText(car.getSoc() + "%");
            holder.getBinding().t2.setText(car.getCar_type());
            holder.getBinding().t3.setText(car.isBattery_status() ? "正常" : "不正常");
            Drawable circledraable = getResources().getDrawable(R.drawable.recharge_choice);
            circledraable.setBounds(0, 0, 55, 55);
            holder.getBinding().now.setCompoundDrawables(circledraable,null,null,null);
            if (car.getBind_status()==1){
                Drawable drawable = getResources().getDrawable(R.mipmap.sign_binding);
                drawable.setBounds(0, 0, 100, 50);
                holder.getBinding().name.setCompoundDrawables(null,null,drawable,null);
            }else if (car.getBind_status()==3){
                Drawable drawable = getResources().getDrawable(R.mipmap.sign_lock);
                drawable.setBounds(0, 0, 100, 50);
                holder.getBinding().name.setCompoundDrawables(null,null,drawable,null);
            }
            if (car.getMonth_mile() == 0) {
                holder.getBinding().progressbar.setVisibility(View.GONE);
                holder.getBinding().mile.setVisibility(View.GONE);
            } else {
                holder.getBinding().mile.setVisibility(View.VISIBLE);
                holder.getBinding().progressbar.setVisibility(View.VISIBLE);
                holder.getBinding().mile.setText("当前剩余：" + car.getLeft_mile() + "  |  " + "套餐总量：" + car.getMonth_mile());
                holder.getBinding().progressbar.setProgress(car.getLeft_mile() * 100 / car.getMonth_mile());
            }
            holder.getBinding().now.setOnClickListener(v -> {
                dangqian(holder.getBinding().now, car.getId());
            });
            holder.getBinding().unbind.setOnClickListener(v -> {
                BaseDialog.showDialog(MyCarActivity.this, "是否确认解绑", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelCar(car.getPlate_number());
                    }
                });
            });
            if (car.isInuse()) {
                holder.getBinding().now.setSelected(true);
            } else {
                holder.getBinding().now.setSelected(false);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private void dangqian(TextView textView, int id) {
        //bind_id
        promptDialog.showLoading("正在设为当前车辆");
        String json = JsonSplice.leftparent + JsonSplice.yin + "bind_id" + JsonSplice.yinandmao + id + JsonSplice.rightparent;
        new MyRequest().spliceJson(API.carnow, true, json, this, promptDialog, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                loadData();
            }
        });
    }

    //bindId[int]	是	绑定ID
    private void cancelCar(String plateNo) {
        //{
        //    "plate": "浙ARB104"
        //}
        promptDialog.showLoading("正在解绑");
        HashMap<String, String> map = new HashMap<>();
        map.put("plate", plateNo);
        new MyRequest().myRequest(API.carunbind, true, map, true, this, promptDialog, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                loadData();
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterCarBinding adapterCarBinding;

        public MyViewHolder(View view) {
            super(view);
        }

        public void setBinding(AdapterCarBinding adapterCarBinding) {
            this.adapterCarBinding = adapterCarBinding;
        }

        public AdapterCarBinding getBinding() {
            return adapterCarBinding;
        }
    }
}
