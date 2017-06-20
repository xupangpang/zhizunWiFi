package com.zhizun.zhizunwifi.http;

import com.zhizun.zhizunwifi.bean.Decrypt;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * service统一接口数据
 * Created by WZG on 2016/7/16.
 */
public interface HttpVerService {
    //发送验证码
    @FormUrlEncoded
    @POST("?service=User.SendMark")
    Observable<Decrypt> SendMark(@FieldMap HashMap<String, String> map);

    //验证验证码
    @FormUrlEncoded
    @POST("?service=User.CheckMark")
    Observable<Decrypt> CheckMark(@FieldMap HashMap<String, String> map);
}