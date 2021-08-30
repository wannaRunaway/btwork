package com.botann.driverclient.ui.activity.login;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.databinding.ActivityUploadCarFinalBinding;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.MainActivity;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.Utils;

/**
 * created by xuedi on 2019/12/24
 */
public class UploadCarFinalActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityUploadCarFinalBinding binding;
    private boolean islogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_car_final);
        binding.header.title.setText("上传司机车辆信息");
        binding.header.back.setOnClickListener(this);
        binding.confim.setOnClickListener(this);
        islogin = getIntent().getBooleanExtra(API.islogin, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
            case R.id.confim:
                if (islogin){ //.已经登录
                    Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    MainApp.getInstance().getApplicationContext().startActivity(intent);
                    Constants.whichFragment = 0;
                }else {
                    Utils.toast(UploadCarFinalActivity.this, "注册成功，请登录");
                    Intent intent = new Intent(UploadCarFinalActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
