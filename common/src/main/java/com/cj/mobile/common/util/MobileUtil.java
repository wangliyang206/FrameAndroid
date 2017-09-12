package com.cj.mobile.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.cj.mobile.common.R;
import com.cj.mobile.common.util.etoast2.EToast2;
import com.cj.mobile.common.util.etoast2.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.cj.mobile.common.util.FileUtil.closeIO;

/**
 * 常用工具
 *
 * @author 王力杨
 */
public class MobileUtil {

    /**
     * 解决子Activity无法接收Activity回调的问题
     * <p/>
     * TabHostMain 的 onActivityResult固定写法如下：
     * <p/>
     * LocalActivityManager activityGroup = new LocalActivityManager(this,true);
     * <p/>
     * Activity subActivity = activityGroup.getCurrentActivity(); //判断是否实现返回值接口
     * if (subActivity instanceof OnTabActivityResultListener) { //获取返回值接口实例
     * OnTabActivityResultListener listener = (OnTabActivityResultListener)
     * subActivity; //转发请求到子Activity listener.onTabActivityResult(requestCode,
     * resultCode, data); }
     * <p/>
     * 子activity中实现 OnTabActivityResultListener
     *
     * @author Administrator
     */
    public interface OnTabActivityResultListener {
        public void onTabActivityResult(int requestCode, int resultCode,
                                        Intent data);
    }

    /**
     * 禁止new
     */
    public MobileUtil() {
    }

    /***
     * 拿到sdcard根路径
     */
    public static String getCacheSavePath() {
        if (HasSDCard()) {
            String savePath = Environment.getExternalStorageDirectory()
                    .getPath();

            return savePath;
        } else {
            return "";
        }
    }

    /***
     * 验证sdcard目录是否存在
     */
    @SuppressLint("SdCardPath")
    public static boolean HasSDCard() {
        try {
            File file = new File("/sdcard");
            return android.os.Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState()) || file.isDirectory();
        } catch (Exception e) {
            return android.os.Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState());
        }
    }

    /***
     * 通知
     */
    @SuppressWarnings("deprecation")
    public static void showNotification(Context context, String title,
                                        String content) {
        // 获取一个通知管理器
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建一个图标
        int icon = android.R.drawable.sym_action_email;
        // 创建一个新的通知，设定图标和标题
        long when = DateFormatUtils.getCurrentMSTime();
        Notification.Builder notification = new Notification.Builder(context);
        //设置图标
        notification.setSmallIcon(icon);
        //发送时间
        notification.setWhen(when);

//        notification.setTicker("显示第二个通知");
        //设置标题
        notification.setContentTitle(title);
        //消息内容
        notification.setContentText(content);
        //设置默认的提示音，振动方式，灯光
        notification.setDefaults(Notification.DEFAULT_SOUND);//Notification.DEFAULT_ALL
        //打开程序后图标消失
        notification.setAutoCancel(true);

        // 发送消息
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);
        // 设置消息内容
        notification.setContentIntent(pendingIntent);
        // 通过通知管理器发送通知
        Notification notification1 = notification.build();
        notificationManager.notify(0, notification1);
    }

    /***
     * 拿到手机的设备ID
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String ret = tm.getDeviceId();
            if (ret != null)
                return ret;
            else
                return "";
        } catch (Exception ex) {
            return "";
        }
    }

    /***
     * 获取手机型号
     */
    public static String getMobileNumber() {
        return android.os.Build.MODEL;
    }

    /***
     * 获取手机厂商
     */
    public static String getMobilePhoneManufacturers() {
        return android.os.Build.MANUFACTURER;
    }

    /***
     * 拿到电话号码(此接口不会100%的获取手机号码，原因是手机号码是映射到sim卡中的。要想100%获取手机号码只能通过靠对接运营商接口获得)
     */
    public static String getPhoneNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String ret = tm.getLine1Number();
            if (ret != null)
                return ret;
            else
                return "";
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 获取手机服务商信息
     */
    public static String getProvidersName(Context context) {
        String ProvidersName = "N/A";
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = tm.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } catch (Exception e) {
            return "";
        }
        return ProvidersName;
    }

    /***
     * Sim卡序列号
     */
    public static String getSimSerialNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String ret = tm.getSimSerialNumber();
            if (ret != null)
                return ret;
            else
                return "";
        } catch (Exception ex) {
            return "";
        }
    }

    /***
     * 呼叫电话
     */
    public static void callPhone(Context context, String phoneNum) {
        Pattern p = Pattern.compile("\\d+?");
        Matcher match = p.matcher(phoneNum);
        if (match.matches()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.tips_phone_format_error, EToast2.LENGTH_LONG).show();
        }
    }

    /***
     * 呼叫电话(无正则判断)
     */
    public static void callPhoneNum(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 发送短信
     *
     * @param context  句柄
     * @param phoneNum 手机号
     * @param content  内容
     */
    public static void sendTextMessage(Activity context, String phoneNum,
                                       String content) {
        SmsManager smsManager = SmsManager.getDefault();
        List<String> contents = smsManager.divideMessage(content);
        for (int i = 0; i < contents.size(); i++) {
            PendingIntent contentIntent = PendingIntent.getBroadcast(context,
                    0, new Intent("SENT_SMS_ACTION"), 0);
            smsManager.sendTextMessage(phoneNum, null, contents.get(i),
                    contentIntent, null);
        }
    }

    /**
     * 发送邮件
     */
    public static void sendMailIntent(Context context, String email) {
        String[] reciver = new String[]{email};
        // String[] mySbuject = new String[] { "test" };
        // String mybody = "测试Email Intent";
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
        myIntent.setType("plain/text");
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);// 收件人
        // myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
        // mySbuject);//主题
        // myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);//邮件内容
        context.startActivity(Intent.createChooser(myIntent, ""));

    }

    /**
     * 获取版本名称
     *
     * @param activity 句柄
     * @return 版本名称
     */
    public static String getVersionName(Context activity) {
        // 获取packagemanager的实例
        PackageManager pm = activity.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = pm.getPackageInfo(activity.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 获取版本号
     *
     * @param activity 句柄
     * @return 版本号
     */
    public static int getVersionCode(Context activity) {
        // 获取packagemanager的实例
        PackageManager pm = activity.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = pm.getPackageInfo(activity.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int version = packInfo.versionCode;
        return version;
    }

    /**
     * 将毫秒级的数转换成正规时间(例：12928转换成功后为00:12=分：秒；常用于媒体播放器)
     *
     * @param time 12928
     * @return 00:12
     */
    @SuppressLint("DefaultLocale")
    public static String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);

    }

    /**
     * 将 时：分：秒转换成毫秒级(常用于媒体播放器)
     *
     * @param time 00:00:12
     * @return
     */
    public static int toTimeFotmatMs(String time) {
        String shi = time.substring(0, time.indexOf(":"));
        String fen = time.substring(time.indexOf(":") + 1,
                time.lastIndexOf(":"));
        String miao = time.substring(time.lastIndexOf(":") + 1);

        return ((Integer.parseInt(shi) * 3600) + (Integer.parseInt(fen) * 60) + Integer
                .parseInt(miao)) * 1000;
    }

    /**
     * 描述：获取src中的图片资源.path 不以’/'开头时默认是从此类所在的包下取资源，以’/'开头则是从
     * ClassPath根下获取。其只是通过path构造一个绝对路径
     *
     * @param path 图片的src路径，如（“image/arrow.png”）
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFormSrc(String path) {
        Bitmap bit = null;
        try {
            bit = BitmapFactory.decodeStream(MobileUtil.class
                    .getResourceAsStream(path));
        } catch (Exception e) {

        }
        return bit;
    }

    /**
     * 从字符串中截取连续*位数字组合
     *
     * @param str    短信内容
     * @param digits 动态密码长度(一般为6位)
     * @return 截取得到的*位动态密码
     */
    public static String getDynamicPassword(String str, int digits) {
        // 6是验证码的位数一般为六位
        Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
                + digits + "})(?![0-9])");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            dynamicPassword = m.group();
        }

        return dynamicPassword;
    }

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 移动方法
     *
     * @param v      需要移动的View
     * @param startX 起始x坐标
     * @param toX    终止x坐标
     * @param startY 起始y坐标
     * @param toY    终止y坐标
     */
    public static void moveFrontBg(View v, int startX, int toX, int startY,
                                   int toY) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
                toY);
        anim.setDuration(200);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    /**
     * 设置 RelativeLayout 控件的大小
     *
     * @param v             需要设置的控件
     * @param screenWidth   屏幕宽度
     * @param heightPersent 高度比例
     */
    public static void setRelativeLayoutWidthHeightBy(View v, int screenWidth,
                                                      double heightPersent) {
        RelativeLayout.LayoutParams maskParams = (LayoutParams) v
                .getLayoutParams();
        maskParams.width = screenWidth;
        maskParams.height = (int) (screenWidth * heightPersent);
        v.setLayoutParams(maskParams);
    }

    /**
     * 设置 ViewGroup 控件的大小
     *
     * @param v             需要设置的控件
     * @param screenWidth   屏幕宽度
     * @param heightPersent 高度比例
     */
    public static void setViewGroupWidthHeightBy(View v, int screenWidth,
                                                 double heightPersent) {
        ViewGroup.LayoutParams maskParams = v.getLayoutParams();
        maskParams.width = screenWidth;
        maskParams.height = (int) (screenWidth * heightPersent);
        v.setLayoutParams(maskParams);
    }

    /**
     * 设置 ViewGroup 控件的大小
     *
     * @param v              需要设置的控件
     * @param screenWidths   屏幕宽度
     * @param heightPersents 高度
     */
    public static void setViewGroupsd(View v, int screenWidths,
                                      int heightPersents) {
        ViewGroup.LayoutParams maskParams = v.getLayoutParams();
        maskParams.width = screenWidths;
        maskParams.height = heightPersents;
        v.setLayoutParams(maskParams);
    }

    /***
     * 实体反射字段
     */
    public static Map<String, Object> beanReflection(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] arrField = obj.getClass().getDeclaredFields();
        for (Field f : arrField) {
            if (f.isAccessible() == false) {
                f.setAccessible(true);
            }
            String name = f.getName();
            Object value;
            try {
                value = f.get(obj);
                map.put(name, value);
            } catch (Exception e) {

            }
        }
        return map;
    }

    /**
     * Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将Bitmap转换成InputStream
     *
     * @param bm
     * @return
     */
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MemoryRecovery.recoveryStream(baos);
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 保存图片
     *
     * @param input    图片流
     * @param fileName 图片路径+名称
     * @return
     */
    public static File saveImage(InputStream input, String fileName) {

        if (fileName.trim().length() == 0 || input == null) {

            return null;

        }
        File file = new File(fileName);
        OutputStream output = null;
        try {

            file.getParentFile().mkdirs();
            if (file.exists() && file.isFile()) {

                file.delete();

            }
            if (!file.createNewFile()) {

                return null;

            }
            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            do {

                int numread = input.read(buffer);
                if (numread == -1) {
                    break;
                }
                output.write(buffer, 0, numread);

            } while (true);
            output.flush();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                output.close();

            } catch (IOException e) {

                e.printStackTrace();

            } catch (Exception e2) {

                e2.printStackTrace();

            }

        }
        return file;

    }

    /**
     * 扫描文件路径
     *
     * @param mContext
     * @param path
     */
    public static void MediaScannerConnection(Context mContext, final String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        mContext.sendBroadcast(intent);
    }

    /**
     * 保存图片
     *
     * @param mBitmap  图片
     * @param fileName 图片路径+名称
     * @return
     */
    public static File saveImage(Bitmap mBitmap, String fileName) {

        if (fileName.trim().length() == 0 || mBitmap == null) {

            return null;

        }
        File file = new File(fileName);
        FileOutputStream iStream = null;
        try {

            file.getParentFile().mkdirs();
            if (file.exists() && file.isFile()) {

                file.delete();

            }
            if (!file.createNewFile()) {

                return null;

            }
            iStream = new FileOutputStream(fileName);
            mBitmap.compress(CompressFormat.PNG, 100, iStream);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                iStream.close();
                iStream = null;
                file = null;

            } catch (Exception e) {

                e.printStackTrace();

            }

        }
        return file;

    }

    /**
     * 图片写入文件
     *
     * @param bitmap   图片
     * @param filePath 文件路径
     * @return 是否写入成功
     */
    public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null) {
            return isSuccess;
        }
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath),
                    8 * 1024);
            isSuccess = bitmap.compress(CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeIO(out);
        }
        return isSuccess;
    }


    /***
     * 加载图片
     */
    public static Bitmap loadBitmapByPath(String path) {
        File file = new File(path);
        if (!file.exists())
            return null;

        try {
            Bitmap bit = BitmapFactory.decodeFile(path); // 自定义//路径
            return bit;
        } catch (Exception ex) {
            // ex.printStackTrace();
            //("###" + ex.getMessage());
        }

        return null;
    }

    /**
     * 只提供获取图片尺寸
     *
     * @param path 完整路径
     * @return 可以使用Bitmap来获取图片的宽度和高度
     */
    @SuppressWarnings("resource")
    public static BitmapFactory.Options getPictureSize(String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            FileInputStream f = new FileInputStream(path);
            byte[] byt = new byte[f.available()];
            f.read(byt);
            BitmapFactory.decodeByteArray(byt, 0, byt.length, options);
            return options;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 加载图片(指定图片宽高)
     *
     * @param path      路径
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return
     */
    @SuppressWarnings("resource")
    public static Bitmap loadBitmapByPathAndSize(String path, int reqWidth,
                                                 int reqHeight) {
        try {

            File file = new File(path);
            if (!file.exists())
                return null;
            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            FileInputStream f = new FileInputStream(path);
            byte[] byt = new byte[f.available()];
            f.read(byt);
            BitmapFactory.decodeByteArray(byt, 0, byt.length, options);
            // 调用上面定义的方法计算inSampleSize值
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);
            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(byt, 0, byt.length, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片比例
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 将图片缩略到指定大小
     *
     * @param res       资源对象
     * @param resId     图片id
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(res.openRawResource(resId), null,
                options);
    }

    /**
     * 加载图片
     *
     * @param path 路径
     * @param size 8(图片的长宽都是原来的1/8)
     * @return
     */
    public static Bitmap loadBitmapByPathAndSize(String path, int size) {
        try {
            File file = new File(path);
            if (!file.exists())
                return null;
            FileInputStream f = new FileInputStream(path);
            Bitmap bit = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = size;// 图片的长宽都是原来的1/8
            BufferedInputStream bis = new BufferedInputStream(f);
            bit = BitmapFactory.decodeStream(bis, null, options);
            return bit;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /***
     * 修改图片大小、旋转
     */
    public static byte[] scaleBitmap(byte[] photodata, int setWidth,
                                     int setHeight, float rotate) {
        if (setWidth == 0 || setHeight == 0) {
            return photodata;
        }

        Bitmap bitmapOrg = BitmapFactory.decodeByteArray(photodata, 0,
                photodata.length);

        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();

        // 定义预转换成的图片的宽度和高度
        int newWidth = setWidth;
        int newHeight = setHeight;

        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);

        // 旋转图片 动作
        if (rotate > 0) {
            matrix.postRotate(rotate);
        }

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
                height, matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        MemoryRecovery.recoveryStream(baos);
        return bytes;
    }

    /***
     * 修改图片大小、旋转
     */
    public static byte[] scaleBitmap(Bitmap bitmapOrg, int setWidth,
                                     int setHeight, float rotate) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();

        // 定义预转换成的图片的宽度和高度
        int newWidth = setWidth;
        int newHeight = setHeight;

        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);

        // 旋转图片 动作
        if (rotate > 0) {
            matrix.postRotate(rotate);
        }

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
                height, matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        MemoryRecovery.recoveryStream(baos);
        return bytes;
    }

    /**
     * 加载图片(说明：特殊业务；常用于点击某个图片后触发放大效果(宽按屏幕尺寸展示、高按原图与屏幕比例展示))
     *
     * @param pictureMsg 控件
     * @param path       路径
     */
    public static void loadBitmaps(View picture, ImageView pictureMsg,
                                   String path, int screenWidth) {
        File file = new File(path);
        if (!file.exists())
            return;

        try {
            FileInputStream f = new FileInputStream(path);
            Bitmap bit = null;
            BufferedInputStream bis = new BufferedInputStream(f);
            bit = BitmapFactory.decodeStream(bis);

            ViewGroup.LayoutParams layoutParams = picture.getLayoutParams();
            float width, height;
            width = bit.getWidth();
            height = bit.getHeight();

            float proportion = height / width;

            float nowHeight = proportion * screenWidth;

            layoutParams.width = screenWidth;
            layoutParams.height = (int) nowHeight;
            picture.setLayoutParams(layoutParams);

            pictureMsg.setImageBitmap(bit);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /***
     * 缩放图片
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 根据宽度等比例缩放图片
     *
     * @param defaultBitmap
     * @param targetWidth   目标宽度
     * @return
     */
    public static Bitmap resizeImageByWidth(Bitmap defaultBitmap,
                                            int targetWidth) {
        int rawWidth = defaultBitmap.getWidth();
        int rawHeight = defaultBitmap.getHeight();
        float targetHeight = targetWidth * (float) rawHeight / (float) rawWidth;
        float scaleWidth = targetWidth / (float) rawWidth;
        float scaleHeight = targetHeight / (float) rawHeight;
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(scaleHeight, scaleWidth);
        return Bitmap.createBitmap(defaultBitmap, 0, 0, rawWidth, rawHeight,
                localMatrix, true);
    }

    /**
     * 描述：获取文字的像素宽.
     *
     * @param str   the str
     * @param paint the paint
     * @return the string width
     */
    public static float getStringWidth(String str, TextPaint paint) {
        float strWidth = paint.measureText(str);
        return strWidth;
    }

    /**
     * 描述：根据分辨率获得字体大小.
     *
     * @param screenWidth  the screen width
     * @param screenHeight the screen height
     * @param textSize     the text size
     * @return the int
     */
    public static int resizeTextSize(int screenWidth, int screenHeight,
                                     int textSize) {
        float ratio = 1;
        try {
            float ratioWidth = (float) screenWidth / 480;
            float ratioHeight = (float) screenHeight / 800;
            ratio = Math.min(ratioWidth, ratioHeight);
        } catch (Exception e) {
        }
        return Math.round(textSize * ratio);
    }

    /**
     * 访问联系人列表(单选)，此方法是访问要想取得联系人信息请在onActivityResult中调用getContactsValues方法
     *
     * @param context    句柄
     * @param resultCode 返回值
     */
    public static void jmpContacts(Activity context, int resultCode) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
                Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only//
        // contacts w/ phone//
        // numbers
        context.startActivityForResult(pickContactIntent, resultCode);
    }

    /**
     * 联系人列表返回的联系人信息(单选)
     *
     * @param context 句柄
     * @param data    联系人列表返回来的参数
     * @return 返回Map类型，key:姓名=name；手机号=phone
     */
    public static Map<String, Object> getContactsValues(Activity context,
                                                        Intent data) {
        Map<String, Object> map = new HashMap<String, Object>();
        Uri contactUri = data.getData();
        // We only need the NUMBER column, because there will be only one
        // row in the result

        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        // String[] projection = {Phone.NUMBER};

        // Perform the query on the contact to get the NUMBER column
        // We don't need a selection or sort order (there's only one result
        // for the given URI)
        // CAUTION: The query() method should be called from a separate
        // thread to avoid blocking
        // your app's UI thread. (For simplicity of the sample, this code
        // doesn't do that.)
        // Consider using CursorLoader to perform the query.
        Cursor cursor = context.getContentResolver().query(contactUri,
                projection, null, null, null);
        cursor.moveToFirst();

        // Retrieve the phone number from the NUMBER column
        int indexPhoneNumber = cursor.getColumnIndex(Phone.NUMBER);
        int indexDisplayName = cursor
                .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
        String phoneNumber = cursor.getString(indexPhoneNumber);
        String displayName = cursor.getString(indexDisplayName);

        if (phoneNumber != null && displayName != null) {
            if (phoneNumber.startsWith("+86"))
                phoneNumber = phoneNumber.substring(3);
            phoneNumber = phoneNumber.replace(" ", "");
            phoneNumber = phoneNumber.replace("-", "");
            map.put("name", displayName);
            map.put("phone", phoneNumber);
        }
        MemoryRecovery.recoveryCursor(cursor);
        return map;
    }

    /**
     * 验证当前手机中是否有安装指定的APP
     *
     * @param context 句柄
     * @param uri     (指定定APP的 包名)
     * @return
     */
    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * 解析Header
     */
    public static String getHeaders(Header[] heaer) {
        if (heaer == null || heaer.length == 0)
            return "";

        String values = "[";
        for (int i = 0; i < heaer.length; i++) {
            String name = heaer[i].getName();
            String val = heaer[i].getValue();
            values = values + name + ":" + val;
        }

        values = values + "]";
        return values;
    }

    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * android 中根据两个经纬度计算两地距离
     *
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return 返回单位是米
     */
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 获取 Android User Agent(浏览器标准格式)
     *
     * @param mContext 句柄
     * @return User Agent String
     */
    public static synchronized String getUserAgentBrowser(Context mContext) {
        String lan = LanguageSettingUtil.get().getLanguage();
        Locale locale = new Locale(lan);
        StringBuffer buffer = new StringBuffer();

//        API Version
        final int apiVersion = android.os.Build.VERSION.SDK_INT;
        buffer.append(apiVersion);

        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }

        // Add version
        String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
        } else {
            version = "1.0";
        }

//      com.android.internal.R.string.web_user_agent
        final String base = mContext.getResources().getText(R.string.web_user_agent).toString();
        return String.format(base, buffer, version);
    }

    /**
     * 获得Android User Agent(自定义格式)
     *
     * @param appContext 句柄
     * @return User Agent String
     */
    public static String getUserAgent(Context appContext) {
        PackageInfo info = getPackageInfo(appContext);

        StringBuilder ua = new StringBuilder("OSChina.NET");
        ua.append('/' + info.versionName + '_'
                + info.versionCode);// app版本信息
        ua.append("/Android");// 手机系统平台
        ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
        ua.append("/" + android.os.Build.MODEL); // 手机型号
        ua.append("/" + getAppId());// 客户端唯一标识
        return ua.toString();
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo(Context appContext) {
        PackageInfo info = null;
        try {
            info = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识(获取设备UUID)
     *
     * @return String
     */
    public static String getAppId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 复制链接
     */
    public static void copyUrl(Activity context, String content) {
        if (!Validate.isEmpty(content)) {
            ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clip.setText(content);
            com.cj.mobile.common.util.etoast2.Toast.makeText(context, R.string.copy_to_clipboard, EToast2.LENGTH_SHORT).show();
        }
    }

    /**
     * * 两个Double数相乘 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     */
    public static Double mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.multiply(b2).doubleValue());
    }

    /**
     * 格式化金额，千分位，保留2位(默认)小数
     *
     * @param price 金额
     * @param num   当前国家价格保留小数点后几位
     * @return 格式化的金额
     */
    public static String formatPrice(String price, int num) {
        String formatString = TextUtils.isEmpty(price) ? "0" : price;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(num);
        nf.setMinimumFractionDigits(num);
        return nf.format(new BigDecimal(formatString).doubleValue());
    }

    /**
     * * 两个Double数相乘 并保留对应的小数位数,小数位数以最长的小数位数为准
     *
     * @param v1 *
     * @param v2 *
     * @return String
     */
    public static String mulReservedDecimal(String v1, String v2) {
        int position = 0;
        int v1dotPos = v1.indexOf(".");
        int v2dotPos = v2.indexOf(".");
        int v1Position = 0;
        if (v1dotPos > 0) {
            v1Position = v1.length() - (v1dotPos + 1);
        }

        int v2Position = 0;
        if (v2dotPos > 0) {
            v2Position = v2.length() - (v2dotPos + 1);
        }

        if (v1Position > v2Position) {
            position = v1Position;
        } else {
            position = v2Position;
        }

        Double resultDouble = mul(v1, v2);

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(position);
        nf.setMinimumFractionDigits(position);
        return nf.format(resultDouble).replace(",", "");
    }

    /*#################################################SwipeBack(开始)#################################################*/

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(android.app.Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertActivityToTranslucentAfterL(activity);
        } else {
            convertActivityToTranslucentBeforeL(activity);
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms before Android 5.0
     */
    public static void convertActivityToTranslucentBeforeL(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz);
            method.setAccessible(true);
            method.invoke(activity, new Object[]{
                    null
            });
        } catch (Throwable t) {
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    private static void convertActivityToTranslucentAfterL(Activity activity) {
        try {
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz, ActivityOptions.class);
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, null, options);
        } catch (Throwable t) {
        }
    }

    /*#################################################SwipeBack(结束)#################################################*/

    /**
     * 判断当前应用是否为启动状态
     *
     * @param context
     * @return
     */
    public static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    /**
     * 拷贝文件
     * 如果目标文件不存在将会自动创建
     *
     * @param srcFile  原文件
     * @param saveFile 目标文件
     * @return 是否拷贝成功
     */
    public static boolean copyFile(final File srcFile, final File saveFile) {
        File parentFile = saveFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs())
                return false;
        }

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            MemoryRecovery.close(inputStream, outputStream);
        }
        return true;
    }
}