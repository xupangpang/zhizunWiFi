package com.zhizun.zhizunwifi.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.activity.InstallManageActivity;
import com.zhizun.zhizunwifi.activity.MainTabActivity;
import com.zhizun.zhizunwifi.activity.PersonalHotspotActivity;
import com.zhizun.zhizunwifi.activity.WiFiDetailActivity;
import com.zhizun.zhizunwifi.activity.WiFiDetailsActivity;
import com.zhizun.zhizunwifi.activity.WiFiMapActivity;
import com.zhizun.zhizunwifi.bean.FreeWifi;
import com.zhizun.zhizunwifi.bean.JsonWifi;
import com.zhizun.zhizunwifi.bean.JsonWifiMain;
import com.zhizun.zhizunwifi.bean.PutAccount;
import com.zhizun.zhizunwifi.bean.StoreMsg;
import com.zhizun.zhizunwifi.bean.WiFiDetailEntity;
import com.zhizun.zhizunwifi.bean.wifiinfo;
import com.zhizun.zhizunwifi.dialog.AlertDialog;
import com.zhizun.zhizunwifi.dialog.CurrentConnectWiFiDialog;
import com.zhizun.zhizunwifi.dialog.PasswordConnectDialog;
import com.zhizun.zhizunwifi.http.BaseResultEntity;
import com.zhizun.zhizunwifi.http.HttpManager;
import com.zhizun.zhizunwifi.http.HttpService;
import com.zhizun.zhizunwifi.service.ListenNetStateService;
import com.zhizun.zhizunwifi.service.ListenNetStateService.NetBind;
import com.zhizun.zhizunwifi.service.ListenNetStateService.WifiConnectStateListener;
import com.zhizun.zhizunwifi.service.ListenNetStateService.WifiStateChangedListener;
import com.zhizun.zhizunwifi.service.LocationService;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.GlideImageLoader;
import com.zhizun.zhizunwifi.utils.JmTools;
import com.zhizun.zhizunwifi.utils.L;
import com.zhizun.zhizunwifi.utils.MarketAPI;
import com.zhizun.zhizunwifi.utils.NetNeedLoginCheckUtil;
import com.zhizun.zhizunwifi.utils.RxBus;
import com.zhizun.zhizunwifi.utils.WifiManage;
import com.zhizun.zhizunwifi.utils.WifiUtils;
import com.zhizun.zhizunwifi.utils.mkQuerypwd;
import com.zhizun.zhizunwifi.utils.router.ActivityDiscovery;
import com.zhizun.zhizunwifi.view.FadingScrollView;
import com.zhizun.zhizunwifi.view.SlideSwitch;
import com.zhizun.zhizunwifi.view.SlideSwitch.OnSwitchChangedListener;
import com.zhizun.zhizunwifi.view.WiFiListLayout;
import com.zhizun.zhizunwifi.view.WiFiListLayout.OnItemListener;
import com.zhizun.zhizunwifi.widget.PopWinConnect;
import com.zhizun.zhizunwifi.widget.PopWinMore;
import com.zhizun.zhizunwifi.widget.PopWinMoreConn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.umeng.socialize.utils.DeviceConfig.context;


@SuppressLint("ValidFragment") public class ConnectFragment2 extends BaseFragment implements OnCheckedChangeListener {
	/** 未开启WIFI 布局 **/
	private RelativeLayout frag_wifilist_wifi_disable_container;
	/** 开启wifi **/
	private Button frag_wifilist_wifi_disable_open;
	/** wifi列表ListView **/
	private WiFiListLayout WiFiListLayoutView;

	/** 正在打开wifi 布局 **/
	LinearLayout ap_disabled;
	/** 未开启的图片 **/
	ImageView ap_disabled_status;
	/** 打开中的图片 **/
	ProgressBar ap_disabled_opening;
	/** 打开按钮 **/
	Button ap_disabled_open;
	/** 打开、关闭按钮 **/
	SlideSwitch sb;
	/** 下拉刷新 **/
	SwipeRefreshLayout refreshableView;
	/** 已经连接上的Wifi对象 **/
	FreeWifi currentConnectWiFi = new FreeWifi();
	/** 已经连接上的wifi名称TextView **/
	TextView tv_ap_ssid;
	/** 连接状态描述 **/
	TextView tv_ap_state_describe;

	/** 已经连接上的wifi名称布局 **/
	FrameLayout fl_connected;
	//TextView tv_state_safe;
	/** 已经连接上的wifi后弹出的pop**/
	FrameLayout fl_wifi_pop;

	/** 已经连接上的wifi名称布局 **/
	RelativeLayout rl_connected;
	/** 已经连接上的wifi打钩 **/
	//ImageView connected;
	//ImageView connected_share;
	/** 已经连接上的wifi名称布局 **/
	Button btn_oneKeySearchWiFi;
	/** 连接wifi动画 **/
	//CustomLoading connectAnim;
	/** 地图 **/
	ImageView headerMenu;

	RadioGroup main_radio;

	private LocationService locationService;

	/** WifiUtils工具类 **/
	private WifiUtils localWifiUtils;
	/** 扫描wifi消息 **/
	private static final int ScanWiFi = 0x100;

	/** wifi身份认证中 **/
	public static final int Authenticating = 0x110;
	/** wifi身份认证通过，正在获取IP中 **/
	public static final int GetIP = 0x111;
	/** 连接wifi中 **/
	public static final int Connceting = 0x112;
	/** 连接wifi成功消息 **/
	public static final int ConncetSuccess = 0x113;
	/** wifi身份认证失败--连接wifi密码错误消息  **/
	public static final int AuthenticatError = 0x114;
	/** wifi断开 **/
	public static final int disconnect = 0x116;
	/** 关闭加载框 **/
	public static final int closeLoadDialog = 0x117;
	/** 密码连接 **/
	private static final int PasswordConnect = 0x3;
	/** 自动连接 **/
	private static final int AutoConnect = 0x4;
	/** 关闭授权 **/
	private static final int Authorization = 108;


	/**
	 * 当前过滤后的免费WiFi
	 */
	private List<FreeWifi> mCurFreeWifiList = new ArrayList<FreeWifi>();
	/**
	 * 过滤后的扫描wifi列表
	 */
	private List<ScanResult> scanWiFiResults;


	private Map<String, Object> netParams;
	private JSONObject postData;

	private double latitude = 0; // 纬度坐标
	private double longitude = 0; // 经度坐标

	/** 开始下拉刷新 **/
	private boolean startRefresh;

	/**
	 * 是否分享
	 */
	private boolean isShareWiFi;
	/**
	 * 需分享的SSID
	 */
	private String curShareSSID;

	private String curShareBSSID;
	/**
	 * 需分享的SSID--对应的密码
	 */
	private String curShareSSID_pwd;

	private Intent NetStateService;
	private String curConnectSSID = null;;
	private final  int WIFIPWD = 900;

	private boolean isShare = false;
	private  HttpService apiService;
	protected CompositeSubscription mCompositeSubscription;
	private boolean isFirst = true;

	private boolean isError = false;
	private List<JsonWifi> jsonWifis;
	private boolean isSharePwd = false;
	private int jsonWifisSize = 0;
	private Button btn_connect;
	private boolean clickOn = false;
	private boolean isOpenwifi = false;
	private Button more_btn;//标题栏更多按钮
	private PopWinMore PopWinMore;
	private PopWinMoreConn PopWinMoreConn;
	private LinearLayout wifi_connected_lay;
	private PopWinConnect PopWinconnect;
	private RelativeLayout title_view;
	private LinearLayout see_the_share;
	private LinearLayout disconnect_lay;
	private boolean isShowSQ = false;
	private FadingScrollView nac_root;
	private Banner wifi_connect_banner;
	private View nac_layout;
	private LinearLayout ll_connected;
	private TextView connect_title;
	private TextView connect_store_name;
	private TextView connect_store_type;
	private LinearLayout wifi_connect_store;
	private List<StoreMsg> storeMsgs;
	private List<String> imgs;
	private TextView pref_item_title;

	@SuppressLint("ValidFragment")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//retrofit  初始化
		apiService =  HttpManager.getService();
		mCompositeSubscription = new CompositeSubscription();

		//wifi刷新视图
		refreshableView = (SwipeRefreshLayout) view.findViewById(R.id.refreshable_view);
		refreshableView.setDistanceToTriggerSync(100);
		refreshableView.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
		refreshableView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				handler.postDelayed(new Runnable() {
					public void run() {
						startRefresh = true;
						getwifipsw();
						GetBusinessInfo();
						scanWiFi();
					}
				}, 1000);
			}
		});
	/*	refreshableView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				handler.postDelayed(new Runnable() {
					public void run() {
						startRefresh = true;
						scanWiFi();
					}
				}, 1000);
			}
		}, 0);*/

		//已连接的wifi名称
		tv_ap_ssid = (TextView) view.findViewById(R.id.tv_ap_ssid);
        //已连接wifi 状态
		tv_ap_state_describe = (TextView) view.findViewById(R.id.tv_ap_state_describe);

		title_view = (RelativeLayout)view.findViewById(R.id.title_view);
		//获取的wifi布局
		WiFiListLayoutView = (WiFiListLayout) view.findViewById(R.id.WiFiListLayoutView);
		WiFiListLayoutView.initView(view);
		WiFiListLayoutView.setOnItemListener(new OnItemListener() {
		@Override
			public void onItemLongClick(View parentView, View view, FreeWifi mFreeWifi,
										ScanResult mScanResult) {
				// TODO Auto-generated method stub
				if(mFreeWifi != null){
					//showcurrentConnectWiFiDialog(view, false, mFreeWifi, null);
				}
			}

			@Override
			public void onItemClick(View parentView, View view, FreeWifi mFreeWifi,
									ScanResult mScanResult) {
				isError = true;
				// TODO Auto-generated method stub
				if(mFreeWifi != null){
					if(!localWifiUtils.isWifiConnected(ConnectFragment2.this.getActivity())){
						startConnect(mFreeWifi.netId, mFreeWifi.getWifiName(), mFreeWifi.wifi_psw, mFreeWifi.getWifi_mac_address(), false,mFreeWifi.isNoPsw());
						//connectAnim.start();
						String[] strs = {curConnectSSID, ""};
						setSSIDView(strs);
					} else{
						showDialog(view, mFreeWifi);
					}
				} else if(mScanResult != null){
					clickOn = true;
					isShare = true;
					if (jsonWifis != null && jsonWifis.size() > 0){
						//不能破解的对话框
						showPasswordConnectDialog(view, mScanResult.SSID, mScanResult.BSSID, null, false,false);
						for (int i = 0; i < jsonWifis.size(); i++) {
							if (mScanResult.SSID.equals(jsonWifis.get(i).getSsid())){
								//能破解的对话框
								showPasswordConnectDialog(view, mScanResult.SSID, mScanResult.BSSID, null, false,true);
							}else {
								//showPasswordConnectDialog(view, mScanResult.SSID, mScanResult.BSSID, null, false,false);
							}
						}

					}else {
						//系统保存的已连接的对话框
						showPasswordConnectDialog(view, mScanResult.SSID, mScanResult.BSSID, null, false,false);
					}
				}
			}
		});
		ap_disabled = (LinearLayout) view.findViewById(R.id.ap_disabled); //
		fl_connected = (FrameLayout) view.findViewById(R.id.fl_connected);
		//tv_state_safe = (TextView) view.findViewById(R.id.tv_state_safe);
		rl_connected = (RelativeLayout) view.findViewById(R.id.rl_connected); //
		fl_wifi_pop = (FrameLayout) view.findViewById(R.id.wifi_pop); //
		//connected = (ImageView) view.findViewById(R.id.connected); //
		//connected_share = (ImageView) view.findViewById(R.id.connected_share);
		btn_oneKeySearchWiFi = (Button) view.findViewById(R.id.btn_oneKeySearchWiFi); //
		rl_connected.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				//if(!wifiPop.isShow())
					//showcurrentConnectWiFiDialog(null, true, null, localWifiUtils.getConnectedSSID(localWifiUtils.getConnectionInfo()));
				return false;
			}
		});
		ap_disabled_status = (ImageView) view.findViewById(R.id.ap_disabled_status); //
		ap_disabled_opening = (ProgressBar) view.findViewById(R.id.ap_disabled_opening); //
		ap_disabled_open = (Button) view.findViewById(R.id.ap_disabled_open); //
		headerMenu = (ImageView) view.findViewById(R.id.headerMenu); //
		//wifi 开关
		sb = (SlideSwitch) view.findViewById(R.id.headerRightSwitch);
		sb.setStatus(true);
		sb.setClickable(true);
		sb.setOnSwitchChangedListener(new OnSwitchChangedListener() {
			@Override
			public void onSwitchChanged(SlideSwitch obj, int status) {
				if(status == SlideSwitch.SWITCH_ON){
					System.out.println("SWITCH_ON-------------------");
					localWifiUtils.WifiOpen();
					sb.setClickable(false);
				}else if(status == SlideSwitch.SWITCH_OFF){
					System.out.println("SWITCH_OFF-------------------");
					localWifiUtils.WifiClose();
					sb.setClickable(false);
				}
			}
		});
		//connectAnim = (CustomLoading) view.findViewById(R.id.connectAnim);
		int wifiCheckState = localWifiUtils.WifiCheckState();
		if(wifiCheckState == WifiManager.WIFI_STATE_DISABLED || wifiCheckState == WifiManager.WIFI_STATE_DISABLING
				|| wifiCheckState == WifiManager.WIFI_STATE_UNKNOWN){ // Wi-Fi is disabled.

		}else if(wifiCheckState == WifiManager.WIFI_STATE_ENABLED){ // Wi-Fi is disabled.
		}else if(wifiCheckState == WifiManager.WIFI_STATE_ENABLING){ // WIFI_STATE_ENABLING
			// 显示开启动画
			ap_disabled_status.setVisibility(View.GONE);
			ap_disabled_opening.setVisibility(View.GONE);
			ap_disabled_open.setVisibility(View.GONE);
		}
		initEvent();
		if (!localWifiUtils.isWifiOpen()){
			sb.setStatus(false);
			refreshableView.setVisibility(View.GONE);
			ap_disabled.setVisibility(View.VISIBLE);
			fl_connected.setVisibility(View.GONE);
			btn_oneKeySearchWiFi.setVisibility(View.GONE);
		}
		//一键连接按钮
		btn_connect = (Button)view.findViewById(R.id.btn_connect);
		btn_connect.setEnabled(false);
		btn_connect.setOnClickListener(this);

		//标题栏更多布局
		more_btn = (Button)view.findViewById(R.id.more_btn);
		more_btn.setOnClickListener(this);

		wifi_connected_lay = (LinearLayout)view.findViewById(R.id.wifi_connected_lay);


		nac_layout = view.findViewById(R.id.nac_layout);

		if (PopWinconnect == null) {
			//自定义的单击事件
			PopWinconnect = new PopWinConnect(ConnectFragment2.this.getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,nac_layout);
		}

		see_the_share = (LinearLayout)view.findViewById(R.id.see_the_share);
		see_the_share.setOnClickListener(this);
		disconnect_lay = (LinearLayout)view.findViewById(R.id.disconnect);
		disconnect_lay.setOnClickListener(this);

		pref_item_title = (TextView)view.findViewById(R.id.connect_store_poin);

		wifi_connect_banner = (Banner)view.findViewById(R.id.wifi_connect_banner);
		//设置banner样式
		wifi_connect_banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
		//设置图片加载器
		wifi_connect_banner.setImageLoader(new GlideImageLoader());
		//设置banner动画效果
		wifi_connect_banner.setBannerAnimation(Transformer.DepthPage);
		//设置自动轮播，默认为true
		wifi_connect_banner.isAutoPlay(true);
		//设置轮播时间
		wifi_connect_banner.setDelayTime(5000);
		wifi_connect_banner.setOnBannerListener(new OnBannerListener() {
			@Override
			public void OnBannerClick(int position) {

				Intent intent = new Intent(ConnectFragment2.this.getContext(), WiFiDetailsActivity.class);
					intent.putExtra("WiFiDetail", new WiFiDetailEntity(connect_title.getText().toString(),
							Math.abs(localWifiUtils.getConnectedIntensity(localWifiUtils.getConnectionInfo())),false,BaseUtils.getSharedPreferences("address",context),0,0,"","",false));
				if (storeMsgs != null){
					for (StoreMsg storeMsg : storeMsgs) {
						if (storeMsg.ssid.equals(connect_title.getText().toString())){
							intent.putExtra("storemsg", storeMsg);
						}
					}
				}
				intent.putExtra("homepage","true");

				startActivity(intent);

			}
		});



		nac_root = (FadingScrollView)view.findViewById(R.id.nac_root);



		ll_connected = (LinearLayout)view.findViewById(R.id.ll_connected);

		connect_title = (TextView)view.findViewById(R.id.connect_title);
		wifi_connect_store = (LinearLayout)view.findViewById(R.id.wifi_connect_store);

		connect_store_name = (TextView)view.findViewById(R.id.connect_store_name);
		connect_store_type = (TextView)view.findViewById(R.id.connect_store_type);

		RxBus.getInstance().toObserverable(WiFiDetailEntity.class)
				//在io线程进行订阅，可以执行一些耗时操作
				.subscribeOn(Schedulers.io())
				//在主线程进行观察，可做UI更新操作
				.observeOn(AndroidSchedulers.mainThread())
				//观察的对象
				.subscribe(new Action1<WiFiDetailEntity>() {
					@Override
					public void call(WiFiDetailEntity user) {
						startConnect(user.netId, user.getWifiName(), user.getWifi_psw(), user.getWifi_mac_address(), false,user.getWifiEncryption());
					}
				});
	}

	@Override
	public void onResume() {
		MobclickAgent.onPageStart("MainScreen");
		super.onResume();
		jsonWifisSize = BaseUtils.getIntSharedPreferences("jsonWifisSize",this.getActivity());
		final String connectedSSID = localWifiUtils.getConnectedSSID(localWifiUtils.getConnectionInfo());
		if (TextUtils.isEmpty(connectedSSID)) {
			btn_connect.setText("一键安全连接");
			if (jsonWifisSize != 0)
			tv_ap_ssid.setText(Html.fromHtml("<big>"+jsonWifisSize+"</big>个免费WiFi"));
			tv_ap_state_describe.setVisibility(View.GONE);
			connect_title.setText("至尊免费WiFi");
			wifi_connect_banner.setVisibility(View.GONE);
			wifi_connect_store.setVisibility(View.GONE);
			ll_connected.setVisibility(View.VISIBLE);
			nac_layout.setAlpha(1);
		}else {
			btn_connect.setText("体检测速");
			tv_ap_state_describe.setVisibility(View.VISIBLE);
			tv_ap_state_describe.setText("连接成功，安全保护中");
			//连接成功展示  商户广告页面
			wifi_connect_banner.setVisibility(View.VISIBLE);
			wifi_connect_store.setVisibility(View.VISIBLE);
			ll_connected.setVisibility(View.GONE);
			connect_title.setText(connectedSSID);

			if (storeMsgs != null){
				for (StoreMsg storeMsg : storeMsgs) {
					if (storeMsg.ssid.equals(connect_title.getText().toString())){
						if (imgs != null){
							imgs.clear();
						}
						//设置图片集合
						for (StoreMsg.pic pic : storeMsg.pic) {
							imgs.add(pic.link);
						}
						wifi_connect_banner.update(imgs);
						wifi_connect_banner.start();
						connect_store_name.setText(storeMsg.name);
						connect_store_type.setText(storeMsg.type);
						pref_item_title.setText("•");
					}
				}
			}
		}

		if(!isCallBack){
			// <unknown ssid> 未知的wifi
			if(localWifiUtils.isWifiConnected(this.getActivity())){
				mWifiConnectStateListener.onConnected(connectedSSID);
			} else {
				State curState = localWifiUtils.getWifiConnectState(getActivity());
				if(curState == State.CONNECTING)
					mWifiConnectStateListener.onConnecting(connectedSSID);
				else if(curState == State.DISCONNECTED)
					handler.sendEmptyMessage(disconnect);
			}
		}
		isFirst = true;
		scanWiFi();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.btn_connect://一键连接
				if ("一键安全连接".equals(btn_connect.getText().toString())){
					if (BaseUtils.isConnect(this.getActivity())){
						uploadPhonePswwk();
						MainTabActivity parentActivity = (MainTabActivity ) getActivity();
						parentActivity.getwifipsw();
						parentActivity.getOneLine();
						Collections.sort(mCurFreeWifiList, new Comparator<FreeWifi>(){
							public int compare(FreeWifi o1, FreeWifi o2) {
								if(o1.getRSSI() > o1.getRSSI()){
									return 1;
								}
								if(o1.getRSSI() == o1.getRSSI()){
									return 0;
								}
								return -1;
							}
						});
						if (mCurFreeWifiList.size() < 0){
                              int pos = 0;
							for (int i = 0; i < mCurFreeWifiList.size(); i++) {
								if (!mCurFreeWifiList.get(i).isNoPsw()){
									pos = i;
									break;
								}
							}

							String[] strs = {mCurFreeWifiList.get(pos).getWifiName(), "正在建立连接"};
							setSSIDView(strs);
							startConnect(mCurFreeWifiList.get(pos).netId, mCurFreeWifiList.get(pos).getWifiName(), mCurFreeWifiList.get(pos).wifi_psw, mCurFreeWifiList.get(pos).getWifi_mac_address(), false,false);
						}else {

							List<JsonWifi>  jsonWifiList = parentActivity.getOneLine();
							if (jsonWifiList.size() > 0) {
								for (int i = 0; i < jsonWifiList.size(); i++) {
										for (int j = 0; j < scanWiFiResults.size(); j++) {
											if (jsonWifiList.get(i).getSsid().equals(scanWiFiResults.get(j).SSID)){
												jsonWifiList.get(i).setLevel(Math.abs(scanWiFiResults.get(j).level));
											}
										}
								}

								for (JsonWifi jsonWifi : jsonWifiList) {
									if (!localWifiUtils.checkIsCurrentWifiHasPassword(jsonWifi.getSsid(),ConnectFragment2.this.getActivity())){
										jsonWifiList.remove(jsonWifi);
									}
								}

								Collections.sort(jsonWifiList, new Comparator<JsonWifi>(){
									public int compare(JsonWifi o1, JsonWifi o2) {
										if(o1.getLevel() > o1.getLevel()){
											return 1;
										}
										if(o1.getLevel() == o1.getLevel()){
											return 0;
										}
										return -1;
									}
								});
								String[] strs = {jsonWifiList.get(0).getSsid(), "正在建立连接"};
								setSSIDView(strs);
								startConnect(-1, jsonWifiList.get(0).getSsid(), jsonWifiList.get(0).getPasswd(), jsonWifiList.get(0).getRoute(), true,false);
							}
						}
					}else {
                        Toast.makeText(ConnectFragment2.this.getActivity(),"需要您在打开数据网络，以获取云端热点信息",Toast.LENGTH_SHORT).show();
					}
				}else if ("输入新密码".equals(btn_connect.getText().toString())){
					showPasswordConnectDialog(null, curShareSSID , curShareBSSID , "密码错误", false,false);
				}else if("认证".equals(btn_connect.getText().toString())){
					Uri uri = Uri.parse("http://www.baidu.com");
					Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent0);
				} else {
					/*final FreeWifi freeWiFi = getconnWiFi_FreeWifi(null);
					if(freeWiFi == null) {
						return;
					}
					if(!localWifiUtils.isWifiConnected(getActivity())){
						showcurrentConnectWiFiDialog(null, true, null, freeWiFi.getWifiName());
					} else{



						wifiPop.setCheckClick(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(getActivity(), SafetyInspectActivity.class);
								intent.putExtra("wifiName", freeWiFi.getWifiName());
								getActivity().startActivityForResult(intent, MainTabActivity.SafetyInspectRequsetCode);
							}
						});
						wifiPop.setSpeedClick(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(getActivity(), NetSpeedActivity.class);
								startActivity(intent);
							}
						});
						boolean isShared = false;
						if(freeWiFi.getmFreeWiFiType() == FreeWiFiType.NetWifi)
							isShared = true;
						String shareStr = isShared ? "已分享" : "分享";

						wifiPop.setShareClick(shareStr, new OnClickListener() {
							@Override
							public void onClick(View view) {
//						showDialog(SHARE_HOTSPOT_DIALOG);
								if(latitude != 0)
									Toast.makeText(getActivity(), "分享成功！", Toast.LENGTH_LONG).show();
									//showPasswordConnectDialog(null, freeWiFi.getWifiName(), freeWiFi.getWifi_mac_address(), null, true);
								else
									Toast.makeText(getActivity(), "分享wifi要先开启定位服务\n当前定位信息获取不到，请先确保定位服务开启状态", Toast.LENGTH_LONG).show();
							}
						});
						wifiPop.setReportClick(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								showReportToFishingDialog();
							}
						});
						wifiPop.setStartInternetClick(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// 跳转到百宝箱页面
								main_radio = ((MainTabActivity)getActivity()).getMain_radio();
								// onCheckedChanged方法只调用了一次：
								((RadioButton) ((MainTabActivity)getActivity()).findViewById(R.id.tab_news)).setChecked(true);;

								// onCheckedChanged方法调用了两次：
//						main_radio.check(main_radio.getChildAt(1).getId());
							}
						}).setDisconnectBtnInternetClick(new OnClickListener() {
									//断开网络
									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										mWifiConnectStateListener.startDisconnect(true);
										wifiPop.closePop();
									}
								}).startPop(WifiStateResult.SUCCESSLOGIN);
					}*/
				}
				break;
			case R.id.rl_connected: // 已连接WiFi布局下拉
				break;
			case R.id.btn_oneKeySearchWiFi: // 一键查询免费WiFi
				System.out.println("一键查询免费WiFi---------");
				btn_oneKeySearchWiFi.setVisibility(View.GONE);
				break;
			case R.id.ap_disabled_open: // 开启WiFi
				localWifiUtils.WifiOpen();
				ap_disabled_status.setVisibility(View.GONE);
				ap_disabled_opening.setVisibility(View.GONE);
				ap_disabled_open.setVisibility(View.GONE);
				sb.setStatus(true);
				sb.setClickable(false);
				break;
			case R.id.headerMenu:
				intent = new Intent(getActivity(), WiFiMapActivity.class);
				getActivity().startActivity(intent);

				//intent = new Intent(getActivity(), WiFiConnectActivity.class);
				//getActivity().startActivity(intent);

				break;
			case R.id.more_btn:
				if (localWifiUtils.isWifiConnected(ConnectFragment2.this.getContext())){
					//wifi连接成功显示的POPWINGDOWS
					if (PopWinMoreConn == null) {
						//自定义的单击事件
						PopWinMoreConn = new PopWinMoreConn(ConnectFragment2.this.getActivity(), this, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						//监听窗口的焦点事件，点击窗口外面则取消显示
						PopWinMoreConn.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									PopWinMoreConn.dismiss();
								}
							}
						});
					}
					//设置默认获取焦点
					PopWinMoreConn.setFocusable(true);
					//以某个控件的x和y的偏移量位置开始显示窗口
					PopWinMoreConn.getContentView().measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
					int i = PopWinMoreConn.getContentView().getMeasuredWidth();

					PopWinMoreConn.showAsDropDown(more_btn,85-i,10);
					//如果窗口存在，则更新
					PopWinMoreConn.update();


					PopWinMoreConn.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							more_btn.setBackgroundResource(R.drawable.btn_more_down);
						}
					});
					if (PopWinMoreConn.isShowing()){
						more_btn.setBackgroundResource(R.drawable.btn_more_up);
					}
				}else {
					//wifi未连接显示的POPWINGDOWS
					if (PopWinMore == null) {
						//自定义的单击事件
						PopWinMore = new PopWinMore(ConnectFragment2.this.getActivity(), this, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						//监听窗口的焦点事件，点击窗口外面则取消显示
						PopWinMore.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									PopWinMore.dismiss();
								}
							}
						});
					}
					//设置默认获取焦点
					PopWinMore.setFocusable(true);
					//以某个控件的x和y的偏移量位置开始显示窗口
					PopWinMore.getContentView().measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
					int i = PopWinMore.getContentView().getMeasuredWidth();

					PopWinMore.showAsDropDown(more_btn,85-i,10);
					//如果窗口存在，则更新
					PopWinMore.update();

					PopWinMore.setwifiSwitch(localWifiUtils.isWifiOpen());

					PopWinMore.setOnDismissListener(new PopupWindow.OnDismissListener() {
						@Override
						public void onDismiss() {
							more_btn.setBackgroundResource(R.drawable.btn_more_down);
						}
					});
					if (PopWinMore.isShowing()){
						more_btn.setBackgroundResource(R.drawable.btn_more_up);
					}
				}

				break;
			case R.id.see_the_share:
				//showRootPswDialog();
                  //showRootPswDialogVIVO();
				break;
			case R.id.disconnect:
				//断开网络
				mWifiConnectStateListener.startDisconnect(true);
				wifi_connected_lay.setVisibility(View.GONE);
				updateListView();
				break;
			case R.id.main_more_wifi:
				//wifi开关
                  if (localWifiUtils.isWifiOpen()){
					  localWifiUtils.WifiClose();
				  }else {
					  localWifiUtils.WifiOpen();
				  }
				PopWinMore.dismiss();
				break;
			case R.id.main_more_hotspot:
				//个人热点
				Intent intent8 = new Intent(getActivity(), PersonalHotspotActivity.class);
				startActivity(intent8);
				PopWinMore.dismiss();
				break;
			case R.id.main_more_router:
				if (BaseUtils.isWifiConnected(ConnectFragment2.this.getContext())) {
					Intent intent1 = new Intent(ConnectFragment2.this.getContext(), ActivityDiscovery.class);
					startActivity(intent1);
				}else {
					Toast.makeText(ConnectFragment2.this.getContext(),"连接上WiFi才能使用",Toast.LENGTH_SHORT).show();
				}

				PopWinMore.dismiss();
				break;
			case R.id.see_the_share_pop:
				//查看分享
				showRootPswDialog();
				//showRootPswDialogVIVO();
				PopWinMoreConn.dismiss();
				break;
			case R.id.main_more_disconnect:
				btn_connect.setText("一键安全连接");
				if (jsonWifisSize != 0)
					tv_ap_ssid.setText(Html.fromHtml("<big>"+jsonWifisSize+"</big>个免费WiFi"));
				tv_ap_state_describe.setVisibility(View.GONE);
				connect_title.setText("至尊免费WiFi");
				wifi_connect_banner.setVisibility(View.GONE);
				wifi_connect_store.setVisibility(View.GONE);
				ll_connected.setVisibility(View.VISIBLE);
				nac_layout.setAlpha(1);
				nac_root.setIshow(false);
				mWifiConnectStateListener.startDisconnect(true);
				wifi_connected_lay.setVisibility(View.GONE);
				updateListView();
				PopWinMoreConn.dismiss();
				break;
			case R.id.main_more_offwifi:
				if (localWifiUtils.isWifiOpen()){
					localWifiUtils.WifiClose();
				}else {
					localWifiUtils.WifiOpen();
				}
				PopWinMoreConn.dismiss();
				break;



		}
	}



	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.connect_layout, null);
		return view;
	}

	@Override
	public void initData() {
		if(localWifiUtils == null){
			localWifiUtils = new WifiUtils(getActivity());
		}
	}


	private boolean scanWiFi(){
		if(localWifiUtils.isWifiOpen()){
			handler.sendEmptyMessage(ScanWiFi);
			return true;
		}
		return false;
	}

	@Override
	public void initEvent() {
		rl_connected.setOnClickListener(this);
		ap_disabled_open.setOnClickListener(this);
		btn_oneKeySearchWiFi.setOnClickListener(this);
		headerMenu.setOnClickListener(this);
		Intent NetStateService = new Intent(getActivity(), ListenNetStateService.class);
		getActivity().bindService( NetStateService, conn, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case ScanWiFi:
					$Log("DetailedState","扫描加载-handler");
					updateListView();
					break;
				case Authenticating:
					$Log("DetailedState","认证中-handler");
					//connectAnim.start();
					setSSIDView(msg.obj);
					fl_connected.setVisibility(View.VISIBLE);
					//tv_state_safe.setVisibility(View.GONE);

					btn_oneKeySearchWiFi.setVisibility(View.GONE);
					//connected.setVisibility(View.GONE);
					//connected_share.setVisibility(View.GONE);
					dismissAllDialog();

					PopWinconnect.setStag(1,msg.obj);

					break;
				case GetIP:
					$Log("DetailedState","获取IP-handler");
					//connectAnim.start();
					setSSIDView(msg.obj);
					fl_connected.setVisibility(View.VISIBLE);
					//tv_state_safe.setVisibility(View.GONE);

					btn_oneKeySearchWiFi.setVisibility(View.GONE);
					//connected.setVisibility(View.GONE);
					//connected_share.setVisibility(View.GONE);
					dismissAllDialog();

					PopWinconnect.setStag(2,msg.obj);

					break;
				case Connceting:
					$Log("DetailedState","连接中-handler");
					connect_title.setText("至尊免费WiFi");
					wifi_connect_banner.setVisibility(View.GONE);
					wifi_connect_store.setVisibility(View.GONE);
					ll_connected.setVisibility(View.VISIBLE);
					nac_layout.setAlpha(1);
					//监听窗口的焦点事件，点击窗口外面则取消显示
					//设置默认获取焦点
					PopWinconnect.setFocusable(false);
					//以某个控件的x和y的偏移量位置开始显示窗口
					PopWinconnect.getContentView().measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

					PopWinconnect.showAsDropDown(title_view,0,0);
					//如果窗口存在，则更新
					PopWinconnect.update();
					PopWinconnect.setStag(0,msg.obj);


					//connectAnim.start();
					setSSIDView(msg.obj);
					fl_connected.setVisibility(View.VISIBLE);
					//tv_state_safe.setVisibility(View.GONE);

					btn_oneKeySearchWiFi.setVisibility(View.GONE);
					//connected.setVisibility(View.GONE);
					//connected_share.setVisibility(View.GONE);
					dismissAllDialog();

					break;
				case ConncetSuccess:
					final Object msgobj = msg.obj;

					Log.e("DetailedState","连接成功-handler");

					NetNeedLoginCheckUtil.needLoginNetworkCheck(new NetNeedLoginCheckUtil.NeedLoginCallBack() {
						@Override
						public void needLogin(boolean needLogin) {
							if (needLogin) {
								btn_connect.setText("认证");
								tv_ap_state_describe.setText("无法联网，需要登录认证");
								PopWinconnect.setrenz(true);
							}else {
								//获取商户信息
								GetBusinessInfo();
								PopWinconnect.setrenz(false);
								btn_connect.setText("体检测速");
								PopWinconnect.setStag(3,msgobj);
								wifi_connected_lay.setVisibility(View.GONE);
								//联网成功后展示 商品广告页
								wifi_connect_banner.setVisibility(View.VISIBLE);
								wifi_connect_store.setVisibility(View.VISIBLE);
								nac_root.setFadingView(nac_layout);
								nac_root.setFadingHeightView(wifi_connect_banner);
								nac_root.setIshow(true);
								ll_connected.setVisibility(View.GONE);
								String[] strs = (String[])msgobj;
								connect_title.setText(strs[0]);
							}
						}
					});





					//connectAnim.stop();
					// 显示已连接上的wifi布局,隐藏一键查询按钮

					//tv_state_safe.setVisibility(View.VISIBLE);
					//connected.setVisibility(View.VISIBLE);
					//connected_share.setVisibility(View.VISIBLE);
					btn_oneKeySearchWiFi.setVisibility(View.GONE);
//				String ssid = localWifiUtils.getConnectedSSID(localWifiUtils.getConnectionInfo());



					setSSIDView(msg.obj);
					scanWiFi();
					dismissAllDialog();

					basehideProgressDialog();

					if (isSharePwd){
						Toast.makeText(ConnectFragment2.this.getActivity(),"分享热点成功!",Toast.LENGTH_SHORT).show();
						isSharePwd = false;
					}



					//成功连接后上传服务器统计连接次数
					putConnectSuccess();

					// 连接成功，上传密码
					if (isShare){
						if (!TextUtils.isEmpty(curShareSSID_pwd) && !curShareSSID_pwd.equals("*")){
							JsonWifi jsonWifi = new JsonWifi();
							jsonWifi.setRouter(curShareBSSID);
							jsonWifi.setSsid(curShareSSID);
							jsonWifi.setPasswd(curShareSSID_pwd);
							jsonWifi.setLat(BaseUtils.getSharedPreferences("lat",ConnectFragment2.this.getActivity()));
							jsonWifi.setLon(BaseUtils.getSharedPreferences("lon",ConnectFragment2.this.getActivity()));
							jsonWifi.save();
						}
						//uploadSinglePsw(curShareBSSID, curShareSSID, curShareSSID_pwd);
						isShare = false;
					}
					break;
				case AuthenticatError:
					$Log("DetailedState","身份认证失败-handler");
					String[] strs = (String[])msg.obj;
					if(strs[0] != null){
						int netId = localWifiUtils.IsConfiguration(strs[0] );
						if(netId != -1) {
							localWifiUtils.removeNetwork(netId);
						}

						if (clickOn){
							PopWinconnect.dismiss();
							tv_ap_state_describe.setText("密码错误，需要重新输入密码");
							btn_connect.setText("输入新密码");
							//showPasswordConnectDialog(null, strs[0] , strs[1] , "密码错误", false,false);
							clickOn = false;
							curShareSSID =  strs[0];
							curShareBSSID =  strs[1];
						}

						if (isSharePwd){
							Toast.makeText(ConnectFragment2.this.getActivity(),"密码错误,分享失败!",Toast.LENGTH_SHORT).show();
							isSharePwd = false;
						}
					}

					// 结束连接动画
					//connectAnim.stop();
					btn_connect.setText("一键安全连接");
					//fl_connected.setVisibility(View.GONE);
					//connected.setVisibility(View.GONE);
					//connected_share.setVisibility(View.GONE);
					btn_oneKeySearchWiFi.setVisibility(View.GONE);
					scanWiFi();
					tv_ap_state_describe.setVisibility(View.GONE);
					break;
				case disconnect:
					$Log("DetailedState","断开或失败-handler");
					if(fl_connected.getVisibility() == View.VISIBLE && localWifiUtils.isWifiOpen()){
						scanWiFi();
						//fl_connected.setVisibility(View.GONE);
						//connected.setVisibility(View.GONE);
						//connected_share.setVisibility(View.GONE);
						btn_oneKeySearchWiFi.setVisibility(View.GONE);
					}
					btn_connect.setText("一键安全连接");
					tv_ap_state_describe.setVisibility(View.GONE);
					break;
				case closeLoadDialog:
					basehideProgressDialog();
					break;
				case WIFIPWD:
					if (msg.obj != null){
						//获取万能wifi库里的密码并保存到数据库中
						List<JsonWifi>  Wifis = (List<JsonWifi>)msg.obj;
						List<JsonWifi>  jsonWifit = BaseUtils.getDiffrent(Wifis,DataSupport.findAll(JsonWifi.class));
						for (JsonWifi jsonWifi : jsonWifit) {
							jsonWifi.save();
						}
						List<JsonWifi>  wifiList = DataSupport.findAll(JsonWifi.class);
						Gson gson = new Gson();
						String aa = gson.toJson(wifiList).toString();
						JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put("data",aa);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						scanWiFi();

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
												e.printStackTrace();

											}

											@Override
											public void onNext(BaseResultEntity<PutAccount> baseResultEntity) {
												if (!baseResultEntity.data.result.equals("success")){
													$Log("JsonResult","上传服务器失败");
												}else {
													$Log("JsonResult","上传服务器成功");
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
					break;
				case Authorization:
					basehideProgressDialog();
					showRootAPKDialog();
					//Toast.makeText(ConnectFragment2.this.getContext(),"授权失败",Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		};
	};

	/**
	 * 设置WiFi名称，连接状态描述
	 * @param obj==String[2]   WiFi名称    连接状态
	 */
	private void setSSIDView(Object obj){
		if(obj != null){
			tv_ap_state_describe.setVisibility(View.VISIBLE);
			String[] strs = (String[])obj;
			tv_ap_ssid.setText(strs[0]);
			tv_ap_state_describe.setText(strs[1]);
		}
	}

	private void dismissAllDialog(){
		if(passwordConnectDialog != null){
			passwordConnectDialog.dismiss();
			passwordConnectDialog = null;
		}
		if(currentConnectWiFiDialog != null){
			currentConnectWiFiDialog.dismiss();
			currentConnectWiFiDialog = null;
		}
		if(alertDialog != null){
			alertDialog.dismiss();
			alertDialog = null;
		}
	}



	private void updateListView(){
      //wifi布局刷新
		if(curRefreshWiFiListView == null){
			curRefreshWiFiListView = new RefreshWiFiListView();
		} else{
			curRefreshWiFiListView.cancel(true);
			curRefreshWiFiListView = new RefreshWiFiListView();
		}
		curRefreshWiFiListView.execute();
	}

	RefreshWiFiListView curRefreshWiFiListView;
	class RefreshWiFiListView extends AsyncTask<Void, Void, Void>{
		String curConnSSID;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mCurFreeWifiList = new ArrayList<FreeWifi>();
			if (scanWiFiResults != null)
				scanWiFiResults.clear();
			scanWiFiResults = localWifiUtils.getWiFiResult(mCurFreeWifiList);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			curConnSSID = localWifiUtils.getConnectedSSID(localWifiUtils.getConnectionInfo());
			if(curConnSSID == null){
				if(fl_connected.getVisibility() == View.VISIBLE){
					curConnSSID = tv_ap_ssid.getText().toString();
				}
			}

			if(refreshableView.getVisibility() != View.VISIBLE)
				if (localWifiUtils.isWifiOpen()){
					refreshableView.setVisibility(View.VISIBLE);
					if(ap_disabled.getVisibility() != View.GONE){
						ap_disabled.setVisibility(View.GONE);
						ap_disabled_status.setVisibility(View.VISIBLE);
						ap_disabled_opening.setVisibility(View.GONE);
						ap_disabled_open.setVisibility(View.VISIBLE);
					}
				}else {
					refreshableView.setVisibility(View.GONE);
				}

			jsonWifis = DataSupport.findAll(JsonWifi.class);

			WiFiListLayoutView.refreshData(mCurFreeWifiList, scanWiFiResults, curConnSSID,jsonWifis,storeMsgs);


			BaseUtils.setSharedPreferences("jsonWifisSize", mCurFreeWifiList.size(), ConnectFragment2.this.getContext());

			if (TextUtils.isEmpty(curConnSSID)) {
				tv_ap_ssid.setText(Html.fromHtml("发现<big>"+mCurFreeWifiList.size()+"</big>个免费WiFi"));
			}else {
				tv_ap_ssid.setText(curConnSSID);
			}

			// 下拉刷新完成
			if(startRefresh){
				refreshableView.setRefreshing(false);
				startRefresh = false;
			}

			btn_connect.setEnabled(true);
		}
	}


	/**
	 *
	 * @param netId
	 * @param SSID
	 * @param password
	 * @param BSSID
	 * @param isInitConn
	 */
	public void startConnect(final int netId, final String SSID,final String password, final String BSSID, final boolean isInitConn,final boolean isOpenWifi){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int curNetId = localWifiUtils.IsConfiguration(netId, SSID);
				if (isOpenWifi){
					isOpenwifi = true;
					curNetId = localWifiUtils.AddOpenWifiConfig(curNetId, SSID, password, BSSID);
				}else {
					if(isInitConn || curNetId == -1){
						if(password != null){
							curNetId = localWifiUtils.AddWifiConfig(curNetId, SSID, password, BSSID);
						}
					}
				}

				if (curNetId != -1) {
					localWifiUtils.enableNetwork(curNetId, true);
					// 得到Wifi配置好的信息
					isShareWiFi = isShare;
					curShareBSSID = BSSID;
					curShareSSID = SSID;
					curShareSSID_pwd = password;

					localWifiUtils.disconnectWifi(false);
					if (localWifiUtils.ConnectWifi(curNetId, true)) {//建立连接成功--开始验证身份
						L.d("WifiUtils","建立连接成功--开始验证身份:  "+curNetId);
					} else{
						isShareWiFi = false;
						curShareSSID = null;
						curShareSSID_pwd = null;
					}
				}
				$Log("curNetId",curNetId+"");
			}
		}).start();
	}

	//举报功能
	private void showReportToFishingDialog() {
		final Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_repor_to_go_fishingt);

		TextView tv_content = (TextView)dialog.findViewById(R.id.dlg_exit_from_home_msg_noti_remind);
		tv_content.setText(String.format(getResources().getString(R.string.act_wifilist_dlg_report_phishing_wifi_body), localWifiUtils.getConnectedSSID(localWifiUtils.getConnectionInfo())));

		Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "举报成功，我们将会进行排查!", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
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


	//Root 查看wifi密码
	private void showRootPswDialog() {

		final Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_repor_to_go_fishingt);

        TextView tv_title = (TextView)dialog.findViewById(R.id.tv_title);
		tv_title.setText("查看分享");
		TextView tv_content = (TextView)dialog.findViewById(R.id.dlg_exit_from_home_msg_noti_remind);
		tv_content.setText("查看当前WiFi密码及分享好友需要Root权限，请授予至尊免费WiFiRoot权限。");

		Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
		btn_ok.setText("授予权限");
		Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isShowSQ = true;
				      dialog.dismiss();
                      baseShowProgressDialog("授权中",false);
				try {
					List<wifiinfo> list = WifiManage.Read();
					if (list != null && list.size() > 0){
						for (wifiinfo wifiinfo : list) {
							if (wifiinfo.Ssid.equals(curConnectSSID)){
								basehideProgressDialog();

								UMWeb web = new UMWeb(MarketAPI.QQ_web);
								web.setTitle(MarketAPI.shareTitle);//标题
								web.setThumb(new UMImage(ConnectFragment2.this.getActivity(), R.drawable.ico));  //缩略图
								web.setDescription(MarketAPI.shareContents+"WiFi名称:"+wifiinfo.Ssid +" WiFi密码"+wifiinfo.Password);//描述

								new ShareAction(ConnectFragment2.this.getActivity())
										.withText(MarketAPI.shareTitle)
										.withMedia(web)
										.setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA)
										.setCallback(umShareListener)
										.open();
							}
                            //存入数据库  以便上传到服务器中
							JsonWifi jsonWifi = new JsonWifi();
							jsonWifi.setRouter(wifiinfo.Address);
							jsonWifi.setSsid(wifiinfo.Ssid);
							jsonWifi.setPasswd(wifiinfo.Password);
							jsonWifi.setLat(BaseUtils.getSharedPreferences("lat",ConnectFragment2.this.getActivity()));
							jsonWifi.setLon(BaseUtils.getSharedPreferences("lon",ConnectFragment2.this.getActivity()));
							jsonWifi.save();
						}
					}
				} catch (Exception e) {
					if (isShowSQ) {
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								handler.sendEmptyMessage(Authorization);
							}
						}, 2000);
					}
					e.printStackTrace();
				}

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


	//下载Root工具
	private void showRootAPKDialog() {

		final Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dlg_repor_to_go_fishingt);

		TextView tv_title = (TextView)dialog.findViewById(R.id.tv_title);
		tv_title.setText("授权失败，无法查看分享");
		TextView tv_content = (TextView)dialog.findViewById(R.id.dlg_exit_from_home_msg_noti_remind);
		tv_content.setText("授权失败，建议使用一键	Root工具获取Root权限。");

		Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
		btn_ok.setText("立即使用");
		Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转安装包管理页
				dialog.dismiss();
                startActivity(new Intent(ConnectFragment2.this.getContext(), InstallManageActivity.class));
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


	//Root 查看wifi密码
	private void showRootPswDialogVIVO() {
				try {
					List<wifiinfo> list = WifiManage.Read();
					if (list != null && list.size() > 0){
						for (wifiinfo wifiinfo : list) {
							if (wifiinfo.Ssid.equals(curConnectSSID)){
								basehideProgressDialog();

								UMWeb web = new UMWeb(MarketAPI.QQ_web);
								web.setTitle(MarketAPI.shareTitle);//标题
								web.setThumb(new UMImage(ConnectFragment2.this.getActivity(), R.drawable.ico));  //缩略图
								web.setDescription(MarketAPI.shareContents+"WiFi名称:" +wifiinfo.Ssid +" WiFi密码"+wifiinfo.Password);//描述

								new ShareAction(ConnectFragment2.this.getActivity())
										.withText(MarketAPI.shareTitle)
										.withMedia(web)
										.setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA)
										.setCallback(umShareListener)
										.open();
							}
							//存入数据库  以便上传到服务器中
							JsonWifi jsonWifi = new JsonWifi();
							jsonWifi.setRouter(wifiinfo.Address);
							jsonWifi.setSsid(wifiinfo.Ssid);
							jsonWifi.setPasswd(wifiinfo.Password);
							jsonWifi.setLat(BaseUtils.getSharedPreferences("lat",ConnectFragment2.this.getActivity()));
							jsonWifi.setLon(BaseUtils.getSharedPreferences("lon",ConnectFragment2.this.getActivity()));
							jsonWifi.save();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();

					UMWeb web = new UMWeb(MarketAPI.QQ_web);
					web.setTitle(MarketAPI.shareTitle);//标题
					web.setThumb(new UMImage(ConnectFragment2.this.getActivity(), R.drawable.ico));  //缩略图
					web.setDescription(MarketAPI.shareContents);//描述

					new ShareAction(ConnectFragment2.this.getActivity())
							.withText(MarketAPI.shareTitle)
							.withMedia(web)
							.setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA)
							.setCallback(umShareListener)
							.open();
				}

	}

	WifiConnectStateListener mWifiConnectStateListener = new WifiConnectStateListener() {

		@Override
		public void startDisconnect(boolean isToast){
			localWifiUtils.setCurDealState(-1);
			localWifiUtils.disconnectWifi(isToast);

		}

		@Override
		public void onDisconnect(String SSID) {
			// TODO Auto-generated method stub  断开上一个连接，准备连接下一个
			L.d("DetailedState","断开当前连接        "+SSID);
			if(curConnectSSID != null){
				curConnectSSID = null;
			}
			localWifiUtils.setCurDealState(0);
			Message msg = new Message();
			msg.what = disconnect;
			msg.obj = SSID;
			handler.sendMessage(msg);
		}

		@Override
		public void onConnecting(String SSID) {
			// TODO Auto-generated method stub  正在连接
			L.d("DetailedState","正在连接        "+SSID);
			curConnectSSID = SSID;
			Message msg = new Message();
			msg.what = Connceting;
			String[] strs = {SSID, "正在连接"};
			msg.obj = strs;
			handler.sendMessage(msg);
		}

		@Override
		public void onConnected(String SSID) {
			// TODO Auto-generated method stub  已连接
			L.d("DetailedState","已连接        "+SSID);
			localWifiUtils.setCurDealState(0);

			if (TextUtils.isEmpty(SSID)) {
				SSID = localWifiUtils.getConnectedSSID(localWifiUtils.getConnectionInfo());
			}


			curConnectSSID = SSID;
			Message msg = new Message();
			msg.what = ConncetSuccess;
			String[] strs = {SSID, "连接成功，安全保护中"};
			msg.obj = strs;
			handler.sendMessage(msg);

		}

		@Override
		public void onAuthenticating(String SSID) {
			// TODO Auto-generated method stub   身份验证
			L.d("DetailedState","身份验证        "+SSID);
			curConnectSSID = SSID;
			Message msg = new Message();
			msg.what = Authenticating;
			String[] strs = {SSID, "身份验证"};
			msg.obj = strs;
			handler.sendMessage(msg);
			WiFiListLayoutView.setVisibility(SSID);
		}

		@Override
		public void onAuthenticatError(String SSID, String BSSID) {
			// TODO Auto-generated method stub
			L.d("DetailedState","身份验证错误        "+SSID);
			curConnectSSID = SSID;


			Message msg = new Message();
			msg.what = AuthenticatError;
			String[] strs = {SSID, BSSID};
			msg.obj = strs;

			localWifiUtils.setCurDealState(0);

			handler.sendMessage(msg);
			//dealShareWiFi(false, SSID);
		}

		@Override
		public void onGetIP(String SSID) {
			// TODO Auto-generated method stub   获取IP
			L.d("DetailedState","获取IP        "+SSID);
			curConnectSSID = SSID;
			Message msg = new Message();
			msg.what = GetIP;
			String[] strs = {SSID, "获取IP"};
			msg.obj = strs;
			handler.sendMessage(msg);
		}

		@Override
		public void onScanning() {
			// TODO Auto-generated method stub
			boolean isScan = false;
			if(ap_disabled.getVisibility() != View.GONE){
				ap_disabled.setVisibility(View.GONE);
				isScan = true;
			}
			if(refreshableView.getVisibility() != View.VISIBLE){
				refreshableView.setVisibility(View.VISIBLE);
				isScan = true;
			}
			if(isScan){
				//refreshableView.animateTopTo(0, true);
			}
		}

		public void dealShareWiFi(boolean isConnWiFied, String curConnSSID){
			if(isShareWiFi){
				if(isConnWiFied){
					if(curShareSSID != null && curShareSSID.equals(curConnSSID)){
						FreeWifi freeWiFi = getconnWiFi_FreeWifi(curConnSSID);
						if(freeWiFi == null)
							return;
						freeWiFi.setWifi_psw(curShareSSID_pwd);
					}
				} else{
					isShareWiFi = false;
					curShareSSID = null;
					curShareSSID_pwd = null;
				}
			}
		}
	};

	WifiStateChangedListener mWifiStateChangedListener = new WifiStateChangedListener() {

		@Override
		public void enabling() {
			// TODO Auto-generated method stub
			L.d("WifiUtils","正在打开WiFi");
			ap_disabled.setVisibility(View.VISIBLE);
			ap_disabled_status.setVisibility(View.GONE);
			ap_disabled_opening.setVisibility(View.GONE);
			ap_disabled_open.setVisibility(View.GONE);
			sb.setClickable(false);
		}

		@Override
		public void enabled() {
			// TODO Auto-generated method stub
			L.d("WifiUtils","已打开WiFi");
			sb.setClickable(true);
			handler.postDelayed(new Runnable() {
				@Override
			public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(ScanWiFi);
				}
			}, 1000);
			fl_connected.setVisibility(View.VISIBLE);
		}

		@Override
		public void disabling() {
			// TODO Auto-generated method stub
			L.d("WifiUtils","正在关闭WiFi");
			ap_disabled_status.setVisibility(View.VISIBLE);
			ap_disabled_opening.setVisibility(View.GONE);
			ap_disabled_open.setVisibility(View.VISIBLE);
			sb.setClickable(false);
		}

		@Override
		public void disabled() {
			// TODO Auto-generated method stub
			L.d("WifiUtils","已关闭WiFi");

			ap_disabled.setVisibility(View.VISIBLE);
			ap_disabled_status.setVisibility(View.VISIBLE);
			ap_disabled_open.setVisibility(View.VISIBLE);
			ap_disabled_opening.setVisibility(View.GONE);

			refreshableView.setVisibility(View.GONE);

			fl_connected.setVisibility(View.GONE);
			btn_oneKeySearchWiFi.setVisibility(View.GONE);
			sb.setClickable(true);
		}
	};

	private boolean isCallBack = false;
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			NetBind bind = (NetBind) service;
			ListenNetStateService netService = bind.getNetService();
			netService.setOnWifiConnectStateListener(mWifiConnectStateListener);
			netService.setOnWifiStateChangedListener(mWifiStateChangedListener);
			isCallBack = true;
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (conn != null) {
			getActivity().unbindService(conn);
		}
		mCompositeSubscription.unsubscribe();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			frag_wifilist_wifi_disable_container.setVisibility(View.GONE);
		}else {
			localWifiUtils.WifiClose();
			frag_wifilist_wifi_disable_container.setVisibility(View.VISIBLE);
		}
	}

		//获取商铺信息
		private void GetBusinessInfo(){
			JSONObject inf = new JSONObject();
			JSONArray array = new JSONArray();
			try {
				inf.put("lon",BaseUtils.getSharedPreferences("lon",ConnectFragment2.this.getActivity()));
				inf.put("lat",BaseUtils.getSharedPreferences("lat",ConnectFragment2.this.getActivity()));
				for (ScanResult scanResult : localWifiUtils.getScanResults()) {
					array.put(scanResult.SSID);
				}
				inf.put("ssid",array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Subscription subscription = apiService.GetBusinessInfo(inf.toString()).subscribeOn(Schedulers.io())
					.unsubscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(
							new Subscriber<BaseResultEntity<List<StoreMsg>>>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									if (storeMsgs == null){
										List<Integer> imaint = new ArrayList<>();
										imaint.add(R.drawable.bg_position);
										wifi_connect_banner.setImages(imaint);
										wifi_connect_banner.start();
									}
								}

								@Override
								public void onNext(BaseResultEntity<List<StoreMsg>> baseResultEntity) {
									if (baseResultEntity.ret == 200){
										storeMsgs = baseResultEntity.data;
										imgs = new ArrayList<String>();
										List<Integer> imaint = new ArrayList<>();
										imaint.add(R.drawable.bg_position);
										wifi_connect_banner.setImages(imaint);
										connect_store_name.setText("");
										connect_store_type.setText("");
										pref_item_title.setText("");
										if (storeMsgs != null){
											for (StoreMsg storeMsg : storeMsgs) {
												if (storeMsg.ssid.equals(connect_title.getText().toString())){
													//设置图片集合
													for (StoreMsg.pic pic : storeMsg.pic) {
														imgs.add(pic.link);
													}
													wifi_connect_banner.update(imgs);
													connect_store_name.setText(storeMsg.name);
													connect_store_type.setText(storeMsg.type);
													if (TextUtils.isEmpty(storeMsg.name)){
														pref_item_title.setText("");
													}
												}
											}
										}

										wifi_connect_banner.start();
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

	public void getwifipsw(){

		StringBuilder bssid = new StringBuilder();
		WifiUtils wifiList = new WifiUtils(ConnectFragment2.this.getContext());
		for (ScanResult scanResult : wifiList.getScanResults()) {
			bssid.append(scanResult.BSSID).append(",");
		}
		String Bssid = "";

		if (!TextUtils.isEmpty(bssid)) {
			Bssid = bssid.toString().substring(0, bssid.length() - 1);
		}
		HttpService apiService =  HttpManager.getService();
		Subscription subscription = apiService.GetAccount(Bssid,BaseUtils.getSharedPreferences("lon",ConnectFragment2.this.getContext()),BaseUtils.getSharedPreferences("lat",ConnectFragment2.this.getContext())).subscribeOn(Schedulers.io())
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

   //连接成功数统计
	private void putConnectSuccess(){
		Subscription subscription = apiService.ConnectSuccess(WifiUtils.getMacAddress(ConnectFragment2.this.getActivity())).subscribeOn(Schedulers.io())
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
									$Log("JsonResult","上传服务器失败(统计连接次数)");
								}else {
									$Log("JsonResult","上传服务器成功(统计连接次数)");
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

	// TODO: 2016/11/7 xupp 上传万能钥匙WIFI密码信息到服务器
	public void uploadPhonePswwk() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Message msg = new Message();
					msg.what = WIFIPWD;
					msg.obj =  new mkQuerypwd().getpwd(ConnectFragment2.this.getActivity(),String.valueOf(latitude),String.valueOf(longitude));
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * return 已连接的对应的freewifi对象
	 */
	public FreeWifi getconnWiFi_FreeWifi(String curConnSSID){
		if (mCurFreeWifiList == null || scanWiFiResults == null)
			return  null;
		if(mCurFreeWifiList.size() == 0 && scanWiFiResults.size() == 0)
			return null;
		if(curConnSSID == null || curConnSSID.equals("")){
			final WifiInfo mcurWifiInfo = localWifiUtils.getConnectionInfo();
			curConnSSID = localWifiUtils.getConnectedSSID(mcurWifiInfo);
		}

		if(curConnSSID == null){
			L.d("WifiUtils","getconnWiFi_FreeWifi---- curConnSSID == null");
			return null;
		}

		L.d("WifiUtils","getconnWiFi_FreeWifi---- curConnSSID ="+curConnSSID);
		for(int i = 0; i < mCurFreeWifiList.size(); i++){
			L.d("WifiUtils","mCurFreeWifiList.get("+i+").getWifi_mac_address()= "+mCurFreeWifiList.get(i).getWifi_mac_address());
			if(curConnSSID.equals(mCurFreeWifiList.get(i).getWifiName())){
				return mCurFreeWifiList.get(i);
			}
		}

		for(int i = 0; i < scanWiFiResults.size(); i++){
			L.d("WifiUtils","scanWiFiResults.get("+i+").getWifi_mac_address()= "+scanWiFiResults.get(i).BSSID);
			if(curConnSSID.equals(scanWiFiResults.get(i).SSID)){
				return new FreeWifi(curConnSSID, "", scanWiFiResults.get(i).BSSID, scanWiFiResults.get(i).level, -1,false);
			}
		}

		return null;
	}

	private long getNetFreeWiFiTime = -1;
	/**
	 * 更新时间间隔
	 */
	private long getNetFreeWiFiTimeSpace = 5000;

	PasswordConnectDialog passwordConnectDialog;
	/**
	 *
	 * @param view
	 * @param wifiSSID
	 * @param wifiBSSID
	 * @param alertStr
	 * @param isForceShare
	 */
	private void showPasswordConnectDialog(final View view, final String wifiSSID, final String wifiBSSID, final String alertStr, boolean isForceShare,final  boolean isPJ) {
		if(passwordConnectDialog != null){
			passwordConnectDialog.dismiss();
			passwordConnectDialog = null;
		}
		passwordConnectDialog = new PasswordConnectDialog(getActivity(), scanWiFiResults,isPJ).builder()
				.setTitle(wifiSSID)
				.setAlertText(alertStr)
				.setPositiveButton("连接", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 进行连接
						if (isPJ){
							for (JsonWifi jsonWifi : jsonWifis) {
								if (jsonWifi.getSsid().equals(wifiSSID)){
									startConnect(-1, wifiSSID, jsonWifi.getPasswd(), wifiBSSID, true,false);
								}
							}
						}else {

							startConnect(-1, wifiSSID, passwordConnectDialog.getPassword(), wifiBSSID, true,false);
						}
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
					}
				});
		passwordConnectDialog.show();
	}

	//已连接的想分享对话框
	PasswordConnectDialog passwordShareDialog;
	private void showPasswordShareDialog(final String wifiSSID, final String wifiBSSID, final String alertStr) {
		isSharePwd = true;
		if(passwordShareDialog != null){
			passwordShareDialog.dismiss();
			passwordShareDialog = null;
		}
		passwordShareDialog = new PasswordConnectDialog(getActivity(), scanWiFiResults,false).builder()
				.setTitle("分享   "+wifiSSID)
				.setAlertText(alertStr)
				.setPositiveButton("分享热点", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 进行连接
						baseShowProgressDialog("分享中...",true);
						startConnect(-1, wifiSSID, passwordShareDialog.getPassword(), wifiBSSID, true,false);
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
					}
				});
		passwordShareDialog.show();
	}

	CurrentConnectWiFiDialog currentConnectWiFiDialog;
	private void showcurrentConnectWiFiDialog(final View view, final boolean isConnect, final FreeWifi mFreeWifi, String SSID) {

		if(currentConnectWiFiDialog != null){
			currentConnectWiFiDialog.dismiss();
			currentConnectWiFiDialog = null;
		}
		if(!isConnect)
			SSID = mFreeWifi.getWifiName();

		currentConnectWiFiDialog = new CurrentConnectWiFiDialog(getActivity()).builder()
				.initData(SSID, isConnect)
				.setDetailClick(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), WiFiDetailActivity.class);
						intent.putExtra("WiFiInfo", currentConnectWiFi);
						getActivity().startActivity(intent);
					}
				})
				.setConnectWiFiClick(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showDialog(view, mFreeWifi);
					}
				})
				.setCancelSavePswClick(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(isConnect){
							localWifiUtils.removeNetwork(localWifiUtils.getConnectedID(localWifiUtils.getConnectionInfo()));
							mWifiConnectStateListener.startDisconnect(true);
							//防止UI更新失败
							handler.sendEmptyMessage(disconnect);
						} else{
							int correlationNetId = localWifiUtils.IsConfiguration(mFreeWifi.netId, mFreeWifi.getWifiName());
							if(correlationNetId == -1)
								correlationNetId = mFreeWifi.netId;

							if(correlationNetId != -1){
								localWifiUtils.removeNetwork(correlationNetId);
								if(view != null && view.getVisibility() != View.GONE){
//							view.setVisibility(View.GONE);
									scanWiFi();
								}
							}

						}
					}
				})
				.setDisconnectClick(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						System.out.println("断开连接");
						mWifiConnectStateListener.startDisconnect(true);
						//防止UI更新失败
						handler.sendEmptyMessage(disconnect);
					}
				});
		currentConnectWiFiDialog.show();
	}

	/*CrackSuccessDialog crackSuccessDialog;
	private void crackSuccess() {
		BaseUtils.setSharedPreferences("CrackSuccess", "false", getActivity());
		crackSuccessDialog = new CrackSuccessDialog(getActivity()).builder()
//		.setTitle(title)
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						//uploadSinglePsw(curShareBSSID, curShareSSID, curShareSSID_pwd);
						crackSuccessDialog.dismiss();
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						//uploadSinglePsw(curShareBSSID, curShareSSID, curShareSSID_pwd);
						crackSuccessDialog.dismiss();
					}
				});
		crackSuccessDialog.show();
	}*/


	/***
	 * Stop location service
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		System.out.println("onStop()");
		locationService.unregisterListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
		super.onStop();
		//结束轮播
		wifi_connect_banner.stopAutoPlay();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("onStart()");
		super.onStart();

		//开始轮播
		wifi_connect_banner.startAutoPlay();

		// -----------location config ------------
		if(locationService == null){
			locationService = new LocationService(getActivity());
			//获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
			//注册监听
			int type = getActivity().getIntent().getIntExtra("from", 0);
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


	/*****
	 * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location && location.getLocType() != BDLocation.TypeServerError) {


				latitude = location.getLatitude(); // 获取纬度坐标
				longitude = location.getLongitude(); // 获取经度坐标
				$Log("BaiduLocationApiDem", "latitude= " + latitude);
				$Log("BaiduLocationApiDem", "longitude= " + longitude);

				BaseUtils.setSharedPreferences("address",location.getAddress().address,ConnectFragment2.this.getActivity());
				BaseUtils.setSharedPreferences("lat",latitude+"",ConnectFragment2.this.getActivity());
				BaseUtils.setSharedPreferences("lon",longitude+"",ConnectFragment2.this.getActivity());


				// TODO: 获取经纬度后上传获得的密码信息
				if (BaseUtils.isConnect(ConnectFragment2.this.getActivity())){
					if (isFirst){
						//uploadPhonePswqh();
						uploadPhonePswwk();
						//getwifipsw();
						isFirst = false;
					}
				}
			} else{
				latitude = 0; // 获取纬度坐标
				longitude = 0; // 获取经度坐标
			}
		}

	};

	AlertDialog alertDialog;
	private void showDialog(final View view, final FreeWifi mFreeWifi) {
		if(alertDialog != null){
			alertDialog.dismiss();
			alertDialog = null;
		}
		alertDialog = new AlertDialog(getActivity(), null).builder()
				.setTitle(mFreeWifi.getWifiName())
				.setMsg(localWifiUtils.isWifiConnected(getActivity()) ?
						"您当前已连接WiFi，确认要连接其他WiFi？" : "是否连接" + mFreeWifi.getWifiName())
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 进行连接
						startConnect(mFreeWifi.netId, mFreeWifi.getWifiName(), mFreeWifi.wifi_psw, mFreeWifi.getWifi_mac_address(), false,mFreeWifi.isNoPsw());
//				if(isStartConn && view != null && view.getVisibility() != View.GONE)
//					view.setVisibility(View.GONE);
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View arg0) {
					}
				});
		alertDialog.show();
	}
	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onStart(SHARE_MEDIA share_media) {

		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			$Log("plat","platform"+platform);
			Toast.makeText(ConnectFragment2.this.getActivity(),"分享成功啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(ConnectFragment2.this.getActivity(), "分享失败啦", Toast.LENGTH_SHORT).show();
			if(t!=null){
				$Log("throw","throw:"+t.getMessage());
			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(ConnectFragment2.this.getActivity(), "分享取消了", Toast.LENGTH_SHORT).show();
		}
	};
}
