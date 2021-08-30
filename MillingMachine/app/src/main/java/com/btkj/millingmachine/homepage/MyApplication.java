package com.btkj.millingmachine.homepage;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.btkj.millingmachine.errorview.GuzhangInterface;
import com.btkj.millingmachine.selftcult.ConnectJiliangHeartBeatInterface;
import com.btkj.millingmachine.selftcult.ConnectWuliHeartBeatInterface;
import com.btkj.millingmachine.selftcult.IsNianmiInterface;
import com.btkj.millingmachine.selftcult.QuemiInterface;
import com.btkj.millingmachine.selftcult.SelfcultFinishInterface;
import com.btkj.millingmachine.serialportutil.RiceUtil;
import com.btkj.millingmachine.serialportutil.SerialPortHandler;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.SoundPlayUtils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.google.gson.JsonObject;

/**
 * created by xuedi on 2019/6/3
 */
public class MyApplication extends Application {
    public SerialPortHandler serialPortHandlerwuli;
    private SerialPortHandler serialPortHandlerjiliang;
    private static MyApplication getMe;

    @Override
    public void onCreate() {
        super.onCreate();
        closeTitle();//关闭标题栏
        appAutomaticStart();//主app自启动
        getMe = this;
        SoundPlayUtils.init(this);
    }

    public static MyApplication getInstance() {
        return getMe;
    }

    //初始化物理串口
    public void initWuliSerialPort() {
        if (serialPortHandlerwuli == null) {
            serialPortHandlerwuli = new SerialPortHandler((String) SharePref.get(getApplicationContext(), API.wuli, "")
                    , Integer.parseInt((String) SharePref.get(getApplicationContext(), API.wulibote, "")), getApplicationContext());
        }
    }

    //初始化计量串口
    public void initJiliangSerialPort() {
        if (serialPortHandlerjiliang == null) {
            serialPortHandlerjiliang = new SerialPortHandler((String) SharePref.get(getApplicationContext(), API.jiliang, "")
                    , Integer.parseInt((String) SharePref.get(getApplicationContext(), API.jiliangbote, "")), getApplicationContext());
        }
    }

    public void sendWuliCmd(String command) {
        serialPortHandlerwuli.sendCmd(RiceUtil.convertCmd(RiceUtil.hexStringToByte(command)));
    }

    public void sendChaxunmicang(String command, Context context, boolean ismain){
        serialPortHandlerwuli.sendChaxunmicangCmd(RiceUtil.convertCmd(RiceUtil.hexStringToByte(command)), context, ismain);
    }

    public void sendFuWeiCmd(String command, boolean isupdate, Context context){
        serialPortHandlerwuli.sendFuWei(RiceUtil.convertCmd(RiceUtil.hexStringToByte(command)), isupdate, context);
    }

    public void sendWuliCmd(byte[] a, int offset, int length ) {
        serialPortHandlerwuli.sendCmd(a, offset, length);
    }

    public void sendShichaQidong(byte[] data){
        serialPortHandlerwuli.sendShiChaQiDong(RiceUtil.convertCmd(data));
    }

    public void isNianmi(IsNianmiInterface isNianmiInterface){
        serialPortHandlerwuli.isnianmi(isNianmiInterface);
    }

    public void sendGuzhangWuliCmd(String command, GuzhangInterface guzhangInterface) {
        serialPortHandlerwuli.sendGuzhangCmd(RiceUtil.convertCmd(RiceUtil.hexStringToByte(command)), guzhangInterface);
    }

    public void sendJiliangCmd(String command, SelfcultFinishInterface finishInter) {
        serialPortHandlerjiliang.sendJiliangCmd(RiceUtil.hexStringToByte(command), finishInter);
    }

//    public void sendWuliHeartBeat(String command, ConnectWuliHeartBeatInterface beatInterface){
//        serialPortHandlerwuli.sendWuliHeartCmd(RiceUtil.convertCmd(RiceUtil.hexStringToByte(command)), beatInterface);
//    }

    public void sendQuemiHeartBeat(String command, QuemiInterface beatInterface){
        serialPortHandlerwuli.sendQueMiHeartCmd(RiceUtil.convertCmd(RiceUtil.hexStringToByte(command)), beatInterface);
    }

//    public void sendJiliangHeartBeat(String command, ConnectJiliangHeartBeatInterface beatInterface){
//        serialPortHandlerjiliang.sendJiliangHeartBeatCmd(RiceUtil.hexStringToByte(command), beatInterface);
//    }

    //主app自启动
    private void appAutomaticStart() {
        /**
         * 调用示例:
         * Intent intent = new Intent("android.intent.action.hongdian.app.write");
         * intent.putExtra(“content”, “{"packagename":"com.example.fixture"}”);
         * sendBroadcast(intent);
         * Intent intent = new Intent("android.intent.action.hongdian.app.write");
         * intent.putExtra(“content”, “{"status", "on"}”);
         * sendBroadcast(intent);
         */
        Intent intent = new Intent("android.intent.action.hongdian.app.write");
        String pack = "{\"packagename\":\"com.btkj.millingmachine\"}";
        intent.putExtra("content", pack);
        sendBroadcast(intent);
        Intent appintent = new Intent("android.intent.action.hongdian.app.write");
        appintent.putExtra("content", "{\"status\":\"on\"}");
        sendBroadcast(appintent);
    }

    //关闭标题栏
    private void closeTitle() {
        Intent intent = new Intent("android.intent.action.hongdian.statusbar.write");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "off");
        intent.putExtra("content", jsonObject.toString());
        sendBroadcast(intent);
    }
}
