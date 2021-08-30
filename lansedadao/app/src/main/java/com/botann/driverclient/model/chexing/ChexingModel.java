package com.botann.driverclient.model.chexing;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/7/1
 */
public class ChexingModel implements Serializable {
    /**
     * 	"code": 0,
     * 	"msg": "成功",
     * 	"content":
     * 	"success": true
     */
    private int code;
    private String msg;
    private boolean success;
    private List<ChexingContent> content;

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

    public List<ChexingContent> getContent() {
        return content;
    }

    public void setContent(List<ChexingContent> content) {
        this.content = content;
    }
}
