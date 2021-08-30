package com.botann.charing.pad.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.charging.pad.R;
import com.cengalabs.flatui.FlatUI;

public abstract class BaseActivityM extends AppCompatActivity implements BaseActivityIntfM
{
    private static final String TAG = BaseActivityM.class.getSimpleName();
//    private int mCheckedMenuItem = Menu.NONE;

    private Button btnBack;

    //上次按下返回键的系统时间
    private long lastBackTime = 0;
    //当前按下返回键的系统时间
    private long currentBackTime = 0;

    private final int APP_THEME = R.array.grass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(shouldLoadViewLayout());
        ActivityController.addActivity(this);
        checkForAccount();
        // converts the default values to dp to be compatible with different screen sizes
        FlatUI.initDefaultValues(this);

        // Default theme should be set before content view is added
        FlatUI.setDefaultTheme(APP_THEME);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setupBackBtn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
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
                btnBackPressed();
            }
        });
    }

    protected void setTitle(String title){
        super.setTitle(title);
        TextView textView = (TextView) findViewById(R.id.tvMainTitle);
        textView.setText(title);
    }

    protected void btnBackPressed(){
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



    protected void checkForAccount()
    {
//        if(!User.getInstance().hasUser())
//        {
//            Intent backToLogin = new Intent(getApplicationContext(), LoginActivity.class);
//            backToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(backToLogin);
//        }
    }

    public void logoutAndClearAccount()
    {
//        User.getInstance().deleteUser();
//        checkForAccount();
//        Toast.makeText(getApplicationContext(), "成功退出当前用户", Toast.LENGTH_SHORT).show();
    }

}
