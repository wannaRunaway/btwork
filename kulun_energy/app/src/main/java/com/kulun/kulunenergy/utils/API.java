package com.kulun.kulunenergy.utils;

/**
 * created by xuedi on 2019/8/1
 */
public class API {
//    public final static String DEBUG_URL = "http://47.98.183.212:8088/powerExchange/";
    public final static String DEBUG_URL = "http://47.98.183.212:9011/pay/app";
//    public final static String DEBUG_URL = "http://app.dongruime.cn:9090/powerExchange/";
    public final static String RELEASE_URL = "https://ttgg.botann.com/powerExchange";
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
    //库仑接口
    public static String URL_LOGIN = "/login";                          // 登录
    public static String URL_INFO = "/information";                     // 获取个人主页信息
    public static String URL_EXCHANGE_LIST = "/exchange/list3";          // 消费记录
    public static String URL_EXCHANGE_COMMON = "/exchange/statistics";   //消费统计信息
    public static String URL_RECHARGE_LIST = "/recharge/list2";          // 充值记录
    public static String URL_CITYID = "/city/getCityByName";            // 通过城市名称获取城市ID
    public static String URL_STATION_LIST  = "/station/list";            // 换电站列表
    public static String URL_COUPON_LIST = "/coupon/list";              // 优惠券
    public static String URL_SYSTEM_MESSAGE = "/message/sysMsgList";    // 系统消息
    public static String URL_POST_MESSAGE = "/message/list";            // 推送消息
    public static String URL_TEAM_INFO = "/team/teamInformation";       // 我的团队
    public static String URL_MEMBER_INFO = "/team/teamMember";          // 成员信息
    public static String URL_MYREWARD = "/team/myReward";          // 成员信息
    public static String URL_VERSION_INFO = "/apkInfo/getApkInfo";      // 获取服务器版本信息
    public static String URL_PAY = "/pay";      // 支付
    public static String URL_COMMENT_SAVE = "/comment/save";        //提交评论
    public static String URL_COMMENT_DETAIL = "/comment/detail";    //查看评论
    public static String URL_QUESTION_SAVE = "/question/save";      //提交问题
    public static String URL_QUESTION_DETAIL = "/question/detail";  //查看问题
    public static String URL_PHOTO_UPLOAD = "/picture/photoUpload";//上传照片
    public static String URL_BAIDU_COORD_TRANSFORM = "http://api.map.baidu.com/geoconv/v1/";   //高德经纬度转换成百度
    public static String URL_QUEUENUM = "/exchange/lineCount";       //当前排队人数
    public static String URL_ALITOKEN = "/getAliOssUploadToken";      //获取阿里云token的url
    public static String URL_CUSTOMERSERVICELIST = "/customerServiceInfo/list";                 //客服列表的list
    public static String URL_PROMOTIONS = "/activity/list";   //优惠活动列表
    public static String URL_ACTIVITY_CONTENT = "/activity/getDetail";  //请求活动内容
    public static String URL_CAR_INFOR = "/activity/getPackageActivityCar";  //app获取套餐活动车辆信息
    public static String URL_ACTIVITY_LIST = "/activity/getActivityRecordList";  //app获取套餐活动列表
    public static String URL_COUPON_ACTIVITY = "/activity/getActivityCouponList"; //app获取活动记录发放的优惠券列表
    public static String URL_PHONEINFO_UP = "/recordVersion";
    public static String URL_REFUND_CHECK = "/refund/totalRefundableAmount";      //查询当前可退款余额
    public static String URL_REFUND_QUERY = "/refund/apply";                      //申请退款
    public static String URL_REFUND_LIST = "/refund/list";                        //退款列表
//    public static String URL_RETRIEVE_PASSWORD = "/updateAccountPassword";    //找回密码
    public static String URL_GET_SMS = "/sendCode";       //app获取验证码
    public static String URL_LOGIN_VERIFICATION = "/verifyCode";          //app通过验证码登录
    public static String URL_NEW_USER_REGISTER = "/updatePwd";                     //新用户注册接口
    public static String GET_CAR_TYPE = "/getDriverCarInfoCarType";                        //获取所有车型
    public static String UPLOAD_CARPLATE = "/charge/addBind";                //绑定车辆
    public static String CHONGDIAN_LIST = "/charge/stationList";             //充电站列表接口
    public static String CHONGDIAN_INFOR = "/charge/connectorAuth";          //充电桩信息
    public static String CHONGDIAN_KAISHI = "/charge/startCharging";         //启动充电
    public static String CHONGDIAN_JIESHU = "/charge/endCharging";           //结束充电
    public static String CHONGDIAN_DIANDAN = "/charge/queryChargeStatus";    //充电订单查询
    public static String PERSONALINFO_UPLOAD = "/authAccount";               //修改账户信息
    public static String COUPON_AVA = "/coupon/couponTemplateList";          //可领取优惠券列表
    public static String COUPON_RECEIVE = "/coupon/receiveCoupon";           //领取优惠券
    public static String PUNCH_LIST = "/clockList";                           //打卡列表
    public static String PUNCH = "/clock";                                    //打卡上车或者下车
    public static String VINGETMILE = "/getCarMile";                         //获取里程
    public static String GETALLCITY = "/getDriverCarInfoCity";                         //查询全部站点城市
    public static String ADDCARINFO = "/addDriverCarInfo";                   //司机车辆增加
    public static String DRIVERCARINFO = "/getDriverCarInfo";                //获取车辆信息
    public static String MYPLATELIST= "/getDriverCarInfoList";               //我的绑定


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
    //新的提示
    public final static String name = "name";
    public final static String account = "account";
    public final static String accountId = "accountId";
    public final static String bucketName = "bucketName";
    public final static String bindcarlist = "bindcarlist";
    public final static String companyBalance = "companyBalance";
    public final static String balance = "balance";
    public final static String token = "token";
}
