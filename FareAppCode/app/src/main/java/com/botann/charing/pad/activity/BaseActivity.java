package com.botann.charing.pad.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charing.pad.callbacks.SGFinishCallBack;
import com.botann.charing.pad.callbacks.SGOnAlertClick;
import com.botann.charing.pad.utils.SnackBarUtils;
import com.cengalabs.flatui.FlatUI;

import com.botann.charging.pad.R;
import com.botann.charing.pad.utils.ToastUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityIntf
{
    private static final String TAG = BaseActivity.class.getSimpleName();
//    private int mCheckedMenuItem = Menu.NONE;

    private Button btnBack;

    //上次按下返回键的系统时间
    private long lastBackTime = 0;
    //当前按下返回键的系统时间
    private long currentBackTime = 0;

    private final int APP_THEME = R.array.grass;
    protected Activity mContext;
    private  KProgressHUD progressHud = null;
    protected String scanedQRCodeStr;

    protected KProgressHUD showProgressHud(String message) {
        if (progressHud == null) {
            progressHud =  KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .show();
        } else {
            progressHud.setLabel(message).show();
        }
        return progressHud;
    }
    protected void hideProgressHud () {
        progressHud.dismiss();
    }

    /**
     * 加载网络数据
     */
    protected void loadDatas(final Boolean isRefresh, final SGFinishCallBack callBack) {}



    protected void toast (String info) {
        ToastUtil.showToast(mContext, info);
    }

    /**
     * snackbar提示
     */
    protected void snackbar (String message){
        SnackBarUtils.showSnackbar(mContext, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        mContext = this;
//        checkForAccount();
        // converts the default values to dp to be compatible with different screen sizes
        FlatUI.initDefaultValues(this);

        // Default theme should be set before content view is added
        FlatUI.setDefaultTheme(APP_THEME);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(viewLayout());
        setupToolbar();
        setupBackBtn();
        initView();

//        PgyCrashManager.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }


    protected void alert (@NonNull String message) {
        alert(null,message,null,null,null);
    }

    protected void alert (@NonNull String message, final SGOnAlertClick onAlertClick) {
        alert(message,"确定",onAlertClick);
    }
    protected void alert (@NonNull String message, Object confirms, final SGOnAlertClick onAlertClick) {
        alert(null,message,null,confirms,onAlertClick);
    }

    protected void alert (String title, @NonNull String message, String cancel, Object confirms, final SGOnAlertClick onAlertClick) {
        if (title == null) title = "提示";
        if (cancel == null) {
            if (confirms == null) {
                cancel = "知道了";
            } else {
                cancel = "取消";
            }
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onAlertClick != null) {
                    onAlertClick.onClick(dialog,which+2);
                } else {
                    dialog.dismiss();
                }
            }
        };
        dialog.setNegativeButton(cancel, onClickListener);
        if (confirms != null) {
            if (confirms instanceof String ) {
                dialog.setPositiveButton((String)confirms, onClickListener);
            } else {
                String [] confirmTitles = (String[]) confirms;
                for (int i = 0; i < confirmTitles.length; i++) {
                    dialog.setPositiveButton(confirmTitles[i], onClickListener);
                }
            }
        }
        dialog.show();
    }

    /**
     * 隐藏编辑键盘
     */
    protected void hideKeybord (TextView textView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textView,InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void setupBackBtn() {
        btnBack = (Button) findViewById(R.id.btn_left);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonLeftPressed();
            }
        });
    }

    protected void setTitle(String title){
        super.setTitle(title);
        TextView textView = (TextView) findViewById(R.id.tvMainTitle);
        textView.setText(title);
    }

    protected void onButtonLeftPressed(){
        finish();
    }


    @Override
    public void onBackPressed()
    {
        currentBackTime = System.currentTimeMillis();
        //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
        if(currentBackTime - lastBackTime > 2 * 1000){
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            lastBackTime = currentBackTime;
        }else{ //如果两次按下的时间差小于2秒，则退出程序
            finish();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // if nav drawer is opened, hide the action items
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }



    public final Button findButtonById(int id){
        return (Button) findViewById(id);
    }

    public final TextView findTextViewById(int id){
        return (TextView) findViewById(id);
    }

    public final EditText findEditTextById(int id) {
        return (EditText) findViewById(id);
    }


}
