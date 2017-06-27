package com.cj.mobile.common.base;

/**
 * 包名:com.cj.mobile.common.base
 * 对象名: IView
 * 描述:基础的View接口
 * 作者: 王力杨
 * 创建日期: 2016/5/26 14:18
 */
public interface IView<T extends IPresenter> {
    void setPresenter(T presenter);
}
