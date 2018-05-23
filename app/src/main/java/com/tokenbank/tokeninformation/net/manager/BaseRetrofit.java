package com.tokenbank.tokeninformation.net.manager;

import com.tokenbank.tokeninformation.net.UrlConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor)
            .connectTimeout(15, TimeUnit.SECONDS) //设置超时
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);  //错误重连

        return builder.build();
    }

    /**
     * 初始化retrofit
     */
    private static Retrofit getRetrofit() {
        //配置retrofit
        return new Retrofit.Builder().baseUrl(UrlConfig.BASE_URL)
            .client(getOkHttp())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}
