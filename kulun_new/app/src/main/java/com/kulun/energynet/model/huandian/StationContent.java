package com.kulun.energynet.model.huandian;
import java.io.Serializable;
import java.util.List;
/**
 * created by xuedi on 2019/4/24
 */
public class StationContent implements Serializable {
    private int total;
    private List<StationInfo> data;
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public List<StationInfo> getData() {
        return data;
    }
    public void setData(List<StationInfo> data) {
        this.data = data;
    }
}
