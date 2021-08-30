package com.botann.driverclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class EvaluateDetailActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private static EvaluateDetailActivity mInstance = null;

    private TextView mBack;
    private TextView mSerialNo;
    private TextView mStation;
    private TextView mStar1;
    private TextView mStar2;
    private TextView mStar3;
    private TextView mStar4;
    private TextView mStar5;
    private TextView mStarText;
    private TextView mTag1;
    private TextView mTag2;
    private TextView mTag3;
    private TextView mTag4;

    private String exchangeRecordId;
    private String serialNum;
    private String stationName;
    private String data;
    private List<TextView> list = new ArrayList<TextView>();

    private static Integer grade = 0;

    public static EvaluateDetailActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        setContentView(R.layout.evaluate_detail);

        Intent intent = getIntent();
        exchangeRecordId = intent.getStringExtra("exchangeRecordId");
        serialNum = intent.getStringExtra("serialNum");
        stationName = intent.getStringExtra("stationName");
        data = intent.getStringExtra("data");
        System.out.println("data: " + data);
        JsonObject comment = new JsonParser().parse(data).getAsJsonObject();

        bindView(comment);
    }

    public void bindView(JsonObject comment) {
        mBack = (TextView) this.findViewById(R.id.evaluate_back);
        mSerialNo = (TextView) this.findViewById(R.id.evaluate_serial_no);
        mStation = (TextView) this.findViewById(R.id.evaluate_station);
        mStar1 = (TextView) this.findViewById(R.id.evaluate_star1);
        mStar2 = (TextView) this.findViewById(R.id.evaluate_star2);
        mStar3 = (TextView) this.findViewById(R.id.evaluate_star3);
        mStar4 = (TextView) this.findViewById(R.id.evaluate_star4);
        mStar5 = (TextView) this.findViewById(R.id.evaluate_star5);
        mStarText = (TextView) this.findViewById(R.id.evaluate_star_text);
        mTag1 = (TextView) this.findViewById(R.id.evaluate_tag1);
        mTag2 = (TextView) this.findViewById(R.id.evaluate_tag2);
        mTag3 = (TextView) this.findViewById(R.id.evaluate_tag3);
        mTag4 = (TextView) this.findViewById(R.id.evaluate_tag4);

        mSerialNo.setText("订单号：" + serialNum);
        mStation.setText(stationName);

        mBack.setOnClickListener(this);

        list.add(mStar1);
        list.add(mStar2);
        list.add(mStar3);
        list.add(mStar4);
        list.add(mStar5);

        starSelected(comment.get("starsNumber").getAsInt());
        tagSelected(comment.get("tagSerial").getAsString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.evaluate_back:
                finish();
                break;

            default:
                break;

        }
    }

    public void starSelected(Integer grade) {
        for(int i=0;i<grade;i++) {
            list.get(i).setBackgroundResource(R.drawable.star);
        }
        for(int i=grade;i<list.size();i++) {
            list.get(i).setBackgroundResource(R.drawable.star2);
        }
        switch (grade) {
            case 1:
                mStarText.setText("不满意，很失望");
                break;
            case 2:
                mStarText.setText("不满意，有点失望");
                break;
            case 3:
                mStarText.setText("一般");
                break;
            case 4:
                mStarText.setText("满意");
                break;
            case 5:
                mStarText.setText("很满意");
                break;
            default:
                break;
        }
    }

    public void tagSelected(String tagSerial) {
        if(tagSerial.indexOf("1") >= 0) {
            mTag1.setSelected(true);
            mTag1.setTextColor(Color.parseColor("#FFD700"));
        }
        if(tagSerial.indexOf("2") >= 0) {
            mTag2.setSelected(true);
            mTag2.setTextColor(Color.parseColor("#FFD700"));
        }
        if(tagSerial.indexOf("3") >= 0) {
            mTag3.setSelected(true);
            mTag3.setTextColor(Color.parseColor("#FFD700"));
        }
        if(tagSerial.indexOf("4") >= 0) {
            mTag4.setSelected(true);
            mTag4.setTextColor(Color.parseColor("#FFD700"));
        }
    }
}
