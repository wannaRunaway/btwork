package com.kulun.energynet.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.energynet.R;
import com.kulun.energynet.model.Message;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.GsonUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MessagePushFragment extends Fragment {
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private int pageNo = 0;
    private Myadapter myadapter;
    private List<Message> list = new ArrayList<>();
    private LinearLayout image;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_push, null);
        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        recyclerView = view.findViewById(R.id.recyclerView);
        image = view.findViewById(R.id.image);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myadapter = new Myadapter(list);
        recyclerView.setAdapter(myadapter);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 0;
                loadData(true, pageNo);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false, pageNo);
            }
        });
        loadData(true, pageNo);
        return view;
    }

    private void loadData(boolean isRefresh, int pageNo) {
        //type复制
        //[int]		0系统通知 1个人通知
        //page
        //[int]		页数 默认0 从0开始
        //size
        //[int]		每页条数 默认20
        String json = "type=1&page="+pageNo+"&size=20";
        new MyRequest().spliceJson(API.messagelist, false, json, getActivity(), null, smartRefreshLayout, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray,boolean isNull) {
                if (isNull){
                    if (isRefresh) {
                        list.clear();
                    }
                    image.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                }
                if (jsonArray != null) {
                    if (isRefresh) {
                        list.clear();
                    }
                    List<Message> mylist = GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Message>>() {
                    }.getType());
                    list.addAll(mylist);
                    myadapter.notifyDataSetChanged();
                    image.setVisibility(list.size()==0?View.VISIBLE:View.GONE);
                }
            }
        });
    }

    private class Myadapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Message> adapterlist;
        public Myadapter(List<Message> list) {
            adapterlist = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_message, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Message data = list.get(position);
            holder.name.setText(data.getContent());
            holder.time.setText(data.getCreate_time());
            if (data.isIs_top()) {
                holder.topIv.setVisibility(View.VISIBLE);
            } else {
                holder.topIv.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return adapterlist.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time;
        private ImageView topIv;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            topIv = itemView.findViewById(R.id.iv_top);
        }
    }
}
