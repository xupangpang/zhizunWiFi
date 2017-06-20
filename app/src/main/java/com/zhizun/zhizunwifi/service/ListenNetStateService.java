package com.zhizun.zhizunwifi.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;

import com.zhizun.zhizunwifi.utils.L;

public class ListenNetStateService extends Service {
	private WifiManager mWifiManager;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                Log.d("WifiUtils", "网络状态已经改变");
//                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//                NetworkInfo mWifi = connectivityManager .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//                NetworkInfo info=intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//
//                boolean isConntect = mWifi.isConnected();
//            	Log.i("WifiUtils","连接WIFI是否成功 = " + isConntect);
//
////            	ConnectFragment.Connecting = false;
//            	if (onGetConnectState != null){
//                    onGetConnectState.GetState(isConntect); // 通知连接网络状态改变
//                }
//
//                info = connectivityManager.getActiveNetworkInfo();
//
//                if(info != null && info.isAvailable()) {
//                    String name = info.getTypeName();
//                    Log.d("WifiUtils", "当前网络名称：" + name);
//                    if (onGetConnectState != null)
//                        onGetConnectState.isAvailable(info.isAvailable());// 通知网络状态改变
//                } else {
//                    Log.d("WifiUtils", "没有可用网络");
//                    if (onGetConnectState != null)
//                        onGetConnectState.isAvailable(false);// 通知网络状态改变
//                }
			}else if(action.equals(WifiManager.RSSI_CHANGED_ACTION)){
				// 信号改变
//    			int strength=getStrength(context);
//    			System.out.println("当前信号 "+strength);
//    			wifiStateImage.setImageLevel(strength);
			}else if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){//监听WIFI开启与关闭广播
				//WIFI开关
				int wifistate=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_DISABLED);
				if(curWifiStateChangedListener != null){
					if(wifistate==WifiManager.WIFI_STATE_ENABLING){
						curWifiStateChangedListener.enabling();
					} else if(wifistate==WifiManager.WIFI_STATE_ENABLED){
						curWifiStateChangedListener.enabled();
					} else if(wifistate==WifiManager.WIFI_STATE_DISABLING){
						curWifiStateChangedListener.disabling();
					} else if(wifistate==WifiManager.WIFI_STATE_DISABLED){
						curWifiStateChangedListener.disabled();
					}
				}
			}else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
				Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (null != parcelableExtra) {
					NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
					L.d("WifiUtils","NETWORK_STATE_CHANGED_ACTION:  "+networkInfo.getState().name());
					if(curWifiConnectStateListener != null){
							String	curConnSSID = networkInfo.getExtraInfo().replaceAll("\"", "");
						if(networkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)){//如果断开连接
							curWifiConnectStateListener.onDisconnect(curConnSSID);
						} else if(networkInfo.getState().equals(NetworkInfo.State.CONNECTING)){//如果断开连接
							if(networkInfo.getDetailedState() == NetworkInfo.DetailedState.AUTHENTICATING){
								curWifiConnectStateListener.onAuthenticating(curConnSSID);
							} else if(networkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR){
								curWifiConnectStateListener.onGetIP(curConnSSID);
							} else{
								curWifiConnectStateListener.onConnecting(curConnSSID);
							}
						} else if(networkInfo.getState().equals(NetworkInfo.State.CONNECTED)){
							if(!curConnSSID.equals("0x") && !curConnSSID.equals("<unknown ssid>")){
								curWifiConnectStateListener.onConnected(curConnSSID);
								if (onGetConnectState != null) {
									onGetConnectState.GetState(curConnSSID, true);
								}
							}
						}
					}
				}
			} else if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){//密码验证错误广播
				SupplicantState state = (SupplicantState) intent.getParcelableExtra(
						WifiManager.EXTRA_NEW_STATE);
//    			 SupplicantState.ASSOCIATING
//    			 SupplicantState.FOUR_WAY_HANDSHAKE
//    			 SupplicantState.DISCONNECTED
//    			 L.d("tag","SUPPLICANT_STATE_CHANGED_ACTION:  "+state.name()+"   ---   "+getWifiSSID(context)+"   ---   "+getWifiMac(context));

				if(curWifiConnectStateListener != null){
					WifiInfo wifiInfo = getWifiInfo(context);
					String SSID = getWifiSSID(wifiInfo);
					String BSSID = getWifiMac(wifiInfo);
					if(state == SupplicantState.ASSOCIATING || state == SupplicantState.ASSOCIATED){
						if(!SSID.equals("0x") && !SSID.equals("<unknown ssid>"))
							curWifiConnectStateListener.onAuthenticating(SSID);
						else
							curWifiConnectStateListener.onDisconnect(SSID);
					} else if(state == SupplicantState.DISCONNECTED){
						if(intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0) == 1){
							if(!SSID.equals("0x") && !SSID.equals("<unknown ssid>")) {
								curWifiConnectStateListener.onAuthenticatError(SSID, BSSID);
								if (onGetConnectState != null) {
									onGetConnectState.GetState(SSID, false);
								}
							}else
								curWifiConnectStateListener.onDisconnect(SSID);
						} else
							curWifiConnectStateListener.onDisconnect(SSID);
					} else if(state == SupplicantState.SCANNING){
						curWifiConnectStateListener.onScanning();
					}
				}
			}
		}
	};

	public void initWifiManager(Context context){
		if(mWifiManager == null)
			mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}

	public WifiInfo getWifiInfo(Context context){
		initWifiManager(context);
		return mWifiManager.getConnectionInfo();
	}

	public String getWifiSSID(WifiInfo wifiInfo){
		if(wifiInfo == null) {
			return "<unknown ssid>";
		}else {

			return wifiInfo.getSSID().replaceAll("\"", "");
		}
	}

	public String getWifiMac(WifiInfo wifiInfo){
		if(wifiInfo == null)
			return "";
		return wifiInfo.getBSSID();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return netBind;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		registerReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public void registerReceiver(){
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	// 网络状态改变之后，通过此接口的实例通知当前网络的状态，此接口在Activity中注入实例对象
	public interface GetConnectState
	{
		public void GetState(String ssid ,boolean isConnected);
		public void isAvailable(boolean isAvailable);
	}

	private GetConnectState onGetConnectState;


	public void setOnGetConnectState(GetConnectState onGetConnectState)
	{
		this.onGetConnectState = onGetConnectState;
	}

	/**
	 * 监听wifi的连接状态的变化回调
	 * @author Administrator
	 *
	 */
	public interface WifiConnectStateListener{
		/**
		 * 执行断开连接操作-外部调用
		 * @param isToast--是否通过toast来打印 断开连接成功 字样
		 */
		public void startDisconnect(boolean isToast);
		/**
		 * 监听断开连接的回调
		 * @param SSID
		 */
		public void onDisconnect(String SSID);
		/**
		 * 监听身份正在验证
		 * @param SSID--当前验证身份的wifi名
		 */
		public void onAuthenticating(String SSID);
		/**
		 * 监听身份验证失败
		 * @param SSID--当前验证身份的wifi名
		 * @param BSSID--当前验证身份的wifi-mac地址--后续操作重连时传递该mac地址存储到本地配置的wifi列表
		 */
		public void onAuthenticatError(String SSID, String BSSID);
		/**
		 * 监听正在获取ip的回调
		 * @param SSID
		 */
		public void onGetIP(String SSID);
		/**
		 * 正在连接
		 * @param SSID
		 */
		public void onConnecting(String SSID);
		/**
		 * 已连接成功
		 * @param SSID
		 */
		public void onConnected(String SSID);
		/**
		 * 扫描wifi的回调
		 */
		public void onScanning();
	}

	/**
	 * 监听wifi的开启及关闭事件的回调
	 * @author
	 *
	 */
	public interface WifiStateChangedListener{
		/**
		 * 正在打开wifi
		 */
		public void enabling();
		/**
		 * 打开wifi完成
		 */
		public void enabled();
		/**
		 * 正在关闭wifi
		 */
		public void disabling();
		/**
		 * 关闭wifi完成
		 */
		public void disabled();
	}

	private WifiConnectStateListener curWifiConnectStateListener;
	public void setOnWifiConnectStateListener(
			WifiConnectStateListener curWifiConnectStateListener){
		this.curWifiConnectStateListener = curWifiConnectStateListener;
	}

	private WifiStateChangedListener curWifiStateChangedListener;
	public void setOnWifiStateChangedListener(
			WifiStateChangedListener curWifiStateChangedListener) {
		this.curWifiStateChangedListener = curWifiStateChangedListener;
	}

	// 定义一个Bind类
	private NetBind netBind = new NetBind();

	public class NetBind extends Binder {
		public ListenNetStateService getNetService() {
			return ListenNetStateService.this;
		}
	}

	// 销毁广播
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}
