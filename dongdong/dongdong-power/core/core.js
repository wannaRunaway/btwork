let Api = require('./api');
let Const = require('./const');
let Data = require('./data');
let WeChat = require('./wechat');
let Event = require('./event');
let Util = require('./util');
let HexMD5 = require('./md5')

let Core = {
    Api: Api,
    Const: Const,
    Data: Data,
    WeChat: WeChat,
    Event: Event,
    Util: Util,
    HexMD5:HexMD5
};

module.exports = Core;
