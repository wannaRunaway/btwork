package com.botann.driverclient.model.promotions;

import com.botann.driverclient.ui.activity.promotions.PromotionContent;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/1/17
 */
public class PromotionsList implements Serializable {
    private int code;
    private String msg;
    private boolean success;
    private PromotionContent content;

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

    public PromotionContent getContent() {
        return content;
    }

    public void setContent(PromotionContent content) {
        this.content = content;
    }

    /**
     "code": 0,
     "msg": "成功",
     "content": {
     },
     "success": true
     */
}
