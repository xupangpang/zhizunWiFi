package com.zhizun.zhizunwifi.utils;  

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.zhizun.zhizunwifi.bean.Vendor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class SystemUtil {
	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/com.zhizun.zhizunwifi/databases";
	private static final int BUFFER_SIZE = 400000;
	/**
	 *
	 */
	public static int dpTurnPx(Context context, int padding_in_dp){
	    final float scale = context.getResources().getDisplayMetrics().density;
	    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
	    return padding_in_px;
	}

	/**
	 * @Title: openDateBase
	 * @Description: TODO(copy wifi数据库放入data/data)
	 * @param @param mContext
	 * @param @param resId    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public static void LoadDb(Context mContext, String fileName, int resId) {
		openDateBase(mContext, DB_PATH + "/" + fileName, resId);
	}

	private static void openDateBase(Context mContext, String dbFile, int resId) {

		File file = new File(dbFile);
		if (!file.exists()) {
			// // 打开raw中得数据库文件，获得stream流
			InputStream stream = mContext.getResources().openRawResource(resId);
			try {

				// 将获取到的stream 流写入道data中
				FileOutputStream outputStream = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = stream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
				stream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}  