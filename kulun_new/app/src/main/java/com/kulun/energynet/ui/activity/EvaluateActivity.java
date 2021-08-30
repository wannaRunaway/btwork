package com.kulun.energynet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kulun.energynet.MainApp;
import com.kulun.energynet.R;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class EvaluateActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private static EvaluateActivity mInstance = null;

    private TextView mBack;
    private TextView mCommit;
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

    private String serialNum;
    private String stationName;
    private List<TextView> list = new ArrayList<TextView>();

    private String exchangeRecordId;
    private Integer starsNumber = 0;
    private String content = "";
    private String tagSerial = "";
    private String tagContent = "";

    public static EvaluateActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        setContentView(R.layout.evaluate);

        Intent intent = getIntent();

        exchangeRecordId = intent.getStringExtra("exchangeRecordId");
        serialNum = intent.getStringExtra("serialNum");
        stationName = intent.getStringExtra("stationName");

        bindView();
    }

    public void bindView() {
        mBack = (TextView) this.findViewById(R.id.evaluate_back);
        mCommit = (TextView) this.findViewById(R.id.evaluate_commit);
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
        mCommit.setOnClickListener(this);
        mStar1.setOnClickListener(this);
        mStar2.setOnClickListener(this);
        mStar3.setOnClickListener(this);
        mStar4.setOnClickListener(this);
        mStar5.setOnClickListener(this);
        mTag1.setOnClickListener(this);
        mTag2.setOnClickListener(this);
        mTag3.setOnClickListener(this);
        mTag4.setOnClickListener(this);

        list.add(mStar1);
        list.add(mStar2);
        list.add(mStar3);
        list.add(mStar4);
        list.add(mStar5);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.evaluate_back:
                finish();
                break;

            case R.id.evaluate_star1:
                starSelected(1);
                break;

            case R.id.evaluate_star2:
                starSelected(2);
                break;

            case R.id.evaluate_star3:
                starSelected(3);
                break;

            case R.id.evaluate_star4:
                starSelected(4);
                break;

            case R.id.evaluate_star5:
                starSelected(5);
                break;

            case  R.id.evaluate_tag1:
                tagSelected(mTag1);
                break;

            case  R.id.evaluate_tag2:
                tagSelected(mTag2);
                break;

            case  R.id.evaluate_tag3:
                tagSelected(mTag3);
                break;

            case  R.id.evaluate_tag4:
                tagSelected(mTag4);
                break;

            case R.id.evaluate_commit:
                tagSerial = "";
                tagContent =  "";
                if(starsNumber == 0) {
                    ToastUtil.showToast(mContext, "请选择评分");
                    return;
                }
                if(mTag1.isSelected()) {
                    tagSerial = tagSerial + 1;
                    tagContent = tagContent + mTag1.getText();
                }
                if(mTag2.isSelected()) {
                    tagSerial = tagSerial + "," + 2;
                    tagContent = tagContent + "," + mTag2.getText();
                }
                if(mTag3.isSelected()) {
                    tagSerial = tagSerial + "," + 3;
                    tagContent = tagContent + "," + mTag3.getText();
                }
                if(mTag4.isSelected()) {
                    tagSerial = tagSerial + "," + 4;
                    tagContent = tagContent + "," + mTag4.getText();
                }
                if(tagSerial.startsWith(",")) {
                    tagSerial = tagSerial.substring(1);
                    tagContent = tagContent.substring(1);
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        String url = API.BASE_URL + API.URL_COMMENT_SAVE;
                        RequestParams params = new RequestParams();
                        params.add("exchangeRecordId", exchangeRecordId);
                        params.add("starsNumber", starsNumber.toString());
                        params.add("content", content);
                        params.add("tagSerial", tagSerial);
                        params.add("tagContent", tagContent);

                        final AsyncHttpClient client = new AsyncHttpClient();
                        //保存cookie，自动保存到了sharepreferences
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                        client.setCookieStore(myCookieStore);
                        client.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                String json = new String(response);
                                Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json comment = " + json);
                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                Integer code = obj.get("code").getAsInt();
                                if(code == 0) {
                                    ToastUtil.showToast(mContext, "评价提交成功");
                                    Constants.whichFragment = 3;

                                    //跳转到消费记录界面
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            MainApp.getInstance().getApplicationContext().startActivity(intent);
                                        }
                                    }, 2000);

                                } else {
                                    ToastUtil.showToast(mContext, obj.get("msg").getAsString());
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                break;

            default:
                break;

        }
    }

    public void starSelected(Integer grade) {
        if(grade <= 2) {
            tagAllClear();
        }
        for(int i=0;i<grade;i++) {
            list.get(i).setBackgroundResource(R.drawable.star);
        }
        for(int i=grade;i<list.size();i++) {
            list.get(i).setBackgroundResource(R.drawable.star2);
        }
        switch (grade) {
            case 1:
                starsNumber = 1;
                mStarText.setText("不满意，很失望");
                content = "不满意，很失望";
                break;
            case 2:
                starsNumber = 2;
                mStarText.setText("不满意，有点失望");
                content = "不满意，有点失望";
                break;
            case 3:
                starsNumber = 3;
                mStarText.setText("一般");
                content = "一般";
                break;
            case 4:
                starsNumber = 4;
                mStarText.setText("满意");
                content = "满意";
                break;
            case 5:
                starsNumber = 5;
                mStarText.setText("很满意");
                content = "很满意";
                break;
            default:
                break;
        }
    }

    public void tagSelected(TextView view) {
        if(starsNumber == 0) {
            ToastUtil.showToast(mContext, "请先选择星级");
            return;
        } else if(starsNumber <= 2) {
            ToastUtil.showToast(mContext, "两星以下不能选择标签");
            return;
        } else {
            if(view.isSelected()) {
                view.setSelected(false);
                view.setTextColor(Color.parseColor("#8A8A8A"));
            } else {
                view.setSelected(true);
                view.setTextColor(Color.parseColor("#FFD700"));
            }
        }
    }

    public void tagAllClear() {
        mTag1.setSelected(false);
        mTag2.setSelected(false);
        mTag3.setSelected(false);
        mTag4.setSelected(false);
        mTag1.setTextColor(Color.parseColor("#8A8A8A"));
        mTag2.setTextColor(Color.parseColor("#8A8A8A"));
        mTag3.setTextColor(Color.parseColor("#8A8A8A"));
        mTag4.setTextColor(Color.parseColor("#8A8A8A"));
    }
}
