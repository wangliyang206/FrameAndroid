package com.cj.mobile.common.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @Title: BaseApplication
 * @Package com.cj.mobile.common.base
 * @Description: Base Application for Application(应用基础)
 * @author: wly
 * @date: 2016/12/3 17:51
 */
public class BaseApplication extends Application {

    private static BaseApplication ourInstance = new BaseApplication();

    public static BaseApplication getInstance() {
        return ourInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;

    }
}

