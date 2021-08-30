package com.kulun.energynet.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kulun.energynet.R;
import com.kulun.energynet.bill.BillActivity;
import com.kulun.energynet.customizeView.PromotionCarSelect;
//import com.kulun.energynet.customizeView.ShakeListener;
import com.kulun.energynet.customizeView.StationAllPopup;
import com.kulun.energynet.customizeView.StationPopup;
import com.kulun.energynet.customizeView.TimeFinishInterface;
import com.kulun.energynet.databinding.ActivityMainBinding;
import com.kulun.energynet.login.PrivacyActivity;
import com.kulun.energynet.login.UseProtocolActivity;
import com.kulun.energynet.mine.ExchangeActivity;
import com.kulun.energynet.mine.MapCustomActivity;
import com.kulun.energynet.mine.MessageActivity;
import com.kulun.energynet.mine.MineActivity;
import com.kulun.energynet.mine.MyActivityListActivity;
import com.kulun.energynet.mine.MyCarActivity;
import com.kulun.energynet.mine.RechargeActivity;
import com.kulun.energynet.mine.ScanActivity;
import com.kulun.energynet.model.Appload;
import com.kulun.energynet.model.BillDetail;
import com.kulun.energynet.model.City;
import com.kulun.energynet.model.Message;
import com.kulun.energynet.model.StationAll;
import com.kulun.energynet.model.StationInfo;
import com.kulun.energynet.model.UseBind;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.popup.ScanPopup;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.requestparams.ResponseCode;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.BaseDialog;
import com.kulun.energynet.utils.DateUtils;
import com.kulun.energynet.utils.GpsUtil;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.ListUtils;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import com.superluo.textbannerlibrary.ITextBannerItemClickListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;

import static com.yanzhenjie.permission.runtime.Permission.ACCESS_FINE_LOCATION;
import static com.yanzhenjie.permission.runtime.Permission.CAMERA;
import static com.yanzhenjie.permission.runtime.Permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity implements View.OnClickListener, PromotionCarSelect, TimeFinishInterface {
    PrivacyDialog privacyDialog;
    private ActivityMainBinding binding;
    private UseBind useBind;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private AMap aMap;
    private List<StationInfo> stationlist = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private final int REQUESTCODE_CITY = 1002, REQUESTCODE_SCAN = 1003, REQUESTCODE_LIST = 1004, REQUESTCODE_GPS = 1010;
    private TimeCount timeCount;
    private StationSelectFragment stationSelectFragment;
    private PromptDialog promptDialog;
    private GeocodeSearch geocodeSearch;
    private UpdateManager updateManager;
    private static BitmapDescriptor bitmap = null;
    private StationPopup stationPopup;
    private StationAllPopup stationAllPopup;
    private String appointment_no;
    private ScanPopup scanPopup;
    private int cityid;
    private List<StationAll> stationAllList = new ArrayList<>();
    //    private SensorManager sensorManager;
//    private Sensor mSensor;
//    private Vibrator vibrator;
//    private ShakeListener shakeListener;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {//
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    if (TextUtils.isEmpty(amapLocation.getCity())) {
                        //有些机型上地位获取到的城市等信息为空，需要根据经纬度查找城市
                        getAddressByLatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    } else {
                        if (amapLocation.getLatitude() != 0 && amapLocation.getLongitude() != 0) {
                            locateInit(amapLocation.getLatitude(), amapLocation.getLongitude());
                        }
                        if (!amapLocation.getCity().isEmpty()) {
                            UserLogin.getInstance().setCityName(amapLocation.getCity());
                            SharePref.put(MainActivity.this, API.cityName, amapLocation.getCity());
                            binding.tvCity.setText(amapLocation.getCity());
                        }
                        Utils.log(null, "", amapLocation.getCity() + "定位地址" + amapLocation.getCityCode() + amapLocation.getErrorCode() + amapLocation.getErrorInfo() + amapLocation.getLatitude() + amapLocation.getLongitude());
                        refreshMap(UserLogin.getInstance().getLatitude(MainActivity.this), UserLogin.getInstance().getLongtitude(MainActivity.this));
                        loadCity(UserLogin.getInstance().getLatitude(MainActivity.this), UserLogin.getInstance().getLongtitude(MainActivity.this), binding.tvCity.getText().toString());
                    }
                } else {
                    UserLogin.getInstance().setCityName("");
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.d(Utils.TAG, "location Error, ErdrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void loadCity(double latitude, double longtitude, String city) {
        new MyRequest().myRequest(API.CITYLIST, false, null, false, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                List<City> cityList = GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<City>>() {
                }.getType());
                int position = Utils.getCityPosition(cityList, city);
                if (position != -1) {
                    cityid = cityList.get(position).getId();
                    UserLogin.getInstance().setCityid(cityid);
                    SharePref.put(MainActivity.this, API.cityId, cityid + "");
                    loadStation(longtitude, latitude, "-1", cityid);
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                appload();
            }
        }, 1500);
        promptDialog = new PromptDialog(this);
        binding.mapview.onCreate(savedInstanceState);
        aMap = binding.mapview.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMarkerClickListener(markerClickListener);
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                float a = cameraPosition.zoom;
                Utils.log("", "", a + "");
                if (a <= 7 && stationAllList.size() == 0) {
                    loadAllstation();
                }
            }
        });
        mLocationClient = new AMapLocationClient(getApplicationContext());
        init();
        if (!(boolean) SharePref.oneget(this, "agree", false)) {
            if (privacyDialog == null) {
                privacyDialog = new PrivacyDialog(MainActivity.this, R.style.mydialog);
                privacyDialog.show();
            }
        }
        binding.finish.setText("一键预约");
        binding.finish.setOnClickListener(v -> {
            if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                Utils.toLogin(MainActivity.this);
                Utils.snackbar(this, "请先登陆");
                return;
            }
            if (stationlist.size() == 0) {
                Utils.snackbar(MainActivity.this, "请等待站点刷新");
                return;
            }
            if (Utils.usebindisNotexist(useBind)) {
                Utils.snackbar(MainActivity.this, "请选择当前车辆");
                return;
            }
            HashMap<String, String> map = new HashMap<>();
            if (UserLogin.getInstance().getMylatitude(MainActivity.this) == 0) {//当前没有定位到
                map.put("longitude", String.valueOf(UserLogin.getInstance().getLongtitude(MainActivity.this)));
                map.put("latitude", String.valueOf(UserLogin.getInstance().getLatitude(MainActivity.this)));
            } else {
                map.put("longitude", String.valueOf(UserLogin.getInstance().getMylongtitude(MainActivity.this)));
                map.put("latitude", String.valueOf(UserLogin.getInstance().getMylatitude(MainActivity.this)));
            }
//            map.put("type", "-1");
            map.put("battery_cnt", String.valueOf(useBind.getBattery_count()));
            map.put("city_id", String.valueOf(UserLogin.getInstance().getCityid(MainActivity.this)));
            new MyRequest().myRequest(API.recomand, true, map, false, this, null, null, new Response() {
                @Override
                public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                    StationInfo stationInfo = GsonUtils.getInstance().fromJson(json, StationInfo.class);
                    showPopDialog(stationInfo);
                    if (stationPopup != null){
                        stationPopup.setStation(stationInfo);
                    }
                }
            });
        });
        binding.image2.setOnClickListener(v -> binding.message.setVisibility(View.GONE));
        binding.banner.setItemOnClickListener(new ITextBannerItemClickListener() {
            @Override
            public void onItemClick(String data, int position) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });
        stationPopup = new StationPopup(this);
        stationAllPopup = new StationAllPopup(this);
    }

    private void loadAllstation() {//加载全部站点 /api/site/all?exclude=320200   全国站点
        String json = "exclude=" + cityid;
        new MyRequest().spliceJson(API.siteall, false, json, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (jsonArray != null) {
                    stationAllList.addAll(GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<StationAll>>() {
                    }.getType()));
                    markerAllCreate(stationAllList, null);
                }
            }
        });
    }

    //app加载信息
    private void appload() {
        new MyRequest().myRequest(API.appload, false, null, false, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                List<Appload> list = GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Appload>>() {
                }.getType());
                String workTime = "", workDay = "", url = "", latestversion = "";
                boolean isforce = false;
                for (int i = 0; i < list.size(); i++) {
                    Appload appload = list.get(i);
                    if (appload.getName().equals("serviceLine") && !appload.getRemark().equals("")) {
                        String phone = appload.getRemark();
                        UserLogin.getInstance().setCustphone(phone);
                        SharePref.put(MainActivity.this, API.customphone, phone);
                    }
                    if (appload.getName().equals("workDay") && !appload.getRemark().equals("")) {
                        workDay = appload.getRemark();
                    }
                    if (appload.getName().equals("workTime") && !appload.getRemark().equals("")) {
                        workTime = appload.getRemark();
                    }
                    if (appload.getName().equals("androidLink") && !appload.getRemark().equals("")) {
                        url = appload.getRemark();
                    }
                    if (appload.getName().equals("forceUpdateAndroid") && !appload.getRemark().equals("")) {//强制是1  不强制是0
                        isforce = appload.getRemark().equals("1") ? true : false;
                    }
                    if (appload.getName().equals("latestVersionAndroid") && !appload.getRemark().equals("")) {
                        latestversion = appload.getRemark();
                    }
                }
                if (!url.equals("") && !latestversion.equals("")) {
                    updateManager = new UpdateManager(MainActivity.this, latestversion, url);
                    updateManager.checkUpdate(isforce, "有新版本APP需要更新");
                }
                if (!"".equals(workTime) && !"".equals(workDay)) {
                    UserLogin.getInstance().setCustinfo(workTime + "\n" + workDay);
                    SharePref.put(MainActivity.this, API.custominfo, workTime + "\n" + workDay);
                }
            }
        });
    }

    private void getAddressByLatLng(double latitude, double longitude) {
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
                String city = regeocodeResult.getRegeocodeAddress().getCity();
                if (longitude != 0 && latitude != 0) {
                    locateInit(latitude, longitude);
                }
                if (!city.isEmpty()) {
                    UserLogin.getInstance().setCityName(city);
                    binding.tvCity.setText(city);
                    SharePref.put(MainActivity.this, API.cityName, city);
                }
                refreshMap(UserLogin.getInstance().getLatitude(MainActivity.this), UserLogin.getInstance().getLongtitude(MainActivity.this));
                loadCity(UserLogin.getInstance().getLatitude(MainActivity.this), UserLogin.getInstance().getLongtitude(MainActivity.this), binding.tvCity.getText().toString());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {
            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    private void locateInit(double latitude, double longitude) {
        UserLogin.getInstance().setLatitude(latitude);
        UserLogin.getInstance().setMylatitude(latitude);
        UserLogin.getInstance().setLongtitude(longitude);
        UserLogin.getInstance().setMylongtitude(longitude);
        SharePref.put(MainActivity.this, API.latitude, latitude + "");
        SharePref.put(MainActivity.this, API.mylatitude, latitude + "");
        SharePref.put(MainActivity.this, API.longtitude, longitude + "");
        SharePref.put(MainActivity.this, API.mylongtitude, longitude + "");
    }

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.getObject() instanceof StationInfo) {//当前城市的站点
                markerCreate(stationlist, (StationInfo) marker.getObject());
                showPopDialog((StationInfo) marker.getObject());
                if (stationPopup != null){
                    stationPopup.setStation((StationInfo) marker.getObject());
                }
            } else {//除了当前城市所有站点
                markerAllCreate(stationAllList, (StationAll) marker.getObject());
                showAllPopDialog((StationAll) marker.getObject());
            }
            return true;
        }
    };

    private void showPopDialog(StationInfo contentBean) {
        stationPopup.name.setText(contentBean.getName());
        stationPopup.address.setText(contentBean.getAddress());
        stationPopup.phone.setText(contentBean.getPhone());
        stationPopup.phone.setOnClickListener(v -> {
            if (stationPopup != null) {
                stationPopup.dismiss();
            }
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + contentBean.getPhone());
            intent.setData(data);
            startActivity(intent);
        });
        stationPopup.worktime.setText(DateUtils.datetoTime(contentBean.getStart_time()) + "~" + DateUtils.datetoTime(contentBean.getEnd_time()));
        if (contentBean.getType() == 0) {
            stationPopup.paidui.setText("排队人数：" + contentBean.getWaiting());
            stationPopup.kucun.setVisibility(View.VISIBLE);
        } else if (contentBean.getType() == 1) {
            stationPopup.paidui.setText("可用电池：" + contentBean.getBattery());
            stationPopup.kucun.setVisibility(View.INVISIBLE);
        }
        stationPopup.kucun.setText(contentBean.getBattery() > 0 ? "电池库存:有" : "电池库存:无");
        int mipmap = 0, lefticon = 0;
        boolean worktime = false;
        String startTime = DateUtils.timeTotime(contentBean.getStart_time());
        String endTime = DateUtils.timeTotime(contentBean.getEnd_time());
        worktime = DateUtils.isBelong(startTime, endTime);
        if (contentBean.getStatus() == 2) {
            mipmap = R.mipmap.sign_repair;
            if (contentBean.getType() == 0) {
                lefticon = R.mipmap.station_repair;
            } else {
                lefticon = R.mipmap.bao_nopower;
            }
        } else {
            if (contentBean.getBattery() > 0 && worktime) {
                if (contentBean.getType() == 0) {//0是换电站
                    mipmap = R.mipmap.sign_working;
                    lefticon = R.mipmap.station_icon;
                } else {
                    if (contentBean.isAppointment()) {
                        mipmap = R.mipmap.sign_reserve;
                    } else {
                        mipmap = R.mipmap.sign_working;
                    }
                    lefticon = R.mipmap.bao_working;
                }
            } else if (contentBean.getBattery() > 0 && !worktime) {//休息中
                mipmap = R.mipmap.sign_rest;
                if (contentBean.getType() == 0) {//0是换电站
                    lefticon = R.mipmap.station_rest;
                } else {
                    lefticon = R.mipmap.bao_rest;
                }
            } else if (worktime && contentBean.getBattery() == 0) {//无电池
                if (contentBean.getType() == 0) {//0是换电站
                    lefticon = R.mipmap.station_icon;
                    mipmap = R.mipmap.sign_working;
                } else {
                    lefticon = R.mipmap.bao_nopower;
                    mipmap = R.mipmap.sign_nopower;
                }
            } else {
                if (contentBean.getType() == 0) {//0是换电站
                    lefticon = R.mipmap.station_icon;
                    mipmap = R.mipmap.sign_working;
                } else {
                    lefticon = R.mipmap.bao_nopower;
                    mipmap = R.mipmap.sign_nopower;
                }
            }
        }
        Drawable rightDrawable = getResources().getDrawable(mipmap);
        Drawable leftDrawable = getResources().getDrawable(lefticon);
        rightDrawable.setBounds(0, 0, 100, 50);
        leftDrawable.setBounds(0, 0, 80, 80);
        stationPopup.name.setCompoundDrawables(leftDrawable, null, rightDrawable, null);
        Drawable distancedrawable = getResources().getDrawable(R.mipmap.station_record_guide);
        distancedrawable.setBounds(0, 0, 60, 60);
        stationPopup.distance.setCompoundDrawables(null, distancedrawable, null, null);
        if (contentBean.getStatus() == 2){//2是维修中状态
            stationPopup.appoint.setVisibility(View.GONE);
            stationPopup.redelayDismiss.setVisibility(View.GONE);
            stationPopup.time.setVisibility(View.GONE);
        }else {
            if (UserLogin.getInstance().getYuyueId() != -1) {//预约状态
                if (UserLogin.getInstance().getYuyueId() == contentBean.getId()) {//预约状态并且是当前得站点
                    stationPopup.appoint.setVisibility(View.GONE);
                    stationPopup.redelayDismiss.setVisibility(View.VISIBLE);
                    stationPopup.time.setVisibility(View.VISIBLE);
                } else {
                    if (contentBean.isAppointment()) {
                        if (worktime && contentBean.getBattery() > 0) {//在运营时间内 并且电池数量大于0
                            stationPopup.appoint.setVisibility(View.VISIBLE);
                            stationPopup.redelayDismiss.setVisibility(View.GONE);
                            stationPopup.time.setVisibility(View.GONE);
                        } else {
                            stationPopup.appoint.setVisibility(View.GONE);
                            stationPopup.redelayDismiss.setVisibility(View.GONE);
                            stationPopup.time.setVisibility(View.GONE);
                        }
                    } else {
                        stationPopup.appoint.setVisibility(View.GONE);
                        stationPopup.redelayDismiss.setVisibility(View.GONE);
                        stationPopup.time.setVisibility(View.GONE);
                    }
                }
            } else {//不是预约状态
                if (contentBean.isAppointment()) {//可以预约
                    if (worktime && contentBean.getBattery() > 0) {//在运营时间内 并且电池数量大于0
                        stationPopup.appoint.setVisibility(View.VISIBLE);
                        stationPopup.redelayDismiss.setVisibility(View.GONE);
                        stationPopup.time.setVisibility(View.GONE);
                    } else {
                        stationPopup.appoint.setVisibility(View.GONE);
                        stationPopup.redelayDismiss.setVisibility(View.GONE);
                        stationPopup.time.setVisibility(View.GONE);
                    }
                } else {
                    stationPopup.appoint.setVisibility(View.GONE);
                    stationPopup.redelayDismiss.setVisibility(View.GONE);
                    stationPopup.time.setVisibility(View.GONE);
                }
            }
        }
        stationPopup.appoint.setOnClickListener(v -> {
            if (Utils.usebindisNotexist(useBind)) {
                Utils.snackbar(MainActivity.this, "请先选择您的车辆，才能预约站点");
                return;
            }
            appoint(contentBean.getId(), useBind.getCar_id());
        });
        stationPopup.delayappointcancel.setOnClickListener(v -> {
            if (Utils.usebindisNotexist(useBind)) {
                Utils.snackbar(MainActivity.this, "请在选择您的车辆，才能取消预约站点");
                return;
            }
            cancelAppoint();
        });
        stationPopup.delayappoint.setOnClickListener(v -> {
            if (Utils.usebindisNotexist(useBind)) {
                Utils.snackbar(MainActivity.this, "请在选择您的车辆，才能延时预约站点");
                return;
            }
            delayappoint();
        });
//        if (UserLogin.getInstance().getAccount() != null) {
        float distance = AMapUtils.calculateLineDistance(new LatLng(UserLogin.getInstance().getMylatitude(this), UserLogin.getInstance().getMylongtitude(this)), new LatLng(contentBean.getLatitude(), contentBean.getLongitude()));
        DecimalFormat df = new DecimalFormat("#.00");
        if (distance < 1000) {
            stationPopup.distance.setText("导航\n" + df.format(distance) + "m");
        } else {
            stationPopup.distance.setText("导航\n" + df.format(distance / 1000) + "km");
        }
        stationPopup.distance.setOnClickListener(view -> {
            if (UserLogin.getInstance().getYuyueId() == -1 && contentBean.getType()==1) {
                Utils.snackbar(MainActivity.this, "请预约成功后前往换电");
            } else {
                Poi start = new Poi("", new com.amap.api.maps.model.LatLng(UserLogin.getInstance().getMylatitude(this), UserLogin.getInstance().getMylongtitude(this)), "");
                Poi end = new Poi("", new com.amap.api.maps.model.LatLng(contentBean.getLatitude(), contentBean.getLongitude()), "");
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), null, MapCustomActivity.class);
                if (stationPopup != null) {
                    stationPopup.dismiss();
                }
            }
        });
        stationPopup.showPopupWindow();
    }

    private void showAllPopDialog(StationAll contentBean) {
        stationAllPopup.name.setText(contentBean.getName());
        stationAllPopup.address.setText(contentBean.getAddress());
        stationAllPopup.phone.setText(contentBean.getPhone());
        stationAllPopup.phone.setOnClickListener(v -> {
            if (stationAllPopup != null) {
                stationAllPopup.dismiss();
            }
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + contentBean.getPhone());
            intent.setData(data);
            startActivity(intent);
        });
        stationAllPopup.worktime.setText(DateUtils.datetoTime(contentBean.getStart_time()) + "~" + DateUtils.datetoTime(contentBean.getEnd_time()));
        int mipmap = 0;
        if (contentBean.getType() == 0){//换电站
            mipmap = R.mipmap.station_icon;
        }else {
            mipmap = R.mipmap.bao_working;
        }
        Drawable leftDrawable = getResources().getDrawable(mipmap);
        leftDrawable.setBounds(0, 0, 80, 80);
        stationAllPopup.name.setCompoundDrawables(leftDrawable, null, null, null);
        stationAllPopup.showPopupWindow();
    }

    private void delayappoint() {//    "appointment_no": "AP2949782536458240"
        HashMap<String, String> map = new HashMap<>();
        map.put("appointment_no", appointment_no);
        new MyRequest().myRequest(API.delayappoint, true, map, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(MainActivity.this, "延时预约成功");
                getcurrentAppint();
            }
        });
    }

    private void cancelAppoint() {
        //{
        //    "appointment_no": "AP2948865170870272"
        //}
        HashMap<String, String> map = new HashMap<>();
        map.put("appointment_no", appointment_no);
        new MyRequest().myRequest(API.cancelappoint, true, map, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(MainActivity.this, "取消预约成功");
                getcurrentAppint();
            }
        });
    }

    private void appoint(int station, int carid) {//预约
        //{
        //    "site_id": 99,
        //    "car_id": 11391
        //}
        HashMap<String, String> map = new HashMap<>();
        map.put("site_id", String.valueOf(station));
        map.put("car_id", String.valueOf(carid));
        new MyRequest().myRequest(API.appoint, true, map, false, MainActivity.this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(MainActivity.this, "预约成功");
                getcurrentAppint();
            }
        });
    }

    private void loadStation(double longitude, double latitude, String type, int cityId) {
        //{站点类型type-1全部 0换电站 1换电宝站点列表默认搜索0.5度约50公理范围
        //    "type": 0,
        //    "latitude": 30.298013,
        //    "longitude": 120.148848
        //}
        HashMap<String, String> map = new HashMap<>();
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        map.put("type", type);
        map.put("city_Id", String.valueOf(cityId));
        new MyRequest().myRequest(API.SITELIST, true, map, false, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                stationlist = GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<StationInfo>>() {
                }.getType());
                boolean ishasHuandianbao = false;
                for (int i = 0; i < stationlist.size(); i++) {
                    if (stationlist.get(i).getType() == 1) {//0是换电站
                        ishasHuandianbao = true;
                    }
                }
                if (ishasHuandianbao) {
                    binding.finish.setVisibility(View.VISIBLE);
                } else {
                    binding.finish.setVisibility(View.GONE);
                }
                markerCreate(stationlist, null);
                getcurrentAppint();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.msg:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(MainActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.img_left:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(MainActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                intent = new Intent(MainActivity.this, MineActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_right:
//                if (UserLogin.getInstance().getCityid(MainActivity.this) == 0){
//                    Utils.snackbar(MainActivity.this, "请先等待定位");
//                    return;
//                }
                intent = new Intent(MainActivity.this, StationListActivity.class);
                intent.putExtra("useBind", useBind);
                startActivityForResult(intent, REQUESTCODE_LIST);
                break;
            case R.id.tv_city:
                intent = new Intent(MainActivity.this, ChoseCityActivity.class);
                startActivityForResult(intent, REQUESTCODE_CITY);
                break;
            case R.id.re_carplate:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(MainActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                intent = new Intent(MainActivity.this, MyCarActivity.class);
                startActivity(intent);
                break;
            case R.id.qrcode:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(MainActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                String carplate = "";
                if (Utils.usebindisNotexist(useBind)) {
                    carplate = "";
                } else {
                    carplate = useBind.getPlate_number();
                }
                BaseDialog.showQrDialog(MainActivity.this, carplate);
                break;
            case R.id.recharge:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(MainActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                intent = new Intent(MainActivity.this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.daka:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(MainActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                if (Utils.usebindisNotexist(useBind)) {
                    Utils.snackbar(MainActivity.this, "请先选择车辆");
                    return;
                }
                intent = new Intent(MainActivity.this, ExchangeActivity.class);
                startActivity(intent);
                break;
            case R.id.scan:
                if (Utils.getToken(this) == null || Utils.getToken(this).equals("")) {
                    Utils.toLogin(MainActivity.this);
                    Utils.snackbar(this, "请先登陆");
                    return;
                }
                if (Utils.usebindisNotexist(useBind)) {
                    Utils.snackbar(MainActivity.this, "请先选择车辆");
                    return;
                }
//                AndPermission.with(this)
//                        .runtime()
//                        .permission(Permission.CAMERA)
//                        .onGranted(permissions -> {
//                            // Storage permission are allowed.
////                            Intent myintent = new Intent(MainActivity.this, ScanActivity.class);
////                            startActivityForResult(myintent, REQUESTCODE_SCAN);
//                        })
//                        .onDenied(permissions -> {
//                            // Storage permission are not allowed.
//                        })
//                        .start();
                Intent myintent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(myintent, REQUESTCODE_SCAN);
                break;
            case R.id.img_kefu:
                Utils.loadkefu(MainActivity.this, binding.imgKefu);
                break;
            case R.id.tv_station_name:
                if (stationlist != null) {
                    stationSelectFragment = new StationSelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(API.station, (Serializable) stationlist);
                    bundle.putBoolean(API.ismain, true);
                    stationSelectFragment.setArguments(bundle);
                    stationSelectFragment.show(getSupportFragmentManager(), "data");
                }
                break;
            default:
                break;
        }
    }

    public void installApk() {
        if (updateManager != null) {
            updateManager.installApk();
        }
    }

    public void setProgress() {
        if (updateManager != null) {
            updateManager.setProgress();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_CITY && resultCode == 1) {//选择城市
            String cityName = data.getStringExtra(API.cityName);
            binding.tvCity.setText(cityName);
            UserLogin.getInstance().setCityName(cityName);
            SharePref.put(MainActivity.this, API.cityName, cityName);
            double lat = data.getDoubleExtra(API.latitude, 0);
            UserLogin.getInstance().setLatitude(lat);
            SharePref.put(MainActivity.this, API.latitude, lat + "");
            double lon = data.getDoubleExtra(API.longtitude, 0);
            UserLogin.getInstance().setLongtitude(lon);
            SharePref.put(MainActivity.this, API.longtitude, lon + "");
            cityid = data.getIntExtra(API.cityId, 0);
            UserLogin.getInstance().setCityid(cityid);
            SharePref.put(MainActivity.this, API.cityId, cityid + "");
            refreshLocation(lat, lon);
        } else if (requestCode == REQUESTCODE_SCAN && resultCode == 2) {//换点扫码哦
            String message = data.getStringExtra(API.code);
            if (message.indexOf("=") == -1) {
                Utils.snackbar(MainActivity.this, "请确认当前二维码是站点二维码");
                return;
            }
            if (message.indexOf("station") != -1) {
                String[] splite = message.split("=");
                postScan(splite[1]);
            }
        } else if (requestCode == REQUESTCODE_LIST && resultCode == 2) {//列表
            if (UserLogin.getInstance().getCityName(MainActivity.this) != null && !UserLogin.getInstance().getCityName(MainActivity.this).isEmpty()) {
                binding.tvCity.setText(UserLogin.getInstance().getCityName(MainActivity.this));
            }
            if (UserLogin.getInstance().getLatitude(MainActivity.this) != 0) {
                refreshLocation(UserLogin.getInstance().getLatitude(MainActivity.this), UserLogin.getInstance().getLongtitude(MainActivity.this));
            }
//            if (UserLogin.getInstance().getCityid(MainActivity.this) != 0) {
//                loadStation(UserLogin.getInstance().getLongtitude(MainActivity.this), UserLogin.getInstance().getLatitude(MainActivity.this), "-1", UserLogin.getInstance().getCityid(MainActivity.this));
//            }
            int stationId = data.getIntExtra("stationId", -1);
            if (stationId != -1 && UserLogin.getInstance().getYuyueId() == -1) {
                if (stationlist.size() > 0) {
                    showPopDialog(Utils.getstation(stationlist, stationId));
                    if (stationPopup !=null){
                        stationPopup.setStation(Utils.getstation(stationlist, stationId));
                    }
                }
            }
        } else if (requestCode == REQUESTCODE_GPS) {
            setLocate();
        }
    }

    //地图显示刷新的位置
    private void refreshLocation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    private void postScan(String data) {
        int stationId = 0;
        if (UserLogin.getInstance().getYuyueId() == -1) {
            stationId = Integer.parseInt((String) SharePref.get(this, API.siteid, "-1"));
        } else {
            stationId = UserLogin.getInstance().getYuyueId();
        }
        if (stationId == -1) {
            Utils.snackbar(this, "当前没有预约，请先预约站点");
            return;
        }
        if (!data.equals(String.valueOf(stationId))) {
            Utils.snackbar(MainActivity.this, "当前预约站点不是扫码站点");
            return;
        }
        BaseDialog.showDialog(MainActivity.this, "您已扫码成功，请确认是否换电", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("appointmentNo", UserLogin.getInstance().getAppointment_no(MainActivity.this));
                new MyRequest().myRequest(API.scanpay, true, map, true, MainActivity.this, null, null, new Response() {
                    @Override
                    public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                        List<BillDetail> list = GsonUtils.getInstance().fromJson(json.get("detail").getAsJsonArray(), new TypeToken<List<BillDetail>>() {
                        }.getType());
                        String name = json.get("siteName").getAsString();
                        String fare = json.get("realFare").getAsString();
                        int recordId = json.get("recordId").getAsInt();
                        scanPopup = new ScanPopup(MainActivity.this, MainActivity.this, list, name, fare, UserLogin.getInstance().getAppointment_no(MainActivity.this),
                                recordId);
                        scanPopup.showPopupWindow();
                    }
                });
            }
        });
    }

    public class PrivacyDialog extends Dialog {
        Context context;

        public PrivacyDialog(Context context, int themeResId) {
            super(context, themeResId);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_privacy);
            TextView xieyiTv = findViewById(R.id.tv_xieyi);
            TextView privacyTv = findViewById(R.id.tv_privacy);
            TextView cancelTv = findViewById(R.id.tv_cancel);
            TextView agreeTv = findViewById(R.id.tv_agree);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            agreeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    SharePref.oneput(context, "agree", true);
                }
            });
            xieyiTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UseProtocolActivity.class);
                    context.startActivity(intent);
                }
            });
            privacyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PrivacyActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }


    private void init() {
        bindClick();
        AndPermission.with(this)
                .runtime()
                .permission(WRITE_EXTERNAL_STORAGE, Permission.READ_PHONE_STATE, ACCESS_FINE_LOCATION, CAMERA)
                .onGranted(permissions -> {
                    setLocate();
                    bluePointLocat(); //定位蓝点
//                    loadStation(UserLogin.getInstance().getLongtitude(MainActivity.this),UserLogin.getInstance().getLatitude(MainActivity.this),
//                            "-1");
//                    loadServiceData();
                })
                .onDenied(permissions -> {
                })
                .start();
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    //获取当前预约//{
    //                    "appointment_no": "AP2946390799945728",
    //                            "end_time": "2020-12-25T14:19:52+08:00",
    //                            "site_id": 99
    private void getcurrentAppint() {
        new MyRequest().myRequest(API.getmyappponit, false, null, false, MainActivity.this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (json != null) {
                    if (json.has("appointment_no")) {
                        appointment_no = json.get("appointment_no").getAsString();
                        UserLogin.getInstance().setAppointment_no(appointment_no);
                        SharePref.put(MainActivity.this, API.appointment_no, appointment_no);
                    }
                    if (json.has("end_time")) {
                        long endTime = DateUtils.date2TimeStamp(json.get("end_time").getAsString());
                        long currentTime = System.currentTimeMillis() / 1000;
                        long time = endTime - currentTime;
                        if (time > 0) {
                            if (stationPopup != null) {
                                stationPopup.appoint.setVisibility(View.GONE);
                                stationPopup.redelayDismiss.setVisibility(View.VISIBLE);
                                stationPopup.time.setVisibility(View.VISIBLE);
                            }
                            if (timeCount != null) {
                                timeCount.cancel();
                                timeCount = new TimeCount(time * 1000, stationPopup.time, MainActivity.this::timeFinish);
                                timeCount.start();
                            } else {
                                timeCount = new TimeCount(time * 1000, stationPopup.time, MainActivity.this::timeFinish);
                                timeCount.start();
                            }
                        }
                        Utils.log("", "", endTime + "分割线" + currentTime);
                    }
                    if (json.has("site_id")) {
                        int position = json.get("site_id").getAsInt();
                        UserLogin.getInstance().setYuyueId(position);
                        SharePref.put(MainActivity.this, API.siteid, position + "");
                        StationInfo stationInfo = Utils.getstation(stationlist, position);
                        if (stationlist.size() != 0 && stationInfo != null) {
                            markerCreate(stationlist, stationInfo);
                            showPopDialog(stationInfo);//获取我的预约
                            if (stationPopup != null){
                                stationPopup.setStation(stationInfo);
                            }
                        }
                    }
                } else {
                    UserLogin.getInstance().setAppointment_no("");
                    UserLogin.getInstance().setYuyueId(-1);
                    SharePref.put(MainActivity.this, API.appointment_no, "");
                    SharePref.put(MainActivity.this, API.siteid, "-1");
                    if (stationPopup != null) {
                        if (stationPopup.getStationInfo()!=null){
                            StationInfo stationInfo = stationPopup.getStationInfo();
                            boolean worktime = false;
                            String startTime = DateUtils.timeTotime(stationInfo.getStart_time());
                            String endTime = DateUtils.timeTotime(stationInfo.getEnd_time());
                            worktime = DateUtils.isBelong(startTime, endTime);
                            if (stationInfo.getType() ==1 && stationInfo.getStatus() != 2 && worktime){
                                stationPopup.appoint.setVisibility(View.VISIBLE);
                            }else {
                                stationPopup.appoint.setVisibility(View.GONE);
                            }
                        }else {
                            stationPopup.appoint.setVisibility(View.GONE);
                        }
                        stationPopup.redelayDismiss.setVisibility(View.GONE);
                        stationPopup.time.setVisibility(View.GONE);
                    }
                    if (timeCount != null) {
                        timeCount.cancel();
                    }
                }
            }
        });
    }

    @Override
    public void timeFinish() {
        timeCount.cancel();
        UserLogin.getInstance().setYuyueId(-1);
        UserLogin.getInstance().setAppointment_no("");
        SharePref.put(MainActivity.this, API.appointment_no, "");
        SharePref.put(MainActivity.this, API.siteid, "-1");
        HashMap<String, String> map = new HashMap<>();
        map.put("appointment_no", appointment_no);
        new MyRequest().myRequest(API.cancelappoint, true, map, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                getcurrentAppint();
            }
        });
        if (stationPopup != null) {
            stationPopup.redelayDismiss.setVisibility(View.GONE);
            stationPopup.appoint.setVisibility(View.VISIBLE);
            stationPopup.time.setVisibility(View.GONE);
        }
    }

    private void bindClick() {
        binding.imgLeft.setOnClickListener(this);
        binding.tvRight.setOnClickListener(this);
        binding.tvCity.setOnClickListener(this);
        binding.imgKefu.setOnClickListener(this);
        binding.reCarplate.setOnClickListener(this);
//        binding.tvHuandian.setOnClickListener(this);
        binding.tvStationName.setOnClickListener(this);
        binding.qrcode.setOnClickListener(this);
        binding.scan.setOnClickListener(this);
        binding.msg.setOnClickListener(this);
        binding.recharge.setOnClickListener(this);
        binding.daka.setOnClickListener(this);
    }

    public void paysuccessLoad() {
        getcurrentAppint();
        Intent intent = new Intent(MainActivity.this, BillActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        //        mLocationClient.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.mapview.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
        if (timeCount != null) {
            timeCount.cancelTime();
            timeCount = null;
        }
        if (privacyDialog != null) {
            privacyDialog.dismiss();
            privacyDialog = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.banner.stopViewAnimator();
//        mLocationClient.stopLocation();
    }

    private void setLocate() {
        if (GpsUtil.isOPen(this)) {
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        } else {
            new AlertDialog.Builder(MainActivity.this).setTitle("定位失败").setMessage("请检查是否开启定位服务").setPositiveButton("开启", (dialogInterface, i) -> {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUESTCODE_GPS);
            }).setNegativeButton("取消", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                UserLogin.getInstance().setCityName("");
            }).create().show();
        }
    }

    private void bluePointLocat() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    private void markerCreate(List<StationInfo> list, StationInfo s) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        int position = list.indexOf(s);
        for (Marker marker : markerList) {
            if (marker.getObject() instanceof StationInfo) {
                marker.remove();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            StationInfo station = list.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
            markerOptions.position(latLng);
            boolean worktime = false;
            String startTime = DateUtils.timeTotime(station.getStart_time());
            String endTime = DateUtils.timeTotime(station.getEnd_time());
            worktime = DateUtils.isBelong(startTime, endTime);
            int mipmap = 0; // 0运营 1关闭 2维修
            if (station.getStatus() == 2){
                if (station.getType() == 0){
                    mipmap = R.mipmap.station_repair_map;
                }else {
                    mipmap = R.mipmap.bao_nopower_map;
                }
            }else {
                if (station.getBattery() > 0 && worktime) {
                    if (station.getType() == 0) {//0是换电站
                        mipmap = R.mipmap.station_working_map;
                    } else {
                        mipmap = R.mipmap.bao_working_map;
                    }
                } else if (station.getBattery() > 0 && !worktime) {//休息中
                    if (station.getType() == 0) {//0是换电站
                        mipmap = R.mipmap.station_rest_map;
                    } else {
                        mipmap = R.mipmap.bao_rest_map;
                    }
                } else if (worktime && station.getBattery() == 0) {//无电池
                    if (station.getType() == 0) {//0是换电站
                        mipmap = R.mipmap.station_working_map;
                    } else {
                        mipmap = R.mipmap.bao_nopower_map;
                    }
                } else {
                    if (station.getType() == 0) {//0是换电站
                        mipmap = R.mipmap.station_working_map;
                    } else {
                        mipmap = R.mipmap.bao_nopower_map;
                    }
                }
            }
            if (position == i) {
                bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), mipmap), 128, 144));
            } else {
                bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), mipmap), 96, 108));
            }
            markerOptions.icon(bitmap);
            Marker marker = aMap.addMarker(markerOptions);
            marker.setObject(station);
            markerList.add(marker);
        }
    }

    private void markerAllCreate(List<StationAll> list, StationAll s) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        int position = list.indexOf(s);
        for (Marker marker : markerList) {
            if (marker.getObject() instanceof StationAll) {
                marker.remove();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            StationAll station = list.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
            markerOptions.position(latLng);
            int mipmap = 0; // 0运营 1关闭 2维修
            if (station.getType() == 0){
                mipmap = R.mipmap.station_working_map;
            }else {
                mipmap = R.mipmap.bao_working_map;
            }
            if (position == i) {
                bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), mipmap), 128, 144));
            } else {
                bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), mipmap), 96, 108));
            }
            markerOptions.icon(bitmap);
            Marker marker = aMap.addMarker(markerOptions);
            marker.setObject(station);
            markerList.add(marker);
        }
    }
    //地图显示刷新的位置
    private void refreshMap(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    public Bitmap setImgSize(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    //    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.mapview.onResume();
        loadData();
        loadMessage();
        if (UserLogin.getInstance().getLatitude(MainActivity.this) != 0) {
            loadCity(UserLogin.getInstance().getLatitude(MainActivity.this), UserLogin.getInstance().getLongtitude(MainActivity.this), UserLogin.getInstance().getCityName(MainActivity.this));
        }
//        if (mSensor != null) {
//            sensorManager.registerListener(shakeListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        }
    }

    private void loadMessage() {
        String json = "type=0&page=0&size=1";
        new MyRequest().spliceJson(API.messagelist, false, json, MainActivity.this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                if (jsonArray != null) {
                    List<Message> mylist = GsonUtils.getInstance().fromJson(jsonArray, new TypeToken<List<Message>>() {
                    }.getType());
                    if (mylist.size() > 0) {
                        Message message = mylist.get(0);
                        String ss = message.getContent();
                        binding.banner.setDatas(Utils.getStrList(ss, 18));
                        binding.message.setVisibility(View.VISIBLE);
                        binding.banner.startViewAnimator();
                    } else {
                        binding.message.setVisibility(View.GONE);
                        binding.banner.stopViewAnimator();
                    }
                } else {
                    binding.message.setVisibility(View.GONE);
                    binding.banner.stopViewAnimator();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.mapview.onPause();
//        if (mSensor != null) {
//            sensorManager.unregisterListener(shakeListener);
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.mapview.onSaveInstanceState(outState);
    }

    @Override
    public void click(StationInfo station) {
        if (station == null) {
            binding.tvStationName.setText("");
            return;
        }
        if (ListUtils.isEmpty(stationlist)) {
            return;
        }
        markerCreate(stationlist, station);
        binding.tvStationName.setText(station.getName());
        showPopDialog(station);
        if (stationPopup !=null){
            stationPopup.setStation(station);
        }
    }

    private void loadData() {
        new MyRequest().mycodeRequest(API.INFO, false, null, true, this, null, null, new ResponseCode() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull, int code, String message) {
                if (json != null || jsonArray != null) {
                    Utils.shareprefload(json, MainActivity.this);//存储个人信息和阿里云信息
                    UserLogin userLogin = GsonUtils.getInstance().fromJson(json, UserLogin.class);
                    if (userLogin.getUnread() > 0) {
                        binding.imgMsg.setImageDrawable(getResources().getDrawable(R.mipmap.icon_message_new));
                    } else {
                        binding.imgMsg.setImageDrawable(getResources().getDrawable(R.mipmap.icon_message));
                    }
                    useBind = Utils.getusebind(true, json, MainActivity.this);
                    if (!Utils.usebindisNotexist(useBind)) {
                        binding.tvCarplate.setText(useBind.getPlate_number());
                        if (useBind.getBusiness_type() == 5) {//5是出租车司机
                            binding.daka.setVisibility(View.VISIBLE);
                        } else {
                            binding.daka.setVisibility(View.GONE);
                        }
                    }
                    if (code == 7) {
                        BaseDialog.showcodeDialog(MainActivity.this, message + "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, MyActivityListActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }
}
