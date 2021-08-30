package com.botann.driverclient.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.botann.driverclient.MainApp;
import com.botann.driverclient.R;
import com.botann.driverclient.adapter.MyPhotoAdapter;
import com.botann.driverclient.model.User;
import com.botann.driverclient.network.api.API;
import com.botann.driverclient.utils.AliOSS;
import com.botann.driverclient.utils.Constants;
import com.botann.driverclient.utils.ToastUtil;
import com.botann.driverclient.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.leefeng.promptlibrary.PromptDialog;
import me.nereo.multi_image_selector.MultiImageSelector;

public class QuestionActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private static QuestionActivity mInstance = null;
    //上次按下返回键的系统时间
    private long lastBackTime = 0;
    private TextView mBack;
    private TextView mCommit;
    private EditText mDescription;
    private TextView mSerialNo;
    private TextView mStation;
    private GridView mPhotos;
    private MyPhotoAdapter adapter;
    private PromptDialog promptDialog;

    private String exchangeRecordId;
    private String serialNum;
    private String stationName;

    private static final int REQUEST_IMAGE = 2;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    private ArrayList<String> mSelectPath = new ArrayList<String>();

    public static QuestionActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInstance = this;
        setContentView(R.layout.question);

        Intent intent = getIntent();

        exchangeRecordId = intent.getStringExtra("exchangeRecordId");
        serialNum = intent.getStringExtra("serialNum");
        stationName = intent.getStringExtra("stationName");

        bindView();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QuestionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);
        }
    }

    public void bindView() {
        mBack = (TextView) this.findViewById(R.id.question_back);
        mCommit = (TextView) this.findViewById(R.id.question_commit);
        mDescription = (EditText) this.findViewById(R.id.question_description);
        mSerialNo = (TextView) this.findViewById(R.id.question_serial_no);
        mStation = (TextView) this.findViewById(R.id.question_station);
        mPhotos = (GridView) this.findViewById(R.id.question_photos);
        promptDialog = new PromptDialog(this);

        mSerialNo.setText("订单号：" + serialNum);
        mStation.setText(stationName);

        mBack.setOnClickListener(this);
        mCommit.setOnClickListener(this);

        adapter = new MyPhotoAdapter(QuestionActivity.this, mSelectPath);
        mPhotos.setAdapter(adapter);

        mPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("view: " + view);
                if (i == mSelectPath.size()) {
                    pickImage();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_back:
                finish();
                break;
            case R.id.question_commit:
                /**
                 * 这里上传图片进行修改，改为上传到阿里云sso,然后上传其他东西
                 */
                //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
                String content = mDescription.getText().toString();
                if ("".equals(content) || content==null) {
                    ToastUtil.showToast(mContext, "请简单描述问题");
                    return;
                }
                if (System.currentTimeMillis() - lastBackTime > 10 * 1000) {
                    commit();
                    lastBackTime = System.currentTimeMillis();
                } else { //如果两次按下的时间差小于2秒，则退出程序
                    Toast.makeText(this, "按键过快，请10秒后再点击哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private void commit(){
        promptDialog.showLoading("正在上传，请稍候");
        if (mSelectPath.size() > 0) {
            recursiveUploadPhoto(User.getInstance().getAccountId(), 0);
        } else {
            uploadQuestion(null);
        }
    }
    public byte[] decodeSampledBitmap(String pathName, int reqWidth, int reqHeight) {
        Bitmap bitmaps =  BitmapFactory.decodeFile(pathName);
        Log.d(Utils.TAG, bitmaps.getWidth()+"图片尺寸的长宽"+bitmaps.getHeight());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap =  BitmapFactory.decodeFile(pathName, options);
        Log.d(Utils.TAG, bitmap.getWidth()+"图片尺寸压缩后的长宽"+bitmap.getHeight());
        return compressImage(bitmap);
    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    /**
     * 图片上传阿里云
     */
    List<String> list = new ArrayList<>();
    private void recursiveUploadPhoto(final int account, final int mark) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String objectKey = account + "_" + System.currentTimeMillis() + ".jpg";
                PutObjectRequest put = new PutObjectRequest("electric-app", objectKey, decodeSampledBitmap(mSelectPath.get(mark), 600, 400));
                list.add(objectKey);
                try {
                    PutObjectResult putResult = AliOSS.getOss().putObject(put);
                    Log.d(Utils.TAG, "UploadSuccess");
                    Log.d(Utils.TAG, putResult.getETag());
                    Log.d(Utils.TAG, putResult.getRequestId());
                    if (mark < mSelectPath.size() - 1) {
                        recursiveUploadPhoto(account, mark + 1);
                    } else {
                        uploadQuestion(list);
                    }
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (ServiceException serviceException) {
                }
            }
        });
    }

    /*
     * 质量压缩
     * */
    private byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 70;
        int a = baos.toByteArray().length;
        while (baos.toByteArray().length / 1024 > 2048) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;
            Log.d(Utils.TAG, "质量压缩了");
        }
        return baos.toByteArray();
    }

    /**
     * 图片上传完毕，提交问题
     *
     * @param objectKey
     */
    private void uploadQuestion(final List<String> objectKey) {
        final String content = mDescription.getText().toString();
        if ("".equals(content)) {
            ToastUtil.showToast(mContext, "请简单描述问题");
            return;
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final String url = API.BASE_URL + API.URL_QUESTION_SAVE;
                final RequestParams params = new RequestParams();
                params.add("exchangeRecordId", exchangeRecordId);
                params.add("content", content);
                if (objectKey != null) {
                    String ss = "";
                    for (int i = 0; i < objectKey.size() - 1; i++) {
                        ss = ss + objectKey.get(i) + ",";
                    }
                    ss = ss + objectKey.get(objectKey.size() - 1);
                    params.add("picture", ss);
                } else {
                    params.add("picture", "");
                }
                final AsyncHttpClient client = new AsyncHttpClient();
                PersistentCookieStore myCookieStore = new PersistentCookieStore(MainApp.getInstance().getApplicationContext());
                client.setCookieStore(myCookieStore);
                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                        mBar.setVisibility(View.INVISIBLE);
                        promptDialog.dismiss();
                        String json = new String(response);
                        Utils.log(url, params, json);
//                        Log.d(MainApp.getInstance().getApplicationContext().getPackageName(), "onSuccess json comment = " + json);
                        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                        Integer code = obj.get("code").getAsInt();
                        if (code == 0) {
                            if (objectKey != null) {
                                objectKey.clear();
                            }
                            ToastUtil.showToast(mContext, "问题提交成功");
                            Constants.whichFragment = 3;
                            //跳转到消费记录界面
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainApp.getInstance().getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    MainApp.getInstance().getApplicationContext().startActivity(intent);
                                }
                            }, 500);

                        } else {
                            ToastUtil.showToast(mContext, obj.get("msg").getAsString());
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                        mBar.setVisibility(View.INVISIBLE);
                        promptDialog.dismiss();
                        Log.e(MainApp.getInstance().getApplicationContext().getPackageName(), "Exception = " + e.toString());
                        Toast.makeText(MainApp.getInstance().getApplicationContext(), "连接到服务器失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            int maxNum = 7;
            MultiImageSelector selector = MultiImageSelector.create(QuestionActivity.this);
            selector.showCamera(true);
            selector.count(maxNum);
            selector.multi();
            selector.origin(mSelectPath);
            selector.start(QuestionActivity.this, REQUEST_IMAGE);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(QuestionActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                adapter = new MyPhotoAdapter(QuestionActivity.this, mSelectPath);
                mPhotos.setAdapter(adapter);

                mPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == mSelectPath.size()) {
                            pickImage();
                        } else {
                            mSelectPath.remove(i);
                            adapter = new MyPhotoAdapter(QuestionActivity.this, mSelectPath);
                            mPhotos.setAdapter(adapter);
                        }
                    }
                });

            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
