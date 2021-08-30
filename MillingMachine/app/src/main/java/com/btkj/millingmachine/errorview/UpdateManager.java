package com.btkj.millingmachine.errorview;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.btkj.millingmachine.BuildConfig;
import com.btkj.millingmachine.R;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.serialutil.FileProcessorImpl;
import com.btkj.millingmachine.serialutil.PacketProcessorImpl;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author PW
 * @Title UpdateManager.java
 * @package cn.roboca.version
 * @Description 软件版本更新的类，可用于前台软件下载，自动安装等。
 * @date 2014-4
 */
public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private String mUrl;
    private String mVerName;
    private int mSerCode;

    private Context mContext;
    private boolean isDevice;
    /* 更新进度条 */
//    private ProgressBar mProgress;
//    private Dialog mDownloadDialog;

    /**
     * 回到主进程处理信息
     *
     * @return
     */
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                // 正在下载
//                case DOWNLOAD:
//                    // 设置进度条位置
////                    mProgress.setProgress(progress);
//                    break;
//                case DOWNLOAD_FINISH:
//
//                    break;
//                default:
//                    break;
//            }
//        }

        ;
//    };

    public UpdateManager(Context context, int serCode, String verName, String versionUrl, boolean isDevice) {
        this.mContext = context;
        this.mUrl = versionUrl;
        this.mVerName = verName;
        this.mSerCode = serCode;
        this.isDevice = isDevice;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        // 显示提示对话框
//            showNoticeDialog();
//        showDownloadDialog();
        downloadApk();
    }

    /**
     * 显示软件下载对话框
     */
//    private void showDownloadDialog() {
//        // 构造软件下载对话框
//        Builder builder = new Builder(mContext);
//        builder.setTitle("正在更新");
//        builder.setCancelable(false);
//        // 给下载对话框增加进度条
//        final LayoutInflater inflater = LayoutInflater.from(mContext);
//        View v = inflater.inflate(R.layout.softupdate_progress, null);
////        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//        builder.setView(v);
//        // 取消更新
//        mDownloadDialog = builder.create();
//        mDownloadDialog.show();
//        // 现在文件
//        downloadApk();
//    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private File apkFile;

    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(mUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    apkFile = new File(mSavePath, mVerName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        Utils.log("numread"+numread+"progress:"+progress);
                        // 更新进度
//                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
//                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
//                            break;
                            if (isDevice) {
                                FileProcessorImpl fileProcessor = new FileProcessorImpl();
                                if (fileProcessor.openAndVerifyFile(apkFile.getAbsolutePath())) {
                                    Utils.log(apkFile.getAbsolutePath() + "文件下载成功：" + apkFile.length());
                                    MyApplication.getInstance().sendFuWeiCmd(API.fuwei, true, mContext);
                                }
                                else {
                                    Utils.log(apkFile.getAbsolutePath() + "文件下载失败：" + apkFile.length());
                                }
                            } else {
                                installApk();  // 安装文件
                            }
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
//            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, mVerName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        /**
         * 判断是否是AndroidN以及更高的版本
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}

