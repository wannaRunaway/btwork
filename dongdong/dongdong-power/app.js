//app.js
const Core = require('./core/core');

App({
    Core: Core,
    onLaunch: function () {
        let phone =  Core.Data.get("login-phone");
        let password =  Core.Data.get("password");
        if(!password){
            Core.Util.goRedirect("../pages/login/login")
            return false;
        }
        let pwd = Core.HexMD5.hexMD5(password);
        Core.Api.User.login(phone, pwd).then(res => {
            let code = res.data.code;
            if(code == 200){
                let data = res.data.content;
                let token = res.header.token;
                Core.Data.setToken(token);
                Core.Data.set("data",data);
                Core.Util.goRedirect("/pages/index/index")
            }else{
                this.showZanTopTips(res.data.msg);
            }
        }).catch(err => {
            console.log(err)
        })
    },
    getInformation(){
        Core.Api.User.getInformation().then(res => {
            let code = res.data.code;
            if(code == 200){
                let data = res.data.content;
                Core.Data.set("data",data);
            }
        }).catch(err => {
            console.log(err)
        })
    },
    globalData: {
        userInfo: null
    }
})