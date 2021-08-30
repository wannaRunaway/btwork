package com.btkj.millingmachine.model.uploadconfig;

import java.io.Serializable;

/**
 * created by xuedi on 2019/5/23
 */
public class UploadConfig implements Serializable {
    private int deviceId, riceOutTime1, riceOutTime2, riceOutTime3, riceOutTime4, riceOutTime5, riceOutOffset1, riceOutOffset2, riceAddCount;
    private String overloadProcted, startDiff, ip, readCardConsole, readCardBandrate, sensorConsole, sensorBandrate, phyCosole, phyBandrate,
            riceProduceRate,maintainPhone,riceOutOffset3, riceOutOffset4, riceOutOffset5;
    private float speed1, speed2, speed3;
    /**
     * deviceId  [int]	是	设备配置
     * overloadProcted[string]	是	过载保护
     * startDiff[string]	是	启动时差
     * ip[string]	是	ip地址
     * readCardConsole[string]	是	读卡串口
     * readCardBandrate[string]	是	读卡串口-波特率
     * sensorConsole[string]	是	计量串口
     * sensorBandrate[string]	是	计量串口-波特率
     * phyCosole[string]	是	物理串口
     * phyBandrate[string]	是	物理串口-波特率
     * riceProduceRate[string]	是	出米率
     * maintainPhone[string]	是	维护电话
     * speed1[float]	是	下谷速度1
     * speed2[float]	是	下谷速度2
     * speed3[float]	是	下谷速度3
     * riceOutTime1[int]	是	碾米预设时间1，秒
     * riceOutTime2[int]	是	碾米预设时间2，秒
     * riceOutTime3[int]	是	碾米预设时间3，秒
     * riceOutTime4[int]	是	碾米预设时间4，秒
     * riceOutTime5[int]	是	碾米预设时间5，秒
     * riceOutOffset1[int]	是	补偿重量,克
     * riceOutOffset2[int]	是	补偿重量,克
     * riceOutOffset3[string]	是	补偿重量,克
     * riceOutOffset4[string]	是	补偿重量,克
     * riceOutOffset5[string]	是	补偿重量,克
     * riceAddCount[int]	是	本次加入谷量，克
     */
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getRiceOutTime1() {
        return riceOutTime1;
    }

    public void setRiceOutTime1(int riceOutTime1) {
        this.riceOutTime1 = riceOutTime1;
    }

    public int getRiceOutTime2() {
        return riceOutTime2;
    }

    public void setRiceOutTime2(int riceOutTime2) {
        this.riceOutTime2 = riceOutTime2;
    }

    public int getRiceOutTime3() {
        return riceOutTime3;
    }

    public void setRiceOutTime3(int riceOutTime3) {
        this.riceOutTime3 = riceOutTime3;
    }

    public int getRiceOutTime4() {
        return riceOutTime4;
    }

    public void setRiceOutTime4(int riceOutTime4) {
        this.riceOutTime4 = riceOutTime4;
    }

    public int getRiceOutTime5() {
        return riceOutTime5;
    }

    public void setRiceOutTime5(int riceOutTime5) {
        this.riceOutTime5 = riceOutTime5;
    }

    public int getRiceOutOffset1() {
        return riceOutOffset1;
    }

    public void setRiceOutOffset1(int riceOutOffset1) {
        this.riceOutOffset1 = riceOutOffset1;
    }

    public int getRiceOutOffset2() {
        return riceOutOffset2;
    }

    public void setRiceOutOffset2(int riceOutOffset2) {
        this.riceOutOffset2 = riceOutOffset2;
    }

    public int getRiceAddCount() {
        return riceAddCount;
    }

    public void setRiceAddCount(int riceAddCount) {
        this.riceAddCount = riceAddCount;
    }

    public String getOverloadProcted() {
        return overloadProcted;
    }

    public void setOverloadProcted(String overloadProcted) {
        this.overloadProcted = overloadProcted;
    }

    public String getStartDiff() {
        return startDiff;
    }

    public void setStartDiff(String startDiff) {
        this.startDiff = startDiff;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReadCardConsole() {
        return readCardConsole;
    }

    public void setReadCardConsole(String readCardConsole) {
        this.readCardConsole = readCardConsole;
    }

    public String getReadCardBandrate() {
        return readCardBandrate;
    }

    public void setReadCardBandrate(String readCardBandrate) {
        this.readCardBandrate = readCardBandrate;
    }

    public String getSensorConsole() {
        return sensorConsole;
    }

    public void setSensorConsole(String sensorConsole) {
        this.sensorConsole = sensorConsole;
    }

    public String getSensorBandrate() {
        return sensorBandrate;
    }

    public void setSensorBandrate(String sensorBandrate) {
        this.sensorBandrate = sensorBandrate;
    }

    public String getPhyCosole() {
        return phyCosole;
    }

    public void setPhyCosole(String phyCosole) {
        this.phyCosole = phyCosole;
    }

    public String getPhyBandrate() {
        return phyBandrate;
    }

    public void setPhyBandrate(String phyBandrate) {
        this.phyBandrate = phyBandrate;
    }

    public String getRiceProduceRate() {
        return riceProduceRate;
    }

    public void setRiceProduceRate(String riceProduceRate) {
        this.riceProduceRate = riceProduceRate;
    }

    public String getMaintainPhone() {
        return maintainPhone;
    }

    public void setMaintainPhone(String maintainPhone) {
        this.maintainPhone = maintainPhone;
    }

    public String getRiceOutOffset3() {
        return riceOutOffset3;
    }

    public void setRiceOutOffset3(String riceOutOffset3) {
        this.riceOutOffset3 = riceOutOffset3;
    }

    public String getRiceOutOffset4() {
        return riceOutOffset4;
    }

    public void setRiceOutOffset4(String riceOutOffset4) {
        this.riceOutOffset4 = riceOutOffset4;
    }

    public String getRiceOutOffset5() {
        return riceOutOffset5;
    }

    public void setRiceOutOffset5(String riceOutOffset5) {
        this.riceOutOffset5 = riceOutOffset5;
    }

    public float getSpeed1() {
        return speed1;
    }

    public void setSpeed1(float speed1) {
        this.speed1 = speed1;
    }

    public float getSpeed2() {
        return speed2;
    }

    public void setSpeed2(float speed2) {
        this.speed2 = speed2;
    }

    public float getSpeed3() {
        return speed3;
    }

    public void setSpeed3(float speed3) {
        this.speed3 = speed3;
    }
}
