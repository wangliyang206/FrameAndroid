package com.cj.mobile.common.domain.usecase;

import com.cj.mobile.common.domain.executor.UseCaseScheduler;
import com.cj.mobile.common.domain.subscriber.ProgressHandler;
import com.cj.mobile.common.domain.subscriber.ProgressSubscriber;

import java.io.Serializable;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public abstract class UseCase<T> {

  private UseCaseScheduler useCaseScheduler;

  private Subscription subscription = Subscriptions.empty();

  private IErrorBundle errorBundle;

  protected UseCase(UseCaseScheduler useCaseScheduler, IErrorBundle errorBundle) {
      this.useCaseScheduler=useCaseScheduler;
      this.errorBundle=errorBundle;
  }

  protected abstract Observable<T> buildUseCaseObservable();

  public final void execute(ProgressHandler progressHandler, UseCaseCallback<T> resultListener) {
    this.subscription = this.buildUseCaseObservable()
         .compose(useCaseScheduler.<T>applyScheduler())
        .subscribe(new ProgressSubscriber<T>(progressHandler,resultListener,errorBundle));
  }

  public final void execute(UseCaseCallback<T> resultListener) {
      execute(null,resultListener);
  }

  public void unsubscribe() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  public interface RequestValue extends Serializable{};
  public interface ResponseValue extends Serializable{};

  public interface UseCaseCallback<R> {
    void onSuccess(R response);
    void onError(int errorcode, String msg);
  }
}
