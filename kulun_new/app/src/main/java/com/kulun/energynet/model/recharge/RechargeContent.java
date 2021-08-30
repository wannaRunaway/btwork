package com.kulun.energynet.model.recharge;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/7/4
 */
public class RechargeContent implements Serializable {
    private int total;
    private List<RechargeInfo> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RechargeInfo> getData() {
        return data;
    }

    public void setData(List<RechargeInfo> data) {
        this.data = data;
    }
}
