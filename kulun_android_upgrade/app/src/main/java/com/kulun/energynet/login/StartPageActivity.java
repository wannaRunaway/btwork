package com.kulun.energynet.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kulun.energynet.R;
import com.kulun.energynet.main.MainActivity;

public class StartPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartPageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },700);
    }
}
