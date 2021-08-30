package com.botann.driverclient.model.customerservice;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2018/11/12
 */
public class BaseCustomerServicebean implements Serializable {
    private int code;
    private String msg;
    private boolean success;
    private List<CustomerServicebean> content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CustomerServicebean> getContent() {
        return content;
    }

    public void setContent(List<CustomerServicebean> content) {
        this.content = content;
    }
}
