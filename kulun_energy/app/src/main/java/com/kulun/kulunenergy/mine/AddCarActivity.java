package com.kulun.kulunenergy.mine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.databinding.DataBindingUtil;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.kulun.kulunenergy.R;
import com.kulun.kulunenergy.customizeView.AliOSS;
import com.kulun.kulunenergy.customizeView.getPhotoFromPhotoAlbum;
import com.kulun.kulunenergy.databinding.ActivityAddcarBinding;
import com.kulun.kulunenergy.main.BaseActivity;
import com.kulun.kulunenergy.model.User;
import com.kulun.kulunenergy.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * created by xuedi on 2019/8/8
 */
public class AddCarActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAddcarBinding binding;
    private long time;
    private boolean isSelectCamera = true;
    private String path1, path2, path3;
    private PromptDialog promptDialog;
    private String bucketName = "";

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addcar);
        binding.head.title.setText("????????????");
        binding.tvCommit.setOnClickListener(this);
        binding.l1.setOnClickListener(this);
        binding.l2.setOnClickListener(this);
        binding.l3.setOnClickListener(this);
        promptDialog = new PromptDialog(this);
//        binding.imgFinish.setOnClickListener(this);
//        binding.imgNext.setOnClickListener(this);
//        binding.layoutHeader.left.setOnClickListener(this);
//        binding.layoutHeader.right.setOnClickListener(this);
//        binding.layoutHeader.title.setText("????????????");
//        islogin = getIntent().getBooleanExtra(API.islogin, false);
//        binding.layoutHeader.right.setVisibility(islogin?View.VISIBLE:View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.tv_commit:
                if (System.currentTimeMillis() - time < 5) {
                    time = System.currentTimeMillis();
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
                if (!checkCarNumber(carPlate)) {
                    Utils.snackbar(AddCarActivity.this, "????????????????????????");
                    return;
                }
                commit();
                break;
            case R.id.img_gettbox:
                String carPlates = binding.etCarplate.getText().toString();
                if (TextUtils.isEmpty(carPlates)) {
                    Utils.snackbar(AddCarActivity.this, "?????????????????????");
                    return;
                }
                gettBox();
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

    private void gettBox() {

    }

    private List<String> pathlist = new ArrayList<>();

    private void commit() {
        pathlist.clear();
        if (path1 == null || path2 == null || path3 == null) {
            Utils.snackbar(this, "????????????????????????");
            return;
        }
        pathlist.add(path1);
        pathlist.add(path2);
        pathlist.add(path3);
        promptDialog.showLoading("????????????????????????");
        recursiveUploadPhoto(User.getInstance().getAccountId(this));
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
                    if (pathlist.size() > 0) {
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

    private void upload(List<String> list) {

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

    /**
     * ?????????????????? ??????  ????????????????????????????????????
     * ??????????????????o???l
     */
    public boolean checkCarNumber(String content) {
        String pattern = "([?????????????????????????????????????????????????????????????????????????????????????????????]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}???)))|([0-9]{6}???)|((([????????????????????????????????????]{1}A)|???B|???D|???E|???H)[0-9]{4}???)|(WJ[??????????????????????????????????????????????????????????????????????????????????????????????????]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        return Pattern.matches(pattern, content);
    }

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
