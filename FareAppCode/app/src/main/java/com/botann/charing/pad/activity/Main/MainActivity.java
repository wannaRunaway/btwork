package com.botann.charing.pad.activity.Main;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.botann.charging.pad.R;
import com.botann.charing.pad.activity.AppDownloadCode.AppDownloadCodeActivity;
import com.botann.charing.pad.activity.caridident.MRAssetUtil;
import com.botann.charing.pad.activity.caridident.MRCarUtil;
import com.botann.charing.pad.activity.caridident.PlateRecognition;
import com.botann.charing.pad.activity.lighter.BatteryLighterActivity;
import com.botann.charing.pad.activity.lighter.BatteryLighterInquireActivity;
import com.botann.charing.pad.activity.malfunctionrecord.MalfunctionRecordActivity;
import com.botann.charing.pad.activity.malfunctionrecord.MalfunctionRepairActivity;
import com.botann.charing.pad.activity.packageactivity.PackageActivity;
import com.botann.charing.pad.callbacks.SGOnAlertClick;
import com.botann.charing.pad.activity.ExchangeRecords.ExchangeRecordActivity;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charing.pad.model.ExchangeSite;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.activity.ExchangeBattery.ExchangeBatteryActivity;
import com.botann.charing.pad.activity.ExchangeOrders.ExchangeOrdersActivity;
import com.botann.charing.pad.activity.Login.LoginActivity;
import com.botann.charing.pad.activity.PayRecords.PayRecordActivity;
import com.botann.charing.pad.activity.Pay.PaymentActivity;
import com.botann.charing.pad.activity.BaseActivity;
import com.botann.charing.pad.utils.GsonUtils;
import com.pgyersdk.update.PgyUpdateManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
/**
 * Created by mengchenyun on 2016/11/21.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btn_payment;
    private LinearLayout btn_changebattery;
    private Button locateBtn;

    private Spinner spStations;
    private List<ExchangeSite> stations = new ArrayList<>();
    LocationManager mLocationManager;
    Location mLocation;
    private static final double EARTH_RADIUS = 6378137.0;
    private boolean updateLocation = true;
    private static boolean showAlertSite = false;
    private static final int CARID_REQUESTCODE = 5;
    private PlateRecognition recognition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setTitle(R.string.app_name);
        if (!User.shared().isLogin()) toLoginActivity();
        /**
         * 车牌需要移植资源文件
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CARID_REQUESTCODE);
            }
        }
        initFile();
//        getLastKnownLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //检测版本更新
        PgyUpdateManager.setIsForced(false); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
        PgyUpdateManager.register(this);
    }

    @Override
    public int viewLayout() {
        return R.layout.home_main;
    }

    @Override
    public void initView() {

        if (!User.shared().isLogin()) {
            toLoginActivity();
            toast("登录失效！");
            return;
        }
        locateBtn = findButtonById(R.id.locationBtn);
        locateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastKnownLocation();
            }
        });
        settingSpinnerStations();
        linearInit();
//        List<MainGrid> mainGrids = new ArrayList<MainGrid>();
//        mainGrids.add(new MainGrid("换电排队", "chargersOrder.png"));
//        mainGrids.add(new MainGrid("换电录入", "charge.png"));
//        mainGrids.add(new MainGrid("账号充值", "payment.png"));
//        mainGrids.add(new MainGrid("充值记录", "paymentRecord.png"));
//        mainGrids.add(new MainGrid("换电记录", "chargeRecord.png"));
//        mainGrids.add(new MainGrid("蓝色大道下载", "download_code.png"));
//        mainGrids.add(new MainGrid("电池库存", "batteryup.png"));
//        mainGrids.add(new MainGrid("故障维修", "fix.png"));
//        mainGrids.add(new MainGrid("故障查询", "search.png"));
//        MainGridAdapter mainGridAdapter = new MainGridAdapter(this, mainGrids);
//        GridView gridView = (GridView) findViewById(R.id.main_grid);
//        gridView.setAdapter(mainGridAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (User.shared().getStationId() == null) {
//                    toast("请选择站点！");
//                    return;
//                }
//                boolean checkAlert = true;
//                Intent intent = null;
//                switch (position) {
//                    case 0: // 排队换电
//                        intent = new Intent(getApplicationContext(), ExchangeOrdersActivity.class);
//                        break;
//                    case 1: // 换电录入
//                        intent = new Intent(getApplicationContext(), ExchangeBatteryActivity.class);
//                        intent.putExtra("clear", true);
//                        break;
//                    case 2: // 账号充值
//                        intent = new Intent(getApplicationContext(), PaymentActivity.class);
//                        break;
//                    case 3: // 充值记录
//                        intent = new Intent(getApplicationContext(), PayRecordActivity.class);
//                        checkAlert = false;
//                        break;
//                    case 4: // 换电记录
//                        intent = new Intent(getApplicationContext(), ExchangeRecordActivity.class);
//                        break;
//                    case 5: // 蓝色达到下载
//                        intent = new Intent(getApplicationContext(), AppDownloadCodeActivity.class);
//                        checkAlert = false;
//                        break;
//                    case 6: //电池库存
//                        intent = new Intent(getApplicationContext(), BatteryInventoryActivity.class);
//                        checkAlert = false;
//                        break;
//                    case 7: //故障维修
//                        intent = new Intent(getApplicationContext(), MalfunctionRepairActivity.class);
//                        intent.putExtra("station", GsonUtils.getInstance().toJson(stations));
//                        checkAlert = false;
//                        break;
//                    case 8: //故障记录
//                        intent = new Intent(getApplicationContext(), MalfunctionRecordActivity.class);
//                        intent.putExtra("station", GsonUtils.getInstance().toJson(stations));
//                        checkAlert = false;
//                        break;
//                    default:
//                        break;
//                }
//
//                if (checkAlert && !showAlertSite) {
//                    final Intent mIntent = intent;
//                    alert(null, "请确认当前选择的站点：" + User.shared().getStation(), "选错了", "站点正确", new SGOnAlertClick() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int index) {
//                            if (index > 0) {
//                                showAlertSite = true;
//                                startActivity(mIntent);
//                            }
//                        }
//                    });
//                } else {
//                    startActivity(intent);
//                }
//
//            }
//        });

//        int width = getResources().getDisplayMetrics().widthPixels;
//        float dp = ResolutionUtil.dpFromPx(this, width);
//        Log.i("width", Float.toString(dp));
//        if(dp <= 1000) {
//            gridView.setNumColumns(1);
//        }

        btn_payment = (LinearLayout) findViewById(R.id.layout_payment);
        btn_changebattery = (LinearLayout) findViewById(R.id.layout_change_battery);

        btn_payment.setOnClickListener(this);
        btn_changebattery.setOnClickListener(this);
    }

    private void linearInit() {
        LinearLayout l1 = (LinearLayout) findViewById(R.id.l1);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.l2);
        LinearLayout l3 = (LinearLayout) findViewById(R.id.l3);
        LinearLayout l4 = (LinearLayout) findViewById(R.id.l4);
        LinearLayout l5 = (LinearLayout) findViewById(R.id.l5);
        LinearLayout l6 = (LinearLayout) findViewById(R.id.l6);
        LinearLayout l7 = (LinearLayout) findViewById(R.id.l7);
        LinearLayout l8 = (LinearLayout) findViewById(R.id.l8);
        LinearLayout l9 = (LinearLayout) findViewById(R.id.l9);
        LinearLayout l10 = (LinearLayout) findViewById(R.id.l10);
        LinearLayout l11 = (LinearLayout) findViewById(R.id.l11);
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        l4.setOnClickListener(this);
        l5.setOnClickListener(this);
        l6.setOnClickListener(this);
        l7.setOnClickListener(this);
        l8.setOnClickListener(this);
        l9.setOnClickListener(this);
        l10.setOnClickListener(this);
        l11.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (User.shared().getStationId() == null) {
            toast("请选择站点！");
            return;
        }
        boolean checkAlert = true;
        Intent intent = null;
        switch (view.getId()) {
//            case R.id.layout_payment:
//                Intent toPayment = new Intent(this, PaymentActivity.class);
//                startActivity(toPayment);
//                break;
//            case R.id.layout_change_battery:
//                Intent toChangeBattery = new Intent(this, ExchangeBatteryActivity.class);
//                toChangeBattery.putExtra("clear", true);
//                startActivity(toChangeBattery);
//                break;
//            default:
//                break;
            case R.id.l1: // 排队换电
                intent = new Intent(getApplicationContext(), ExchangeOrdersActivity.class);
                break;
            case R.id.l2: // 换电录入
                intent = new Intent(getApplicationContext(), ExchangeBatteryActivity.class);
                intent.putExtra("clear", true);
                break;
            case R.id.l3: // 账号充值
                intent = new Intent(getApplicationContext(), PaymentActivity.class);
                break;
            case R.id.l4: // 充值记录
                intent = new Intent(getApplicationContext(), PayRecordActivity.class);
                checkAlert = false;
                break;
            case R.id.l5: // 换电记录
                intent = new Intent(getApplicationContext(), ExchangeRecordActivity.class);
                break;
            case R.id.l6: // 蓝色达到下载
                intent = new Intent(getApplicationContext(), AppDownloadCodeActivity.class);
                checkAlert = false;
                break;
            case R.id.l7: //故障维修
                intent = new Intent(getApplicationContext(), MalfunctionRepairActivity.class);
                intent.putExtra("station", GsonUtils.getInstance().toJson(stations));
                checkAlert = false;
                break;
            case R.id.l8: //故障记录
                intent = new Intent(getApplicationContext(), MalfunctionRecordActivity.class);
                intent.putExtra("station", GsonUtils.getInstance().toJson(stations));
                checkAlert = false;
                break;
            case R.id.l9: //电池驳运
                intent = new Intent(getApplicationContext(), BatteryLighterActivity.class);
                intent.putExtra("station", GsonUtils.getInstance().toJson(stations));
                checkAlert = false;
                break;
            case R.id.l10: //电池驳运查询
                intent = new Intent(getApplicationContext(), BatteryLighterInquireActivity.class);
                intent.putExtra("station", GsonUtils.getInstance().toJson(stations));
                checkAlert = false;
                break;
            case R.id.l11: //套餐活动
                intent = new Intent(getApplicationContext(), PackageActivity.class);
                checkAlert = false;
                break;
            default:
                break;
        }

        if (checkAlert && !showAlertSite) {
            final Intent mIntent = intent;
            alert(null, "请确认当前选择的站点：" + User.shared().getStation(), "选错了", "站点正确", new SGOnAlertClick() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    if (index > 0) {
                        showAlertSite = true;
                        startActivity(mIntent);
                    }
                }
            });
        } else {
            startActivity(intent);
        }
    }


    private void settingSpinnerStations() {

        spStations = (Spinner) findViewById(R.id.sp_station);
        ArrayAdapter adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, new ArrayList<String>());
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spStations.setAdapter(adapter);
        //为下拉列表设置各种事件的响应，这个事响应菜单被选中

        spStations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                showAlertSite = false;
                ExchangeSite site = stations.get(position);
                User.shared().setStationId(site.getId());
                User.shared().setStation(site.getName());
                User.shared().update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        requestSites(new SGFinishCallBack() {
            @Override
            public void onFinish(Boolean result) {
                if (result) {
                    chooseStation();
                }
            }
        });
    }

    private void chooseStation() {
        if (spStations == null) return;
        boolean locatedSuccess = mLocation != null;
        final List<String> stationNames = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            ExchangeSite site = stations.get(i);
            stationNames.add(site.getName());
        }
        ArrayAdapter adapter = (ArrayAdapter) spStations.getAdapter();
        adapter.clear();
        adapter.addAll(stationNames);

        //读取记住的 location
//                    SharedPreferences preferences = SharedPreferencesUtil.getSharedPreferences(mContext);
//                    String station = preferences.getString(BatteryStorageStationKey,"");
//                    String station = User.shared().getStation();
        boolean useOther = false;
        if (!locatedSuccess) {
            if (User.shared().getStationId() != null) {
                for (int i = 0; i < stations.size(); i++) {
                    ExchangeSite site = stations.get(i);
                    if (site.getId() == User.shared().getStationId()) {
                        spStations.setSelection(i);
                        useOther = true;
                        break;
                    }
                }
            }

        }
        if (!useOther) {
            if (stations.size() > 0) {
                spStations.setSelection(0);
                ExchangeSite site = stations.get(0);
                User.shared().setStationId(site.getId());
                User.shared().setStation(site.getName());
                User.shared().update();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onButtonLeftPressed() {
        doLogout();
    }

    private void doLogout() {
        alert("确认退出吗？", new SGOnAlertClick() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                dialog.dismiss();
                if (index > 0) logout();
            }
        });
    }

    private void logout() {
        showAlertSite = false;
        User.shared().setPassword("");
        User.shared().update();
        toLoginActivity();
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void getLastKnownLocation() {

//        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        if (PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            toast("没有获取位置权限，请在 设置 中开启权限。");
            return;
        }
        if (mLocationManager == null)
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getAllProviders();
        if (providers.size() <= 0) {
            toast("没有可用的位置提供器");
            return;
        }
        String provider = "gps";
        if (!providers.contains(provider)) {
            provider = "network";
            if (!providers.contains(provider)) {
                provider = providers.get(0);
            }
        }
        updateLocation = true;
        mLocationManager.requestLocationUpdates(provider, 30000, 10, locationListener);


    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
            Log.i("", "status" + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("", "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("", "onProviderDisabled: " + provider + ".." + Thread.currentThread().getName());
            mLocation = null;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("", "onLocationChanged: " + ".." + Thread.currentThread().getName());
            if (updateLocation) {
                updateLocation = false;
                mLocation = location;
                sortStations();
                chooseStation();
            } else {
                mLocation = null;
            }
        }
    };

    private void sortStations() {

        if (mLocation != null) {
            if (mLocationManager != null) mLocationManager.removeUpdates(locationListener);
//            //如果位置发生变化,重新显示
//            showLocation(location);
            //获取维度信息
            double latitude = mLocation.getLatitude();
            //获取经度信息
            double longitude = mLocation.getLongitude();
            mLocation = null;
            for (int i = 0; i < stations.size(); i++) {
                ExchangeSite site = stations.get(i);
                site.setDistance(getDistance(longitude, latitude, site.getLongitude(), site.getLatitude()));
            }
            Collections.sort(stations, new Comparator<ExchangeSite>() {
                @Override
                public int compare(ExchangeSite o1, ExchangeSite o2) {
                    if (o1.getDistance() > o2.getDistance()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        } else {
            Log.i("", "location == null");
        }

    }

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 获取所有站点
     */
    private void requestSites(final SGFinishCallBack finishCallBack) {
        final long time = System.currentTimeMillis();
        Log.d("btrq", time + "返回时间");
        SGHTTPManager.POST(API.URL_STATIONS, null, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess) {
                    Log.d("btrq", System.currentTimeMillis() - time + "返回时间2");
                    stations = fetchModel.listModelOfContent(ExchangeSite.class);
//                    sortStations();
                    finishCallBack.onFinish(true);
                } else {
                    finishCallBack.onFinish(false);
                }
            }
        });
    }

    /**
     * opencv初始化需要加载
     */
    private void initFile() {
        String sdcarddir = Environment.getExternalStorageDirectory() + File.separator + "pr";
        MRAssetUtil.CopyAssets(this, MRCarUtil.ApplicationDir, sdcarddir);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("btkj", "OpenCV loaded successfully");
                    System.loadLibrary("hyperlpr");
                    initReco();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loadOpenCV();
    }

    private void loadOpenCV() {
        if (!OpenCVLoader.initDebug()) {
            Log.d("btkj", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d("btkj", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CARID_REQUESTCODE:
                initFile();
                loadOpenCV();
                break;
            default:
                break;
        }
    }

    private void initReco() {
        recognition = new PlateRecognition();
    }
}
