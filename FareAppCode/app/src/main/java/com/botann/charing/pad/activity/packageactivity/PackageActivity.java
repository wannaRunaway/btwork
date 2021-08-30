package com.botann.charing.pad.activity.packageactivity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charging.pad.databinding.ActivityPackageBinding;
import com.botann.charing.pad.activity.BaseActivity;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.callbacks.PromotionsInterface;
import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.components.zxing.decode.Utils;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.model.packageactivity.PackageActivityContent;
import com.botann.charing.pad.model.packageactivity.PackageActivityinfo;
import com.botann.charing.pad.model.packageactivity.PackageCarinfo;
import com.botann.charing.pad.utils.DateUtils;
import com.botann.charing.pad.utils.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xuedi on 2019/2/22
 * 扫描二维码，账号请求先得到车辆，车辆唯一直接请求得到活动列表；车辆不唯一则得到车辆列表，选择车辆得到活动列表。
 */
public class PackageActivity extends BaseActivity implements View.OnClickListener, PromotionsInterface, AdapterView.OnItemSelectedListener {
    private ActivityPackageBinding binding;
    private PackageAdapter packageAdapter;
    private List<PackageActivityinfo> activitylist = new ArrayList<>();
    private List<PackageCarinfo> carinfoList;
    private int pageNum = 1;
    private int carId;
    private String carPlate = "";

    @Override
    public int viewLayout() {
        return R.layout.activity_package;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package);
        packageAdapter = new PackageAdapter(this, activitylist, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(packageAdapter);
        binding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(true);
                Log.d("xuedi", "onRefresh");
            }
        });
        binding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum = pageNum + 1;
                loadData(false);
                Log.d("xuedi", "onLoadMore");
            }
        });
        binding.btnScan.setOnClickListener(this);
        binding.btnLeft.setOnClickListener(this);
    }

    private final String CAR_FIRST_STRING = "请选择车牌";

    //size 1不显示提示选项，size>1显示选择车牌
    private void initSpinner(Spinner spinner, List<PackageCarinfo> list) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            stringList.add(list.get(i).getCarNo());
        }
        if (list.size() > 1) {
            stringList.add(0, CAR_FIRST_STRING);
        }
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringList);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(levelAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    //加载活动列表
    private void loadData(final boolean isRefresh) {
        if (binding.etAccount.getText().toString().isEmpty()) {
            Toast.makeText(this, "帐号不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        URLParams urlParams = new URLParams();
        urlParams.put("account", binding.etAccount.getText().toString());
        urlParams.put("siteId", User.shared().getStationId());
        urlParams.put("adminId", User.shared().getId());
        if (carId != 0) {
            urlParams.put("carId", carId);
        }
        urlParams.put("pageNo", pageNum);
        SGHTTPManager.POST(API.URL_ACTIVITY_LIST, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                binding.smartRefresh.finishRefresh();
                binding.smartRefresh.finishLoadMore();
                if (isSuccess) {
                    PackageActivityContent content = SGFetchModel.getGson().fromJson(fetchModel.getJsonObject().toString(), PackageActivityContent.class);
                    if (isRefresh) {
                        activitylist.clear();
                        if (content.getActivity() == null || content.getActivity().size() == 0) {
                            if (content.getCar() != null || content.getCar().size() != 0) {
                                if (content.getCar().get(0).getReason() !=null && !content.getCar().get(0).getReason().equals("")){
                                    Toast.makeText(PackageActivity.this, content.getCar().get(0).getReason(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                    if (content.getCar() != null && content.getCar().size() != 0) {
                        initetView(content.getCar().get(0));
                    }
                    activitylist.addAll(content.getActivity());
                    if (activitylist.size() == 0) {
                        binding.tvInfo.setVisibility(View.VISIBLE);
                        binding.smartRefresh.setVisibility(View.GONE);
                    } else {
                        binding.tvInfo.setVisibility(View.GONE);
                        binding.smartRefresh.setVisibility(View.VISIBLE);
                    }
                    packageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PackageActivity.this, userInfo, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //edittext的初始化
    private void initetView(PackageCarinfo carinfo) {
        binding.etCartype.setText(carinfo.getCarType() + "");
        binding.etDefaultRule.setText(carinfo.getDefaultExchangeRuleName() + "");
        binding.etPriorityRule.setText(carinfo.getFirstExchangeRuleName() + "");
    }

    //加载车辆的list
    private void loadCarlist() {
        if (binding.etAccount.getText().toString().isEmpty()) {
            Toast.makeText(this, "帐号不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        URLParams urlParams = new URLParams();
        urlParams.put("account", binding.etAccount.getText().toString());
        urlParams.put("siteId", User.shared().getStationId());
        urlParams.put("adminId", User.shared().getId());
        urlParams.put("pageNo", pageNum);
        SGHTTPManager.POST(API.URL_ACTIVITY_LIST, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    PackageActivityContent content = SGFetchModel.getGson().fromJson(fetchModel.getJsonObject().toString(), PackageActivityContent.class);
                    if (content.getCar() == null || content.getCar().size() == 0) {
                        Toast.makeText(PackageActivity.this, "该账号没有绑定车", Toast.LENGTH_LONG).show();
                    }
                    if (content.getCar().size() > 1) {
                        Toast.makeText(PackageActivity.this, "请选择车牌", Toast.LENGTH_LONG).show();
                    }
                    carinfoList = content.getCar();
                    initSpinner(binding.spCarplate, carinfoList);
//                    if (content.getCar().size() == 1) {
//                        Log.d("xuedi", "loadCarlist");
//                        loadData(true);
//                    }
                } else {
                    Toast.makeText(PackageActivity.this, userInfo, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                Intent toScan = new Intent(PackageActivity.this, QRScanActivity.class);
                startActivityForResult(toScan, 1);
                break;
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    //点击recyclerView item
    @Override
    public void click(PackageActivityinfo promotions) {
        Intent intent = new Intent(this, PackageInfoActivity.class);
        intent.putExtra("promotions", promotions);
        intent.putExtra("carPlate", carPlate);
        intent.putExtra("account", binding.etAccount.getText().toString());
        PackageCarinfo packageCarinfo = null;
        for (int j = 0; j < carinfoList.size(); j++) {
            if (carPlate.equals(carinfoList.get(j).getCarNo())) {
                packageCarinfo = carinfoList.get(j);
            }
        }
        intent.putExtra("packageCarinfo", packageCarinfo);
        startActivity(intent);
    }

    //点击选择车牌
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_carplate:
                carPlate = (String) adapterView.getSelectedItem();
                for (int j = 0; j < carinfoList.size(); j++) {
                    if (adapterView.getSelectedItem().equals(carinfoList.get(j).getCarNo())) {
                        carId = carinfoList.get(j).getCarId();
                    }
                }
                if (!carPlate.equals(CAR_FIRST_STRING)) {
                    pageNum = 1;
                    Log.d("xuedi", "onItemSelected");
                    loadData(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private class PackageAdapter extends RecyclerView.Adapter<PackageViewHolder> {
        private Context context;
        private List<PackageActivityinfo> list;
        private PromotionsInterface anInterface;

        public PackageAdapter(PackageActivity packageActivity, List<PackageActivityinfo> activitylist, PromotionsInterface anInterface) {
            this.context = packageActivity;
            this.list = activitylist;
            this.anInterface = anInterface;
        }

        @Override
        public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_package, null);
            PackageViewHolder holder = new PackageViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PackageViewHolder holder, int position) {
            final PackageActivityinfo packageActivityinfo = list.get(position);
            holder.tv_name.setText(packageActivityinfo.getName() + "");
            holder.tv_time.setText("活动时间:" + DateUtils.stampToYear(packageActivityinfo.getStartTime()) + "~~" + DateUtils.stampToYear(packageActivityinfo.getEndTime()));
            holder.re_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    anInterface.click(packageActivityinfo);
                }
            });
            holder.tv_money.setText("￥ " + packageActivityinfo.getPackagePrice());
            holder.tv_distance.setText("");
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class PackageViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_time, tv_money, tv_distance;
        private RelativeLayout re_content;

        public PackageViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            re_content = (RelativeLayout) itemView.findViewById(R.id.re_content);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {  //帐号二维码识别
            String account = data.getStringExtra(QRScanActivity.ALL);
            if (resultCode != RESULT_OK) {
                ToastUtil.showToast(PackageActivity.this, "扫描识别失败！");
                return;
            }
            binding.etAccount.setText(account);
            loadCarlist();
        }
    }
}
