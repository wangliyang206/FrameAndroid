package com.cj.mobile.common.base;

import com.cj.mobile.common.domain.subscriber.ProgressHandler;
import com.cj.mobile.common.domain.subscriber.ProgressSubscriber;
import com.cj.mobile.common.domain.usecase.ErrorBundle;
import com.cj.mobile.common.domain.usecase.UseCase;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Title: BasePresenter
 * @Package com.cj.mobile.common.base
 * @Description: 负责完成View于Model间的逻辑和交互(此类中是对于请求过程、请求响应简单封装)
 * @author: wly
 * @date: 2016/12/3 16:48
 */
public abstract class BasePresenter<M, V> implements IPresenter {
    private ErrorBundle errorBundle;
    /*订阅管理*/
    private final List<Subscription> subscriptions = new ArrayList<>();

    public BasePresenter(ErrorBundle errorBundle) {
        this.errorBundle = errorBundle;
    }

    /**
     * 有loading条的 订阅
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
            subscriptions.add(observable.compose(this.<T>applySchelders()).subscribe(subscriber));
        };
    }

    /**
     * 无loading条的 订阅
     * @param observable
     * @param listener
     * @param <T>
     */
    protected final <T> void doSubscription(Observable<T> observable, UseCase.UseCaseCallback<T> listener) {

        doSubscription(observable, null, listener);
    }

    private <T> Observable.Transformer<T, T> applySchelders() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void onDestroy() {
        unsubscribe();
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
