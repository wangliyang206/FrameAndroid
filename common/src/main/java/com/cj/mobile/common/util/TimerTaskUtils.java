package com.cj.mobile.common.util;


import timber.log.Timber;

/***
 * 倒计时工具类
 *
 * @author wly
 */
public class TimerTaskUtils extends CountDownTimer {
    //是否停止
    private boolean isStop = false;
    //回调
    private TimerTaskCallback css;

    public interface TimerTaskCallback {
        void overTimerTaskCallback();
    }

    public void setData(TimerTaskCallback css) {
        this.css = css;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public TimerTaskUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
    }

    @Override
    public void onTick(long millisUntilFinished) {//计时过程显示
        Timber.d("-----------倒计时中(" + millisUntilFinished / 1000 + "秒)--------");
    }

    @Override
    public void onFinish() {//计时完毕时触发
        isStop = false;

        Timber.d("-----计时完毕时触发-----");

        //以下是触发业务
        if (this.css != null) {
            this.css.overTimerTaskCallback();
        }
    }

}
