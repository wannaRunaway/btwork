package com.kulun.energynet.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/10/29
 */
public class PunchModel implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * content : {"total":0,"data":[{"id":10,"type":0,"status":0,"accountId":232,"carId":148,"carMile":105811,"soc":21,"relateId":0,"createTime":1572337809000,"del":false,"driverName":"含里程满","account":null,"car":null,"carInfo":null,"bind":null}]}
     * success : true
     */

    private int code;
    private String msg;
    private ContentBean content;
    private boolean success;

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

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public class ContentBean implements Serializable{
        /**
         * total : 0
         * data : [{"id":10,"type":0,"status":0,"accountId":232,"carId":148,"carMile":105811,"soc":21,"relateId":0,"createTime":1572337809000,"del":false,"driverName":"含里程满","account":null,"car":null,"carInfo":null,"bind":null}]
         */

        private int total;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public class DataBean implements Serializable{
            /**
             * id : 10
             * type : 0
             * status : 0
             * accountId : 232
             * carId : 148
             * carMile : 105811
             * soc : 21
             * relateId : 0
             * createTime : 1572337809000
             * del : false
             * driverName : 含里程满
             * account : null
             * car : null
             * carInfo : null
             * bind : null
             */

            private int id;
            private int type;
            private int status;
            private int accountId;
            private int carId;
            private int carMile;
            private double soc;
            private int relateId;
            private long createTime;
            private boolean del;
            private String driverName;
            private Object account;
            private Object car;
            private Object carInfo;
            private Object bind;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getAccountId() {
                return accountId;
            }

            public void setAccountId(int accountId) {
                this.accountId = accountId;
            }

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

            public double getSoc() {
                return soc;
            }

            public void setSoc(double soc) {
                this.soc = soc;
            }

            public int getRelateId() {
                return relateId;
            }

            public void setRelateId(int relateId) {
                this.relateId = relateId;
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

            public String getDriverName() {
                return driverName;
            }

            public void setDriverName(String driverName) {
                this.driverName = driverName;
            }

            public Object getAccount() {
                return account;
            }

            public void setAccount(Object account) {
                this.account = account;
            }

            public Object getCar() {
                return car;
            }

            public void setCar(Object car) {
                this.car = car;
            }

            public Object getCarInfo() {
                return carInfo;
            }

            public void setCarInfo(Object carInfo) {
                this.carInfo = carInfo;
            }

            public Object getBind() {
                return bind;
            }

            public void setBind(Object bind) {
                this.bind = bind;
            }
        }
    }
}
