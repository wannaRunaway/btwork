const Promise = require('./es6-promise-min');
const Const = require('./const');
const Data = require('./data');
const Util = require("./util")

let apiList = {
    User:{
        login:['login','username','pwd'],
        getAccountList: ['shareRecord', 'pageNo', 'pageSize', 'startTime', 'endTime'],
        getAccountStatistics: ['shareRecord/statistics','startTime', 'endTime'],
        getCashList:['cashOutRecord','pageNo','pageSize'],
        applyCash :['applyCash','amount'],
        getInformation: ['information'],
        logout:['loginOut']
    }
};

let api = {};

for (let moduleKey in apiList) {
    let moduleApiList = apiList[moduleKey];
    api[moduleKey] = {};
    for (let functionName in moduleApiList) {
        let config = moduleApiList[functionName];
        api[moduleKey][functionName] = (function (config) {
            return function () {
                let action = config[0];
                let data = {};
                if (config.length > 1) {
                    for (let i = 1; i < config.length; i++) {
                        let key = config[i];
                        let value = arguments[i - 1];
                        if (value === undefined) {
                            continue;
                        }
                        data[key] = value;
                    }
                }
                return post(action, data);
            };
        })(config);
    }
}

function transformObjectToUrlencodedData(obj) {
    let p = [];
    for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
            p.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
        }
    }
    return p.join('&');
}

function post(url, params) {
    let token = Data.getToken() || "";
    let host = Const.NET.END_POINT;
    return new Promise((resolve, reject) => {
        let requestData = Object.assign({
            token: token
        }, params);
        console.info(`${host}/${url}?${transformObjectToUrlencodedData(requestData)}`);
        wx.request({
            url: `${host}/${url}`,
            data: requestData,
            method: "POST",
            header: {'content-type': 'application/x-www-form-urlencoded','token':token},
            success: function (res) {
                if (res.data.hasOwnProperty('code')) {
                    if (res.data.code === 200) {
                        resolve(res);
                    }else {
                         resolve(res);
                    }

                }
                reject({
                    code: Const.ERROR.ERROR_NETWORK,
                    response: res
                });
            },
            fail: function (reason) {
                reject({
                    code: Const.ERROR.ERROR_NETWORK,
                    response: reason
                })
            }
        });
    });
}

module.exports = api;
