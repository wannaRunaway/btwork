package com.btkj.chongdianbao.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
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
import com.btkj.chongdianbao.R;
import com.btkj.chongdianbao.databinding.ActivityMainBinding;
import com.btkj.chongdianbao.databinding.StationMapBinding;
import com.btkj.chongdianbao.login.PasswordLoginActivity;
import com.btkj.chongdianbao.login.PrivacyActivity;
import com.btkj.chongdianbao.login.UseProtocolActivity;
import com.btkj.chongdianbao.mine.ConsumeActivity;
import com.btkj.chongdianbao.mine.MessageActivity;
import com.btkj.chongdianbao.mine.MineActivity;
import com.btkj.chongdianbao.mine.RechargeActivity;
import com.btkj.chongdianbao.mine.ScanActivity;
import com.btkj.chongdianbao.model.BaseObject;
import com.btkj.chongdianbao.model.CityListModel;
import com.btkj.chongdianbao.model.Informodel;
import com.btkj.chongdianbao.model.RecommendStationModel;
import com.btkj.chongdianbao.model.ReserverCurrentModel;
import com.btkj.chongdianbao.model.ReserverDelayModel;
import com.btkj.chongdianbao.model.ScanStationModel;
import com.btkj.chongdianbao.model.ServiceListModel;
import com.btkj.chongdianbao.model.StartReserverModel;
import com.btkj.chongdianbao.model.Station;
import com.btkj.chongdianbao.model.StationListModel;
import com.btkj.chongdianbao.model.User;
import com.btkj.chongdianbao.utils.API;
import com.btkj.chongdianbao.utils.BaseDialog;
import com.btkj.chongdianbao.utils.BaseRequest;
import com.btkj.chongdianbao.utils.Customize;
import com.btkj.chongdianbao.utils.GpsUtil;
import com.btkj.chongdianbao.utils.GsonUtils;
import com.btkj.chongdianbao.utils.ListUtils;
import com.btkj.chongdianbao.utils.MyRequest;
import com.btkj.chongdianbao.utils.Myparams;
import com.btkj.chongdianbao.utils.SharePref;
import com.btkj.chongdianbao.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.yanzhenjie.permission.AndPermission;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leefeng.promptlibrary.PromptDialog;

/*import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;*/

public class MainActivity extends BaseActivity implements INaviInfoCallback, View.OnClickListener, DialogInterface.OnDismissListener, TimeFinishInterface, PromotionCarSelect {
    PrivacyDialog privacyDialog;
    private ActivityMainBinding binding;
    private StationMapBinding dialogbinding;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private AMap aMap;
    private List<CityListModel.DataBean> citylist = new ArrayList<>();
    private Dialog mStationDialog;
    private String plateNo;
    private int bindId, carType, batteryCount, cityId;
    private List<Station> stationlist;
    private List<Marker> markerList = new ArrayList<>();
    private EasyPopup customPopup;
    private final int REQUESTCODE_CITY = 1002, REQUESTCODE_CAR = 1001, REQUESTCODE_SCAN = 1003, REQUESTCODE_LIST = 1004,REQUESTCODE_GPS=1010;
    private TimeCount timeCount;
    private TimeFinishInterface timeFinishInterface;
    private StationSelectFragment stationSelectFragment;
    private PromptDialog promptDialog;
    private ServiceListModel serviceListModel;
    private String apkPath;
    private GeocodeSearch geocodeSearch;
    private UpdateManager updateManager;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {//
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    if(TextUtils.isEmpty(amapLocation.getCity())){
                        //??????????????????????????????????????????????????????????????????????????????????????????
                        getAddressByLatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                    }else {
                        User.getInstance().setCityName(amapLocation.getCity()+"");
                        binding.tvCity.setText(amapLocation.getCity());
                        User.getInstance().setMylatitude(amapLocation.getLatitude());
                        User.getInstance().setMylongtitude(amapLocation.getLongitude());
                        Utils.log(null, null, amapLocation.getCity() + "????????????" + amapLocation.getCityCode() + amapLocation.getErrorCode() + amapLocation.getErrorInfo() + amapLocation.getLatitude() + amapLocation.getLongitude());
                        for (int i = 0; i < citylist.size(); i++) {
                            if (citylist.get(i).getName().equals(amapLocation.getCity())) {
                                cityId = citylist.get(i).getId();
                            }
                        }
                        if (cityId == 0) {
                            cityId = 330100; //??????????????????
                        }
                        //TODO
                        refreshMap(amapLocation.getLatitude(), amapLocation.getLongitude());
                        loadStation(cityId, 1, 100, "", amapLocation.getLongitude(), amapLocation.getLatitude());
                    }
                } else {
                    User.getInstance().setCityName("null");
                    //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
                    Log.d(Utils.TAG, "location Error, ErdrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void getAddressByLatLng(double latitude,double longitude) {
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
                String city= regeocodeResult.getRegeocodeAddress().getCity();
                User.getInstance().setCityName(city);
                binding.tvCity.setText(city);
                User.getInstance().setMylatitude(latitude);
                User.getInstance().setMylongtitude(longitude);
                for (int i = 0; i < citylist.size(); i++) {
                    if (citylist.get(i).getName().equals(city)) {
                        cityId = citylist.get(i).getId();
                    }
                }
                if (cityId == 0) {
                    cityId = 330100; //??????????????????
                }
                //TODO
                refreshMap(latitude, longitude);
                loadStation(cityId, 1, 100, "", latitude, longitude);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {

            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //????????????
        geocodeSearch.getFromLocationAsyn(query);
    }

    // ?????? Marker ??????????????????
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker ?????????????????????????????????
        // ?????? true ?????????????????????????????????????????????false
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (User.getInstance().isIsneedlogin()){
                Intent intent = new Intent(MainActivity.this, PasswordLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            if (User.getInstance().isYuyue()) {
                Utils.snackbar(getApplicationContext(), MainActivity.this, "??????????????????????????????????????????????????????");
                return true;
            }
            markerCreate(stationlist, (Station) marker.getObject());
            showPopDialog((Station) marker.getObject());

            return true;
        }
    };

    private void initpopDialog(Station contentBean) {
        dialogbinding.tvCountTime.setVisibility(View.GONE);
        dialogbinding.tvName.setText(contentBean.getName());
        dialogbinding.tvAddress.setText(contentBean.getAddress());
        dialogbinding.tvPhone.setText(contentBean.getTelephone());
        dialogbinding.tvClock.setText(contentBean.getWorkTime());
        float distance = AMapUtils.calculateLineDistance(new LatLng(User.getInstance().getMylatitude(), User.getInstance().getMylongtitude()), new LatLng(contentBean.getLatitude(), contentBean.getLongitude()));
        DecimalFormat df = new DecimalFormat("#.00");
        if (distance < 1000) {
            dialogbinding.tvDistance.setText("?????????" + df.format(distance) + "m");
        } else {
            dialogbinding.tvDistance.setText("?????????" + df.format(distance / 1000) + "km");
        }
        int status = contentBean.getStatusApp();
        Utils.log(null, null, status + "????????????");
        if (status == 0) {
            if (contentBean.getAvailBatteryCountMap().getKwh10() + contentBean.getAvailBatteryCountMap().getKwh12() + contentBean.getAvailBatteryCountMap().getKwh15() >= 2) {
                dialogbinding.imgStatus.setBackgroundResource(R.mipmap.station_status_normal);
                dialogbinding.imgYuyueImm.setBackgroundResource(R.mipmap.btn_order);
            } else {
                dialogbinding.imgStatus.setBackgroundResource(R.mipmap.station_status_full);
                dialogbinding.imgYuyueImm.setBackgroundResource(R.mipmap.btn_recommend);
            }
        } else if (status == 1) {
            dialogbinding.imgStatus.setBackgroundResource(R.mipmap.station_status_close);
            dialogbinding.imgYuyueImm.setBackgroundResource(R.mipmap.btn_recommend);
        }
        if (User.getInstance().isDelay()) {
            dialogbinding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.booking_btn_success_delay));
        } else {
            dialogbinding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.booking_btn_delay));
        }
        if (User.getInstance().isYuyue()) {
            dialogbinding.imgYuyueImm.setVisibility(View.GONE);
            dialogbinding.reBottom.setVisibility(View.VISIBLE);
        } else {
            dialogbinding.imgYuyueImm.setVisibility(View.VISIBLE);
            dialogbinding.reBottom.setVisibility(View.GONE);
        }
        if(contentBean.getType()==3){
            dialogbinding.tvDetail.setVisibility(View.VISIBLE);
            dialogbinding.tvDetail.setOnClickListener(view->{
                Intent intent=new Intent(MainActivity.this, StationDetailActivity.class);
                intent.putExtra("stationId",contentBean.getId());
                startActivity(intent);
            });
        }else {
            dialogbinding.tvDetail.setVisibility(View.GONE);
        }
        dialogbinding.imgYuyueImm.setOnClickListener(view -> {
            if (status == 0) {
                if (contentBean.getAvailBatteryCountMap().getKwh10() + contentBean.getAvailBatteryCountMap().getKwh12() + contentBean.getAvailBatteryCountMap().getKwh15() >= 2) {
                    startReserver(contentBean.getId(), contentBean);
                } else {
                    mStationDialog.dismiss();
                    recommendStation();
                }
            } else if (status == 1) {
                mStationDialog.dismiss();
                recommendStation();
            }
        });
        dialogbinding.imgGuide.setOnClickListener(view -> {
            Poi start = new Poi("", new com.amap.api.maps.model.LatLng(User.getInstance().getMylatitude(), User.getInstance().getMylongtitude()), "");
            /**?????????????????????????????????,??????POI???ID "B000A83M61"????????????????????????????????????????????????????????????????????????**/
            /**Poi????????????????????????PoiID???PoiiD????????????????????????Poiid?????????????????????????????????**/
            Poi end = new Poi("", new com.amap.api.maps.model.LatLng(contentBean.getLatitude(), contentBean.getLongitude()), "");
            AmapNaviPage.getInstance().showRouteActivity(MainActivity.this, new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.NAVI), MainActivity.this);
        });
        dialogbinding.imgYuyueDelay.setOnClickListener(view -> {
            if (User.getInstance().isDelay()) {
                Utils.snackbar(getApplicationContext(), MainActivity.this, "?????????????????????");
                return;
            }
            BaseDialog.showDialog(MainActivity.this, "???????????????????????????10??????", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delayappoint();
                }
            });
        });
        dialogbinding.imgYuyueCancel.setOnClickListener(view -> {
            BaseDialog.showDialog(MainActivity.this, "???????????????????????????", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelappoint();
                }
            });
        });
    }

    private void showPopDialog(Station contentBean) {
        if (bindId == 0 || batteryCount == 0 || carType == 0) {
            Utils.snackbar(getApplicationContext(), MainActivity.this, "??????????????????");
            return;
        }
        if (mStationDialog == null) {
            mStationDialog = new Dialog(this, R.style.dialog);
            View root = LayoutInflater.from(this).inflate(R.layout.station_map, null);
            dialogbinding = StationMapBinding.bind(root);
            initpopDialog(contentBean);
            mStationDialog.setContentView(root);
            Window dialogWindow = mStationDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            //dialogWindow.setWindowAnimations(R.style.dialogstyle);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = -20;
            lp.width = (int) getResources().getDisplayMetrics().widthPixels; // ??????
            root.measure(0, 0);
            lp.height = root.getMeasuredHeight();
            lp.alpha = 9f;
            dialogWindow.setAttributes(lp);
            mStationDialog.setCanceledOnTouchOutside(true);
            mStationDialog.setOnDismissListener(this);
        } else {
            initpopDialog(contentBean);
        }
        mStationDialog.show();
    }

    //????????????
    private void cancelappoint() {
        //id  [int]	???	??????id
        if (!User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), MainActivity.this, API.yueyue_code);
            return;
        }
        String url = API.BASE_URL + API.CANCELAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(User.getInstance().getReserverId()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                User.getInstance().setDelay(false);
                User.getInstance().setYuyue(false);
                if (mStationDialog != null) {
                    mStationDialog.dismiss();
                }
                if (timeCount != null) {
                    timeCount.onFinish();
                    timeCount.cancel();
                }
            }
        });
    }

    //????????????
    private void delayappoint() {
        //id  [int]	???	??????id
        if (!User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), MainActivity.this, "????????????");
            return;
        }
        String url = API.BASE_URL + API.DELAYAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(User.getInstance().getReserverId()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                User.getInstance().setDelay(true);
                ReserverDelayModel model = GsonUtils.getInstance().fromJson(json, ReserverDelayModel.class);
                if (timeCount != null) {
                    timeCount.cancel();
                    long time = model.getContent().getEndTime() - System.currentTimeMillis();
                    Utils.log(null, null, time + "?????????????????????");
                    timeCount = new TimeCount(time, dialogbinding.tvCountTime, dialogbinding.imgStatus, timeFinishInterface);
                    timeCount.start();
                }
                dialogbinding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.booking_btn_success_delay));
            }
        });
    }

    /**
     * stationId    [int]	???	??????id
     * bindId       [int]	???	??????id
     * batteryCount [int]	???	????????????
     * batteryType  [int]	???	???????????????10???10????????????12???12????????????15:15????????????
     */
    private void startReserver(int stationId, Station station) {
        String url = API.BASE_URL + API.ADDAPPOINT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("stationId", String.valueOf(stationId));
        map.put("bindId", String.valueOf(bindId));
        map.put("batteryCount", String.valueOf(batteryCount));
        map.put("batteryType", String.valueOf(carType));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        promptDialog.showLoading("????????????...");
        new MyRequest().mypromptrequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                dialogbinding.imgYuyueImm.setVisibility(View.GONE);
                dialogbinding.reBottom.setVisibility(View.VISIBLE);
                StartReserverModel startReserverModel = GsonUtils.getInstance().fromJson(json, StartReserverModel.class);
                User.getInstance().setReserverId(startReserverModel.getContent().getId());
                User.getInstance().setYuyue(true);
                User.getInstance().setStation(station);
                long time = startReserverModel.getContent().getEndTime() - System.currentTimeMillis();
                Utils.log(null, null, time + "?????????????????????");
                if (mStationDialog != null) {
                    if (timeCount != null) {
                        timeCount.cancel();
                    }
                    timeCount = new TimeCount(time, dialogbinding.tvCountTime, dialogbinding.imgStatus, timeFinishInterface);
                    timeCount.start();
                }
            }
        }, promptDialog);
    }

    private void loadStation(int cityId, int pageNo, int pageSize, String name, double longitude, double latitude) {
        /**
         * cityId    [int]		??????Id
         * pageNo    [int]		??????????????????1
         * pageSize  [int]		???/???????????????20
         * name      [string]		????????????????????????
         * longitude [double]		????????????
         * latitude  [double]		????????????
         */
        final String url = API.BASE_URL + API.STATIONLIST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("cityId", String.valueOf(cityId));
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        if (!name.isEmpty()) {
            map.put("name", name);
        }
        map.put("longitude", String.valueOf(longitude));
        map.put("latitude", String.valueOf(latitude));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myshouyerequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                StationListModel model = GsonUtils.getInstance().fromJson(json, StationListModel.class);
                stationlist = model.getContent();
                markerCreate(stationlist, null);
            }
        },null,null,false);
    }


    private void loadCityList() {
        String url = API.BASE_URL + API.CITYLIST;
        Map<String, String> map = new HashMap<>();
        RequestParams requestParams = new RequestParams();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myshouyerequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                CityListModel model = GsonUtils.getInstance().fromJson(json, CityListModel.class);
                citylist = model.getContent();
            }
        }, null, null, true);
    }

    private void getMessage(){
        String url = API.BASE_URL + API.INFO;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myshouyerequest(url, requestParams, MainActivity.this, getApplicationContext(), false, new Myparams() {
            @Override
            public void message(String json) {
                Informodel informodel = GsonUtils.getInstance().fromJson(json, Informodel.class);
                if(informodel.getContent().getNoReaded()>0){
                    binding.ivMsg.setImageResource(R.mipmap.icon_redmsg);
                    binding.imgLeft.setImageResource(R.mipmap.icon_redperson);
                }else {
                    binding.ivMsg.setImageResource(R.mipmap.icon_nomalmsg);
                    binding.imgLeft.setImageResource(R.mipmap.personal_normal);
                }
            }
        },null,null,true);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (User.getInstance().isIsneedlogin()){
            intent = new Intent(MainActivity.this, PasswordLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }
        switch (view.getId()) {
            case R.id.iv_msg:
                intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.img_left:
                intent = new Intent(MainActivity.this, MineActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_right:
                if (stationlist == null) {
                    Utils.snackbar(getApplicationContext(), MainActivity.this, "???????????????????????????");
                    return;
                }
                intent = new Intent(MainActivity.this, StationListActivity.class);
                intent.putExtra("list", (Serializable) stationlist);
                intent.putExtra("lists", (Serializable) stationlist);
                intent.putExtra(API.cityId, cityId);
                intent.putExtra(API.bindId, bindId);
                intent.putExtra(API.carType, carType);
                intent.putExtra(API.batterycount, batteryCount);
                intent.putExtra(API.cityName, binding.tvCity.getText().toString());
                startActivityForResult(intent, REQUESTCODE_LIST);
                break;
            case R.id.tv_city:
                intent = new Intent(MainActivity.this, ChoseCityActivity.class);
                startActivityForResult(intent, REQUESTCODE_CITY);
                break;
            case R.id.re_carplate:
                intent = new Intent(MainActivity.this, ChoseCarplateActivity.class);
                if(!TextUtils.isEmpty(binding.tvCarplate.getText().toString())){
                    intent.putExtra("carPlate",binding.tvCarplate.getText().toString());
                }
                startActivityForResult(intent, REQUESTCODE_CAR);
                break;
            case R.id.img_money:
                intent = new Intent(MainActivity.this, ConsumeActivity.class);
                startActivity(intent);
                break;
            case R.id.img_invite:
                BaseDialog.showImageDialog(MainActivity.this);
                break;
            case R.id.img_recharge:
                intent = new Intent(MainActivity.this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.img_scan:
                if (bindId == 0 || batteryCount == 0 || carType == 0) {
                    Utils.snackbar(getApplicationContext(), MainActivity.this, "??????????????????");
                    return;
                }
                AndPermission.with(this)
                        .permission(Manifest.permission.CAMERA)
                        .rationale((context, permissions, executor) -> {
                            // ????????????????????????????????????
                            executor.execute();
                        })
                        .onGranted(permissions -> {
                            Intent myintent = new Intent(MainActivity.this, ScanActivity.class);
                            startActivityForResult(myintent, REQUESTCODE_SCAN);
                        })
                        .onDenied(permissions ->  {
                            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                                showSettingDialog(MainActivity.this);
                                return;
                            }
                            Utils.snackbar(getApplicationContext(),MainActivity.this,"??????????????????");
                        })
                        .start();

                break;
            case R.id.guide_next:
                if (!User.getInstance().isYuyue()) {
                    if (timeCount != null) {
                        timeCount.onFinish();
                    }
                }
                markerCreate(stationlist, User.getInstance().getStation());
                showPopDialog(User.getInstance().getStation());
                break;
            case R.id.img_kefu:
                if(serviceListModel!=null){
                    if (serviceListModel.isSuccess()) {
                        showEasyPopup(serviceListModel);
                    } else {
                        Utils.snackbar(getApplicationContext(), MainActivity.this, serviceListModel.getMsg());
                    }
                }else {
                    Utils.snackbar(getApplicationContext(), MainActivity.this, "???????????????????????????");
                }
                break;
            case R.id.tv_huandian:
                recommendStation();
                break;
            case R.id.tv_station_name:
                if(stationlist!=null){
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

    private void loadServiceData() {
        String url = API.BASE_URL + API.SERVICELIST;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myshouyerequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                serviceListModel = GsonUtils.getInstance().fromJson(json, ServiceListModel.class);
                    try {
                        String versionName = serviceListModel.getContent().get(4).getValue();
                        apkPath = serviceListModel.getContent().get(11).getValue();
                        SharePref.put(MainActivity.this,API.apkurl,apkPath);
                        String qiangzhi = serviceListModel.getContent().get(8).getValue();
                        String content = serviceListModel.getContent().get(10).getValue();
                        updateManager = new UpdateManager(MainActivity.this, versionName, apkPath);
                        //6??????  8??????
                        if (qiangzhi.equals("1")) {
                            updateManager.checkUpdate(true, content);
                        } else {
                            updateManager.checkUpdate(false, content);
                        }
                    } catch (Exception e) {
                        Utils.log(null, null, "??????????????????");
                    }
        }}, null, null, true);
    }
    public void installApk(){
        if(updateManager!=null){
            updateManager.installApk();
        }
    }
    public void setProgress(){
        if(updateManager!=null){
            updateManager.setProgress();
        }
    }

    private void showEasyPopup(ServiceListModel model) {
        customPopup = new EasyPopup(this)
                .setContentView(R.layout.layout_easy_popup)
                //??????????????????PopupWindow?????????????????????
                .setFocusAndOutsideEnable(true)
                .createPopup();
        customPopup.showAtAnchorView(binding.imgKefu, VerticalGravity.BELOW, HorizontalGravity.ALIGN_RIGHT, 0, 0);
        TextView tv_phone = customPopup.getView(R.id.tv_phone);
        TextView tv_time = customPopup.getView(R.id.tv_time);
        TextView tv_call = customPopup.getView(R.id.tv_call);
        tv_phone.setText("????????????\n" + model.getContent().get(0).getValue());
        tv_time.setText(model.getContent().get(1).getValue() + "\n" + model.getContent().get(2).getValue());
        tv_call.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + model.getContent().get(0).getValue());
            intent.setData(data);
            startActivity(intent);
        });
    }

    /**
     * bindId          [int]	???	??????id
     * batteryType    [int]	???	?????????????????? 10,12,15
     * batteryCount   [int]	???	????????????
     * longitude   [double]		???????????????????????????
     * latitude       [double]		???????????????????????????
     */
    private void recommendStation() {
        String url = API.BASE_URL + API.STATIONRECOMMEND;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        if (bindId == 0 || batteryCount == 0 || carType == 0) {
            Utils.snackbar(getApplicationContext(), MainActivity.this, "??????????????????");
            return;
        }
        map.put("bindId", String.valueOf(bindId));
        map.put("batteryType", String.valueOf(carType));
        map.put("batteryCount", String.valueOf(batteryCount));
        map.put("longitude", String.valueOf(User.getInstance().getMylongtitude()));
        map.put("latitude", String.valueOf(User.getInstance().getMylatitude()));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        promptDialog.showLoading("????????????...");
        new MyRequest().myshouyerequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                RecommendStationModel model = GsonUtils.getInstance().fromJson(json, RecommendStationModel.class);
                if (model != null && model.getContent() != null && model.getContent().size() > 0) {
                    markerCreate(stationlist, model.getContent().get(0));
                    showPopDialog(model.getContent().get(0));
                }
            }
        },promptDialog, null,false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_CAR && resultCode == 1002) {
            plateNo = data.getStringExtra(API.plateNo);
            bindId = data.getIntExtra(API.bindId, 0);
            carType = data.getIntExtra(API.carType, 0);
            batteryCount = data.getIntExtra(API.batterycount, 0);
            binding.tvCarplate.setText(plateNo);
        } else if (requestCode == REQUESTCODE_CITY && resultCode == 1) {
            binding.tvCity.setText(data.getStringExtra(API.cityName));
            cityId = data.getIntExtra(API.cityId, 0);
            double longtitude = Double.parseDouble(data.getStringExtra(API.longtitude));
            double latitude = Double.parseDouble(data.getStringExtra(API.latitude));
            refreshLocation(latitude, longtitude);
            loadStation(cityId, 1, 100, "", longtitude, latitude);
        } else if (requestCode == REQUESTCODE_SCAN && resultCode == 2) {
            String message = data.getStringExtra(API.code);
            if (message.indexOf("station") != -1) {
                String[] splite = message.split("=");
                postScan(splite[1]);
            }
            if (message.indexOf("driver") != -1) {
                String[] splite = message.split("=");
                invite(splite[1]);
            }
        } else if (requestCode == REQUESTCODE_LIST && resultCode == 2) {
            if (User.getInstance().isYuyue()) {
                markerCreate(stationlist, User.getInstance().getStation());
                binding.reCarplate.setVisibility(View.GONE);
                binding.guideNext.setVisibility(View.VISIBLE);
                binding.tvHuandian.setVisibility(View.GONE);
            } else {
                binding.reCarplate.setVisibility(View.VISIBLE);
                binding.guideNext.setVisibility(View.GONE);
                binding.tvHuandian.setVisibility(View.VISIBLE);
                if (mStationDialog != null) {
                    mStationDialog.dismiss();
                }
            }
        }else if(requestCode==REQUESTCODE_GPS){
            setLocate();
        }
    }

    //???????????????????????????
    private void refreshLocation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    /**
     * ????????????
     * inviteDriverAccountNo     [string]	???	??????????????????
     * verify    [boolean]		??????????????? ??????false
     */
    private void invite(String data) {
        String url = API.BASE_URL + API.INVITE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("inviteDriverAccountNo", data);
        map.put("verify", String.valueOf(true));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, MainActivity.this, getApplicationContext(), true,new Myparams() {
            @Override
            public void message(String json) {
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                new AlertDialog.Builder(MainActivity.this).setMessage("???????????????" + baseObject.getContent() + "?????????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        inviteAgain(data);
                    }
                }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });
    }

    private void inviteAgain(String data) {
        String url = API.BASE_URL + API.INVITE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("inviteDriverAccountNo", data);
        map.put("verify", String.valueOf(false));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                Utils.snackbar(getApplicationContext(), MainActivity.this, "??????????????????");
            }
        });
    }

    private void postScan(String data) {
        String url = API.BASE_URL + API.SCANCODE;
        /**
         * stationId  [int]	???	??????id
         * bindId     [int]	???	??????id
         */
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("stationId", String.valueOf(data));
        map.put("bindId", String.valueOf(bindId));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myrequest(url, requestParams, MainActivity.this, getApplicationContext(), true, new Myparams() {
            @Override
            public void message(String json) {
                ScanStationModel model = GsonUtils.getInstance().fromJson(json, ScanStationModel.class);
                double money = User.getInstance().getBalance() - model.getContent().getReal();
                DecimalFormat df = new DecimalFormat("#.00");
                User.getInstance().setBalance(Double.parseDouble(df.format(money)));
                binding.reCarplate.setVisibility(View.VISIBLE);
                binding.guideNext.setVisibility(View.GONE);
                binding.tvHuandian.setVisibility(View.VISIBLE);
                User.getInstance().setYuyue(false);
                User.getInstance().setDelay(false);
                Intent intent = new Intent(MainActivity.this, ConsumeActivity.class);
                intent.putExtra(API.isfromScan, true);
                if (mStationDialog != null) {
                    mStationDialog.dismiss();
                }
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String s){
        if(!TextUtils.isEmpty(s)&&s.equals("homereaded")){
            binding.ivMsg.setImageResource(R.mipmap.icon_nomalmsg);
            binding.imgLeft.setImageResource(R.mipmap.personal_normal);
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        timeFinishInterface = this;
        promptDialog = new PromptDialog(this);

        binding.mapview.onCreate(savedInstanceState);
        aMap = binding.mapview.getMap();
        aMap.setOnMarkerClickListener(markerClickListener);
        BaseRequest.getInfo(MainActivity.this, getApplicationContext());

        mLocationClient = new AMapLocationClient(getApplicationContext());

        if((boolean)SharePref.get(this,"agree",false)){
            init();
        }else {
            if(privacyDialog==null){
                privacyDialog=new PrivacyDialog(MainActivity.this,R.style.mydialog);
                privacyDialog.show();
            }
        }
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }
    public void showSettingDialog(Context context) {
        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle("??????????????????")
                .setMessage("???????????????????????????????????????????????????????????????????????????")
                .setPositiveButton("???????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AndPermission.permissionSetting(MainActivity.this).execute();
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }



    public class PrivacyDialog extends Dialog {
        Context context;
        public PrivacyDialog(Context context, int themeResId) {
            super(context, themeResId);
            this.context=context;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_privacy);
            TextView xieyiTv=findViewById(R.id.tv_xieyi);
            TextView privacyTv=findViewById(R.id.tv_privacy);
            TextView cancelTv=findViewById(R.id.tv_cancel);
            TextView agreeTv=findViewById(R.id.tv_agree);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    init();
                }
            });
            agreeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    SharePref.put(context,"agree",true);
                    init();
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
    private void init(){
        bindClick();
        carinit();
        AndPermission.with(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION)
                .rationale((context, permissions, executor) -> {
                    // ????????????????????????????????????
                    executor.execute();
                })
                .onGranted(permissions -> {
                    setLocate();
                    loadCityList();
                    bluePointLocat(); //????????????
                    loadServiceData();
                })
                .onDenied(permissions ->  {
                    if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                        showSettingDialog(MainActivity.this);
                        return;
                    }
                    Utils.snackbar(getApplicationContext(),MainActivity.this,"??????????????????");
                })
                .start();
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    //???????????????????????????
    private void carinit() {
        bindId = (int) SharePref.get(MainActivity.this, API.bindId, 0);
        carType = (int) SharePref.get(MainActivity.this, API.carType, 0);
        batteryCount = (int) SharePref.get(MainActivity.this, API.batterycount, 0);
        plateNo = (String) SharePref.get(MainActivity.this, API.plateNo, "");
        if (!plateNo.isEmpty()) {
            binding.tvCarplate.setText(plateNo);
        }else {
            binding.tvCarplate.setText("???????????????");
        }
    }

    //??????????????????
    private void getcurrentAppint() {
        //bindId  [int]		??????id
        String url = API.BASE_URL + API.CURRENTAPPONT;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("bindId", String.valueOf(bindId));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().myshouyerequest(url, requestParams, MainActivity.this, getApplicationContext(), false, new Myparams() {
            @Override
            public void message(String json) {
                ReserverCurrentModel model = GsonUtils.getInstance().fromJson(json, ReserverCurrentModel.class);
                if (model.getContent() != null && model.getContent().getStation() != null) {
                    binding.tvHuandian.setVisibility(View.GONE);
                    binding.reCarplate.setVisibility(View.GONE);
                    binding.guideNext.setVisibility(View.VISIBLE);
                    User.getInstance().setReserverId(model.getContent().getRecord().getId());
                    User.getInstance().setYuyue(true);
                    User.getInstance().setStation(model.getContent().getStation());
                    long time = model.getContent().getRecord().getEndTime() - System.currentTimeMillis();

                    showPopDialog(model.getContent().getStation());
                    if (mStationDialog != null) {
                        if (timeCount != null) {
                            timeCount.cancel();
                        }
                        timeCount = new TimeCount(time, dialogbinding.tvCountTime, dialogbinding.imgStatus, timeFinishInterface);
                        timeCount.start();
                    }
                }else {
                    if(User.getInstance().isYuyue()){//????????????????????????????????????????????????????????????????????????
                        if (timeCount != null) {
                            timeCount.onFinish();
                            timeCount.cancel();
                        }
                        if (mStationDialog != null){
                            mStationDialog.dismiss();
                        }
                    }

                    mainBottomRefresh();
                }
            }
        }, null,null,true);
    }

    @Override
    public void timeFinish() {
        timeCount.cancel();
        User.getInstance().setYuyue(false);
        User.getInstance().setDelay(false);
        if (mStationDialog != null) {
            dialogbinding.imgYuyueDelay.setImageDrawable(getResources().getDrawable(R.mipmap.booking_btn_delay));
            dialogbinding.imgYuyueImm.setVisibility(View.VISIBLE);
            dialogbinding.reBottom.setVisibility(View.GONE);
        }
    }

    private void bindClick() {
        binding.imgLeft.setOnClickListener(this);
        binding.tvRight.setOnClickListener(this);
        binding.tvCity.setOnClickListener(this);
        binding.imgKefu.setOnClickListener(this);
        binding.imgMoney.setOnClickListener(this);
        binding.imgInvite.setOnClickListener(this);
        binding.imgRecharge.setOnClickListener(this);
        binding.imgScan.setOnClickListener(this);
        binding.reCarplate.setOnClickListener(this);
        binding.tvHuandian.setOnClickListener(this);
        binding.guideNext.setOnClickListener(this);
        binding.tvStationName.setOnClickListener(this);
        binding.ivMsg.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        //        mLocationClient.onDestroy();
        //???activity??????onDestroy?????????mMapView.onDestroy()???????????????
        binding.mapview.onDestroy();
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        if (timeCount != null) {
            timeCount.cancelTime();
            timeCount=null;
        }
        if(privacyDialog!=null){
            privacyDialog.dismiss();
            privacyDialog=null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mLocationClient.stopLocation();
    }

    private void setLocate(){
        if(GpsUtil.isOPen(this)){
            //????????????????????????
            mLocationClient.setLocationListener(mLocationListener);
            //?????????AMapLocationClientOption??????
            mLocationOption = new AMapLocationClientOption();
            //?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //???????????????????????????
            //??????????????????false???
            mLocationOption.setOnceLocation(true);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }else {
            new AlertDialog.Builder(MainActivity.this).setTitle("????????????").setMessage("?????????????????????????????????").setPositiveButton("??????", (dialogInterface, i) -> {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUESTCODE_GPS);
            }).setNegativeButton("??????", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                User.getInstance().setCityName("null");
            }).create().show();
        }
    }


    private void bluePointLocat() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//??????????????????????????????myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????1???1???????????????????????????myLocationType????????????????????????????????????
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(1000); //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        aMap.setMyLocationStyle(myLocationStyle);//?????????????????????Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);?????????????????????????????????????????????????????????
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.setMyLocationEnabled(true);// ?????????true?????????????????????????????????false??????????????????????????????????????????????????????false???
    }

    private static BitmapDescriptor bitmap = null;

    private void markerCreate(List<Station> list, Station station) {
        if(ListUtils.isEmpty(list)){
            return;
        }
        int position = list.indexOf(station);
        for (Marker marker : markerList) {
            marker.remove();
        }
        for (int i = 0; i < list.size(); i++) {
            Station stationInfo = list.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(stationInfo.getLatitude(), stationInfo.getLongitude());
            markerOptions.position(latLng);
            switch (stationInfo.getStatusApp()) {//statusApp  0?????????  1?????????
                case 1:
                    if (position == i) {
                        bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.mipmap.position_close_selected), 105, 126));
                    } else {
                        bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.mipmap.position_close_selected), 75, 90));
                    }
                    break;
                case 0:
                    if (stationInfo.getAvailBatteryCountMap().getKwh10() + stationInfo.getAvailBatteryCountMap().getKwh12() + stationInfo.getAvailBatteryCountMap().getKwh15() >= 2) {
                        if (position == i) {
                            bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.mipmap.position_leisure_selected), 105, 126));
                        } else {
                            bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.mipmap.position_leisure_selected), 75, 90));
                        }
                    } else {
                        if (position == i) {
                            bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.mipmap.position_busy_selected), 105, 126));
                        } else {
                            bitmap = BitmapDescriptorFactory.fromBitmap(setImgSize(BitmapFactory.decodeResource(getResources(), R.mipmap.position_busy_selected), 75, 90));
                        }
                    }
                    break;
                default:
                    break;
            }
            markerOptions.icon(bitmap);
            Marker marker = aMap.addMarker(markerOptions);
            marker.setObject(stationInfo);
            markerList.add(marker);
        }
    }


    //???????????????????????????
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

    @Override
    public void onDismiss(DialogInterface dialogInterface) { //dialog???????????????
        mainBottomRefresh();
    }

    private void mainBottomRefresh() {
        if (User.getInstance().isYuyue()) {
            binding.reCarplate.setVisibility(View.GONE);
            binding.guideNext.setVisibility(View.VISIBLE);
            binding.tvHuandian.setVisibility(View.GONE);
        } else {
            binding.reCarplate.setVisibility(View.VISIBLE);
            binding.guideNext.setVisibility(View.GONE);
            binding.tvHuandian.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //???activity??????onResume?????????mMapView.onResume ()???????????????????????????
        binding.mapview.onResume();
        carinit();
        getcurrentAppint();
        getMessage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView.onPause ()????????????????????????
        binding.mapview.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)??????????????????????????????
        binding.mapview.onSaveInstanceState(outState);
    }


    @Override
    public void click(Station station) {
        if (User.getInstance().isYuyue()) {
            Utils.snackbar(getApplicationContext(), MainActivity.this, "??????????????????????????????????????????");
            return;
        }
        if (station == null) {
            binding.tvStationName.setText("");
            return;
        }
        if(ListUtils.isEmpty(stationlist)){
            return;
        }
        markerCreate(stationlist,station);
        binding.tvStationName.setText(station.getName());
        showPopDialog(station);
    }


    @Override
    public void onInitNaviFailure() {
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

    }
}
