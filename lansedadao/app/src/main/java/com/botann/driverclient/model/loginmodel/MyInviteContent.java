package com.botann.driverclient.model.loginmodel;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/4/19
 */
public class MyInviteContent implements Serializable {
    /**
     * {
     *      *         "total":0,
     *      *         "data":[
     *      *             {
     *      *                 "addParentTime":1555641674000,
     *      *                 "phone":"18329111248",
     *      *                 "name":"157所属司机",
     *      *                 "id":10597,
     *      *                 "account":"2019041900010597"
     *      *             },
     *      *
     *      *         ]
     *      *     },
     */
    private int total;
    private List<MyInviteData> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MyInviteData> getData() {
        return data;
    }

    public void setData(List<MyInviteData> data) {
        this.data = data;
    }
}
