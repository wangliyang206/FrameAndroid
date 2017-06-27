package com.cj.mobile.common.domain.executor;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 包名:com.chinamall21.mobile.common.domain.executor
 * 对象名: UseCaseScheduler
 * 描述:
 * 作者: 赵志军
 * 邮箱：zhijun.zhao@21chinamall.com
 * 创建日期: 2016/5/26 11:44
 */
public class UseCaseScheduler {
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;


    protected UseCaseScheduler(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }


    public <T> Observable.Transformer<T,T> applyScheduler(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler());
            }
        };
    }
}
