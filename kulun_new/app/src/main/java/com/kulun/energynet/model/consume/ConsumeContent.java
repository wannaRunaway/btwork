package com.kulun.energynet.model.consume;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/7/4
 */
public class ConsumeContent implements Serializable {
    private int total;
    private List<ConsumeInfo> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ConsumeInfo> getData() {
        return data;
    }

    public void setData(List<ConsumeInfo> data) {
        this.data = data;
    }
}
