// pages/login/login.js
let Component = require('../../assets/style/index');

let App = getApp();
const Core = App.Core;

Page(Object.assign({}, Component.TopTips, {
    data: {
        phone: "",
        password: "",
    },

    onLoad: function () {

    },

    onShow: function () {
        let phone = Core.Data.get("login-phone");
        this.setData({phone: phone})
    },
    bindKeyInput: function (e) {
        let $this = this;
        let type = e.currentTarget.dataset.type;
        let value = e.detail.value;
        switch (type) {
            case "phone":
                $this.setData({phone: value});
                Core.Data.set("login-phone", value)
                break;
            case "password":
                $this.setData({password: value});
                Core.Data.set("password", value)
                break;
            default:
                break;
        }

    },
    login() {
        let phone = this.data.phone;
        let pwd = Core.HexMD5.hexMD5(this.data.password);
        if (!phone) {
            this.showZanTopTips('请输入联系电话');
            return false;
        }
        if (!App.Core.Util.isPhone(phone)) {
            this.showZanTopTips('联系电话格式错误');
            return false;
        }
        if (!pwd) {
            this.showZanTopTips('请输入密码');
            return false;
        }
        Core.Api.User.login(phone, pwd).then(res => {
            console.log(res);
            let code = res.data.code;
            if(code == 200){
                let data = res.data.content;
                let token = res.header.token;
                Core.Data.setToken(token);
                Core.Data.set("data",data);
                Core.Util.goRedirect("../index/index")
            }else{
                this.showZanTopTips(res.data.msg);
            }
        }).catch(err => {
            console.log(err)
        })
    }

}));