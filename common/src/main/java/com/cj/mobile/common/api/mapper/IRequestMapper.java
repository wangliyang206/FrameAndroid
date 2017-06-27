package com.cj.mobile.common.api.mapper;

import com.cj.mobile.common.model.GsonRequest;

/**
 * @Title: IRequestMapper
 * @Package com.cj.mobile.common.api.mapper
 * @Description: 将基本数据转换成请求数据
 * @author: 王力杨
 * @date: 16/5/24 上午10:07
 */
public interface IRequestMapper {
    public <T> GsonRequest<T> transform(T t);
}
