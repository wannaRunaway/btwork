package com.botann.charing.pad.activity.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.botann.charging.pad.R;

import com.botann.charing.pad.base.SGFetchModel;
import com.botann.charing.pad.base.URLParams;
import com.botann.charing.pad.model.User;
import com.botann.charing.pad.base.API;
import com.botann.charing.pad.base.SGHTTPManager;
import com.botann.charing.pad.activity.Main.MainActivity;
import com.botann.charing.pad.utils.MD5Util;
import com.botann.charing.pad.utils.ToastUtil;


/**
 * Created by mengchenyun on 2016/11/5.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnClearUsername;
    private Button btnClearPassword;
    private Button btnLogout;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
    }

    private void initView() {
        setContentView(R.layout.login_main);
//        setupToolbar();
//        setTitle(R.string.app_name);
        btnClearUsername = (Button) findViewById(R.id.btn_clear_username);
        btnClearUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("");
            }
        });
        btnClearPassword = (Button) findViewById(R.id.btn_clear_password);
        btnClearPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.setText("");
            }
        });
        etUsername = (EditText) findViewById(R.id.et_username);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    btnClearUsername.setVisibility(View.VISIBLE);
                } else {
                    btnClearUsername.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (etUsername.getText().length()>0) btnClearUsername.setVisibility(View.VISIBLE);
                } else {
                    btnClearUsername.setVisibility(View.INVISIBLE);
                }
            }
        });
        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    btnClearPassword.setVisibility(View.VISIBLE);
                } else {
                    btnClearPassword.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (etPassword.getText().length()>0) btnClearPassword.setVisibility(View.VISIBLE);
                } else {
                    btnClearPassword.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(checkCredential(etUsername.getText().toString(), etPassword.getText().toString()) == 0) {
//                    GetLoginResponse.getLoginResponse(LoginActivity.this, "0", username, MD5Util.MD5Encode(password, "UTF-8"));
//                    GetLoginResponse.getLoginResponse(LoginActivity.this, "0", username, password);
//                    doWithLoginResult(true);
                    URLParams params = new URLParams();
                    params.put("username",username);
                    params.put("password",MD5Util.MD5Encode(password));
                    btnLogin.setEnabled(false);
                    SGHTTPManager.POST(API.URL_LOGIN, params, new SGHTTPManager.SGRequestCallBack() {
                        @Override
                        public void onResponse(Boolean isSuccess, String userInfo, SGFetchModel fetchModel) {
                            btnLogin.setEnabled(true);
                            if (isSuccess) {
                                User user = fetchModel.modelOfContent(User.class);
                                ToastUtil.showToast(mContext, "????????????");
                                createNewAccount(etUsername.getText().toString(), etPassword.getText().toString(),user.getId());
                                Intent toMain = new Intent(mContext, MainActivity.class);
                                toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(toMain);
                            } else {
                                ToastUtil.showToast(mContext, userInfo);
                            }
                        }
                    });
                } else {
                    ToastUtil.showToast(mContext, "??????????????????????????????");
                }
            }
        });
//        btnLogout = (Button) findViewById(R.id.btn_left);
//        btnLogout.setVisibility(View.GONE);
        if (!User.shared().getUsername().isEmpty()) {
            etUsername.setText(User.shared().getUsername());
        }
    }

    private void createNewAccount(String username, String password,Integer userId) {
        User.shared().saveOrUpdateUser(userId,username, MD5Util.MD5Encode(password, "UTF-8"),"");
    }

    public void doWithLoginResult(Integer userId, boolean status) {
        if(status) {
            ToastUtil.showToast(mContext, "????????????");
            createNewAccount(etUsername.getText().toString(), etPassword.getText().toString(),userId);
            Intent toMain = new Intent(mContext, MainActivity.class);
            toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(toMain);
        } else {
            Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }


    private int checkCredential(String username, String password) {
        if(!username.isEmpty() && !password.isEmpty()) {
            return 0;
        }
        return 1;
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    /**
     * ?????????????????????????????????????????????
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // ???????????????????????????View????????????????????????EditText??????????????????????????????????????????????????????????????????
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ??????EditText???????????????????????????????????????????????????????????????????????????????????????????????????EditText??????????????????
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // ??????EditText????????????????????????
                return false;
            } else {
                return true;
            }
        }
        // ??????????????????EditText?????????????????????????????????????????????????????????????????????EditView????????????????????????????????????????????????
        return false;
    }

    /**
     * ??????????????????????????????????????????
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
}
