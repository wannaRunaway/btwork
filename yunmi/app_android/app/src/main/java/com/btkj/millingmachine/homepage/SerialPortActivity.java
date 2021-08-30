package com.btkj.millingmachine.homepage;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.TextView;

import com.btkj.millingmachine.R;
import com.btkj.millingmachine.databinding.ActivitySerialPortBinding;
import com.btkj.millingmachine.util.API;
import com.btkj.millingmachine.util.Utils;
import com.btkj.millingmachine.viewutils.SharePref;

import Crt603rxDrv.Crt603rx;

/**
 * created by xuedi on 2019/5/14
 */
public class SerialPortActivity extends BaseActivity {
    private ActivitySerialPortBinding binding;
    Crt603rx mCrt603rx = new Crt603rx();
    //    private SerialPort serialPort = new SerialPort();
    private byte[] send = new byte[8];
    //    SerialPortUtil serialPortUtil;
    //初始化
//    SerialPortManager serialPortManager = new SerialPortManager();

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_serial_port);
//        serialPort.initPort(serialPortManager);
//        binding.nianmi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                send[0] = 0x7e;
//                send[1] = (byte) 0xa0;
//                send[2] = 0x01;
//                send[3] = 0x55;
//                send[4] = (byte) getyihuo();
//                serialPortManager.sendBytes(send);
//            }
//        });
//        serialPortUtil = new SerialPortUtil();
//        SerialPortFinder finder = new SerialPortFinder();
//        serialPortUtil.openSerialPort(finder.getAllDevices());
//        SerialPort serialPort = new SerialPort();
//        serialPort.initPort(this, serialPortManager);
        // 天线状态。 0 关闭， 1 开启
        int bWire = 0;
        //自动寻卡。 0 关闭， 1 开启
        int bAutoFindCard = 1;
        mCrt603rx.SetWorkMode(bWire, bAutoFindCard);
        mCrt603rx.OpenDevice(((String) SharePref.get(this, "duka", "")).toCharArray(), 19200);
        Utils.log((String) SharePref.get(this, "duka", ""));
        binding.duka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyApplication.getInstance().sendWuliCmd(API.nianmistart);
//                send(binding.duka);
            }
        });
        binding.chengzhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //称重传感器代码
              MyApplication.getInstance().sendJiliangCmd(API.chengzhong, null);
            }
        });
        binding.qupi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyApplication.getInstance().sendJiliangCmd(API.qupi, null);
            }
        });
//        binding.qupi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //01 06 00 11 00 01 18 0F
//                send[0] = 0x01;
//                send[1] = 0x06;
//                send[2] = 0x00;
//                send[3] = 0x11;
//                send[4] = 0x00;
//                send[5] = 0x01;
//                send[6] = 0x18;
//                send[7] = 0x0f;
//                serialPortManager.sendBytes(send);
//            }
//        });
    }

    @Override
    public void initHeaderBottom() {

    }

    private void startcult() {
//        SerialManager.getInstance().sendBytes(NetProtocal.protocalsend(1, (byte) 0x10));
//        SerialManager.getInstance().sendBytes(NetProtocal.nianmisend());
    }

    private void stopcult(){
//        SerialManager.getInstance().sendBytes(NetProtocal.protocalsend(2, (byte) 0x55));
//        SerialManager.getInstance().sendBytes(NetProtocal.nianmistop());
    }

    //设置主板为工作状态
    private void serialMotherBoard() {
//        SerialManager.getInstance().sendBytes(NetProtocal.protocalsend(6, (byte) 0xaa));
    }

    private void send(TextView tv) {
        int iRet = 0;
        // iMode  寻卡模式 0 寻所有卡， 1 只寻未休眠卡
        int iMode = 0;
        long strlog;
        int[] iOutLen = new int[2];
        byte[] byOutUid = new byte[128];
        iRet = mCrt603rx.FindCard(iMode, iOutLen, byOutUid);
        //76852205
        String ss = "";
        for (int i = 0; i < iOutLen[0]; i++) {
            ss += String.format("%02x", byOutUid[i]);
        }
        String s01 = ss.substring(6,8);
        String s02 = ss.substring(4,6);
        String s03 = ss.substring(2,4);
        String s04 = ss.substring(0,2);
        String carnum = s01+s02+s03+s04;
        strlog = Long.parseLong(ss, 16);
        Utils.log( ss+"=="+strlog+"=="+carnum+"=="+Long.parseLong(carnum, 16));
        tv.setText("===分割线===" + carnum);
        /**
         * 参数：发送数据 byte[]
         * 返回：发送是否成功
         */
//        boolean sendBytes = serialPortManager.sendBytes(toString.getBytes());
//        Utils.log("发送数据" + sendBytes);
//        serialPortUtil.sendSerialPort(toString);
    }
}
