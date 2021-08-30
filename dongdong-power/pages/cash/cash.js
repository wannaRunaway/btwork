// pages/cash/cash.js
let Component = require('../../assets/style/index');
let App = getApp();
const Core = App.Core;
Page(Object.assign({}, Component.TopTips, {

    /**
     * 页面的初始数据
     */
    data: {
        userName: "",
        bankName: "",
        bankNo: "",
        amount: "",
        // surplusAmount: "",
        balance:""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        App.getInformation();
        let info = Core.Data.get("data");
        let balance = info.balance;
        let userName = info.bankUserName;
        let bankNme = info.bankName;
        let bankNo = info.bankNo.substring(info.bankNo.length - 4);
        // let applyCrashOutAmount = parseInt(info.applyCrashOutAmount);
        // let surplusAmount = 10000 - applyCrashOutAmount
        this.setData({
            userName: userName,
            bankName: bankNme,
            bankNo: bankNo,
            balance:balance
            // surplusAmount: surplusAmount
        })

    },


    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },
    bindKeyInput(e) {
        let $this = this;
        let value = e.detail.value;
        this.setData({
            amount: value,
        })
    },
    cash() {
        let amount = this.data.amount;
        if (!amount) {
            this.showZanTopTips('请输入提现金额');
            return false;
        }
        if (amount <= 0){
            this.showZanTopTips('提现金额必须大于0');
            return false;
        }
        if(amount >this.data.balance){
            this.showZanTopTips('账户余额不足');
            return false;
        }
        if (amount > 10000){
            this.showZanTopTips('今日最多可提现10000元');
            return false;
        }

        Core.Api.User.applyCash(amount).then(res => {

            let code = res.data.code;
            if (code == 801){
                Util.goRedirect("../login/login")
                this.showZanTopTips(res.data.msg);
                return;
            }
            if (code == 200) {
                this.setData({
                    amount: ""
                })
                Core.Util.reLaunchGo("../success/success")
            } else {
                this.setData({
                    amount: ""
                })
                this.showZanTopTips(res.data.msg);
            }
        }).catch(err => {
            console.log(err)
        })
    }

}))