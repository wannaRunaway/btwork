package com.botann.charing.pad.model.batterylighter;
import java.io.Serializable;
import java.util.List;
/**
 * created by xuedi on 2018/12/4
 */
public class BatteryLighterList implements Serializable {
    private int total;
    private List<BatteryLighter> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BatteryLighter> getData() {
        return data;
    }

    public void setData(List<BatteryLighter> data) {
        this.data = data;
    }
}
