package com.kulun.energynet.ui.activity.more;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.R;
import com.kulun.energynet.adapter.CustomerServiceAdapter;
import com.kulun.energynet.inter.CustomerServiceInterface;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.customerservice.BaseCustomerServicebean;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.apache.http.Header;
/**
 * created by xuedi on 2018/11/12
 * @des 拨打客服界面
 */
public class CustomerServiceActivity extends AppCompatActivity {
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerservice);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartrefresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData();
            }
        });
    }

    /**
     * 请求数据
     * app/customerServiceInfo/list?accountId=30
     */
    private void loadData(){
        String Url = API.BASE_URL + API.URL_CUSTOMERSERVICELIST;
        RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        Log.d(Utils.TAG, "请求url:"+Url);
        Log.d(Utils.TAG, "请求参数"+requestParams);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        final PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(cookieStore);
        httpClient.post(Url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                smartRefreshLayout.finishRefresh();
                String json = new String(responseBody);
                Log.d(Utils.TAG, "返回json"+json);
                BaseCustomerServicebean customerServicebeanList = GsonUtils.getInstance().fromJson(json, BaseCustomerServicebean.class);
                recyclerView.setAdapter(new CustomerServiceAdapter(CustomerServiceActivity.this, customerServicebeanList.getContent(), new CustomerServiceInterface() {
                    @Override
                    public void click(String name, final String phone, String date) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerServiceActivity.this);
                        builder.setTitle(name);
                        builder.setMessage(phone+"\n"+date);
                        builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                callPhone(phone);
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(CustomerServiceActivity.this, "请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
