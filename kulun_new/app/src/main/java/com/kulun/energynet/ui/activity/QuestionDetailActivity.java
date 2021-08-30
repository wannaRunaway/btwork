package com.kulun.energynet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.kulun.energynet.R;
import com.kulun.energynet.adapter.MyQuestionAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class QuestionDetailActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private static QuestionDetailActivity mInstance = null;

    private TextView mBack;
    private EditText mDescription;
    private TextView mSerialNo;
    private TextView mStation;
    private GridView mPhotos;
    private MyQuestionAdapter adapter;
    private TextView tv_feed_back;
    private String serialNum;
    private String stationName;
    private String data;

    private ArrayList<String> mSelectPath = new ArrayList<String>();

    public static QuestionDetailActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        setContentView(R.layout.question_detail);

        Intent intent = getIntent();

        serialNum = intent.getStringExtra("serialNum");
        stationName = intent.getStringExtra("stationName");
        data = intent.getStringExtra("data");
        JsonObject obj = new JsonParser().parse(data).getAsJsonObject();

        bindView(obj);

        adapter = new MyQuestionAdapter(QuestionDetailActivity.this, mSelectPath);
        mPhotos.setAdapter(adapter);

        mPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("i+l: " + i + "-" + l);
                System.out.println("path: " + mSelectPath.get(i));
                Intent intent = new Intent(QuestionDetailActivity.this,
                        QuestionPhotoActivity.class);
                intent.putExtra("path", mSelectPath.get(i));
                startActivity(intent);
            }
        });
    }

    public void bindView(JsonObject obj) {
        mBack = (TextView) this.findViewById(R.id.question_back);
        mDescription = (EditText) this.findViewById(R.id.question_description);
        mSerialNo = (TextView) this.findViewById(R.id.question_serial_no);
        mStation = (TextView) this.findViewById(R.id.question_station);
        mPhotos = (GridView) this.findViewById(R.id.question_photos);

        mSerialNo.setText("订单号：" + serialNum);
        mStation.setText(stationName);
        mDescription.setText("问题："+obj.get("content").getAsString());
        //int    status:处理状态 0 未处理 1 已处理
        //String handle_content:处理内容
        tv_feed_back = (TextView) findViewById(R.id.tv_feed_back);
        tv_feed_back.setText("处理回复："+obj.get("handleContent").getAsString());
        if (obj.get("status").getAsInt() == 1){
            tv_feed_back.setVisibility(View.VISIBLE);
        }else {
            tv_feed_back.setVisibility(View.INVISIBLE);
        }
        mBack.setOnClickListener(this);
        JsonArray pictureList = obj.get("pictureList").getAsJsonArray();
        if(pictureList.size() > 0) {
            for(int i=0;i<pictureList.size();i++) {
                String path = pictureList.get(i).toString();
                mSelectPath.add(path.substring(1, path.length() - 1));
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_back:
                finish();
                break;

            default:
                break;

        }
    }

}
