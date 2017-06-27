package com.cj.mobile.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cj.mobile.common.R;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity工具类
 *
 * @author 王力杨
 */
public class ActivityUtils {
    private ActivityUtils() {
    }


    /**
     * 窗体跳转
     *
     * @param old 当前窗体
     * @param cls 目标窗体
     */
    public static void jump(Context old, Class<?> cls) {
        jump(old, cls, -1, null, false, -1);
    }

    /**
     * 窗体跳转
     *
     * @param old     当前窗体
     * @param cls     目标窗体
     * @param mBundle 传递参数
     * @param flags   信号旗
     */
    public static void jump(Context old, Class<?> cls, Bundle mBundle, int flags) {
        jump(old, cls, -1, mBundle, false, flags);
    }

    /**
     * 窗体跳转
     *
     * @param old         当前窗体
     * @param cls         目标窗体
     * @param requestCode 请求编号
     * @param mBundle     传递参数
     */
    public static void jump(Context old, Class<?> cls, int requestCode, Bundle mBundle) {
        jump(old, cls, requestCode, mBundle, false, -1);
    }

    /**
     * 窗体跳转
     *
     * @param old         当前窗体
     * @param cls         目标窗体
     * @param requestCode 请求编号
     * @param mBundle     传递参数
     * @param clearTop    是否启动一个新实例的活动
     */
    public static void jump(Context old, Class<?> cls, int requestCode,
                            Bundle mBundle, boolean clearTop) {
        jump(old, cls, requestCode, mBundle, clearTop, -1);
    }

    /**
     * 窗体跳转
     *
     * @param old         当前窗体
     * @param cls         目标窗体
     * @param requestCode 请求编号
     * @param mBundle     传递参数
     * @param clearTop    是否启动一个新实例的活动
     * @param flags       信号旗
     */
    public static void jump(Context old, Class<?> cls, int requestCode,
                            Bundle mBundle, boolean clearTop, int flags) {
        Intent intent = new Intent();
        intent.setClass(old, cls);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }

        Activity activity = (Activity) old;
        if (clearTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        if (flags > -1) {
            intent.setFlags(flags);
        }

        if (requestCode != -1) {
            intent.putExtra("requestCode", requestCode);
            activity.startActivityForResult(intent, requestCode);
        } else
            activity.startActivity(intent);
    }

    /**
     * 窗体跳转
     *
     * @param old         当前窗体
     * @param cls         目标窗体
     * @param requestCode 请求编号
     */
    public static void jump(Context old, Class<?> cls, int requestCode) {
        jump(old, cls, requestCode, null);
    }

    /**
     * 获取drawable下的图片
     *
     * @param context     句柄
     * @param mDrawableId 图片id
     * @return
     */
    public static Drawable getDrawable(Context context, int mDrawableId) {
        Drawable drawable = context.getResources().getDrawable(mDrawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 获取Strings.xml文件中的内容
     *
     * @param context   句柄
     * @param mStringId Strings.xml文件中name的id
     * @return 字符串内容
     */
    public static String getString(Context context, int mStringId) {
        return context.getResources().getString(mStringId);
    }

    /**
     * 获取Strings.xml文件中的内容
     *
     * @param context
     * @param mStringId
     * @param args
     * @return
     */
    public static String getString(Context context, int mStringId, Object... args) {
        String testStr = context.getResources().getString(mStringId);
        return String.format(testStr, args);
    }

    /**
     * 获取Color.xml文件中的颜色值
     *
     * @param context  句柄
     * @param mColorId Color.xml文件中name的id
     * @return 颜色值
     */
    public static int getColor(Context context, int mColorId) {
        return context.getResources().getColor(mColorId);
    }

    /**
     * 将颜色字符串例如#FF00FF转换int颜色值
     *
     * @param colorStr #ffffff
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static int colorFromString(String colorStr) {
        if (colorStr == null)
            return 0;
        colorStr = colorStr.toUpperCase();
        colorStr = colorStr.trim();
        colorStr = colorStr.replaceAll("#", "");
        colorStr = colorStr.replaceAll("0x", "");

        if (colorStr.length() == 6) {
            int i = 0;
            int red = ((int) colorStr.charAt(i++) - 55) * 16;
            red += (int) colorStr.charAt(i++) - 55;

            int green = ((int) colorStr.charAt(i++) - 55) * 16;
            green += (int) colorStr.charAt(i++) - 55;

            int blue = ((int) colorStr.charAt(i++) - 55) * 16;
            blue += (int) colorStr.charAt(i++) - 55;

            return Color.rgb(red, green, blue);
        }

        return 0;
    }

    /**
     * 获取资源ID 例：图片a.png：在传入str时就写a,cla写Drawable.class；</br>
     * 如果为颜色值：str写颜色的name,cla写Color.class
     *
     * @param str 名称
     * @param cla 对应的Class
     * @return 资源Id(数值类型)
     */
    @SuppressWarnings("rawtypes")
    public static int getResource(String str, Class cla) {
        Field field = null;
        int r_id = -1;
        try {
            field = cla.getField(str);
            r_id = field.getInt(field.getName());
        } catch (Exception e) {

        }
        return r_id;
    }

    /**
     * 获取EditText内容
     *
     * @param edt EditText控件
     * @return String字符串
     */
    public static String getViewContent(EditText edt) {
        if (edt == null)
            return "";
        return edt.getText().toString().trim();
    }

    /**
     * 获取TextView内容
     *
     * @param tv TextView控件
     * @return String字符串
     */
    public static String getViewContent(TextView tv) {
        if (tv == null)
            return "";
        return tv.getText().toString().trim();
    }

    /**
     * 获取Button内容
     *
     * @param btn Button控件
     * @return String字符串
     */
    public static String getViewContent(Button btn) {
        if (btn == null)
            return "";
        return btn.getText().toString().trim();
    }

    // 最后一次点击时间
    private static long lastClickTime;

    /**
     * 解决了按钮重复点击
     *
     * @return
     */
    public static boolean isDoubleClick() {
        long time = DateFormatUtils.getCurrentMSTime();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 退出程序对话框(会杀死进程)
     *
     * @param context
     */
    public static void showExitDialog(final Activity context) {
        AlertDialogCustom.CSSShowAlert css = new AlertDialogCustom.CSSShowAlert() {
            @Override
            public void onOver(boolean isSucceed) {
                if (isSucceed) {
                    context.finish();
                    Process.killProcess(Process.myPid());
                }
            }
        };

        String title = getString(context, R.string.showAlerttitle_Information);
        String content = getString(context, R.string.common_exit_app_tips);
        String exit = getString(context, R.string.common_exit_app);
        String cancel = getString(context, R.string.common_cancel);

        AlertDialogCustom.showAlert(context, R.drawable.warning, title, content, exit, cancel, css);
    }

    /**
     * 按两次Back在退出程序
     *
     * @param context 句柄
     */
    public static void exitSys(Activity context) {
        if ((System.currentTimeMillis() - lastClickTime) > 2000) {
            showToastText(context,
                    ActivityUtils.getString(context, R.string.common_exit));
            lastClickTime = System.currentTimeMillis();
        } else {
            context.finish();
            /*当前是退出APP后结束进程。如果不这样做，那么在APP结束后需求手动将EventBus中所注册的监听全部清除以免APP在次启动后重复注册监听*/
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * 弹出软件键盘，适用于Activity跳转完毕后需要打开软件键盘时调用此方法
     *
     * @param context 句柄
     * @param input   EditText控件
     */
    public static void showTimeControlPopupInputWindow(final Context context,
                                                       final EditText input) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                showInputWindow(context, input);
            }
        }, 998);
    }

    /**
     * 指定弹出软件键盘
     *
     * @param context 句柄
     * @param edtTxt  EditText控件
     */
    public static void showInputWindow(Context context, EditText edtTxt) {
        InputMethodManager showKeyboard = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        showKeyboard.showSoftInput(edtTxt, 0);
    }

    /**
     * 指定隐藏键盘
     *
     * @param context 句柄
     * @param edtTxt  EditText控件
     */
    public static void hideInputWindow(Context context, EditText edtTxt) {
        InputMethodManager showKeyboard = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        showKeyboard.hideSoftInputFromWindow(edtTxt.getWindowToken(), 0);
    }

    /**
     * 强制隐藏键盘
     */
    public static void hideInputWindow(Activity context) {
        ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(context.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /***
     * 提示框
     */
    public static void showToastText(Context context, int resid) {
        showToastText(context, context.getString(resid));
    }

    /***
     * 提示框
     */
    public static void showToastText(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 测量这个view，最后通过getMeasuredWidth()获取宽度和高度.
     *
     * @param v 要测量的view
     * @return 测量过的view
     */
    public static void measureView(View v) {
        if (v == null) {
            return;
        }
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
    }

    /**
     * 添加Fragment，方法内调用了addToBackStack()(所以支持多Fragment回退)
     *
     * @param activity         句柄
     * @param fragment         Fragment对象
     * @param contentFrame     布局layout的ID
     * @param isAddToBackStack 是否将Fragment加入到回退栈中
     * @param bundle           参数
     */
    public static void startFragmentAdd(
            FragmentActivity activity,
            Fragment fragment,
            int contentFrame,
            boolean isAddToBackStack,
            Bundle bundle) {

        if (fragment == null) {
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        } else {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.replace(contentFrame, fragment,
                fragment.getClass().getName()).commitAllowingStateLoss();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context 句柄
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context 句柄
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取屏幕尺寸
     */
    public static Point getScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point out = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(out);
        }else{
            int width = display.getWidth();
            int height = display.getHeight();
            out.set(width, height);
        }
        return out;
    }

    /**
     * 获取窗口宽度
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        return mScreenWidth;
    }

    /**
     * 获取窗口高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeigh(Context context) {
        // 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeigh = dm.heightPixels;
        return mScreenHeigh;
    }

    /**
     * 设置ScrollView滑动到指定位置
     * 注意：在调用此方法前应先将view中的数据填充；
     * 滚到到底部：   mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
     * 滚动到顶部：   mScrollview.fullScroll(ScrollView.FOCUS_UP);
     *
     * @param mScrollview 滑动控件
     * @param view        子控件
     */
    public static void setScrollViewSpecifiedLocation(final ScrollView mScrollview, final View view) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mScrollview == null || view == null) {
                    return;
                }

                int offset = mScrollview.getHeight() - view.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                mScrollview.scrollTo(0, offset);
            }
        });

    }
}
