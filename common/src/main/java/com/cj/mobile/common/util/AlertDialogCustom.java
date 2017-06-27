package com.cj.mobile.common.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.cj.mobile.common.R;

/**
 * 自定义Dialog
 *
 * @author 王力杨
 */
public class AlertDialogCustom {

    public interface CSSShowAlert {
        public void onOver(boolean isSucceed);
    }

    /**
     * 简单弹框
     *
     * @param context
     * @param msg
     */
    public static void showAlert(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(ActivityUtils.getString(context, R.string.common_ok), null);
        builder.show();
    }

    /**
     * 自定义系统样式Dialog(两个按钮，按钮的名称可以自定义)
     *
     * @param activity   句柄
     * @param title      提示标题
     * @param content    提示内容
     * @param okName     OK的按钮名称
     * @param cancelName Cancel的按钮名称
     * @param css        回调函数
     */
    public static void showAlert(int style, Context activity,
                                 String title, String content,
                                 String okName, String cancelName,
                                 final CSSShowAlert css) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(okName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (css != null)
                                    css.onOver(true);
                                dialog.cancel(); // 删除对话框
                            }
                        })
                .setNegativeButton(cancelName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (css != null)
                                    css.onOver(false);
                                dialog.cancel(); // 删除对话框
                            }
                        });

        AlertDialog alert = builder.create();// 创建对话框
        alert.show();// 显示对话框
    }

    /**
     * 自定义系统样式Dialog(两个按钮，按钮的名称可以自定义)
     *
     * @param activity   句柄
     * @param title      提示标题
     * @param content    提示内容
     * @param okName     OK的按钮名称
     * @param cancelName Cancel的按钮名称
     * @param css        回调函数
     */
    public static void showAlert(Context activity, String title,
                                 String content, String okName, String cancelName,
                                 final CSSShowAlert css) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(okName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (css != null)
                                    css.onOver(true);
                                dialog.cancel(); // 删除对话框
                            }
                        })
                .setNegativeButton(cancelName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (css != null)
                                    css.onOver(false);
                                dialog.cancel(); // 删除对话框
                            }
                        });

        AlertDialog alert = builder.create();// 创建对话框
        alert.show();// 显示对话框
    }

    /**
     * 系统样式Dialog(只有一个按钮，按钮名称可以自定义)
     *
     * @param activity 句柄
     * @param title    提示的标题
     * @param content  提示内容
     * @param btnName  按钮名称
     * @param css      回调函数
     */
    public static void showAlert(Context activity, String title,
                                 String content, String btnName, final CSSShowAlert css) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(btnName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); // 删除对话框
                                if (css != null)
                                    css.onOver(true);
                            }
                        });

        AlertDialog alert = builder.create();// 创建对话框
        alert.show();// 显示对话框
    }

    /**
     * 自定义系统样式Dialog(两个按钮，按钮的名称可以自定义)
     *
     * @param activity   句柄
     * @param icon       提示Logo
     * @param title      提示标题
     * @param content    提示内容
     * @param okName     OK的按钮名称
     * @param cancelName Cancel的按钮名称
     * @param css        回调函数
     */
    public static void showAlert(Context activity, int icon, String title,
                                 String content, String okName, String cancelName,
                                 final CSSShowAlert css) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(okName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (css != null)
                                    css.onOver(true);
                                dialog.cancel(); // 删除对话框
                            }
                        })
                .setNegativeButton(cancelName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (css != null)
                                    css.onOver(false);
                                dialog.cancel(); // 删除对话框
                            }
                        });

        AlertDialog alert = builder.create();// 创建对话框
        alert.show();// 显示对话框
    }

    /**
     * 系统样式Dialog(只有一个按钮，按钮名称可以自定义)
     *
     * @param activity 句柄
     * @param icon     提示Logo
     * @param title    提示的标题
     * @param content  提示内容
     * @param btnName  按钮名称
     * @param css      回调函数
     */
    public static void showAlert(Context activity, int icon, String title,
                                 String content, String btnName, final CSSShowAlert css) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setIcon(icon)
                .setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(btnName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); // 删除对话框
                                if (css != null)
                                    css.onOver(true);
                            }
                        });

        AlertDialog alert = builder.create();// 创建对话框
        try {
            alert.show();// 显示对话框
        } catch (Exception e) {
        }
    }

    /**
     * 弹出View对话框(只做弹出，不做View中任何点击事件效果)
     *
     * @param activity 句柄
     * @param view     内容(View视图)
     */
    public static AlertDialog showAlert(Context activity, View view,
                                        int width, int height) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (width == 0 && height == 0)
            builder.setView(view);

        AlertDialog alert = builder.create();// 创建对话框
        alert.show();// 显示对话框

        if (width != 0 && height != 0) {
            WindowManager.LayoutParams layoutParams = alert.getWindow()
                    .getAttributes();
            layoutParams.width = width;
            layoutParams.height = height;

            alert.getWindow().setGravity(Gravity.CENTER);
            alert.getWindow().setAttributes(layoutParams);
            alert.getWindow().setContentView(view);
        }

        return alert;
    }
}
