package com.botann.driverclient.ui.activity.login;

import java.io.Serializable;
import java.util.List;

/**
 * created by xuedi on 2019/12/24
 */
public class CityModel implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * content : [{"id":210200,"name":"大连市","parentId":210000,"shortName":"大连","level":"2","cityCode":"0411","zipCode":"116011","mergerName":"中国,辽宁省,大连市","lng":"121.618622","lat":"38.91459","pinyin":"Dalian"}]
     */

    private int code;
    private String msg;
    private List<ContentBean> content;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public class ContentBean implements Serializable{
        /**
         * id : 210200
         * name : 大连市
         * parentId : 210000
         * shortName : 大连
         * level : 2
         * cityCode : 0411
         * zipCode : 116011
         * mergerName : 中国,辽宁省,大连市
         * lng : 121.618622
         * lat : 38.91459
         * pinyin : Dalian
         */

        private int id;
        private String name;
        private int parentId;
        private String shortName;
        private String level;
        private String cityCode;
        private String zipCode;
        private String mergerName;
        private String lng;
        private String lat;
        private String pinyin;

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

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getMergerName() {
            return mergerName;
        }

        public void setMergerName(String mergerName) {
            this.mergerName = mergerName;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }
    }
}
