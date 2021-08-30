package com.btkj.chongdianbao.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/16
 */
public class Station implements Serializable {
    /**
     * accountMain : null
     * address : 杭州市冰江
     * appointmentBatteryCountMap : {"kwh10":0,"kwh12":0,"kwh15":0}
     * availBatteryCountMap : {"kwh10":0,"kwh12":0,"kwh15":0}
     * channelCount : 12
     * cityId : 330100
     * companyId : 17
     * contact : zwj
     * createTime : 1563187277000
     * del : false
     * distance : 0
     * exStationId : 0
     * exchangeRuleDetail : null
     * fullElectBatteryCountMap : {"kwh10":0,"kwh12":0,"kwh15":0}
     * id : 1
     * latitude : 30.261591
     * locLatitude : null
     * locLongitude : null
     * longitude : 120.089918
     * name : 下沙站
     * number : 12wdw
     * place : 浙江省 杭州市 西湖区
     * shareRule : null
     * status : 0
     * statusApp : 1
     * telephone : 13018757865
     * totalBatteryCount : 0
     * type : 0
     * updateTime : 1563187277000
     * workTime : 09:00-24:00
     * zoneId : 330106
     */

    private Object accountMain;
    private String address;
    private AppointmentBatteryCountMapBean appointmentBatteryCountMap;
    private AvailBatteryCountMapBean availBatteryCountMap;
    private int channelCount;
    private int cityId;
    private int companyId;
    private String contact;
    private long createTime;
    private boolean del;
    private int distance;
    private int exStationId;
    private Object exchangeRuleDetail;
    private FullElectBatteryCountMapBean fullElectBatteryCountMap;
    private int id;
    private double latitude;
    private Object locLatitude;
    private Object locLongitude;
    private double longitude;
    private String name;
    private String number;
    private String place;
    private Object shareRule;
    private int status;
    private int statusApp;
    private String telephone;
    private int totalBatteryCount;
    private int type;
    private long updateTime;
    private String workTime;
    private int zoneId;
    private int position, positionItem;
    private boolean isclick;

    public boolean isIsclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }

    public int getPositionItem() {
        return positionItem;
    }

    public void setPositionItem(int positionItem) {
        this.positionItem = positionItem;
    }

    public Object getAccountMain() {
        return accountMain;
    }

    public void setAccountMain(Object accountMain) {
        this.accountMain = accountMain;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AppointmentBatteryCountMapBean getAppointmentBatteryCountMap() {
        return appointmentBatteryCountMap;
    }

    public void setAppointmentBatteryCountMap(AppointmentBatteryCountMapBean appointmentBatteryCountMap) {
        this.appointmentBatteryCountMap = appointmentBatteryCountMap;
    }

    public AvailBatteryCountMapBean getAvailBatteryCountMap() {
        return availBatteryCountMap;
    }

    public void setAvailBatteryCountMap(AvailBatteryCountMapBean availBatteryCountMap) {
        this.availBatteryCountMap = availBatteryCountMap;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getExStationId() {
        return exStationId;
    }

    public void setExStationId(int exStationId) {
        this.exStationId = exStationId;
    }

    public Object getExchangeRuleDetail() {
        return exchangeRuleDetail;
    }

    public void setExchangeRuleDetail(Object exchangeRuleDetail) {
        this.exchangeRuleDetail = exchangeRuleDetail;
    }

    public FullElectBatteryCountMapBean getFullElectBatteryCountMap() {
        return fullElectBatteryCountMap;
    }

    public void setFullElectBatteryCountMap(FullElectBatteryCountMapBean fullElectBatteryCountMap) {
        this.fullElectBatteryCountMap = fullElectBatteryCountMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Object getLocLatitude() {
        return locLatitude;
    }

    public void setLocLatitude(Object locLatitude) {
        this.locLatitude = locLatitude;
    }

    public Object getLocLongitude() {
        return locLongitude;
    }

    public void setLocLongitude(Object locLongitude) {
        this.locLongitude = locLongitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Object getShareRule() {
        return shareRule;
    }

    public void setShareRule(Object shareRule) {
        this.shareRule = shareRule;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatusApp() {
        return statusApp;
    }

    public void setStatusApp(int statusApp) {
        this.statusApp = statusApp;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getTotalBatteryCount() {
        return totalBatteryCount;
    }

    public void setTotalBatteryCount(int totalBatteryCount) {
        this.totalBatteryCount = totalBatteryCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public class AppointmentBatteryCountMapBean implements Serializable{
        /**
         * kwh10 : 0
         * kwh12 : 0
         * kwh15 : 0
         */

        private int kwh10;
        private int kwh12;
        private int kwh15;

        public int getKwh10() {
            return kwh10;
        }

        public void setKwh10(int kwh10) {
            this.kwh10 = kwh10;
        }

        public int getKwh12() {
            return kwh12;
        }

        public void setKwh12(int kwh12) {
            this.kwh12 = kwh12;
        }

        public int getKwh15() {
            return kwh15;
        }

        public void setKwh15(int kwh15) {
            this.kwh15 = kwh15;
        }
    }

    public class AvailBatteryCountMapBean implements Serializable{
        /**
         * kwh10 : 0
         * kwh12 : 0
         * kwh15 : 0
         */

        private int kwh10;
        private int kwh12;
        private int kwh15;

        public int getKwh10() {
            return kwh10;
        }

        public void setKwh10(int kwh10) {
            this.kwh10 = kwh10;
        }

        public int getKwh12() {
            return kwh12;
        }

        public void setKwh12(int kwh12) {
            this.kwh12 = kwh12;
        }

        public int getKwh15() {
            return kwh15;
        }

        public void setKwh15(int kwh15) {
            this.kwh15 = kwh15;
        }
    }

    public class FullElectBatteryCountMapBean implements Serializable{
        /**
         * kwh10 : 0
         * kwh12 : 0
         * kwh15 : 0
         */

        private int kwh10;
        private int kwh12;
        private int kwh15;

        public int getKwh10() {
            return kwh10;
        }

        public void setKwh10(int kwh10) {
            this.kwh10 = kwh10;
        }

        public int getKwh12() {
            return kwh12;
        }

        public void setKwh12(int kwh12) {
            this.kwh12 = kwh12;
        }

        public int getKwh15() {
            return kwh15;
        }

        public void setKwh15(int kwh15) {
            this.kwh15 = kwh15;
        }
    }
}
