package com.cj.mobile.common.base;

import android.content.Context;

import com.cj.mobile.common.domain.subscriber.ProgressHandler;
import com.cj.mobile.common.domain.subscriber.ProgressSubscriber;
import com.cj.mobile.common.domain.usecase.ErrorBundle;
import com.cj.mobile.common.domain.usecase.IErrorBundle;
import com.cj.mobile.common.domain.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 包名： com.cj.mobile.common.base
 * 对象名： BaseBo
 * 描述：
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 9:06
 */

public class BaseBo {
    private IErrorBundle errorBundle;
    public Context mContext;

    public BaseBo(Context context) {
        this.mContext = context;
        this.errorBundle = ErrorBundle.init(context, -1);
    }

    public BaseBo(Context context, int tokenInvalid) {
        this.mContext = context;
        this.errorBundle = ErrorBundle.init(context, tokenInvalid);
    }

    private final List<Subscription> subscriptions = new ArrayList<>();

    /**
     * 有loading条的 订阅
     *
     * @param observable
     * @param handler
     * @param listener
     * @param <T>
     */
    protected final <T> void doSubscription(Observable<T> observable,
                                            ProgressHandler handler,
                                            UseCase.UseCaseCallback<T> listener) {

        Subscriber<T> subscriber = new ProgressSubscriber<T>(handler, listener, errorBundle);
        if (observable != null) {
            subscriptions.add(observable.compose(this.<T>applySchedulers()).subscribe(subscriber));
        }
        ;
    }

    /**
     * 进行Observable转换
     *
     * @param <T>
     * @return
     */
    private <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 无loading条的 订阅
     *
     * @param observable
     * @param listener
     * @param <T>
     */
    protected final <T> void doSubscription(Observable<T> observable, UseCase.UseCaseCallback<T> listener) {

        doSubscription(observable, null, listener);
    }

    /**
     * 取消订阅
     */
    public void unsubscribe() {
        synchronized (subscriptions) {
            while (subscriptions.size() > 0) {
                Subscription subscription = subscriptions.remove(0);
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
        }
    }
}
