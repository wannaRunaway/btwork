package com.btkj.millingmachine.serialportutil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.btkj.millingmachine.errorview.GuzhangInterface;
import com.btkj.millingmachine.errorview.MarginLackActivity;
import com.btkj.millingmachine.selftcult.IsNianmiInterface;
import com.btkj.millingmachine.selftcult.QuemiInterface;
import com.btkj.millingmachine.selftcult.SelfcultFinishInterface;
import com.btkj.millingmachine.serialutil.IPacketReceiver;
import com.btkj.millingmachine.serialutil.PacketProcessorImpl;
import com.btkj.millingmachine.util.Utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * created by xuedi on 2019/6/13
 */
public class SerialPortHandler {
    private SerialPort sp;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ReadThread rThread;
    private Context context;
    private IPacketReceiver packetReceiver = new PacketProcessorImpl();

    public SerialPortHandler(String chuankoumingcheng, int botelv, Context context) {
        try {
            this.context = context;
            sp = new SerialPort(new File(chuankoumingcheng), botelv, 0);
            inputStream = sp.getInputStream();
            outputStream = sp.getOutputStream();
            Utils.log("串口打开成功,串口名称" + chuankoumingcheng + "波特率:" + botelv);
            rThread = new ReadThread();
            rThread.start();
        } catch (Exception e) {
            Utils.log("串口打开失败");
        }
    }

    private class ReadThread extends Thread {
        public void run() {
            super.run();
            byte[] buffer = new byte[1024];
            while (!isInterrupted()) {
                int size;
                try {
                    if (inputStream == null)
                        return;
                    if (inputStream.available() <= 0) {
                        continue;
                    } else
                        Thread.sleep(50);
                    size = inputStream.read(buffer);
                    if (size > 0) {
                        byte[] newBuffer = new byte[size];
                        for (int i = 0; i < size; i++) {
                            newBuffer[i] = buffer[i];
                            if(isupdate) {
                                packetReceiver.storeRcvByte(buffer[i], mContext);
                            }
                        }
                        if (!isupdate) {
                            receiveMainboard(newBuffer, size);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.log(" read error" + e.toString());
                }
            }
        }
    }

    public boolean isupdate = false;
    public boolean isquemi = false;
    public boolean isnianmi = false;
    private Context mContext;
    public boolean sendFuWei(byte[] data, boolean isupdate, Context context) {
        this.isupdate = isupdate;
        this.mContext = context;
        try {
            outputStream.write(data);
            Log.d("xuedi", "发送指令" + RiceUtil.bytesToHexString(data, data.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendCmd(byte[] data) {
        try {
            outputStream.write(data);
            Log.d("xuedi", "发送指令" + RiceUtil.bytesToHexString(data, data.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Context micangContext;
    private boolean ismain;
    public boolean sendChaxunmicangCmd(byte[] data, Context context, boolean ismain) {
        micangContext = context;
        this.ismain = ismain;
        try {
            outputStream.write(data);
            Log.d("xuedi", "发送指令" + RiceUtil.bytesToHexString(data, data.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendCmd(byte[] data, int offset, int length) {
        try {
            outputStream.write(data, offset, length);
            Log.d("xuedi", "发送指令" + RiceUtil.bytesToHexString(data, data.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private IsNianmiInterface isNianmiInterface;
    public void isnianmi(IsNianmiInterface isNianmiInterface){
        this.isNianmiInterface = isNianmiInterface;
    }

    public boolean sendShiChaQiDong(byte[] data){
        try {
            outputStream.write(data);
            Log.d("xuedi", "发送指令" + RiceUtil.bytesToHexString(data, data.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private GuzhangInterface guzhangInterface;

    public boolean sendGuzhangCmd(byte[] data, GuzhangInterface guzhangInterface) {
        this.guzhangInterface = guzhangInterface;
        try {
            outputStream.write(data);
            Log.d("xuedi", "发送指令" + RiceUtil.bytesToHexString(data, data.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    private ConnectWuliHeartBeatInterface wuliHeartBeatInterface;
//
//    public boolean sendWuliHeartCmd(byte[] data, ConnectWuliHeartBeatInterface wuliHeartBeatInterface) {
//        this.wuliHeartBeatInterface = wuliHeartBeatInterface;
//        try {
//            outputStream.write(data);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    private QuemiInterface quemiInterface;

    public boolean sendQueMiHeartCmd(byte[] data, QuemiInterface quemiInterface) {
        this.quemiInterface = quemiInterface;
        try {
            outputStream.write(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    private ConnectJiliangHeartBeatInterface jiliangHeartBeatInterface;
//
//    public boolean sendJiliangHeartBeatCmd(byte[] data, ConnectJiliangHeartBeatInterface jiliangHeartBeatInterface) {
//        this.jiliangHeartBeatInterface = jiliangHeartBeatInterface;
//        try {
//            outputStream.write(data);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    private SelfcultFinishInterface finishInterface;

    public boolean sendJiliangCmd(byte[] data, SelfcultFinishInterface finishInterface) {
        this.finishInterface = finishInterface;
        try {
            outputStream.write(data);
            Log.d("xuedi", RiceUtil.bytesToHexString(data, data.length));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void receiveMainboard(byte[] buffer, int size) {
        //防止粘连处理
//        int cmdNum = 0;
//        byte[][] newData = new byte[10][];
        try {
//            for (int i = 0; i < size; i++) {
//                if (buffer[i] == 0x7E) {
//                    int len = 4 + buffer[i + 2];
//                    newData[cmdNum] = new byte[len];
//                    int z = 0;
//                    newData[cmdNum][z] = buffer[i];
//                    i++;
//                    z++;
//                    newData[cmdNum][z] = buffer[i];
//                    i++;
//                    z++;
//                    int dataLen = (int) buffer[i];
//                    newData[cmdNum][z] = buffer[i];
//                    i++;
//                    z++;
//                    for (int t = 0; t < dataLen; t++) {
//                        newData[cmdNum][z] = buffer[i];
//                        i++;
//                        z++;
//                    }
//                    newData[cmdNum][z] = buffer[i];
//                    cmdNum++;
//                }
//            }
            String back = RiceUtil.bytesToHexString(buffer, size);
//            if (back.indexOf("23C7") != -1) {
//                if (wuliHeartBeatInterface != null) {
//                    Utils.log("物理心跳");
//                    wuliHeartBeatInterface.wuliHeartBeat();
//                }
//            }
            if (back.indexOf("23C702CC00") != -1) {
                if (!isquemi) {
                    Intent intent = new Intent(micangContext, MarginLackActivity.class);
                    intent.putExtra("ismain", ismain);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    micangContext.startActivity(intent);
                }
            }
            if (back.indexOf("23C702F100") != -1) {
                if (isquemi) {
                    if (quemiInterface != null) {
                        quemiInterface.lack();
                    }
                }
            }
//            if (back.indexOf("0103") != -1) {
//                if (jiliangHeartBeatInterface != null) {
//                    Utils.log("计量心跳");
//                    jiliangHeartBeatInterface.jiliangHeartBeat();
//                }
//            }
            if (back.indexOf("23C8") != -1) {
                if (guzhangInterface != null) {
                    guzhangInterface.message(back);
                }
            }
            if (back.indexOf("23D301CC") != -1){
                if (isNianmiInterface != null && isnianmi) {
                    isNianmiInterface.isguozai();
                }
            }
            if (back.indexOf("0103") != -1) {
                //01030400050000EA32
                String result = back.substring(6, 10);
                long resultLong = Long.parseLong(result, 16);
                if (resultLong < 10000) { //偶尔有数值特别大的时候
                    if (finishInterface != null) {
                        finishInterface.selffinsh(resultLong);
                    }
                }
                Utils.log("16进制的碾米数量:" + result + "10进制的碾米数量:" + resultLong);
            }
            Utils.log("Main data 主板返回：" + RiceUtil.bytesToHexString(buffer, size));
        } catch (Exception e) {
            e.printStackTrace();
//            if (wuliHeartBeatInterface != null) {
//                Utils.log("物理心跳");
//                wuliHeartBeatInterface.wuliHeartBeat();
//            }
            Utils.log("Main data 主板返回：发生了错误" + e);
        }
    }

//    /**
//     * app协议文档：
//     * 7E A0 01 30 EF Main data 主板返回：7E A0 01 55 8A
//     * 7E A1 01 99 47 Main data 主板返回：7E A1 01 55 8B
//     * 7E A2 01 AA 77 Main data 主板返回：7E A2 01 55 88 7E A7 02 00 01 DA
//     * 7E A7 01 AA 72 Main data 主板返回：7E A7 02 00 01 DA  7E A7 02 01 01 DB
//     * 7E A8 01 AA 7D Main data 主板返回：7E A8 06 00 00 00 00 00 00 D0
//     * 7E AA 01 10 C5 Main data 主板返回：7E AA 01 55 80
//     * 日志没有看到A3、A5、A6、A9
//     */
//    public static boolean protocalReceive(String stringList, Context context) {
//        boolean isSuccess = false;
//        String order = stringList.substring(6, 8);
//        ArrayList<String> malfunctionList = new ArrayList<>();
//        switch (stringList.substring(2, 4)) {
//            case "A0": //开始碾米
//                if (order.equals("55")) {
//                    isSuccess = true;
//                }
//                break;
//            case "A1": //停止碾米
//                if (order.equals("AA")) {
//                    isSuccess = true;
//                }
//                break;
//            case "A2": //结束交易
//                if (order.equals("55")) {
//                    isSuccess = true;
//                }
//                break;
//            case "A5": //取米仓灯带操作
//                if (order.equals("55")) {
//                    isSuccess = true;
//                }
//                break;
//            case "A6": //设置工作命令状态
//                if (order.equals("55")) {
//                    isSuccess = true;
//                }
//                break;
//            case "A7": //查询谷仓状态
//                String riceAnother = stringList.substring(8, 10);
//                if (order.equals("01") && riceAnother.equals("01")) {
//                    Intent intent = new Intent(context, MarginLackActivity.class);
//                    context.startActivity(intent);
//                    isSuccess = true;
//                }
//                break;
//            case "A8": //查询设备故障命令
//                if (!order.equals("00")) {
//                    switch (stringList.substring(6, 8)) {
//                        case "01":
//                            malfunctionList.add("驱动器故障-通讯故障");
//                            break;
//                        case "02":
//                            malfunctionList.add("驱动器故障-低压报警");
//                            break;
//                        case "03":
//                            malfunctionList.add("驱动器故障-过流报警");
//                            break;
//                        case "04":
//                            malfunctionList.add("驱动器故障-温度报警");
//                            break;
//                        default:
//                            break;
//                    }
//                }
//                if (stringList.substring(8, 10).equals("01")) {
//                    malfunctionList.add("碾米电机故障-过流报警");
//                }
//                if (stringList.substring(10, 12).equals("01")) {
//                    malfunctionList.add("提升电机故障-过流报警");
//                }
//                if (!stringList.substring(12, 14).equals("00")) {
//                    switch (stringList.substring(12, 14)) {
//                        case "01":
//                            malfunctionList.add("空调故障-通讯故障");
//                            break;
//                        case "02":
//                            malfunctionList.add("空调故障-温控传感器故障");
//                            break;
//                        case "03":
//                            malfunctionList.add("空调故障-除霜传感器故障");
//                            break;
//                        default:
//                            break;
//                    }
//                }
//                isSuccess = true;
//                break;
//            default:
//                isSuccess = true;
//                break;
//        }
//        String ss = "";
//        for (int i = 0; i < malfunctionList.size(); i++) {
//            ss = ss + malfunctionList.get(i);
//        }
//        Utils.log(ss);
//        return isSuccess;
//    }
}
