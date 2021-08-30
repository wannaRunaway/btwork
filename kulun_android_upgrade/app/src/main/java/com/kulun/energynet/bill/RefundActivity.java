package com.kulun.energynet.bill;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityRefundHuandianBinding;
import com.kulun.energynet.login.ForgetPasswordActivity;
import com.kulun.energynet.login.VeriCodeLoginActivity;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.model.UserLogin;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.requestparams.ResponseString;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.Utils;

/**
 * created by xuedi on 2020/3/18
 */
public class RefundActivity extends BaseActivity implements View.OnClickListener {
    private ActivityRefundHuandianBinding binding;
    private double money;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refund_huandian);
        binding.title.left.setOnClickListener(this);
        binding.title.title.setText("退款");
        binding.bottom.finish.setText("立即退款");
        binding.bottom.finish.setOnClickListener(this);
        Drawable leftDrawable = getResources().getDrawable(R.mipmap.icon_rmb);
        leftDrawable.setBounds(0, 0, 30,45);
        binding.etMoney.setCompoundDrawables(leftDrawable, null, null, null);
        binding.tvRefund.setText("· 您的退款金额到账时间由各充值渠道决定，请耐心等待，约1~3个工作日到账；" +
                "\n· 退款是按充值记录逐笔对款，可能会生成多条退款记录；\n· 根据各充值渠道规则，只能退3个月的充值金额。");
        binding.etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2 + 1);
                        binding.etMoney.setText(s);
                        binding.etMoney.setSelection(s.length()); //光标移到最后
                    }
                }
//                未输入数字的情况下输入小数点时，个位数字自动补零
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    binding.etMoney.setText(s);
                    binding.etMoney.setSelection(2);
                }
//                如果文本以"0"开头并且第二个字符不是"."，不允许继续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        binding.etMoney.setText(s.subSequence(0, 1));
                        binding.etMoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loadMoney();
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    //加载信息
    private void loadMoney() {
        new MyRequest().pay(API.refundamount, false, null, RefundActivity.this, null, null, new ResponseString() {
            @Override
            public void response(String s, JsonObject jsonObject) {
                try {
                    money = Double.parseDouble(s);
                    binding.etMoney.setHint("可退款金额"+money+"元");
                }catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.finish:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        if (Utils.isFastClick()) {
            Utils.snackbar(RefundActivity.this, "点击过快");
            return;
        }
        if (TextUtils.isEmpty(binding.etMoney.getText().toString())) {
            Utils.snackbar(this,"请输入退款金额");
            return;
        }
        try {
            double etmoney = Double.parseDouble(binding.etMoney.getText().toString());
            if (etmoney > money) {
                Utils.snackbar(this,  "输入金额不能大于可退款金额");
                return;
            }
        } catch (Exception e) {
        }
        if (String.valueOf(money).indexOf(".") != -1) {
            String[] moneyarray = String.valueOf(money).split("\\.");
            if (moneyarray[1].length() > 2) {
                Utils.snackbar(this, "退款金额最多只能保留两位小数");
                return;
            }
        }
        String reason = "";
        if (!TextUtils.isEmpty(binding.etRefund.getText().toString())){
            reason =  binding.etRefund.getText().toString();
        }
        try {
            double uploadMoney = Double.parseDouble(binding.etMoney.getText().toString());
            uploadRefund(uploadMoney, reason);
        }catch (Exception e){
        }
    }

    private void uploadRefund(double money, String reason) {
        String json = JsonSplice.leftparent+JsonSplice.yin+"amount"+JsonSplice.yinandmao+money+JsonSplice.dou
                +JsonSplice.yin+"reason"+JsonSplice.yinandmao+JsonSplice.yin+reason+JsonSplice.yin+JsonSplice.rightparent;
        new MyRequest().spliceJson(API.refundapply, true, json, RefundActivity.this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(RefundActivity.this, "申请成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },500);
            }
        });
    }
}

