package com.kulun.energynet.ui.fragment;

import java.io.Serializable;

/**
 * created by xuedi on 2020/1/7
 */
public class ConsumeAllModel implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * content : {"count":16,"totalRealFare":687.17,"totalRefundMoney":0,"totalCoupon":133.5}
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
         * count : 16
         * totalRealFare : 687.17
         * totalRefundMoney : 0
         * totalCoupon : 133.5
         */

        private int count;
        private double totalRealFare, totalRefundMoney;
        private double totalCoupon;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getTotalRealFare() {
            return totalRealFare;
        }

        public void setTotalRealFare(double totalRealFare) {
            this.totalRealFare = totalRealFare;
        }

        public double getTotalCoupon() {
            return totalCoupon;
        }

        public void setTotalCoupon(double totalCoupon) {
            this.totalCoupon = totalCoupon;
        }

        public double getTotalRefundMoney() {
            return totalRefundMoney;
        }

        public void setTotalRefundMoney(double totalRefundMoney) {
            this.totalRefundMoney = totalRefundMoney;
        }
    }
}
