package com.kulun.energynet.model.promotions;

import java.io.Serializable;

/**
 * created by xuedi on 2019/1/21
 */
public class PromotionDetail implements Serializable {
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
    private DetailContent content;

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

    public DetailContent getContent() {
        return content;
    }

    public void setContent(DetailContent content) {
        this.content = content;
    }
}
