package com.kulun.energynet.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.bigkoo.pickerview.OptionsPickerView;
import com.kulun.energynet.R;
import com.kulun.energynet.model.Bean.JsonBean;
import com.kulun.energynet.model.User;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.GaoDeMapActivity;
import com.kulun.energynet.ui.activity.MainActivity;
import com.kulun.energynet.utils.AliOSS;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.GetJsonDataUtil;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import com.google.android.gms.appindexing.AppIndex;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion on 2017/7/26.
 */
public class StationFragment extends Fragment implements View.OnClickListener,INaviInfoCallback {
    private TextView tv_showmap;
    public TextView tv_cityname;
    private ArrayList<ArrayList<String>> option2Items = new ArrayList<>();
    private ArrayList<JsonBean> option1Items = new ArrayList<>();
    private ArrayList<ArrayList<Double>> Lat2Items = new ArrayList<>();
    private ArrayList<ArrayList<Double>> Lon2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> option3Items = new ArrayList<>();
    private TextView tv_chongdian, tv_huandian;
    private ViewPager viewpager;
    public HuandianFragment huandianFragment;
    public ChongdianFragment chongdianFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    protected boolean islocationfinish = false;
    //????????????
    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation amapLocation) {
            if (amapLocation != null) {
                islocationfinish = true;
                if (amapLocation.getErrorCode() == 0) {
//                    User.getInstance().setCityName(amapLocation.getCity());
//                    User.getInstance().setLatitude(amapLocation.getLatitude());
//                    User.getInstance().setLongtitude(amapLocation.getLongitude());
                    SharePref.put(getContext(), API.lat, Double.doubleToRawLongBits(amapLocation.getLatitude()));
                    SharePref.put(getContext(), API.lon, Double.doubleToRawLongBits(amapLocation.getLongitude()));
                    SharePref.put(getContext(), API.city, amapLocation.getCity()==null?"":amapLocation.getCity());
                    Utils.log(null,"","city"+amapLocation.getCity()+"--"+amapLocation.getLatitude()+"--"+amapLocation.getLongitude());
                    if (amapLocation != null && amapLocation.getCity() != null && !amapLocation.getCity().equals("")){
                        tv_cityname.setText(amapLocation.getCity()+"");
                    }else {
                        String city = (String) SharePref.get(getContext(), API.city, "");
                        if (city.equals("")) {
                            tv_cityname.setText("???????????????");
//                            Utils.toast(getContext(), "???????????????");
                        }else {
                            tv_cityname.setText(city);
                        }
                    }
                    if (tv_cityname.getText().toString() != null) {
                        if (huandianFragment != null && huandianFragment.refresh != null) {
                            huandianFragment.refreshUI();
                        }
                        if (chongdianFragment != null && chongdianFragment.refresh != null) {
                            chongdianFragment.refreshUI();
                        }
                    }
                } else {
                    //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
                    Log.d(Utils.TAG, "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    String city = (String) SharePref.get(getContext(), API.city, "");
                    if (city.equals("")) {
                        tv_cityname.setText("???????????????");
//                        Utils.toast(getContext(), "???????????????");
                    }else {
                        tv_cityname.setText(city);
                        if (huandianFragment != null && huandianFragment.refresh != null){
                            huandianFragment.refreshUI();
                        }
                        if (chongdianFragment != null && chongdianFragment.refresh != null){
                            chongdianFragment.refreshUI();
                        }
                    }
                }
            }
        }
    };
    public AMapLocationClientOption mLocationOption = null;
    private List<String> permissionList_Map = new ArrayList<>();
    public void refreshUI() {
        if (huandianFragment != null && huandianFragment.refresh != null) {
            huandianFragment.pageNo = 1;
            huandianFragment.refreshUI();
        }
        if (chongdianFragment != null && chongdianFragment.refresh != null) {
            chongdianFragment.pageNo = 1;
            chongdianFragment.refreshUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_fragmnet, container, false);
        tv_cityname = view.findViewById(R.id.tv_cityname);
        tv_showmap = view.findViewById(R.id.tv_showmap);
        tv_showmap.setOnClickListener(this);
        tv_cityname.setOnClickListener(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initJsonData();
            }
        });
        initView(view);
        initFragment();
        setLocate();
        requestMapPermissions();
        return view;
    }

    private void initView(View view) {
        viewpager = view.findViewById(R.id.viewpager);
        tv_huandian = view.findViewById(R.id.tv_huandian);
        tv_chongdian = view.findViewById(R.id.tv_chongdian);
        tv_huandian.setOnClickListener(this);
        tv_chongdian.setOnClickListener(this);
        Utils.log(null,"","city"+(String) SharePref.get(getContext(), API.city, ""));
    }

    private void initFragment() {
        select(0);
        huandianFragment = new HuandianFragment();
        chongdianFragment = new ChongdianFragment();
        fragmentList.add(huandianFragment);
        fragmentList.add(chongdianFragment);
        viewpager.setAdapter(new MyViewPageAdapter(getChildFragmentManager()));
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
//                    showPromptDialog();
                }
                select(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void select(int position) {
        switch (position) {
            case 0:
                tv_huandian.setTextSize(20);
                tv_chongdian.setTextSize(15);
                break;
            case 1:
                tv_huandian.setTextSize(15);
                tv_chongdian.setTextSize(20);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showmap: //????????????\
                res();
                if (permissionList_Map.size() == 0) {
                    tomapActivity();
                } else {
                    Utils.toast(getActivity(), "??????????????????????????????????????????");
                }
                break;
            case R.id.tv_cityname: //????????????
                showPickerView();
                break;
            case R.id.tv_chongdian:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tv_huandian:
                viewpager.setCurrentItem(0);
                break;
            default:
                break;
        }
    }

    //?????????????????????????????????
    private void showPromptDialog() {
        SharedPreferences preferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        boolean isChongdian = preferences.getBoolean("hasChargeAccount", false);
        if (!isChongdian) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("????????????????????????????????????????????????????????????");
            builder.setNeutralButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1001) {
            String city = (String) SharePref.get(getContext(), API.city, "");
            if (!tv_cityname.getText().toString().equals(city)) {
                tv_cityname.setText(city);
                refreshUI();
            }
        }
    }

    private void tomapActivity() {
        if (((String) SharePref.get(getContext(), API.city, "")).equals("")){
            Utils.toast(getContext(), "???????????????????????????");
            return;
        }
        Intent intent = new Intent(getActivity(), GaoDeMapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("huandianlist", (Serializable) huandianFragment.list);
        bundle.putSerializable("chongdianlist", (Serializable) chongdianFragment.list);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    private void showPickerView() {
        final OptionsPickerView pvOptions = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                final String city = option2Items.get(options1).get(options2);
                tv_cityname.setText(city);
//                User.getInstance().setCityName(city);
                SharePref.put(getContext(), API.city, city);
                refreshUI();
            }
        })
                .setTitleText("????????????")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //???????????????????????????
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(option1Items, option2Items);//???????????????
        pvOptions.show();
    }

    private void initJsonData() {
        String JsonData = new GetJsonDataUtil().getJson(getActivity(), "city.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);
        option1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//????????????
            ArrayList<String> CityList = new ArrayList<>();//????????????????????????????????????
            ArrayList<Double> LatList = new ArrayList<>();//????????????????????????????????????
            ArrayList<Double> LonList = new ArrayList<>();//????????????????????????????????????
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//??????????????????????????????????????????
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//??????????????????????????????
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                Double Lat = jsonBean.get(i).getCityList().get(c).getLat();
                Double Lon = jsonBean.get(i).getCityList().get(c).getLon();
                CityList.add(CityName);//????????????
                LatList.add(Lat);//??????????????????
                LonList.add(Lon);//??????????????????
                ArrayList<String> City_AreaList = new ArrayList<>();//??????????????????????????????
                //??????????????????????????????????????????????????????????????????null ?????????????????????????????????????????????
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//?????????????????????????????????
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//?????????????????????????????????
                    }
                }
                Province_AreaList.add(City_AreaList);//??????????????????????????????
            }
            option2Items.add(CityList);
            Lat2Items.add(LatList);
            Lon2Items.add(LonList);
            option3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
//            permissionList_Map.clear();
//            if (grantResults.length > 0) {
//                for (int i = 0; i < grantResults.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        return;
//                    }
//                }
//                //???????????????
//                Log.d(Utils.TAG, "???????????????");
//                mLocationClient.startLocation();
//            }
            // 1002???????????????????????????????????????
            Utils.log(null, "", "??????????????????" + grantResults.length);
            if (grantResults.length > 0) {
                // ?????????????????????????????????????????????????????????????????????????????????
                for (int i = 0; i < grantResults.length; i++) {
                    // PERMISSION_DENIED ????????????????????????????????????????????????????????????????????????????????????
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Utils.toast(getContext(), permissions[i] + "??????????????????");
                        Utils.log(null, "", "??????????????????");
                    }
                }
            }
            requestMapPermissions();
            if (grantResults.length == 0){
                mLocationClient.startLocation();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }

    //????????????
    private void setLocate() {
        //???????????????
        mLocationClient = new AMapLocationClient(getContext());
        //????????????????????????
        mLocationClient.setLocationListener(mLocationListener);
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        //?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //???????????????????????????

        //??????????????????false???
        mLocationOption.setOnceLocation(true);
        //????????????3s???????????????????????????????????????
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        //??????????????????????????????????????????
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void requestMapPermissions() {
        permissionList_Map.clear();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissionList_Map.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    permissionList_Map.toArray(new String[permissionList_Map.size()]), 2);
        }
        if (permissionList_Map.size() == 0) {
            mLocationClient.startLocation();
        }
    }

    private void res(){
        permissionList_Map.clear();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissionList_Map.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    permissionList_Map.toArray(new String[permissionList_Map.size()]), 2);
        }
    }

    private class MyViewPageAdapter extends FragmentPagerAdapter {
        public MyViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
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
