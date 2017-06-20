package com.zhizun.zhizunwifi.bean;

import java.io.Serializable;

/**
 * 商铺认证
 *
 * @author Administrator
 *
 */
public class WifiShop implements Serializable {
	private static final long serialVersionUID = 1L;

	// public String objectId;
	public String phone;
	public String wifi_name;
	public String wifi_psw;
	public String wifi_mac_address;
	public String WiFi_Mac;
	public String latitude;// 纬度坐标
	public String longitude;// 经度坐标
	public int RSSI;// 信号强度
	public String connectTimers;// 连接次数

	public String user_name;// 申办人名字
	public String shop_name;// 店名
	public String shop_type;// 店铺类型
	public String shop_image;// 营业执照
	public String user_image;// 身份证
	public boolean is_pass;// 是否审核通过

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_type() {
		return shop_type;
	}

	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}

	public String getShop_image() {
		return shop_image;
	}

	public void setShop_image(String shop_image) {
		this.shop_image = shop_image;
	}

	public String getUser_image() {
		return user_image;
	}

	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}

	public boolean isIs_pass() {
		return is_pass;
	}

	public void setIs_pass(boolean is_pass) {
		this.is_pass = is_pass;
	}

	// public String getObjectId() {
	// return objectId;
	// }
	//
	// public void setObjectId(String objectId) {
	// this.objectId = objectId;
	// }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getWiFi_Mac() {
		return WiFi_Mac;
	}

	public void setWiFi_Mac(String wiFi_Mac) {
		WiFi_Mac = wiFi_Mac;
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

	public String getConnectTimers() {
		return connectTimers;
	}

	public void setConnectTimers(String connectTimers) {
		this.connectTimers = connectTimers;
	}

}