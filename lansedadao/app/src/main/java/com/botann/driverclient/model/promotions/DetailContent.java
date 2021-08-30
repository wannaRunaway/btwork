package com.botann.driverclient.model.promotions;

import java.io.Serializable;

/**
 * created by xuedi on 2019/1/21
 */
public class DetailContent implements Serializable {
    /**
     * "remark":"测试"
     */
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
