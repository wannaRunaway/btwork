package com.kulun.kulunenergy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompressUtils extends AsyncTask<List<String>, Void, List<String>> {

    private ImageCallBack callBack;

    public CompressUtils(ImageCallBack callBack) {
        this.callBack=callBack;
    }

    @Override
    protected List<String> doInBackground(List<String>... params) {
        List<String> list=params[0];
        List<String> result=new ArrayList<>();
        if(list!=null){
            for (int i = 0; i <list.size() ; i++) {
                String path=list.get(i);
                if(path.endsWith("jpg") ||path.endsWith("JPG") || path.endsWith("png") ||path.endsWith("PNG") || path.endsWith("JPEG") ||path.endsWith("jpeg")){//目前只支持这几种格式压缩
                    String name=path.substring(path.lastIndexOf("/")+1);
                    String IMAGES_PATH = Environment.getExternalStorageDirectory() + "/dongPic/";
                    File imgFile = new File(IMAGES_PATH);
                    if (!imgFile.exists()) {
                        imgFile.mkdir();
                    }
                    String compressPath=IMAGES_PATH+name;
                    if(compressQuality(path,compressPath)){
                        result.add(compressPath);
                    }else {
                        result.add(path);
                    }
                }else {
                    result.add(path);
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<String> result) {
        if(callBack!=null){
            callBack.finish(result);
        }
    }
    public interface ImageCallBack {
         void finish(List<String> path);
    }

    private boolean compressQuality(String filePath, String savePath){
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(savePath));
            if(bitmap!=null){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);// (0-100)压缩文件
            }
            return  true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
