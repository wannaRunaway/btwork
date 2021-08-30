package com.kulun.energynet.ui.activity.login;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kulun.energynet.R;
import com.kulun.energynet.db.SharedPreferencesHelper;
import com.kulun.energynet.model.User;
import com.kulun.energynet.model.UserInfo;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.network.api.GetLoginResponse;
import com.kulun.energynet.network.api.GetMessageResponse;
import com.kulun.energynet.network.api.GetVersion;
import com.kulun.energynet.ui.activity.MainActivity;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etPhone;
    private EditText etPassword;
    private Button btnLogin;
    private Context mContext;
    private TextView tv_title;
    private static LoginActivity mInstance = null;
    private TextView tv_forget_password, tv_newuser_register, xieyizhengce;

    /**
     * isShake摇一摇切换 线上取消摇一摇
     */
//    private boolean isShake = false;
//    private Vibrator vibrator;
//    private SensorManager sensorManager;
//    private Sensor accelerateSensor;
    public static LoginActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        initView();
        GetVersion.getVersion(this);
        requestParams();
        if (!(Boolean) SharePref.get(this,API.isfirst,false)){
            showpromotDialog();
        }
    }

    private void initView() {
        setContentView(R.layout.login_main);
        etPhone = (EditText) findViewById(R.id.account_username);
        etPassword = (EditText) findViewById(R.id.account_validation);
        btnLogin = (Button) findViewById(R.id.login);
        tv_title = (TextView) findViewById(R.id.title);
        if (checkForAccount() == 0) {
            SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
            etPhone.setText(preferences.getString("phone", ""));
            etPassword.setText(preferences.getString("password", ""));
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                if (checkCredential(phone, password) == 0) {
//                    GetConsumeResponse.getConsumeResponse(LoginActivity.this, 11, 1, 20);
                    GetLoginResponse.getLoginResponse(LoginActivity.this, phone, password);
                } else {
                    ToastUtil.showToast(mContext, "用户名和密码不能为空");
                }
            }
        });
        tv_forget_password = findViewById(R.id.tv_forget_password);
        tv_newuser_register = findViewById(R.id.tv_newuser_register);
        tv_forget_password.setOnClickListener(this);
        tv_newuser_register.setOnClickListener(this);
        xieyizhengce = findViewById(R.id.xieyihezhengce);
        xieyizhengce.setOnClickListener(this);
        /**
         * 摇一摇切换，找到震动service
         */
//        if (isShake) {
//            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        }
    }

    //权限请求
    private List<String> permissionList_Map = new ArrayList<>();
    private void requestParams() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList_Map.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissionList_Map.isEmpty()) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    permissionList_Map.toArray(new String[permissionList_Map.size()]), 2);
        }
    }

    private int checkForAccount() {
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        if (!token.equals("")) {
            return 0;
        }
        return 1;
    }

    private void createNewAccount(String phone, String password, User user) {
//        Constants.accountId = user.getAccountId();
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.putInt("accountId", user.getAccountId());
        editor.putString("token", user.getToken());
        editor.commit();
    }

    /**
     * "account": "2018091300000016",
     * "name": "测试司机f1",
     * "balance": 0.00,
     * "couponNum": 0,
     * "messageNum": 0,
     * "city": "杭州市",
     * "appCanRecharge": 0,
     * "bindCarList": [{
     * "id": 12,
     * "plate_number": "苏E05EV8",
     * "status": 1
     * }]    * 	},
     */
//    private void createNewUserInfo(UserInfo userInfo) {
//        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//        editor.putString("account", userInfo.getAccount());
//        editor.putString("name", userInfo.getName());
//        editor.putFloat("balance", userInfo.getBalance());
//        editor.putInt("couponNum", userInfo.getCouponNum());
//        editor.putInt("messageNum", userInfo.getMessageNum());
//        editor.putString("city", userInfo.getCity());
//        editor.putInt("appCanRecharge", userInfo.getAppCanRecharge());
//        SharedPreferencesHelper.getInstance(LoginActivity.this).putAccountString("bindCarList", GsonUtils.getInstance().toJson(userInfo.getBindCarList()));
//        editor.putBoolean("hasChargeAccount" ,userInfo.isHasChargeAccount());
//        editor.putBoolean("hasAuthAccount", userInfo.isHasAuthAccount());
//        editor.putFloat("chargeBalance" ,userInfo.getChargeBalance());
//        SharedPreferencesHelper.getInstance(LoginActivity.this).putAccountString("chargeBindCarList", GsonUtils.getInstance().toJson(userInfo.getChargeBindCarList()));
//        editor.commit();
//    }

    public void doWithLoginResult(String message, User user, boolean status) {
        if (status) {
            if (user.getAccountId() != 0) {
//                Constants.isTeam = 0;
                ToastUtil.showToast(mContext, "登录成功");
//                GetInfoResponse.getInfoResponse(LoginActivity.this, user.getAccountId().toString());
//                GetConsumeResponse.getConsumeResponse(LoginActivity.this, user.getAccountId(), 1, 20);
//                GetRechargeResponse.getRechargeResponse(LoginActivity.this, user.getAccountId(), 1, 20);
                GetMessageResponse.getPostMessage(LoginActivity.this, user.getAccountId(), 1, 20);
                createNewAccount(etPhone.getText().toString(), etPassword.getText().toString(), user);
                Intent toMain = new Intent(mContext, MainActivity.class);
                toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toMain);
            }
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void doWithConsumeRecordsResult(String message, String res, boolean status) {
        if (status) {
            //createConsumeInfoList(res);
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void doWithUserInfoResult(String message, UserInfo userInfo, boolean status) {
        if (status) {
//            createNewUserInfo(userInfo);
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void doWithRechargeRecordsResult(String message, String res, boolean status) {
        if (status) {
            //createRechargeInfoList(res);
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void doWithErrorResult(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private int checkCredential(String phone, String password) {
        if (!phone.isEmpty() && !password.isEmpty()) {
            return 0;
        }
        return 1;
    }

    /**
     * 实现点击空白处，软键盘消失事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_forget_password:
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_newuser_register:
                User.getInstance().setAccountId(0);//注册accountid清0
                intent = new Intent(LoginActivity.this, HuandianRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.xieyihezhengce:
//                intent = new Intent(LoginActivity.this, UseProtocolActivity.class);
                showpromotDialog();
                break;
            default:
                break;
        }
    }

    private AlertDialog alertDialog;
    private void showpromotDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_prompt, null);
        TextView tv_user_protocl = view.findViewById(R.id.t4); //协议
        TextView tv_zhengce = view.findViewById(R.id.t5);
        TextView tv_disagree = view.findViewById(R.id.tv_disagree);
        TextView tv_agree = view.findViewById(R.id.tv_agree);
        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                SharePref.put(LoginActivity.this, API.isfirst, true);
            }
        });
        tv_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_user_protocl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UseProtocolActivity.class);
                startActivity(intent);
            }
        });
        tv_zhengce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        alertDialog = builder.setView(view).create();
        alertDialog.show();
    }
    /**
     * 摇一摇切换
     */
//    @Override
//    protected void onStart() {
//        super.onStart();
//        /**
//         * 摇一摇初始化和注册
//         */
//        if (isShake) {
//            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//            if (sensorManager != null) {
//                accelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//                if (accelerateSensor != null) {
//                    sensorManager.registerListener(this, accelerateSensor, SensorManager.SENSOR_DELAY_UI);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        if (isShake) {
//            if (sensorManager != null) {
//                sensorManager.unregisterListener(this);
//            }
//        }
//        super.onPause();
//    }
    /**
     * 加速传感器
     */
//    boolean ischange = true;
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            //获取三个方向值
//            float[] values = sensorEvent.values;
//            float x = values[0];
//            if (Math.abs(x) > 17) {
//                vibrator.vibrate(300);
//                ischange = !ischange;
//                Log.d("xue", "摇一摇震动了");
//                if (ischange){
//                    tv_title.setText("蓝色大道");
//                    Constants.BASE_URL="http://p.botann.com:8191";
//                }else {
//                    tv_title.setText("蓝色大道测试版");
//                    Constants.BASE_URL="http://121.196.237.125:9008";
//                }
//            }
//        }
//    }
//    /**
//     *加速传感器
//     */
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//    }
}
