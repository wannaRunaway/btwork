package com.btkj.millingmachine.ricetake;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityTakeRiceBinding;
import com.btkj.millingmachine.errorview.FixActivity;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.model.BaseObject;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.TimeFinishInterface;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.btkj.millingmachine.viewutils.TimeCount;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * created by xuedi on 2019/5/4
 */
public class TakeRiceActivity extends BaseActivity implements View.OnClickListener, TimeFinishInterface {
    private ActivityTakeRiceBinding binding;
    private String phone = "";
    private TimeCount timeCount;

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_take_rice);
        bindingOnclick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeCount = new TimeCount(120000, 1000, binding.center.time, this);
        timeCount.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img0:
                selectInput("0");
                break;
            case R.id.img1:
                selectInput("1");
                break;
            case R.id.img2:
                selectInput("2");
                break;
            case R.id.img3:
                selectInput("3");
                break;
            case R.id.img4:
                selectInput("4");
                break;
            case R.id.img5:
                selectInput("5");
                break;
            case R.id.img6:
                selectInput("6");
                break;
            case R.id.img7:
                selectInput("7");
                break;
            case R.id.img8:
                selectInput("8");
                break;
            case R.id.img9:
                selectInput("9");
                break;
            case R.id.img_clear:
                phone = "";
                binding.tvPhone.setText(phone);
                break;
            case R.id.img_confirm:
                if (binding.tvPhone.getText().toString().isEmpty()) {
                    Utils.toast(TakeRiceActivity.this, "网单号码不能为空");
                    return;
                }
                judgeisFix();
                break;
            case R.id.img_close:
                if (!phone.isEmpty()) {
                    phone = phone.substring(0, phone.length() - 1);
                    binding.tvPhone.setText(phone);
                }
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    //判断是否为维修卡
    private void judgeisFix() {
        String url = API.BASE_URL + API.CARD_FIX_PAY;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        /**
         * deviceId（int）， icCardChipId（String）
         */
        map.put("deviceId", String.valueOf(SharePref.get(this, "id", 0)));
        map.put("icCardChipId", String.valueOf(phone));
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        Utils.log(url + "\n" + requestParams);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                BaseObject baseObject = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                if (baseObject.isSuccess()) {
                    Intent intent = new Intent(TakeRiceActivity.this, FixActivity.class);
                    intent.putExtra("phone", binding.tvPhone.getText().toString());
                    startActivityForResult(intent, 2);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {
            String shicha = (String) SharePref.get(TakeRiceActivity.this, API.qidongshicha, "");
            String guozaidianliu = (String) SharePref.get(TakeRiceActivity.this, API.guozaibaohu, "");
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

    //控制手机号充值
    private void selectInput(String value) {
        phone = phone + value;
        binding.tvPhone.setText(phone);
    }

    //设置点击
    private void bindingOnclick() {
        binding.img0.setOnClickListener(this);
        binding.img1.setOnClickListener(this);
        binding.img2.setOnClickListener(this);
        binding.img3.setOnClickListener(this);
        binding.img4.setOnClickListener(this);
        binding.img5.setOnClickListener(this);
        binding.img6.setOnClickListener(this);
        binding.img7.setOnClickListener(this);
        binding.img8.setOnClickListener(this);
        binding.img9.setOnClickListener(this);
        binding.imgClear.setOnClickListener(this);
        binding.imgConfirm.setOnClickListener(this);
        binding.center.back.setOnClickListener(this);
        binding.imgClose.setOnClickListener(this);
    }

    @Override
    public void timeFinish() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeCount.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount.cancel();
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = configModel.getData();
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.layoutBottom.imgLogoBottom);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("客服电话：" + config.getServiceCall());
        }
    }
}
