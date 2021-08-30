package com.kulun.energynet.main;

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
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.kulun.energynet.R;
import com.kulun.energynet.utils.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @Title UpdateManager.java
 * @package cn.roboca.version
 * @Description 软件版本更新的类，可用于前台软件下载，自动安装等。
 * @author PW
 * @date 2014-4
 */
public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private String mUrl;
    private String mVerName;

    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private MyHandler mHandler;

    /**
     * 回到主进程处理信息
     * @return
     */
   /* @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };*/
    static class MyHandler extends Handler{
        WeakReference<MainActivity> weakReference;
        public MyHandler(MainActivity activity){
            weakReference=new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            MainActivity activity=weakReference.get();
            if(activity==null){
                return;
            }
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    activity.setProgress();
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    activity.installApk();
                    break;
                default:
                    break;
            }
        }
    }



    public UpdateManager(Context context, String verName, String versionUrl) {
        this.mContext = context;
        this.mUrl=versionUrl;
        this.mVerName=verName;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(boolean isqiangzhi, String content) {
        if (isUpdate()) {
            // 显示提示对话框
            showNoticeDialog(isqiangzhi, content);
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private boolean isUpdate() {
        // 获取当前软件版本
        String versionName = getVersionName(mContext);
        if (getdeletepoint(mVerName) > getdeletepoint(versionName)) {
            return true;
        }
        return false;
    }

    public int getdeletepoint(String str){
        String s = str.replace(".", "");
        Utils.log(null,"","软件版本"+Integer.parseInt(s));
        return Integer.parseInt(s);
    }


    /**
     * 获取软件版本号
     *
     * @param context 绑定的界面信息
     * @return
     */
    private String getVersionName(Context context) {
        String versionName = "";
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(this.getClass().getName(),e.getMessage());
        }
        return versionName;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(boolean isqiangzhi, String content) {
        if (isqiangzhi){
            // 构造对话框
            new Builder(mContext).setTitle("软件更新")
                    .setCancelable(false).setMessage(content)
                    .setPositiveButton("更新", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // 显示下载对话框
                            showDownloadDialog();
                        }
                    }).show();
        }else {
            // 构造对话框
            new Builder(mContext).setTitle("软件更新")
                    .setCancelable(false).setMessage(content)
                    .setPositiveButton("更新", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // 显示下载对话框
                            showDownloadDialog();
                        }
                    })
                    .setNegativeButton("稍后更新", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        if((MainActivity) mContext!=null){
            mHandler=new MyHandler((MainActivity) mContext);
        }
        Builder builder = new Builder(mContext);
        builder.setTitle("正在更新");
        builder.setCancelable(false);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }



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
     *@author coolszy
     *@date 2012-4-26
     *@blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdcard = Environment.getExternalStorageDirectory().toString();
                    URL url = new URL(mUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    String apkName = mUrl.substring(mUrl.lastIndexOf("/") + 1, mUrl.length());
                    File apkFile = new File(sdcard, apkName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
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
            mDownloadDialog.dismiss();
        }
    };

    public void setProgress(){
        if(mDownloadDialog!=null){
            mProgress.setProgress(progress);
        }
    }

    /**
     * 安装APK文件
     */
    public void installApk() {
        String apkName = mUrl.substring(mUrl.lastIndexOf("/") + 1, mUrl.length());
        File apkfile = new File(Environment.getExternalStorageDirectory().toString(), apkName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            String[] command = {"chmod", "777", apkfile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", apkfile);  //包名.fileprovider
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}

