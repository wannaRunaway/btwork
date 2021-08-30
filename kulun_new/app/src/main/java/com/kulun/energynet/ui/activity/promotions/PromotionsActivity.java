package com.kulun.energynet.ui.activity.promotions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kulun.energynet.R;
import com.kulun.energynet.adapter.PromotionsAdapter;
import com.kulun.energynet.databinding.ActivityPromotionsBinding;
import com.kulun.energynet.inter.PromotionsInterface;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.promotions.Promotions;
import com.kulun.energynet.model.promotions.PromotionsList;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.apache.http.Header;
import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/1/7
 */
public class PromotionsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityPromotionsBinding binding;
    private PromotionsAdapter adapter;
    private List<Promotions> list = new ArrayList<>();
    private int pageNo = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_promotions);
        binding.tvBack.setOnClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(PromotionsActivity.this));
        adapter = new PromotionsAdapter(list, PromotionsActivity.this, new PromotionsInterface() {
            @Override
            public void click(Promotions promotions, String type) {
                /**
                 * 跳转到详情界面
                 */
                switch (type){
                    case "按次套餐":
                        Intent intent = new Intent(PromotionsActivity.this, CountPromotionDetailsActivity.class);
                        intent.putExtra("promotions", promotions);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        break;
                    case "里程套餐":
                    case "充值送":
                        Intent intentli = new Intent(PromotionsActivity.this, PromotionDetailsActivity.class);
                        intentli.putExtra("promotions", promotions);
                        intentli.putExtra("type", type);
                        startActivity(intentli);
                        break;
                    case "费用套餐":
                        if (promotions.getPackageTime() == 0) { // 0是一个自然月， 1是半个自然月
                            Intent intentfei = new Intent(PromotionsActivity.this, PriceDetailsAcrivity.class);
                            intentfei.putExtra("promotions", promotions);
                            intentfei.putExtra("type", type);
                            startActivity(intentfei);
                        }else {
                            Intent intentfei = new Intent(PromotionsActivity.this, HalfPriceDetailsActivity.class);
                            intentfei.putExtra("promotions", promotions);
                            intentfei.putExtra("type", type);
                            startActivity(intentfei);
                        }
                        break;
                    default:
                        break;
                }
            }});
        binding.recyclerView.setAdapter(adapter);
        binding.smartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNo = 1;
                loadData(true);
            }
        });
        binding.smartrefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo = pageNo + 1;
                loadData(false);
            }
        });
        loadData(true);
    }

    /**
     * 优惠活动界面加载
     * http://121.196.237.125:9008/pay/app/packageActivity/list?accountId=100
     */
    private void loadData(final boolean isRefresh) {
        String url = API.BASE_URL + API.URL_PROMOTIONS;
        RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("pageNo", String.valueOf(pageNo));
        Log.d(Utils.TAG, "请求url:" + url);
        Log.d(Utils.TAG, "请求参数:"+ requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                String json = new String(responseBody);
                Log.d(Utils.TAG, "返回的json:"+json);
                PromotionsList promotionsList = GsonUtils.getInstance().fromJson(json, PromotionsList.class);
                if (isRefresh){
                    list.clear();
                }
                if (promotionsList.getContent() != null && promotionsList.getContent().getData() != null && promotionsList.getContent().getData().size() != 0){
                    list.addAll(promotionsList.getContent().getData());
                }
                adapter.notifyDataSetChanged();
                if (list.size() == 0){
                    binding.tvInfo.setVisibility(View.VISIBLE);
                }else {
                    binding.tvInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PromotionsActivity.this, "请求失败", Toast.LENGTH_LONG).show();
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
}
