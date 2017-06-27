package com.cj.mobile.common.module.server;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.widget.EditText;

import com.cj.mobile.common.util.MobileUtil;

/**
 * 监听短信数据库<br/>
 * 作用：短信服务发送短信后，手机端开启短信监听服务，一旦监听对应短信内容后将会自动填充到输入框
 *
 * @author 王力杨
 */
public class SmsContent extends ContentObserver {
    private Uri uri = Uri.parse("content://sms/inbox");
    private Uri registUri = Uri.parse("content://sms/");
    private Activity activity; // 上下文
    private EditText edit; // 取到手机号码，显示在EditText上
    private String phone; // 需要监听的手机号码
    private int digits; // 动态密码的位数

    private boolean isOpen; // 当前监听是否开启

    /**
     * 构造方法
     *
     * @param activity 句柄
     * @param edit     截取动态密码后需要显示的控件
     * @param phone    需要监听的手机号码
     * @param digits   动态密码位数
     * @param handler
     */
    public SmsContent(Activity activity, EditText edit, String phone,
                      int digits, Handler handler) {
        super(handler);
        this.activity = activity;
        this.edit = edit;
        this.phone = phone;
        this.digits = digits;

        onAddListening();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        isOpen = true;// 在这加的原因是怕外面根据对象重新增加监听
        // 读取收件箱中指定号码的短信
        Cursor cursor = activity.managedQuery(uri, new String[]{"_id",
                        "address", "read", "body"}, " address=? and read=?",
                new String[]{"" + phone + "", "0"}, "_id desc");
        // 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
        if (cursor != null && cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put("read", "1"); // 修改短信为已读模式
            cursor.moveToNext();
            int smsbodyColumn = cursor.getColumnIndex("body");
            String smsBody = cursor.getString(smsbodyColumn);
            edit.setText(MobileUtil.getDynamicPassword(smsBody, digits));
            // 显示以后关闭监听
            onDestroy();
        }
        // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }

    /**
     * 注册监听
     */
    public void onAddListening() {
        if (!isOpen) {
            // 注册监听
            this.activity.getContentResolver().registerContentObserver(registUri, true, this);
            isOpen = true;
        }
    }

    /**
     * 关闭监听
     */
    public void onDestroy() {
        if (isOpen) {
            activity.getContentResolver().unregisterContentObserver(this);
            isOpen = false;
        }
    }

}
