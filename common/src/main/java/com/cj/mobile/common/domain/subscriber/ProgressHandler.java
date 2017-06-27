package com.cj.mobile.common.domain.subscriber;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 包名:com.chinamall21.mobile.common.domain.interactor
 * 对象名: ProgressHandler
 * 描述:处理请求过程中订阅者发送的消息，与ProgressSubscriber配合使用
 * 作者: 赵志军
 * 邮箱：zhijun.zhao@21chinamall.com
 * 创建日期: 2016/5/26 10:04
 */
public abstract class ProgressHandler extends Handler {
    private ProgressCancelListener progressCancelListener;

    public ProgressHandler() {
        //定义主线程
        super(Looper.getMainLooper());
    }

    final void setProgressCancelListener(ProgressCancelListener progressCancelListener) {
        this.progressCancelListener = progressCancelListener;
    }

    public final void cancel() {
        if (this.progressCancelListener != null) {
            this.progressCancelListener.onCancelProgress();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case ProgressSubscriber.MSGTYPE_SHOW:
                showProgress();
                break;
            case ProgressSubscriber.MSGTYPE_DISMISS:
                dismisProgress();
                break;
            default:
                break;
        }
    }

    public abstract void showProgress();

    public abstract void dismisProgress();
}
