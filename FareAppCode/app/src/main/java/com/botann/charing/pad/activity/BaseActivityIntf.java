package com.botann.charing.pad.activity;

/**
 * Created by liushanguo on 2017/5/12.
 */

public interface BaseActivityIntf {

    /**
     * 视图数据源 dataSource 控制一些加载顺序，activity不用再关心顺序
     * @return
     */
    int  viewLayout();

    /**
     * 生命周期代理方法 delegate
     */
    void initView();



}

