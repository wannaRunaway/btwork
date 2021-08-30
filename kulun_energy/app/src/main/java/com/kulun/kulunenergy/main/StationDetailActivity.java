package com.kulun.kulunenergy.main;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityStationDetailBinding;
import com.kulun.kulunenergy.model.StationDetailModel;
import com.kulun.kulunenergy.modelnew.StationInfo;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationDetailActivity extends BaseActivity {
    ActivityStationDetailBinding binding;
    private List<StationInfo> list=new ArrayList<>();
    private StationDetailAdapter adapter;
    private int stationId;
    private int pageNo = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding= DataBindingUtil.setContentView(StationDetailActivity.this,R.layout.activity_station_detail);
        if(getIntent()!=null){
            stationId=getIntent().getIntExtra("stationId",0);
            //L.e("stationId="+stationId);
        }
        binding.header.title.setText("电站详情");
        binding.header.left.setOnClickListener(view-> finish());
        binding.smartRefresh.setOnRefreshListener(refreshLayout -> {
            pageNo=1;
            loadData(true);
        });
        binding.smartRefresh.setOnLoadMoreListener(refreshLayout -> {
            pageNo = pageNo + 1;
            loadData(false);
        });
        initRecyclerView();
        loadData(true);
    }

    private void initRecyclerView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new StationDetailAdapter();
        binding.rv.setAdapter(adapter);
    }

    private void loadData(boolean isRefresh) {
        String url = API.BASE_URL + API.STATIONDETAIL;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id",String.valueOf(stationId));
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(100));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, StationDetailActivity.this, getApplicationContext(), true, json -> {
            StationDetailModel model = GsonUtils.getInstance().fromJson(json, StationDetailModel.class);
            if (isRefresh) {
                list.clear();
            }
//            list.addAll(model.getContent());
            adapter.notifyDataSetChanged();
        }, binding.smartRefresh);
    }

    private class StationDetailAdapter extends RecyclerView.Adapter<StationDetailViewHolder>{
        @Override
        public StationDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(StationDetailActivity.this).inflate(R.layout.item_stationdetail,null);
            return new StationDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(StationDetailViewHolder holder, int position) {
            StationInfo contentBean=list.get(position);
            int number=position+1;
            if(number>9){
                holder.numberTv.setText(String.valueOf(number));
            }else {
                holder.numberTv.setText("0"+String.valueOf(number));
            }
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.greenView.getLayoutParams();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int width= dm.widthPixels/3;
//            if(contentBean.getBattery_status()==1){
//                holder.powerIv.setVisibility(View.GONE);
//                holder.timeTv.setText("可用");
//                holder.timeTv.setTextColor(Color.parseColor("#00d489"));
//                params.width=width;
//            }else {
//                holder.powerIv.setVisibility(View.VISIBLE);
//                holder.timeTv.setTextColor(Color.parseColor("#333333"));
//                holder.timeTv.setText("预计"+(int)contentBean.getSurplus_time()+"分钟");
//                params.width= (int)((contentBean.getSoc()/100)*width);
//            }
//            holder.greenView.setLayoutParams(params);
//            holder.greenView.setBackgroundResource(R.drawable.shape_power_green);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class StationDetailViewHolder extends RecyclerView.ViewHolder{
        private TextView numberTv;
        private View greenView;
        private TextView timeTv;
        private ImageView powerIv;
        StationDetailViewHolder(View itemView) {
            super(itemView);
            numberTv=itemView.findViewById(R.id.tv_number);
            greenView=itemView.findViewById(R.id.view_green);
            timeTv=itemView.findViewById(R.id.tv_time);
            powerIv=itemView.findViewById(R.id.iv_power);
        }
    }
}
