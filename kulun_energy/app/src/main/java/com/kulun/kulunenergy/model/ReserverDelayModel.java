package com.kulun.kulunenergy.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/16
 */
public class ReserverDelayModel implements Serializable {

    /**
     * code : 200
     * content : {"batteryCount":4,"batteryType":12,"bindId":26,"cityId":null,"createTime":1565937894000,"del":false,"delayedCount":1,"delayedTime":10,"endTime":1565940294000,"endUseTime":1565937894000,"id":41,"latitude":null,"longitude":null,"plateNo":"","reason":"","startTime":1565937894000,"stationId":4,"status":0}
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

    public class ContentBean implements Serializable{
        /**
         * batteryCount : 4
         * batteryType : 12
         * bindId : 26
         * cityId : null
         * createTime : 1565937894000
         * del : false
         * delayedCount : 1
         * delayedTime : 10
         * endTime : 1565940294000
         * endUseTime : 1565937894000
         * id : 41
         * latitude : null
         * longitude : null
         * plateNo :
         * reason :
         * startTime : 1565937894000
         * stationId : 4
         * status : 0
         */

        private int batteryCount;
        private int batteryType;
        private int bindId;
        private Object cityId;
        private long createTime;
        private boolean del;
        private int delayedCount;
        private int delayedTime;
        private long endTime;
        private long endUseTime;
        private int id;
        private Object latitude;
        private Object longitude;
        private String plateNo;
        private String reason;
        private long startTime;
        private int stationId;
        private int status;

        public int getBatteryCount() {
            return batteryCount;
        }

        public void setBatteryCount(int batteryCount) {
            this.batteryCount = batteryCount;
        }

        public int getBatteryType() {
            return batteryType;
        }

        public void setBatteryType(int batteryType) {
            this.batteryType = batteryType;
        }

        public int getBindId() {
            return bindId;
        }

        public void setBindId(int bindId) {
            this.bindId = bindId;
        }

        public Object getCityId() {
            return cityId;
        }

        public void setCityId(Object cityId) {
            this.cityId = cityId;
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

        public int getDelayedCount() {
            return delayedCount;
        }

        public void setDelayedCount(int delayedCount) {
            this.delayedCount = delayedCount;
        }

        public int getDelayedTime() {
            return delayedTime;
        }

        public void setDelayedTime(int delayedTime) {
            this.delayedTime = delayedTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getEndUseTime() {
            return endUseTime;
        }

        public void setEndUseTime(long endUseTime) {
            this.endUseTime = endUseTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
