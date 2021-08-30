package com.botann.charing.pad.model.packagedetail;

import java.io.Serializable;

/**
 * created by xuedi on 2019/3/25
 */
public class PackageContent implements Serializable {
    //     *         "remark":"测试"
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
