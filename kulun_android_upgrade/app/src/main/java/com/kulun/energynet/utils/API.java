package com.kulun.energynet.utils;

/**
 * created by xuedi on 2019/8/1
 */
public class API {
    //new api
//    public final static String baseurl = "http://47.98.183.212:9006/api/";
//    public static String baseurl = "https://chargingapp.klenergy.com.cn/api/";
    public static String baseurl = "https://test.klenergy.com.cn/api/";
    public final static String getsms = "send_sms";
    public final static String REGISTER = "user/register";
    public final static String LOGIN = "user/login";
    public final static String SITELIST = "site/list";
    ///api/site/all?exclude=320200   全国站点
    public final static String siteall = "site/all";
    public final static String recomand = "site/near";
    public final static String CITYLIST = "area/open";
    public final static String LOGINOUT = "user/logout";
    public final static String INFO = "user/profile";
    public final static String changePassword = "password/edit";
    public final static String passwordReset = "password/reset";
    public final static String gettbox = "car/tbox";
    public final static String bind = "car/bind";
    public final static String osstoken = "oss/token";
    public final static String getmycarlist = "car/list";
    public final static String carnow = "car/inuse";
    public final static String carunbind = "car/unbind";
    public final static String carapplylist = "car/apply";
    public final static String getmyappponit = "book/get";
    public final static String appoint = "book/add";
    public final static String cancelappoint = "book/cancel";
    public final static String delayappoint = "book/delay";
    public final static String appload = "load/list";
    public final static String appointlist = "book/list";
    public final static String messagelist = "message/list";
    public final static String activitylist = "activity/list";
    public final static String activitydetail = "activity/package/detail";
    public final static String pay = "pay/order";
    public final static String clockList = "driver/clock/list";
    public final static String clock = "driver/clock";
    public final static String couponlist = "coupon/list";
    public final static String billlist = "bill/list";
    public final static String billdetail = "bill/detail";
    public final static String buypackage = "activity/package/buy";
    public final static String scanpay = "exchange/consume";
    public final static String questionCommit = "bill/question";
    public final static String questionCheck = "bill/question/detail";
    public final static String commentCommit = "bill/comment";
    public final static String commentCheck = "bill/comment/detail";
    public final static String refundamount = "refund/amount";
    public final static String refundapply = "refund/apply";

    //通用提示语
    public final static String net_error = "网络异常，请稍候";
    public final static String time = "time";
    public final static String code = "code";
    public final static String station = "station";
    public final static String ismain = "ismain";
    public final static String apkurl = "apkurl";
    //新的提示
    public final static String name = "name";
    public final static String account = "account";
    public final static String bucketName = "bucketName";
    public final static String balance = "balance";
    public final static String token = "token";
    public final static String phone = "phone";
    public final static String password = "password";
    public final static String username = "username";
    public final static String longtitude = "longtitude";
    public final static String mylongtitude = "mylongtitude";
    public final static String mylatitude = "mylatitude";
    public final static String latitude = "latitude";
    public final static String cityName = "cityname";
    public final static String cityId = "cityId";
    public final static String usebind = "usebind";
    public final static String aliyunEndpoint = "aliyunEndpoint";
    public final static String aliyunStsServer = "aliyunStsServer";
    public final static String ak = "ak";
    public final static String sk = "sk";
    public final static String myosstoken = "myosstoken";
    public final static String expiration = "expiration";
    public final static String customphone = "customphone";
    public final static String custominfo = "custominfo";
    public final static String appointment_no = "appointment_no";
    public final static String siteid = "siteid";
    //"questioned":false,"commented":false,
    public final static String questioned = "questioned";
    public final static String commented = "commented";
    public final static String register = "register";
}
