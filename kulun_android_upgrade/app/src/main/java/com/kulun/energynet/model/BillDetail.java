package com.kulun.energynet.model;

import java.io.Serializable;

public class BillDetail implements Serializable {
    //{"code":0,"data":{"amount":"0.01","detail":[{"name":"账单类型","value":"活动"},{"name":"活动名称","value":"无锡ER30包月套餐2500元包10000公里"},{"name":"订单号","value":"OD4706980719497216"},{"name":"消费时间","value":"2020年12月30日 10:25:50"}]}}
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
