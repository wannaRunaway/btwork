package com.kulun.kulunenergy.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/8/26
 */
public class AddCarModel implements Serializable {

    /**
     * code : 200
     * content : {"bindLogId":2474,"plateNo":"沪AD66385","carTypeId":2,"batteryCount":4,"kwh":12,"bindId":47,"status":1,"carId":20,"carTypeName":"ER30"}
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
         * bindLogId : 2474
         * plateNo : 沪AD66385
         * carTypeId : 2
         * batteryCount : 4
         * kwh : 12
         * bindId : 47
         * status : 1
         * carId : 20
         * carTypeName : ER30
         */

        private int bindLogId;
        private String plateNo;
        private int carTypeId;
        private int batteryCount;
        private int kwh;
        private int bindId;
        private int status;
        private int carId;
        private String carTypeName;

        public int getBindLogId() {
            return bindLogId;
        }

        public void setBindLogId(int bindLogId) {
            this.bindLogId = bindLogId;
        }

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
