package com.zhizun.zhizunwifi;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhizun.zhizunwifi.service.LocationService;
import com.zhizun.zhizunwifi.utils.SystemUtil;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.ioc.IocContainer;

import org.litepal.LitePalApplication;

import java.util.List;

public class MainApplication extends LitePalApplication {

	public static MainApplication INSTANCE;

	public LocationService locationService;
	public Vibrator mVibrator;

	private boolean isDownload;

	public static final String SHAREDNAME = "zhizunWifi_shared";

	@Override
	public void onCreate() {
		super.onCreate();

		/*// 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
		// 调试时，将第三个参数改为true
		Bugly.init(this, "017f3fa4fc", true);*/

		UMShareAPI.get(this);

		isDownload = false;

		String processName = getProcessName(this, android.os.Process.myPid());
		if (processName != null) {
			boolean defaultProcess = processName.equals(getPackageName());
			if (defaultProcess) {
				initAppForMainProcess();
			} else if (processName.contains(":remote")) {
				// 百度定位service
				return;
			} else if (processName.contains(":bdservice_v1")){
				// 百度推送service
				return;
			}
		}
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				// TODO Auto-generated method stub
				Log.e("apptbs", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub

			}
		};
		QbSdk.setTbsListener(new TbsListener() {
			@Override
			public void onDownloadFinish(int i) {
				Log.d("apptbs","onDownloadFinish");
			}

			@Override
			public void onInstallFinish(int i) {
				Log.d("apptbs","onInstallFinish");
			}

			@Override
			public void onDownloadProgress(int i) {
				Log.d("apptbs","onDownloadProgress:"+i);
			}
		});

		QbSdk.initX5Environment(getApplicationContext(),  cb);

		PlatformConfig.setWeixin("wxd4a1900b8b9f9880", "da2125f842d582d67961e7039195f232");
		PlatformConfig.setSinaWeibo("4084745817", "17fa6fba6d3a93224c825a04601e5282","http://api.wifi.anzhuo.com/");
		PlatformConfig.setQQZone("1105170697", "bQ3TMyyd60SyTBDX");

		SystemUtil.LoadDb(getApplicationContext(), "macbrand.db", R.raw.wifi);
	}

	public static String getProcessName(Context cxt, int pid) {
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (RunningAppProcessInfo procInfo : runningApps) {
			if (procInfo.pid == pid) {
				return procInfo.processName;
			}
		}
		return null;
	}

	public void initAppForMainProcess() {
		INSTANCE = this;

		/***
		 * 初始化定位sdk，建议在Application中创建
		 */
		locationService = new LocationService(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		//CrashHandler.getInstance().init(this);//UncaughtException异常处理器

//      WriteLog.getInstance().init(); // 初始化日志

		// 一些常量的配置
		Const.netadapter_page_no = "p";
		Const.netadapter_step = "step";
		Const.response_data = "data";
		Const.netadapter_step_default = 7;
		Const.netadapter_json_timeline = "pubdate";
		Const.DATABASE_VERSION = 20;
		Const.net_pool_size = 30;
		Const.net_error_try = true;
		IocContainer.getShare().initApplication(this);
	}

	public static MainApplication getInstance() {
		return INSTANCE;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	/*@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		// you must install multiDex whatever tinker is installed!
		MultiDex.install(base);


		// 安装tinker
		Beta.installTinker();
	}*/


}
