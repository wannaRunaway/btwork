package com.btkj.millingmachine.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.btkj.millingmachine.R;

/**
 * created by xuedi on 2019/4/28
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
        initHeaderBottom();
    }

    public abstract void initView();

    public abstract void initHeaderBottom();
}
