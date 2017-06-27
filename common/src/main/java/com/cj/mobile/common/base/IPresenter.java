package com.cj.mobile.common.base;

/**
 * 包名:com.cj.mobile.common.base
 * 对象名: IPresenter
 * 描述:基础的Presenter接口
 * 作者: 王力杨
 * 创建日期: 2016/8/17 14:19
 */
public interface IPresenter {

    /**
     * 初始化数据
     */
    void onStart();

    /**
     * 销毁
     */
    void onDestroy();
}
