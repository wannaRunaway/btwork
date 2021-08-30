package com.kulun.energynet.ui.activity.refund;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kulun.energynet.BaseObjectString;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityRefundBinding;
import com.kulun.energynet.model.RefundFailModel;
import com.kulun.energynet.model.User;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

/**
 * created by xuedi on 2019/12/9
 */
public class ReFundChongdianActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityRefundBinding binding;
    private String myMoney;
    private String msg = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_refund);
        binding.title.back.setOnClickListener(this);
        binding.title.title.setText("充电账户申请退款");
        binding.imgSubmit.setOnClickListener(this);
        binding.tvRefund.setText("退款须知\n\n· 您的退款金额到账时间由各充值渠道决定，请耐心等待，约1~3个工作日到账；" +
                "\n\n· 退款是按充值记录逐笔对款，可能会生成多条退款记录；\n\n· 根据各充值渠道规则，只能退3个月的充值金额。");
        loadInfo();
    }

    //加载信息
    private void loadInfo() {
        final String url = API.BASE_URL + API.URL_REFUND_CHECK;
        final RequestParams params = new RequestParams();
        /**
         * accountId[int]	是	账户id
         * type[int]	是	固定值4
         */
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("type", "4");
        final AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ReFundChongdianActivity.this);
        client.setCookieStore(myCookieStore);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Utils.log(url, params, json);
                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                if (obj.get("success").getAsBoolean()){
                    BaseObjectString object = GsonUtils.getInstance().fromJson(json, BaseObjectString.class);
                    binding.tvMoney.setVisibility(View.VISIBLE);
                    myMoney = object.getContent();
                    binding.tvMoney.setText("可退款余额："+object.getContent()+"元");
                }else {
                    RefundFailModel model = GsonUtils.getInstance().fromJson(json, RefundFailModel.class);
                    if (model.getMsg() != null) {
                        Utils.toast(ReFundChongdianActivity.this, model.getMsg());
                        msg = model.getMsg();
                    }
                    binding.tvMoney.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String json = new String(responseBody);
                Utils.log(url, params, json);
                binding.tvMoney.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.img_submit:
                submit();
                break;
                default:
                    break;
        }
    }

    private void submit() {
        if (!msg.equals("")){
            Utils.toast(ReFundChongdianActivity.this, msg);
            return;
        }
        String money = binding.etMoney.getText().toString();
        if (money == null || money.equals("")){
            Utils.toast(ReFundChongdianActivity.this, "请输入退款金额");
            return;
        }
        try {
            double etmoney = Double.parseDouble(money);
            double mymoney = Double.parseDouble(myMoney);
            if (etmoney > mymoney){
                Utils.toast(ReFundChongdianActivity.this, "退款金额不能大于可退款金额");
                return;
            }
        }catch (Exception e){
        }
        if (money.indexOf(".")!=-1) {
            String[] moneyarray = money.split("\\.");
            if (moneyarray[1].length()>2){
                Utils.toast(ReFundChongdianActivity.this, "退款金额最多只能保留两位小数");
                return;
            }
        }
        String reason = binding.etRefund.getText().toString();
        /**
         * accountId[int]	是	账户id
         * type[int]	是	固定值4
         * refundMoney[double]	是	退款金额
         * refundReason[string]	是	退款原因，可空闲
         */
        final String url = API.BASE_URL + API.URL_REFUND_QUERY;
        final RequestParams params = new RequestParams();
        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        params.add("type", "4");
        params.add("refundMoney", money);
        params.add("refundReason", reason);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(ReFundChongdianActivity.this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url,params,json);
                BaseObjectString baseObjectString = GsonUtils.getInstance().fromJson(json, BaseObjectString.class);
                if (baseObjectString.isSuccess()){
                    Utils.toast(ReFundChongdianActivity.this, "申请退款完成");
                    finish();
                }else {
                    if (baseObjectString.getMsg() != null) {
                        Utils.toast(ReFundChongdianActivity.this, baseObjectString.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String json = new String(responseBody);
                Utils.log(url,params,json);
            }
        });
    }
}
