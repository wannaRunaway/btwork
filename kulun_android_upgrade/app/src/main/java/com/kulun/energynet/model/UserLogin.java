package com.kulun.energynet.model;

import android.app.Activity;

import com.kulun.energynet.utils.API;
import com.kulun.energynet.utils.SharePref;

import java.io.Serializable;

public class UserLogin implements Serializable {
    //{
    //{"code":0,"data":{"account":57124,"name":"","phone":"15209603592","balance":0,"unread":0,
    // "token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDkyOTM4MTIsImlhdCI6MTYwODY4OTAxMiwidXNlcmlkIjo1NzEyNCwidXNlcm5hbWUiOiIifQ.uObhx1WnL5tcVvJAlIA6B8ls0ijz32JbwVmA4rEhjP0",
    // "use_bind":{"id":0,"car_id":0,"plate_number":"","bind_status":0,"left_mile":0,"month_mile":0,"inuse":false,"battery_count":0,"battery_status":false,"soc":0}}}
    //"ali_oss":{"OssEndPoint":"oss-cn-shanghai.aliyuncs.com","StsEndPoint":"sts.cn-shanghai.aliyuncs.com","BucketName":"app-back-98039b9ced38"}}}
    private static UserLogin userLogin;

    public static UserLogin getInstance() {
        if (null == userLogin) {
            synchronized (UserLogin.class) {
                if (null == userLogin) {
                    userLogin = new UserLogin();
                }
            }
        }
        return userLogin;
    }

    public String phone, token = "", name, wxappid, account;
    public double balance;
    public boolean islogin = false;
    public double latitude, longtitude;
    private double mylatitude, mylongtitude;
    public String cityName;
    public StationInfo stationInfo;
    public int id, usebind, unread, cityid;
    public UseBind use_bind;
    public Alioss ali_oss;
    public int yuyueId = -1;
    private String custphone, custinfo, appointment_no, identity;
    private boolean canRefund;

    public boolean isCanRefund() {
        return canRefund;
    }

    public void setCanRefund(boolean canRefund) {
        this.canRefund = canRefund;
    }

    public int getCityid(Activity activity) {
        if (cityid == 0){
            String s = (String) SharePref.get(activity,API.cityId,"0");
            return Integer.parseInt(s);
        }
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public double getMylatitude(Activity activity) {
        if (mylatitude==0){
            String s = (String) SharePref.get(activity,API.mylatitude,"0");
            return Double.parseDouble(s);
        }
        return mylatitude;
    }

    public void setMylatitude(double mylatitude) {
        this.mylatitude = mylatitude;
    }

    public double getMylongtitude(Activity activity) {
        if (mylongtitude==0){
            String s = (String) SharePref.get(activity,API.mylongtitude,"0");
            return Double.parseDouble(s);
        }
        return mylongtitude;
    }

    public void setMylongtitude(double mylongtitude) {
        this.mylongtitude = mylongtitude;
    }

    public String getAppointment_no(Activity activity) {
        if (appointment_no == null || appointment_no.equals("")) {
            return (String) SharePref.get(activity, API.appointment_no, "");
        }
        return appointment_no;
    }

    public void setAppointment_no(String appointment_no) {
        this.appointment_no = appointment_no;
    }

    public String getCustphone(Activity activity) {
        if (custphone == null || custphone.equals("")) {
            return (String) SharePref.get(activity, API.customphone, "");
        }
        return custphone;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setCustphone(String custphone) {
        this.custphone = custphone;
    }

    public String getCustinfo(Activity activity) {
        if (custinfo == null || custinfo.equals("")) {
            return (String) SharePref.get(activity, API.custominfo, "");
        }
        return custinfo;
    }

    public void setCustinfo(String custinfo) {
        this.custinfo = custinfo;
    }

    public int getYuyueId() {
        return yuyueId;
    }

    public void setYuyueId(int yuyueId) {
        this.yuyueId = yuyueId;
    }

    public Alioss getAli_oss() {
        return ali_oss;
    }

    public void setAli_oss(Alioss ali_oss) {
        this.ali_oss = ali_oss;
    }

    public UseBind getUse_bind() {
        return use_bind;
    }

    public void setUse_bind(UseBind use_bind) {
        this.use_bind = use_bind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude(Activity activity) {
        if (latitude == 0) {
            String string = (String) SharePref.get(activity, API.latitude, "0");
            return Double.parseDouble(string);
        }
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude(Activity activity) {
        if (longtitude == 0) {
            String string = (String) SharePref.get(activity, API.longtitude, "0");
            return Double.parseDouble(string);
        }
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getCityName(Activity activity) {
        if (cityName == null){
            cityName = (String) SharePref.get(activity,API.cityName,"");
            return cityName;
        }
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isIslogin() {
        return islogin;
    }

    public void setIslogin(boolean islogin) {
        this.islogin = islogin;
    }

    public StationInfo getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(StationInfo stationInfo) {
        this.stationInfo = stationInfo;
    }

    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getUsebind() {
        return usebind;
    }

    public void setUsebind(int usebind) {
        this.usebind = usebind;
    }

    // "use_bind":{"id":0,"car_id":0,"plate_number":"","bind_status":0,"left_mile":0,"month_mile":0,"inuse":false,"battery_count":0,"battery_status":false,"soc":0}}}
    public class UseBind implements Serializable {
        private int id, car_id, bind_status, left_mile, month_mile, soc, battery_count;
        private String plate_number;
        private boolean inuse, battery_status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCar_id() {
            return car_id;
        }

        public void setCar_id(int car_id) {
            this.car_id = car_id;
        }

        public int getBind_status() {
            return bind_status;
        }

        public void setBind_status(int bind_status) {
            this.bind_status = bind_status;
        }

        public int getLeft_mile() {
            return left_mile;
        }

        public void setLeft_mile(int left_mile) {
            this.left_mile = left_mile;
        }

        public int getMonth_mile() {
            return month_mile;
        }

        public void setMonth_mile(int month_mile) {
            this.month_mile = month_mile;
        }

        public int getSoc() {
            return soc;
        }

        public void setSoc(int soc) {
            this.soc = soc;
        }

        public int getBattery_count() {
            return battery_count;
        }

        public void setBattery_count(int battery_count) {
            this.battery_count = battery_count;
        }

        public String getPlate_number() {
            return plate_number;
        }

        public void setPlate_number(String plate_number) {
            this.plate_number = plate_number;
        }

        public boolean isInuse() {
            return inuse;
        }

        public void setInuse(boolean inuse) {
            this.inuse = inuse;
        }

        public boolean isBattery_status() {
            return battery_status;
        }

        public void setBattery_status(boolean battery_status) {
            this.battery_status = battery_status;
        }
    }

    public class Alioss implements Serializable {
        //{"OssEndPoint":"oss-cn-shanghai.aliyuncs.com","StsEndPoint":"sts.cn-shanghai.aliyuncs.com","BucketName":"app-back-98039b9ced38"}}
        private String OssEndPoint, StsEndPoint, BucketName;

        public String getOssEndPoint() {
            return OssEndPoint;
        }

        public void setOssEndPoint(String ossEndPoint) {
            OssEndPoint = ossEndPoint;
        }

        public String getStsEndPoint() {
            return StsEndPoint;
        }

        public void setStsEndPoint(String stsEndPoint) {
            StsEndPoint = stsEndPoint;
        }

        public String getBucketName() {
            return BucketName;
        }

        public void setBucketName(String bucketName) {
            BucketName = bucketName;
        }
    }
}
