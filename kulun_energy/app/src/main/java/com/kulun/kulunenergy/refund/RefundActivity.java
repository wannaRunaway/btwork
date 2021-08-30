package com.kulun.kulunenergy.refund;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.databinding.ActivityRefundBinding;
import com.kulun.kulunenergy.login.PasswordLoginActivity;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.OssTokenModel;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.utils.API;
import com.kulun.kulunenergy.utils.CashInputFilter;
import com.kulun.kulunenergy.utils.CompressUtils;
import com.kulun.kulunenergy.utils.Constants;
import com.kulun.kulunenergy.utils.Customize;
import com.kulun.kulunenergy.utils.GsonUtils;
import com.kulun.kulunenergy.utils.ListUtils;
import com.kulun.kulunenergy.requestparams.MyRequest;
import com.kulun.kulunenergy.utils.OnClickEvent;
import com.kulun.kulunenergy.utils.SharePref;
import com.kulun.kulunenergy.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leefeng.promptlibrary.PromptDialog;

public class RefundActivity extends BaseActivity {
    private ActivityRefundBinding binding;
    private List<String> mylist;
    private GridViewAdapter mGridViewAddImgAdapter;
    private String bucket="app-back-98039b9ced38";
//    private String bucket="dongruime-app";
    private String objectkeyPrefix="pictures/refund/";
    private OSSClient oss;
    private boolean hasToken;
    List<String> lastUpLoad= Collections.synchronizedList(new ArrayList<String>());//线程安全
    private PromptDialog promptDialog;
    private int consumeId;
    private double realAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding= DataBindingUtil.setContentView(this, R.layout.activity_refund);
        binding.header.title.setText("退款申请");
        binding.header.left.setOnClickListener(view -> finish());
        //binding.etReason.setFilters(new InputFilter[]{new EmojiFilter()});
        if(getIntent()!=null){
            consumeId=getIntent().getIntExtra("consumeId",0);
            realAmount=getIntent().getDoubleExtra("realAmount",0.00);
        }
        init();
        getOssToken(false);
    }

    private void getOssToken(boolean isCommit) {
        String url = API.BASE_URL + API.OSSTOKEN;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(cookieStore);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                OssTokenModel ossTokenModel=GsonUtils.getInstance().fromJson(json,OssTokenModel.class);
                if (ossTokenModel.getCode() == 801){  //其他有登陆情况
                    cleanLoginInfo();
                    return;
                }
                if (ossTokenModel.isSuccess()&&ossTokenModel.getContent()!=null) {
                    OssTokenModel.ContentBean contentBean=ossTokenModel.getContent();
                    String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
//                    String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
                    OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(contentBean.getAccessKeyId(), contentBean.getAccessKeySecret(), contentBean.getSecurityToken());
                    oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
                    hasToken=true;
                    if(isCommit){
                        showDialog();
                        if(ListUtils.isEmpty(mylist)){
                            finalCommit();
                        }else {
                            commitPic();
                        }
                    }
                }else {
                    if (!TextUtils.isEmpty(ossTokenModel.getMsg())&&isCommit) {
                        Utils.snackbar( RefundActivity.this, ossTokenModel.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if(isCommit){
                    Utils.snackbar(RefundActivity.this, API.net_error);
                }
           }
        });
    }
    private void cleanLoginInfo(){
        SharePref.put(RefundActivity.this, API.bindId, 0);
        SharePref.put(RefundActivity.this, API.plateNo, "");
        SharePref.put(RefundActivity.this, API.carType, 0);
        SharePref.put(RefundActivity.this, API.batterycount, 0);
//        User.getInstance().setYuyue(false);
//        User.getInstance().setDelay(false);
        User.getInstance().setIsneedlogin(true);
//        User.getInstance().setIsneedcheckIdcard(false);//退出后则不弹出身份认证提示
        Utils.snackbar(RefundActivity.this,"请重新登录");
        Intent intent = new Intent(RefundActivity.this, PasswordLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void init() {
        InputFilter[] filters={new CashInputFilter()};
        binding.etBalance.setFilters(filters);
        binding.etBalance.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mylist=new ArrayList<>();
        mGridViewAddImgAdapter = new GridViewAdapter(RefundActivity.this, mylist, new GridViewAdapter.DeletePicCallBack() {
            @Override
            public void del(int index) {
                try {
                    String picUrl=mylist.get(index);
                    for(int i = mylist.size() - 1; i >= 0; i--){
                        String item = mylist.get(i);
                        if(picUrl.equals(item)){
                            mylist.remove(item);
                        }
                    }
                    mGridViewAddImgAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        binding.gridView.setAdapter(mGridViewAddImgAdapter);
        binding.gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == parent.getChildCount() - 1) {
                //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                if (mylist.size() == Constants.MAX_SELECT_PIC_NUM) {
                    //最多添加5张图片
                    Intent intent=new Intent(RefundActivity.this, ImagePreActivity.class);
                    intent.putStringArrayListExtra("imgList", (ArrayList<String>) mylist);
                    intent.putExtra("position",position);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    //添加凭证图片
                    selectPic(Constants.MAX_SELECT_PIC_NUM - mylist.size());
                }
            } else {
                Intent intent=new Intent(RefundActivity.this, ImagePreActivity.class);
                intent.putStringArrayListExtra("imgList", (ArrayList<String>) mylist);
                intent.putExtra("position",position);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        binding.ivCommit.setOnClickListener(new OnClickEvent(500) {
            @Override
            public void singleClick(View v) {
                String balance=binding.etBalance.getText().toString().trim();
                String reason=binding.etReason.getText().toString().trim();
                if(TextUtils.isEmpty(balance)){
                    Utils.snackbar(RefundActivity.this,"请输入退款金额");
                    return;
                }
                if(Double.valueOf(balance)>realAmount){
                    Utils.snackbar(RefundActivity.this,"退款金额不能大于实际支付金额");
                    return;
                }
                if(TextUtils.isEmpty(reason)){
                    Utils.snackbar(RefundActivity.this,"请输入退款原因");
                    return;
                }
                commit();
            }
        });
    }

    private void commit() {
        if(hasToken){
            showDialog();
            if(ListUtils.isEmpty(mylist)){
                finalCommit();
            }else {
                commitPic();
            }
        }else {
            getOssToken(true);
        }
    }

    private void showDialog() {
        if(promptDialog==null){
            promptDialog=new PromptDialog(this);
            promptDialog.showLoading("正在上传中...");
        }else {
            promptDialog.showLoading("正在上传中...");
        }
    }

    private void commitPic() {
        lastUpLoad.clear();
        new CompressUtils(pathList -> {
            for (int i = 0; i <pathList.size() ; i++) {
                String path=pathList.get(i);
                String name=path.substring(path.lastIndexOf("/")+1);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType("image/jpg");
                PutObjectRequest put = new PutObjectRequest(bucket,objectkeyPrefix+name,path,objectMetadata);
                oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        runOnUiThread(() -> {
                            lastUpLoad.add(objectkeyPrefix + name);
                            //L.e("oss成功size="+lastUpLoad.size());
                            if(lastUpLoad.size()==pathList.size()){
                                //L.e("循环已走完");
                                finalCommit();
                            }
                        });
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        //L.e("oss失败");
                        runOnUiThread(() -> {
                            if(promptDialog!=null){
                                promptDialog.dismiss();
                            }
                            Utils.snackbar(RefundActivity.this, "图片上传失败");
                        });
                    }
                });
            }
        }).execute(mylist);
    }

    public static List<String> getImagePathFromSD() {
        // 图片列表
        List<String> imagePathList = new ArrayList<>();
        // 得到sd卡内image文件夹的路径   File.separator(/)
        // String filePath = Environment.getExternalStorageDirectory().toString();
        String filePath = Environment.getExternalStorageDirectory() + "/dongPic/";
        File imgFile = new File(filePath);
        if (!imgFile.exists()) {
            imgFile.mkdir();
        }
        String filePath1 = Environment.getExternalStorageDirectory() + "/dongPic";
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath1);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (File file : files) {
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }
    @SuppressLint("DefaultLocale")
    public static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        }
        return isImageFile;
    }

    private void finalCommit() {
        String imgKey="";
        if(!ListUtils.isEmpty(lastUpLoad)){
            StringBuffer sb=new StringBuffer();
            for (int i = 0; i <lastUpLoad.size() ; i++) {
                sb.append(lastUpLoad.get(i));
                if(i!=lastUpLoad.size()-1){
                    sb.append(",");
                }
            }
            imgKey=sb.toString();
        }
        //L.e("上传图片="+imgKey);
        String balance=binding.etBalance.getText().toString().trim();
        String reason=binding.etReason.getText().toString().trim();
        //L.e("imgKey="+imgKey);
        String url = API.BASE_URL + API.APPLYREFUND;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("id",String.valueOf(consumeId));
        map.put("balance",balance);
        map.put("reason",reason);
        if(!TextUtils.isEmpty(imgKey)){
            map.put("imgKey",imgKey);
        }
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        new MyRequest().mypromptrequest(url, requestParams, RefundActivity.this, getApplicationContext(), true, json -> {
            List<String> imagePathFromSD =getImagePathFromSD();
            for (int i = 0; i < imagePathFromSD.size(); i++) {
                File file = new File(imagePathFromSD.get(i));
                file.delete();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                android.net.Uri uri = android.net.Uri.fromFile(file);
                intent.setData(uri);
                RefundActivity.this.sendBroadcast(intent);
            }
            EventBus.getDefault().post("refreshConsume");
            Intent intent=new Intent();
            RefundActivity.this.setResult(RESULT_OK,intent);
            RefundActivity.this.finish();
        }, promptDialog);
    }

    @Override
    protected void onDestroy() {
        if(promptDialog!=null){
            promptDialog.dismiss();
        }
        super.onDestroy();
    }


    private void selectPic(int maxTotal) {
        PictureSelector.create(RefundActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(maxTotal)
                .isCamera(false)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode==PictureConfig.CHOOSE_REQUEST){
                refreshAdapter(PictureSelector.obtainMultipleResult(data));
            }
        }
    }
    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                mylist.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
    }
}
