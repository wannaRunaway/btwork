package com.botann.driverclient.model.chongdian;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/2
 */
public class Chongdianzhuang implements Serializable {
    /**
     * 列表返回模式
     *
     * 对象字段：
     * id int id值；
     * connectorName string 充电桩名称；
     * equipmentType int 设备类型，1直流设备， 2交流设备， 3交直流一体设备， 4无线设备， 5其他；
     * power number 额定功率，单位：kW
     * status int 充电桩状态 状态，0离网，1空闲，2占用（未充电），3占用（充电中），4占用（预约锁定），255故障；
     */
    private int id, equipmentType, power, status;
    private String connectorName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(int equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }
}
