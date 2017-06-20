package com.zhizun.zhizunwifi.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.zhizun.zhizunwifi.bean.NetWorkSpeedInfo;

import android.util.Log;

public class ReadFile {

	public boolean isStop = false;

	public byte[] getFileFromUrl(String url, NetWorkSpeedInfo netWorkSpeedInfo, int...testTime) {
		int currentByte = 0;
		int fileLength = 0;
		long startTime = 0;
		long intervalTime = 0; // 间隔时间

		byte[] b = null;

		int bytecount = 0;
		URL urlx = null;
		URLConnection con = null;
		InputStream stream = null;
		try {
			Log.d("URL:", url);
			urlx = new URL(url);
			con = urlx.openConnection();
			con.setConnectTimeout(20000);
			con.setReadTimeout(20000);
			fileLength = con.getContentLength();
			stream = con.getInputStream();
			netWorkSpeedInfo.totalBytes = fileLength;
			b = new byte[fileLength];
			netWorkSpeedInfo.hadFinishedBytes = 0;
			startTime = System.currentTimeMillis();
			while ((currentByte = stream.read()) != -1) {
				netWorkSpeedInfo.hadFinishedBytes++;
//				Log.d("tag","currentByte=   "+currentByte);
//				Log.d("tag","netWorkSpeedInfo.hadFinishedBytes=   "+netWorkSpeedInfo.hadFinishedBytes);
				intervalTime = System.currentTimeMillis() - startTime;
				if (intervalTime == 0) {
					netWorkSpeedInfo.speed = 1024;
				} else {
					netWorkSpeedInfo.speed = (netWorkSpeedInfo.hadFinishedBytes / intervalTime) * 1000;
//					Log.d("tag",netWorkSpeedInfo.hadFinishedBytes+" / "+ intervalTime + " * "+1000+"  =  "+netWorkSpeedInfo.speed);
				}
				if (bytecount < fileLength) {
					b[bytecount++] = (byte) currentByte;
				}
				if(isStop || (testTime != null && testTime.length > 0 && intervalTime - testTime[0] >= 0)){
					stream.close();
					break;
				}
			}
		} catch (Exception e) {
			Log.e("exception : ", e.getMessage() + "");
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (Exception e) {
				Log.e("exception : ", e.getMessage());
			}

		}
		return b;
	}

}
