const Promise = require('./es6-promise-min');

let WeChat = {
    login: () => {
        return new Promise((resolve, reject) => {
            wx.login({ success: resolve, fail: reject });
        });
    },
    getUserInfo: () => {
        return new Promise((resolve, reject) => {
            wx.getUserInfo({ success: resolve, fail: reject });
        });
    },
    setStorage: (key, value) => {
        return new Promise((resolve, reject) => {
            wx.setStorage({ key: key, data: value, success: resolve, fail: reject });
        });
    },
    getStorage: key => {
        return new Promise((resolve, reject) => {
            wx.getStorage({ key: key, success: resolve, fail: reject });
        });
    },
    getLocation: type => {
        return new Promise((resolve, reject) => {
            wx.getLocation({ type: type, success: resolve, fail: reject });
        });
    },
	getSetting: () => {
		return new Promise((resolve, reject) => {
			wx.getSetting({ success: resolve, fail: reject });
		});
	},
	openSetting: () => {
		return new Promise((resolve, reject) => {
			wx.openSetting({ success: resolve, fail: reject });
		});
	},
	authorize: scope => {
		return new Promise((resolve, reject) => {
			wx.authorize({ scope: scope, success: resolve, fail: reject });
		});
	},
	downloadFile: filePath => {
		return new Promise((resolve, reject) => {
			console.log(new Date() + 'download file: ', filePath);
			wx.downloadFile({url: filePath, success: resolve, fail: reject})
		})
	},
	saveVideoToPhotosAlbum: teamFilePath => {
		return new Promise((resolve, reject) => {
			wx.saveVideoToPhotosAlbum({filePath: teamFilePath, success: resolve, fail: reject})
		})
	},
	scanCode: () => {
		return new Promise((resolve, reject) => {
			wx.scanCode({success: resolve, fail: reject})
		})
	},
    call: (num) => {
        return new Promise((resolve, reject) => {
            wx.makePhoneCall({phoneNumber: num, success: resolve, fail: reject})
        })
    },
    getSystemInfo: () => {
        return new Promise((resolve, reject) => {
            wx.getSystemInfo({success: resolve, fail: reject})
        })
    },
	requestPayment: (timeStamp, nonceStr, pag, signType, paySign) => {
		return new Promise((resolve, reject) => {
			wx.requestPayment({
				'timeStamp': timeStamp,
				'nonceStr': nonceStr,
				'package': pag,
				'signType': signType,
				'paySign': paySign,
				"success": resolve,
				fail: reject
			})
		})
	}
};

module.exports = WeChat;
