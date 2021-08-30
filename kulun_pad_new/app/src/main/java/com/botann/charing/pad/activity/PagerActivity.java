package com.botann.charing.pad.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charging.pad.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by liushanguo on 2018/4/20.
 */

public abstract class PagerActivity extends BaseActivity {


    protected XRecyclerView mRecyclerView;
    protected ArrayList listData;
    protected RecyclerView.Adapter cellAdapter;
    protected int pageSize = 30;
    protected int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listData = new ArrayList<>();


        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadDatas(true, new SGFinishCallBack() {
                    @Override
                    public void onFinish(Boolean result) {
                        cellAdapter.notifyDataSetChanged();
                        currentPage = 2;
                        mRecyclerView.refreshComplete();
                        if (result) {
                            Log.i("加载","no more true");
                            mRecyclerView.setNoMore(true);
                        }else {
                            Log.i("加载","no more false");
                            mRecyclerView.setNoMore(false);
                        }
                    }
                });

            }

            @Override
            public void onLoadMore() {
                loadDatas(false, new SGFinishCallBack() {
                    @Override
                    public void onFinish(Boolean result) {
                        cellAdapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();
                        if (result) mRecyclerView.setNoMore(true);
                        currentPage++;
                    }
                });
            }
        });

        mRecyclerView.refresh();
    }
}
