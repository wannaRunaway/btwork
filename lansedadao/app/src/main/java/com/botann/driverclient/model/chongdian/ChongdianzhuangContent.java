package com.botann.driverclient.model.chongdian;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/7/2
 */
public class ChongdianzhuangContent implements Serializable {
    private int total;
    private List<Chongdianzhuang> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Chongdianzhuang> getData() {
        return data;
    }

    public void setData(List<Chongdianzhuang> data) {
        this.data = data;
    }
}
