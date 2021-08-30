package com.kulun.energynet.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CouponModel implements Serializable {
    /**
     * total : 0
     * data : [{"id":130,"activityNumber":1161,"type":50,"name":"满20减10优惠券","amount":10,"receiveBeginTime":1569735300001,"receiveExpireTime":1569821700001,"effectFewDay":0,"effectDay":30,"accountType":0,"siteCompanyId":2,"remark":"充电营销活动发放","del":0,"createBy":"系统自动","createTime":1569735300635,"couponLimits":null,"couponId":0,"couponAndTemplate":null,"couponTemplateAndSet":null},{"id":121,"activityNumber":1159,"type":50,"name":"无门槛10元优惠券","amount":10,"receiveBeginTime":1569735300001,"receiveExpireTime":1569821700001,"effectFewDay":0,"effectDay":30,"accountType":0,"siteCompanyId":2,"remark":"充电营销活动发放","del":0,"createBy":"系统自动","createTime":1569735300048,"couponLimits":null,"couponId":0,"couponAndTemplate":null,"couponTemplateAndSet":null}]
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
         * id : 130
         * activityNumber : 1161
         * type : 50
         * name : 满20减10优惠券
         * amount : 10
         * receiveBeginTime : 1569735300001
         * receiveExpireTime : 1569821700001
         * effectFewDay : 0
         * effectDay : 30
         * accountType : 0
         * siteCompanyId : 2
         * remark : 充电营销活动发放
         * del : 0
         * createBy : 系统自动
         * createTime : 1569735300635
         * couponLimits : null
         * couponId : 0
         * couponAndTemplate : null
         * couponTemplateAndSet : null
         */

        private int id;
        private int activityNumber;
        private int type;
        private String name;
        private BigDecimal amount;
        private long receiveBeginTime;
        private long receiveExpireTime;
        private int effectFewDay;
        private int effectDay;
        private int accountType;
        private int siteCompanyId;
        private String remark;
        private int del;
        private String createBy;
        private long createTime;
        private Object couponLimits;
        private int couponId;
        private Object couponAndTemplate;
        private Object couponTemplateAndSet;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getActivityNumber() {
            return activityNumber;
        }

        public void setActivityNumber(int activityNumber) {
            this.activityNumber = activityNumber;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public long getReceiveBeginTime() {
            return receiveBeginTime;
        }

        public void setReceiveBeginTime(long receiveBeginTime) {
            this.receiveBeginTime = receiveBeginTime;
        }

        public long getReceiveExpireTime() {
            return receiveExpireTime;
        }

        public void setReceiveExpireTime(long receiveExpireTime) {
            this.receiveExpireTime = receiveExpireTime;
        }

        public int getEffectFewDay() {
            return effectFewDay;
        }

        public void setEffectFewDay(int effectFewDay) {
            this.effectFewDay = effectFewDay;
        }

        public int getEffectDay() {
            return effectDay;
        }

        public void setEffectDay(int effectDay) {
            this.effectDay = effectDay;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public int getSiteCompanyId() {
            return siteCompanyId;
        }

        public void setSiteCompanyId(int siteCompanyId) {
            this.siteCompanyId = siteCompanyId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getDel() {
            return del;
        }

        public void setDel(int del) {
            this.del = del;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getCouponLimits() {
            return couponLimits;
        }

        public void setCouponLimits(Object couponLimits) {
            this.couponLimits = couponLimits;
        }

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

        public Object getCouponAndTemplate() {
            return couponAndTemplate;
        }

        public void setCouponAndTemplate(Object couponAndTemplate) {
            this.couponAndTemplate = couponAndTemplate;
        }

        public Object getCouponTemplateAndSet() {
            return couponTemplateAndSet;
        }

        public void setCouponTemplateAndSet(Object couponTemplateAndSet) {
            this.couponTemplateAndSet = couponTemplateAndSet;
        }
    }
}
