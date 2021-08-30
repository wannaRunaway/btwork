package com.btkj.millingmachine.model.config;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/5/23
 */
public class CompanyHelpList implements Serializable {
    /**
     *  *         "companyHelpList":[
     *  *             {
     *  *                 "id":1,
     *  *                 "companyId":28,
     *  *                 "sort":0,
     *  *                 "title":"如何使用会员卡购米",
     *  *                 "content":"1、选择购米重量 2、将卡放到感应区 3、扣款成功，等待碾米 4、注意：碾米期间请不要走开",
     *  *                 "delFlag":0,
     *  *                 "optType":1,
     *  *                 "operator":"平台管理员",
     *  *                 "remark":"增加",
     *  *                 "updateTime":[
     *  *                     2019,
     *  *                     5,
     *  *                     21,
     *  *                     13,
     *  *                     57,
     *  *                     29,
     *  *                     274033000
     *  *                 ]
     *  *             },
     *  *         ],
     */
    private int id, companyId, sort, delFlag, optType;
    private String title, content, operator, remark;
    private List<Integer> updateTime;
    private boolean isshow;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getOptType() {
        return optType;
    }

    public void setOptType(int optType) {
        this.optType = optType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Integer> getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(List<Integer> updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isIsshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }
}
