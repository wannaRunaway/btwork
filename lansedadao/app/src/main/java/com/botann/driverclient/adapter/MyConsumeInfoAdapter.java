package com.botann.driverclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.model.consume.ConsumeInfo;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.EvaluateActivity;
import com.botann.driverclient.ui.activity.EvaluateDetailActivity;
import com.botann.driverclient.ui.activity.QuestionActivity;
import com.botann.driverclient.ui.activity.QuestionDetailActivity;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.DateUtils;
import com.botann.driverclient.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Orion on 2017/7/21.
 */
public class MyConsumeInfoAdapter extends RecyclerView.Adapter<MyConsumeInfoAdapter.MyViewHolder> {
    private static DecimalFormat df = new DecimalFormat("######0.00");
    private Context mContext;
    private List<ConsumeInfo> list;
//    private boolean isCustomerNotNull;

    public MyConsumeInfoAdapter(List<ConsumeInfo> list, Context context) {
        mContext = context;
        this.list = list;
//        this.isCustomerNotNull = isCustomerNotNull;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_consume, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mOrderNum;
        TextView mSiteName;
        TextView mCarBrand;
        TextView mConsumeDate;
        TextView mTotalMiles;
        TextView mReferMiles;
        TextView mFare;
        TextView mEvaluate;
        TextView mQuestion, mCustomer;
        TextView tv_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            mOrderNum = (TextView) itemView.findViewById(R.id.order_num);
            mSiteName = (TextView) itemView.findViewById(R.id.site_name);
            mCarBrand = (TextView) itemView.findViewById(R.id.car_brand);
            mConsumeDate = (TextView) itemView.findViewById(R.id.consume_date);
            mTotalMiles = (TextView) itemView.findViewById(R.id.total_miles);
            mReferMiles = (TextView) itemView.findViewById(R.id.refer_miles);
            mFare = (TextView) itemView.findViewById(R.id.fare);
            mEvaluate = (TextView) itemView.findViewById(R.id.evaluate);
            mQuestion = (TextView) itemView.findViewById(R.id.question);
            mCustomer = (TextView) itemView.findViewById(R.id.tv_customer);
            tv_type = itemView.findViewById(R.id.tv_type);
        }

        public void bind(final ConsumeInfo content) {
//            if (isCustomerNotNull) {
//                mCustomer.setVisibility(View.VISIBLE);
//                mCustomer.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toCustomer(content);
//                    }
//                });
//            } else {
//            }
            mCustomer.setVisibility(View.INVISIBLE);
            String type = "";
            //bigType int 1充电类型 0换电类型；
            switch (content.getBigType()) {
                case 0:
                    type = "换电记录";
                    mQuestion.setVisibility(View.VISIBLE);
                    mEvaluate.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    type = "充电记录";
                    mQuestion.setVisibility(View.GONE);
                    mEvaluate.setVisibility(View.GONE);
                    break;
                default:
                    break;

            }
            tv_type.setText(type);
            String dingdan = content.getSerialNo();
            try {
                if (dingdan.indexOf("-") != -1) {
                    String[] strings = dingdan.split("-");
                    mOrderNum.setText("订单号：" + strings[0]);
                } else {
                    mOrderNum.setText("订单号：" + dingdan);
                }
            } catch (Exception e) {
                mOrderNum.setText("订单号：" + dingdan);
            }
            mSiteName.setText("站点名称：" + content.getStationName());
            mCarBrand.setText("车牌号：" + content.getPlateNumber());
            mConsumeDate.setText(DateUtils.stampToDate(Long.parseLong(content.getCreateTime())));
            if (content.getBigType() == 0) {
                mTotalMiles.setText("总里程：" + content.getTotalMile() + "km");
                mReferMiles.setText("计费里程：" + content.getChangeMile() + "km");
            } else {
                mTotalMiles.setText("充电时长：" + DateUtils.getTimeString(content.getChargeTime()));
                mReferMiles.setText("充电电量：" + content.getTotalPower() + "度");
            }
            //1里程套餐计费；4 按次套餐; 5 ruleType=5时     leftMile剩余金额和monthMile包月金额  单位分
            if (content.getMonthMile() == 0 && content.getRefundMoney() == 0) {
                if (content.getRuleType() == 4 || content.getRuleType() == 1) {
                    mFare.setText("实际支付/优惠券/余额/套餐剩余    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile());
                } else if (content.getRuleType() == 5){
                    mFare.setText("实际支付/优惠券/余额/套餐剩余    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile()/100.00);
                } else {
                    mFare.setText("实际支付/优惠券/余额    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0));
                }
            } else if (content.getMonthMile() != 0 && content.getRefundMoney() == 0) {
                 if (content.getRuleType() == 5){
                     mFare.setText("实际支付/优惠券/余额/套餐剩余    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile()/100.00);
                 }else {
                     mFare.setText("实际支付/优惠券/余额/套餐剩余    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile());
                 }
            } else if (content.getMonthMile() == 0 && content.getRefundMoney() != 0) {
                if (content.getRuleType() == 4 || content.getRuleType() == 1) {
                    mFare.setText("实际支付/优惠券/余额/套餐剩余/退款金额    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile() + "/" + (content.getRefundMoney() != 0 ? df.format(content.getRefundMoney()) : 0));
                }else if (content.getRuleType() == 5){
                    mFare.setText("实际支付/优惠券/余额/套餐剩余/退款金额    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile()/100.00 + "/" + (content.getRefundMoney() != 0 ? df.format(content.getRefundMoney()) : 0));
                }else {
                    mFare.setText("实际支付/优惠券/余额/退款金额    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + (content.getRefundMoney() != 0 ? df.format(content.getRefundMoney()) : 0));
                }
            } else if (content.getMonthMile() != 0 && content.getRefundMoney() != 0) {
                if (content.getRuleType() == 5){
                    mFare.setText("实际支付/优惠券/余额/套餐剩余/退款金额    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile()/100.00 + "/" + (content.getRefundMoney() != 0 ? df.format(content.getRefundMoney()) : 0));
                }else {
                    mFare.setText("实际支付/优惠券/余额/套餐剩余/退款金额    " + (content.getRealFare() != 0 ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != 0 ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != 0 ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile() + "/" + (content.getRefundMoney() != 0 ? df.format(content.getRefundMoney()) : 0));
                }
            }
            if (content.getCommentStatus() == 0) {
                mEvaluate.setText("评价");
            } else {
                mEvaluate.setText("查看评价");
            }
            if (content.getQuestionStatus() == 0) {
                mQuestion.setText("资费疑问");
            } else {
                mQuestion.setText("查看问题");
            }
            mEvaluate.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (content.getCommentStatus() == 0) {
                        Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), EvaluateActivity.class);
                        intent.putExtra("exchangeRecordId", String.valueOf(content.getId()));
                        intent.putExtra("serialNum", content.getSerialNo());
                        intent.putExtra("stationName", content.getStationName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainApp.getInstance().getApplicationContext().startActivity(intent);
                    } else {
                        //查找评价内容
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                String url = API.BASE_URL + API.URL_COMMENT_DETAIL;
                                RequestParams params = new RequestParams();
                                params.add("exchangeRecordId", String.valueOf(content.getId()));

                                final AsyncHttpClient client = new AsyncHttpClient();
                                //保存cookie，自动保存到了sharepreferences
                                PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                                client.setCookieStore(myCookieStore);
                                client.post(url, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                        String json = new String(response);
                                        Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json comment detail = " + json);
                                        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                        JsonObject data = obj.get("content").getAsJsonObject();
                                        String res = data.toString();

                                        Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), EvaluateDetailActivity.class);
                                        intent.putExtra("exchangeRecordId", String.valueOf(content.getId()));
                                        intent.putExtra("serialNum", content.getSerialNo());
                                        intent.putExtra("stationName", content.getStationName());
                                        intent.putExtra("data", res);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        MainApp.getInstance().getApplicationContext().startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                        Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                        Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    }
                }
            }));

            mQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (content.getQuestionStatus() == 0) {
                        Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), QuestionActivity.class);
                        intent.putExtra("exchangeRecordId", String.valueOf(content.getId()));
                        intent.putExtra("serialNum", content.getSerialNo());
                        intent.putExtra("stationName", content.getStationName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainApp.getInstance().getApplicationContext().startActivity(intent);
                    } else {
                        //查找提问
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                String url = API.BASE_URL + API.URL_QUESTION_DETAIL;
                                RequestParams params = new RequestParams();
                                params.add("exchangeRecordId", String.valueOf(content.getId()));
                                final AsyncHttpClient client = new AsyncHttpClient();
                                //保存cookie，自动保存到了sharepreferences
                                PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                                client.setCookieStore(myCookieStore);
                                client.post(url, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                        String json = new String(response);
                                        Log.d(Utils.TAG, "onSuccess json question detail = " + json);
                                        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                        if (obj.get("code").getAsInt() != 0) {
                                            Toast.makeText(MainApp.getInstance().getApplicationContext(), obj.get("msg").getAsString(), Toast.LENGTH_SHORT);
                                            return;
                                        }
                                        JsonObject data = obj.get("content").getAsJsonObject();
                                        String res = data.toString();

                                        Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), QuestionDetailActivity.class);
                                        intent.putExtra("serialNum", content.getSerialNo());
                                        intent.putExtra("stationName", content.getStationName());
                                        intent.putExtra("data", res);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        MainApp.getInstance().getApplicationContext().startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                        Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                        Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }

        /**
         * 联系客服
         *
         * @param content
         */
//        private void toCustomer(ConsumeInfo content) {
//            String title = "时空电动客服";
//            /**
//             * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
//             * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
//             * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
//             */
//            String sourceUrl = "换电时间:" + DateUtils.stampToDate(content.getCreateDate());
//            String sourceTitle = "订单号:" + content.getSerialNum();
//            String ss = "";
//            if (content.getMonthMile() == null || content.getMonthMile() == 0) {
//                ss = "实际支付/优惠券/余额 " + (content.getRealFare() != null ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != null ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != null ? df.format(content.getBalance()) : 0);
//            } else {
//                ss = "实际支付/优惠券/余额/本月剩余里程 " + (content.getRealFare() != null ? df.format(content.getRealFare()) : 0) + "/" + (content.getCoupon() != null ? df.format(content.getCoupon()) : 0) + "/" + (content.getBalance() != null ? df.format(content.getBalance()) : 0) + "/" + content.getLeftMile();
//            }
//            String sourceCustom = "车牌:" + content.getCarNumber() + ",计费里程:" + content.getRealMile()
//                    + "," + ss + ",站点" + content.getStationName();
//            ConsultSource source = new ConsultSource(sourceUrl, sourceTitle, sourceCustom);
//            ProductDetail.Builder builder = new ProductDetail.Builder();
//            builder.setTitle(sourceTitle);
//            builder.setNote(sourceUrl);
//            builder.setDesc(sourceCustom);
//            builder.setShow(1);
//            source.productDetail = builder.build();
//            YSFUserInfo userInfo = new YSFUserInfo();
//            userInfo.userId = String.valueOf(Constants.accountId);
//            SharedPreferences preferences = mContext.getSharedPreferences("data", mContext.MODE_PRIVATE);
//            String phone = preferences.getString("phone", "");
//            userInfo.data = "[{\"key\":\"phone\", \"value\":" + "\"" + phone + "\"" + "}]";
//            Log.d("xuedi", userInfo.data + "输出的data");
//            Unicorn.setUserInfo(userInfo);
//            /**
//             * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
//             * 如果返回为false，该接口不会有任何动作
//             *
//             * @param context 上下文
//             * @param title   聊天窗口的标题
//             * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
//             */
//            Unicorn.openServiceActivity(mContext, title, source);
//        }
    }
}
