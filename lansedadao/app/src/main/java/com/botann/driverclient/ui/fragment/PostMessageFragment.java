package com.botann.driverclient.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.adapter.BaseAdapter;
import com.botann.driverclient.adapter.ILoadCallback;
import com.botann.driverclient.adapter.MessageInfoAdapter;
import com.botann.driverclient.adapter.MyMessageInfoAdapter;
import com.botann.driverclient.adapter.OnLoad;
import com.botann.driverclient.adapter.adapterinter.MessageInfoInterface;
import com.botann.driverclient.model.MessageInfo;
import com.botann.driverclient.model.User;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.ui.activity.WebViewActivity;
import com.botann.driverclient.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Orion on 2017/8/7.
 */
public class PostMessageFragment extends Fragment {
    RefreshLayout messageRefresh;
    RecyclerView messageInfo;
    BaseAdapter mAdapter;
    int loadCount;
    int totalRecords;

    private static boolean flag = false;

    //消息记录数据
    List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();

    public static PostMessageFragment newInstance(String messageInfoList, Integer total) {
        PostMessageFragment fragment = new PostMessageFragment();
        Bundle bundle = new Bundle();

        bundle.putString("messageInfoList",messageInfoList);
        bundle.putInt("total",total);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        messageInfoList = gson.fromJson(getArguments().getString("messageInfoList"),new TypeToken<List<MessageInfo>>(){}.getType());
        totalRecords = getArguments().getInt("total");
        loadCount = 0;

        //创建被装饰者实例
        final MyMessageInfoAdapter adapter = new MyMessageInfoAdapter(getActivity(), new MessageInfoInterface() {
            @Override
            public void tourl(String url) {
                /**
                 * 跳转到webView加载url
                 */
                Intent intent = new Intent();
                intent.putExtra("url", url);
                intent.setClass(MainApp.getInstance().getApplicationContext(), WebViewActivity.class);
                startActivity(intent);
            }
        });
        //创建装饰者实例，并传入被装饰者和回调接口
        mAdapter = new MessageInfoAdapter(adapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                //此处模拟做网络操作，0.5s延迟，将拉取的数据更新到adapter中
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<MessageInfo> dataSet = new ArrayList<MessageInfo>();
                        if(loadCount == 0) {
                            dataSet = messageInfoList;
                        } else {
                            //发送一次请求，加载更多，dataSet更新为新查出来的数据
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    String url = API.BASE_URL + API.URL_POST_MESSAGE;
                                    RequestParams params = new RequestParams();
                                    params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                                    params.add("pageNo", loadCount  + "");
                                    params.add("pageSize", "20");

                                    final AsyncHttpClient client = new AsyncHttpClient();
                                    //保存cookie，自动保存到了sharepreferences
                                    PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                                    client.setCookieStore(myCookieStore);
                                    client.post(url, params, new AsyncHttpResponseHandler() {

                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            String json = new String(responseBody);
                                            Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json = " + json);
                                            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                            JsonObject content = obj.get("content").getAsJsonObject();
                                            JsonArray data = content.get("data").getAsJsonArray();
                                            String res = data.toString();
                                            if(res != null) {
                                                SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("postMessageList",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.putString("postMessageLoadMoreList", res);
                                                editor.commit();
                                                flag = true;
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

                            if(flag) {
                                SharedPreferences sp =  MainApp.getInstance().getApplicationContext().getSharedPreferences("postMessageList",MODE_PRIVATE);
                                String res = sp.getString("postMessageLoadMoreList", "");
                                Gson gson = new Gson();
                                dataSet = gson.fromJson(res, new TypeToken<List<MessageInfo>>(){}.getType());
                            }
                        }
                        //数据的处理最终还是交给被装饰的adapter来处理
                        adapter.appendData(dataSet);
                        callback.onSuccess();
                        //模拟加载到没有更多数据的情况，触发onFailure
                        if(loadCount++ == (totalRecords/20 + 1)){
                            callback.onFailure();
                        }
                    }
                },500);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        messageRefresh = (RefreshLayout) view.findViewById(R.id.message_refresh);
        messageInfo = (RecyclerView) view.findViewById(R.id.message_list);
        messageRefresh.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final List<MessageInfo> dataSet = new ArrayList<MessageInfo>();
                        String url = API.BASE_URL + API.URL_POST_MESSAGE;
                        RequestParams params = new RequestParams();
                        params.add("accountId", String.valueOf(User.getInstance().getAccountId()));
                        params.add("pageNo","1");
                        params.add("pageSize","20");

                        final AsyncHttpClient client = new AsyncHttpClient();
                        //保存cookie，自动保存到了sharepreferences
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                        client.setCookieStore(myCookieStore);
                        client.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                String json = new String(response);
                                Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json = " + json);
                                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                                JsonObject content = obj.get("content").getAsJsonObject();
                                JsonArray data = content.get("data").getAsJsonArray();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                //刷新完成
                                Integer total = content.get("total").getAsInt();
                                String res = data.toString();
                                PostMessageFragment PMF = new PostMessageFragment().newInstance(res,total);
                                transaction.replace(R.id.message_fragment_container,PMF);
                                transaction.commit();
                                if(res != null) {
                                    SharedPreferences sp = MainApp.getInstance().getApplicationContext().getSharedPreferences("message",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();

                                    editor.putString("postMessageList",res);
                                    editor.putInt("post_total",total);
                                    editor.commit();
                                }
                                flag = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                                Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                                Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },500);
                messageRefresh.finishRefresh(500);
            }
        });
        messageInfo.setAdapter(mAdapter);
        messageInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }
}
