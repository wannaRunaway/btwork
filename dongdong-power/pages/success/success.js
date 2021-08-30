// pages/success/success.js
let App = getApp();
const Core = App.Core;
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

    goBack(){
        let targetUrl = '../index/index?isShow=' + true;
        Core.Util.goRedirect(targetUrl);
    }
})