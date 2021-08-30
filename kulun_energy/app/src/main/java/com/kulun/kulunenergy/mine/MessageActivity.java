package com.kulun.kulunenergy.mine;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityMessageBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.MessageModel;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.DateUtils;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.requestparams.Myparams;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MessageActivity extends BaseActivity {
    private  ActivityMessageBinding binding;
    private int pageNo = 1;
    private Myadapter myadapter;
    private List<MessageModel.ContentBean> list = new ArrayList<>();
    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        binding.header.title.setText("我的消息");
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
        String url = API.BASE_URL + API.MYMESSAGE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mysmartrequest(url, requestParams, MessageActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                MessageModel model = GsonUtils.getInstance().fromJson(json, MessageModel.class);
                if (isRefresh) {
                    list.clear();
                }
                list.addAll(model.getContent());
                binding.image.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
                myadapter.notifyDataSetChanged();
                EventBus.getDefault().post("readed");
                EventBus.getDefault().post("homereaded");
            }
        },binding.smartRefresh);
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setCookieStore(cookieStore);
//        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                finishLoad();
//                String json = new String(responseBody);
//                Utils.log(url, requestParams, json);
//                MessageModel model = GsonUtils.getInstance().fromJson(json, MessageModel.class);
//                if (model.isSuccess()) {
//                    if (isRefresh) {
//                        list.clear();
//                    }
//                    list.addAll(model.getContent());
//                    binding.imgageBack.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
//                    myadapter.notifyDataSetChanged();
//                } else {
//                    Utils.snackbar(getApplicationContext(), MessageActivity.this, model.getMsg());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                finishLoad();
//                Utils.log(url, requestParams, null);
//                Utils.snackbar(getApplicationContext(), MessageActivity.this, API.net_error);
//            }
//        });
    }

    private void finishLoad() {
        binding.smartRefresh.finishLoadMore();
        binding.smartRefresh.finishRefresh();
    }
    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MessageActivity.this).inflate(R.layout.adapter_message, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            MessageModel.ContentBean data = list.get(position);
            holder.name.setText(data.getContent());
            holder.time.setText(DateUtils.stampToDate(data.getCreateTime()));
            if(data.getTop()==0){
                holder.topIv.setVisibility(View.GONE);
            }else {
                holder.topIv.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time;
        private ImageView topIv;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            topIv=itemView.findViewById(R.id.iv_top);
        }
    }
}
