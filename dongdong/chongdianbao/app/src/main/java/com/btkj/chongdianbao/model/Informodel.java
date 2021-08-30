package com.btkj.chongdianbao.model;

import java.io.Serializable;

/**
 * created by xuedi on 2019/9/2
 */
public class Informodel implements Serializable {

    /**
     * code : 200
     * content : {"balance":1110919,"id":41,"name":"薛地","noReaded":4,"phone":"15209603592"}
     * msg : 成功
     * success : true
     */

    private int code;
    private ContentBean content;
    private String msg;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
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

    public class ContentBean implements Serializable{
        /**
         * balance : 1110919
         * id : 41
         * name : 薛地
         * noReaded : 4
         * phone : 15209603592
         *  accountNo
         */

        private double balance;
        private int id;
        private String name;
        private int noReaded;
        private String phone,  accountNo;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
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

        public int getNoReaded() {
            return noReaded;
        }

        public void setNoReaded(int noReaded) {
            this.noReaded = noReaded;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }
    }
}
