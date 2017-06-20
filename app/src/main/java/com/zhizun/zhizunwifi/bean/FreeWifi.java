package com.zhizun.zhizunwifi.bean;

import java.io.Serializable;

public class FreeWifi  implements Serializable{
	private static final long serialVersionUID = 1L;

	public enum FreeWiFiType{
		NetWifi,localWifi
	}
	private FreeWiFiType mFreeWiFiType = FreeWiFiType.NetWifi;

	public int netId = -1;

	public String user_name;
	public String wifi_name;
	public String wifi_psw;
	public String wifi_mac_address;

	/**
	 * 1代表店铺
	 * 0代表普通免费
	 * -1代表本地
	 */
	public int is_store;
	public String Store_name;

	public int RSSI;// 信号强度
	private String latitude;// 纬度坐标
	private String longitude;// 经度坐标
	private int connectTimers;// 连接次数
	private boolean isNoPsw;//是否不需要密码

	public FreeWifi(){}

	/**
	 * 本地
	 * @param wifiName
	 * @param wifiPassword
	 * @param wifi_macAddress
	 * @param RSSI
	 */
	public FreeWifi(String wifiName, String wifiPassword, String wifi_macAddress, int RSSI, int netId,boolean isnopsw){
		this.wifi_name = wifiName;
		this.wifi_psw = wifiPassword;
		this.wifi_mac_address = wifi_macAddress;
		this.RSSI = RSSI;
		this.netId = netId;

		mFreeWiFiType = FreeWiFiType.localWifi;
		is_store = -1;
		connectTimers = 0;
		this.isNoPsw = isnopsw;
	}

	/**
	 * 网络
	 * @param wifiName
	 * @param wifiPassword
	 * @param wifi_macAddress
	 * @param RSSI
	 */
	public FreeWifi(String wifiName, String wifiPassword, String wifi_macAddress, int is_store, String Store_name, int RSSI, String latitude, String longitude){
		this.wifi_name = wifiName;
		this.wifi_psw = wifiPassword;
		this.wifi_mac_address = wifi_macAddress;

		this.is_store = is_store;
		this.Store_name = Store_name;

		this.RSSI = RSSI;
		this.latitude = latitude;
		this.longitude = longitude;

		mFreeWiFiType = FreeWiFiType.NetWifi;

		connectTimers = 0;
	}

	public FreeWiFiType getmFreeWiFiType() {
		return mFreeWiFiType;
	}

	public void setmFreeWiFiType(FreeWiFiType mFreeWiFiType) {
		this.mFreeWiFiType = mFreeWiFiType;
	}

	public String getWifi_name() {
		return wifi_name;
	}


	public void setWifi_name(String wifi_name) {
		this.wifi_name = wifi_name;
	}

	public String getWifi_psw() {
		return wifi_psw;
	}

	public void setWifi_psw(String wifi_psw) {
		this.wifi_psw = wifi_psw;
	}

	public String getWifi_mac_address() {
		return wifi_mac_address;
	}

	public void setWifi_mac_address(String wifi_mac_address) {
		this.wifi_mac_address = wifi_mac_address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int getRSSI() {
		return RSSI;
	}

	public void setRSSI(int rSSI) {
		RSSI = rSSI;
	}

	public int getConnectTimers() {
		return connectTimers;
	}

	public void setConnectTimers(int connectTimers) {
		this.connectTimers = connectTimers;
	}

	public String getWifiName() {
		return wifi_name.replaceAll("\"", "");
	}

	public int getIs_store() {
		return is_store;
	}

	public String getStore_name() {
		return Store_name;
	}

	public boolean isNoPsw() {
		return isNoPsw;
	}

	public void setNoPsw(boolean noPsw) {
		isNoPsw = noPsw;
	}
}