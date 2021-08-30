package com.btkj.millingmachine.selftcult;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityPayresultBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.SoundPlayUtils;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.VideoViewFullScreen;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/5/3
 */
public class PayResultActivity extends BaseActivity implements SelfcultFinishInterface, IsNianmiInterface {
    private ActivityPayresultBinding binding;
    private String money;
    private TimeCount timeCount;
    private int weight;
//    private byte[] nianmistart;
    private int position = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (msg.what == 1) {
//                MyApplication.getInstance().sendShichaQidong(nianmistart);
//                Utils.log("电流过载启动碾米");
//            }
        }
    };
    private int buchangzhi;
    private SelfcultFinishInterface finishInterface;
    private int firstchange, secondchange;
    private int xiagusudu1, xiagusudu2, xiagusudu3;
    private String serialNo;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MyApplication.getInstance().sendJiliangCmd(API.chengzhong, finishInterface);
            handler.postDelayed(runnable, 200);
        }
    };
//    private Runnable runnablecheng = new Runnable() {
//        @Override
//        public void run() {
//            MyApplication.getInstance().sendChaxunmicang(API.chaxunmicang, PayResultActivity.this, false);
//            handler.postDelayed(runnablecheng, 10000);
//        }
//    };

    private VideoViewFullScreen viewFullScreen;
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payresult);
        viewFullScreen = binding.videoview;
        initVideoView();
        finishInterface = this;
        MyApplication.getInstance().sendJiliangCmd(API.qupi, this);
        MyApplication.getInstance().serialPortHandlerwuli.isnianmi = true;
        money = getIntent().getStringExtra("money");
        weight = getIntent().getIntExtra("weight", 0);
        serialNo = getIntent().getStringExtra("serialNo");
//        nianmistart = getIntent().getByteArrayExtra("nianmi");
        xiagusudu1 = Integer.parseInt((String) SharePref.get(this, API.xiagusudu1, ""));
        xiagusudu2 = Integer.parseInt((String) SharePref.get(this, API.xiagusudu2, ""));
        xiagusudu3 = Integer.parseInt((String) SharePref.get(this, API.xiagusudu3, ""));
        xiagu[0] = 0x23;
        xiagu[1] = (byte) 0xCC;
        xiagu[2] = 0x01;
        int time = 0;
        switch (weight) {
            case 1 * 500:
                time = Integer.parseInt((String) SharePref.get(this, API.yushenianmishijian1, ""));
                buchangzhi = Integer.parseInt((String) SharePref.get(this, API.buchangzhongliang1, ""));
                break;
            case 2 * 500:
                time = Integer.parseInt((String) SharePref.get(this, API.yushenianmishijian2, ""));
                buchangzhi = Integer.parseInt((String) SharePref.get(this, API.buchangzhongliang2, ""));
                break;
            case 3 * 500:
                time = Integer.parseInt((String) SharePref.get(this, API.yushenianmishijian3, ""));
                buchangzhi = Integer.parseInt((String) SharePref.get(this, API.buchangzhongliang3, ""));
                break;
            case 4 * 500:
                time = Integer.parseInt((String) SharePref.get(this, API.yushenianmishijian4, ""));
                buchangzhi = Integer.parseInt((String) SharePref.get(this, API.buchangzhongliang4, ""));
                break;
            case 5 * 500:
                time = Integer.parseInt((String) SharePref.get(this, API.yushenianmishijian5, ""));
                buchangzhi = Integer.parseInt((String) SharePref.get(this, API.buchangzhongliang5, ""));
                break;
            default:
                break;
        }
        firstchange = (weight - buchangzhi) * 2 / 10;
        secondchange = (weight - buchangzhi) - firstchange;
        timeCount = new TimeCount(time * 1000, 1000);
        timeCount.start();
        Utils.log("预设时间" + time + "碾米斤数" + weight);
//        startcult();
        binding.tvBottomInfo.setText("支付成功");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.tvBottomInfo.setText("本次消费" + money + "元");
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.tvBottomInfo.setText("正在碾米，请稍候");
            }
        }, 4000);
        //01030400050000EA32
        MyApplication.getInstance().isNianmi(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
//        handler.postDelayed(runnablecheng, 5000);
        SoundPlayUtils.play(3);
    }

    private void initVideoView() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.selfcult);
        viewFullScreen.setVideoURI(uri);
        viewFullScreen.start();
        viewFullScreen.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                        if (i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            viewFullScreen.setBackgroundColor(Color.TRANSPARENT);
                        }
                        return true;
                    }
                });
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
//        viewFullScreen.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//
//                viewFullScreen.setVideoURI(uri);
//                viewFullScreen.start();
//            }
//        });
    }

    @Override
    public void initHeaderBottom() {

    }

    private void startcult() {
//        MyApplication.getInstance().sendWuliCmd(API.nianmistart);
    }

    private void stopcult() {
        MyApplication.getInstance().sendWuliCmd(API.nianmistop);
    }

    private boolean isFirst = true, isSecond = true, isThired = true;
    //下谷速度
    private byte[] xiagu = new byte[4];

    @Override
    public void selffinsh(long result) {
        Utils.logserial(result + "==" + firstchange + "==" + secondchange + "==" + "==" + xiagusudu1 + "==" + xiagusudu2 + "==" + xiagusudu3 + "==" + weight
                + "==" + buchangzhi);
        if (result < firstchange && isFirst) {//第一段速度
            isFirst = false;
            Utils.logserial("第一段速度" + result + "==" + firstchange + "==" + isFirst + "==" + xiagusudu1);
            xiagu[3] = (byte) (xiagusudu1 & 0xff);
//            MyApplication.getInstance().sendWuliCmd(API.bujindianjisudu + xiagusudu1);
            MyApplication.getInstance().sendShichaQidong(xiagu);
        }
        if (result > firstchange && result < secondchange && isSecond) {
            isSecond = false;
            Utils.logserial("第二段速度" + result + "==" + secondchange + "==" + firstchange + "==" + isSecond + "==" + xiagusudu2);
            xiagu[3] = (byte) (xiagusudu2 & 0xff);
//            MyApplication.getInstance().sendWuliCmd(API.bujindianjisudu + xiagusudu2);
            MyApplication.getInstance().sendShichaQidong(xiagu);
        }
        if (result > secondchange && isThired) {
            isThired = false;
            Utils.logserial("第三段速度" + result + "==" + secondchange + "==" + firstchange + "==" + isThired + "==" + xiagusudu3);
            xiagu[3] = (byte) (xiagusudu3 & 0xff);
//            MyApplication.getInstance().sendWuliCmd(API.bujindianjisudu + xiagusudu3);
            MyApplication.getInstance().sendShichaQidong(xiagu);
        }
        if (result >= (weight - buchangzhi)) {
            Utils.log("称重器收到的:" + result + "需要碾米数量" + weight + "补偿值" + buchangzhi);
            nianmistop(result);
        }
    }

    //1称重器感应到了碾米够分量；2预设的时间到了
    private void nianmistop(long result) {
        clearHandler();
        stopcult();
//        String time = (String) SharePref.get(this, API.qidongshicha, "");
//        int timeNeed = Integer.parseInt(time) * 1000;
//        new Handler().postDelayed(() -> {
        MyApplication.getInstance().isNianmi(null);
        MyApplication.getInstance().serialPortHandlerwuli.isnianmi = false;
        Intent intent = new Intent(PayResultActivity.this, SelfcultFinishActivity.class);
        intent.putExtra("ismain", false);
        intent.putExtra("weight", result + buchangzhi);
        intent.putExtra("serialNo", serialNo);
        startActivity(intent);
        finish();
//        }, timeNeed);
    }

    @Override
    public void isguozai() {
        position = position + 1;
        Utils.log("电流过载");
//        handler.removeMessages(1);
//        handler.sendEmptyMessageDelayed(1, 10000);
        if (position == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String url = API.BASE_URL + API.ERROR;
                    RequestParams requestParams = new RequestParams();
                    Map<String, String> map = new HashMap<>();
                    map.put("deviceId", String.valueOf(SharePref.get(PayResultActivity.this, "id", 0)));
                    map.put("errorId", "2");
                    requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    PersistentCookieStore cookieStore = new PersistentCookieStore(PayResultActivity.this);
                    httpClient.setCookieStore(cookieStore);
                    httpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String json = new String(responseBody);
                        Utils.log("电流过载返回" + json);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        }
                    });
                }
            });
        }
    }

    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            nianmistop(weight);
        }
    }

    private void clearHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().isNianmi(null);
        MyApplication.getInstance().serialPortHandlerwuli.isnianmi = false;
        clearVideo();
        timeCount.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().isNianmi(null);
        MyApplication.getInstance().serialPortHandlerwuli.isnianmi = false;
        clearVideo();
        timeCount.cancel();
    }

    private void clearVideo(){
        if (viewFullScreen != null) {
            viewFullScreen.stopPlayback();
            viewFullScreen.setOnCompletionListener(null);
            viewFullScreen.setOnPreparedListener(null);
            binding.content.removeAllViews();
            viewFullScreen = null;
        }
    }
}
