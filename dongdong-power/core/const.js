let Const = {
    ERROR: {
        ERROR_NETWORK: -10000,
        ERROR_INVALID: -1,
        ERROR_PARAM_NOT_SET: 1,
        ERROR_TOKEN_INVALID: 2,
        ERROR_LOGIN_FAIL: 3,
        ERROR_WRONG_PARAM: 4,
        ERROR_NOT_EXIST: 5,
        ERROR_EXIST: 6,
        ERROR_ORG_NOT_EXIST: 7,
        ERROR_ORG_MEMBER_NOT_EXISTS: 8,
        ERROR_REGISTER: 9,
        ERROR_USER_NOT_EXISTS: 10,
        ERROR_PHONE_HAS_BEEN_TAKEN: 11,
        ERROR_BIND_USER_BIND_EXISTS: 12,
        ERROR_WRONG_TYPE: 13,
        ERROR_SAVE_ERROR: 14,
        ERROR_ACTION_NOT_ALLOWED: 15,
        ERROR_WRONG_VERIFICATION_CODE: 16,
        ERROR_SEND_PHONE_VCODE_TOO_OFTEN: 17
    },

    NET: {
        // 测试
        // END_POINT: 'http://121.196.237.125:9090/powerExchange/wechatApplet',
        // END_POINT: 'https://mapi.zjhzxy.cn/client/1',
        // 正式
        // END_POINT: 'https://dongruime.cn/app/wechatApplet',
        END_POINT: 'https://ttgg.botann.com/powerExchange/wechatApplet',
        // IMG_UPLOAD_END_POINT: 'https://sw-base-api.innotick.com/static/file-upload',
        // IMG_DOWNLOAD_END_POINT: 'https://static.innotick.com/smartwork/file',
        // IMG_URL_PREFIX: 'http://enjoy-oss.oss-cn-hangzhou.aliyuncs.com/img/',
        // FILE_URL_PREFIX: 'https://static.innotick.com/smartwork/file/'
    },

    DATA: {
        KEY_PREFIX: 'dongdong-power.wx.data.',
        KEY_TOKEN: 'token',
        KEY_USER: 'user',
        KEY_WX_USER: 'wx-user',
        KEY_USER_TYPE: 'user-type',
        KEY_DEFAULT_AVATAR: '../../images/index/default-avatar@2x.png',
        MAP_KEY: 'HPNBZ-B426V-CZQPP-UN4R6-QYOF2-MYFU3'
    },
}
module.exports = Const;
