package com.botann.driverclient.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Orion on 2017/8/3.
 */
public class CouponInfo implements Serializable {
    private Integer id;

    private Integer type;

    private String couponName;

    private Integer accountId;

    private BigDecimal amount;

    private BigDecimal cashAmount;

    private BigDecimal used;

    private Integer status;

    private Long beginDate;

    private Long expireDate;

    private String remark;

    private Long createTime;

    private Boolean del;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName == null ? null : couponName.trim();
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
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
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Boolean getDel() {
        return del;
    }

    public void setDel(Boolean del) {
        this.del = del;
    }
//
//    private Integer fare;
//    private Integer used;
//    private Integer usedAmouont;
//    private Long createDate;
//    private Long endDate;
//    private Integer prescription;
//    private String couponTitle;
//
//    public Integer getFare() {
//        return fare;
//    }
//
//    public void setFare(Integer fare) {
//        this.fare = fare;
//    }
//
//    public Integer getUsed() {
//        return used;
//    }
//
//    public void setUsed(Integer used) {
//        this.used = used;
//    }
//
//    public Integer getUsedAmouont() {
//        return usedAmouont;
//    }
//
//    public void setUsedAmouont(Integer usedAmouont) {
//        this.usedAmouont = usedAmouont;
//    }
//
//    public Long getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Long createDate) {
//        this.createDate = createDate;
//    }
//
//    public Long getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Long endDate) {
//        this.endDate = endDate;
//    }
//
//    public Integer getPrescription() {
//        return prescription;
//    }
//
//    public void setPrescription(Integer prescription) {
//        this.prescription = prescription;
//    }
//
//    public String getCouponTitle() {
//        return couponTitle;
//    }
//
//    public void setCouponTitle(String couponTitle) {
//        this.couponTitle = couponTitle;
//    }

}
