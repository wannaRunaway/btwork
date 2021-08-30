package com.btkj.chongdianbao.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/16
 */
public class ReserverCurrentModel implements Serializable {

    /**
     * code : 200
     * content : {"record":{"batteryCount":4,"batteryType":12,"bindId":47,"bindLogId":2495,"cityId":null,"createTime":1566888667000,"del":false,"delayedCount":0,"delayedTime":0,"endTime":1566890467000,"endUseTime":1566888667000,"id":154,"latitude":null,"longitude":null,"plateNo":"","reason":"","startTime":1566888667000,"stationId":4,"stationName":"","status":0},"station":{"accountMain":null,"address":"杭州市西湖区留和路129号安能物流","appointmentBatteryCountMap":{"kwh10":0,"kwh12":4,"kwh15":0},"availBatteryCountMap":{"kwh10":1056,"kwh12":146,"kwh15":1002},"channelCount":92,"cityId":330100,"companyId":17,"contact":"杨康","createTime":1563330068000,"del":false,"distance":null,"exStationId":3,"exchangeRuleDetail":null,"fullElectBatteryCountMap":{"kwh10":1056,"kwh12":150,"kwh15":1002},"id":4,"latitude":30.23132,"locLatitude":null,"locLongitude":null,"longitude":120.05118,"name":"留下","number":"00003","place":"浙江省 杭州市 西湖区","shareRule":null,"status":0,"statusApp":0,"telephone":"13296728167","totalBatteryCount":3119,"type":0,"updateTime":1563330068000,"workTime":"00:00~23:59","zoneId":330106}}
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
         * record : {"batteryCount":4,"batteryType":12,"bindId":47,"bindLogId":2495,"cityId":null,"createTime":1566888667000,"del":false,"delayedCount":0,"delayedTime":0,"endTime":1566890467000,"endUseTime":1566888667000,"id":154,"latitude":null,"longitude":null,"plateNo":"","reason":"","startTime":1566888667000,"stationId":4,"stationName":"","status":0}
         * station : {"accountMain":null,"address":"杭州市西湖区留和路129号安能物流","appointmentBatteryCountMap":{"kwh10":0,"kwh12":4,"kwh15":0},"availBatteryCountMap":{"kwh10":1056,"kwh12":146,"kwh15":1002},"channelCount":92,"cityId":330100,"companyId":17,"contact":"杨康","createTime":1563330068000,"del":false,"distance":null,"exStationId":3,"exchangeRuleDetail":null,"fullElectBatteryCountMap":{"kwh10":1056,"kwh12":150,"kwh15":1002},"id":4,"latitude":30.23132,"locLatitude":null,"locLongitude":null,"longitude":120.05118,"name":"留下","number":"00003","place":"浙江省 杭州市 西湖区","shareRule":null,"status":0,"statusApp":0,"telephone":"13296728167","totalBatteryCount":3119,"type":0,"updateTime":1563330068000,"workTime":"00:00~23:59","zoneId":330106}
         */

        private RecordBean record;
        private Station station;

        public RecordBean getRecord() {
            return record;
        }

        public void setRecord(RecordBean record) {
            this.record = record;
        }

        public Station getStation() {
            return station;
        }

        public void setStation(Station station) {
            this.station = station;
        }

        public class RecordBean implements Serializable {
            /**
             * batteryCount : 4
             * batteryType : 12
             * bindId : 47
             * bindLogId : 2495
             * cityId : null
             * createTime : 1566888667000
             * del : false
             * delayedCount : 0
             * delayedTime : 0
             * endTime : 1566890467000
             * endUseTime : 1566888667000
             * id : 154
             * latitude : null
             * longitude : null
             * plateNo :
             * reason :
             * startTime : 1566888667000
             * stationId : 4
             * stationName :
             * status : 0
             */

            private int batteryCount;
            private int batteryType;
            private int bindId;
            private int bindLogId;
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
            private String stationName;
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

            public int getBindLogId() {
                return bindLogId;
            }

            public void setBindLogId(int bindLogId) {
                this.bindLogId = bindLogId;
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
        }
    }
}
