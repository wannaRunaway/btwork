package com.kulun.energynet.model;
import java.io.Serializable;
import java.util.List;
/**
 * created by xuedi on 2019/1/22
 */
public class PromotionContent implements Serializable {
    private int total;
    private List<Promotions> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Promotions> getData() {
        return data;
    }

    public void setData(List<Promotions> data) {
        this.data = data;
    }
    /**
     * "total": 0,
     *      "data": [{
     *
     *      }]
     */
}
