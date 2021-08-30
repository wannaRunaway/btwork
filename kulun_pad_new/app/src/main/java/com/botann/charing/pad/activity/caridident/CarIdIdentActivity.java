package com.botann.charing.pad.activity.caridident;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.base.URLParams;

public class CarIdIdentActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView tv_title, tv_takeph, tv_infor;
    private CheckBox cb_flashselect;
    private EasyPRPreView sf_scanarea;
    private Button bt_finish, bt_back;
    private EditText et_carnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carid);
        initView();
    }

    public void initView() {
        tv_title = (TextView) findViewById(R.id.tvMainTitle);
        bt_finish = (Button) findViewById(R.id.btn_right);
        cb_flashselect = (CheckBox) findViewById(R.id.cb_flash);
        sf_scanarea = (EasyPRPreView) findViewById(R.id.preview);
        et_carnum = (EditText) findViewById(R.id.et_num);
        bt_back = (Button) findViewById(R.id.btn_left);
        tv_takeph = (TextView) findViewById(R.id.tv_takephoto);
        tv_infor = (TextView) findViewById(R.id.tv_infor);
        bt_finish.setText("完成");
        bt_finish.setVisibility(View.VISIBLE);
        tv_title.setText("车牌扫描");
//        hidesoft(et_carnum);
        initListener();
    }

    private void initListener() {
        bt_finish.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        tv_takeph.setOnClickListener(this);
        cb_flashselect.setOnCheckedChangeListener(this);
        sf_scanarea.setRecognizedListener(new EasyPRPreSurfaceView.OnRecognizedListener() {
            @Override
            public void onRecognized(String result) {
                if (null == result || result.equals("")) {
                    Toast.makeText(CarIdIdentActivity.this, "请重新扫描车牌", Toast.LENGTH_SHORT).show();
                    tv_infor.setText("请重新扫描车牌");
                    et_carnum.setText("");
                } else {
                    tv_infor.setText("车牌号:");
                    et_carnum.setText(result);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sf_scanarea != null) {
            sf_scanarea.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sf_scanarea != null) {
            sf_scanarea.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sf_scanarea != null) {
            sf_scanarea.onDestroy();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                if (et_carnum.getText().toString() == null || et_carnum.getText().toString().equals("")) {
                    Toast.makeText(CarIdIdentActivity.this, "车牌号码为空，请重新识别", Toast.LENGTH_SHORT).show();
                    return;
                }
                isCaridExist();
                break;
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_takephoto:
                /**
                 * 开始识别
                 */
                sf_scanarea.recognize();
                break;
            default:
                break;
        }
    }

    private void isCaridExist() {
        URLParams urlParams = new URLParams();
        urlParams.put("plateNumber", et_carnum.getText().toString());
        SGHTTPManager.POST(API.URL_CARID_EXIST, urlParams, new SGHTTPManager.SGRequestCallBack() {
            @Override
            public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                if (isSuccess){
                    backtoIntentActivity();
                }else {
                    Toast.makeText(CarIdIdentActivity.this, userInfo, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void backtoIntentActivity() {
        Intent intent = new Intent();
        intent.putExtra("carid", et_carnum.getText().toString());
        setResult(1002, intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.cb_flash) {
            CameraManager.get().switchFlashLight(CarIdIdentActivity.this);
        }
    }

    private void hidesoft(EditText v){
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
