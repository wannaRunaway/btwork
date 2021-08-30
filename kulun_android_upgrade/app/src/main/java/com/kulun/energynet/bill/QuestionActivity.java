package com.kulun.energynet.bill;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.AliOSS;
import com.kulun.energynet.databinding.QuestionBinding;
import com.kulun.energynet.mine.MyPhotoAdapter;
import com.kulun.energynet.model.Bill;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import me.nereo.multi_image_selector.MultiImageSelector;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    //上次按下返回键的系统时间
    private long lastBackTime = 0;
    private MyPhotoAdapter adapter;
    private static final int REQUEST_IMAGE = 2;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
//    private String bucketName = "";
    private ArrayList<String> mSelectPath = new ArrayList<String>();
    private QuestionBinding binding;
    private PromptDialog promptDialog;
    private OSS alioss;
    private Bill bill;
    private int siteid;
    private int exId;
    private String orderNo,site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.question);
        binding.header.left.setOnClickListener(v -> finish());
//        binding.header.right.setOnClickListener(this);
        binding.header.title.setText("资费疑问");
        binding.finish.setText("提交");
        binding.finish.setOnClickListener(v -> {
            /**
             * 这里上传图片进行修改，改为上传到阿里云sso,然后上传其他东西
             */
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            String content = binding.etQuestion.getText().toString();
            if ("".equals(content) || content==null) {
                Utils.snackbar(QuestionActivity.this, "请简单描述问题");
                return;
            }
            if (System.currentTimeMillis() - lastBackTime > 10 * 1000) {
                commit();
                lastBackTime = System.currentTimeMillis();
            } else { //如果两次按下的时间差小于2秒，则退出程序
                Toast.makeText(this, "按键过快，请10秒后再点击哦", Toast.LENGTH_SHORT).show();
            }
        });
        exId = getIntent().getIntExtra("exId", 0);
        orderNo = getIntent().getStringExtra("orderNo");
        site = getIntent().getStringExtra("site");
        binding.number.setText(orderNo);
        binding.station.setText(site);
        bindView();
        promptDialog = new PromptDialog(this);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QuestionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);
        }
        bill = (Bill) getIntent().getSerializableExtra("bill");
        siteid = getIntent().getIntExtra("siteid", 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadOssToken();
            }
        },1000);
    }

    public void bindView() {
        adapter = new MyPhotoAdapter(QuestionActivity.this, mSelectPath);
        binding.gridview.setAdapter(adapter);
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("view: " + view);
                if (i == mSelectPath.size()) {
                    pickImage();
                }
            }
        });
    }

    private void loadOssToken() {
        new MyRequest().myRequest(API.osstoken, false, null, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                String ak = json.get("AccessKeyId").getAsString();
                String sk = json.get("AccessKeySecret").getAsString();
                String token = json.get("SecurityToken").getAsString();
                String expiration = json.get("Expiration").getAsString();
                SharePref.put(QuestionActivity.this, API.ak, ak);
                SharePref.put(QuestionActivity.this, API.sk, sk);
                SharePref.put(QuestionActivity.this, API.myosstoken, token);
                SharePref.put(QuestionActivity.this, API.expiration, expiration);
                alioss = AliOSS.getOss(QuestionActivity.this);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            default:
                break;
        }
    }
    private void commit(){
        promptDialog.showLoading("正在上传，请稍候");
        List<String> list = new ArrayList<>();
        if (mSelectPath.size() > 0) {
            recursiveUploadPhoto(Utils.getAccount(this), 0, list);
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
    private void recursiveUploadPhoto(final String account, final int mark, List<String> list) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String objectKey = account + "_" + System.currentTimeMillis() + ".jpg";
                PutObjectRequest put = new PutObjectRequest(Utils.getBucketName(QuestionActivity.this), objectKey, decodeSampledBitmap(mSelectPath.get(mark), 600, 400));
                list.add(objectKey);
                try {
                    PutObjectResult putResult = alioss.putObject(put);
                    Log.d(Utils.TAG, "UploadSuccess");
                    Log.d(Utils.TAG, putResult.getETag());
                    Log.d(Utils.TAG, putResult.getRequestId());
                    if (mark < mSelectPath.size() - 1) {
                        recursiveUploadPhoto(account, mark + 1, list);
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
        final String content = binding.etQuestion.getText().toString();
        if ("".equals(content) || null == content) {
            Utils.snackbar(this, "请简单描述问题");
            return;
        }
        String picture = "";
        if (objectKey != null) {
            String ss = "";
            for (int i = 0; i < objectKey.size() - 1; i++) {
                ss = ss + objectKey.get(i) + ",";
            }
            picture = ss + objectKey.get(objectKey.size() - 1);
        } else {
            picture = "";
        }
        String mycontent = binding.etQuestion.getText().toString();
        String json = JsonSplice.leftparent+JsonSplice.yin+"exId"+JsonSplice.yinandmao+exId+JsonSplice.dou+
                JsonSplice.yin+"siteId"+JsonSplice.yinandmao+siteid+JsonSplice.dou+
                JsonSplice.yin+"picture"+JsonSplice.yinandmao+JsonSplice.yin+picture+JsonSplice.yinanddou+
                JsonSplice.yin+"content"+JsonSplice.yinandmao+JsonSplice.yin+mycontent+JsonSplice.yin+JsonSplice.rightparent;
        new MyRequest().spliceJson(API.questionCommit, true, json, QuestionActivity.this, promptDialog, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(QuestionActivity.this, "资费疑问上传成功");
                finish();
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
            int maxNum = 4;
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
                binding.gridview.setAdapter(adapter);
                binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == mSelectPath.size()) {
                            pickImage();
                        } else {
                            mSelectPath.remove(i);
                            adapter = new MyPhotoAdapter(QuestionActivity.this, mSelectPath);
                            binding.gridview.setAdapter(adapter);
                        }
                    }
                });

            }
        }
    }
}
