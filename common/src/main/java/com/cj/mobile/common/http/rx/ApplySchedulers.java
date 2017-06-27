package com.cj.mobile.common.http.rx;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 包名： com.cj.mobile.common.http.rx
 * 对象名： ApplySchedulers
 * 描述：进行Observable转换
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 9:45
 */

public class ApplySchedulers<T> implements Observable.Transformer<T, T> {

    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable
                .timeout(10, TimeUnit.SECONDS)// 任务超时设置为10秒
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}