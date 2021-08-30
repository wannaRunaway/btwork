package com.kulun.energynet.ui.activity.chongdian;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityChongdianZhongBinding;
import com.kulun.energynet.db.SharedPreferencesHelper;
import com.kulun.energynet.inter.ChongdianCarConfirm;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.chongdian.ChongdianStartModel;
import com.kulun.energynet.model.chongdian.ChongdianZhongContent;
import com.kulun.energynet.model.chongdian.ChongdianZhongModel;
import com.kulun.energynet.model.chongdian.ChongdianZhuangInforContent;
import com.kulun.energynet.model.loginmodel.SmscodeModel;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.BaseActivity;
import com.kulun.energynet.ui.activity.MainActivity;
import com.kulun.energynet.ui.activity.PayActivity;
import com.kulun.energynet.ui.activity.login.BindCarActivity;
import com.kulun.energynet.ui.fragment.CarSelectorDialogFragment;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.DateUtils;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.ToastUtil;
import com.kulun.energynet.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.apache.http.Header;
/**
 * created by xuedi on 2019/7/3
 */
public class ChongdianZhongActivity extends BaseActivity implements View.OnClickListener, ChongdianCarConfirm {
    private ActivityChongdianZhongBinding binding;
    private ChongdianZhuangInforContent info;
    private boolean isdingdan = false;
    private String serialNo = "";
    private int bindId;
    private CarSelectorDialogFragment carSelectorDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chongdian_zhong);
        isdingdan = getIntent().getBooleanExtra("isdingdan", false);
        if (isdingdan) {
            serialNo = (String) getIntent().getSerializableExtra("sep");
            loadData(serialNo);
            binding.tvChongdianPress.setText("结束充电");
            long startTime = Long.parseLong(SharedPreferencesHelper.getInstance(this).getString(API.start_time_chongdian, "0"));
            if (startTime != 0){
                binding.chTime.setBase(SystemClock.elapsedRealtime()- (System.currentTimeMillis() - startTime));
            }else {
                binding.chTime.setBase(SystemClock.elapsedRealtime());
            }
            binding.chTime.start();
        }else {
            SharedPreferencesHelper.getInstance(this).putString(API.start_time_chongdian, "0");
        }
        info = (ChongdianZhuangInforContent) getIntent().getSerializableExtra("info");
        binding.tvBack.setOnClickListener(this);
        binding.tvChongdianPress.setOnClickListener(this);
        binding.tvAddCar.setOnClickListener(this);
        binding.tvSelectCar.setOnClickListener(this);
        binding.tvRefresh.setOnClickListener(this);
        String bindCar_chongdian = SharedPreferencesHelper.getInstance(this).getAccountString(API.sp_chongdian, "");
        if (bindCar_chongdian.isEmpty()) {
            binding.tvSelectCar.setText("选择充电车辆");
        } else {
            binding.tvSelectCar.setText(bindCar_chongdian);
            bindId = Integer.parseInt(SharedPreferencesHelper.getInstance(this).getAccountString(API.bindId_chongdian, String.valueOf(0)));
        }
        binding.smartrefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!serialNo.isEmpty()) {
                    loadData(serialNo);
                } else {
                    finishLoad();
                }
            }
        });
        carSelectorDialogFragment = new CarSelectorDialogFragment();
        initBaseView();
        binding.progress.setValue(1f);
    }

    private void initBaseView() {
        binding.tvStationname.setText("充电站：" + info.getStation().getStationName());
        binding.tvCode.setText("充电桩编号：" + info.getConnector().getConnectorName());
        binding.tvPark.setText("停车服务：" + info.getStation().getParkFee());
        binding.tvChongdianPrice.setText("充电费：" + info.getPolicy().getElecPrice() + "元/度");
        binding.tvFuwuPrice.setText("服务费：" + info.getPolicy().getServicePrice() + "元/度");
    }

    private void loadData(String content) {
        //startChargeSeq
        //[string]	是	订单编号
        final String url = API.BASE_URL + API.CHONGDIAN_DIANDAN;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("startChargeSeq", content);
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setCookieStore(cookieStore);
        Utils.log(url, requestParams, null);
        httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                finishLoad();
                String json = new String(responseBody);
                Utils.log(null, "", json);
                ChongdianZhongModel model = GsonUtils.getInstance().fromJson(json, ChongdianZhongModel.class);
                if (model.isSuccess()) {
                    initView(model);
                } else {
                    Utils.toast(ChongdianZhongActivity.this, model.getMsg());
                    if (model.getContent().getStopReason() == 2) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChongdianZhongActivity.this);
                        builder.setMessage("您的充电余额不足，请充值");
                        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(ChongdianZhongActivity.this, PayActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {
                finishLoad();
                Utils.toast(ChongdianZhongActivity.this, API.error_internet);
            }
        });
    }

    private void finishLoad() {
        binding.smartrefreshLayout.finishRefresh();
        binding.smartrefreshLayout.finishLoadMore();
    }

    //初始化view
    private void initView(ChongdianZhongModel model) {
        ChongdianZhongContent content = model.getContent();
        binding.progress.setValue((Float) content.getSoc());
        binding.tvDianliu.setText("电流(A)：" + content.getCurrentA());
        binding.tvDianya.setText("电压(V)：" + content.getVoltageA());
        binding.tvGonglv.setText("功率(kw)：" + content.getPower());
        binding.tvDianliangyichong.setText(content.getTotalPower() + "度");
        binding.tvDianfei.setText(content.getTotalMoney() + "元");
        if (content.getStartChargeSeqStat() == 2 || content.getStartChargeSeqStat() == 4) {
            binding.tvChongdianPress.setClickable(true);
            binding.tvChongdianPress.setBackground(getResources().getDrawable(R.drawable.login_on));
        } else {
            binding.tvChongdianPress.setClickable(false);
            binding.tvChongdianPress.setBackground(getResources().getDrawable(R.drawable.grey_back));
        }
        if (content.getStartChargeSeqStat() == 4){
            SharedPreferencesHelper.getInstance(this).putString(API.start_time_chongdian, "0");
            binding.chTime.stop();
            isdingdan = false;
            binding.tvChongdianPress.setText("开始充电");
        }else {
            isdingdan = true;
        }
    }

    private long lastClickTime;

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis(); // 此方法仅用于Android
        if (time - lastClickTime < 5000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                toMainActivity();
                break;
            case R.id.tv_chongdian_press:
                if (isFastDoubleClick()) {
                    Utils.toast(ChongdianZhongActivity.this, "按钮点击过快");
                    return;
                }
                if (binding.tvChongdianPress.getText().toString().equals("开始充电")) {
                    startChargeing();
                } else {
                    endChargeing();
                }
                break;
            case R.id.tv_select_car:
                if (isdingdan){
                    Utils.toast(ChongdianZhongActivity.this, "充电中不可点击");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean(API.isChongdian, true);
                carSelectorDialogFragment.setArguments(bundle);
                carSelectorDialogFragment.show(getSupportFragmentManager(), "data");
                break;
            case R.id.tv_add_car:
                if (isdingdan){
                    Utils.toast(ChongdianZhongActivity.this, "充电中不可点击");
                    return;
                }
                Intent intent = new Intent(ChongdianZhongActivity.this, BindCarActivity.class);
                intent.putExtra("isregister", false);
                startActivity(intent);
                break;
            case R.id.tv_refresh:
                if (!serialNo.isEmpty()) {
                    loadData(serialNo);
                } else {
                    finishLoad();
                }
                break;
            default:
                break;
        }
    }

    //结束充电
    private void endChargeing() {
        if (bindId == 0) {
            Utils.toast(ChongdianZhongActivity.this, "请先选择车牌");
            return;
        }
        if (serialNo.isEmpty()) {
            Utils.toast(ChongdianZhongActivity.this, "请先开始充电");
            return;
        }
        final String url = API.BASE_URL + API.CHONGDIAN_JIESHU;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("startChargeSeq", serialNo);
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                SmscodeModel smscodeModel = GsonUtils.getInstance().fromJson(json, SmscodeModel.class);
                if (smscodeModel.isSuccess()) {
                    Utils.toast(ChongdianZhongActivity.this, "充电结束");
                    binding.tvChongdianPress.setText("开始充电");
                    SharedPreferencesHelper.getInstance(ChongdianZhongActivity.this).putString(API.start_time_chongdian, "0");
                    binding.chTime.stop();
                    isdingdan = false;
                } else {
                    Utils.toast(ChongdianZhongActivity.this, smscodeModel.getMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(ChongdianZhongActivity.this, API.error_internet);
            }
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MainApp.getInstance().getApplicationContext().startActivity(intent);
        Constants.whichFragment = 0;
    }

    @Override
    public void onBackPressed() {
        toMainActivity();
    }

    //开始充电
    private void startChargeing() {
        if (bindId == 0) {
            Utils.toast(ChongdianZhongActivity.this, "请先选择车牌");
            return;
        }
        final String url = API.BASE_URL + API.CHONGDIAN_KAISHI;
        final RequestParams requestParams = new RequestParams();
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("connectorId", String.valueOf(info.getConnector().getId()));
        requestParams.add("bindId", String.valueOf(bindId));
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                ChongdianStartModel model = GsonUtils.getInstance().fromJson(json, ChongdianStartModel.class);
                if (model.isSuccess()) {
                    serialNo = model.getContent();
                    loadData(model.getContent());
                    binding.tvChongdianPress.setText("结束充电");
                    binding.chTime.setBase(SystemClock.elapsedRealtime());
                    binding.chTime.start();
                    isdingdan = true;
                } else {
                    if (model != null) {
                        Utils.toast(ChongdianZhongActivity.this, model.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(ChongdianZhongActivity.this, API.error_internet);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.chTime.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.chTime.stop();
    }

    @Override
    public void click(String plateNum, int bindId) {
        this.bindId = bindId;
        binding.tvSelectCar.setText("车牌号：" + plateNum);
        SharedPreferencesHelper.getInstance(this).putAccountString(API.sp_chongdian, plateNum);
        SharedPreferencesHelper.getInstance(this).putAccountString(API.bindId_chongdian, String.valueOf(bindId));
    }
}
