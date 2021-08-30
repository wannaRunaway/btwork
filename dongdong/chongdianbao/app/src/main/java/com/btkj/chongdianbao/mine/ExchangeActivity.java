package com.btkj.chongdianbao.mine;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityExchangeBinding;
import com.btkj.chongdianbao.main.BaseActivity;
import com.btkj.chongdianbao.model.ClockBean;
import com.btkj.chongdianbao.model.ClockListBean;
import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.DateUtils;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.ListUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.OnClickEvent;
import com.btkj.chongdianbao.utils.SharePref;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeActivity extends BaseActivity {
    ActivityExchangeBinding binding;
    private int pageNo = 1;
    private ExchangeAdapter myadapter;
    private List<ClockListBean.ContentBean> list = new ArrayList<>();
    private int carId;
    private int clockType=0;
    @Override
    public void initView(Bundle savedInstanceState) {
        binding= DataBindingUtil.setContentView(this, R.layout.activity_exchange);
        binding.header.title.setText("打卡");
        binding.header.left.setOnClickListener(view -> finish());
        myadapter = new ExchangeAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myadapter);

        carInit();
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData(true);
                if(binding.ivCar.getVisibility()==View.GONE){
                    getClockStatus();
                }
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(pageNo==1&&ListUtils.isEmpty(list)){
                    binding.smartRefresh.finishRefresh();
                    binding.smartRefresh.finishLoadMore();
                }else {
                    pageNo = pageNo + 1;
                    loadData(false);
                }
            }
        });
        loadData(true);
    }
    private void loadData(boolean isRefresh){
        String url = API.BASE_URL + API.clockList;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("carId",String.valueOf(carId));
        map.put("pageNo", String.valueOf(pageNo));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, ExchangeActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ClockListBean clockListBean=GsonUtils.getInstance().fromJson(json,ClockListBean.class);
                if(isRefresh){
                    list.clear();
                }
                if(pageNo==1&& !ListUtils.isEmpty(clockListBean.getContent())){
                   ClockListBean.ContentBean contentBean=new ClockListBean.ContentBean();
                   contentBean.setDriverName("司机姓名");
                   contentBean.setCreateTime(-1);
                   contentBean.setSoc(-1);
                   contentBean.setCarMile(-1);
                   contentBean.setType(-1);
                   list.add(contentBean);
                }
                list.addAll(clockListBean.getContent());
                binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                myadapter.notifyDataSetChanged();

            }
        },binding.smartRefresh);
    }

    private void carInit() {
        carId = (int) SharePref.get(ExchangeActivity.this, API.carId, 0);
        getClockStatus();
        binding.ivCar.setOnClickListener(new OnClickEvent() {
            @Override
            public void singleClick(View v) {
                ExChangeDialog dialog=new ExChangeDialog(ExchangeActivity.this,clockType,R.style.mydialog);
                dialog.show();
            }
        });
    }


    private void getClockStatus(){
        String url = API.BASE_URL + API.CLOCK;
        RequestParams params = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", "-1");
        map.put("carId", String.valueOf(carId));
        map.put("soc","0");
        map.put("mile","0");
        params.add(Customize.SIGN, Customize.getRequestParams(map, params));
        new MyRequest().myrequest(url, params, ExchangeActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ClockBean clockBean=GsonUtils.getInstance().fromJson(json, ClockBean.class);
                if(clockBean.getContent()==null){
                    clockType=0;
                }else {
                    if(clockBean.getContent().getType()==0&&User.getInstance().getId()==clockBean.getContent().getDriverId()){
                        //如果是当前账号和打卡信息的司机是同一个人
                        clockType=1;
                    }else if(clockBean.getContent().getType()==1){
                        clockType=0;
                    }
                }
                binding.ivCar.setVisibility(View.VISIBLE);
                if(clockType==0){
                    binding.ivCar.setImageResource(R.mipmap.img_upcar);
                }else {
                    binding.ivCar.setImageResource(R.mipmap.img_downcar);
                }
            }
        });
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
            ClockListBean.ContentBean data = list.get(position);
            holder.nameTv.setText(data.getDriverName());
            if(position==0){
                holder.view.setVisibility(View.GONE);
            }else {
                holder.view.setVisibility(View.VISIBLE);
            }
            if(data.getCreateTime()==-1){
                holder.timeTv.setText("打卡时间");
            }else {
                holder.timeTv.setText(DateUtils.stampToTime(data.getCreateTime()));
            }
            if(data.getSoc()==-1){
                holder.socTv.setText("剩余SOC");
            }else {
                int soc= (int) data.getSoc();
                holder.socTv.setText(String.valueOf(soc));
            }
            if(data.getCarMile()==-1){
                holder.allTv.setText("总里程");
            }else {
                holder.allTv.setText(String.valueOf(data.getCarMile()));
            }
            if(data.getType()==-1){
                holder.typeTv.setText("打卡类型");
            }else if(data.getType()==0){
                holder.typeTv.setText("上车");
            }else {
                holder.typeTv.setText("下车");
            }
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    private class ExchangeViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv, timeTv, typeTv, allTv, socTv;
        private View view;
        public ExchangeViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_name);
            timeTv = itemView.findViewById(R.id.tv_time);
            socTv=itemView.findViewById(R.id.tv_soc);
            typeTv=itemView.findViewById(R.id.tv_type);
            allTv=itemView.findViewById(R.id.tv_all);
            view=itemView.findViewById(R.id.view);
        }
    }

    public class ExChangeDialog extends Dialog {
        private Context context;
        private int type;
        public ExChangeDialog(Context context,int type,int themeResId){
            super(context, themeResId);
            this.context=context;
            this.type=type;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_exchange);
            EditText mileEt=findViewById(R.id.et_mile);
            EditText socEt=findViewById(R.id.et_soc);
            ImageView sureIv=findViewById(R.id.iv_sure);
            sureIv.setOnClickListener(v -> {
                String soc=socEt.getText().toString().trim();
                String mile=mileEt.getText().toString().trim();
                if(TextUtils.isEmpty(soc)){
                    Utils.snackbar(context,ExchangeActivity.this,"请输入SOC");
                    return;
                }else if(Integer.valueOf(soc)<0||Integer.valueOf(soc)>100){
                    Utils.snackbar(context,ExchangeActivity.this,"SOC必须在0-100之间");
                    return;
                }
                if(TextUtils.isEmpty(mile)){
                    Utils.snackbar(context,ExchangeActivity.this,"请输入里程");
                    return;
                }else if(Integer.valueOf(mile)<=0){
                    Utils.snackbar(context,ExchangeActivity.this,"里程必须大于0");
                    return;
                }
                clock(type,soc,mile);
            });
        }

        private void clock(int type,String soc,String mile){
            String url = API.BASE_URL + API.CLOCK;
            RequestParams params = new RequestParams();
            Map<String, String> map = new HashMap<>();
            map.put("type", String.valueOf(type));
            map.put("carId", String.valueOf(carId));
            map.put("soc",soc);
            map.put("mile",mile);
            params.add(Customize.SIGN, Customize.getRequestParams(map, params));
            new MyRequest().myrequest(url, params, ExchangeActivity.this, getApplicationContext(), true, new Myparams() {
                @Override
                public void message(String json) {
                    dismiss();
                    if(type==0){
                        clockType=1;
                    }else {
                        clockType=0;
                    }
                    if(clockType==0){
                        binding.ivCar.setImageResource(R.mipmap.img_upcar);
                    }else {
                        binding.ivCar.setImageResource(R.mipmap.img_downcar);
                    }
                    pageNo=1;
                    loadData(true);
                }
            });
        }
    }
}
