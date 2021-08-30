package com.kulun.energynet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.R;
import com.kulun.energynet.model.recharge.RechargeInfo;

/**
 * Created by Oiron on 2017/7/21.
 */
public class RechargeInfoAdapter extends BaseAdapter<RechargeInfo> {
    private BaseAdapter mAdapter;
    private static final int mPageSize = 20;
    private int mPagePosition = 0;
    private boolean hasMoreData = true;
    private OnLoad mOnLoad;

    public RechargeInfoAdapter(BaseAdapter adapter, OnLoad onLoad) {
        mAdapter = adapter;
        mOnLoad = onLoad;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == R.layout.footer_bottom_tip) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new RechargeInfoAdapter.NoMoreItemVH(view);
        }else if(viewType == R.layout.recharge_footer) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new RechargeInfoAdapter.LoadingItemVH(view);
        }else{
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RechargeInfoAdapter.LoadingItemVH) {
            requestData(mPagePosition, mPageSize);
        }else if(holder instanceof RechargeInfoAdapter.NoMoreItemVH) {

        }else{
            mAdapter.onBindViewHolder(holder, position);
        }

    }

    private void requestData(int pagePosition, int pageSize) {

        //网络请求如果是异步请求，则在成功之后的回调中添加数据，并且调用notifyDataSetChanged方法，hasMoreData为true
        //如果没有数据了，则hasMoreData为false,然后通知变化，更新recylerview
        if(mOnLoad != null) {
            mOnLoad.load(pagePosition, pageSize, new ILoadCallback() {
                @Override
                public void onSuccess() {
                    notifyDataSetChanged();
                    mPagePosition = (mPagePosition + 1) * mPageSize;
                    hasMoreData = true;
                }

                @Override
                public void onFailure() {
                    hasMoreData = false;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1) {
            if(hasMoreData) {
                return R.layout.recharge_footer;
            }else{
                return R.layout.footer_bottom_tip;
            }
        }else{
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    static class LoadingItemVH extends RecyclerView.ViewHolder {

        public LoadingItemVH(View itemView) {
            super(itemView);
        }
    }

    static class NoMoreItemVH extends RecyclerView.ViewHolder {

        public NoMoreItemVH(View itemView) {
            super(itemView);
        }
    }
}
