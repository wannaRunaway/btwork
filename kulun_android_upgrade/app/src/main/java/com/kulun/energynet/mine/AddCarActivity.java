package com.kulun.energynet.mine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulun.energynet.R;
import com.kulun.energynet.customizeView.AliOSS;
import com.kulun.energynet.customizeView.KeyboardUtil;
import com.kulun.energynet.customizeView.getPhotoFromPhotoAlbum;
import com.kulun.energynet.databinding.ActivityAddcarBinding;
import com.kulun.energynet.main.BaseActivity;
import com.kulun.energynet.requestparams.MyRequest;
import com.kulun.energynet.requestparams.Response;
import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.JsonSplice;
import com.kulun.energynet.utils.SharePref;
import com.kulun.energynet.utils.Utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import me.leefeng.promptlibrary.PromptDialog;
//import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper;
//??????master??????
/**
 * created by xuedi on 2019/8/8
 */
public class AddCarActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAddcarBinding binding;
    private boolean isSelectCamera = true;
    private String path1, path2, path3;
    private PromptDialog promptDialog;
    //    private double miles;
    private int car_type;
    private OSS alioss;
    private KeyboardUtil keyboardUtil;

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcar);
        binding.head.title.setText("????????????");
        binding.bottom.finish.setOnClickListener(this);
        binding.head.left.setOnClickListener(this);
        binding.l1.setOnClickListener(this);
        binding.l2.setOnClickListener(this);
        binding.l3.setOnClickListener(this);
        binding.imgGettbox.setOnClickListener(this);
        promptDialog = new PromptDialog(this);
        binding.etMileage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2 + 1);
                        binding.etMileage.setText(s);
                        binding.etMileage.setSelection(s.length()); //??????????????????
                    }
                }
//                ????????????????????????????????????????????????????????????????????????
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    binding.etMileage.setText(s);
                    binding.etMileage.setSelection(2);
                }
//                ???????????????"0"?????????????????????????????????"."????????????????????????
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        binding.etMileage.setText(s.subSequence(0, 1));
                        binding.etMileage.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.etCarplate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil(AddCarActivity.this, binding.etCarplate);
                    keyboardUtil.hideSoftInputMethod();
                    keyboardUtil.showKeyboard();
                } else {
                    keyboardUtil.showKeyboard();
                }
                return false;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadOssToken();
            }
        }, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null) {
                if (keyboardUtil.isShow()) {
                    keyboardUtil.hideKeyboard();
                }else {
                    finish();
                }
            }else {
                finish();
            }
        }
        return false;
    }

    public void hideKeyboard(MotionEvent event, View view) {
        try {
//            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // ???????????????????????????????????????????????????????????????????????????????????????
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // ????????????
                    if (keyboardUtil.isShow()) {
                        keyboardUtil.hideKeyboard();
                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                View view = getCurrentFocus();
                hideKeyboard(ev, binding.keyboardView);//??????????????????????????????????????????
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.finish:
                if (Utils.isFastClick()) {
                    Utils.snackbar(AddCarActivity.this, "????????????");
                    return;
                }
                String carPlate = binding.etCarplate.getText().toString();
                if (TextUtils.isEmpty(carPlate)) {
                    Utils.snackbar(AddCarActivity.this, "????????????????????????");
                    return;
                }
                if (carPlate.length() > 8) {
                    Utils.snackbar(AddCarActivity.this, "??????????????????????????????8???");
                    return;
                }
                if (carPlate.length() < 7) {
                    Utils.snackbar(AddCarActivity.this, "??????????????????????????????7???");
                    return;
                }
//                if (!checkCarNumber(carPlate)) {
//                    Utils.snackbar(AddCarActivity.this, "????????????????????????");
//                    return;
//                }
                if (TextUtils.isEmpty(binding.etMileage.getText().toString())) {
                    Utils.snackbar(AddCarActivity.this, "?????????????????????");
                    return;
                }
                commit(carPlate, binding.etMileage.getText().toString());
                break;
            case R.id.img_gettbox:
                String carPlates = binding.etCarplate.getText().toString();
                if (TextUtils.isEmpty(carPlates)) {
                    Utils.snackbar(AddCarActivity.this, "?????????????????????");
                    return;
                }
                if (carPlates.length() > 8) {
                    Utils.snackbar(AddCarActivity.this, "??????????????????????????????8???");
                    return;
                }
                if (carPlates.length() < 7) {
                    Utils.snackbar(AddCarActivity.this, "??????????????????????????????7???");
                    return;
                }
//                if (!checkCarNumber(carPlates)) {
//                    Utils.snackbar(AddCarActivity.this, "????????????????????????");
//                    return;
//                }
                gettBox(carPlates);
                break;
            case R.id.l1:
                new AlertDialog.Builder(this).setMessage("???????????????????????????").setPositiveButton("?????????", new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(this).setMessage("???????????????????????????").setPositiveButton("?????????", new DialogInterface.OnClickListener() {
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
                new AlertDialog.Builder(this).setMessage("???????????????????????????").setPositiveButton("?????????", new DialogInterface.OnClickListener() {
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

    private void loadOssToken() {
        new MyRequest().myRequest(API.osstoken, false, null, true, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                String ak = json.get("AccessKeyId").getAsString();
                String sk = json.get("AccessKeySecret").getAsString();
                String token = json.get("SecurityToken").getAsString();
                String expiration = json.get("Expiration").getAsString();
                SharePref.put(AddCarActivity.this, API.ak, ak);
                SharePref.put(AddCarActivity.this, API.sk, sk);
                SharePref.put(AddCarActivity.this, API.myosstoken, token);
                SharePref.put(AddCarActivity.this, API.expiration, expiration);
                alioss = AliOSS.getOss(AddCarActivity.this);
            }
        });
    }

    private void gettBox(String carPlates) {
        HashMap<String, String> map = new HashMap<>();
        map.put("plate", carPlates);
        new MyRequest().spliceJson(API.gettbox, false, "plate=" + carPlates, this, null, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                //"vin": "LDP53A938HC101517",
                //        "total_miles": 129.6
                if (json.has("vin")) {
                    binding.tvFrame.setVisibility(View.VISIBLE);
                    binding.tvFrame.setText("????????????  " + json.get("vin").getAsString());
                }
                if (json.has("total_miles")) {
                    double total_miles = json.get("total_miles").getAsDouble();
                    binding.etMileage.setText((int) total_miles + "");
                }
                if (json.has("car_type")) {
                    car_type = json.get("car_type").getAsInt();
                }
            }
        });
    }

    private void commit(String carPlate, String mile) {
        if (path1 == null || path2 == null || path3 == null) {
            Utils.snackbar(this, "????????????????????????");
            return;
        }
        List<String> pathlist = new ArrayList<>();
        List<String> objectkeylist = new ArrayList<>();
        pathlist.add(path1);
        pathlist.add(path2);
        pathlist.add(path3);
        promptDialog.showLoading("????????????????????????");
        try {
            recursiveUploadPhoto(Utils.getAccount(this), carPlate, Double.valueOf(mile).intValue(), pathlist, objectkeylist);
        } catch (Exception e) {
            Utils.snackbar(this, "?????????????????????");
            promptDialog.dismiss();
        }
    }

    /**
     * ?????????????????????
     */
    private void recursiveUploadPhoto(final String account, String carplate, int mile, List<String> pathlist, List<String> objectjetlist) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String objectKey = account + "_" + System.currentTimeMillis() + ".jpg";
                PutObjectRequest put = new PutObjectRequest(Utils.getBucketName(AddCarActivity.this), objectKey, decodeSampledBitmap(pathlist.get(0), 600, 400));
                pathlist.remove(0);
                objectjetlist.add(objectKey);
                try {
                    PutObjectResult putResult = alioss.putObject(put);
                    Log.d(Utils.TAG, "UploadSuccess");
                    Log.d(Utils.TAG, putResult.getETag());
                    Log.d(Utils.TAG, putResult.getRequestId());
                    if (pathlist.size() > 0) {
                        recursiveUploadPhoto(Utils.getAccount(AddCarActivity.this), carplate, mile, pathlist, objectjetlist);
                    } else {
                        upload(objectjetlist, carplate, mile);
                    }
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (ServiceException serviceException) {
                }
            }
        });
    }

    private void upload(List<String> list, String carplate, int mile) {
        //plate_number  miles  photos
        String string = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                string = string + list.get(i) + ";";
            } else {
                string = string + list.get(i);
            }
        }
        String json = JsonSplice.leftparent + JsonSplice.yin + "plate" + JsonSplice.yinandmao + JsonSplice.yin + carplate + JsonSplice.yinanddou +
                JsonSplice.yin + "miles" + JsonSplice.yinandmao + mile + JsonSplice.dou +
                JsonSplice.yin + "photos" + JsonSplice.yinandmao + JsonSplice.yin + string + JsonSplice.yin + JsonSplice.rightparent;
        new MyRequest().spliceJson(API.bind, true, json, this, promptDialog, null, new Response() {
            @Override
            public void response(JsonObject json, JsonArray jsonArray, boolean isNull) {
                Utils.snackbar(AddCarActivity.this, "????????????");
                finish();
            }
        });
    }

    public byte[] decodeSampledBitmap(String pathName, int reqWidth, int reqHeight) {
        Bitmap bitmaps =  BitmapFactory.decodeFile(pathName);
        Log.d(Utils.TAG, bitmaps.getWidth()+"?????????????????????"+bitmaps.getHeight());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap =  BitmapFactory.decodeFile(pathName, options);
        Log.d(Utils.TAG, bitmap.getWidth()+"??????????????????????????????"+bitmap.getHeight());
        return compressImage(bitmap);
    }

    /*
     * ????????????
     * */
    private byte[] compressImage(Bitmap image) {
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

    /**
     * ?????????????????? ??????  ????????????????????????????????????
     * ??????????????????o???l
     */
//    public boolean checkCarNumber(String content) {
//        String pattern = "([?????????????????????????????????????????????????????????????????????????????????????????????]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}???)))|([0-9]{6}???)|((([????????????????????????????????????]{1}A)|???B|???D|???E|???H)[0-9]{4}???)|(WJ[??????????????????????????????????????????????????????????????????????????????????????????????????]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
//        return Pattern.matches(pattern, content);
//    }

    private String mCameraImagePath;

    /**
     * ??????????????????
     */
    private void xiangce(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, position);
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (isSelectCamera) {
                        binding.img1.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                        path1 = mCameraImagePath;
                    } else {
                        path1 = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                        binding.img1.setImageBitmap(BitmapFactory.decodeFile(path1));
                    }
                    break;
                case 2:
                    if (isSelectCamera) {
                        binding.img2.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                        path2 = mCameraImagePath;
                    } else {
                        path2 = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
                        binding.img2.setImageBitmap(BitmapFactory.decodeFile(path2));
                    }
                    break;
                case 3:
                    if (isSelectCamera) {
                        binding.img3.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                        path3 = mCameraImagePath;
                    } else {
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
