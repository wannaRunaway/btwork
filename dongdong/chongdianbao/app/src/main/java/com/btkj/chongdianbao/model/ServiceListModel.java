package com.btkj.chongdianbao.model;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/8/28
 */
public class ServiceListModel implements Serializable {

    /**
     * code : 200
     * content : [{"category":"","del":0,"id":12,"mkey":"customer_service_phone","name":"","sort":0,"value":"400-800-128"}]
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

    public static class ContentBean {
        /**
         * category :
         * del : 0
         * id : 12
         * mkey : customer_service_phone
         * name :
         * sort : 0
         * value : 400-800-128
         */

        private String category;
        private int del;
        private int id;
        private String mkey;
        private String name;
        private int sort;
        private String value;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getDel() {
            return del;
        }

        public void setDel(int del) {
            this.del = del;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMkey() {
            return mkey;
        }

        public void setMkey(String mkey) {
            this.mkey = mkey;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
