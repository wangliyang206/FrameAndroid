package com.cj.mobile.common.base;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cj.mobile.common.R;
import com.cj.mobile.common.util.etoast2.EToast2;
import com.cj.mobile.common.util.etoast2.Toast;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * Activity 基类 (所有Activity用到同样的方法或参数时都在这里定义 例：隐藏标题栏、横屏竖屏展示)
 *
 * @author 王力杨
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Fragment mFragment;
    /**
     * 缓存所已打开的Activity
     */
    public static Map<String, BaseActivity> cacheActivitys = new HashMap<String, BaseActivity>();
    public static BaseActivity activeAcitity;

    protected int mColorId = R.color.transparent_color;//状态栏的默认背景色
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*设置窗体*/
        setForm();

        /*载入界面布局*/
        setContentView(getViewID());

        /*依赖注入*/
        ButterKnife.bind(this);

        /*沉浸式状态栏*/
        initStateBar();

        /*提供对Bundle操作*/
        getBundleValues();

        /*初始化基本信息*/
        init();

        //初始化View数据
        initViewData();
    }

    /**
     * 初始化沉浸式
     */
    private void initStateBar() {
        if (isNeedLoadStatusBar()) {
            loadStateBar();
        }
    }

    /**
     * 子类是否需要实现沉浸式,默认需要
     *
     * @return
     */
    protected boolean isNeedLoadStatusBar() {
        return true;
    }

    private void loadStateBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个状态栏颜色
        tintManager.setStatusBarTintResource(getColorId());
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected int getColorId() {
        return mColorId;
    }


    @Override
    protected void onResume() {
        try {
            activeAcitity = this;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onResume();

    }

    @Override
    protected void onPause() {
        try {
            activeAcitity = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onPause();

    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {

        //当界面关闭时释放资源
        synchronized (BaseActivity.class) {
            if (cacheActivitys.containsKey(this.getClass().getName()))
                cacheActivitys.remove(this.getClass().getName());
        }

        super.onDestroy();

        /*销毁注解依赖*/
//        ButterKnife.unbind(this);

        /*销毁View中相关内容*/
        destroyView();

    }

    /**
     * 初始化信息"通用信息"
     */
    private void init() {

        synchronized (BaseActivity.class) {
            // 缓存已打开的Activity
            cacheActivitys.put(this.getClass().getName(), this);
        }
    }

    /**
     * 设置窗体<br/>
     * 例如：窗口样式、窗体全屏等
     */
    protected void setForm() {

    }

    /**
     * 绑定XML布局文件
     *
     * @return
     */
    protected abstract int getViewID();

    /**
     * 初始化View Data
     */
    protected void initViewData() {

    }

    /**
     * 销毁View Data
     */
    protected void destroyView() {

    }

    /**
     * 获取“上一个界面”传过来的数据
     */
    protected void getBundleValues() {

    }

    /**
     * 点击事件
     *
     * @param v 点击的控件
     */
    protected void onClick(View v) {

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);

            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // land
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // port
            }
        } catch (Exception ex) {
        }

    }

    /***
     * 通知更新UI
     */
    public void noticeUpdateUi(Bundle bundle) {
        Set<String> set = cacheActivitys.keySet();
        for (String key : set) {
            cacheActivitys.get(key).updateUi(bundle);
        }
    }

    /***
     * 更新UI
     */
    public void updateUi(Bundle bundle) {

    }


    public static boolean hasActivieActivity() {
        return activeAcitity != null;
    }

    /**
     * 验证当前activity是否在活动组中
     *
     * @param name this.getClass().getName()
     * @return
     */
    public static boolean hasActivity(String name) {
        Set<String> set = cacheActivitys.keySet();
        for (String key : set) {
            if (key.equals(name))
                return true;
        }

        return false;
    }

    public static boolean isActive(String name) {
        if (activeAcitity != null && activeAcitity.getClass().getName().equals(name))
            return true;
        else
            return false;
    }

    /**
     * 验证Class是否在  活动中
     */
    public static boolean isClassActive(Class<?> cla) {
        if (activeAcitity != null) {
            return activeAcitity.getClass().getName().equalsIgnoreCase(cla.getName());
//			return activeAcitity.getClass().getGenericSuperclass().toString().contains(cla.getName());
        } else
            return false;
    }

    /**
     * 获取当前活动中的activity
     */
    public static BaseActivity getActive() {
        return activeAcitity;
    }

    /**
     * 关闭全部activity
     */
    public static void finishAll() {
        Set<String> set = cacheActivitys.keySet();
        for (String key : set) {
            cacheActivitys.get(key).finish();
        }
        cacheActivitys.clear();
        activeAcitity = null;
    }

    /**
     * 关闭当前activity
     */
    public static void finishActive() {
        if (activeAcitity != null) {
            activeAcitity.finish();
        }
    }

    /** 提示 */
    protected void showShortText(String text){
        Toast.makeText(getApplicationContext(), text, EToast2.LENGTH_SHORT).show();
    }

    /** 提示 */
    protected void showShortText(int resId){
        Toast.makeText(getApplicationContext(), resId, EToast2.LENGTH_SHORT).show();
    }

    /** 新增Fragment */
    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    /** 替换/复位Fragment */
    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            transaction.commit();
        }
    }

    /** Android中App字体大小不随系统改变而改变 */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }
}
