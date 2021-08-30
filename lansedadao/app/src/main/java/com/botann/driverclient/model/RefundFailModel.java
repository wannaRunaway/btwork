package com.botann.driverclient.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/12/11
 */
public class RefundFailModel implements Serializable {

    /**
     * code : -1
     * msg : 充电账户退款审核中
     * content : {"id":192,"accountId":283,"exchangeRecordId":0,"serialNumber":"","type":4,"status":0,"refundScore":0,"refundMoney":938.21,"refundCoupon":0,"days":0,"refundReason":"","checkReason":"","refundUserId":2,"refundUser":"时空耀顶","checkUserId":0,"checkUser":"","createTime":1576049653000,"checkTime":null,"del":false,"tradeRefundList":null}
     * success : false
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
         * id : 192
         * accountId : 283
         * exchangeRecordId : 0
         * serialNumber :
         * type : 4
         * status : 0
         * refundScore : 0
         * refundMoney : 938.21
         * refundCoupon : 0.0
         * days : 0
         * refundReason :
         * checkReason :
         * refundUserId : 2
         * refundUser : 时空耀顶
         * checkUserId : 0
         * checkUser :
         * createTime : 1576049653000
         * checkTime : null
         * del : false
         * tradeRefundList : null
         */

        private int id;
        private int accountId;
        private int exchangeRecordId;
        private String serialNumber;
        private int type;
        private int status;
        private int refundScore;
        private double refundMoney;
        private double refundCoupon;
        private int days;
        private String refundReason;
        private String checkReason;
        private int refundUserId;
        private String refundUser;
        private int checkUserId;
        private String checkUser;
        private long createTime;
        private Object checkTime;
        private boolean del;
        private Object tradeRefundList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public int getExchangeRecordId() {
            return exchangeRecordId;
        }

        public void setExchangeRecordId(int exchangeRecordId) {
            this.exchangeRecordId = exchangeRecordId;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
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

        public int getRefundScore() {
            return refundScore;
        }

        public void setRefundScore(int refundScore) {
            this.refundScore = refundScore;
        }

        public double getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(double refundMoney) {
            this.refundMoney = refundMoney;
        }

        public double getRefundCoupon() {
            return refundCoupon;
        }

        public void setRefundCoupon(double refundCoupon) {
            this.refundCoupon = refundCoupon;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getRefundReason() {
            return refundReason;
        }

        public void setRefundReason(String refundReason) {
            this.refundReason = refundReason;
        }

        public String getCheckReason() {
            return checkReason;
        }

        public void setCheckReason(String checkReason) {
            this.checkReason = checkReason;
        }

        public int getRefundUserId() {
            return refundUserId;
        }

        public void setRefundUserId(int refundUserId) {
            this.refundUserId = refundUserId;
        }

        public String getRefundUser() {
            return refundUser;
        }

        public void setRefundUser(String refundUser) {
            this.refundUser = refundUser;
        }

        public int getCheckUserId() {
            return checkUserId;
        }

        public void setCheckUserId(int checkUserId) {
            this.checkUserId = checkUserId;
        }

        public String getCheckUser() {
            return checkUser;
        }

        public void setCheckUser(String checkUser) {
            this.checkUser = checkUser;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(Object checkTime) {
            this.checkTime = checkTime;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public Object getTradeRefundList() {
            return tradeRefundList;
        }

        public void setTradeRefundList(Object tradeRefundList) {
            this.tradeRefundList = tradeRefundList;
        }
    }
}
