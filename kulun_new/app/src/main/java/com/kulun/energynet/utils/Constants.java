package com.kulun.energynet.utils;

/**
 * Created by Orion on 2017/7/11.
 */
public class Constants {
    public static Integer driverId = -1;
//    public final static String serverType = "/pay/app";
//    public static String BASE_URL= "http://47.98.183.212:9011";
    /**
     * public final static String DEBUG_URL = "http://47.98.183.212:9090/powerExchange/";
     *     public final static String RELEASE_URL = "https://ttgg.botann.com/powerExchange/";
     */
    //"http://47.98.183.212:9011/pay";
//    public static String BASE_URL = "http://p.botann.com:8191";
    //申请车队正式服务器二维码地址
//    public static String qrURL = "http://www.botann.com:8085/carteam/driver/toRegister";
    //申请车队测试服务器二维码地址
    public static String qrURL = "http://www.botann.com:8088/carteam/driver/toRegister";
    //账户余额充值方式，1-支付宝，0-微信
    public static Integer rechargeType = 1;
    //微信appid
    public static String wx_appid = "wx089f395a367fccce";
    //MainActivity加载时，哪个Fragment显示。0-我的账户，1-换电站，2-充值记录，3-消费记录
    public static Integer whichFragment = 0;
    public static String city = "";
    public static double currentLongitude = 120.150;       //定不到位默认给杭州的经度
    public static double currentLatitude = 30.280;          //定不到位默认给杭州的维度

//    public static String cityname = "";\



    public static volatile Integer couponTotal = 0;
    public static volatile String couponRes = "";
    public static volatile Integer messageTotal = 0;
    public static volatile String messageRes = "";
//    public static double mylongitude;
//    public static double mylatitude;
}
