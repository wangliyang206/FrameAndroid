package com.cj.mobile.common.domain.usecase;

/**
 * 包名:com.chinamall21.mobile.common.domain.usecase
 * 对象名: ErrorBundle
 * 描述:错误处理接口
 * 作者: 赵志军
 * 邮箱：zhijun.zhao@21chinamall.com
 * 创建日期: 2016/5/31 17:26
 */
public interface IErrorBundle {
    public <T> void praseError(Throwable throwable, UseCase.UseCaseCallback<T> useCase);
}
