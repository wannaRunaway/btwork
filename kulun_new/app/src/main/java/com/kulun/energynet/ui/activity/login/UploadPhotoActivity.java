package com.kulun.energynet.ui.activity.login;
import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.databinding.DataBindingUtil;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.kulun.energynet.BaseObjectString;
import com.kulun.energynet.R;
import com.kulun.energynet.databinding.ActivityUploadPhotoBinding;
import com.kulun.energynet.db.SharedPreferencesHelper;
import com.kulun.energynet.model.User;
import com.kulun.energynet.network.api.API;
import com.kulun.energynet.ui.activity.QuestionActivity;
import com.kulun.energynet.utils.AliOSS;
import com.kulun.energynet.utils.GsonUtils;
import com.kulun.energynet.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/*
 * created by xuedi on 2019/12/24
 */
public class UploadPhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityUploadPhotoBinding binding;
    private String path1,path2,path3;
    private boolean islogin;
    private PromptDialog promptDialog;
    //????????????????????????????????????
    private long lastBackTime = 0;
    private boolean isSelectCamera = true;
    private int type;
    private String bucketName = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_photo);
        binding.header.back.setOnClickListener(this);
        binding.tvCommit.setOnClickListener(this);
        binding.header.title.setText("???????????????????????????");
        binding.l1.setOnClickListener(this);
        binding.l2.setOnClickListener(this);
        binding.l3.setOnClickListener(this);
        islogin = getIntent().getBooleanExtra(API.islogin, false);
        type = getIntent().getIntExtra(API.type, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//??????Android 6.0
            if (!checkPermission()) { //???????????????????????????
                requestPermissions(); //????????????
            }
        }
        promptDialog = new PromptDialog(this);
        bucketName = SharedPreferencesHelper.getInstance(UploadPhotoActivity.this).getString(API.bucketName, "");
    }

    // ??????????????????
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_commit:
                //??????????????????????????????????????????????????????????????????????????????2?????????????????????????????????
                if (System.currentTimeMillis() - lastBackTime > 5 * 1000) {
                    commit();
                    lastBackTime = System.currentTimeMillis();
                } else { //????????????????????????????????????2?????????????????????
                    Toast.makeText(this, "??????????????????5??????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.l1:
                new AlertDialog.Builder(UploadPhotoActivity.this).setMessage("???????????????????????????").setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openCamera(1);
                        isSelectCamera = true;
                    }
                }).setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        xiangce(1);
                        isSelectCamera = false;
                    }
                }).create().show();
                break;
            case R.id.l2:
                new AlertDialog.Builder(UploadPhotoActivity.this).setMessage("???????????????????????????").setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openCamera(2);
                        isSelectCamera = true;
                    }
                }).setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        xiangce(2);
                         isSelectCamera = false;
                    }
                }).create().show();
                break;
            case R.id.l3:
                new AlertDialog.Builder(UploadPhotoActivity.this).setMessage("???????????????????????????").setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openCamera(3);
                        isSelectCamera = true;
                    }
                }).setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        xiangce(3);
                        isSelectCamera = false;
                    }
                }).create().show();
                break;
            default:
                break;
        }
    }

    private void xiangce(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, position);
    }

    private List<String> pathlist = new ArrayList<>();
    private void commit() {
        pathlist.clear();
        if (path1==null||path2==null||path3==null){
            Utils.toast(UploadPhotoActivity.this, "????????????????????????");
            return;
        }
        pathlist.add(path1);
        pathlist.add(path2);
        pathlist.add(path3);
        promptDialog.showLoading("????????????????????????");
        if (User.getInstance() == null){
            recursiveUploadPhoto(0);
        }else {
            recursiveUploadPhoto(User.getInstance().getAccountId());
        }
    }

    /**
     * ?????????????????????
     */
    List<String> list = new ArrayList<>();
    private void recursiveUploadPhoto(final int account) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String objectKey = account + "_" + System.currentTimeMillis() + ".jpg";
                PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, decodeSampledBitmap(pathlist.get(0), 600, 400));
                pathlist.remove(0);
                list.add(objectKey);
                try {
                    PutObjectResult putResult = AliOSS.getOss().putObject(put);
                    Log.d(Utils.TAG, "UploadSuccess");
                    Log.d(Utils.TAG, putResult.getETag());
                    Log.d(Utils.TAG, putResult.getRequestId());
                    if (pathlist.size()>0) {
                        recursiveUploadPhoto(account);
                    } else {
                        upload(list);
                    }
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (ServiceException serviceException) {
                }
            }
        });
    }

    /**
     *     "id":1,// ??????-??????????????????????????? ??????????????????
     *     "accountId":1,// ??????-??????????????????????????? ??????????????????
     *     "phone":"18329112222",// ??????????????? ??????????????????
     *     "businessType":6,// 6???????????????, 99?????????app/dictionary/getByTypeValue????????????// businessType=6 ????????????????????????
     *     "name":"??????",// ????????????
     *     "identity":"353100201912171111",// ?????????
     *     "plateNumber":"???A12345",
     *     "vin":"12345678901234567",
     *     "carTypeId":3,// ??????id???app/getCarType????????????
     *     "provinceId":330000,// ??????id???app/city/getAllCity????????????
     *     "cityId":330100,// ??????id???app/city/getAllCity????????????
     *     "photo":"key;key2;key2;key3"// ??????key
     *     intent.putExtra("phone", phone);
     *         intent.putExtra("name", binding.etName.getText().toString());
     *         intent.putExtra("identity", binding.etIdcard.getText().toString());
     *         intent.putExtra("plateNumber", binding.etCarplate.getText().toString());
     *         intent.putExtra("vin", binding.etChejia.getText().toString());
     *         intent.putExtra("carTypeId", cartypeid);
     *         intent.putExtra("provinceId", provinceid);
     *         intent.putExtra("cityId", cityid);
     */
    private void upload(final List<String> objectKey) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final String url = API.BASE_URL + API.ADDCARINFO;
                OkHttpClient okHttpClient = new OkHttpClient();
                JSONObject jsonObject = new JSONObject();
                    try {
                        if (User.getInstance().getAccountId() != 0) {
                            jsonObject.put("accountId", String.valueOf(User.getInstance().getAccountId()));
                        }
                        if (getIntent().getIntExtra("id", 0) != 0){
                            jsonObject.put("id", String.valueOf(getIntent().getIntExtra("id", 0)));
                        }
                        jsonObject.put("businessType", "6");
                        jsonObject.put("phone", getIntent().getStringExtra("phone"));
                        jsonObject.put("name", getIntent().getStringExtra("name"));
                        jsonObject.put("identity", getIntent().getStringExtra("identity"));
                        jsonObject.put("plateNumber", getIntent().getStringExtra("plateNumber"));
                        jsonObject.put("vin", getIntent().getStringExtra("vin"));
                        jsonObject.put("carTypeId", getIntent().getIntExtra("carTypeId", 0));
                        jsonObject.put("provinceId", getIntent().getIntExtra("provinceId", 0));
                        jsonObject.put("cityId", getIntent().getIntExtra("cityId", 0));
                        jsonObject.put("firstMiles", getIntent().getStringExtra("licheng"));
                        jsonObject.put("settleType", getIntent().getIntExtra("settleType", 0));
                        jsonObject.put("role", getIntent().getIntExtra("role", 0));
                        jsonObject.put("type", type);
                        if (objectKey != null) {
                            String ss = "";
                            for (int i = 0; i < objectKey.size() - 1; i++) {
                                ss = ss + objectKey.get(i) + ";";
                            }
                            ss = ss + objectKey.get(objectKey.size() - 1);
                            jsonObject.put("photo", ss);
                        } else {
                            jsonObject.put("photo", "");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                final FormBody formBody = new FormBody.Builder().add("driverCarInfo", jsonObject.toString()).build();
                Utils.log(null,jsonObject.toString(),null);
                Request request = new Request.Builder().url(url).post(formBody).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                promptDialog.dismiss();
                            }
                        });
                        Utils.log(url, formBody.toString(), "??????");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Utils.log(url, formBody.toString(), json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                promptDialog.dismiss();
                            }
                        });
                        try {
                            final JSONObject jsonObject1 = new JSONObject(json);
                            if (jsonObject1.getBoolean("success")){
                                Intent intent = new Intent(UploadPhotoActivity.this, UploadCarFinalActivity.class);
                                intent.putExtra(API.islogin, islogin);
                                startActivity(intent);
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (jsonObject1.getString("msg") != null) {
                                                Utils.toast(UploadPhotoActivity.this, jsonObject1.getString("msg"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public byte[] decodeSampledBitmap(String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // ??????isjustdecodebounds?????????
        opt.inJustDecodeBounds = true;
        // ?????????????????????????????????????????????
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        // ???????????????????????????
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        // isSampleSize????????????????????????????????????????????????2??????????????????????????????????????????1/2
        opt.inSampleSize = 2;
        // ??????????????????????????????????????????????????????
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)
                opt.inSampleSize = picHeight / screenHeight;
        }
        // ???????????????????????????????????????????????????????????????bitmap
        opt.inJustDecodeBounds = false;
        Bitmap bitmaps =  BitmapFactory.decodeFile(pathName, opt);
        Log.d(Utils.TAG, bitmaps.getWidth()+"?????????????????????"+bitmaps.getHeight());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap =  BitmapFactory.decodeFile(pathName, options);
        Log.d(Utils.TAG, bitmap.getWidth()+"??????????????????????????????"+bitmap.getHeight());
        return compressImagebyte(bitmap);
    }

    /*
     * ????????????
     * */
    private byte[] compressImagebyte(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//???????????????????????????100????????????????????????????????????????????????baos???
        int options = 70;
        int a = baos.toByteArray().length;
        while (baos.toByteArray().length / 1024 > 2048) { //?????????????????????????????????????????????100kb,??????????????????
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//????????????options%?????????????????????????????????baos???
            options -= 10;
            Log.d(Utils.TAG, "???????????????");
        }
        return baos.toByteArray();
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

    private boolean checkPermission() {
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(UploadPhotoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean haveWritePermission = ContextCompat.checkSelfPermission(UploadPhotoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return haveCameraPermission && haveWritePermission;

    }
    // ????????????????????????????????????Android 10????????????????????????????????????
    private String mCameraImagePath;
    // ?????????Android 10????????????
//    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    /**
     * ??????????????????
     */
    private void openCamera(int position) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ?????????????????????
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;
            if (false) {
                // ??????android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //??????Android 7.0?????????????????????FileProvider????????????content?????????Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, position);
            }
        }
    }

    /**
     * ??????????????????uri,?????????????????????????????? Android 10????????????????????????
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // ???????????????SD???,????????????SD?????????,?????????SD????????????????????????
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * ???????????????????????????
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                    switch (requestCode) {
                        case 1:
                            if (isSelectCamera) {
                                binding.img1.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                                path1 = mCameraImagePath;
                            }else {
                                path1 = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                                binding.img1.setImageBitmap(BitmapFactory.decodeFile(path1));
                            }
                            break;
                        case 2:
                            if (isSelectCamera) {
                                binding.img2.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                                path2 = mCameraImagePath;
                            }else {
                                path2 = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                                binding.img2.setImageBitmap(BitmapFactory.decodeFile(path2));
                            }
                            break;
                        case 3:
                            if (isSelectCamera) {
                                binding.img3.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                                path3 = mCameraImagePath;
                            }else {
                                path3 = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                                binding.img3.setImageBitmap(BitmapFactory.decodeFile(path3));
                            }
                            break;
                        default:
                            break;
                    }
                }
        }
    }
