package com.cj.mobile.common.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * Activity 基类 (所有Activity用到同样的方法或参数时都在这里定义 例：隐藏标题栏、横屏竖屏展示)
 *
 * @author 王力杨
 */
public abstract class BaseActivity extends AppCompatActivity { //AppCompatActivity

    /**
     * 缓存所已打开的Activity
     */
    public static Map<String, BaseActivity> cacheActivitys = new HashMap<String, BaseActivity>();
    public static BaseActivity activeAcitity;

    private RequestManager mImgLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*设置窗体*/
        setForm();

        /*载入界面布局*/
        setContentView(getViewID());

        /*依赖注入*/
        ButterKnife.bind(this);

        /*提供对Bundle操作*/
        getBundleValues();

        /*初始化基本信息*/
        init();

        //初始化View数据
        initViewData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (useGlide())
        /*图片开始加载*/
            Glide.with(getApplicationContext()).onStart();
    }

    @Override
    protected void onResume() {
        try {
            activeAcitity = this;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onResume();

        if (useGlide())
            //图片恢复加载
            Glide.with(getApplicationContext()).resumeRequests();

    }

    @Override
    protected void onPause() {
        try {
            activeAcitity = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onPause();

        if (useGlide())
            //图片暂停加载
            Glide.with(getApplicationContext()).pauseRequests();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (useGlide())
        /*停止加载*/
            Glide.with(getApplicationContext()).onStop();
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

        if (useGlide())
            if (Util.isOnMainThread())
        /*取消图片请求*/
                Glide.with(getApplicationContext()).onDestroy();

        /*销毁注解依赖*/
//        ButterKnife.unbind(this);

        /*销毁View中相关内容*/
        destroyView();

        //如果要使用eventbus请将此方法返回true
        if (useEventBus())
            EventBus.getDefault().unregister(this);
    }

    /**
     * 是否使用eventBus,默认为(false)
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 是否使用Glide
     *
     * @return
     */
    protected boolean useGlide() {
        return false;
    }

    /**
     * 初始化信息"通用信息"
     */
    private void init() {

        synchronized (BaseActivity.class) {
            // 缓存已打开的Activity
            cacheActivitys.put(this.getClass().getName(), this);
        }

        //如果要使用eventbus请将此方法返回true
        if (useEventBus())
            //注册到事件主线
            EventBus.getDefault().register(this);
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

    /**
     * 获取一个图片加载管理器
     *
     * @return RequestManager
     */
    public synchronized RequestManager getImgLoader() {
        if (mImgLoader == null)
            mImgLoader = Glide.with(this);
        return mImgLoader;
    }
}
