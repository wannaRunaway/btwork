package com.btkj.millingmachine.errorview;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivityFixBinding;
import com.btkj.millingmachine.homepage.BaseActivity;
import com.btkj.millingmachine.model.BaseObject;
import com.btkj.millingmachine.model.config.Config;
import com.btkj.millingmachine.model.config.ConfigModel;
import com.btkj.millingmachine.network.GsonUtils;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by xuedi on 2019/5/5
 */
public class FixActivity extends BaseActivity implements View.OnClickListener {
    private ActivityFixBinding binding;
    private String phone;
    //获取到每个view的值
    private String zhongduanbianhao;
    private String weihudianhua;
    private String bencijiaruguliang;
    private String chumilv;
    private String guozaibaohu;
    private String qidongshicha;
    private String ipcode;
    private String dukachuankou;
    private String dukabotelv;
    private String jiliangchuankou;
    private String jiliangbotelv;
    private String wulichuankou;
    private String wulibotelv;
    private String xiagusudu1;
    private String xiagusudu2;
    private String xiagusudu3;
    private String nianmiyushe1;
    private String nianmiyushe2;
    private String nianmiyushe4;
    private String nianmiyushe3;
    private String nianmiyushe5;
    private String buchangzhongliang1;
    private String buchangzhongliang2;
    private String buchangzhongliang3;
    private String buchangzhongliang4;
    private String buchangzhongliang5;
    //每个view对应的存储
    private int zhongduanbianhaoSF;
    private String weihudianhuaSF;
    //    private String bencijiaruguliangSF;
    private String chumilvSF;
    private String guozaibaohuSF;
    private String qidongshichaSF;
    private String ipcodeSF;
    private String dukachuankouSF;
    private String dukabotelvSF;
    private String jiliangchuankouSF;
    private String jiliangbotelvSF;
    private String wulichuankouSF;
    private String wulibotelvSF;
    private String xiagusudu1SF;
    private String xiagusudu2SF;
    private String xiagusudu3SF;
    private String nianmiyushe1SF;
    private String nianmiyushe2SF;
    private String nianmiyushe4SF;
    private String nianmiyushe3SF;
    private String nianmiyushe5SF;
    private String buchangzhongliang1SF;
    private String buchangzhongliang2SF;
    private String buchangzhongliang3SF;
    private String buchangzhongliang4SF;
    private String buchangzhongliang5SF;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(FixActivity.this);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    private void hideTimeBack() {
        binding.center.daojishi.setVisibility(View.INVISIBLE);
        binding.center.time.setVisibility(View.INVISIBLE);
        binding.center.back.setVisibility(View.INVISIBLE);
    }
    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fix);
        setupUI(findViewById(R.id.content));
        binding.imgSave.setOnClickListener(this);
        binding.center.back.setOnClickListener(this);
        phone = getIntent().getStringExtra("phone");
        getViewSf();
        setView();
        hideTimeBack();
        binding.tvIpdizhi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    // 在这里编写自己想要实现的功能
                }
                return false;
            }
        });
    }

    //获取每个view的保存值
    private void getViewSf() {
        dukachuankouSF = (String) SharePref.get(FixActivity.this, API.duka, "");
        dukabotelvSF = (String) SharePref.get(FixActivity.this, API.dukabote, "");
        jiliangchuankouSF = (String) SharePref.get(FixActivity.this, API.jiliang, "");
        jiliangbotelvSF = (String) SharePref.get(FixActivity.this, API.jiliangbote, "");
        wulichuankouSF = (String) SharePref.get(FixActivity.this, API.wuli, "");
        wulibotelvSF = (String) SharePref.get(FixActivity.this, API.wulibote, "");
        guozaibaohuSF = (String) SharePref.get(FixActivity.this, API.guozaibaohu, "");
        qidongshichaSF = (String) SharePref.get(FixActivity.this, API.qidongshicha, "");
        zhongduanbianhaoSF = (int) SharePref.get(FixActivity.this, API.id, 0);
        ipcodeSF = (String) SharePref.get(FixActivity.this, API.ip, "");
//        bencijiaruguliangSF = (String) SharePref.get(FixActivity.this, API.bencijiaruguliang, "");
        chumilvSF = (String) SharePref.get(FixActivity.this, API.chumilv, "");
        weihudianhuaSF = (String) SharePref.get(FixActivity.this, API.weihudianhua, "");
        nianmiyushe1SF = (String) SharePref.get(FixActivity.this, API.yushenianmishijian1, "");
        nianmiyushe2SF = (String) SharePref.get(FixActivity.this, API.yushenianmishijian2, "");
        nianmiyushe3SF = (String) SharePref.get(FixActivity.this, API.yushenianmishijian3, "");
        nianmiyushe4SF = (String) SharePref.get(FixActivity.this, API.yushenianmishijian4, "");
        nianmiyushe5SF = (String) SharePref.get(FixActivity.this, API.yushenianmishijian5, "");
        buchangzhongliang1SF = (String) SharePref.get(FixActivity.this, API.buchangzhongliang1, "");
        buchangzhongliang2SF = (String) SharePref.get(FixActivity.this, API.buchangzhongliang2, "");
        buchangzhongliang3SF = (String) SharePref.get(FixActivity.this, API.buchangzhongliang3, "");
        buchangzhongliang4SF = (String) SharePref.get(FixActivity.this, API.buchangzhongliang4, "");
        buchangzhongliang5SF = (String) SharePref.get(FixActivity.this, API.buchangzhongliang5, "");
        xiagusudu1SF = (String) SharePref.get(FixActivity.this, API.xiagusudu1, "");
        xiagusudu2SF = (String) SharePref.get(FixActivity.this, API.xiagusudu2, "");
        xiagusudu3SF = (String) SharePref.get(FixActivity.this, API.xiagusudu3, "");
    }

    //之前有改动view参数，这些做限制
    private void setView() {
        if (!guozaibaohuSF.isEmpty()) {
            binding.tvGuozaibaohu.setText(guozaibaohuSF);
        }
        if (!qidongshichaSF.isEmpty()) {
            binding.tvQidongshicha.setText(qidongshichaSF);
        }
        if (zhongduanbianhaoSF != 0) {
            binding.tvZhongduanbianhao.setText(zhongduanbianhaoSF + "");
        }
        if (!ipcodeSF.isEmpty()) {
            binding.tvIpdizhi.setText(ipcodeSF);
        }
        if (!dukachuankouSF.isEmpty()) {
            binding.tvDukachuankou.setText(dukachuankouSF);
        }
        if (!dukabotelvSF.isEmpty()) {
            binding.botelvduka.setText(dukabotelvSF);
        }
//        if (!bencijiaruguliangSF.isEmpty()) {
//            binding.tvBencijiaruguliang.setText(bencijiaruguliangSF);
//        }
        if (!jiliangchuankouSF.isEmpty()) {
            binding.tvJiliangchuankou.setText(jiliangchuankouSF);
        }
        if (!jiliangbotelvSF.isEmpty()) {
            binding.botelvjiliang.setText(jiliangbotelvSF);
        }
        if (!wulichuankouSF.isEmpty()) {
            binding.tvWulichuankou.setText(wulichuankouSF);
        }
        if (!wulibotelvSF.isEmpty()) {
            binding.botelvwuli.setText(wulibotelvSF);
        }
        if (!chumilvSF.isEmpty()) {
            binding.tvChumilv.setText(chumilvSF);
        }
        if (!weihudianhuaSF.isEmpty()) {
            binding.etWeihudianhua.setText(weihudianhuaSF);
        }
        if (!nianmiyushe1SF.isEmpty()) {
            binding.tvYusheshijian1.setText(nianmiyushe1SF);
        }
        if (!nianmiyushe2SF.isEmpty()) {
            binding.tvYusheshijian2.setText(nianmiyushe2SF);
        }
        if (!nianmiyushe3SF.isEmpty()) {
            binding.tvYusheshijian3.setText(nianmiyushe3SF);
        }
        if (!nianmiyushe4SF.isEmpty()) {
            binding.tvYusheshijian4.setText(nianmiyushe4SF);
        }
        if (!nianmiyushe5SF.isEmpty()) {
            binding.tvYusheshijian5.setText(nianmiyushe5SF);
        }
        if (!buchangzhongliang1SF.isEmpty()) {
            binding.tvBuchangzhongliang1.setText(buchangzhongliang1SF);
        }
        if (!buchangzhongliang2SF.isEmpty()) {
            binding.tvBuchangzhongliang2.setText(buchangzhongliang2SF);
        }
        if (!buchangzhongliang3SF.isEmpty()) {
            binding.tvBuchangzhongliang3.setText(buchangzhongliang3SF);
        }
        if (!buchangzhongliang4SF.isEmpty()) {
            binding.tvBuchangzhongliang4.setText(buchangzhongliang4SF);
        }
        if (!buchangzhongliang5SF.isEmpty()) {
            binding.tvBuchangzhongliang5.setText(buchangzhongliang5SF);
        }
        if (!xiagusudu1SF.isEmpty()) {
            binding.tvXiagusudu1.setText(xiagusudu1SF);
        }
        if (!xiagusudu2SF.isEmpty()) {
            binding.tvXiagusudu2.setText(xiagusudu2SF);
        }
        if (!xiagusudu3SF.isEmpty()) {
            binding.tvXiagusudu3.setText(xiagusudu3SF);
        }
    }

    private void findView() {
        zhongduanbianhao = binding.tvZhongduanbianhao.getText().toString();
        weihudianhua = binding.etWeihudianhua.getText().toString();
        bencijiaruguliang = binding.tvBencijiaruguliang.getText().toString();
        chumilv = binding.tvChumilv.getText().toString();
        guozaibaohu = binding.tvGuozaibaohu.getText().toString();
        qidongshicha = binding.tvQidongshicha.getText().toString();
        ipcode = binding.tvIpdizhi.getText().toString();
        dukachuankou = binding.tvDukachuankou.getText().toString();
        dukabotelv = binding.botelvduka.getText().toString();
        jiliangchuankou = binding.tvJiliangchuankou.getText().toString();
        jiliangbotelv = binding.botelvjiliang.getText().toString();
        wulichuankou = binding.tvWulichuankou.getText().toString();
        wulibotelv = binding.botelvwuli.getText().toString();
        xiagusudu1 = binding.tvXiagusudu1.getText().toString();
        xiagusudu2 = binding.tvXiagusudu2.getText().toString();
        xiagusudu3 = binding.tvXiagusudu3.getText().toString();
        nianmiyushe1 = binding.tvYusheshijian1.getText().toString();
        nianmiyushe2 = binding.tvYusheshijian2.getText().toString();
        nianmiyushe4 = binding.tvYusheshijian4.getText().toString();
        nianmiyushe3 = binding.tvYusheshijian3.getText().toString();
        nianmiyushe5 = binding.tvYusheshijian5.getText().toString();
        buchangzhongliang1 = binding.tvBuchangzhongliang1.getText().toString();
        buchangzhongliang2 = binding.tvBuchangzhongliang2.getText().toString();
        buchangzhongliang3 = binding.tvBuchangzhongliang3.getText().toString();
        buchangzhongliang4 = binding.tvBuchangzhongliang4.getText().toString();
        buchangzhongliang5 = binding.tvBuchangzhongliang5.getText().toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_save:
                uploadDevice();
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    private void shareSave() {
        SharePref.put(FixActivity.this, API.duka, dukachuankou);
        SharePref.put(FixActivity.this, API.dukabote, dukabotelv);
        SharePref.put(FixActivity.this, API.jiliang, jiliangchuankou);
        SharePref.put(FixActivity.this, API.jiliangbote, jiliangbotelv);
        SharePref.put(FixActivity.this, API.wuli, wulichuankou);
        SharePref.put(FixActivity.this, API.wulibote, wulibotelv);
        SharePref.put(FixActivity.this, API.guozaibaohu, guozaibaohu);
        SharePref.put(FixActivity.this, API.qidongshicha, qidongshicha);
        SharePref.put(FixActivity.this, API.id, Integer.parseInt(zhongduanbianhao));
        SharePref.put(FixActivity.this, API.ip, ipcode);
//        SharePref.put(FixActivity.this, API.bencijiaruguliang, bencijiaruguliang);
        SharePref.put(FixActivity.this, API.chumilv, chumilv);
        SharePref.put(FixActivity.this, API.weihudianhua, weihudianhua);
        SharePref.put(FixActivity.this, API.yushenianmishijian1, nianmiyushe1);
        SharePref.put(FixActivity.this, API.yushenianmishijian2, nianmiyushe2);
        SharePref.put(FixActivity.this, API.yushenianmishijian3, nianmiyushe3);
        SharePref.put(FixActivity.this, API.yushenianmishijian4, nianmiyushe4);
        SharePref.put(FixActivity.this, API.yushenianmishijian5, nianmiyushe5);
        SharePref.put(FixActivity.this, API.buchangzhongliang1, buchangzhongliang1);
        SharePref.put(FixActivity.this, API.buchangzhongliang2, buchangzhongliang2);
        SharePref.put(FixActivity.this, API.buchangzhongliang3, buchangzhongliang3);
        SharePref.put(FixActivity.this, API.buchangzhongliang4, buchangzhongliang4);
        SharePref.put(FixActivity.this, API.buchangzhongliang5, buchangzhongliang5);
        SharePref.put(FixActivity.this, API.xiagusudu1, xiagusudu1);
        SharePref.put(FixActivity.this, API.xiagusudu2, xiagusudu2);
        SharePref.put(FixActivity.this, API.xiagusudu3, xiagusudu3);
    }

    //上传device设备编号
    private void uploadDevice() {
        findView();
        Map<String, String> map = new HashMap<>();
        if (isStringtoInt(zhongduanbianhao)) {
            if (zhongduanbianhao.isEmpty()) {
                Utils.toast(FixActivity.this, "请输入终端编号");
                return;
            }
            if (weihudianhua.isEmpty()) {
                Utils.toast(FixActivity.this, "请输入维护电话");
                return;
            }
            if (bencijiaruguliang.isEmpty()) {
                Utils.toast(FixActivity.this, "请输入本次加入谷量");
                return;
            }
            if (chumilv.isEmpty()) {
                Utils.toast(FixActivity.this, "请输入出米率");
                return;
            }
            if (guozaibaohu.isEmpty()){
                Utils.toast(FixActivity.this, "请输入过载保护");
                return;
            }
            if (qidongshicha.isEmpty()){
                Utils.toast(FixActivity.this, "请输入启动时差");
                return;
            }
            map.put("deviceId", zhongduanbianhao);
            map.put("maintainPhone", weihudianhua);
            if (phone != null) {
                map.put("icCardChipId", phone);
            }
            map.put("riceAddCount", bencijiaruguliang);
            map.put("riceProduceRate", chumilv);
            if (!guozaibaohu.isEmpty()) {
                map.put("overloadProcted", guozaibaohu);
            }
            if (!qidongshicha.isEmpty()) {
                map.put("startDiff", qidongshicha);
            }
            if (!ipcode.isEmpty()) {
                map.put("ip", ipcode);
            }
            if (!dukachuankou.isEmpty()) {
                map.put("readCardConsole", dukachuankou);
            }
            if (!dukabotelv.isEmpty()) {
                map.put("readCardBandrate", dukabotelv);
            }
            if (!jiliangchuankou.isEmpty()) {
                map.put("sensorConsole", jiliangchuankou);
            }
            if (!jiliangbotelv.isEmpty()) {
                map.put("sensorBandrate", jiliangbotelv);
            }
            if (!wulichuankou.isEmpty()) {
                map.put("phyCosole", wulichuankou);
            }
            if (!wulibotelv.isEmpty()) {
                map.put("phyBandrate", wulibotelv);
            }
            if (!xiagusudu1.isEmpty()) {
                map.put("speed1", xiagusudu1);
            }
            if (!xiagusudu2.isEmpty()) {
                map.put("speed2", xiagusudu2);
            }
            if (!xiagusudu3.isEmpty()) {
                map.put("speed3", xiagusudu3);
            }
            if (!nianmiyushe1.isEmpty()) {
                map.put("riceOutTime1", nianmiyushe1);
            }
            if (!nianmiyushe2.isEmpty()) {
                map.put("riceOutTime2", nianmiyushe2);
            }
            if (!nianmiyushe3.isEmpty()) {
                map.put("riceOutTime3", nianmiyushe3);
            }
            if (!nianmiyushe4.isEmpty()) {
                map.put("riceOutTime4", nianmiyushe4);
            }
            if (!nianmiyushe5.isEmpty()) {
                map.put("riceOutTime5", nianmiyushe5);
            }
            if (!buchangzhongliang1.isEmpty()) {
                map.put("riceOutOffset1", buchangzhongliang1);
            }
            if (!buchangzhongliang2.isEmpty()) {
                map.put("riceOutOffset2", buchangzhongliang2);
            }
            if (!buchangzhongliang3.isEmpty()) {
                map.put("riceOutOffset3", buchangzhongliang3);
            }
            if (!buchangzhongliang4.isEmpty()) {
                map.put("riceOutOffset4", buchangzhongliang4);
            }
            if (!buchangzhongliang5.isEmpty()) {
                map.put("riceOutOffset5", buchangzhongliang5);
            }
        } else {
            Utils.toast(this, "终端编号请输入数字");
            return;
        }
        final String url = API.BASE_URL + API.DEVICE_UPLOAD;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        final RequestParams requestParams = new RequestParams();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        Utils.log("url:" + url + "\nrequestParams:" + requestParams);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log("json:" + json);
                BaseObject baseObj = GsonUtils.getInstance().fromJson(json, BaseObject.class);
                if (baseObj.isSuccess()) {
                    shareSave();
                    Intent intent = getIntent();
                    intent.putExtra("data", Integer.parseInt(zhongduanbianhao));
                    setResult(2, intent);
                    finish();
                } else {
                    if (baseObj != null && baseObj.getError() != null) {
                        Utils.toast(FixActivity.this, baseObj.getError());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(FixActivity.this, "网络请求失败");
            }
        });
    }

    private boolean isStringtoInt(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    //初始化头部和底部
    @Override
    public void initHeaderBottom() {
        ConfigModel configModel = GsonUtils.getInstance().fromJson((String) SharePref.get(this, "config", ""), ConfigModel.class);
        Config config = null;
        if (configModel != null) {
            config = configModel.getData();
        }
        if (config != null) {
            if (config.getIsShowDeviceLogo() == 0) {
                Utils.Glide(this, config.getDeviceLogoImgUrl(), binding.center.imgTopIcon);
            }
//            Utils.Glide(this, config.getLogoImgUrl(), binding.center.imgTopIcon);
//            binding.layoutBottom.tvTitle.setText(config.getLogoTitle());
            binding.center.tvPhone.setText("技术支持：" + config.getSupportCall());
        }
    }
}
