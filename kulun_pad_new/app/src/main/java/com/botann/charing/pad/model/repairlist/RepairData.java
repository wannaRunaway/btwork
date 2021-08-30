package com.botann.charing.pad.model.repairlist;

import java.io.Serializable;

/**
 * created by xuedi on 2018/11/6
 */
public class RepairData implements Serializable {
    /*
    * 报修详情-所有状态都显示
faultReportPersion String 上报人员
sendRepairTime Date  报修时间
faultPhenomenon String 故障现象
status int 状态；1 已报修（红色），2 已维修（黄色） ，3 已完成\已修复（绿色），4 未修复（红色）
stationCityName String 上报站点城市
siteName String 站点名称
demageShelfNumber String 设备编号
IdNumber String 监控屏编号;监控屏版本号
demageWarehouseNumber String 仓位编号
demageCode String 故障代码
faultLevel int 故障等级
-------------------------------------------------
维修详情-已维修，已修复状态显示
repairPersion String 维修人员
repairTime Date 维修时间
repairContent String 维修内容
    * */

    private int id, siteId, faultLevel, status;
    private String faultClassificationZero, faultReason, faultPhenomenon, demageShelfNumber, demageWarehouseNumber, demageCode, superDemagePosition, faultReportPersion;
    private String repairContent, repairPersion, idNumber, siteName;
    private long sendRepairTime, dealRepairTime, faultTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getFaultLevel() {
        return faultLevel;
    }

    public String getFaultLevelString(){
        String stringLevel = null;
        switch (faultLevel) {
            case 1:
                stringLevel = "一级";
                break;
            case 2:
                stringLevel = "二级";
                break;
            case 3:
                stringLevel = "三级";
                break;
            default:
                break;
        }
        return stringLevel;
    }

    public void setFaultLevel(int faultLevel) {
        this.faultLevel = faultLevel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFaultClassificationZero() {
        return faultClassificationZero;
    }

    public void setFaultClassificationZero(String faultClassificationZero) {
        this.faultClassificationZero = faultClassificationZero;
    }

    public String getFaultReason() {
        return faultReason;
    }

    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

    public String getFaultPhenomenon() {
        return faultPhenomenon;
    }

    public void setFaultPhenomenon(String faultPhenomenon) {
        this.faultPhenomenon = faultPhenomenon;
    }

    public String getDemageShelfNumber() {
        return demageShelfNumber;
    }

    public void setDemageShelfNumber(String demageShelfNumber) {
        this.demageShelfNumber = demageShelfNumber;
    }

    public String getDemageWarehouseNumber() {
        return demageWarehouseNumber;
    }

    public void setDemageWarehouseNumber(String demageWarehouseNumber) {
        this.demageWarehouseNumber = demageWarehouseNumber;
    }

    public String getDemageCode() {
        return demageCode;
    }

    public void setDemageCode(String demageCode) {
        this.demageCode = demageCode;
    }

    public String getSuperDemagePosition() {
        return superDemagePosition;
    }

    public void setSuperDemagePosition(String superDemagePosition) {
        this.superDemagePosition = superDemagePosition;
    }

    public String getFaultReportPersion() {
        return faultReportPersion;
    }

    public void setFaultReportPersion(String faultReportPersion) {
        this.faultReportPersion = faultReportPersion;
    }

    public String getRepairContent() {
        return repairContent;
    }

    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    public String getRepairPersion() {
        return repairPersion;
    }

    public void setRepairPersion(String repairPersion) {
        this.repairPersion = repairPersion;
    }

    public long getSendRepairTime() {
        return sendRepairTime;
    }

    public void setSendRepairTime(long sendRepairTime) {
        this.sendRepairTime = sendRepairTime;
    }

    public long getDealRepairTime() {
        return dealRepairTime;
    }

    public void setDealRepairTime(long dealRepairTime) {
        this.dealRepairTime = dealRepairTime;
    }

    public long getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(long faultTime) {
        this.faultTime = faultTime;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     *      * 			"id": 10,
     *      * 			"siteId": 1,
     *      * 			"faultClassificationZero": "充电架",
     *
     *      * 			"faultClassificationOne": "",
     *      * 			"faultClassificationTwo": "",
     *      * 			"faultClassificationThree": "",
     *
     *      * 			"faultReason": "",
     *      * 			"faultLevel": 1,
     *      * 			"faultPhenomenon": "",
     *      * 			"demageShelfNumber": "1",
     *      * 			"demageWarehouseNumber": "1",
     *      * 			"demageCode": "E01",
     *      * 			"superDemagePosition": "",
     *      * 			"status": 1,
     *      * 			"faultReportPersion": "",
     *      * 			"repairTime": 0.00,
     *      * 			"repairContent": "",
     *      * 			"createDate": 1541486134000,
     *      * 			"updateDate": 1541486134000,
     *      * 			"sendRepairTime": 1541486134000,
     *      * 			"dealRepairTime": 1541486134000,
     *      * 			"faultTime": 1541486100000,
     *      * 			"remark": "新增报修",
     *      * 			"operator": "admin",
     *      * 			"del": 0,
     *      * 			"pageNo": null,
     *      * 			"pageSize": null,
     *      * 			"pageStart": null,
     *      * 			"startTime": null,
     *      * 			"endTime": null,
     *      * 			"faultTimeStr": null,
     *      * 			"startTimeStr": null,
     *      * 			"endTimeStr": null,
     *      * 			"adminId": null,
     *      * 			"idNumber": "1"
     */
}
