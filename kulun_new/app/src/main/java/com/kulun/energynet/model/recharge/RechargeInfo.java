package com.kulun.energynet.model.recharge;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Orion on 2017/7/21.
 */
public class RechargeInfo implements Serializable {
    /**
     * 分页返回结果：
     * id Number id值；
     * bigType int 1充电充值，0换电充值；
     * changeBalance Number 充值金额；
     * payType Number 充值类型；     换电类型：0微信，1支付宝，2现金；     充电类型：1现金，2微信，3支付宝
     * createTime Time 充值时间；
     * name String 站点名称；
     */
    private Number id, changeBalance;
    private int bigType, payType;
    private String createTime, name;

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public Number getChangeBalance() {
        return changeBalance;
    }

    public void setChangeBalance(Number changeBalance) {
        this.changeBalance = changeBalance;
    }

    public int getBigType() {
        return bigType;
    }

    public void setBigType(int bigType) {
        this.bigType = bigType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    //    private Integer id;
//
//    private Integer changeType;
//
//    private Integer payType;
//
//    private Integer accountId;
//
//    private Integer exchangeRecordId;
//
//    private Integer siteId;
//
//    private Integer adminUserId;
//
//    private BigDecimal nowBalance;
//
//    private BigDecimal changeBalance;
//
//    private Integer nowScore;
//
//    private Integer changeScore;
//
//    private BigDecimal nowCoupon;
//
//    private BigDecimal changeCoupon;
//
//    private String description;
//
//    private String remark;
//
//    private Long createTime;
//
//    private Boolean del;
//
//    private String name;
//
//    private String account;
//
//    private String siteName;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getChangeType() {
//        return changeType;
//    }
//
//    public void setChangeType(Integer changeType) {
//        this.changeType = changeType;
//    }
//
//    public Integer getPayType() {
//        return payType;
//    }
//
//    public void setPayType(Integer payType) {
//        this.payType = payType;
//    }
//
//    public Integer getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(Integer accountId) {
//        this.accountId = accountId;
//    }
//
//    public Integer getExchangeRecordId() {
//        return exchangeRecordId;
//    }
//
//    public void setExchangeRecordId(Integer exchangeRecordId) {
//        this.exchangeRecordId = exchangeRecordId;
//    }
//
//    public Integer getSiteId() {
//        return siteId;
//    }
//
//    public void setSiteId(Integer siteId) {
//        this.siteId = siteId;
//    }
//
//    public Integer getAdminUserId() {
//        return adminUserId;
//    }
//
//    public void setAdminUserId(Integer adminUserId) {
//        this.adminUserId = adminUserId;
//    }
//
//    public BigDecimal getNowBalance() {
//        return nowBalance;
//    }
//
//    public void setNowBalance(BigDecimal nowBalance) {
//        this.nowBalance = nowBalance;
//    }
//
//    public BigDecimal getChangeBalance() {
//        return changeBalance;
//    }
//
//    public void setChangeBalance(BigDecimal changeBalance) {
//        this.changeBalance = changeBalance;
//    }
//
//    public Integer getNowScore() {
//        return nowScore;
//    }
//
//    public void setNowScore(Integer nowScore) {
//        this.nowScore = nowScore;
//    }
//
//    public Integer getChangeScore() {
//        return changeScore;
//    }
//
//    public void setChangeScore(Integer changeScore) {
//        this.changeScore = changeScore;
//    }
//
//    public BigDecimal getNowCoupon() {
//        return nowCoupon;
//    }
//
//    public void setNowCoupon(BigDecimal nowCoupon) {
//        this.nowCoupon = nowCoupon;
//    }
//
//    public BigDecimal getChangeCoupon() {
//        return changeCoupon;
//    }
//
//    public void setChangeCoupon(BigDecimal changeCoupon) {
//        this.changeCoupon = changeCoupon;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description == null ? null : description.trim();
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark == null ? null : remark.trim();
//    }
//
//    public Long getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Long createTime) {
//        this.createTime = createTime;
//    }
//
//    public Boolean getDel() {
//        return del;
//    }
//
//    public void setDel(Boolean del) {
//        this.del = del;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
//
//    public String getSiteName() {
//        return siteName;
//    }
//
//    public void setSiteName(String siteName) {
//        this.siteName = siteName;
//    }
}
