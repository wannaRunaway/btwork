package com.kulun.energynet.mine;

import android.app.Dialog;
import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityExchangeBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.Daka;
import com.kulun.energynet.model.UseBind;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.Utils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExchangeActivity extends BaseActivity {
    ActivityExchangeBinding binding;
    private int pageNo = 0;
    private ExchangeAdapter myadapter;
    private List<Daka> list = new ArrayList<>();
    private int clockType = 0;
    private UseBind useBind;
    private int type;//type=0上班type=1下班
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exchange);
        binding.header.title.setText("一键交接");
        binding.header.left.setOnClickListener(view -> finish());
        myadapter = new ExchangeAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myadapter);
        useBind = Utils.getusebind(false,null,ExchangeActivity.this);
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
        binding.ivCar.setVisibility(useBind.getBusiness_type()==5?View.VISIBLE:View.GONE);
        binding.ivCar.setOnClickListener(v -> {
            ExChangeDialog dialog=new ExChangeDialog(ExchangeActivity.this,R.style.mydialog);
            dialog.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true);
    }

    //    "bindId": 15019,
    //    "driverClockType": 1,
    //    "carMile": 1000,
    //    "soc": 50
    private void daka(int soc, int mile) {
        HashMap<String,String> map = new HashMap<>();
        map.put("bindId", String.valueOf(useBind.getId()));
        map.put("driverClockType", type+"");//0上班 1下班
        map.put("soc", String.valueOf(soc));
        map.put("carMile", String.valueOf(mile));
        new MyRequest().myRequest(API.clock, true, map, false, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                Utils.snackbar(ExchangeActivity.this, "打卡成功");
                loadData(true);
            }
        });
    }

    private void loadData(boolean isRefresh) {
        //{
        //    "bindId": 15019,
        //    "page": 0
        //}
        HashMap<String, String> map = new HashMap<>();
        map.put("bindId", String.valueOf(useBind.getId()));
        map.put("page", String.valueOf(pageNo));
        new MyRequest().myRequest(API.clockList, true, map, false, this, null, binding.smartRefresh, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                if (isNull){
                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                    isworkDaka();
                }
                if (jsonArray != null) {
                    if (isRefresh) {
                        list.clear();
                    }
                    list.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Daka>>() {
                    }.getType()));
                    binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                    myadapter.notifyDataSetChanged();
                    isworkDaka();
                }
            }
        });
    }

    private void isworkDaka() {//打卡记录有数据并且第一条是本人打的上班卡 显示下班  其余都是上班  type=0上班type=1下班
        if (list.size() > 0) {
            Daka daka = list.get(0);
            if (daka.getAccount().equals(UserLogin.getInstance().getAccount()) && daka.getType() == 0) {
                binding.ivCar.setText("下车打卡");
                type=1;
            }else {
                binding.ivCar.setText("上车打卡");
                type=0;
            }
        }
    }



    private class ExchangeAdapter extends RecyclerView.Adapter<ExchangeViewHolder> {
        @NonNull
        @Override
        public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ExchangeActivity.this).inflate(R.layout.item_exchange, parent, false);
            ExchangeViewHolder holder = new ExchangeViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
            Daka data = list.get(position);
            holder.nameTv.setText(data.getAccountName());
            holder.timeTv.setText((data.getCreateTime()));
            holder.allTv.setText(data.getCarMile()+"km");
            if (data.getType() == 0) {
                holder.typeTv.setText("上车");
            } else {
                holder.typeTv.setText("下车");
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class ExchangeViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv, timeTv, typeTv, allTv;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_name);
            timeTv = itemView.findViewById(R.id.tv_time);
//            socTv=itemView.findViewById(R.id.tv_soc);
            typeTv = itemView.findViewById(R.id.tv_type);
            allTv = itemView.findViewById(R.id.tv_all);
        }
    }

    public class ExChangeDialog extends Dialog {
        public ExChangeDialog(Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_exchange);
            EditText mileEt = findViewById(R.id.et_mile);
            mileEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            mileEt.setText(s.subSequence(0, 1));
                            mileEt.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            EditText socEt = findViewById(R.id.et_soc);
            socEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            mileEt.setText(s.subSequence(0, 1));
                            mileEt.setSelection(1);
                            return;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            TextView confirm = findViewById(R.id.confirm);
            TextView cancel = findViewById(R.id.cancel);
            confirm.setOnClickListener(v -> {
                String soc = socEt.getText().toString().trim();
                String mile = mileEt.getText().toString().trim();
                if (TextUtils.isEmpty(soc)) {
                    Utils.snackbar(ExchangeActivity.this, "请输入SOC");
                    return;
                } else if (Integer.valueOf(soc) < 0 || Integer.valueOf(soc) > 100) {
                    Utils.snackbar(ExchangeActivity.this, "SOC必须在0-100之间");
                    return;
                }
                if (TextUtils.isEmpty(mile)) {
                    Utils.snackbar(ExchangeActivity.this, "请输入里程");
                    return;
                } else if (Integer.valueOf(mile) <= 0) {
                    Utils.snackbar(ExchangeActivity.this, "里程必须大于0");
                    return;
                }
                daka(Double.valueOf(soc).intValue(), Double.valueOf(mile).intValue());
                dismiss();
            });
            cancel.setOnClickListener(v -> dismiss());
        }
    }
}
