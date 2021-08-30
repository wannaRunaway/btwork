package com.kulun.energynet.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/2/18
 */
public class PackageRecordList implements Serializable {
    /**
     * {
     *     "code":0,
     *     "msg":"成功",
     *     "content":{
     *
     *     },
     *     "success":true
     * }
     */
    private int code;
    private String msg;
    private boolean success;
    private RecordContent content;

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

    public RecordContent getContent() {
        return content;
    }

    public void setContent(RecordContent content) {
        this.content = content;
    }
}
