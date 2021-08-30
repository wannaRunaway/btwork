package com.btkj.chongdianbao.utils;

/**
 * created by xuedi on 2019/8/1
 */
public class API {
//    public final static String DEBUG_URL = "http://47.98.183.212:8088/powerExchange/";
    public final static String DEBUG_URL = "https://ttgg.botann.com/powerExchange/";
//    public final static String DEBUG_URL = "http://app.dongruime.cn:9090/powerExchange/";
    public final static String RELEASE_URL = "https://ttgg.botann.com/powerExchange/";
    // public final static String LOCAL_URL = "http://192.168.20.83/";
    public final static String BASE_URL = Constants.isDebug?DEBUG_URL:RELEASE_URL;
    public final static String LOGIN_PASSWORD = "login";
    public final static String LOGIN_VERI_CODE = "verifyCode";
    public final static String SMSCODE = "sendCode";
    public final static String REGISTER = "register";
    public final static String ADDCAR = "addBindCar";
    public final static String MYBINDCAR = "myBindCar";
    public final static String CITYLIST = "openUpCity";
    public final static String STATIONLIST = "stationList";
    public final static String ADDAPPOINT = "addAppointment";
    public final static String DELAYAPPOINT = "delayAppointment";
    public final static String CURRENTAPPONT = "currAppointment";
    public final static String CANCELAPPOINT = "cancelAppointment";
    public final static String STATIONRECOMMEND = "stationForAppointment";
    public final static String SCANCODE = "scanStationCode";
    public final static String RECHARGE = "recharge";
    public final static String YUYUEHISTORY = "myAppointmentRecords";

    public final static String UPDATEAUTH = "updateAuth";
    public final static String UPDATEPASSWORD = "updatePwd";
    public final static String LOGINOUT = "loginOut";
    public final static String SERVICELIST = "appLoadList";
    public final static String INVITE = "scanInviteDriverCode";
    public final static String INVITELIST = "myInviteDriver";
    public final static String INFO = "information";
    public final static String COUPON = "myCoupon";
    public final static String RECHARGELIST = "rechargeRecord";
    public final static String EXCHANGELIST = "exchangeRecord";
    public final static String ACTIVITYLIST = "activityList";
    public final static String DELETECAR = "unBindCar";
    public final static String MYMESSAGE = "myMessage";
    public final static String OSSTOKEN = "getAliOssUploadToken";
    public final static String APPLYREFUND = "exchangeRefund";
    public final static String CANCELREFUND = "cancelExchangeRefund";
    public final static String STATIONDETAIL = "stationBatteryList";
    public final static String CLOCK = "clock";
    public final static String clockList = "clockList";
    //通用提示语
    public final static String net_error = "网络异常，请稍候";
    public final static String qrinfo = "dongdong.power://driver?accountNo=";
    public final static String brocastStart = "brocaststart";
    public final static String brocastStop = "brocaststop";
    public final static String time = "time";
    public final static String yueyue_code = "请先预约";
    //activity跳转
    public final static String carType = "type";
    public final static String bindId = "bindId";
    public final static String carId = "carId";
    public final static String plateNo = "plate";
    public final static String batterycount = "batterycount";
    public final static String longtitude = "longtitude";
    public final static String latitude = "latitude";
    public final static String cityId = "cityid";
    public final static String cityName = "cityname";
    public final static String code = "code";
    public final static String islogin = "islogin";
    public final static String station = "station";
    public final static String isfromScan = "isfromscan";
    public final static String ismain = "ismain";
    //public final static String apppath = "apppath";
    public final static String apkurl = "apkurl";
}
