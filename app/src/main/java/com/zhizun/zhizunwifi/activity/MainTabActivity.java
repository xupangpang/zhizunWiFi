package com.zhizun.zhizunwifi.activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.zhizun.zhizunwifi.MainApplication;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.adapter.FragmentActivityTabAdapter;
import com.zhizun.zhizunwifi.adapter.FragmentActivityTabAdapter.OnRgsExtraCheckedChangedListener;
import com.zhizun.zhizunwifi.bean.JsonWifi;
import com.zhizun.zhizunwifi.bean.JsonWifiMain;
import com.zhizun.zhizunwifi.bean.PutAccount;
import com.zhizun.zhizunwifi.bean.Version;
import com.zhizun.zhizunwifi.bean.wifiinfo;
import com.zhizun.zhizunwifi.dialog.AlertDialog;
import com.zhizun.zhizunwifi.fragment.ConnectFragment2;
import com.zhizun.zhizunwifi.fragment.NewsFragment;
import com.zhizun.zhizunwifi.fragment.SecurityFragment;
import com.zhizun.zhizunwifi.fragment.UserPanelFragment;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.service.LocationService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.DeviceUtil;
import com.zhizun.zhizunwifi.utils.JmTools;
import com.zhizun.zhizunwifi.utils.TranslucentStatusBarUtils;
import com.zhizun.zhizunwifi.utils.WifiManage;
import com.zhizun.zhizunwifi.utils.WifiUtils;
import com.zzad.media.Bean.DataBean;
import com.zzad.media.InterFace.BannerDataListener;
import com.zzad.media.StartSDK;
import com.zzad.media.view.BannerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainTabActivity extends FragmentActivity {

	protected static final int NEED_UPDATE = 1;

	TextView title;
	RadioGroup main_radio;
	RadioButton tab_wifilist, tab_news, /*tab_security, tab_tools,*/ tab_more;

	FragmentActivityTabAdapter tabAdapter;
	public List<Fragment> fragments = new ArrayList<Fragment>();

	ConnectFragment2 connectFragment = new ConnectFragment2();
	NewsFragment newsFragment = new NewsFragment();
	SecurityFragment securityFragment = new SecurityFragment();
	//	MoreFragment moreFragment = new MoreFragment();
	UserPanelFragment userPanelFragment = new UserPanelFragment();

	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener;

	public static List<wifiinfo> wifiInfos;
	private WifiManage wifiManage;

	public final static int SafetyInspectRequsetCode = 1;
	public final static String SafetyInspectActionDefault = "default";
	public final static String SafetyInspectActionNet = "net";
	private long exitTime = 0;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder mBuilder;
	private final int  DOWNLOAD = 1000;
	AlertDialog alertDialog;
	private HttpService apiService;
	protected CompositeSubscription mCompositeSubscription;
	private BannerView bannerView;
	private boolean isjupe = true;
	private List<JsonWifi>  oneLine = new ArrayList<>();
	private LocationService locationService;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createDownloadNotification();

		// 默认情况下，actionBar放在你的activity的顶部，且作为activity布局的一部分。设置成为覆盖模式后，actionBar相当于漂浮在activity之上，不干预activity的布局
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main_tab);

		TranslucentStatusBarUtils.transparencyBar(this);

		apiService = HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();


		PushManager.getInstance().initialize(this.getApplicationContext());

		onRgsExtraCheckedChangedListener = new OnRgsExtraCheckedChangedListener();
		initView();


		//showUpdateDialog("","","","");

		//checkVersion();

		//去检查更新   上线时打开
		if (BaseUtils.isConnect(this)) {
			checkVersion();
			getwifipsw();
		}

		   /*
            Banner广告，在相应Banner嵌入位置调用，必须在初始化之后调用
           */
		bannerView = (BannerView) findViewById(R.id.bannerView);
		StartSDK.getBannerData(this, new BannerDataListener() {
			@Override
			public void getDataFinish(List<DataBean> list) {
				bannerView.setDataForView(list);//为控件设置数据并展示
			}

			@Override
			public void getDataError(String error) {

			}

		});

		//内插屏广告，必须在初始化之后调用
		StartSDK.showInApp(this);

		//外插屏广告，必须在初始化之后调用
		StartSDK.showOutApp(this);




	}

	public void getwifipsw(){

		StringBuilder bssid = new StringBuilder();
		WifiUtils wifiList = new WifiUtils(this);
		for (ScanResult scanResult : wifiList.getScanResults()) {
			bssid.append(scanResult.BSSID).append(",");
		}
		String Bssid = "";

		if (!TextUtils.isEmpty(bssid)) {
			Bssid = bssid.toString().substring(0, bssid.length() - 1);
		}
		HttpService apiService =  HttpManager.getService();
		Subscription subscription = apiService.GetAccount(Bssid,BaseUtils.getSharedPreferences("lon",MainTabActivity.this),BaseUtils.getSharedPreferences("lat",MainTabActivity.this)).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<JsonWifiMain>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								e.printStackTrace();

							}

							@Override
							public void onNext(BaseResultEntity<JsonWifiMain> baseResultEntity) {
								if (baseResultEntity.ret == 200){
									List<JsonWifi>  jsonWifiList = new ArrayList<JsonWifi>();
									for (int i = 0; i < baseResultEntity.data.all.size(); i++) {
										JsonWifi jsonWifi = new JsonWifi();
										jsonWifi.setRouter(baseResultEntity.data.all.get(i).router);
										jsonWifi.setSsid(baseResultEntity.data.all.get(i).ssid);
										jsonWifi.setPasswd(baseResultEntity.data.all.get(i).passwd);
										jsonWifi.setLon(baseResultEntity.data.all.get(i).lon);
										jsonWifi.setLat(baseResultEntity.data.all.get(i).lat);
										jsonWifiList.add(jsonWifi);
									}

									oneLine = jsonWifiList;

									List<JsonWifi>  jsonWifit = BaseUtils.getDiffrent(jsonWifiList,DataSupport.findAll(JsonWifi.class));
									for (JsonWifi jsonWifi : jsonWifit) {
										jsonWifi.save();
									}
								}
							}
							@Override
							public void onStart() {
								super.onStart();
							}
						}

				);

		mCompositeSubscription.add(subscription);
	}

	public List<JsonWifi> getOneLine(){
		return  oneLine;
	}

	private void initView() {
		main_radio = (RadioGroup) findViewById(R.id.main_radio);
		tab_wifilist = (RadioButton) findViewById(R.id.tab_wifilist);
		tab_news = (RadioButton) findViewById(R.id.tab_news);
		tab_more = (RadioButton) findViewById(R.id.tab_more);
		fragments.add(connectFragment);
		fragments.add(newsFragment);
		fragments.add(userPanelFragment);
		tabAdapter = new FragmentActivityTabAdapter(
				this, fragments, R.id.tab_content, main_radio,title);
		tabAdapter.setOnRgsExtraCheckedChangedListener(onRgsExtraCheckedChangedListener);


			if (BaseUtils.isWifiConnected(this)){
				isjupe = false;
				tabAdapter.setCheckFrament(tab_news,1);
			}else {
				tabAdapter.setCheckFrament(tab_wifilist,0);
			}



	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		connectFragment = null;
		newsFragment = null;
//		securityFragment = null;
//		appWallFragment = null;
		userPanelFragment = null;
		mCompositeSubscription.unsubscribe();
		//9500清除图片缓存
		StartSDK.clearMemoryCache(this);
	}

	public void transformCheckFrament(){
		if (isjupe) {
			tabAdapter.showTab(1);
			tab_news.setChecked(true);
			isjupe = true;
		}

	}

	public void transformwifiFrament(){
		tabAdapter.showTab(0);
		tab_wifilist.setChecked(true);
		BaseUtils.setSharedPreferences("wifiChang","no",this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			boolean isGoBackWeb = false;
			if(tabAdapter.getCurrentFragment() instanceof NewsFragment){
				isGoBackWeb = ((NewsFragment) tabAdapter.getCurrentFragment()).goBack_webView(keyCode, event);
			}

			if(!isGoBackWeb){
				boolean isRemind = getConfigure();
				if(isRemind){
					//showExitDialog();
					if((System.currentTimeMillis()-exitTime) > 2000){
						Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
						exitTime = System.currentTimeMillis();
					} else {
						putwifipsd();
						finish();
						MobclickAgent.onKillProcess(this);
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					}
				}else {
					finish();
				}
			}
		}

		return false;
	}

	private void putwifipsd(){
		List<JsonWifi>  wifiList = DataSupport.findAll(JsonWifi.class);
		Gson gson = new Gson();
		String aa = gson.toJson(wifiList).toString();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("data",aa);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String time = System.currentTimeMillis()+"";

		Subscription subscription = apiService.putAccount(JmTools.encryptionEnhanced(time,jsonObject.toString()),time).subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<PutAccount>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onNext(BaseResultEntity<PutAccount> baseResultEntity) {

							}
							@Override
							public void onStart() {
								super.onStart();
							}
						}

				);
		mCompositeSubscription.add(subscription);
	}

	private boolean getConfigure() {
		SharedPreferences sharedata = getSharedPreferences(MainApplication.SHAREDNAME, 0);
		boolean isRemind = sharedata.getBoolean("act_wifi_exit_wiperswitch",true);
		return isRemind;
	}
	private void setConfigure(boolean isRemind) {
		Editor sharedata = getSharedPreferences(MainApplication.SHAREDNAME, 0).edit();
		sharedata.putBoolean("act_wifi_exit_wiperswitch", isRemind);
		sharedata.commit();
	}

	private void showExitDialog(){
		final Dialog dialog = new Dialog(MainTabActivity.this,R.style.Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_exit_from_home);

		final CheckBox cb = (CheckBox)dialog.findViewById(R.id.dlg_exit_from_home_checkbox_cb);

		Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setConfigure(!cb.isChecked());
				dialog.dismiss();
				finish();
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//$Log("tag","mainTab:::"+requestCode+"   "+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == SafetyInspectRequsetCode){
			if(resultCode == RESULT_OK){
				if(SafetyInspectActionNet.equals(data.getAction())){
					main_radio.check(R.id.tab_news);
				}
			}
		}
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Subscription subscription = apiService.MessagePush("getui","zhizunwifi").subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<PutAccount>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
							}

							@Override
							public void onNext(BaseResultEntity<PutAccount> baseResultEntity) {
								if (!baseResultEntity.data.result.equals("success")){
									PushManager.getInstance().stopService(MainTabActivity.this);
								}else {
									PushManager.getInstance().initialize(MainTabActivity.this);
								}

							}

							@Override
							public void onStart() {
								super.onStart();
							}
						}

				);
		mCompositeSubscription.add(subscription);


// -----------location config ------------
		if(locationService == null){
			locationService = new LocationService(MainTabActivity.this);
			//获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
			//注册监听
			int type = MainTabActivity.this.getIntent().getIntExtra("from", 0);
			if (type == 0) {
				locationService.setLocationOption(locationService.getDefaultLocationClientOption());
			} else if (type == 1) {
				locationService.setLocationOption(locationService.getOption());
			}
		}
		locationService.registerListener(mListener);
		if(!locationService.isStart())
			locationService.start();// 定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request

	}

	@Override
	protected void onStop() {
		super.onStop();
		locationService.unregisterListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
	}


	/*****
	 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {

				BaseUtils.setSharedPreferences("address", location.getAddress().address, MainTabActivity.this);
				BaseUtils.setSharedPreferences("lat", location.getLatitude() + "", MainTabActivity.this);
				BaseUtils.setSharedPreferences("lon", location.getLongitude() + "", MainTabActivity.this);


			}
		}


	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		if (BaseUtils.getSharedPreferences("wifiChang", this) != null){
		if (BaseUtils.getSharedPreferences("wifiChang", this).equals("yes")){
			transformwifiFrament();
		}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}


	private void  createDownloadNotification(){

		Intent intent = new Intent(this,MainTabActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this,1,intent,0);

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("至尊WiFi正在准备更新...")//设置通知栏标题
				.setProgress(0,0,true)
				.setTicker("至尊WiFi正在更新")
				.setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_ALL)//默认通知声音和震动
				.setContentIntent(pIntent)
		        .setFullScreenIntent(pIntent,true)
				.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
	}


	public RadioGroup getMain_radio() {
		return main_radio;
	}
	/**
	 * 连接服务器,检查版本信息.
	 */
	private void checkVersion() {
		HttpService apiService =  HttpManager.getService();
		Subscription subscription = apiService.Version("至尊免费WiFi", String.valueOf(DeviceUtil.getVersionCode(this)),"zhizunwifi").subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						new Subscriber<BaseResultEntity<Version>>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {
								e.printStackTrace();
								//Log.e("updateError------>",e.getMessage());
							}

							@Override
							public void onNext(BaseResultEntity<Version> baseResultEntity) {
								if (baseResultEntity.ret == 200){
									//Log.e("update--->",baseResultEntity.data.app_version+"     "+baseResultEntity.data.app_url);
									if (Integer.parseInt(baseResultEntity.data.app_version) > DeviceUtil.getVersionCode(MainTabActivity.this)) {
										showUpdateDialog(baseResultEntity.data.desc, baseResultEntity.data.app_name, baseResultEntity.data.app_url, baseResultEntity.data.update);
									}
								}else {
									//Log.e("update---->","获取更新信息失败");
								}

							}

							@Override
							public void onStart() {
								super.onStart();
							}
						}

				);
		mCompositeSubscription.add(subscription);
	}

	/**
	 * 显示升级提醒的对话框
	 *
	 */
	private void showUpdateDialog(final String updataMsg,final String apkname,final String downloadurl,final String isMandatory) {
		if (alertDialog != null) {
			alertDialog.dismiss();
			alertDialog = null;
		}
		boolean exitTag;
		String btnname = "";
		if (isMandatory.equals("2")){
			btnname = "退出应用";
			exitTag = false;
		}else {
			btnname = "下次再说";
			exitTag =true;
		}
		  alertDialog = new AlertDialog(this, null).builder()
				  .setCancelable(exitTag)
				  .setTitle("版本更新")
				  .setMsg(Html.fromHtml(updataMsg).toString())
				  .setNegativeButton(btnname, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
                           if (isMandatory.equals("2")){
							   alertDialog.dismiss();
							   finish();
							   MobclickAgent.onKillProcess(MainTabActivity.this);
							   android.os.Process.killProcess(android.os.Process.myPid());
							   System.exit(0);
						   }else {
							   alertDialog.dismiss();
						   }
					}
				})
				.setPositiveButton("马上更新", new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {

						mNotificationManager.notify(DOWNLOAD, mBuilder.build());
						Toast.makeText(MainTabActivity.this,"至尊WiFi正在更新中，更新进度请查看通知栏！",Toast.LENGTH_SHORT).show();
						// 下载新版本的apk , 替换安装
						HttpUtils httputils = new HttpUtils();
						final File file = new File(Environment
								.getExternalStorageDirectory(), apkname);
						httputils.download(downloadurl, file.getAbsolutePath(),
								false, new RequestCallBack<File>() {
									@Override
									public void onSuccess(ResponseInfo<File> arg0) {
										// 替换安装apk
										Intent intent = new Intent();
										intent.setAction("android.intent.action.VIEW");
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.addCategory("android.intent.category.DEFAULT");
										intent.setDataAndType(Uri.fromFile(file),
												"application/vnd.android.package-archive");
										startActivity(intent);

										mNotificationManager.cancel(DOWNLOAD);
									}

									@Override
									public void onFailure(HttpException arg0,
														  String arg1) {
									}

									@Override
									public void onLoading(long total, long current, boolean isUploading) {
										super.onLoading(total, current, isUploading);
										double x_double = current * 1.0;
										double tempresult = x_double / total;
										DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
										// 百分比格式，后面不足2位的用0补齐
										String result = df1.format(tempresult);

										NotificationCompat.Builder mBuilder02 = new NotificationCompat.Builder(MainTabActivity.this);
										mBuilder02.setContentTitle("至尊WiFi正在下载中...")//设置通知栏标题
												.setProgress(100,(int)(Float.parseFloat(result) * 100),false)
												.setTicker("至尊WiFi正在更新")
												.setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
												.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
												.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
										mNotificationManager.notify(DOWNLOAD,mBuilder02.build());
									}
								});
					}
				});
		alertDialog.show();
	}

}
