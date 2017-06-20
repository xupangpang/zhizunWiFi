package com.zhizun.zhizunwifi.http;

import com.squareup.okhttp.ResponseBody;
import com.zhizun.zhizunwifi.bean.BussCooper;
import com.zhizun.zhizunwifi.bean.GetAdvice;
import com.zhizun.zhizunwifi.bean.InstallManger;
import com.zhizun.zhizunwifi.bean.JsonWifiImp;
import com.zhizun.zhizunwifi.bean.JsonWifiMain;
import com.zhizun.zhizunwifi.bean.Login;
import com.zhizun.zhizunwifi.bean.PutAccount;
import com.zhizun.zhizunwifi.bean.Register;
import com.zhizun.zhizunwifi.bean.StoreMsg;
import com.zhizun.zhizunwifi.bean.UserHead;
import com.zhizun.zhizunwifi.bean.UserSign;
import com.zhizun.zhizunwifi.bean.Version;
import com.zhizun.zhizunwifi.bean.WiFiMap;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * service统一接口数据
 * Created by WZG on 2016/7/16.
 */
public interface HttpService {

    //上传wifi密码
    @FormUrlEncoded
    @POST("?service=Wifi.putAccount")
    Observable<BaseResultEntity<PutAccount>> putAccount(@Field("data") String data,
                                                        @Field("T") String t);

    //获取发现网页地址
    @FormUrlEncoded
    @POST("?service=Wifi.GetAdvice")
    Observable<BaseResultEntity<GetAdvice>> GetAdvice(@Field("channel") String data,
                                                                    @Field("flag") String flag);

    //获取更新信息
    @FormUrlEncoded
    @POST("?service=Wifi.Version")
    Observable<BaseResultEntity<Version>> Version(@Field("app_name") String app_name,
                                                  @Field("version") String version,
                                                  @Field("flag") String flag);


    //上传用户头像
    @Multipart
    @POST("?service=Wifi.UploadUserHead")
    Observable<BaseResultEntity<UserHead>> UploadUserHead(@Part("file\"; filename=\"image_head.png\"") RequestBody file,
                                                          @Part("user") RequestBody description);


    //上传用户头像
    @Multipart
    @POST("?service=Wifi.UploadUserHead")
    Observable<BaseResultEntity<UserHead>> UploadUserHeadOpen(@Part("file\"; filename=\"image_head.png\"") RequestBody file,
                                                          @Part("openid") RequestBody description);

    //用户注册
    @FormUrlEncoded
    @POST("?service=Wifi.UserRegister")
    Observable<BaseResultEntity<Register>> UserRegister(@Field("user") String user,
                                                        @Field("passwd") String passwd);


    //用户登录
    @FormUrlEncoded
    @POST("?service=Wifi.UserLogin")
    Observable<BaseResultEntity<Login>> UserLogin(@Field("user") String user,
                                                  @Field("passwd") String passwd);

    //上传用户信息
    @FormUrlEncoded
    @POST("?service=Wifi.UploadUserInfo")
    Observable<BaseResultEntity<PutAccount>> UploadUserInfo(@Field("user") String user,
                                                            @Field("openid") String openid,
                                                            @Field("name") String name,
                                                            @Field("sex") String sex);

    //签到
    @FormUrlEncoded
    @POST("?service=Wifi.UserSign")
    Observable<BaseResultEntity<UserSign>> UserSign(@Field("user") String user,
                                                    @Field("openid") String openid);


    //向服务器发送用户连接成功
    @FormUrlEncoded
    @POST("?service=Wifi.ConnectSuccess")
    Observable<BaseResultEntity<PutAccount>> ConnectSuccess(@Field("mac") String user);


    //推送开关
    @FormUrlEncoded
    @POST("?service=Wifi.MessagePush")
    Observable<BaseResultEntity<PutAccount>> MessagePush(@Field("getui") String user,
                                                         @Field("flag") String flag);

    //第三方登录
    @FormUrlEncoded
    @POST("?service=User.UserOAuth")
    Observable<BaseResultEntity<Login>> UserOAuth(@Field("openid") String openid,
                                                  @Field("name") String name,
                                                  @Field("head") String head,
                                                  @Field("sex") String sex);

    //获取可破解的wifi信息
    @FormUrlEncoded
    @POST("?service=Wifi.GetAccount")
    Observable<BaseResultEntity<JsonWifiMain>> GetAccount(@Field("router") String router,
                                                          @Field("lon") String lon,
                                                          @Field("lat") String lat);

    //获取ROOT软件信息
    @GET("?service=apps.Root")
    Observable<BaseResultEntity<List<InstallManger>>> Root();


    //反馈信息
    @FormUrlEncoded
    @POST("?service=Wifi.UserFeedback")
    Observable<BaseResultEntity> UserFeedback(@Field("content") String content);

    //获取周围wifi信息
    @FormUrlEncoded
    @POST("?service=Wifi.getSsidFromPos")
    Observable<BaseResultEntity<List<WiFiMap>>> getSsidFromPos(@Field("lat") String lat,
                                                         @Field("lon") String lon);


    //获取wifi商家信息
    @FormUrlEncoded
    @POST("?service=Wifi.GetBusinessInfo")
    Observable<BaseResultEntity<List<StoreMsg>>> GetBusinessInfo(@Field("data") String data);

    //根据用户名查询其提交的wifi的审核状态
    @FormUrlEncoded
    @POST("?service=Wifi.CheckUserWifiStatus")
    Observable<BaseResultEntity<List<BussCooper>>> CheckUserWifiStatus(@Field("user") String user);

    //反馈信息
    @Multipart
    @POST("?service=Wifi.PutBusinessInfo")
    Observable<BaseResultEntity> PutBusinessInfo(@Query("lat") String lat,
                                                 @Query("lon") String lon,
                                                 @Query("router") String router,
                                                 @Query("phone") String phone,
                                                 @Query("passwd") String passwd,
                                                 @Query("username") String username,
                                                 @Query("ssid") String ssid,
                                                 @Query("name") String name,
                                                 @Query("address") String address,
                                                 @Query("type") String type,
                                                 @Query("detailAdress") String detailAdress,
                                                 @Part("file1\";filename=\"file1.png\"") RequestBody file1,
                                                 @Part("file2\";filename=\"file2.png\"") RequestBody file2,
                                                 @Part("file3\";filename=\"file3.png\"") RequestBody file3
                                                 );

    //忘记密码
    @FormUrlEncoded
    @POST("?service=Wifi.ResetPasswd")
    Observable<BaseResultEntity> ResetPasswd(@Field("user") String user,@Field("passwd") String passwd);


    //获取wifi信息,包括ssid,密码,经纬度
    @FormUrlEncoded
    @POST("?service=Wifi.GetWifi")
    Observable<BaseResultEntity<List<JsonWifiImp>>> GetWifi(@Field("router") String openid,
                                                               @Field("ssid") String ssid,
                                                               @Field("lon") String lon,
                                                               @Field("lat") String lat);


    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
}