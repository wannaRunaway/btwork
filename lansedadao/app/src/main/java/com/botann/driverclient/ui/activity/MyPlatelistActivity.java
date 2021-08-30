package com.botann.driverclient.ui.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.botann.driverclient.BaseObjectString;
import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityMyPlateBinding;
import com.botann.driverclient.db.SharedPreferencesHelper;
import com.botann.driverclient.model.MyApplyModel;
import com.botann.driverclient.model.User;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.login.DriverinfoBean;
import com.botann.driverclient.ui.activity.login.UploadCarFinalActivity;
import com.botann.driverclient.ui.activity.login.UploadDriverCarInfoActivity;
import com.botann.driverclient.ui.activity.login.UploadPhotoActivity;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by xuedi on 2020/3/17
 */
public class MyPlatelistActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMyPlateBinding binding;
    private int pageNo = 1;
    private MyAdapter myAdapter;
    private List<DriverinfoBean> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_my_plate);
        binding.tvBack.setOnClickListener(this);
        binding.tvAdd.setOnClickListener(this);
        binding.smartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter();
        binding.recyclerView.setAdapter(myAdapter);
        loadData(true);
    }

    private void loadData(final boolean isRefresh) {
        final String url = API.BASE_URL + API.MYPLATELIST;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("pageNo", String.valueOf(pageNo));
        params.add("pageSize","20");
        final AsyncHttpClient client = new AsyncHttpClient();
        //保存cookie，自动保存到了sharepreferences
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                String json = new String(responseBody);
                Utils.log(url,params,json);
                MyApplyModel model = GsonUtils.getInstance().fromJson(json, MyApplyModel.class);
                if (isRefresh){
                    list.clear();
                }
                if (model != null && model.getContent() != null) {
                    list.addAll(model.getContent().getData());
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.smartrefreshLayout.finishRefresh();
                binding.smartrefreshLayout.finishLoadMore();
                Utils.log(url,params,null);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_add:
                Intent intent = new Intent(MyPlatelistActivity.this, UploadDriverCarInfoActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra(API.islogin, true);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MyPlatelistActivity.this).inflate(R.layout.adapter_myplate, null);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
//            final MyApplyModel.ContentBean.DataBean bean = list.get(position);
            holder.tv_plate.setText(list.get(position).getPlateNumber()+"");
            holder.tv_type.setText(list.get(position).getCarTypeName()+"");
            switch (list.get(position).getStatus()){ //0审核 1成功 2失败  type【0新增绑定 【1换车解绑（已通过绑定）】  【2离职解绑（已通过绑定）】
                case 0:
                    holder.tv_info.setText("审核中");
                    holder.tv_info.setTextColor(getResources().getColor(R.color.black));
                    holder.content.setClickable(false);
                    break;
                case 1:
                    if (list.get(position).getType() == 2){
                        holder.tv_info.setText("已解绑");
                        holder.tv_info.setTextColor(getResources().getColor(R.color.black));
                    }else {
                        holder.tv_info.setText("换车/解绑");
                        holder.tv_info.setTextColor(getResources().getColor(R.color.transparent));
                        holder.content.setClickable(true);
                        holder.content.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (SharedPreferencesHelper.getInstance(MyPlatelistActivity.this).getInt(API.isshangban, 10) == 0){
                                    Utils.toast(MyPlatelistActivity.this, "您还在上班中...");
                                    return;
                                }
                                showDialog(list, position);
                            }
                        });
                    }
                    break;
                case 2:
                    holder.tv_info.setText("不通过");
                    holder.tv_info.setTextColor(getResources().getColor(R.color.black));
                    holder.content.setClickable(true);
                    holder.content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (list.get(position).getType() == 2){ //离职解绑
                                if (SharedPreferencesHelper.getInstance(MyPlatelistActivity.this).getBoolean(API.isshangban, false)){
                                    Utils.toast(MyPlatelistActivity.this, "您还在上班中...");
                                    return;
                                }
                                new AlertDialog.Builder(MyPlatelistActivity.this).setMessage("您确定重新提交离职解绑吗")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                upload(2, list.get(position));
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).setCancelable(true).create().show();
                            }else {
                                Intent intent = new Intent(MyPlatelistActivity.this, UploadDriverCarInfoActivity.class);
                                intent.putExtra("type", 0);
                                intent.putExtra(API.islogin, true);
                                intent.putExtra("data", list.get(position));
                                startActivity(intent);
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }

        private void showDialog(final List<DriverinfoBean> list, final int position){
            new AlertDialog.Builder(MyPlatelistActivity.this).setMessage("请选择解绑类型")
                    .setPositiveButton("换车解绑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(MyPlatelistActivity.this, UploadDriverCarInfoActivity.class);
                            intent.putExtra(API.type, 1);
                            intent.putExtra(API.islogin, true);
                            intent.putExtra("id", list.get(position).getId());
                            startActivity(intent);
                        }
                    }).setNegativeButton("离职解绑", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    upload(2, list.get(position));
                }
            }).setCancelable(true).create().show();
        }

        private void upload(final int type, final DriverinfoBean bean) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    final String url = API.BASE_URL + API.ADDCARINFO;
                    OkHttpClient okHttpClient = new OkHttpClient();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("accountId", bean.getAccountId());
                        jsonObject.put("id", bean.getId());
                        jsonObject.put("businessType", bean.getBusinessType());
                        jsonObject.put("phone", bean.getPhone());
                        jsonObject.put("name", bean.getName());
                        jsonObject.put("identity", bean.getIdentity());
                        jsonObject.put("plateNumber", bean.getPlateNumber());
                        jsonObject.put("vin", bean.getVin());
                        jsonObject.put("carTypeId", bean.getCarTypeId());
                        jsonObject.put("provinceId", bean.getProvinceId());
                        jsonObject.put("cityId", bean.getCityId());
                        jsonObject.put("firstMiles", bean.getFirstMiles());
                        jsonObject.put("settleType", bean.getSettleType());
                        jsonObject.put("role", bean.getRole());
                        jsonObject.put("type", type);
                        jsonObject.put("photo", bean.getPhoto());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final FormBody formBody = new FormBody.Builder().add("driverCarInfo", jsonObject.toString()).build();
                    Utils.log(null,jsonObject.toString(),null);
                    Request request = new Request.Builder().url(url).post(formBody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Utils.log(url, formBody.toString(), "失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            Utils.log(url, formBody.toString(), json);
                            try {
                                final JSONObject jsonObject1 = new JSONObject(json);
                                if (jsonObject1.getBoolean("success")){
                                    pageNo = 1;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadData(true);
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (jsonObject1.getString("msg") != null) {
                                                    Utils.toast(MyPlatelistActivity.this, jsonObject1.getString("msg"));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

//        private void leave(int type, MyApplyModel.ContentBean.DataBean bean) {
//            final String url = API.BASE_URL + API.ADDCARINFO;
//            final RequestParams params = new RequestParams();
//            params.add("accountId", String.valueOf(bean.getAccountId()));
//            params.add("id", String.valueOf(bean.getId()));
//            params.add("businessType", String.valueOf(bean.getBusinessType()));
//            params.add("phone", String.valueOf(bean.getPhone()));
//            params.add("name", bean.getName());
//            params.add("identity", bean.getIdentity());
//            params.add("plateNumber", bean.getPlateNumber());
//            params.add("vin", bean.getVin());
//            params.add("carTypeId", String.valueOf(bean.getCarTypeId()));
//            params.add("provinceId", String.valueOf(bean.getProvinceId()));
//            params.add("cityId", String.valueOf(bean.getCityId()));
//            params.add("firstMiles", String.valueOf(bean.getFirstMiles()));
//            params.add("settleType", String.valueOf(bean.getSettleType()));
//            params.add("role", String.valueOf(bean.getRole()));
//            params.add("type", String.valueOf(type));
//            params.add("photo", String.valueOf(bean.getPhone()));
//            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//            PersistentCookieStore cookieStore = new PersistentCookieStore(MyPlatelistActivity.this);
//            asyncHttpClient.setCookieStore(cookieStore);
//            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    String json = new String(responseBody);
//                    Utils.log(url,params,json);
//                    BaseObjectString objectString = GsonUtils.getInstance().fromJson(json, BaseObjectString.class);
//                    if (objectString.isSuccess()){
//                        pageNo = 1;
//                        loadData(true);
//                    }else {
//                        if (objectString.getMsg() != null && !objectString.getMsg().equals("")) {
//                            Utils.toast(MyPlatelistActivity.this, objectString.getMsg());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    String json = new String(responseBody);
//                    Utils.log(url,params,json);
//                }
//            });
//        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_plate, tv_type, tv_info;
        private RelativeLayout content;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_plate = itemView.findViewById(R.id.tv_plate);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_info = itemView.findViewById(R.id.tv_info);
            content = itemView.findViewById(R.id.content);
        }
    }
}
