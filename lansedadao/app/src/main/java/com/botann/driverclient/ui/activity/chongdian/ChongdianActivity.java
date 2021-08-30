package com.botann.driverclient.ui.activity.chongdian;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityChongdianBinding;
import com.botann.driverclient.model.BindCar;
import com.botann.driverclient.model.User;
import com.botann.driverclient.model.chongdian.ChongdianzhuangInfoModer;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.BaseActivity;
import com.botann.driverclient.ui.activity.login.BindCarActivity;
import com.botann.driverclient.utils.GsonUtils;
import com.botann.driverclient.utils.Utils;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.io.Serializable;
import java.util.List;
/**
 * created by xuedi on 2019/7/2
 */
public class ChongdianActivity extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, QRCodeReaderView.OnQRCodeReadListener {
    private ActivityChongdianBinding binding;
//    private List<BindCar> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chongdian);
//        SharedPreferences preferences = getSharedPreferences("data", this.MODE_PRIVATE);
//        list = GsonUtils.getInstance().fromJson(preferences.getString("chargeBindCarList", ""), new TypeToken<List<BindCar>>() {
//        }.getType());
//        if (list.size() == 0) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("您尚未绑定车辆，请先添加车辆");
//            builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Intent intent = new Intent(ChongdianActivity.this, BindCarActivity.class);
//                    intent.putExtra("isregister", false);
//                    startActivity(intent);
//                }
//            });
//            builder.show();
//        }
        binding.tvBack.setOnClickListener(this);
        binding.tvCertain.setOnClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (binding.qrdecoderview != null) {
            binding.qrdecoderview.startCamera();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (binding.qrdecoderview != null) {
            binding.qrdecoderview.stopCamera();
        }
    }
    @Override
    protected void onDestroy() {
        if (binding.qrdecoderview != null) {
            binding.qrdecoderview.stopCamera();
        }
        super.onDestroy();
    }
    private void initQRCodeReaderView() {
//        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
//        resultTextView = (TextView) findViewById(R.id.result_text_view);
//        flashlightCheckBox = (CheckBox) findViewById(R.id.flashlight_checkbox);
//        enableDecodingCheckBox = (CheckBox) findViewById(R.id.enable_decoding_checkbox);
//        pointsOverlayView = (PointsOverlayView) findViewById(R.id.points_overlay_view);
        binding.qrdecoderview.setAutofocusInterval(2000L);
        binding.qrdecoderview.setOnQRCodeReadListener(this);
        binding.qrdecoderview.setBackCamera();
        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                binding.qrdecoderview.setTorchEnabled(isChecked);
            }
        });
//        enableDecodingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                qrCodeReaderView.setQRDecodingEnabled(isChecked);
//            }
//        });
        binding.qrdecoderview.startCamera();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_certain://确定
                if (binding.etCode.getText().toString().isEmpty()) {
                    Utils.toast(ChongdianActivity.this, "请输入充电桩编码");
                    return;
                }
                loadCode();
                break;
            default:
                break;
        }
    }

    //上传信息
    private void loadCode() {
        final String url = API.BASE_URL + API.CHONGDIAN_INFOR;
        /**
         * operatorId
         * [string]	是	运营商编号
         * connectorId
         * [string]	是	充电枪编号
         */
        final RequestParams requestParams = new RequestParams();
//        requestParams.add("operatorId", );
        requestParams.add("accountId", String.valueOf(User.getInstance().getAccountId()));
        requestParams.add("connectorId", binding.etCode.getText().toString());
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(url, requestParams, json);
                ChongdianzhuangInfoModer moder = GsonUtils.getInstance().fromJson(json, ChongdianzhuangInfoModer.class);
                if (moder.isSuccess()) {
                    Intent intent = new Intent(ChongdianActivity.this, ChongdianZhongActivity.class);
                    intent.putExtra("info", moder.getContent());
                    intent.putExtra("isdingdan", false);
//                    intent.putExtra("list", (Serializable) list);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChongdianActivity.this);
                    builder.setMessage(moder.getMsg());
                    builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.toast(ChongdianActivity.this, API.error_internet);
            }
        });
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
//        binding.overlayview.setPoints(points);
        String message = "";
        if (text.indexOf("=") != -1){
            String splite[] = text.split("=");
            message = splite[1];
        }else {
            message = text;
        }
        if (message != null) {
            binding.etCode.setText(message);
        }
        if (binding.etCode.getText().toString().isEmpty()) {
            Utils.toast(ChongdianActivity.this, "请输入充电桩编码");
            return;
        }
        loadCode();
//        binding.etCode.setText(text);
    }
}
