package com.cj.mobile.common.domain.subscriber;


import com.cj.mobile.common.domain.usecase.IErrorBundle;
import com.cj.mobile.common.domain.usecase.UseCase;

import rx.Subscriber;

/**
 * 包名:com.chinamall21.mobile.common.domain.interactor
 * 对象名: ProgressSubscriber
 * 描述:实现请求过程中显示请求等待窗口的观察者类
 * 作者: 赵志军
 * 邮箱：zhijun.zhao@21chinamall.com
 * 创建日期: 2016/5/26 10:03
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    public static final int MSGTYPE_SHOW = 0;
    public static final int MSGTYPE_DISMISS = 1;

    /**用于向主线程发送通知*/
    private final ProgressHandler progressHandler;
    /**处理订阅结果*/
    private final UseCase.UseCaseCallback<T> resultListener;
    private final IErrorBundle errorBundle;

    public ProgressSubscriber(ProgressHandler progressHandler,
                              UseCase.UseCaseCallback<T> resultListener,
                              IErrorBundle errorBundle) {
        this.progressHandler = progressHandler;
        if (this.progressHandler != null) {
            this.progressHandler.setProgressCancelListener(this);
        }
        this.resultListener = resultListener;
        this.errorBundle = errorBundle;
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgress();
    }

    @Override
    public void onCompleted() {
        dismissProgress();
    }

    @Override
    public void onError(Throwable e) {
        if (resultListener != null && errorBundle != null) {
            errorBundle.praseError(e, resultListener);
        }
        dismissProgress();
    }

    @Override
    public void onNext(T t) {
        if (resultListener != null) {
            resultListener.onSuccess(t);
        }
    }

    /**
     * 发送显示请求状态的消息
     */
    private void showProgress() {
        if (progressHandler != null) {
            progressHandler.sendEmptyMessage(MSGTYPE_SHOW);
        }
    }

    /**
     * 发送关闭请求状态的消息
     */
    private void dismissProgress() {
        if (progressHandler != null) {
            progressHandler.sendEmptyMessage(MSGTYPE_DISMISS);
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
