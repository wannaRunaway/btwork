package com.btkj.chongdianbao.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/27
 */
public class ScanStationModel implements Serializable {

    /**
     * code : 200
     * content : {"adminUserId":0,"afterBalance":null,"appointmentRecordId":232,"batteryCount":null,"batteryType":null,"bindLogId":2516,"carMile":null,"chargeAccountId":92,"commentStatus":0,"coupon":null,"createTime":1567488506983,"del":false,"fare":0.6,"id":69,"plateNo":"","questionStatus":0,"real":0.6,"remark":"换电","serialNo":"","soc":null,"stationId":20,"stationName":"","status":1,"travelMile":null}
     * msg : 成功
     * success : true
     */

    private int code;
    private ContentBean content;
    private String msg;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public class ContentBean implements Serializable {
        /**
         * adminUserId : 0
         * afterBalance : null
         * appointmentRecordId : 232
         * batteryCount : null
         * batteryType : null
         * bindLogId : 2516
         * carMile : null
         * chargeAccountId : 92
         * commentStatus : 0
         * coupon : null
         * createTime : 1567488506983
         * del : false
         * fare : 0.6
         * id : 69
         * plateNo :
         * questionStatus : 0
         * real : 0.6
         * remark : 换电
         * serialNo :
         * soc : null
         * stationId : 20
         * stationName :
         * status : 1
         * travelMile : null
         */

        private int adminUserId;
        private Object afterBalance;
        private int appointmentRecordId;
        private Object batteryCount;
        private Object batteryType;
        private int bindLogId;
        private Object carMile;
        private int chargeAccountId;
        private int commentStatus;
        private Object coupon;
        private long createTime;
        private boolean del;
        private double fare;
        private int id;
        private String plateNo;
        private int questionStatus;
        private double real;
        private String remark;
        private String serialNo;
        private Object soc;
        private int stationId;
        private String stationName;
        private int status;
        private Object travelMile;

        public int getAdminUserId() {
            return adminUserId;
        }

        public void setAdminUserId(int adminUserId) {
            this.adminUserId = adminUserId;
        }

        public Object getAfterBalance() {
            return afterBalance;
        }

        public void setAfterBalance(Object afterBalance) {
            this.afterBalance = afterBalance;
        }

        public int getAppointmentRecordId() {
            return appointmentRecordId;
        }

        public void setAppointmentRecordId(int appointmentRecordId) {
            this.appointmentRecordId = appointmentRecordId;
        }

        public Object getBatteryCount() {
            return batteryCount;
        }

        public void setBatteryCount(Object batteryCount) {
            this.batteryCount = batteryCount;
        }

        public Object getBatteryType() {
            return batteryType;
        }

        public void setBatteryType(Object batteryType) {
            this.batteryType = batteryType;
        }

        public int getBindLogId() {
            return bindLogId;
        }

        public void setBindLogId(int bindLogId) {
            this.bindLogId = bindLogId;
        }

        public Object getCarMile() {
            return carMile;
        }

        public void setCarMile(Object carMile) {
            this.carMile = carMile;
        }

        public int getChargeAccountId() {
            return chargeAccountId;
        }

        public void setChargeAccountId(int chargeAccountId) {
            this.chargeAccountId = chargeAccountId;
        }

        public int getCommentStatus() {
            return commentStatus;
        }

        public void setCommentStatus(int commentStatus) {
            this.commentStatus = commentStatus;
        }

        public Object getCoupon() {
            return coupon;
        }

        public void setCoupon(Object coupon) {
            this.coupon = coupon;
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

        public double getFare() {
            return fare;
        }

        public void setFare(double fare) {
            this.fare = fare;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public int getQuestionStatus() {
            return questionStatus;
        }

        public void setQuestionStatus(int questionStatus) {
            this.questionStatus = questionStatus;
        }

        public double getReal() {
            return real;
        }

        public void setReal(double real) {
            this.real = real;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public Object getSoc() {
            return soc;
        }

        public void setSoc(Object soc) {
            this.soc = soc;
        }

        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getTravelMile() {
            return travelMile;
        }

        public void setTravelMile(Object travelMile) {
            this.travelMile = travelMile;
        }
    }
}
