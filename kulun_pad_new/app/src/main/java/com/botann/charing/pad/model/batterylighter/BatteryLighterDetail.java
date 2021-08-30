package com.botann.charing.pad.model.batterylighter;

import java.io.Serializable;

/**
 * created by xuedi on 2018/12/3
 */
public class BatteryLighterDetail implements Serializable {
    /**
     * 电池类型  电池数量  电池电量
     */
    private String type, elect, num;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getElect() {
        return elect;
    }

    public void setElect(String elect) {
        this.elect = elect;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
