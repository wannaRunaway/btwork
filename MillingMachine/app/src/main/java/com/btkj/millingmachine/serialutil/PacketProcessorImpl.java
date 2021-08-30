package com.btkj.millingmachine.serialutil;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.btkj.millingmachine.errorview.UpgradeActivity;
import com.btkj.millingmachine.homepage.MainActivity;
import com.btkj.millingmachine.homepage.MyApplication;
import com.btkj.millingmachine.network.Customize;
import com.btkj.millingmachine.serialportutil.RiceUtil;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

import android_serialport_api.SerialPort;
//import gnu.io.SerialPort;
//import gnu.io.UnsupportedCommOperationException;

public class PacketProcessorImpl extends AbsPacketDecomposer implements IPacketProcessor {
    private IPacketComposer packetComposer = new PacketComposerImpl();


    public void writePacket(PacketType packetType, int contentOffset, int contentLen) {
        int packetLen = packetComposer.packetComposite(packetType, contentOffset, contentLen);
//        SerialComProcessor.sendToPort(serialPort, packetComposer.getOut_packet(), packetLen);
        MyApplication.getInstance().sendWuliCmd(packetComposer.getOut_packet(), 0, packetLen);
    }

    @Override
    public boolean processPacket(PacketType packetType, int packetDataOffset, Context context) {
        if (packetType == null) {
            Utils.log("Packet type is null.");
            return false;
        }
        switch (packetType) {
            case D_CON_REQ:
                this.writePacket(PacketType.U_CON_ACK, 0, 0);
                this.writePacket(PacketType.U_UPDATE_REQ, 0, 0);
                break;
            case D_UPDATE__ACK_REQ:
                this.writePacket(PacketType.U_UPDATE_ACK_PROFILE, 0, 0);
                break;
            case D_LOAD_DATA_REQ:
                int fileContentOffset = this.decodePacketInt(packetDataOffset);
                int fileContentLen = this.decodePacketInt(packetDataOffset+4);
                this.writePacket(PacketType.U_LOAD_DATA_ACK, fileContentOffset, fileContentLen);
                break;
            case D_UPDATE_RESULT:
                int updateRst = this.decodePacketByte(packetDataOffset);
                Utils.log(updateRst+"远程升级主板bootloader--返回");
                MyApplication.getInstance().serialPortHandlerwuli.isupdate = false;
                ((UpgradeActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadData(context, updateRst);
                    }
                });
                break;
            default:
                    Utils.log("Invalid packet type:" + packetType);
                    return false;
        }
        return true;
    }

    private void uploadData(Context context, int updateRst) {
        /**
         *  deviceId int   设备id
            firmwareUpdate Short 状态，升级成功0，升级失败>1
         *  firmwareHversion String 非必填 硬件版本；firmwareSversion String 非必填 软件版本
         */
        String url = API.BASE_URL + API.deviceUpload;
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = new HashMap<>();
        map.put("deviceId",  String.valueOf(SharePref.get(context, API.id, 0)));
        map.put("firmwareUpdate", updateRst == 1?"0":String.valueOf(updateRst));
        map.put("firmwareHversion", FileInfo.getInstance().getHwVersionHex());
        map.put("firmwareSversion", FileInfo.getInstance().getSwVersionHex());
        requestParams.add(Customize.SIGN, Customize.getRequestParams(map, requestParams));
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
        asyncHttpClient.setCookieStore(persistentCookieStore);
        Utils.log(requestParams.toString());
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                Utils.log(json);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Utils.log("网络异常");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }
    /**
     *  if (isDevice) {
     *                         FileProcessorImpl fileProcessor = new FileProcessorImpl();
     *                         fileProcessor.openAndVerifyFile(apkFile.getAbsolutePath());
     *                         Utils.log(apkFile.getAbsolutePath() + "文件路径" + apkFile.length());
     *                         MyApplication.getInstance().sendFuWeiCmd(API.fuwei, true, mContext);
     *                     } else {
     *                         installApk();  // 安装文件
     *                     }
     */
}
