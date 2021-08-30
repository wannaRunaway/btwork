package com.kulun.energynet.model.chongdian;

import com.kulun.energynet.model.huandian.StationInfo;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/6/27
 */
public class ChongdianContent implements Serializable {
    private int total;
    private List<ChongdianInfo> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ChongdianInfo> getData() {
        return data;
    }

    public void setData(List<ChongdianInfo> data) {
        this.data = data;
    }
}
