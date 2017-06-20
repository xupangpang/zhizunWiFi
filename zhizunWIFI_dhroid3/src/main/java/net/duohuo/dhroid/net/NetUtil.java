package net.duohuo.dhroid.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.duohuo.dhroid.util.MD5;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
/**
 * 网络访问
 * @author duohuo-jinghao
 *
 */
public class NetUtil {
	
	static StringBuffer md5;
	
	/**
	 * 同步网络访问数据
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String sync(String url, String method,
			Map<String, Object> params) throws IOException {
		StringBuffer sb = null;
		InputStream in = syncStream(url, method, params);
		if (in != null) {
			Scanner scanner = new Scanner(in);
			sb = new StringBuffer();
			while (scanner.hasNext()) {
				sb.append(scanner.nextLine());
			}
			in.close();
			scanner.close();
			return new String(sb);
		}
		return null;

	}

	/**
	 * 获取同步获取网络流
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static InputStream syncStream(String url, String method,
			Map<String, Object> params) throws IOException {
		md5 = new StringBuffer();
		HttpResponse response;
		if (method.equalsIgnoreCase("POST")) {
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				if (params.get(key) != null) {
					formparams.add(new BasicNameValuePair(key, params.get(key)
							.toString()));
					
					// 拼接加密字符串
					md5.append("&" + key + "=" + params.get(key).toString());
				}
			}
			if(md5.length() > 0){
				md5.delete(0, 1); // 删除掉第一个"&"
				Log.e("duohuo_DhNet", "加密原型= " + md5.toString());
			}
			formparams.add(new BasicNameValuePair("sign", MD5.MD5_TWO(md5.toString())));
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
					"UTF-8");
			httppost.setEntity(entity);
			response =HttpManager.execute(httppost);
			Header[] headers=response.getHeaders("Set-Cookie");
			System.out.println(headers);
		} else {
			if (!url.contains("?")) {
				url += "?";
			}else{
				if(!url.endsWith("&")){
					url+="&";
				}
			}
			url=url + encodeUrl(params);
			HttpGet httpGet = new HttpGet(url);
			response = HttpManager.execute(httpGet);
		}
		HttpEntity rentity = response.getEntity();
		if (rentity != null) {
			return rentity.getContent();
		}
		return null;
	}
	
	
	
	/**
	 * 获取get时的url
	 * @param params
	 * @return
	 */
	public static String encodeUrl(Map<String, Object> params) {
		if (params == null || params.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String key : params.keySet()) {
			if (params.get(key) != null) {
				sb.append(key.trim()).append("=").append(URLEncoder.encode((params.get(key).toString())))
						.append("&");
			}
		}
		return sb.toString();
	}
}