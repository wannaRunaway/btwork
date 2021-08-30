package com.kulun.energynet.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kulun.energynet.MainApp;
import com.kulun.energynet.QrcodeDialog;
import com.kulun.energynet.R;
import com.kulun.energynet.adapter.BaseAdapter;
import com.kulun.energynet.adapter.ILoadCallback;
import com.kulun.energynet.adapter.MemberInfoAdapter;
import com.kulun.energynet.adapter.MyMemberInfoAdapter;
import com.kulun.energynet.adapter.OnLoad;
import com.kulun.energynet.model.MemberInfo;
import com.kulun.energynet.model.RewardInfo;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.utils.Constants;
import com.kulun.energynet.utils.EncryptionUtil;
import com.kulun.energynet.utils.QRCodeUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.apache.http.Header;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Orion on 2017/9/21.
 */
public class TeamFragment extends Fragment implements View.OnClickListener {
    private static SimpleDateFormat format =  new SimpleDateFormat("MM-dd");
    private String name;
    private String teamName;
    private Integer yesterdayCurrent;
    private String serverRank;
    private String totalCurrent;
    private Long catchDate;
    private Integer rank;
    private String reward;
    private String monthBonusName;
    private Double monthBonus;
    private String monthRecruitBounusName;
    private Double monthRecruitBounus;
    private String monthManageBonusName;
    private Double monthManageBonus;

    private TextView mChiefName;
    private TextView mTeamName;
    private TextView mYesterdayCurrent;
    private TextView mServerRank;
    private TextView mTotalCurrent;
    private TextView mChief;
    private TextView mQrcode;
    private TextView mMyBonus;
    private TextView mMemberInfo;
    private TextView mMonthBonus;
    private TextView mMonthBonusNum;
    private TextView mMonthRecruitBonus;
    private TextView mMonthRecruitBonusNum;
    private TextView mMonthManageBonus;
    private TextView mMonthManageBonusNum;

    RefreshLayout teamRefresh;
    RecyclerView memberInfo;
    LinearLayout bonusInfo;
    BaseAdapter mAdapter;
    int loadCount;
    int totalRecords;

    //奖金
    private List<RewardInfo> rewardList = new ArrayList<RewardInfo>();
    //成员信息数据
    private List<MemberInfo> memberInfoList = new ArrayList<MemberInfo>();

    public static TeamFragment newInstance(String name, String teamName, Integer yesterdayCurrent, String serverRank, String totalCurrent, Long catchDate, Integer rank, String memberInfoList, String rewardList) {
        TeamFragment fragment = new TeamFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("teamName", teamName);
        bundle.putInt("yesterdayCurrent", yesterdayCurrent);
        bundle.putString("serverRank", serverRank);
        bundle.putString("totalCurrent", totalCurrent);
        bundle.putLong("catchDate", catchDate);
        bundle.putInt("rank", rank);
        bundle.putString("memberInfoList",memberInfoList);
        bundle.putString("rewardList",rewardList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        name = getArguments().getString("name");
        teamName = getArguments().getString("teamName");
        yesterdayCurrent = getArguments().getInt("yesterdayCurrent");
        serverRank = getArguments().getString("serverRank");
        totalCurrent = getArguments().getString("totalCurrent");
        catchDate = getArguments().getLong("catchDate");
        rank = getArguments().getInt("rank");

        Gson gson = new Gson();
        rewardList = gson.fromJson(getArguments().getString("rewardList"),new TypeToken<List<RewardInfo>>(){}.getType());
        monthBonusName = rewardList.get(0).getName();
        monthBonus = rewardList.get(0).getReward();
        monthRecruitBounusName = rewardList.get(1).getName();
        monthRecruitBounus = rewardList.get(1).getReward();
        monthManageBonusName = rewardList.get(2).getName();
        monthManageBonus = rewardList.get(2).getReward();

        memberInfoList = gson.fromJson(getArguments().getString("memberInfoList"),new TypeToken<List<MemberInfo>>(){}.getType());
        totalRecords = memberInfoList.size();

        //创建被装饰者实例
        final MyMemberInfoAdapter adapter = new MyMemberInfoAdapter(getActivity());
        //创建装饰者实例，并传入被装饰者和回调接口
        mAdapter = new MemberInfoAdapter(adapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                //此处模拟做网络操作，0.5s延迟，将拉取的数据更新到adapter中
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<MemberInfo> dataSet = memberInfoList;
                        //数据的处理最终还是交给被装饰的adapter来处理
                        adapter.appendData(dataSet);
                        callback.onSuccess();
                        //模拟加载到没有更多数据的情况，触发onFailure
                        if(loadCount++==(totalRecords/20)){
                            callback.onFailure();
                        }
                    }
                },500);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_fragment,container,false);
        mChiefName = (TextView) view.findViewById(R.id.chief_name);
        mTeamName = (TextView) view.findViewById(R.id.team_name);
        mYesterdayCurrent = (TextView) view.findViewById(R.id.yesterday_current);
        mServerRank = (TextView) view.findViewById(R.id.server_rank);
        mTotalCurrent = (TextView) view.findViewById(R.id.total_current);
        mChief = (TextView) view.findViewById(R.id.chief);
        mQrcode = (TextView) view.findViewById(R.id.team_qrcode);
        teamRefresh = (RefreshLayout) view.findViewById(R.id.team_refreshLayout);
        mMyBonus = (TextView) view.findViewById(R.id.my_bonus);
        mMemberInfo = (TextView) view.findViewById(R.id.member_info);
        memberInfo = (RecyclerView) view.findViewById(R.id.team_list);
        bonusInfo = (LinearLayout) view.findViewById(R.id.bonus_info);

        mMonthBonus = (TextView) view.findViewById(R.id.month_bonus);
        mMonthBonusNum = (TextView) view.findViewById(R.id.month_bonus_num);
        mMonthRecruitBonus = (TextView) view.findViewById(R.id.month_recruit_bonus);
        mMonthRecruitBonusNum = (TextView) view.findViewById(R.id.month_recruit_bonus_num);
        mMonthManageBonus = (TextView) view.findViewById(R.id.month_manage_bonus);
        mMonthManageBonusNum = (TextView) view.findViewById(R.id.month_manage_bonus_num);

        bonusInfo.setVisibility(View.VISIBLE);
        memberInfo.setVisibility(View.INVISIBLE);

        mMyBonus.setOnClickListener(this);
        mMemberInfo.setOnClickListener(this);
        mMyBonus.setSelected(true);
        mMemberInfo.setSelected(false);

        mChiefName.setText(name);
        if(rank <= 0) {
            if(teamName != null){
                mTeamName.setText(teamName);
            }else{
                mTeamName.setText("我还没有团队");
            }
            mChief.setText("");
            mTeamName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 2.3f));
            mChief.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 0));
            mQrcode.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1.7f));
            mQrcode.setOnClickListener(this);
        }else{
            mTeamName.setText(teamName);
            mChief.setText(rank == 1 ? "见习小队长" : (rank == 2 ? "小队长" : (rank == 3 ? "见习中队长" : (rank == 4 ? "中队长" : (rank == 5 ? "见习大队长" : "大队长")))));
            mTeamName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1.0f));
            mChief.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1.3f));
            mQrcode.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1.7f));
            mQrcode.setOnClickListener(this);
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        Double yCurrent = Double.valueOf(yesterdayCurrent) / 100;
        if(catchDate != 0){
            mYesterdayCurrent.setText(df.format(yCurrent) + "元/" + format.format(catchDate));
//            if(yesterdayCurrent%100 >= 50) {
//                mYesterdayCurrent.setText((yesterdayCurrent/100+1) + "元/" + format.format(catchDate));
//            }else{
//                mYesterdayCurrent.setText(yesterdayCurrent/100 + "元/" + format.format(catchDate));
//            }
        }else{
            Date date = new Date();
            mYesterdayCurrent.setText(df.format(yCurrent) + "元/" + format.format(date));
//            if(yesterdayCurrent%100 >= 50) {
//                mYesterdayCurrent.setText((yesterdayCurrent/100 + 1) + "元/" + format.format(date));
//            }else{
//                mYesterdayCurrent.setText(yesterdayCurrent/100 + "元/" + format.format(date));
//            }
        }
        mServerRank.setText((serverRank.equals("") || serverRank == null) ? "无" : serverRank );
        mTotalCurrent.setText(totalCurrent);

        //设置奖金值
        mMonthBonus.setText(monthBonusName);
        mMonthBonusNum.setText(df.format(monthBonus) + "元");
        mMonthRecruitBonus.setText(monthRecruitBounusName);
        mMonthRecruitBonusNum.setText(df.format(monthRecruitBounus) + "元");
        mMonthManageBonus.setText(monthManageBonusName);
        mMonthManageBonusNum.setText(df.format(monthManageBonus) + "元");

        teamRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url = API.BASE_URL+API.URL_TEAM_INFO;
                        RequestParams params = new RequestParams();
                        params.add("driverId", Constants.driverId.toString());

                        final AsyncHttpClient client = new AsyncHttpClient();
                        //保存cookie，自动保存到了sharepreferences
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                        client.setCookieStore(myCookieStore);
                        client.get(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                String json = new String(response);
                                Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json = " + json);
                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                JsonObject data = obj.get("data").getAsJsonObject();
                                String tName = data.get("teamName") != JsonNull.INSTANCE ? data.get("teamName").getAsString() : null;
                                Long cDate = data.get("catchDate") != JsonNull.INSTANCE ? data.get("catchDate").getAsLong() : 0;
                                //团队司机信息刷新完成
                                SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("team",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();

                                editor.putString("name",data.get("name").getAsString());
                                editor.putString("phone",data.get("phone").getAsString());
                                editor.putInt("yesterdayCurrent",data.get("yesterdayCurrent").getAsInt());
                                editor.putString("serverRank",data.get("serverRank").getAsString());
                                editor.putString("totalCurrent",data.get("totalCurrent").getAsString());
                                editor.putString("teamName",tName);
                                editor.putLong("catchDate",cDate);
                                editor.putInt("rank", data.get("rank").getAsInt());
                                editor.commit();

                                name = data.get("name").getAsString();
                                teamName = data.get("teamName") != JsonNull.INSTANCE ? data.get("teamName").getAsString() : null;
                                yesterdayCurrent = data.get("yesterdayCurrent").getAsInt();
                                serverRank = data.get("serverRank").getAsString();
                                totalCurrent = data.get("totalCurrent").getAsString();
                                catchDate = data.get("catchDate") != JsonNull.INSTANCE ? data.get("catchDate").getAsLong() : 0;
                                rank = data.get("rank").getAsInt();

                                String url = API.BASE_URL + API.URL_MYREWARD;
                                RequestParams params = new RequestParams();
                                params.add("driverId", Constants.driverId.toString());

                                final AsyncHttpClient client = new AsyncHttpClient();
                                //保存cookie，自动保存到了sharepreferences
                                PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                                client.setCookieStore(myCookieStore);
                                client.get(url, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                        String json = new String(response);
                                        Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json myreward = " + json);
                                        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                        JsonArray data = obj.get("data").getAsJsonArray();
                                        reward = data.toString();

                                        SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("reward", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();

                                        editor.putString("rewardList", reward);
                                        editor.commit();

                                        String url = API.BASE_URL+API.URL_MEMBER_INFO;
                                        RequestParams params = new RequestParams();
                                        params.add("driverId",Constants.driverId.toString());

                                        final AsyncHttpClient client = new AsyncHttpClient();
                                        //保存cookie，自动保存到了sharepreferences
                                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                                        client.setCookieStore(myCookieStore);
                                        client.get(url, params, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                                String json = new String(response);
                                                Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json = " + json);
                                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                                JsonArray data = obj.get("data").getAsJsonArray();
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                                //刷新完成
                                                String res = data.toString();
                                                TeamFragment TF = new TeamFragment().newInstance(name,teamName,yesterdayCurrent,serverRank,totalCurrent,catchDate,rank,res,reward);
                                                transaction.replace(R.id.fragment_container,TF);
                                                transaction.commit();
                                                if(res != null) {
                                                    SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("member",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sp.edit();

                                                    editor.putString("memberInfoList",res);
                                                    editor.commit();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                        Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                        Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                                    }

                                });

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                }, 500);
                teamRefresh.finishRefresh(500);
            }
        });
        memberInfo.setAdapter(mAdapter);
        memberInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.team_qrcode:
                Bitmap qrBitmap = null;
                try {
                    //qrBitmap = QRCodeUtil.createQRCode("http://www.botann.com:8085/carteam/driver/toRegister?iviteId="+Constants.driverId+"&data="+ EncryptionUtil.Encrypt("{\"iviteId\":\"" + Constants.driverId + "\"}"), 600, 600);
                    //qrBitmap = QRCodeUtil.createQRCode("http://www.botann.com:8088/carteam/driver/toRegister?iviteId="+Constants.driverId+"&data="+ EncryptionUtil.Encrypt("{\"iviteId\":\"" + Constants.driverId + "\"}"), 600, 600);
                    qrBitmap = QRCodeUtil.createQRCode(Constants.qrURL + "?iviteId="+Constants.driverId+"&data="+ EncryptionUtil.Encrypt("{\"iviteId\":\"" + Constants.driverId + "\"}"), 600, 600);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                QrcodeDialog.Builder dialogBuild = new QrcodeDialog.Builder(this.getActivity());
                dialogBuild.setImage(qrBitmap);
                QrcodeDialog dialog = dialogBuild.create();
                dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                dialog.show();
                break;

            case R.id.my_bonus:
                bonusInfo.setVisibility(View.VISIBLE);
                memberInfo.setVisibility(View.INVISIBLE);
                mMyBonus.setSelected(true);
                mMemberInfo.setSelected(false);
                break;

            case R.id.member_info:
                bonusInfo.setVisibility(View.INVISIBLE);
                memberInfo.setVisibility(View.VISIBLE);
                mMyBonus.setSelected(false);
                mMemberInfo.setSelected(true);
                break;
        }
    }
}
