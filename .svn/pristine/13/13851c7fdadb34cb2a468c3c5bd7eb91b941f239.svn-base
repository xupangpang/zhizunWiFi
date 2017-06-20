package com.zhizun.zhizunwifi.utils;

import android.util.Log;

import com.zhizun.zhizunwifi.http.HttpManager;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * @author xupp
 * @date 2017/3/13
 */

public class LogInterceptor implements Interceptor {

    public static String TAG = "LogInterceptor";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        $Log(TAG,"\n");
        $Log(TAG,"-----------------Start----------------");
        $Log(TAG, "| "+request.toString());
        String method=request.method();
        if("POST".equals(method)){
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                Log.d(TAG, "| RequestParams:{"+sb.toString()+"}");
            }
        }
        $Log(TAG, "| Response:" + content);
        $Log(TAG,"----------End:"+duration+"毫秒----------");
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    /**
     * [日志输出]
     *
     * @param msg
     */
    protected void $Log(String tag,String msg) {
        if (HttpManager.debug) {
            Log.d(tag, msg);
        }
    }
}
