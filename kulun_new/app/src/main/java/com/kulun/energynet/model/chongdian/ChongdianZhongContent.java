package com.kulun.energynet.model.chongdian;
import java.io.Serializable;
/**
 * created by xuedi on 2019/7/3
 */
public class ChongdianZhongContent implements Serializable {
    /**
     * 对象字段：
     * id int 订单id；
     * startChargeSeqStat int 订单状态，1启动中，2充电中，3停止中，4已结束，5未知；
     * connectorStatus int 充电设备装啊提，1空闲,2占用（未充电）,3占用（充电中）,4占用（预约锁定），255故障；
     * power number 设备额定功率，单位：kW；
     * currentA number A相电流， 单位：A；
     * voltageA number A相电压， 单位：V；
     * startTime time 开始充电时间；
     * endTime time 本次采样充电时间；
     * totalPower number 累计充电量，单位：度；
     * totalMoney number 累计总金额，单位：元；
     * soc number 电池剩余电量；
     * stopReason
     */
    private int id, startChargeSeqStat, connectorStatus, stopReason;
    private Number power, currentA, voltageA, totalPower, totalMoney;
    private String startTime, endTime;
    private float soc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartChargeSeqStat() {
        return startChargeSeqStat;
    }

    public void setStartChargeSeqStat(int startChargeSeqStat) {
        this.startChargeSeqStat = startChargeSeqStat;
    }

    public int getConnectorStatus() {
        return connectorStatus;
    }

    public void setConnectorStatus(int connectorStatus) {
        this.connectorStatus = connectorStatus;
    }

    public Number getPower() {
        return power;
    }

    public void setPower(Number power) {
        this.power = power;
    }

    public Number getCurrentA() {
        return currentA;
    }

    public void setCurrentA(Number currentA) {
        this.currentA = currentA;
    }

    public Number getVoltageA() {
        return voltageA;
    }

    public void setVoltageA(Number voltageA) {
        this.voltageA = voltageA;
    }

    public Number getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(Number totalPower) {
        this.totalPower = totalPower;
    }

    public Number getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Number totalMoney) {
        this.totalMoney = totalMoney;
    }

    public float getSoc() {
        return soc;
    }

    public void setSoc(float soc) {
        this.soc = soc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStopReason() {
        return stopReason;
    }

    public void setStopReason(int stopReason) {
        this.stopReason = stopReason;
    }
}
