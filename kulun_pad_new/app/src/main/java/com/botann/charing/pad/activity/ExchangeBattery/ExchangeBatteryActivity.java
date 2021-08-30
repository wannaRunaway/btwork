package com.botann.charing.pad.activity.ExchangeBattery;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charing.pad.activity.caridident.CarIdIdentActivity;
import com.botann.charing.pad.base.OnMultiClickListener;
import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charing.pad.callbacks.SGOnAlertClick;
import com.botann.charing.pad.activity.BaseActivity;
import com.botann.charing.pad.activity.ExchangeBattery.adapter.NextBatteryGroupAdapter;
import com.botann.charing.pad.activity.ExchangeBattery.adapter.PrevBatteryGroupAdapter;
import com.botann.charing.pad.activity.Pay.PaymentActivity;
import com.botann.charing.pad.components.zxing.decode.Utils;
import com.botann.charing.pad.model.carinfo.Carinfo;
import com.botann.charing.pad.model.LsBatteryInfoPack;
import com.botann.charing.pad.utils.GsonUtils;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import java.util.ArrayList;
import java.util.List;

import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.components.zxing.QRScanActivity;
import com.botann.charing.pad.model.BatteryGroup;
import com.botann.charing.pad.utils.ToastUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.botann.charing.pad.activity.ExchangeBattery.ExchangeBatteryActivity.*;


/**
 * Created by mengchenyun on 2016/12/28.
 * 2种业务场景：
 * 1）：排队过来的直接换电
 * 2）：直接换电录入
 * 车牌、帐号都可以先扫描，唯一则获取信息，对应多个需要扫描另一个
 */

public class ExchangeBatteryActivity extends BaseActivity implements View.OnClickListener, ClickAccount {

    private Spinner spBatteryAmount;
    private Spinner spBatteryAmountNext;
    private EditText etAccount;
    private EditText etMiles;
    private EditText etPreTotalMiles;
    private EditText etDriverName;
    private EditText etCarNo;
    private EditText etPrevSOC, etupSOC;
    private EditText etNextSOC;
    private EditText etBatteryColor;
    private EditText etRemark;
    private ExpandableHeightListView prevBatteryGroupView;
    private ExpandableHeightListView nextBatteryGroupView;
    private List<BatteryGroup> prevBatteryList = new ArrayList<BatteryGroup>();
    private List<BatteryGroup> nextBatteryList = new ArrayList<BatteryGroup>();
    private List prevBatteryCodes;
    private List nextBatteryCodes;
    private String batteryNum = "1";
    private String batteryNumUp = "1";
    private Button btnScan;
    private Button btnSearch;
    private Button btnGetNextBatteryInfo;
    private String batteriesStr;
    private Button bt_scancar;
    private Spinner spinner_batteryup, spinner_batterydown;
    private String batteryputonNum = "0";
    private String batterydownNum = "0";
    /**
     * 新加入车牌选择和扫描,换上电池数量
     */
    private Spinner spinner_carid;
    private String carPlateNum = "";
    private int exchangeBatteryNum = 2;
    private boolean isJustOnce = true;

    @Override
    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setTitle("换电录入（" + User.shared().getStation() + "）");
    }

    @Override
    protected void onStart() {
        super.onStart();
        resolveIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    /**
     * 其他界面跳转进入此界面
     * 需要清除所有的ui,换电排队则获取账号和车牌，然后获取信息
     */
    private void resolveIntent(Intent intent) {
        if (intent.getBooleanExtra("clear", false)) {
            intent.putExtra("clear", false);
            clearAll();
            String account = intent.getStringExtra("account");
            String platenumber = intent.getStringExtra("platenumber");
            if (platenumber == null || platenumber.equals("")) {
                platenumber = "";
            }
            carPlateNum = platenumber;
            etAccount.setText(null != account && !"".equals(account) ? account : "");
            setSpinnerAdapter(platenumber, null);
            spinner_carid.setSelection(0);
            if (account != null && !account.equals("")) {
                loadCarId(platenumber, account);
            }
        }
    }

    /**
     * 网络请求失败，清除除了账号和车牌之外的所有界面
     */
    private void clear() {
        etDriverName.setText("");
        etPrevSOC.setText("");
        etMiles.setText("");
        etRemark.setText("");
        etupSOC.setText("0");
        etPreTotalMiles.setText("");
        prevBatteryList.clear();
        setPrevBatteryGroup(1);
    }

    /**
     * 退出界面，清除ui
     */
    private void clearAll() {
        carPlateNum = "";
        setSpinnerAdapter(null, null);
        etAccount.setText("");
        etDriverName.setText("");
        etPrevSOC.setText("");
        etMiles.setText("");
        etRemark.setText("");
        etupSOC.setText("0");
        etPreTotalMiles.setText("");
        prevBatteryList.clear();
        spinner_batteryup.setSelection(0);
        spinner_batterydown.setSelection(0);
        setPrevBatteryGroup(1);
    }

    /**
     * 确定layout
     */
    @Override
    public int viewLayout() {
        return R.layout.activity_exchange_battery_main;
    }
    private long time;
    /**
     * baseActivity里面的初始化，view init
     */
    @Override
    public void initView() {
        etAccount = (EditText) findViewById(R.id.et_account);
        btnScan = (Button) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(this);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        spinner_carid = (Spinner) findViewById(R.id.spinner_carid);
        spinner_batteryup = (Spinner) findViewById(R.id.spinner_batteryup);
        spinner_batterydown = findViewById(R.id.spinner_batterydown);
        initSpinner();
        etDriverName = (EditText) findViewById(R.id.etDriverName);
        spBatteryAmount = (Spinner) findViewById(R.id.sp_battery);
        spBatteryAmount.setEnabled(false);
        spBatteryAmountNext = (Spinner) findViewById(R.id.sp_battery_next);
        etPreTotalMiles = (EditText) findViewById(R.id.etPreTotalMiles);
        etMiles = (EditText) findViewById(R.id.etMiles);
        etRemark = (EditText) findViewById(R.id.etRemark);
        etPrevSOC = (EditText) findViewById(R.id.etPrevSOC);
        etupSOC = findViewById(R.id.etupSOC);
        etupSOC.setText("0");
        btnGetNextBatteryInfo = (Button) findViewById(R.id.getNewBatteryInfo);
        btnGetNextBatteryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGetNextBatteryInfo.setEnabled(false);
                String account = etAccount.getText().toString();
                if (account.isEmpty()) {
                    ToastUtil.showToast(mContext, "必须先获取账户名！");
                    btnGetNextBatteryInfo.setEnabled(true);
                } else {
                }
            }
        });
        initialSpBattery();
        etNextSOC = (EditText) findViewById(R.id.etNextSOC);
        prevBatteryGroupView = (ExpandableHeightListView) findViewById(R.id.prevBatteryGroup_list);
        nextBatteryGroupView = (ExpandableHeightListView) findViewById(R.id.nextBatteryGroup_list);
        etBatteryColor = (EditText) findViewById(R.id.etBatteryColor);
        final Button btnSubmit = (Button) findViewById(R.id.btn_right);
        btnSubmit.setVisibility(Button.VISIBLE);
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnSubmit.setEnabled(false);
//                postSubmit(new SGFinishCallBack() {
//                    @Override
//                    public void onFinish(Boolean result) {
//                        btnSubmit.setEnabled(true);
//                    }
//                });
//            }
//        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() - time < 5000) {
                    Toast.makeText(mContext, "点击过快", Toast.LENGTH_SHORT).show();
                    return;
                }
                time = System.currentTimeMillis();
                postSubmit();
            }
        });
//        btnSubmit.setOnClickListener(new OnMultiClickListener() {
//            @Override
//            public void onMultiClick(View v) {
//                btnSubmit.setEnabled(false);
//                postSubmit(new SGFinishCallBack() {
//                    @Override
//                    public void onFinish(Boolean result) {
//                        btnSubmit.setEnabled(true);
//                    }
//                });
//            }
//
//            @Override
//            public void onNoneClick(View view) {
//                Log.d("xuedi", "点击onNoneClick...");
//                Toast.makeText(mContext, "点击过快", Toast.LENGTH_SHORT).show();
////                ToastUtil.showMyToast(toast, 3000);
//            }
//        });
        spBatteryAmount.setSelection(0);
        spBatteryAmountNext.setSelection(0);
        setAllBatteryList(1);
        bt_scancar = (Button) findViewById(R.id.btn_scancar);
        bt_scancar.setOnClickListener(this);
    }


    /**
     * 帐号二维码返回，请求数据(只有账号请求，账号车牌一起请求)
     * 扫描车牌返回，请求数据（只有车牌请求， 账号车牌一起请求）
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {  //帐号二维码识别
            String account = data.getStringExtra(QRScanActivity.ALL);
            if (resultCode != RESULT_OK) {
                ToastUtil.showToast(mContext, "扫描识别失败！");
                return;
            }
            etAccount.setText(account);
            /*
             * 车牌为空，单一帐号获取用户细信息，并给予提示
             * 车牌不为空，账号和车牌2个参数获取用户信息，并给与提示
             * */
            if (carPlateNum.equals("")) {
                loadCarbyAccount(account);
            } else {
                loadCarId(carPlateNum, account);
            }
        } else if (requestCode == 1001 && resultCode == 1002) {  //车牌扫描识别
            String carid = data.getStringExtra("carid");
            carPlateNum = carid;
            setSpinnerAdapter(carid, null);
            if (etAccount.getText().toString() == null || etAccount.getText().toString().isEmpty()) {
                getAccountByPlate(carid);
            } else {
                loadCarId(carid, etAccount.getText().toString());
            }
        }
    }

    @Override
    public void clickAccount(String account, String carPlate) {
        etAccount.setText(account);
        loadCarId(carPlate, account);
    }

    private void getAccountByPlate(final String carPlate) {//plateNumber
        URLParams urlParams = new URLParams();
        urlParams.put("plateNumber", carPlate);
        SGHTTPManager.POST(API.GET_ACCOUNT_BYPLATE, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    if (fetchModel.getJsonArray().length() > 0) {
                        JSONArray jsonArray = fetchModel.getJsonArray();
                        List<AccountByPlateModelContent> list = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                AccountByPlateModelContent content = new AccountByPlateModelContent();
                                content.setName(jsonObject.getString("name"));
                                content.setPhone(jsonObject.getString("phone"));
                                content.setAccount(jsonObject.getString("account"));
                                list.add(content);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        showMyDialog(list, carPlate);
                    } else {
                        showToast("当前车牌没有绑定帐号");
                    }
                } else {
                    showToast(userInfo);
                }
            }
        });
    }

    //显示选择弹窗
    private AlertDialog alertDialog;

    private void showMyDialog(List<AccountByPlateModelContent> content, String carPlate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_exchange, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(content, this, carPlate));
        alertDialog = builder.setView(view).create();
        alertDialog.show();
    }

    /**
     * 通过账号获取信息
     */
    private void loadCarbyAccount(String value) {
        URLParams urlParams = new URLParams();
        urlParams.put("account", value);
        SGHTTPManager.POST(API.URL_CARINFO_BYACCOUNT, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                refreshUI(isSuccess, userInfo, fetchModel, true);
            }
        });
    }

    private String carId;

    /**
     * 车牌和账户一起请求、或者车牌独自请求， 获取信息
     */
    private void loadCarId(String carId, String account) {
        URLParams urlParams = new URLParams();
        urlParams.put("plateNumber", carId);
        if (null != account && !account.equals("")) {
            urlParams.put("account", account);
        }
        SGHTTPManager.POST(API.URL_CARID_INFO, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                refreshUI(isSuccess, userInfo, fetchModel, false);
            }
        });
    }

    /**
     * 司机信息请求完毕后，刷新界面ui
     * 加入多辆车集合
     */
    private void refreshUI(Boolean isSuccess, String userInfo, SGFetchModel fetchModel, boolean isAccount) {
        if (isSuccess) {
            Carinfo carinfo = SGFetchModel.getGson().fromJson(fetchModel.getJsonObject().toString(), Carinfo.class);
            if (isAccount) {
                setSpinnerAdapter(carinfo.getCar().getCarplate(), null);
            } else {
                choseSpinnerPlate(carinfo.getCar().getCarplate(), carinfo.getAccountPlateNumber());
            }
            etDriverName.setText(carinfo.getCar().getUsername());
            etPreTotalMiles.setText(carinfo.getCar().getOldTotalMiles());
            etMiles.setText(carinfo.getCar().getTotalMiles());
            etPrevSOC.setText(carinfo.getCar().getSoc());
            etupSOC.setText("0");
            etAccount.setText(carinfo.getAccount());
            carId = carinfo.getCarId();
            String car = fetchModel.getSting("car");
            try {
                JSONObject jsonObject = new JSONObject(car);
                batteriesStr = jsonObject.getString("lsBatteryInfoPack");
            } catch (JSONException e) {
                Toast.makeText(ExchangeBatteryActivity.this, "lsBatteryInfoPack解析错误", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            setPrevBatteryGroup(carinfo.getCar().getLsBatteryInfoPack());
        } else if (fetchModel.code == 2002) {
            if (isAccount) {
                List<String> persons = SGFetchModel.getGson().fromJson(fetchModel.content, new TypeToken<List<String>>() {
                }.getType());
                setSpinnerAdapter(null, persons);
            }
            showToast(userInfo);
        } else {
            showToast(userInfo);
            clear();
        }
    }

    /**
     * toast提示
     */
    private void showToast(String message) {
        Toast toast = Toast.makeText(ExchangeBatteryActivity.this, message, Toast.LENGTH_LONG);
        ToastUtil.showMyToast(toast, 5000);
    }

    /**
     * 生成提交订单
     */
    public void postSubmit() {
        if (!etupSOC.getText().toString().equals("")){
            Double upsoc = Double.valueOf(etupSOC.getText().toString());
            if (upsoc <0 || upsoc>100){
                Toast.makeText(mContext, "换上soc的范围在0~100之间", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        showProgressHud("正在提交换电信息...");
        URLParams params = new URLParams();
        final String account = etAccount.getText().toString();
        params.put("account", account);
        params.put("carId", carId);
        params.put("siteId", User.shared().getStationId());
        params.put("soc", etPrevSOC.getText());
        params.put("upSoc", etupSOC.getText().toString());
        params.put("mile", etMiles.getText());
        params.put("remark", etRemark.getText());
        params.put("upBatteryNum", Integer.parseInt(batteryputonNum));
        params.put("batteryNum", Integer.parseInt(batterydownNum));
        params.put("upBatteryCount", exchangeBatteryNum);
        SGHTTPManager.POST(API.URL_EXCHANGE_CONFIRM_CHECK, params, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                hideProgressHud();
                if (isSuccess) {
                    Intent intent = new Intent(getApplicationContext(), ExchangeBatteryInfoConfirmActivity.class);
                    JSONObject recordObject = null, shareRecord = null;
                    try {
                        recordObject = fetchModel.getJsonObject().getJSONObject("record");
                        if (recordObject != null) {
                            intent.putExtra("exchangeRecordId", recordObject.getString("id"));
                            intent.putExtra("account", etAccount.getText().toString());
                            intent.putExtra("driverName", etDriverName.getText().toString());//用户名
                            intent.putExtra("accountBalance", recordObject.getString("accountBalance"));//余额
                            intent.putExtra("fare", recordObject.getDouble("fare"));//需支付
                            intent.putExtra("carMile", recordObject.getString("carMile"));  //总里程
                            intent.putExtra("travelMile", recordObject.getInt("travelMile"));    //行驶里程
                            intent.putExtra("chargeMile", recordObject.getString("chargeMile"));//计费里程
                            intent.putExtra("couponUse", recordObject.getString("couponUse"));
                            intent.putExtra("realFare", recordObject.getString("realFear"));
                            intent.putExtra("accountType", recordObject.getString("exchangeRuleType"));//0对私,1对公(冗余)
                            intent.putExtra("batteryS", batteriesStr);
                            intent.putExtra("carplateNum", recordObject.getString("carPlateNumber"));
                        }
                        shareRecord = fetchModel.getJsonObject().getJSONObject("shareRecord");
                        if (shareRecord != null) {
                            intent.putExtra("sharename", shareRecord.getString("accountName"));
                            Utils.log("进入了解析里面");
                            intent.putExtra("sharepay", shareRecord.getDouble("realFear"));
                            intent.putExtra("sharelicheng", shareRecord.getString("chargeMile"));
                            intent.putExtra("shareid", shareRecord.getInt("id"));
                            intent.putExtra("fenzhangyouhuiquan", shareRecord.getString("couponUse"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                } else {
                    if (userInfo != null && !userInfo.equals("")) {
//                    if (fetchModel.code == 201) { // 对私余额不足 202对公余额不足
                        alert(userInfo, "充值", new SGOnAlertClick() {
                            @Override
                            public void onClick(DialogInterface dialog, int index) {
                                if (index > 0) {
                                    Intent toPaymentActivity = new Intent(getApplicationContext(), PaymentActivity.class);
                                    toPaymentActivity.putExtra("account", account);
                                    startActivity(toPaymentActivity);
                                }
                            }
                        });
                    }
//                    } else if (fetchModel.code == 201) {
//                        alert("扣款账户(对公账户)余额不足，无法换电！");
//                    } else {
//                        alert(userInfo);
//                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * 车牌点击识别
             */
            case R.id.btn_scancar:
                if (etAccount.getText().toString().isEmpty() || isJustOnce) {
                    isJustOnce = false;
                    request_permissions();
                    if (permissionList != null && permissionList.size() == 0) {
                        Intent intent = new Intent(ExchangeBatteryActivity.this, CarIdIdentActivity.class);
                        startActivityForResult(intent, 1001);
                    }
                } else {
                    finish();
                }
                break;
            /**
             * 二维码点击识别
             */
            case R.id.btn_scan:
                Intent toScan = new Intent(getApplicationContext(), QRScanActivity.class);
                startActivityForResult(toScan, 1);
                break;
            /**
             * 点击获取拉取信息
             */
            case R.id.btn_search:
                hideKeybord(etAccount);
                if (etAccount.getText() == null || etAccount.getText().equals("")) {
                    toast("请扫描帐号二维码");
                    return;
                }
                if (carPlateNum.equals(carPlatePrompt)) {
                    showToast(carPlatePrompt);
                    return;
                }
                if (!carPlateNum.equals("")) {
                    loadCarId(carPlateNum, etAccount.getText().toString());
                    return;
                }
                loadCarbyAccount(etAccount.getText().toString());
            default:
                break;
        }
    }

    /**
     * 初始化spinnerAdapter, 点击获取
     */
    private ArrayAdapter<String> spinnerAdapter;

    private void initSpinner() {
        /**
         * 车牌spinner初始化
         */
        spinnerAdapter = new ArrayAdapter<String>(ExchangeBatteryActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_carid.setAdapter(spinnerAdapter);
        spinner_carid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                carPlateNum = spinnerAdapter.getItem(position);
                if (carPlateNum.equals(carPlatePrompt)) {
                    showToast("请选择车牌");
                    return;
                }
                if (carPlateNum != null && !carPlateNum.equals("")) {
                    loadCarId(carPlateNum, etAccount.getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /**
         * 换上电池spinner
         */
        String[] batteryup = {"2", "3", "4", "6", "7", "8"};
        final ArrayAdapter<String> batteryupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, batteryup);
        batteryupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_batteryup.setAdapter(batteryupAdapter);
        spinner_batteryup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                batteryputonNum = batteryupAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //换下电池spinner
        String[] batterydown = {"2", "3", "4", "6", "7", "8"};
        final ArrayAdapter<String> batterydownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, batterydown);
        batterydownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_batterydown.setAdapter(batterydownAdapter);
        spinner_batterydown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                batterydownNum = batterydownAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * spinner刷新
     * 设置提醒默认选中的车牌号
     */
    private void setSpinnerAdapter(String data, List<String> datalist) {
        spinnerAdapter.clear();
        if (data != null) {
            spinnerAdapter.add(data);
        }
        if (datalist != null) {
            spinnerAdapter.add(carPlatePrompt);
            spinnerAdapter.addAll(datalist);
        }
    }

    /*
     * 请求完毕， spinner需要确定设置的位置
     */
    private void choseSpinnerPlate(String data, List<String> datalist) {
        if (datalist.size() == 0) {
            setSpinnerAdapter(data, datalist);
            return;
        }
        spinnerAdapter.clear();
        spinnerAdapter.addAll(datalist);
        for (int i = 0; i < datalist.size(); i++) {
            if (data.equals(datalist.get(i))) {
                spinner_carid.setSelection(i);
            }
        }
    }

    private final String carPlatePrompt = "请选择车牌";

    /**
     * 下方电池等信息展示
     */
    private void initialSpBattery() {
        List<String> batterys = new ArrayList<String>();
        for (int i = 1; i < 9; i++) {
            batterys.add(i + "");
        }
        ArrayAdapter<String> batteryAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, batterys);
        spBatteryAmount.setAdapter(batteryAdapter);
        spBatteryAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String num = parent.getItemAtPosition(position).toString();
                switch (num) {
                    case "1":
                        setPrevBatteryGroup(1);
                        batteryNum = "1";
                        break;
                    case "2":
                        setPrevBatteryGroup(2);
                        batteryNum = "2";
                        break;
                    case "3":
                        setPrevBatteryGroup(3);
                        batteryNum = "3";
                        break;
                    case "4":
                        setPrevBatteryGroup(4);
                        batteryNum = "4";
                        break;
                    case "5":
                        setPrevBatteryGroup(5);
                        batteryNum = "5";
                        break;
                    case "6":
                        setPrevBatteryGroup(6);
                        batteryNum = "6";
                        break;
                    case "7":
                        setPrevBatteryGroup(7);
                        batteryNum = "7";
                        break;
                    case "8":
                        setPrevBatteryGroup(8);
                        batteryNum = "8";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBatteryAmountNext.setAdapter(batteryAdapter);
        spBatteryAmountNext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String num = parent.getItemAtPosition(position).toString();
                switch (num) {
                    case "1":
                        setNextBatteryGroup(1);
                        batteryNumUp = "1";
                        break;
                    case "2":
                        setNextBatteryGroup(2);
                        batteryNumUp = "2";
                        break;
                    case "3":
                        setNextBatteryGroup(3);
                        batteryNumUp = "3";
                        break;
                    case "4":
                        setNextBatteryGroup(4);
                        batteryNumUp = "4";
                        break;
                    case "5":
                        setNextBatteryGroup(5);
                        batteryNumUp = "5";
                        break;
                    case "6":
                        setNextBatteryGroup(6);
                        batteryNumUp = "6";
                        break;
                    case "7":
                        setNextBatteryGroup(7);
                        batteryNumUp = "7";
                        break;
                    case "8":
                        setNextBatteryGroup(8);
                        batteryNumUp = "8";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setAllBatteryList(int num) {
        setPrevBatteryGroup(num);
        setNextBatteryGroup(num);
    }

    public void setPrevBatteryGroup(int num) {
        int size = prevBatteryList.size();
        if (size == 0) {
            prevBatteryList = new ArrayList<BatteryGroup>();
            for (int i = 0; i < num; i++) {
                BatteryGroup prevBatteryGroup = new BatteryGroup("电池编号" + (i + 1), "",
                        "最高单体" + (i + 1), "", "最低单体" + (i + 1), "");
                prevBatteryList.add(prevBatteryGroup);
            }
            setPrevBatteryList(prevBatteryList);
        } else {
            if (num <= size) {
                prevBatteryList = prevBatteryList.subList(0, num);
                setPrevBatteryList(prevBatteryList);
            } else {
                for (int i = size; i < num; i++) {
                    BatteryGroup prevBatteryGroup = new BatteryGroup("电池编号" + (i + 1), "",
                            "最高单体" + (i + 1), "", "最低单体" + (i + 1), "");
                    prevBatteryList.add(prevBatteryGroup);
                }
                setPrevBatteryList(prevBatteryList);
            }
        }
    }

    public void setPrevBatteryGroup(List<LsBatteryInfoPack> batteryInfoPacks) {
        prevBatteryList = new ArrayList<BatteryGroup>();
        int i = 1;
        for (LsBatteryInfoPack batteryInfoPack : batteryInfoPacks) {
            BatteryGroup batteryGroup = new BatteryGroup("电池编号" + i, batteryInfoPack.getBatteryNumber(),
                    "最高单体" + i, batteryInfoPack.getHighestSingletonVolt(),
                    "最低单体" + i, batteryInfoPack.getLowestSingletonVolt());
            prevBatteryList.add(batteryGroup);
            i++;
        }
        int size = batteryInfoPacks.size();
        if (size > 0) spBatteryAmount.setSelection(size - 1);
        setPrevBatteryList(prevBatteryList);
    }

    public void setNextBatteryGroup(int num) {
        int size = nextBatteryList.size();
        if (size == 0) {
            nextBatteryList = new ArrayList<BatteryGroup>();
            for (int i = 0; i < num; i++) {
                BatteryGroup nextBatteryGroup = new BatteryGroup("电池编号" + (i + 1), "",
                        "最高单体" + (i + 1), "", "最低单体" + (i + 1), "");
                nextBatteryList.add(nextBatteryGroup);
            }
            setNextBatteryList(nextBatteryList);
        } else {
            if (num <= size) {
                nextBatteryList = nextBatteryList.subList(0, num);
                setNextBatteryList(nextBatteryList);
            } else {
                for (int i = size; i < num; i++) {
                    BatteryGroup nextBatteryGroup = new BatteryGroup("电池编号" + (i + 1), "",
                            "最高单体" + (i + 1), "", "最低单体" + (i + 1), "");
                    nextBatteryList.add(nextBatteryGroup);
                }
                setNextBatteryList(nextBatteryList);
            }
        }
    }

    public void setNextBatteryGroup(List<LsBatteryInfoPack> batteryInfoPacks) {
        nextBatteryList = new ArrayList<BatteryGroup>();
        int i = 1;
        for (LsBatteryInfoPack batteryInfoPack : batteryInfoPacks) {
            BatteryGroup batteryGroup = new BatteryGroup("电池编号" + i, batteryInfoPack.getBatteryNumber(),
                    "最高单体" + i, batteryInfoPack.getHighestSingletonVolt(),
                    "最低单体" + i, batteryInfoPack.getLowestSingletonVolt());
            nextBatteryList.add(batteryGroup);
            i++;
        }
        int size = batteryInfoPacks.size();
        spBatteryAmountNext.setSelection(size - 1);
        setNextBatteryList(nextBatteryList);
    }

    public void setPrevBatteryList(List<BatteryGroup> batteryItems) {
        PrevBatteryGroupAdapter prevBatteryAdapter = new PrevBatteryGroupAdapter(this, batteryItems);
        prevBatteryGroupView.setExpanded(true);
        prevBatteryGroupView.setAdapter(prevBatteryAdapter);
    }

    public void setNextBatteryList(List<BatteryGroup> batteryItems) {
        NextBatteryGroupAdapter nextBatteryAdapter = new NextBatteryGroupAdapter(this, batteryItems);
        nextBatteryGroupView.setExpanded(true);
        nextBatteryGroupView.setAdapter(nextBatteryAdapter);
    }

    /**
     * 车牌和二维码识别需要照相机、读写内存卡多个权限
     */
    List<String> permissionList = new ArrayList<>();

    private void request_permissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), 1002);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:
                permissionList.clear();
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(ExchangeBatteryActivity.this, permissions[i] + "权限被拒绝了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<AccountByPlateModelContent> content;
        private ClickAccount clickAccount;
        private String carPlate;

        public MyAdapter(List<AccountByPlateModelContent> content, ClickAccount clickAccount, String carPlate) {
            this.carPlate = carPlate;
            this.content = content;
            this.clickAccount = clickAccount;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ExchangeBatteryActivity.this).inflate(R.layout.adapter_account_byplate, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final AccountByPlateModelContent data = content.get(position);
            holder.name.setText(data.getName() + "");
            holder.phone.setText(data.getPhone() + "");
            holder.re.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    clickAccount.clickAccount(data.getAccount(), carPlate);
                }
            });
        }

        @Override
        public int getItemCount() {
            return content.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, phone;
        private RelativeLayout re;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            re = (RelativeLayout) itemView.findViewById(R.id.re);
        }
    }
}
