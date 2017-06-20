package com.zhizun.zhizunwifi.http;



import com.zhizun.zhizunwifi.utils.LogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http交互处理类
 * Created by xupp on 2016/7/16.
 */
public class HttpManager {
    //是否是测试环境
    public static final boolean debug = false;

    /*基础url*/
    public static final String VERIFICATION_URL = "http://api.user.xiaohua.com/User3/";
    public static final String BASE_URL_DEBUG = "http://tapi.wifi.anzhuo.com/";
    public static final String BASE_URL_RELEASE = "http://api.wifi.anzhuo.com/";
    public static final String BASE_URL = debug ? BASE_URL_DEBUG : BASE_URL_RELEASE;
    /*超时设置*/
    private static final int DEFAULT_TIMEOUT = 30;
    private static HttpService httpService;
    private static HttpVerService httpVerService;


    public static HttpService getService() {
        if (httpService == null) {
            //手动创建一个OkHttpClient并设置超时时间
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LogInterceptor())
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();


            httpService = new Retrofit.Builder()
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build().create(HttpService.class);
        }
        return httpService;
    }

    public static HttpVerService getVerService() {
        if (httpVerService == null) {
            //手动创建一个OkHttpClient并设置超时时间
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LogInterceptor())
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();


            httpVerService = new Retrofit.Builder()
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(VERIFICATION_URL)
                    .build().create(HttpVerService.class);
        }
        return httpVerService;
    }



}
