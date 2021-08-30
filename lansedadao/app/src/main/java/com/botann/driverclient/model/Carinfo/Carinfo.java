package com.botann.driverclient.model.Carinfo;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/2/16
 */
public class Carinfo implements Serializable {
    /**
     * {
     *     "code":0,
     *     "msg":"成功",
     *     "content":[
     *
     *     ],
     *     "success":true
     * }
     */
    private int code;
    private String msg;
    private boolean success;
    private List<CarContent> content;

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

    public List<CarContent> getContent() {
        return content;
    }

    public void setContent(List<CarContent> content) {
        this.content = content;
    }
}
