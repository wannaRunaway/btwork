package com.botann.driverclient.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/12/10
 */
public class RefundModel implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * content : {"total":0,"data":[{"id":14,"accountId":240,"applyNo":"RDSwom120191210155942","status":1,"type":0,"outTradeNo":"RDGrk8D20191210154624","outRequestNo":"RDwbt7l20191210155942","totalAmount":0.01,"amount":0.01,"refundTime":1575964784000,"failReason":"","failNo":"","failAmount":0,"remark":"司机app自主申请充电账户退款","updateTime":1575964784000,"createTime":1575964783000,"refundApplyType":4}]}
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
         * data : [{"id":14,"accountId":240,"applyNo":"RDSwom120191210155942","status":1,"type":0,"outTradeNo":"RDGrk8D20191210154624","outRequestNo":"RDwbt7l20191210155942","totalAmount":0.01,"amount":0.01,"refundTime":1575964784000,"failReason":"","failNo":"","failAmount":0,"remark":"司机app自主申请充电账户退款","updateTime":1575964784000,"createTime":1575964783000,"refundApplyType":4}]
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
             * id : 14
             * accountId : 240
             * applyNo : RDSwom120191210155942
             * status : 1
             * type : 0
             * outTradeNo : RDGrk8D20191210154624
             * outRequestNo : RDwbt7l20191210155942
             * totalAmount : 0.01
             * amount : 0.01
             * refundTime : 1575964784000
             * failReason :
             * failNo :
             * failAmount : 0
             * remark : 司机app自主申请充电账户退款
             * updateTime : 1575964784000
             * createTime : 1575964783000
             * refundApplyType : 4
             */

            private int id;
            private int accountId;
            private String applyNo;
            private int status;
            private int type;
            private String outTradeNo;
            private String outRequestNo;
            private double totalAmount;
            private double amount;
            private long refundTime;
            private String failReason;
            private String failNo;
            private int failAmount;
            private String remark;
            private long updateTime;
            private long createTime;
            private int refundApplyType;

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

            public String getApplyNo() {
                return applyNo;
            }

            public void setApplyNo(String applyNo) {
                this.applyNo = applyNo;
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

            public String getOutTradeNo() {
                return outTradeNo;
            }

            public void setOutTradeNo(String outTradeNo) {
                this.outTradeNo = outTradeNo;
            }

            public String getOutRequestNo() {
                return outRequestNo;
            }

            public void setOutRequestNo(String outRequestNo) {
                this.outRequestNo = outRequestNo;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public long getRefundTime() {
                return refundTime;
            }

            public void setRefundTime(long refundTime) {
                this.refundTime = refundTime;
            }

            public String getFailReason() {
                return failReason;
            }

            public void setFailReason(String failReason) {
                this.failReason = failReason;
            }

            public String getFailNo() {
                return failNo;
            }

            public void setFailNo(String failNo) {
                this.failNo = failNo;
            }

            public int getFailAmount() {
                return failAmount;
            }

            public void setFailAmount(int failAmount) {
                this.failAmount = failAmount;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getRefundApplyType() {
                return refundApplyType;
            }

            public void setRefundApplyType(int refundApplyType) {
                this.refundApplyType = refundApplyType;
            }
        }
    }
}
