package com.botann.charing.pad.model.repairlist;
import java.io.Serializable;
import java.util.List;
/**
 * created by xuedi on 2018/11/5
 */
public class RepairList implements Serializable {
    private int total;
    private List<RepairData> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RepairData> getData() {
        return data;
    }

    public void setData(List<RepairData> data) {
        this.data = data;
    }
    /**
     * "content": {
     * 		"total": 1,
     * 		"data": [{
     *                }]    * 	},
     */
}
