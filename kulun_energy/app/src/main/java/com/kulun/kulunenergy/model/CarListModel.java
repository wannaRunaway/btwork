package com.kulun.kulunenergy.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/8/15
 */
public class CarListModel implements Serializable {

    /**
     * code : 200
     * content : [{"plateNo":"浙A278J3","carTypeId":5,"batteryCount":4,"kwh":12,"bindId":26,"status":1,"carId":1,"carTypeName":"E17"}]
     * msg : 成功
     * success : true
     */

    private int code;
    private String msg;
    private boolean success;
    private List<ContentBean> content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public class ContentBean implements Serializable{
        /**
         * plateNo : 浙A278J3
         * carTypeId : 5
         * batteryCount : 4
         * kwh : 12
         * bindId : 26
         * status : 1
         * carId : 1
         * carTypeName : E17
         */

        private String plateNo;
        private int carTypeId;
        private int batteryCount;
        private int kwh;
        private int bindId;
        private int status;
        private int carId;
        private String carTypeName;

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public int getCarTypeId() {
            return carTypeId;
        }

        public void setCarTypeId(int carTypeId) {
            this.carTypeId = carTypeId;
        }

        public int getBatteryCount() {
            return batteryCount;
        }

        public void setBatteryCount(int batteryCount) {
            this.batteryCount = batteryCount;
        }

        public int getKwh() {
            return kwh;
        }

        public void setKwh(int kwh) {
            this.kwh = kwh;
        }

        public int getBindId() {
            return bindId;
        }

        public void setBindId(int bindId) {
            this.bindId = bindId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCarId() {
            return carId;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public String getCarTypeName() {
            return carTypeName;
        }

        public void setCarTypeName(String carTypeName) {
            this.carTypeName = carTypeName;
        }
    }
}
