package com.kulun.energynet.model;
import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/2/18
 */
public class RecordContent implements Serializable {
    /**
     * "total":0,
     *      *         "data":[
     *      *             {
     *      *
     *      *
     *      *         ]
     */
    private int total;
    private List<RecordData> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RecordData> getData() {
        return data;
    }

    public void setData(List<RecordData> data) {
        this.data = data;
    }
}
