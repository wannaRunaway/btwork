package com.btkj.millingmachine.homepage;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityMainBinding;
import com.btkj.millingmachine.errorview.ConnectErrorActivity;
import com.btkj.millingmachine.errorview.ConnectingServerFragment;
import com.btkj.millingmachine.errorview.FixActivity;
import com.btkj.millingmachine.errorview.MarginLackActivity;
import com.btkj.millingmachine.errorview.SystemErrorFragment;
import com.btkj.millingmachine.errorview.UpgradeActivity;
import com.btkj.millingmachine.help.HelpActivity;
import com.btkj.millingmachine.model.appupdate.AppUpdate;
import com.btkj.millingmachine.model.appupdate.UpdateData;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.rechargequery.RechargeQueryActivity;
import com.btkj.millingmachine.ricetake.TakeRiceActivity;
import com.btkj.millingmachine.selftcult.ConnectJiliangHeartBeatInterface;
import com.btkj.millingmachine.selftcult.ConnectWuliHeartBeatInterface;
import com.btkj.millingmachine.selftcult.SelfcultActivity;
import com.btkj.millingmachine.selftcult.SelfcultFinishActivity;
import com.btkj.millingmachine.serialportutil.RiceUtil;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.DataUtils;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimeFinishInterface, ConnectWuliHeartBeatInterface, ConnectJiliangHeartBeatInterface {
    private ActivityMainBinding binding;
    private TimeCount timeCount;
    private static final int REQUEST_CODE = 1;
    private int device_id = 0;
    private ConfigModel configModel;
    private VideoDialogFragment videoDialogFragment;
    private ConnectingServerFragment connectingServerFragment;
    private SystemErrorFragment systemErrorFragment;
    private long timeJiliang, timeWuli;
    private ConnectJiliangHeartBeatInterface jiliangHeartBeatInterface;
    private ConnectWuliHeartBeatInterface wuliHeartBeatInterface;
    private boolean isMain = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        onbindingclick();
        wuliHeartBeatInterface = this;
        jiliangHeartBeatInterface = this;
        device_id = (Integer) SharePref.get(this, "id", 0);
        if (device_id != 0) {
            loadDevice();
            serialOpen();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getApkInfo(0); //app升级
                }
            }, 10000);
        } else {
            Intent intent = new Intent(this, FixActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
        Utils.log("mainActivity重创建");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
    Intent it;
    //开关机时间
    private void openAndDown(String startTime, String endTime) {
        SharePref.put(MainActivity.this, "start_time", startTime);
        SharePref.put(MainActivity.this, "end_time", endTime);
        it = new Intent("android.intent.action.hongdian.timer.startupandshutdown");
        it.putExtra("enable", true);
        it.putExtra("week", "[1,2,3,4,5,6,7]");
        it.putExtra("timeon", startTime);
        it.putExtra("timeoff", endTime);
        handler.sendEmptyMessageDelayed(1, 1000);
        Utils.log("发送定时开关机时间" + startTime + "结束" + endTime + DataUtils.stampToDate(System.currentTimeMillis()));
        sendBroadcast(it);
    }

    private void hideAll(FragmentTransaction fragmentTransaction) {
        if (videoDialogFragment != null) {
            fragmentTransaction.hide(videoDialogFragment);
        }
        if (connectingServerFragment != null) {
            fragmentTransaction.hide(connectingServerFragment);
        }
        if (systemErrorFragment != null) {
            fragmentTransaction.hide(systemErrorFragment);
        }
    }

    private void showFragment(int number) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideAll(fragmentTransaction);
        switch (number) {
            case 1:
                if (connectingServerFragment == null) {
                    connectingServerFragment = new ConnectingServerFragment();
                }
                if (connectingServerFragment.isAdded()) {
                    fragmentTransaction.show(connectingServerFragment).commitAllowingStateLoss();
                } else {
                    fragmentTransaction.add(R.id.content, connectingServerFragment).show(connectingServerFragment).commitAllowingStateLoss();
                }
                break;
            case 2:
                if (systemErrorFragment == null) {
                    systemErrorFragment = new SystemErrorFragment();
                }
                if (systemErrorFragment.isAdded()) {
                    fragmentTransaction.show(systemErrorFragment).commitAllowingStateLoss();
                } else {
                    fragmentTransaction.add(R.id.content, systemErrorFragment).show(systemErrorFragment).commitAllowingStateLoss();
                }
                break;
            default:
                break;
        }
    }

    public void showVideoFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (videoDialogFragment == null) {
            videoDialogFragment = new VideoDialogFragment();
        }
        if (videoDialogFragment.isAdded()) {
            transaction.show(videoDialogFragment).commitAllowingStateLoss();
        } else {
            transaction.add(R.id.content, videoDialogFragment).show(videoDialogFragment).commitAllowingStateLoss();
        }
    }

    public void hideVideoFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (videoDialogFragment == null) {
            videoDialogFragment = new VideoDialogFragment();
        }
        if (videoDialogFragment.isAdded()) {
            transaction.hide(videoDialogFragment).commitAllowingStateLoss();
        } else {
            transaction.add(R.id.content, videoDialogFragment).hide(videoDialogFragment).commitAllowingStateLoss();
        }
        cancelTimer();
        initCountTimer();
    }

    //倒计时完毕跳入到主页面
    @Override
    protected void onResume() {
        super.onResume();
        initCountTimer();
        isMain = true;
    }

    //初始化一个定时倒计时任务
    private void initCountTimer() {
        timeCount = new TimeCount(60000, 1000, binding.layoutHeader.tvTime, this);
        timeCount.start();
    }

    //打开串口
    private void serialOpen() {
        MyApplication.getInstance().initWuliSerialPort();
        MyApplication.getInstance().initJiliangSerialPort();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == 2) {
            device_id = data.getIntExtra("data", 0);
            if (device_id == 0) {
                Utils.toast(MainActivity.this, "设备编号为0，请检查");
                return;
            }
            loadDevice();
            serialOpen();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getApkInfo(0); //app升级
                }
            }, 10000);
            String shicha = (String) SharePref.get(MainActivity.this, API.qidongshicha, "");
            String guozaidianliu = (String) SharePref.get(MainActivity.this, API.guozaibaohu, "");
            byte[] shacha = new byte[4];
            shacha[0] = 0x23;
            shacha[1] = (byte) 0xD1;
            shacha[2] = 0x01;
            shacha[3] = (byte) (Integer.parseInt(shicha) & 0xff);
            MyApplication.getInstance().sendShichaQidong(shacha);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    byte[] dianliu = new byte[5];
                    dianliu[0] = 0x23;
                    dianliu[1] = (byte) 0xD2;
                    dianliu[2] = 0x02;
                    dianliu[3] = (byte) ((Integer.parseInt(guozaidianliu)>>8) & 0xff);
                    dianliu[4] = (byte) (Integer.parseInt(guozaidianliu) & 0xff);
                    MyApplication.getInstance().sendShichaQidong(dianliu);
                }
            }, 500);
        }
    }

    int a = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                a = a + 1;
                if (a < 5) {
                    Utils.log("定时开关机发送信息" + DataUtils.stampToDate(System.currentTimeMillis()));
                    sendBroadcast(it);
                    handler.sendEmptyMessageDelayed(1, 1000);
                } else {
                    handler.removeMessages(1);
                }
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadDevice();
        }
    };

    //更新设备配置
    private void loadDevice() {
        String url = API.BASE_URL + API.DEVICE_CONFIG;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", String.valueOf(device_id));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.logserial(json);
                configModel = GsonUtils.getInstance().fromJson(json, ConfigModel.class);
                SharePref.put(MainActivity.this, "config", json);
                initHeaderBottom();
                if (configModel != null && configModel.getData() != null && configModel.getData().getFetchCmdPeriod() != 0) {
                    handler.postDelayed(runnable, configModel.getData().getFetchCmdPeriod());
//                    Utils.log("handler联网发送数据");
                }
                String startTime = configModel.getData().getStartRunTime();
                String endTime = configModel.getData().getEndRunTime();
                if (!SharePref.get(MainActivity.this, "start_time", "").equals(startTime) ||
                        !SharePref.get(MainActivity.this, "end_time", "").equals(endTime)) {
                    a = 0;
                    openAndDown(startTime, endTime);
                }
                if (connectingServerFragment != null) {
                    getSupportFragmentManager().beginTransaction().hide(connectingServerFragment).commitAllowingStateLoss();
                }
                if (configModel.getData().getConStatus() == 0) {
                    showFragment(2);
                } else {
                    if (systemErrorFragment != null) {
                        getSupportFragmentManager().beginTransaction().hide(systemErrorFragment).commitAllowingStateLoss();
                    }
                }
                if (configModel != null && configModel.getData() != null && configModel.getData().isOpenDoor()) {
                    MyApplication.getInstance().sendWuliCmd(API.nianmistop);
//                    String time = (String) SharePref.get(MainActivity.this, API.qidongshicha, "");
//                    int timeNeed = Integer.parseInt(time) * 1000;
//                    handler.postDelayed(()->{
                        Intent intent = new Intent(MainActivity.this, SelfcultFinishActivity.class);
                        intent.putExtra("ismain", true);
                        startActivity(intent);
//                    }, timeNeed);
                }
                if (configModel.getData().getFirmwareUpdate() == 1 && isMain){
                    getApkInfo(1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log("网络请求失败");
                handler.postDelayed(runnable, 10000);
                Utils.log("handler断网发送数据");
                showFragment(1);
            }
        });
    }

    private void getApkInfo(int type) { // app升级 type int 类型   0 app；1 硬件
        String url = API.BASE_URL + API.APPUPDATE;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(type));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        Utils.log(requestParams.toString());
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                AppUpdate appUpdate = GsonUtils.getInstance().fromJson(json, AppUpdate.class);
                if (appUpdate.isSuccess()) {
                    if (type == 0) {
                        if (Integer.parseInt(appUpdate.getData().getVersionCode()) > Utils.getVersionCode(MainActivity.this)) {
                            //下载apk
                            Intent intent = new Intent(MainActivity.this, UpgradeActivity.class);
                            intent.putExtra("updatedata", appUpdate.getData());
                            intent.putExtra("isdevice", false);
                            startActivity(intent);
                        }
                    }else if (type == 1){
                        Intent intent = new Intent(MainActivity.this, UpgradeActivity.class);
                        intent.putExtra("updatedata", appUpdate.getData());
                        intent.putExtra("isdevice", true);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log("升级失败");
            }
        });
    }


    //初始化头部和底部
    public void initHeaderBottom() {
        if (configModel != null) {
            Config config = configModel.getData();
            if (config != null) {
                if (config.getIsShowDeviceLogo() == 0) {
                    Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.layoutHeader.imgTopIcon);
                }
                Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
                binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
                binding.layoutBottom.tvPhone.setText("客服电话：" + config.getServiceCall());
                binding.tvRicetoday.setText("今日米价：" + config.getRicePrice() / 2 + "元/斤");
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_selfcult:
                intent.setClass(MainActivity.this, SelfcultActivity.class);
                startActivity(intent);
                MyApplication.getInstance().sendChaxunmicang(API.chaxunmicang, MainActivity.this, true);
                break;
            case R.id.img_recharge:
                intent.setClass(MainActivity.this, RechargeQueryActivity.class);
                startActivity(intent);
                break;
            case R.id.img_rice_take:
                intent.setClass(MainActivity.this, TakeRiceActivity.class);
                startActivity(intent);
                break;
            case R.id.img_help:
                intent.setClass(MainActivity.this, HelpActivity.class);
                intent.putExtra("config", configModel);
                startActivity(intent);
                break;
            case R.id.img_back:
                toVideoPlay();
                break;
            default:
                break;
        }
    }

    //到播放视频页面
    private void toVideoPlay() {
        cancelTimer();
        showVideoFragment();
//        clearHandler();
    }

    @Override
    public void timeFinish() {
        toVideoPlay();
//        checkSerialPort();  //检查串口通信
    }
//
//    private void checkSerialPort() { //检查串口通信
//        MyApplication.getInstance().sendWuliHeartBeat(API.chaxunmicang, wuliHeartBeatInterface);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                MyApplication.getInstance().sendJiliangHeartBeat(API.chengzhong, jiliangHeartBeatInterface);
//            }
//        }, 1000);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Utils.log("物理"+(System.currentTimeMillis() - timeWuli)+"计量"+(System.currentTimeMillis() - timeJiliang));
//                if ((System.currentTimeMillis() - timeWuli) >= 10000 && (System.currentTimeMillis() - timeJiliang) >= 10000) {
//                    Intent intent = new Intent(MainActivity.this, ConnectErrorActivity.class);
//                    intent.putExtra("code", "8");
//                    intent.putExtra("code", "9");
//                    startActivity(intent);
//                }else if ((System.currentTimeMillis() - timeJiliang) >= 12000) {
//                    Intent intent = new Intent(MainActivity.this, ConnectErrorActivity.class);
//                    intent.putExtra("code", "9");
//                    startActivity(intent);
//                }else if ((System.currentTimeMillis() - timeWuli) >= 12000){
//                    Intent intent = new Intent(MainActivity.this, ConnectErrorActivity.class);
//                    intent.putExtra("code", "8");
//                    startActivity(intent);
//                }
//            }
//        }, 10000);
//    }

    private void clearHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    private void onbindingclick() {
        binding.imgSelfcult.setOnClickListener(this);
        binding.imgRecharge.setOnClickListener(this);
        binding.imgRiceTake.setOnClickListener(this);
        binding.imgHelp.setOnClickListener(this);
        binding.includeBack.imgBack.setOnClickListener(this);
        String path = getExternalFilesDir("me").getPath();
        newDirectory(path, "video");
    }

    public void newDirectory(String _path, String dirName) {
        File file = new File(_path + "/" + dirName);
        try {
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        clearHandler();
        cancelTimer();
        isMain = false;
        Utils.log("MainActivity onStop");
    }

    //停止定时任务
    private void cancelTimer() {
        if (timeCount != null) {
            timeCount.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearHandler();
        cancelTimer();
        Utils.log("MainActivity onDestory");
    }

    @Override
    public void jiliangHeartBeat() { //发送计量心跳
        timeJiliang = System.currentTimeMillis();
    }

    @Override
    public void wuliHeartBeat() { //发送物理心跳
        timeWuli = System.currentTimeMillis();
    }
}
