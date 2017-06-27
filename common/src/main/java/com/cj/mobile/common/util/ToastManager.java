package com.cj.mobile.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cj.mobile.common.R;

/**
 * 自定义Tost
 *
 * @author 王力杨
 */
@SuppressLint("HandlerLeak")
public class ToastManager {

    private final String TAG = "ToastManager";
    private static ToastManager mToastManager;
    private WindowManager mWM;
    private View mView;
    private TextView mTips;
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
            if (mView != null)
                mView.setAnimation(animation);
            hide();
        }
    };

    private long showLong = 2000L;

    public static ToastManager instance() {
        if (mToastManager != null) {
            mToastManager.cancel();
        }
        if (mToastManager == null)    //判断为空时,如当前在显示会立即隐藏
            mToastManager = new ToastManager();
        return mToastManager;
    }

    public void show(Context context, int resid) {
        this.mContext = context;
        init(context, resid, "", Gravity.CENTER, 0, 0);
    }

    public void show(Context context, int resid, int showLong) {
        this.mContext = context;
        this.showLong = showLong;
        init(context, resid, "", 17, 0, 0);
    }

    public void show(Context context, String text) {
        this.mContext = context;
        init(context, 0, text, 17, 0, 0);
    }

    public void show(Context context, String text, int showLong) {
        this.mContext = context;
        this.showLong = showLong;
        init(context, 0, text, 17, 0, 0);
    }

    public void show(Context context, int resid, int gravity, int xOffset, int yOffset) {
        this.mContext = context;
        init(context, resid, "", gravity, xOffset, yOffset);
    }

    private void init(Context context, int resid, String msg, int gravity,
                      int xOffset, int yOffset) {
        try {
            hide();
            context = context.getApplicationContext();
            this.mView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.toast_custom_layout, null);
            this.mTips = ((TextView) this.mView.findViewById(R.id.txvi_toastCustomLayout_message));
            if (this.mView != null) {
                if (resid > 0)
                    this.mTips.setText(resid);
                else {
                    this.mTips.setText(msg);
                }
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.height = -2;
                params.width = -2;
                params.flags = 152;

                params.format = -3;
                params.type = 2005;

                params.gravity = gravity;
                params.x = xOffset;
                params.y = yOffset + 120;

                this.mWM = ((WindowManager) context.getSystemService("window"));// window
                if (this.mView.getParent() != null) {
                    this.mWM.removeView(this.mView);
                }

                this.mWM.addView(this.mView, params);
                this.mHandler.sendEmptyMessageDelayed(0, this.showLong);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void hide() {
        try {
            if (this.mView != null) {
                if (this.mView.getParent() != null) {
                    this.mWM.removeView(this.mView);
                }
                this.mView = null;
                this.mTips = null;
                this.mWM = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "hide() ex = " + e.getMessage());
        }
    }

    public void cancel() {
        try {
            if (this.mView != null) {
                if (this.mView.getParent() != null) {
                    this.mWM.removeView(this.mView);
                }
                this.mWM = null;
                this.mView = null;
                this.mTips = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mToastManager = null;
    }
}