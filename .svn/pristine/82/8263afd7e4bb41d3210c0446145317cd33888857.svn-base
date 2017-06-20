package com.zhizun.zhizunwifi.utils;

import android.annotation.SuppressLint;
import android.os.Environment;


/**
 * @ClassName: BmobConstants
 * @Description: TODO
 * @author smile
 * @date 2014-6-19 下午2:48:33
 */
@SuppressLint("SdCardPath")
public class Constants {

	/**
	 * 自动更新接口
	 */
	public static final String UPDATA_VERSION = "http://wifiapp.anzhuo.com/wifi_json/updata_version.php";


	/**
	 * 网络数据接口
	 *
	 */
	/**
	 * 会员注册
	 */
	public static final String USER_REGIST_URL = "http://wifiapp.anzhuo.com/wifi_json/reg.php";
	/**
	 * 会员登陆
	 */
	public static final String USER_LOGIN_URL = "http://wifiapp.anzhuo.com/wifi_json/login.php";
	/**
	 * 会员签到
	 */
	public static final String USER_SIGN_URL = "http://wifiapp.anzhuo.com/wifi_json/user_sign.php";
	/**
	 * 会员修改昵称
	 */
	public static final String ALTER_NICKNAME_URL = "http://wifiapp.anzhuo.com/wifi_json/user_edit_nc.php";
	/**
	 * 会员修改性别
	 */
	public static final String ALTER_GENDER_URL = "http://wifiapp.anzhuo.com/wifi_json/user_edit_sex.php";
	/**
	 * 文件上传接口
	 */
	public static final String FILE_UPLOAD_URL = "http://wifiapp.anzhuo.com/wifi_json/upload.php";
	/**
	 * 更新头像接口
	 */
	public static final String UPDATE_USER_ICON_URL = "http://wifiapp.anzhuo.com/wifi_json/user_edit_img.php";
	/**
	 * 获取免费wifi接口
	 * 参数：macinfo(mac地址集以 , 格开)
	 */
	public static final String FREE_WIFI_URL = "http://wifiapp.anzhuo.com/wifi_json/wifi_out_all.php";
	/**
	 * 分享wifi接口
	 * 参数：user_name(用户名)
	 * 参数：wifi_name(WiFi名称)
	 * 参数：wifi_psw(WiFi密码)
	 * 参数：wifi_mac_address(WiFi Mac地址)
	 * 参数：latitude(纬度)
	 * 参数：longitude(经度)
	 * 参数：sign_type(签名方式)
	 */
	public static final String SHARE_WIFI_URL = "http://wifiapp.anzhuo.com/wifi_json/wifi_get.php";

	/**
	 * sp文件名
	 */
	static final String SHAREDNAME = "zzwifi";

	/**
	 * 存放发送图片的目录
	 */
	public static String BMOB_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/zhizun/image/";

	/**
	 * 我的头像保存目录
	 */
	public static String MyAvatarDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhizun/avatar/";
	/**
	 * 拍照回调
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像

	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
	public static final String EXTRA_STRING = "extra_string";

	public static final long SPLASH_TIME = 3000;//开屏停留时间 单位毫秒


	public static final String ACTION_REGISTER_SUCCESS_FINISH ="register.success.finish";//注册成功之后登陆页面退出
}
