const Const = require('./const');

let Util = {
    go: go,
    goRedirect: goRedirect,
	reLaunchGo: reLaunchGo,
	switchTab: switchTab,
    goBack: goBack,
    getImgUrl: getImgUrl,
    getFileUrl: getFileUrl,
    showSuccessToast: showSuccessToast,
    inArray: inArray,
    time: time,
    timeFormat: timeFormat,
    timeFormatForYMD: timeFormatForYMD,
    numberFormat: numberFormat,
    union: union,
    intersection: intersection,
    difference: difference,
    isEmail: isEmail,
    isPhone: isPhone,
    timestampToTime:timestampToTime
};

function go(url) {
    wx.navigateTo({ url: url });
}

function goRedirect(url) {
    wx.redirectTo({ url: url });
}

function reLaunchGo(url) {
	wx.reLaunch({url: url})
}

function switchTab(url) {
	wx.switchTab({url: url})
}

function goBack(delta = 1) {
	wx.navigateBack(delta);
}

function getImgUrl(imgName) {
    if (imgName.indexOf('://') !== -1) {
        return imgName;
    }

    return Const.NET.IMG_URL_PREFIX + imgName;
}

function getFileUrl(fileName) {
    return Const.NET.FILE_URL_PREFIX + fileName;
}

function showSuccessToast(title, time, url, isGoRedirect = false) {
    wx.showToast({
        title: title,
        icon: 'success',
        duration: time,
        success: function () {
            setTimeout(function () {
                if (isGoRedirect) {
                    goRedirect(url);
                } else {
                    go(url);
                }
            }, time)
        }
    });
}

function inArray(needle, haystack, argStrict) {

    let key = '',
        strict = !!argStrict;

    if (strict) {
        for (key in haystack) {
            if (haystack[key] === needle) {
                return true;
            }
        }
    } else {
        for (key in haystack) {
            if (haystack[key] === needle) {
                return true;
            }
        }
    }

    return false;
}

function time() {
    return parseInt(new Date().getTime() / 1000, 10);
}

function timeFormat(date, detail, seperator) {
    if (!seperator) seperator = '/'
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();

    if (detail) {
        let hour = date.getHours();
        let minute = date.getMinutes();
        let second = date.getSeconds();

      return [year, month, day].map(numberFormat).join(seperator) + ' ' + [hour, minute, second].map(numberFormat).join(':');
    }

  return [year, month, day].map(numberFormat).join(seperator);
}
function timestampToTime (date) {
    var date = new Date(date) //时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-'
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-'
    var D = date.getDate() + ' '
    var h = date.getHours() + ':'
    var m = date.getMinutes() + ':'
    var s = date.getSeconds()
    return Y+M+D+h+m+s

}
function timeFormatForYMD(date, separator, isCN = false) {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();

    if (isCN) {
        return year + '年 ' + month + '月' + day + '日';
    }
    return [year, month, day].map(numberFormat).join(separator);
}

function numberFormat(n) {
    n = n.toString();
    return n[1] ? n : '0' + n;
}

function union(arr1, arr2) {
    let set1 = new Set(arr1);
    let set2 = new Set(arr2);

    let unionSet = new Set([...set1, ...set2]);
    return [...unionSet];
}

function intersection(arr1, arr2) {
    let set1 = new Set(arr1);
    let set2 = new Set(arr2);

    let intersectionSet = new Set([...set1].filter(x => set2.has(x)));
    return [...intersectionSet];
}

function difference(arr1, arr2) {
    let set1 = new Set(arr1);
    let set2 = new Set(arr2);

    let differenceSet = new Set([...set1].filter(x => !set2.has(x)));
    return [...differenceSet];
}

function isEmail(email) {
    let reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(email);
}

function isPhone(phone) {
    let phoneReg = /^1[3|4|5|7|8][0-9]{9}$/;
    let telReg = /^0[\d]{2,3}-[\d]{7,8}$/;
    return phoneReg.test(phone) || telReg.test(phone);
}

module.exports = Util;
