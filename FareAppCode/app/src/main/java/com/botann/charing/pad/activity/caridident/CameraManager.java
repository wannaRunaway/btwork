package com.botann.charing.pad.activity.caridident;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class CameraManager {

    private Camera mCamera;
    private static CameraManager cameraManager;

    public static CameraManager get() {
        if(cameraManager == null)
            cameraManager = new CameraManager();
        return cameraManager;
    }

    public void cameraFocus(){
        if (mCamera != null){
            try{
                mCamera.autoFocus(null);
//                handler.postDelayed(runnable, 500);
            }catch (Exception e){
            }
        }
    }

//    public void unRegisterCameraFocus(){
//        if (mCamera != null && handler != null){
//            handler.removeCallbacks(runnable);
//        }
//    }

    /**
     * 开关闪光灯
     *
     * @return 闪光灯是否开启
     */
    public boolean switchFlashLight(Context context) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters != null && parameters.getFlashMode()!= null) {
                if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(parameters);
                    return true;
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(parameters);
                    return false;
                }
            }else {
                Toast.makeText(context, "此设备没有闪光灯功能", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public void openDevice(SurfaceHolder holder, Context context){
        if (null == mCamera) {
            try {
                mCamera = Camera.open();
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPictureFormat(ImageFormat.JPEG);
                parameters.setRotation(90);
                checkPreviewSize(parameters, mCamera);
                parameters.setPictureSize(1920, 1080);
                Log.d("btkj",parameters.getPictureSize().width+Build.MODEL+"sss"+parameters.getPictureSize().height+"ss"+parameters.getPreviewSize().width+"SS"+parameters.getPreviewSize().height);
                mCamera.setParameters(parameters);
                mCamera.setDisplayOrientation(90);
                reStartPreView(holder);
//                cameraFocus();
            } catch (Exception e) {
                Toast.makeText(context, "摄像头开启失败, 可能是没有获取到摄像头权限,请检查.", Toast.LENGTH_SHORT)
                        .show();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        } else {
            reStartPreView(holder);
        }
    }

    public void checkPreviewSize(Camera.Parameters parameters, Camera camera){
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        parameters.setPreviewSize(bestSize.width, bestSize.height);
    }

    public void closeDvice(){
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 重新开始识别
     */
    public void reStartPreView(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                cameraFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Camera getCamera() {
        return mCamera;
    }
}
