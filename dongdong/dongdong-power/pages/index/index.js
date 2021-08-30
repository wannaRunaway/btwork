// pages/index/index.js
let App = getApp();
const Core = App.Core;
import { timeFormat } from '../../core/util.js'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        isShow: true,
        status: 0,
        information: "",
        accountList: [],
        accountListPage: 1,
        accountListPageSize: 20,
        accountTips: "加载中...",
        isAccountLoading: false,
        isAccountLoad: false,

        cashList: [],
        cashListPage: 1,
        cashListPageSize: 20,
        cashTips: "加载中",
        isCashLoading: false,
        isCashLoad: false,

        searchDate: '',
        searchDateStr: '',
        endDate: '2020-01-01',
        totalAmount: -1,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        if (options.isShow) {
            this.setData({
                isShow: false
            })
        }
        App.getInformation();
        let data = Core.Data.get("data");
        this.setData({
            information: data,
        })
        let accountListPage = this.data.accountListPage;
        let accountListPageSize = this.data.accountListPageSize
        this.getAccountList(accountListPage, accountListPageSize);

        //获取提现记录
        let cashListPage = this.data.accountListPage;
        let cashListPageSize = this.data.accountListPageSize
        this.getCashList(cashListPage, cashListPageSize);

        let endDate = timeFormat(new Date(), null, '-')
        this.setData({
          endDate: endDate
        })
      // console.log('00' + 1)
      // console.log('001'.slice(1))
    },
  bindDateChange({ detail }) {
    let date = detail.value
    let arr = date.split('-')
    this.setData({
      searchDate: detail.value,
      searchDateStr: arr[0] + '年' + parseInt(arr[1]) + '月份'
    })
    this.getAccountList(1, 20)
    let times = this.getStartEndTime()
    Core.Api.User.getAccountStatistics(times[0],times[1]).then(res => {
      let code = res.data.code
      // console.log(res)
      // console.log('[[[[[[[[')
      if (code == 200) {
        // console.log(res.data)
        var data = -1
        if (res.data.content == null) {
          data = -1
        } else {
          data = res.data.content.totalValue
          if (!(data >= 0)) data = -1
        }
        this.setData({
          totalAmount: data
        })
        // console.log(res)
      }
      if (code == 801) {
        Core.Util.goRedirect("../login/login")
        return;
      }
    }).catch(err => {
      console.log(err)
    })
  },
  bindcancel (e) {
    this.setData({
      searchDate: '',
       totalAmount: -1,
    })
    this.getAccountList(1, 20)
  },
    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    onSwitch: function (e) {
        this.setData({
            isShow: !this.data.isShow
        })

    },
//  下拉刷新
    onPullDownRefresh: function () {
      // wx.log('------------')
      // console.log('')
        wx.showNavigationBarLoading() //在标题栏中显示加载
        this.setData({
            accountTips: "",
            accountList: [],
            isAccountLoad: false,
            accountListPage: 1,
            cashTips: "",
            cashList: [],
            isCashLoad: false,
            cashListPage: 1,
        });
        this.getCashList(1, 20);
        this.getAccountList(1, 20);
        Core.Api.User.getInformation().then(res => {
            let code = res.data.code;
            if (code == 200) {
                let data = res.data.content;
                Core.Data.set("data", data);
                this.setData({
                    information: data
                })
            }
            if (code == 801) {
                Core.Util.goRedirect("../login/login")
                return;
            }
        }).catch(err => {
            console.log(err)
        })

    },
    getStartEndTime() {
      var startTime = ''
      var endTime = ''
      if (this.data.searchDate) {
        let arr = this.data.searchDate.split('-')
        let year = parseInt(arr[0])
        let month = parseInt(arr[1])
        // if (month == 12) {
        //   year = year + 1
        //   month = 1
        // }
        startTime = year + ('00' + month).slice(-2) + '01'
        month = month + 1
        if (month == 13) {
          year = year + 1
          month = 1
        }
        endTime = year + ('00' + month).slice(-2) + '01'
        // console.log(('00' + month))
        // console.log(('00' + month).slice(2))
        // console.log(endTime)
      }
      return [startTime, endTime]
    },
    //到账记录列表
    getAccountList(page, pageSize) {
      console.log('----------')
        let times = this.getStartEndTime()
        setTimeout(() => {
          Core.Api.User.getAccountList(page, pageSize, times[0], times[1]).then(res => {
                this.setData({
                    accountTips: "",
                })
                let list = this.data.accountList;
                // console.log(list)
                if (res.data.code == 200) {
                    let length = res.data.content.length;
                    this.setData({
                        isAccountLoading: false
                    })
                    if (page == 1 && length == 0) {
                        this.setData({
                            accountTips: "暂无到账记录",
                            accountList: [],
                        })
                        return false;
                    }
                    if (length !== pageSize) {
                        this.setData({
                            accountTips: "暂无更多到账记录",
                            isAccountLoad: true,
                        })
                    }
                    if (page == 1) list = []
                    let data = res.data.content;
                    data.forEach(item => {
                        item['newCreatetime'] = Core.Util.timestampToTime(item.create_time)
                        list.push(item)
                    })
                    // list.forEach(item => {
                    //     item.newCreatetime = Core.Util.timestampToTime(item.create_time);
                    // })
                    this.setData({
                        accountList: list
                    })
                    wx.hideNavigationBarLoading()
                    wx.stopPullDownRefresh()
                }
                if (res.data.code == 801) {
                    Core.Util.goRedirect("../login/login")
                    wx.hideNavigationBarLoading()
                    wx.stopPullDownRefresh()
                    return;
                }
            }).catch(err => {
                console.log(err);
            })
        }, 300)

    },

    //提现记录列表
    getCashList(page, pageSize) {
        setTimeout(() => {
            Core.Api.User.getCashList(page, pageSize).then(res => {
                this.setData({
                    cashTips: "",
                })
                let list = this.data.cashList;
                if (res.data.code == 200) {
                    let length = res.data.content.length;
                    this.setData({
                        isCashLoading: false
                    })
                    if (page == 1 && length == 0) {
                        this.setData({
                            cashTips: "暂无提现记录",
                        })
                        return false;
                    }
                    if (length !== pageSize) {
                        ;this.setData({
                            cashTips: "暂无更多提现记录",
                            isCashLoad: true,
                        })
                    }

                    let data = res.data.content;
                    data.forEach(item => {
                        list.push(item)
                    })
                    list.forEach(item => {
                        item.newCreatetime = Core.Util.timestampToTime(item.create_time);
                    })
                    this.setData({
                        cashList: list
                    })
                    wx.hideNavigationBarLoading()
                    wx.stopPullDownRefresh()
                }
                if (res.data.code == 801) {
                    Core.Util.goRedirect("../login/login")
                    wx.hideNavigationBarLoading()
                    wx.stopPullDownRefresh()
                    return;
                }
            }).catch(err => {
                console.log(err);
            })
        }, 300)

    },
    //到账记录滑动到底部触发的函数
    slideAccountList() {

        this.setData({
            accountTips: "加载中...",

        })

        setTimeout(() => {
            if (this.data.isAccountLoad) {
                this.setData({
                    accountTips: "暂无更多到账记录"
                })
                return false;
            }
            if (this.data.isAccountLoading) {
                return;
            }
            this.setData({
                accountListPage: this.data.accountListPage + 1,
                isAccountLoading: true,
            })
            let accountListPageSize = this.data.accountListPageSize;
            let accountListPage = this.data.accountListPage;
            this.getAccountList(accountListPage, accountListPageSize)
        }, 500)
    },
    slideCashList() {
        this.setData({
            cashTips: "加载中..."
        })
        setTimeout(() => {
                if (this.data.isCashLoad) {
                    this.setData({
                        cashTips: "暂无更多提现记录"
                    })
                    return false;

                }
                if(this.data.isCashLoading){
                    return;
                }
                this.setData({
                    cashListPage: this.data.cashListPage + 1,
                    isCashLoading: true,
                })
                let cashListPageSize = this.data.cashListPageSize;
                let cashListPage = this.data.cashListPage;
                this.getCashList(cashListPage, cashListPageSize)

            }
            , 500)
    },
    //跳转到提现页面
    goToCash() {

        Core.Util.go("../cash/cash")
    },
    //退出登录
    logout(){
        Core.Api.User.logout().then(res => {
            let code = res.data.code;
            if(code == 200){
                Core.Data.clear();
                Core.Util.goRedirect("../login/login")
            }
            if (code == 801) {
                Core.Util.goRedirect("../login/login")
                return;
            }
        }).catch(err => {
            console.log(err)
        })
    }
})