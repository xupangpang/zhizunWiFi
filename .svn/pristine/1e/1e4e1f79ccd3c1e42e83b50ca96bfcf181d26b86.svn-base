package com.zhizun.zhizunwifi.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 *
 * @项目名:	ZhizunWIFI
 * @包名:	com.zhizun.zhizunwifi.utils
 * @类名:	HttpConnections
 * @创建者:	梁辉
 * @创建时间:	2016-6-25 上午10:46:29
 * @描述:	网络连接工具类
 *
 * @SVN版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public abstract class HttpConnections {

	/**
	 * 带参数的异步POST网络请求
	 *
	 * @param requestUrl
	 *            请求的URL
	 * @param ParamsMap
	 *            请求的参数
	 */

	public void DoPostRequest(String requestUrl,
							  HashMap<String, Object> ParamsMap) {

		HttpUtils httpPost = new HttpUtils(5000);
//		httpPost.configCurrentHttpCacheExpiry(1000 * 5);// 设置超时时间

		RequestParams params = new RequestParams();
		Iterator it = ParamsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			System.out.println(key + "----" + value);
			params.addBodyParameter(key.toString(), value.toString());
		}

		httpPost.send(HttpMethod.POST, requestUrl, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						// 请求成功
						requestFailure(error, msg);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 请求失败
						requestSuccess(responseInfo.result);

					}
				});
	}


	/**
	 * 空参的异步POST网络请求
	 *
	 * @param requestUrl
	 *            请求的URL
	 *
	 */

	public void DoPostRequest(String requestUrl) {

		HttpUtils httpPost = new HttpUtils(5000);
//		httpPost.configCurrentHttpCacheExpiry(1000 * 5);// 设置超时时间

		httpPost.send(HttpMethod.POST, requestUrl,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						// 请求成功
						requestFailure(error, msg);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 请求失败
						System.out.println("onSuccess-------"
								+ responseInfo.result);
						requestSuccess(responseInfo.result);

					}
				});
	}



	/**
	 * 带参数的异步Get网络请求
	 *
	 * @param requestUrl
	 *            请求的URL
	 * @param ParamsMap
	 *            请求的参数
	 */

	public void DoGetRequest(String requestUrl,
							 HashMap<String, Object> ParamsMap) throws Exception {

		HttpUtils httpGet = new HttpUtils(5000);
//		httpGet.configCurrentHttpCacheExpiry(1000 * 5);// 设置超时时间

		RequestParams requestParams = new RequestParams();
		Iterator it = ParamsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			requestParams.addQueryStringParameter(entry.getKey().toString(),
					entry.getValue().toString());
		}
		httpGet.send(HttpMethod.GET, requestUrl, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 请求成功
						System.out.println("onSuccess-------"
								+ responseInfo.result);
						requestSuccess(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// 请求失败
						requestFailure(error, msg);
					}
				});

	}


	/**
	 * 空参的异步Get网络请求
	 *
	 * @param requestUrl
	 *            请求的URL
	 */

	public void DoGetRequest(String requestUrl) throws Exception {

		HttpUtils httpGet = new HttpUtils(5000);
//		httpGet.configCurrentHttpCacheExpiry(1000 * 5);// 设置超时时间

		httpGet.send(HttpMethod.GET, requestUrl,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 请求成功
						System.out.println("onSuccess-------"
								+ responseInfo.result);
						requestSuccess(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// 请求失败
						requestFailure(error, msg);
					}
				});

	}


	public abstract void requestFailure(HttpException arg0, String arg1);

	public abstract void requestSuccess(String json);
}