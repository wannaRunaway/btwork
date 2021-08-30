package com.btkj.chongdianbao.model;

import java.io.Serializable;

public class OssTokenModel {

    /**
     * StatusCode : 200
     * AccessKeyId : STS.NPpt8ytyPWASv8chn2ypRwyPZ
     * AccessKeySecret : 4Q7fJzi2wHy4txBUqNCCg5f4o5Nud5RRPDjrRX8q9Zoa
     * SecurityToken : CAISmAJ1q6Ft5B2yfSjIr5PFP4LNmaZx4IO4dB7SjG5ndf9+mLz7uDz2IHxKfXBrBu0WsPowmWBT7vYSlqdrTIBIX3vJfcBw55JY/FsyyUFDDYnng4YfgbiJREKhaXeirvKwDsz9SNTCAIDPD3nPii50x5bjaDymRCbLGJaViJlhHLt1Mw6jdmgEfpQ0QDFvs8gHL3DcGO+wOxrx+ArqAVFvpxB3hBEYi8G2ydbO7QHF3h+oiL1Xhfyoe8b8N5c0Z8omCI7qgrUtTMebjn4MsSot3bxtkalJ9Q3AutygGFRL632ESbGIrIQ2d1AkNvZkRPMU96CsxecLs+jShpnxzA1WIeZWXiLQSYat2sLYH/mzMdI0ZTMf8dU8WD/lGoABKw1sVkK80VkU5fTii05TcuBC5L4M4U3JS7XJaN0jDHXN+jsbMp2tUbeAhd67RwFQYdiGqB7oxj3y3lbM7Avm77Nix8Q+mU+IqhGNIYHanI6FQrea3NFKRFKQhYepEzf+SUFKb5UVk2piJPt0ykwlqyCw0ckQqq2RzIYWtrls8BA=
     * Expiration : 2019-10-21T07:47:19Z
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
        private String AccessKeyId;
        private String AccessKeySecret;
        private String SecurityToken;
        private String Expiration;

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            AccessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            AccessKeySecret = accessKeySecret;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String securityToken) {
            SecurityToken = securityToken;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String expiration) {
            Expiration = expiration;
        }
    }
}
