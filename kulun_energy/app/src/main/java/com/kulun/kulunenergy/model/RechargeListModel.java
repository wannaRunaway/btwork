package com.kulun.kulunenergy.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/9/2
 */
public class RechargeListModel implements Serializable {

    /**
     * code : 200
     * content : [{"accountNo":"2019082600000064","balance":1111111,"createTime":1566888658000,"del":false,"id":131,"outTradeNo":"","payPlatform":0,"payWay":0,"remark":"后台充值，操作人：admin","status":1,"tradeNo":""}]
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
         * accountNo : 2019082600000064
         * balance : 1111111
         * createTime : 1566888658000
         * del : false
         * id : 131
         * outTradeNo :
         * payPlatform : 0
         * payWay : 0
         * remark : 后台充值，操作人：admin
         * status : 1
         * tradeNo :
         */

        private String accountNo;
        private double balance;
        private long createTime;
        private boolean del;
        private int id;
        private String outTradeNo;
        private int payPlatform;
        private int payWay;
        private String remark;
        private int status;
        private String tradeNo;

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public int getPayPlatform() {
            return payPlatform;
        }

        public void setPayPlatform(int payPlatform) {
            this.payPlatform = payPlatform;
        }

        public int getPayWay() {
            return payWay;
        }

        public void setPayWay(int payWay) {
            this.payWay = payWay;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }
    }
}
