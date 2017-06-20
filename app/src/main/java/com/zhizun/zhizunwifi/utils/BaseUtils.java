package com.zhizun.zhizunwifi.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhizun.zhizunwifi.bean.JsonWifi;
import com.zhizun.zhizunwifi.bean.Vendor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseUtils {

	static final String FOLDER = "/CoochuaCatch/";
	static int ViewWidth = 0;
	static int ViewHeight = 0;

	public static void closeInputMethod(View view, Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(view,InputMethodManager.SHOW_FORCED); //强制显示
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘

	}

	public static void setSharedPreferences(String key, String value,
											Context context) {

		Editor sharedata = context.getSharedPreferences(Constants.SHAREDNAME, Activity.MODE_PRIVATE).edit();
		sharedata.putString(key, value);
		sharedata.commit();
	}

	public static void setSharedPreferences(String key, int value,
											Context context) {

		Editor sharedata = context.getSharedPreferences(Constants.SHAREDNAME, Activity.MODE_PRIVATE).edit();
		sharedata.putInt(key, value);
		sharedata.commit();
	}

	public static int getIntSharedPreferences(String key, Context context) {

		try{
			SharedPreferences sharedata = context.getSharedPreferences(Constants.SHAREDNAME,
					Activity.MODE_PRIVATE);
			int data = sharedata.getInt(key, 0);
			return data;
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}

	}

	public static String getSharedPreferences(String key, Context context) {

		try{
			SharedPreferences sharedata = context.getSharedPreferences(Constants.SHAREDNAME,
					Activity.MODE_PRIVATE);
			String data = sharedata.getString(key, "");
			return data;
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 文件缓存路径
	 *
	 * @param username
	 *            文件名
	 * @param pattrn
	 *            文件类型
	 * @return
	 */
	public static String getSDPath(String username, String pattrn) {

		System.out.println(getSDRoot() + FOLDER + username + pattrn
				+ "----------");
		return getSDRoot() + FOLDER + username + pattrn;
	}

	public static String getSDRoot(){
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		return sdDir.toString();
	}
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存btmap至png图片
	 *
	 * @param mBitmap
	 * @return
	 * @throws IOException
	 */
	public static String saveMyBitmap(Bitmap mBitmap, String username,
									  String pattrn) throws IOException {
		String filePath = getSDPath(username, pattrn);
		System.out.println("filePath=" + filePath);
		File f = new File(filePath);
		if (!f.getParentFile().exists()) {
			f.mkdirs();
		}
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
		}
		try {
			fOut.close();
		} catch (IOException e) {
		}
		return filePath;
	}

	/**
	 * 将图片转换成bitmap
	 *
	 * @param f
	 * @return
	 */
	public static Bitmap decodeFile(File f) {
		Bitmap b = null;
		int IMAGE_MAX_SIZE = 300;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (Exception e) {
		}
		return b;
	}

	// 将bitmap裁剪成圆形
	public static Bitmap makeRoundCorner(Bitmap bitmap) {
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int left = 0, top = 0, right = width, bottom = height;
			float roundPx = height / 2;
			if (width > height) {
				left = (width - height) / 2;
				top = 0;
				right = left + height;
				bottom = height;
			} else if (height > width) {
				left = 0;
				top = (height - width) / 2;
				right = width;
				bottom = top + width;
				roundPx = width / 2;
			}
			Bitmap output = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(left, top, right, bottom);
			RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}
		return null;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取当前系统版本
	 * 18-》4.3
	 * 16-》4.1
	 *  11-》android 3.0
	 *  8-》2.2
	 * @return
	 */
	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {

		}
		System.out.println("version="+version);
		return version;
	}

	/**
	 * 判断当前系统用的语言
	 * @param context
	 * @return
	 */

	@SuppressLint("DefaultLocale")
	public static boolean isCN(Context context){
		boolean flag=false;
		String able= context.getResources().getConfiguration().locale.getCountry();
		if(able.toUpperCase().contains("CN")){
			flag=true;
		}
		return flag;
	}

	/**
	 * 读取数据流
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}


	/**
	 * 把一个流的内容读取出来转化成 字符串
	 * @param is
	 * @return 解析成功返回字符串 解析失败返回null
	 */
	public static String readStreamtoString(InputStream is){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
			}
			is.close();
			return baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 从btye中 转换 bitmp
	 * @param bytes
	 * @param opts
	 * @return
	 */
	public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 * 图片按比例大小压缩方法（根据Bitmap图片压缩）
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 80) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			Log.e("lxf", "压缩一次");
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 * @param context Context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected())
			{
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED)
				{
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}

	//是否连接WIFI
	public static boolean isWifiConnected(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifiNetworkInfo.isConnected())
		{
			return true ;
		}

		return false ;
	}



	/**
	 * 移动数据开启和关闭
	 * @param context
	 * @param enabled
	 */
	public static void setMobileDataStatus(Context context, boolean enabled)

	{

		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// ConnectivityManager类

		Class<?> conMgrClass = null;

		// ConnectivityManager类中的字段
		Field iConMgrField = null;
		// IConnectivityManager类的引用
		Object iConMgr = null;
		// IConnectivityManager类
		Class<?> iConMgrClass = null;
		// setMobileDataEnabled方法
		Method setMobileDataEnabledMethod = null;
		try {

			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象Mservice
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());

			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);

			// 设置setMobileDataEnabled方法是否可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);

		}

		catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (NoSuchFieldException e) {

			e.printStackTrace();
		}

		catch (SecurityException e) {
			e.printStackTrace();

		} catch (NoSuchMethodException e)

		{
			e.printStackTrace();
		}

		catch (IllegalArgumentException e) {

			e.printStackTrace();
		}

		catch (IllegalAccessException e) {

			e.printStackTrace();
		}

		catch (InvocationTargetException e)

		{

			e.printStackTrace();

		}

	}


	/**
	 * 获取移动数据开关状态
	 * @param context
	 * @param getMobileDataEnabled
	 * @return
	 */
	public static boolean getMobileDataStatus(Context context, String getMobileDataEnabled)
	{

		ConnectivityManager cm;

		cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		Class cmClass = cm.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;
		Boolean isOpen = false;
		try {

			Method method = cmClass.getMethod(getMobileDataEnabled, argClasses);

			isOpen = (Boolean) method.invoke(cm, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen;

	}


	/**
	 * 判断手机号格式
	 * @param mobiles
	 * 			需要判断的号码
	 * @return
	 */
	public  static  boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * @return
	 */
	public static String date2TimeStamp(String date_str){
		try {
			String signTime= date_str+" 23:59:59";
			System.out.println("date2TimeStamp===="+signTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("---------------"+String.valueOf(sdf.parse(signTime).getTime()/1000));
			return String.valueOf(sdf.parse(signTime).getTime()/1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";


	}

	/**
	 * 通过路径取文件名
	 * @param pathandname
	 * @return
	 */
	public static String getFileName(String pathandname){

		int start=pathandname.lastIndexOf("/");
		int end=pathandname.lastIndexOf(".");
		if(start!=-1 && end!=-1){
			return pathandname.substring(start+1,end);
		}else{
			return null;
		}

	}

	/**
	 * 遍历目录查找文件
	 * @param Path
	 * @param Extension
	 * @param IsIterative
	 */
	public static String getFiles(String Path, String Extension,boolean IsIterative) //搜索目录，扩展名，是否进入子文件夹
	{
		File[] files =new File(Path).listFiles();

		for (int i =0; i < files.length; i++)
		{
			File f = files[i];
			if (f.isFile())
			{
				if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) //判断扩展名
					return f.getPath();

				if (!IsIterative)
					break;
			}
			else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) //忽略点文件（隐藏文件/文件夹）
				getFiles(f.getPath(), Extension, IsIterative);
		}
		return null;
	}

	/**
	 * 借助Xutils框架 文件下载
	 */
	public static void download(String savePath,String downLoadUrl,RequestCallBack<File> requestCallBack){
		HttpUtils http = new HttpUtils();
		http.download(downLoadUrl, savePath, true, false, requestCallBack);

	}

	/**
	 * 删除文件
	 */
	public static void delFile(String filePath){
		File file = new File(filePath);
		if(file.isFile()){
			file.delete();
		}
		file.exists();
	}



	/**
	 * 获取应用程序的版本名称
	 * @param context 上下文
	 * @return 版本名称
	 */
	public static String getAppVersionName(Context context){
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo  packinfo = pm.getPackageInfo(context.getPackageName(), 0);
			String versionName = packinfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			//根本不可能到达
			//can't reach
			return "";
		}
	}
	/**
	 * 获取应用程序的版本名称
	 * @param context 上下文
	 * @return 版本号
	 */
	public static int getAppVersionCode(Context context){
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo  packinfo = pm.getPackageInfo(context.getPackageName(), 0);
			int versionCode = packinfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName =
				(String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	//获取手机网络连接状态
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null&& info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
// TODO: handle exception
			Log.v("error",e.toString());
		}
		return false;
	}


	/**
	 * uri 转换为  SD卡实际地址
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	public static String getMacAddress(final Context context){
		WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		return m_szWLANMAC;
	}

	/**
	 * 获取两个List的不同元素
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List<JsonWifi> getDiffrent(List<JsonWifi> list1, List<JsonWifi> list2) {
		long st = System.nanoTime();
		List<JsonWifi> diff = new ArrayList<JsonWifi>();
		List<JsonWifi> maxList = list1;
		List<JsonWifi> minList = list2;
		if(list2.size()>list1.size())
		{
			maxList = list2;
			minList = list1;
		}
		Map<JsonWifi,Integer> map = new HashMap<JsonWifi,Integer>(maxList.size());
		for (JsonWifi string : maxList) {
			map.put(string, 1);
		}
		for (JsonWifi string : minList) {
			if(map.get(string)!=null)
			{
				map.put(string, 2);
				continue;
			}
			diff.add(string);
		}
		for(Map.Entry<JsonWifi, Integer> entry:map.entrySet())
		{
			if(entry.getValue()==1)
			{
				diff.add(entry.getKey());
			}
		}
		return diff;

	}



}
