package com.botann.driverclient.model;

import java.io.Serializable;

/**
 * Created by Orion on 2017/8/4.
 */
public class MessageInfo implements Serializable {
    private Integer id;
    private Long createTime;
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
