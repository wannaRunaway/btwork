package com.botann.driverclient.model.consume;

import java.io.Serializable;

/**
 * Created by Orion on 2017/7/19.
 */
public class ConsumeInfo implements Serializable {
    /**
     * "id": 845,
     * 			"serialNum": "2018103000000845",
     * 			"createDate": 1540880818000,
     * 			"stationName": "留下(杭州市 西湖区)",
     * 			"carNumber": "浙GPB999",
     * 			"realMile": 100,
     * 			"referMiles": 110341,
     * 			"realFare": 45.0,
     * 			"coupon": 0.0,
     * 			"balance": 55.0,
     * 			"questionStatus": 1,
     * 			"commentStatus": 0,
     * 			"travelMile": 110341,
     * 			"fare": 45.0,
     * 			"leftMile": 0,
     * 			"monthMile": 0
     * 		refundMoney
     */
    /**
     * bigType int 1充电类型 0换电类型；
     * id int 记录id；
     * stationName string 站点名称；
     * serialNo string 订单号；
     * plateNumber string 车牌号；
     * createTime time 订单时间；
     * realFare number 实际支付金额；
     * coupon number 优惠券使用金额；
     * balance number 账户余额，bigType=1 充电账户，bigType=0 换电账户；
     * --充电字段--chargeTime int 充电时长，单位：秒；totalPower number 累计充电量，单位：度；
     * --换电字段--
     * totalMile int 总里程；
     * changeMile int 计费里程；
     * monthMile int 包月里程；
     * leftMile int 包月剩余里程；
     * accountMile int 里程账户剩余里程；
     */
    private int bigType, id;
    private String stationName, serialNo, plateNumber, createTime;
    private float realFare, coupon, balance;
    //换电字段
    private int totalMile, changeMile, monthMile, leftMile, accountMile;
    //充电字段
    private int chargeTime;
    private float totalPower;
    private int questionStatus, commentStatus;
    private double refundMoney;
    private int ruleType;

    public int getRuleType() {
        return ruleType;
    }

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public int getBigType() {
        return bigType;
    }
    public void setBigType(int bigType) {
        this.bigType = bigType;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public float getRealFare() {
        return realFare;
    }
    public void setRealFare(float realFare) {
        this.realFare = realFare;
    }
    public float getCoupon() {
        return coupon;
    }
    public void setCoupon(float coupon) {
        this.coupon = coupon;
    }
    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }
    public int getTotalMile() {
        return totalMile;
    }
    public void setTotalMile(int totalMile) {
        this.totalMile = totalMile;
    }
    public int getChangeMile() {
        return changeMile;
    }
    public void setChangeMile(int changeMile) {
        this.changeMile = changeMile;
    }
    public int getMonthMile() {
        return monthMile;
    }
    public void setMonthMile(int monthMile) {
        this.monthMile = monthMile;
    }
    public int getLeftMile() {
        return leftMile;
    }
    public void setLeftMile(int leftMile) {
        this.leftMile = leftMile;
    }
    public int getAccountMile() {
        return accountMile;
    }
    public void setAccountMile(int accountMile) {
        this.accountMile = accountMile;
    }
    public int getChargeTime() {
        return chargeTime;
    }
    public void setChargeTime(int chargeTime) {
        this.chargeTime = chargeTime;
    }
    public float getTotalPower() {
        return totalPower;
    }
    public void setTotalPower(float totalPower) {
        this.totalPower = totalPower;
    }

    public int getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(int questionStatus) {
        this.questionStatus = questionStatus;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }
    //    private Integer id;
//    private String serialNum;
//    private Long createDate;
//    private String stationName;
//    private String carNumber;
//    private Integer realMile;
//    private Integer referMiles;
//    private Double realFare;
//    private Double coupon;
//    private Double balance;
//    private Integer questionStatus;
//    private Integer commentStatus;
//    private Integer leftMile, monthMile;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getSerialNum() {
//        return serialNum;
//    }
//
//    public void setSerialNum(String serialNum) {
//        this.serialNum = serialNum;
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
//    public String getStationName() {
//        return stationName;
//    }
//
//    public void setStationName(String stationName) {
//        this.stationName = stationName;
//    }
//
//    public String getCarNumber() {
//        return carNumber;
//    }
//
//    public void setCarNumber(String carNumber) {
//        this.carNumber = carNumber;
//    }
//
//    public Integer getRealMile() {
//        return realMile;
//    }
//
//    public void setRealMile(Integer realMile) {
//        this.realMile = realMile;
//    }
//
//    public Integer getReferMiles() {
//        return referMiles;
//    }
//
//    public void setReferMiles(Integer referMiles) {
//        this.referMiles = referMiles;
//    }
//
//    public Double getRealFare() {
//        return realFare;
//    }
//
//    public void setRealFare(Double realFare) {
//        this.realFare = realFare;
//    }
//
//    public Double getCoupon() {
//        return coupon;
//    }
//
//    public void setCoupon(Double coupon) {
//        this.coupon = coupon;
//    }
//
//    public Double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(Double balance) {
//        this.balance = balance;
//    }
//
//    public Integer getQuestionStatus() {
//        return questionStatus;
//    }
//
//    public void setQuestionStatus(Integer questionStatus) {
//        this.questionStatus = questionStatus;
//    }
//
//    public Integer getCommentStatus() {
//        return commentStatus;
//    }
//
//    public void setCommentStatus(Integer commentStatus) {
//        this.commentStatus = commentStatus;
//    }
//
//    public Integer getLeftMile() {
//        return leftMile;
//    }
//
//    public void setLeftMile(Integer leftMile) {
//        this.leftMile = leftMile;
//    }
//
//    public Integer getMonthMile() {
//        return monthMile;
//    }
//
//    public void setMonthMile(Integer monthMile) {
//        this.monthMile = monthMile;
//    }
}
