package com.botann.driverclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.botann.driverclient.R;
import com.botann.driverclient.adapter.MyQuestionAdapter;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class QuestionPhotoActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private static QuestionPhotoActivity mInstance = null;

    private TextView mBack;
    private ImageView mImage;

    private String path = "";

    public static QuestionPhotoActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        setContentView(R.layout.question_photo_bigger);

        Intent intent = getIntent();

        path = intent.getStringExtra("path");
        System.out.println(path);
        bindView();

    }

    public void bindView() {
        mBack = (TextView) this.findViewById(R.id.question_photo_back);
        mImage = (ImageView) this.findViewById(R.id.photo_bigger);

        mBack.setOnClickListener(this);
        Glide.with(mContext).load(path).into(mImage);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_photo_back:
                finish();
                break;

            default:
                break;

        }
    }

}
