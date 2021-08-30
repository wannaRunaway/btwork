const Const = require('./const');
const Util = require('./util');

let Data = {
    isGuest: isGuest,
    getToken: getToken,
    setToken: setToken,
    getUser: getUser,
    setUser: setUser,
    getUserType: getUserType,
    setUserType: setUserType,
	getWxUser: getWxUser,
	setWxUser: setWxUser,
    getAvatar: getAvatar,
    get: get,
    set: set,
    remove: remove,
    clear: clear,
    getName: getName,
    clearAuthData: clearAuthData
};

function isGuest() {
    let token = getToken();
    return !token;
}

function getKey(key) {
    return Const.DATA.KEY_PREFIX + key;
}

function get(key) {
    key = getKey(key);
    return wx.getStorageSync(key)
}

function set(key, val) {
    key = getKey(key);
    wx.setStorageSync(key, val)
}

function remove(key) {
    key = getKey(key);
    wx.removeStorageSync(key);
}

function clear() {
    wx.clearStorageSync();
}

function getToken() {
    let key = Const.DATA.KEY_TOKEN;
    return get(key);
}

function setToken(token) {
    let key = Const.DATA.KEY_TOKEN;
    return set(key, token);
}

function getUser() {
    let key = Const.DATA.KEY_USER;
    return get(key);
}

function setUser(user) {
    let key = Const.DATA.KEY_USER;
    return set(key, user);
}

function getUserType() {
    let key = Const.DATA.KEY_USER_TYPE;
    return get(key);
}

function setUserType(useType) {
    let key = Const.DATA.KEY_USER_TYPE;
    return set(key, useType);
}

function getWxUser() {
	let key = Const.DATA.KEY_WX_USER;
	return get(key);
}

function setWxUser(wxUser) {
	let key = Const.DATA.KEY_WX_USER;
	return set(key, wxUser);
}

function getName(name) {
    return name ? name : '未命名';
}

function getAvatar(img) {
    if (img) {
        return img === Const.DATA.KEY_DEFAULT_AVATAR ? Const.DATA.KEY_DEFAULT_AVATAR: Util.getImgUrl(img);
    } else {
        return Const.DATA.KEY_DEFAULT_AVATAR;
    }
}

function clearAuthData() {
    setToken('');
    setUser('')
}

module.exports = Data;
