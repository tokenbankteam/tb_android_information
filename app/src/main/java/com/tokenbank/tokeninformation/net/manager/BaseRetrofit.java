package com.tokenbank.tokeninformation.net.manager;

import com.tokenbank.tokeninformation.net.UrlConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class BaseRetrofit {
    //请求的接口对象
    private static HttpService service;
    //是否缓存
    private boolean isUseCache;
    private int maxCacheTime = 60;

    public void setMaxCacheTime(int maxCacheTime) {
        this.maxCacheTime = maxCacheTime;
    }

    public void setUseCache(boolean useCache) {
        this.isUseCache = useCache;
    }

    /**
     * 创建HttpService
     *
     * @return
     */
    public static HttpService getService() {
        if (service == null) {
            synchronized (BaseRetrofit.class) {
                if (service == null) {
                    service = getRetrofit().create(HttpService.class);
                }
            }
        }
        return service;
    }

    /**
     * 初始化配置OkHttpClient
     */
    private static OkHttpClient getOkHttp() {
        //获取build
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //打印日志
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(loggingInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    /**
     * 初始化retrofit
     */
    private static Retrofit getRetrofit() {
        //配置retrofit
        return new Retrofit.Builder()
                .baseUrl(UrlConfig.BASE_URL)
                .client(getOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

}
