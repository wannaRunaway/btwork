package com.botann.driverclient.model;

/**
 * Created by Administrator on 2018/1/30.
 */

public class CommentInfo {

    private Integer id;

    private Integer accountId;

    private Integer exchangeRecordId;

    private Integer siteid;

    private Integer starsNumber;

    private String content;

    private String tagSerial;

    private String tagContent;

    private Long createTime;

    private Integer del;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getExchangeRecordId() {
        return exchangeRecordId;
    }

    public void setExchangeRecordId(Integer exchangeRecordId) {
        this.exchangeRecordId = exchangeRecordId;
    }

    public Integer getSiteid() {
        return siteid;
    }

    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    public Integer getStarsNumber() {
        return starsNumber;
    }

    public void setStarsNumber(Integer starsNumber) {
        this.starsNumber = starsNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTagSerial() {
        return tagSerial;
    }

    public void setTagSerial(String tagSerial) {
        this.tagSerial = tagSerial;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }
}
