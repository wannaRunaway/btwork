package com.btkj.chongdianbao.model;

public class ClockBean {

    /**
     * code : 200
     * content : {"carId":8,"carMile":1000,"carTboxMile":20540,"createTime":1594881661000,"del":false,"driverId":13,"driverName":"返利平","id":2,"relateId":0,"soc":100,"status":4,"type":0}
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

    public static class ContentBean {
        /**
         * carId : 8
         * carMile : 1000
         * carTboxMile : 20540
         * createTime : 1594881661000
         * del : false
         * driverId : 13
         * driverName : 返利平
         * id : 2
         * relateId : 0
         * soc : 100.0
         * status : 4
         * type : 0
         */

        private int carId;
        private int carMile;
        private int carTboxMile;
        private long createTime;
        private boolean del;
        private int driverId;
        private String driverName;
        private int id;
        private int relateId;
        private double soc;
        private int status;
        private int type;

        public int getCarId() {
            return carId;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public int getCarMile() {
            return carMile;
        }

        public void setCarMile(int carMile) {
            this.carMile = carMile;
        }

        public int getCarTboxMile() {
            return carTboxMile;
        }

        public void setCarTboxMile(int carTboxMile) {
            this.carTboxMile = carTboxMile;
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

        public int getDriverId() {
            return driverId;
        }

        public void setDriverId(int driverId) {
            this.driverId = driverId;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRelateId() {
            return relateId;
        }

        public void setRelateId(int relateId) {
            this.relateId = relateId;
        }

        public double getSoc() {
            return soc;
        }

        public void setSoc(double soc) {
            this.soc = soc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
