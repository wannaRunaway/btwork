package com.botann.driverclient.model.chongdian;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/15
 */
public class Connector implements Serializable {
    /**
     * id : 26
     * stationId : 654
     * operatorId : MA1MY0GF9
     * equipmentId : 32010100000106
     * connectorId : 3201020000003907
     * connectorName : CQC007-A
     * connectorType : 4
     * voltageLowerLimits : 500.0
     * voltageUpperLimits : 500.0
     * current : 60.0
     * power : 30.0
     * parkNo : 0
     * nationalStandard : 2
     * status : 3
     * parkStatus : 0
     * lockStatus : 0
     * createTime : 1561558441000
     * updateTime : 1562554440000
     * del : false
     */

    private int id;
    private String stationId;
    private String operatorId;
    private String equipmentId;
    private String connectorId;
    private String connectorName;
    private int connectorType;
    private double voltageLowerLimits;
    private double voltageUpperLimits;
    private double current;
    private double power;
    private String parkNo;
    private int nationalStandard;
    private int status;
    private int parkStatus;
    private int lockStatus;
    private long createTime;
    private long updateTime;
    private boolean del;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }

    public int getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(int connectorType) {
        this.connectorType = connectorType;
    }

    public double getVoltageLowerLimits() {
        return voltageLowerLimits;
    }

    public void setVoltageLowerLimits(double voltageLowerLimits) {
        this.voltageLowerLimits = voltageLowerLimits;
    }

    public double getVoltageUpperLimits() {
        return voltageUpperLimits;
    }

    public void setVoltageUpperLimits(double voltageUpperLimits) {
        this.voltageUpperLimits = voltageUpperLimits;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getParkNo() {
        return parkNo;
    }

    public void setParkNo(String parkNo) {
        this.parkNo = parkNo;
    }

    public int getNationalStandard() {
        return nationalStandard;
    }

    public void setNationalStandard(int nationalStandard) {
        this.nationalStandard = nationalStandard;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getParkStatus() {
        return parkStatus;
    }

    public void setParkStatus(int parkStatus) {
        this.parkStatus = parkStatus;
    }

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }
    /**
     * {
     * 			"id": 26,
     * 			"stationId": "654",
     * 			"operatorId": "MA1MY0GF9",
     * 			"equipmentId": "32010100000106",
     * 			"connectorId": "3201020000003907",
     * 			"connectorName": "CQC007-A",
     * 			"connectorType": 4,
     * 			"voltageLowerLimits": 500.0,
     * 			"voltageUpperLimits": 500.0,
     * 			"current": 60.0,
     * 			"power": 30.0,
     * 			"parkNo": "0",
     * 			"nationalStandard": 2,
     * 			"status": 3,
     * 			"parkStatus": 0,
     * 			"lockStatus": 0,
     * 			"createTime": 1561558441000,
     * 			"updateTime": 1562554440000,
     * 			"del": false
     *                },
     */


}
