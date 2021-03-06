package com.kulun.energynet.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.kulun.energynet.R;
import com.kulun.energynet.model.Bean.JsonBean;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.huandian.StationInfo;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.fragment.AccountFragment;
import com.kulun.energynet.ui.fragment.ConsumeFragment;
import com.kulun.energynet.ui.fragment.RechargeFragment;
import com.kulun.energynet.ui.fragment.StationFragment;
import com.kulun.energynet.utils.AliOSS;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.Utils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion on 2017/6/19.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private TextView tabConsume;
    private TextView tabRecharge;
    private TextView tabStation;
    private TextView tabAccount;
    private ConsumeFragment consumeFragment;
    private RechargeFragment rechargeFragment;
    private StationFragment stationFragment;
    private AccountFragment accountFragment;
    private GoogleApiClient client2;
    private final static int REQUESTCODE_WRIEXT = 777;
    //????????????????????????????????????
    private long lastBackTime = 0;
    //????????????????????????????????????
    private long currentBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.activity_main);
        bindView();
        selected();
        showFragment(Constants.whichFragment);
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_WRIEXT);
        }
        AliOSS.getOss();
    }

    //0-???????????????1-????????????2-???????????????3-????????????
    private void showFragment(int number) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAll(fragmentTransaction);
        switch (number) {
            case 3:
                fragmentTransaction.show(consumeFragment).commitAllowingStateLoss();
                tabConsume.setSelected(true);
                break;
            case 2:
                fragmentTransaction.show(rechargeFragment).commitAllowingStateLoss();
                tabRecharge.setSelected(true);
                break;
            case 1:
                fragmentTransaction.show(stationFragment).commitAllowingStateLoss();
                tabStation.setSelected(true);
                break;
            case 0:
                fragmentTransaction.show(accountFragment).commitAllowingStateLoss();
                tabAccount.setSelected(true);
                break;
            default:
                break;
        }
    }

    private void hideAll(FragmentTransaction fragmentTransaction) {
        if (accountFragment != null) {
            fragmentTransaction.hide(accountFragment);
        }else {
            accountFragment = new AccountFragment();
            if (accountFragment.isAdded()){
                fragmentTransaction.hide(accountFragment);
            }else {
                fragmentTransaction.add(R.id.fragment_container, accountFragment).hide(accountFragment);
            }
        }
        if (stationFragment != null) {
            fragmentTransaction.hide(stationFragment);
        }else {
            stationFragment = new StationFragment();
            if (stationFragment.isAdded()){
                fragmentTransaction.hide(stationFragment);
            }else {
                fragmentTransaction.add(R.id.fragment_container, stationFragment).hide(stationFragment);
            }
        }
        if (rechargeFragment != null) {
            fragmentTransaction.hide(rechargeFragment);
        }else {
            rechargeFragment = new RechargeFragment();
            if (rechargeFragment.isAdded()){
                fragmentTransaction.hide(rechargeFragment);
            }else {
                fragmentTransaction.add(R.id.fragment_container, rechargeFragment).hide(rechargeFragment);
            }
        }
        if (consumeFragment != null) {
            fragmentTransaction.hide(consumeFragment);
        }else {
            consumeFragment = new ConsumeFragment();
            if (consumeFragment.isAdded()){
                fragmentTransaction.hide(consumeFragment);
            }else {
                fragmentTransaction.add(R.id.fragment_container, consumeFragment).hide(consumeFragment);
            }
        }
    }

    private void bindView() {
        tabConsume = (TextView) this.findViewById(R.id.txt_consume);
        tabRecharge = (TextView) this.findViewById(R.id.txt_recharge);
        tabStation = (TextView) this.findViewById(R.id.txt_station);
        tabAccount = (TextView) this.findViewById(R.id.txt_account);
        tabConsume.setOnClickListener(this);
        tabRecharge.setOnClickListener(this);
        tabStation.setOnClickListener(this);
        tabAccount.setOnClickListener(this);
    }

    public void selected() {
        tabConsume.setSelected(false);
        tabRecharge.setSelected(false);
        tabStation.setSelected(false);
        tabAccount.setSelected(false);
    }

    //0-???????????????1-????????????2-???????????????3-????????????
    @Override
    public void onClick(View view) {
        selected();
        switch (view.getId()) {
            case R.id.txt_consume:
                showFragment(3);
                tabConsume.setSelected(true);
                break;
            case R.id.txt_recharge:
                showFragment(2);
                tabRecharge.setSelected(true);
                break;
            case R.id.txt_station:
                showFragment(1);
                tabStation.setSelected(true);
                break;
            case R.id.txt_account:
                showFragment(0);
                tabAccount.setSelected(true);
                break;
            default:
                break;
        }
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client2.connect();
        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
        /**
         * 
         * ????????????????????????
         */
//        addUnreadCountChangeListener(true);
    }

    @Override
    public boolean onLongClick(View view) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE_WRIEXT) {
            AliOSS.getOss();
        }
    }

    @Override
    public void onBackPressed() {
        currentBackTime = System.currentTimeMillis();
        //??????????????????????????????????????????????????????????????????????????????2?????????????????????????????????
        if (currentBackTime - lastBackTime > 2 * 1000) {
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            lastBackTime = currentBackTime;
        } else { //????????????????????????????????????2?????????????????????
            finish();
//            qiyuLogout();
        }
    }

    //    private int unReadMessageNum;
    // ??????????????????????????????add ??? true ??????????????? false ??????????????????
    // ?????????????????????????????????????????????????????????
//    private UnreadCountChangeListener listener = new UnreadCountChangeListener() { // ????????????????????????
//        @Override
//        public void onUnreadCountChange(int count) {
//            // ??????????????????, count ?????????????????????
//            // ???????????? Unicorn.getUnreadCount() ?????????????????????
//            unReadMessageNum = count;
//            Log.d("btre", "????????????"+count);
//            if (fragmentPager == 1) {
//                unReadMessage(Quit, count);
//            }
//        }
//    };
//    private void unReadMessage(TextView textView, int messageNum){
//        if (messageNum > 0){
//            textView.setText("?????? " + messageNum);
//            textView.setClickable(true);
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    YSFUserInfo userInfo = new YSFUserInfo();
//                    userInfo.userId = String.valueOf(Constants.accountId);
//                    SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
//                    String phone = preferences.getString("phone", "");
//                    userInfo.data = "[{\"key\":\"phone\", \"value\":"+"\""+phone+"\""+"}]";
//                    Log.d("xuedi", userInfo.data+"?????????data");
//                    Unicorn.setUserInfo(userInfo);
//                    Unicorn.openServiceActivity(MainActivity.this, "??????????????????", new ConsultSource("", "", ""));
//                }
//            });
//        }else {
//            textView.setClickable(false);
//            textView.setText("");
//        }
//    }
//    private void addUnreadCountChangeListener(boolean add) {
//        Unicorn.addUnreadCountChangeListener(listener, add);
//    }
}
