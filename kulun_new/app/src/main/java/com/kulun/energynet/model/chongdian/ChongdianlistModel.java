package com.kulun.energynet.model.chongdian;

import com.kulun.energynet.model.huandian.StationContent;

import java.io.Serializable;

/**
 * created by xuedi on 2019/6/27
 */
public class ChongdianlistModel implements Serializable {
    private String msg;
    private boolean success;
    private int code;
    private ChongdianContent content;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ChongdianContent getContent() {
        return content;
    }

    public void setContent(ChongdianContent content) {
        this.content = content;
    }
}
