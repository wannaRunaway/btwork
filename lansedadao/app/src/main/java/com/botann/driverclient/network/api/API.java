package com.botann.driverclient.network.api;

import com.botann.driverclient.utils.Constants;

/**
 * Created by Orion on 2017/7/11.
 */
public class API {

    /****************      Server      ****************/
    //服务器
//    public static String BASE_URL = Constants.appBaseURL + Constants.serverType;
    //测试服务器
    public static String BASE_URL = Constants.BASE_URL + Constants.serverType;
    /****************      API      ****************/
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

    //换电出行直接拿过来的东西
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
    //加入提示消息
    public static String error_internet = "网络连接失败，请重试";
    public static String isChongdian = "ischongdian";
    //存储的shareprecence
    public static String sp_chongdian = "bindcar_chongdian";
    public static String bindId_chongdian = "bindid_chongdian";
    public static String start_time_chongdian = "start_time_chongdian";
    public static final String coupon = "coupon";
    public static final String chongdian = "chongdian";
    public static final String lat = "lat";
    public static final String lon = "lon";
    public static final String city = "city";
    public static final String cityId = "cityid";
    public static final String registertype = "registertype";
    public static final String islogin = "islogin";
    public static final String isfirst = "isFirst";
    public static final String accountType = "account_type";
    public static final String exchangeAccountAppRefund  = "exchangeAccountAppRefund";
    public static final String type = "mytype";
    public static final String isshangban = "isshangban";
}
