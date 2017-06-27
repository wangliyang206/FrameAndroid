package com.cj.mobile.common.domain.subscriber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cj.mobile.common.util.Validate;

/**
 * 包名:com.chinamall21.mobile.common.domain.interactor
 * 对象名: ProgressDialogHandler
 * 描述:对话框消息处理类：利用对话框进行请求等待
 * 作者: 赵志军
 * 邮箱：zhijun.zhao@21chinamall.com
 * 创建日期: 2016/5/26 13:17
 */
public class ProgressDialogHandler extends ProgressHandler {
    private Context mContext;
    private String msg;
    private boolean cancelable = false;
    private ProgressDialog dialog;

    /**
     * 创建对话框消息处理类
     *
     * @param context    显示上下文
     * @param msg        等待消息
     * @param cancelable 是否可以取消
     */
    public ProgressDialogHandler(Context context, String msg, boolean cancelable) {
        super();
        this.mContext = context;
        this.msg = msg;
        this.cancelable = cancelable;
    }

    /**
     * 创建对话框消息处理类
     *
     * @param context    显示上下文
     * @param resid      等待消息ID
     * @param cancelable 是否可以取消
     */
    public ProgressDialogHandler(Context context, Integer resid, boolean cancelable) {
        super();
        this.mContext = context;
        this.msg = context.getResources().getString(resid);
        this.cancelable = cancelable;
    }

    @Override
    public void showProgress() {
        if (mContext == null) return;
        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(cancelable);
        if (!Validate.isEmpty(msg)) {
            dialog.setMessage(msg);
        }
        if (cancelable) {
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel();
                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void dismisProgress() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
