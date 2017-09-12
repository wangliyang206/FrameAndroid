package com.cj.mobile.common.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cj.mobile.common.ui.swipeback.SwipeBackActivity;
import com.cj.mobile.common.ui.swipeback.SwipeBackLayout;
import com.cj.mobile.common.util.etoast2.EToast2;
import com.cj.mobile.common.util.etoast2.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可
 * OnCreate中配置如下：
 * initSwipeBack();
 * 主题：
 * android:theme="@style/ThemeSwipeBack"
 */
public abstract class BaseBackActivity extends SwipeBackActivity {
    private Fragment mFragment;
    /**
     * 缓存所已打开的Activity
     */
    public static Map<String, BaseBackActivity> cacheActivitys = new HashMap<String, BaseBackActivity>();
    public static BaseBackActivity activeAcitity;

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

        //初始化回退界面
        initSwipeBack(SwipeBackLayout.EDGE_LEFT, 50);
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
        synchronized (BaseBackActivity.class) {
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
     * 初始化 界面右滑动回退
     *
     * @param edgeFlages 方向,EDGE_LEFT、EDGE_RIGHT、EDGE_BOTTOM、EDGE_ALL
     */
    protected void initSwipeBack(int edgeFlages) {
        getSwipeBackLayout().setEdgeTrackingEnabled(edgeFlages);
        getSwipeBackLayout().setEdgeSize(this.getResources().getDisplayMetrics().widthPixels);
    }

    /**
     * 初始化 界面右滑动回退
     *
     * @param edgeFlages 方向,EDGE_LEFT、EDGE_RIGHT、EDGE_BOTTOM、EDGE_ALL
     * @param size       边缘大小
     */
    protected void initSwipeBack(int edgeFlages, int size) {
        getSwipeBackLayout().setEdgeTrackingEnabled(edgeFlages);
        getSwipeBackLayout().setEdgeSize(size);
    }

    /**
     * 初始化信息"通用信息"
     */
    private void init() {

        synchronized (BaseBackActivity.class) {
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
    public static BaseBackActivity getActive() {
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
     * 新增Fragment
     */
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

    /**
     * 替换/复位Fragment
     */
    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            transaction.commit();
        }
    }

    /**
     * 提示
     */
    protected void showShortText(String text) {
        Toast.makeText(getApplicationContext(), text, EToast2.LENGTH_SHORT).show();
    }

    /**
     * 提示
     */
    protected void showShortText(int resId) {
        Toast.makeText(getApplicationContext(), resId, EToast2.LENGTH_SHORT).show();
    }
}