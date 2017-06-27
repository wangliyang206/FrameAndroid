package com.cj.mobile.common.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 获取验证码计时
 *
 * @author 王力杨
 */
@SuppressLint("HandlerLeak")
public class VerificationCodeTiming {
    private String btnText;
    private Timer mTimer;
    private Button mCodeBtn;
    public int time = 60;
    TimerTask task;

    public VerificationCodeTiming(Button btn) {
        this.mCodeBtn = btn;

    }

    public VerificationCodeTiming(Button btn, String text, int time) {
        this.mCodeBtn = btn;
        this.btnText = text;
        this.time = time;
    }

    private final Handler upHandler = new Handler() {
        private int[] location;
        private int y;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (time > 0) {
                        mCodeBtn.setEnabled(false);
                        location = new int[2];
                        // mCodeBtn.getLocationOnScreen(location);
                        // y = location[1];
                        // if (y > 120) {
                        mCodeBtn.setText(time + "s");// + "秒后重新发送"
                        // }
                        mCodeBtn.setTextSize(14);
                    } else {
                        mTimer.cancel();
                        mCodeBtn.setEnabled(true);
                        mCodeBtn.setText(btnText);
                        mCodeBtn.setTextSize(14);
                    }
                    break;

                default:
                    break;
            }
        }

        ;

    };

    public void runTimer() {
        mTimer = new Timer(true);
        task = new TimerTask() {

            @Override
            public void run() {
                time--;
                Message msg = upHandler.obtainMessage();
                msg.what = 1;
                upHandler.sendMessage(msg);
            }

        };
        mTimer.schedule(task, 100, 1000);
    }

    public void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        time = 0;
    }
}
