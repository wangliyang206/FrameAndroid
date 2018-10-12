package com.cj.mobile.common.service;

import com.cj.mobile.common.util.SSLSocketClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

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
        /** 拦截器  给所有的请求添加消息头 */
//        Interceptor mInterceptor = new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
//                Request request = chain.request()
//                        .newBuilder()
//                        .addHeader("Accept-Encoding", "gzip, deflate")
//                        .build();
//                return chain.proceed(request);
//            }
//        };

        /** log拦截器  打印所有的 log */
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });
        /*NONE,       //不打印log
        BASIC,      //只打印 请求首行 和 响应首行
        HEADERS,    //打印请求和响应的所有 Header
        BODY        //所有数据全部打印*/
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)                                     // 请求超时
                .writeTimeout(30, TimeUnit.SECONDS)                                       // 写入超时
                .readTimeout(30, TimeUnit.SECONDS)                                        // 读取超时
                .addInterceptor(new Retry(3))                                            // 添加自定义拦截(网络请求，超时重试)
                .addInterceptor(interceptor)                                                        // 添加消息（log）拦截
//                .addNetworkInterceptor(new GzipRequestInterceptor())    //GZIP数据压缩
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())                            // 配置SSlSocketFactory
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())                            // 配置一个HostnameVerifier来忽略host验证
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
