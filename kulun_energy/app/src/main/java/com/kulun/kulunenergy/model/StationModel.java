package com.kulun.kulunenergy.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/9/2
 */
public class StationModel implements Serializable {

    /**
     * code : 200
     * content : [{"adminUserId":0,"afterBalance":98.2,"appointmentRecordId":239,"batteryCount":4,"batteryType":15,"bindLogId":2516,"carMile":746,"chargeAccountId":92,"commentStatus":0,"coupon":0,"createTime":1567488733000,"del":false,"fare":0.6,"id":70,"plateNo":"浙ADC5693","questionStatus":0,"real":0.6,"remark":"换电","serialNo":"BYYJBJ1567488733447","soc":78,"stationId":20,"stationName":"伯坦站","status":1,"travelMile":0},{"adminUserId":0,"afterBalance":98.8,"appointmentRecordId":232,"batteryCount":4,"batteryType":15,"bindLogId":2516,"carMile":746,"chargeAccountId":92,"commentStatus":0,"coupon":0,"createTime":1567488507000,"del":false,"fare":0.6,"id":69,"plateNo":"浙ADC5693","questionStatus":0,"real":0.6,"remark":"换电","serialNo":"BKLQSJ1567488507004","soc":78,"stationId":20,"stationName":"伯坦站","status":1,"travelMile":0},{"adminUserId":0,"afterBalance":99.4,"appointmentRecordId":228,"batteryCount":4,"batteryType":15,"bindLogId":2516,"carMile":746,"chargeAccountId":92,"commentStatus":0,"coupon":0,"createTime":1567487558000,"del":false,"fare":0.6,"id":65,"plateNo":"浙ADC5693","questionStatus":0,"real":0.6,"remark":"换电","serialNo":"BPGQJQ1567487558285","soc":78,"stationId":20,"stationName":"伯坦站","status":1,"travelMile":8}]
     * msg : 成功
     * success : true
     */

//    private int code;
//    private String msg;
//    private boolean success;
//    private List<ContentBean> content;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public List<ContentBean> getContent() {
//        return content;
//    }
//
//    public void setContent(List<ContentBean> content) {
//        this.content = content;
//    }
//
//    public class ContentBean implements Serializable{
//        /**
//         * adminUserId : 0
//         * afterBalance : 98.2
//         * appointmentRecordId : 239
//         * batteryCount : 4
//         * batteryType : 15
//         * bindLogId : 2516
//         * carMile : 746
//         * chargeAccountId : 92
//         * commentStatus : 0
//         * coupon : 0
//         * createTime : 1567488733000
//         * del : false
//         * fare : 0.6
//         * id : 70
//         * plateNo : 浙ADC5693
//         * questionStatus : 0
//         * real : 0.6
//         * remark : 换电
//         * serialNo : BYYJBJ1567488733447
//         * soc : 78
//         * stationId : 20
//         * stationName : 伯坦站
//         * status : 1
//         * travelMile : 0
//         */
//
//        private int adminUserId;
//        private double afterBalance;
//        private int appointmentRecordId;
//        private int batteryCount;
//        private int batteryType;
//        private int bindLogId;
//        private int carMile;
//        private int chargeAccountId;
//        private int commentStatus;
//        private double coupon;
//        private long createTime;
//        private boolean del;
//        private double fare;
//        private int id;
//        private String plateNo;
//        private int questionStatus;
//        private double real;
//        private String remark;
//        private String serialNo;
//        private float soc;
//        private int stationId;
//        private String stationName;
//        private int status;
//        private int travelMile;
//
//        private RefundBean refund;
//
//        public RefundBean getRefundBean() {
//            return refund;
//        }
//
//        public void setRefundBean(RefundBean refund) {
//            this.refund = refund;
//        }
//
//        public int getAdminUserId() {
//            return adminUserId;
//        }
//
//        public void setAdminUserId(int adminUserId) {
//            this.adminUserId = adminUserId;
//        }
//
//        public double getAfterBalance() {
//            return afterBalance;
//        }
//
//        public void setAfterBalance(double afterBalance) {
//            this.afterBalance = afterBalance;
//        }
//
//        public int getAppointmentRecordId() {
//            return appointmentRecordId;
//        }
//
//        public void setAppointmentRecordId(int appointmentRecordId) {
//            this.appointmentRecordId = appointmentRecordId;
//        }
//
//        public int getBatteryCount() {
//            return batteryCount;
//        }
//
//        public void setBatteryCount(int batteryCount) {
//            this.batteryCount = batteryCount;
//        }
//
//        public int getBatteryType() {
//            return batteryType;
//        }
//
//        public void setBatteryType(int batteryType) {
//            this.batteryType = batteryType;
//        }
//
//        public int getBindLogId() {
//            return bindLogId;
//        }
//
//        public void setBindLogId(int bindLogId) {
//            this.bindLogId = bindLogId;
//        }
//
//        public int getCarMile() {
//            return carMile;
//        }
//
//        public void setCarMile(int carMile) {
//            this.carMile = carMile;
//        }
//
//        public int getChargeAccountId() {
//            return chargeAccountId;
//        }
//
//        public void setChargeAccountId(int chargeAccountId) {
//            this.chargeAccountId = chargeAccountId;
//        }
//
//        public int getCommentStatus() {
//            return commentStatus;
//        }
//
//        public void setCommentStatus(int commentStatus) {
//            this.commentStatus = commentStatus;
//        }
//
//        public double getCoupon() {
//            return coupon;
//        }
//
//        public void setCoupon(double coupon) {
//            this.coupon = coupon;
//        }
//
//        public long getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(long createTime) {
//            this.createTime = createTime;
//        }
//
//        public boolean isDel() {
//            return del;
//        }
//
//        public void setDel(boolean del) {
//            this.del = del;
//        }
//
//        public double getFare() {
//            return fare;
//        }
//
//        public void setFare(double fare) {
//            this.fare = fare;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getPlateNo() {
//            return plateNo;
//        }
//
//        public void setPlateNo(String plateNo) {
//            this.plateNo = plateNo;
//        }
//
//        public int getQuestionStatus() {
//            return questionStatus;
//        }
//
//        public void setQuestionStatus(int questionStatus) {
//            this.questionStatus = questionStatus;
//        }
//
//        public double getReal() {
//            return real;
//        }
//
//        public void setReal(double real) {
//            this.real = real;
//        }
//
//        public String getRemark() {
//            return remark;
//        }
//
//        public void setRemark(String remark) {
//            this.remark = remark;
//        }
//
//        public String getSerialNo() {
//            return serialNo;
//        }
//
//        public void setSerialNo(String serialNo) {
//            this.serialNo = serialNo;
//        }
//
//        public float getSoc() {
//            return soc;
//        }
//
//        public void setSoc(float soc) {
//            this.soc = soc;
//        }
//
//        public int getStationId() {
//            return stationId;
//        }
//
//        public void setStationId(int stationId) {
//            this.stationId = stationId;
//        }
//
//        public String getStationName() {
//            return stationName;
//        }
//
//        public void setStationName(String stationName) {
//            this.stationName = stationName;
//        }
//
//        public int getStatus() {
//            return status;
//        }
//
//        public void setStatus(int status) {
//            this.status = status;
//        }
//
//        public int getTravelMile() {
//            return travelMile;
//        }
//
//        public void setTravelMile(int travelMile) {
//            this.travelMile = travelMile;
//        }
//
//
//        public class RefundBean implements Serializable{
//            private int id;
//            private double value;
//            private int status;
//            private String reason;
//            private String result;
//            private List<String> img_key;
//
//            public String getResult() {
//                return result;
//            }
//
//            public void setResult(String result) {
//                this.result = result;
//            }
//
//            public String getReason() {
//                return reason;
//            }
//
//            public void setReason(String reason) {
//                this.reason = reason;
//            }
//
//            public List<String> getImg_key() {
//                return img_key;
//            }
//
//            public void setImg_key(List<String> img_key) {
//                this.img_key = img_key;
//            }
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public double getValue() {
//                return value;
//            }
//
//            public void setValue(double value) {
//                this.value = value;
//            }
//
//            public int getStatus() {
//                return status;
//            }
//
//            public void setStatus(int status) {
//                this.status = status;
//            }
//        }
//    }
}
