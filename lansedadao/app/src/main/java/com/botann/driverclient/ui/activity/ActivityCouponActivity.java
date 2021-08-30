package com.botann.driverclient.ui.activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.R;
import com.botann.driverclient.model.couponrecord.CouponData;
import com.botann.driverclient.model.couponrecord.CouponRecordModle;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.promotions.PromotionsRecordActivity;
import com.botann.driverclient.utils.DateUtils;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;

import java.util.List;

/**
 * created by xuedi on 2019/2/18
 */
public class ActivityCouponActivity extends AppCompatActivity implements View.OnClickListener {
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tv_back;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_activity);
        id = getIntent().getIntExtra("id", 0);
        initView();
    }

    //寻找view
    private void initView() {
        smartRefreshLayout = findViewById(R.id.smartrefresh_layout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData();
            }
        });
        smartRefreshLayout.autoRefresh();
    }

    //加载数据
    private void loadData() {
        String url = API.BASE_URL + API.URL_COUPON_ACTIVITY;
        RequestParams requestParams = new RequestParams();
        requestParams.add("activityRecordId", String.valueOf(id));
        Log.d(Utils.TAG, "url:"+url+"\n"+"请求参数:"+requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(persistentCookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                String json = new String(responseBody);
                Log.d("xuedi", "json返回值:"+json);
                CouponRecordModle recordModle = GsonUtils.getInstance().fromJson(json, CouponRecordModle.class);
                recyclerView.setAdapter(new CouponAdapter(ActivityCouponActivity.this, recordModle.getContent()));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ActivityCouponActivity.this, "请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private class CouponAdapter extends RecyclerView.Adapter<CouponViewHolder> {
        private Context context;
        private List<CouponData> data;
        public CouponAdapter(Context activityCouponActivity, List<CouponData> data) {
            this.context = activityCouponActivity;
            this.data = data;
        }

        @Override
        public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_coupon, null);
            CouponViewHolder holder = new CouponViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CouponViewHolder holder, int position) {
            CouponData couponData = data.get(position);
            holder.tv_money.setText("￥"+couponData.getAmount());
            holder.tv_title.setText(couponData.getCouponName()+"");
            holder.tv_date.setText(DateUtils.stampToYear(couponData.getBeginDate())+" 至 "+DateUtils.stampToYear(couponData.getExpireDate()));
            long time = System.currentTimeMillis();
            if (time >= couponData.getExpireDate()){
                holder.img_expired.setVisibility(View.VISIBLE);
            }else {
                holder.img_expired.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class CouponViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_expired;
        private TextView tv_money,tv_title, tv_date;
        public CouponViewHolder(View itemView) {
            super(itemView);
            img_expired = itemView.findViewById(R.id.image_expired);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
