package com.cj.mobile.common.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 服务端接口工厂类
 */
public class RetrofitFactoty {
    private static String serverUrl = "";

    public static <T> T getService(Class<T> clazz, String url) {
        serverUrl = url;
        Retrofit retrofit = createRetrofitService();
        return retrofit.create(clazz);
    }

    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     */
    public static Retrofit createRetrofitService() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())//添加消息拦截
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }
}
