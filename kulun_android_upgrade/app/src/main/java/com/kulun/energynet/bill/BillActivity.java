package com.kulun.energynet.bill;

import android.content.Intent;
import android.os.Bundle;
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
import com.kulun.energynet.databinding.ActivityBillBinding;
import com.kulun.energynet.databinding.AdapterBillBinding;
import com.kulun.energynet.mine.RechargeActivity;
import com.kulun.energynet.model.Bill;
import com.kulun.energynet.model.BillDetail;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.popup.BillDatePopup;
import com.kulun.energynet.popup.BilltypePopup;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;

public class BillActivity extends AppCompatActivity implements View.OnClickListener, TypePopclick, BillDatePopup.Popdateclick {
    private ActivityBillBinding binding;
    private MyAdapter adapter;
    private List<Bill> list = new ArrayList<>();
    private int page;
    private PromptDialog promptDialog;
    private BilltypePopup billtypePopup;
    private BillDatePopup billDatePopup;
    private int mytype = 0;
//    private boolean isload = false;
    private TypePopclick typePopclick;
    private BillDatePopup.Popdateclick popdateclick;
    private boolean canRefund;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill);
        typePopclick = this;
        popdateclick = this;
        binding.header.title.setText("我的账单");
        binding.header.left.setOnClickListener(this);
        binding.pay.setOnClickListener(this);
        binding.refund.setOnClickListener(this);
        binding.date.setOnClickListener(this);
        binding.type.setOnClickListener(this);
        promptDialog = new PromptDialog(this);
        billtypePopup = new BilltypePopup(BillActivity.this, typePopclick);
        billDatePopup = new BillDatePopup(BillActivity.this, popdateclick);
        adapter = new MyAdapter(list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                load(true, String.valueOf(mytype), String.valueOf(page));
                loadInfo();
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                load(false, String.valueOf(mytype), String.valueOf(page));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load(true, "0", String.valueOf(page));
        loadInfo();
    }

    private void loadInfo() {//获取当前的余额
        new MyRequest().myRequest(API.INFO, false, null, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (json != null || jsonArray != null) {
                    UserLogin userLogin = GsonUtils.getInstance().fromJson(json, UserLogin.class);
                    binding.money.setText(userLogin.getBalance() + "");
                    billtypePopup.typebinding.money.setText(userLogin.getBalance() + "");
                    billDatePopup.binding.money.setText(userLogin.getBalance() + "");
                    Utils.getusebind(true, json, BillActivity.this);
                    canRefund = userLogin.isCanRefund();
                    if (canRefund){
                        binding.refund.setVisibility(View.VISIBLE);
                    }else {
                        binding.refund.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    private void load(boolean isRefresh, String type, String page) {
        //type复制[string]		0全部订单;1充值订单;2套餐订单;3消费订单;4退款订单
        //page[string]		页数，默认值0，从0开始
        //size[string]		每页条数，默认值20
        //from[string]		2006-01
        //to[string]		2006-01
        String json = "type=" + type + "&page=" + page;
        new MyRequest().spliceJson(API.billlist, false, json, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (isNull){
                    if (isRefresh) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                }
                if (jsonArray != null) {
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Bill>>() {
                    }.getType()));
                    adapter.notifyDataSetChanged();
                    binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    private void load(boolean isRefresh, String type, String page, String time) {
        //type复制[string]		0全部订单;1充值订单;2套餐订单;3消费订单;4退款订单
        //page[string]		页数，默认值0，从0开始
        //size[string]		每页条数，默认值20
        //from[string]		2006-01
        //to[string]		2006-01
        String json = "type=" + type + "&page=" + page + "&month=" + time;
        new MyRequest().spliceJson(API.billlist, false, json, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (json == null || isNull) {
                    if (isRefresh) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                        binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                    }
                }
                if (jsonArray != null) {
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Bill>>() {
                    }.getType()));
                    adapter.notifyDataSetChanged();
                    binding.image.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.pay:
                Intent intent = new Intent(BillActivity.this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.refund:
                Intent intent1 = new Intent(BillActivity.this, RefundActivity.class);
                startActivity(intent1);
                break;
            case R.id.date:
                billDatePopup.showPopupWindow();
                break;
            case R.id.type:
                billtypePopup.showPopupWindow();
                break;
            default:
                break;
        }
    }

    @Override
    public void billpopclick(String type) {
        page = 0;
        switch (type) {//type=0全部type=1充值订单type=2消费订单type=3退款订单账单返回添加cType账单详情请求时用cType请求
            // 	0全部订单;1充值订单;2套餐订单;3消费订单;4退款订单
            case "全部":
                mytype = 0;
                load(true, "0", String.valueOf(page));
                break;
            case "充值":
                mytype = 1;
                load(true, "1", String.valueOf(page));
                break;
            case "活动":
                mytype = 3;
                load(true, "3", String.valueOf(page));
                break;
            case "消费":
                mytype = 2;
                load(true, "2", String.valueOf(page));
                break;
            case "退款":
                mytype = 4;
                load(true, "4", String.valueOf(page));
                break;
            default:
                break;
        }
        binding.type.setText(type + "");
    }

    @Override
    public void datepopclick(String time) {//年份和月份
        if (time.equals("")) {
            binding.date.setText("全部");
            load(true, String.valueOf(mytype), String.valueOf(page));
        } else {
//            String fromjson = from.replaceAll("年", "-").replaceAll("月", "");
//            String tojson = to.replaceAll("年", "-").replaceAll("月", "");
//            String json = fromjson +"~"+ tojson;
            binding.date.setText(time);
            load(true, String.valueOf(mytype), String.valueOf(page), time);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Bill> adapterlist;

        public MyAdapter(List<Bill> list) {
            this.adapterlist = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BillActivity.this).inflate(R.layout.adapter_bill, null);
            AdapterBillBinding binding = AdapterBillBinding.bind(view);
            MyViewHolder holder = new MyViewHolder(view);
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Bill bill = list.get(position);
            String type = "";//change_type(-4修改减少优惠券,-3删除优惠券 -2退款,-1换电消费，1手机充值，2人工后台充值,3pad充值,
            // 4仅发放优惠券,5修改增加优惠券),6 pad套餐购买  7app套餐购买；12换电分账充值;13提现
            int mipmap = 0;
            switch (bill.getcType()) {
                case 1:
                case 2:
                case 3:
                    type = "充值";
                    mipmap = R.mipmap.bill_recharge;
                    break;
                case 6:
                case 7:
                    type = "活动";
                    mipmap = R.mipmap.icon_activity;
                    break;
                case -1:
                    type = "消费";
                    mipmap = R.mipmap.bill_amount;
                    break;
                case -2:
                    mipmap = R.mipmap.bill_refund;
                    type = "退款";
                    break;
                default:
                    break;
            }
            holder.binding.name.setText(type);
            holder.binding.image.setImageResource(mipmap);
            holder.binding.time.setText(bill.getCreate_time());
            String left = type.equals("充值") ? "+" : "";
            holder.binding.money.setText(left + bill.getChange_balance() + "元");
            holder.binding.money.setTextColor(type.equals("充值") ? getResources().getColor(R.color.red) : getResources().getColor(R.color.black));
            holder.binding.re.setOnClickListener(v -> {
                //bid[string]	是
                //cType[string]	是
                String json = "bid=" + bill.getBid() + "&cType=" + bill.getcType();
                promptDialog.showLoading("加载中...");
                new MyRequest().spliceJson(API.billdetail, false, json, BillActivity.this, promptDialog, null, new Response() {
                    @Override
                    public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                        if (json != null) {
                            switch (bill.getcType()) {//change_type(-4修改减少优惠券,-3删除优惠券 -2退款,-1换电消费，1手机充值，2人工后台充值,3pad充值,
                                // 4仅发放优惠券,5修改增加优惠券),6 pad套餐购买  7app套餐购买；12换电分账充值;13提现
                                case 1:
                                case 2:
                                case 3:
                                    Intent intent = new Intent(BillActivity.this, BillRechargeDetailsActivity.class);
                                    try {
                                        intent.putExtra("data", (Serializable) GsonUtils.getInstance().fromJson(json.get("detail").getAsJsonArray(), new TypeToken<List<BillDetail>>() {
                                        }.getType()));
                                        intent.putExtra("bill", bill);
                                    } catch (Exception e) {
                                    }
                                    startActivity(intent);
                                    break;
                                case 6:
                                case 7:
                                    Intent intentpackage = new Intent(BillActivity.this, BillActivityActivity.class);
                                    try {
                                        intentpackage.putExtra("data", (Serializable) GsonUtils.getInstance().fromJson(json.get("detail").getAsJsonArray(), new TypeToken<List<BillDetail>>() {
                                        }.getType()));
                                        intentpackage.putExtra("bill", bill);
                                    } catch (Exception e) {
                                    }
                                    startActivity(intentpackage);
                                    break;
                                case -1:
                                    Intent intentconsume = new Intent(BillActivity.this, BillConsumeActivity.class);
                                    //"questioned":false,"commented":false,
//                                    try {
                                    intentconsume.putExtra("data", (Serializable) GsonUtils.getInstance().fromJson(json.get("detail").getAsJsonArray(), new TypeToken<List<BillDetail>>() {
                                    }.getType()));
                                    intentconsume.putExtra("bill", bill);
                                    if (json.has("questioned")) {
                                        intentconsume.putExtra("questioned", json.get("questioned").getAsBoolean());
                                    }
                                    if (json.has("commented")) {
                                        intentconsume.putExtra("commented", json.get("commented").getAsBoolean());
                                    }
                                    if (json.has("siteId")) {
                                        intentconsume.putExtra("siteid", json.get("siteId").getAsInt());
                                    }
                                    if (json.has("site")) {
                                        intentconsume.putExtra("site", json.get("site").getAsString());
                                    }
                                    if (json.has("exId")) {
                                        intentconsume.putExtra("exId", json.get("exId").getAsInt());
                                    }
                                    if (json.has("orderNo")) {
                                        intentconsume.putExtra("orderNo", json.get("orderNo").getAsString());
                                    }
//                                    }catch (Exception e){
//                                    }
                                    startActivity(intentconsume);
                                    break;
                                case -2:
                                    Intent intentrefund = new Intent(BillActivity.this, BillRefundActivity.class);
                                    try {
                                        intentrefund.putExtra("data", (Serializable) GsonUtils.getInstance().fromJson(json.get("detail").getAsJsonArray(), new TypeToken<List<BillDetail>>(){}.getType()));
                                        intentrefund.putExtra("bill", bill);
                                        intentrefund.putExtra("status", json.get("status").getAsInt());
                                        intentrefund.putExtra("amount", json.get("amount").getAsDouble());
                                    }catch (Exception e){
                                    }
                                    startActivity(intentrefund);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
            });
        }

        @Override
        public int getItemCount() {
            return adapterlist.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterBillBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setBinding(AdapterBillBinding binding) {
            this.binding = binding;
        }
    }
}
