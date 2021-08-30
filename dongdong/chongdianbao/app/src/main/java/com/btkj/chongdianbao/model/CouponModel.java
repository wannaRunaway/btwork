package com.btkj.chongdianbao.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/9/2
 */
public class CouponModel implements Serializable {

    /**
     * code : 200
     * content : [{"accountId":161,"amount":1000,"beginDate":1567267200000,"come":0,"createTime":1567420463000,"del":false,"expireDate":1572451200000,"id":43,"name":"fifa 20","remark":"test操作人：admin","status":1,"type":1}]
     * msg : 成功
     * success : true
     */

    private int code;
    private String msg;
    private boolean success;
    private List<ContentBean> content;

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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public class ContentBean implements Serializable{
        /**
         * accountId : 161
         * amount : 1000
         * beginDate : 1567267200000
         * come : 0
         * createTime : 1567420463000
         * del : false
         * expireDate : 1572451200000
         * id : 43
         * name : fifa 20
         * remark : test操作人：admin
         * status : 1
         * type : 1
         */

        private int accountId;
        private int amount;
        private long beginDate;
        private int come;
        private long createTime;
        private boolean del;
        private long expireDate;
        private int id;
        private String name;
        private String remark;
        private int status;
        private int type;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public long getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(long beginDate) {
            this.beginDate = beginDate;
        }

        public int getCome() {
            return come;
        }

        public void setCome(int come) {
            this.come = come;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public long getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(long expireDate) {
            this.expireDate = expireDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
