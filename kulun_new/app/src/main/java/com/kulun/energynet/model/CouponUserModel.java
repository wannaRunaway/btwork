package com.kulun.energynet.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * created by xuedi on 2019/9/29
 */
public class CouponUserModel implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * content : {"total":11,"data":[{"id":3012,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569564000000,"expireDate":1572156000000,"remark":"充电营销活动发放","createTime":1569564000000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":0,"produceAccountId":0,"couponAndTemplate":null},{"id":3013,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569564000000,"expireDate":1572156000000,"remark":"充电营销活动发放","createTime":1569564000000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":0,"produceAccountId":0,"couponAndTemplate":null},{"id":3014,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569564000000,"expireDate":1572156000000,"remark":"充电营销活动发放","createTime":1569564000000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":0,"produceAccountId":0,"couponAndTemplate":null},{"id":3120,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569600001000,"expireDate":1572192001000,"remark":"充电营销活动发放","createTime":1569600001000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":607,"produceAccountId":240,"couponAndTemplate":null},{"id":3121,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569600001000,"expireDate":1572192001000,"remark":"充电营销活动发放","createTime":1569600001000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":608,"produceAccountId":240,"couponAndTemplate":null},{"id":3119,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569600001000,"expireDate":1572192001000,"remark":"充电营销活动发放","createTime":1569600001000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":606,"produceAccountId":240,"couponAndTemplate":null},{"id":3218,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569686400000,"expireDate":1572278400000,"remark":"充电营销活动发放","createTime":1569686400000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":705,"produceAccountId":240,"couponAndTemplate":null},{"id":3219,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569686400000,"expireDate":1572278400000,"remark":"充电营销活动发放","createTime":1569686400000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":706,"produceAccountId":240,"couponAndTemplate":null},{"id":3220,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569686401000,"expireDate":1572278401000,"remark":"充电营销活动发放","createTime":1569686401000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":707,"produceAccountId":240,"couponAndTemplate":null},{"id":3692,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569735300000,"expireDate":1572327300000,"remark":"充电营销活动发放","createTime":1569735300000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":1176,"produceAccountId":240,"couponAndTemplate":null},{"id":3693,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569735300000,"expireDate":1572327300000,"remark":"充电营销活动发放","createTime":1569735300000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":1177,"produceAccountId":240,"couponAndTemplate":null}]}
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
         * total : 11
         * data : [{"id":3012,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569564000000,"expireDate":1572156000000,"remark":"充电营销活动发放","createTime":1569564000000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":0,"produceAccountId":0,"couponAndTemplate":null},{"id":3013,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569564000000,"expireDate":1572156000000,"remark":"充电营销活动发放","createTime":1569564000000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":0,"produceAccountId":0,"couponAndTemplate":null},{"id":3014,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569564000000,"expireDate":1572156000000,"remark":"充电营销活动发放","createTime":1569564000000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":0,"produceAccountId":0,"couponAndTemplate":null},{"id":3120,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569600001000,"expireDate":1572192001000,"remark":"充电营销活动发放","createTime":1569600001000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":607,"produceAccountId":240,"couponAndTemplate":null},{"id":3121,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569600001000,"expireDate":1572192001000,"remark":"充电营销活动发放","createTime":1569600001000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":608,"produceAccountId":240,"couponAndTemplate":null},{"id":3119,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569600001000,"expireDate":1572192001000,"remark":"充电营销活动发放","createTime":1569600001000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":606,"produceAccountId":240,"couponAndTemplate":null},{"id":3218,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569686400000,"expireDate":1572278400000,"remark":"充电营销活动发放","createTime":1569686400000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":705,"produceAccountId":240,"couponAndTemplate":null},{"id":3219,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569686400000,"expireDate":1572278400000,"remark":"充电营销活动发放","createTime":1569686400000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":706,"produceAccountId":240,"couponAndTemplate":null},{"id":3220,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569686401000,"expireDate":1572278401000,"remark":"充电营销活动发放","createTime":1569686401000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":707,"produceAccountId":240,"couponAndTemplate":null},{"id":3692,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569735300000,"expireDate":1572327300000,"remark":"充电营销活动发放","createTime":1569735300000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":1176,"produceAccountId":240,"couponAndTemplate":null},{"id":3693,"type":50,"couponName":"无门槛优惠券15元","accountId":240,"amount":15,"cashAmount":0,"used":0,"status":10,"beginDate":1569735300000,"expireDate":1572327300000,"remark":"充电营销活动发放","createTime":1569735300000,"del":false,"carTypeId":0,"carType":null,"activityRecordId":1177,"produceAccountId":240,"couponAndTemplate":null}]
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
             * id : 3012
             * type : 50
             * couponName : 无门槛优惠券15元
             * accountId : 240
             * amount : 15
             * cashAmount : 0
             * used : 0
             * status : 10
             * beginDate : 1569564000000
             * expireDate : 1572156000000
             * remark : 充电营销活动发放
             * createTime : 1569564000000
             * del : false
             * carTypeId : 0
             * carType : null
             * activityRecordId : 0
             * produceAccountId : 0
             * couponAndTemplate : null
             */

            private int id;
            private int type;
            private String couponName;
            private int accountId;
            private BigDecimal amount;
            private BigDecimal cashAmount;
            private BigDecimal used;
            private int status;
            private long beginDate;
            private Long expireDate;
            private String remark;
            private long createTime;
            private boolean del;
            private int carTypeId;
            private Object carType;
            private int activityRecordId;
            private int produceAccountId;
            private Object couponAndTemplate;
            private List<String> stationNames;

            public List<String> getStationNames() {
                return stationNames;
            }

            public void setStationNames(List<String> stationNames) {
                this.stationNames = stationNames;
            }

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

            public String getCouponName() {
                return couponName;
            }

            public void setCouponName(String couponName) {
                this.couponName = couponName;
            }

            public int getAccountId() {
                return accountId;
            }

            public void setAccountId(int accountId) {
                this.accountId = accountId;
            }

            public BigDecimal getAmount() {
                return amount;
            }

            public void setAmount(BigDecimal amount) {
                this.amount = amount;
            }

            public BigDecimal getCashAmount() {
                return cashAmount;
            }

            public void setCashAmount(BigDecimal cashAmount) {
                this.cashAmount = cashAmount;
            }

            public BigDecimal getUsed() {
                return used;
            }

            public void setUsed(BigDecimal used) {
                this.used = used;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public long getBeginDate() {
                return beginDate;
            }

            public void setBeginDate(long beginDate) {
                this.beginDate = beginDate;
            }

            public Long getExpireDate() {
                return expireDate;
            }

            public void setExpireDate(Long expireDate) {
                this.expireDate = expireDate;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
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

            public int getCarTypeId() {
                return carTypeId;
            }

            public void setCarTypeId(int carTypeId) {
                this.carTypeId = carTypeId;
            }

            public Object getCarType() {
                return carType;
            }

            public void setCarType(Object carType) {
                this.carType = carType;
            }

            public int getActivityRecordId() {
                return activityRecordId;
            }

            public void setActivityRecordId(int activityRecordId) {
                this.activityRecordId = activityRecordId;
            }

            public int getProduceAccountId() {
                return produceAccountId;
            }

            public void setProduceAccountId(int produceAccountId) {
                this.produceAccountId = produceAccountId;
            }

            public Object getCouponAndTemplate() {
                return couponAndTemplate;
            }

            public void setCouponAndTemplate(Object couponAndTemplate) {
                this.couponAndTemplate = couponAndTemplate;
            }
        }
    }
}
