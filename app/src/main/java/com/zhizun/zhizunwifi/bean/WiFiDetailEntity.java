package com.zhizun.zhizunwifi.bean;

import java.io.Serializable;

/**
 * @author xupp
 * @date 2017/4/13
 */

public class WiFiDetailEntity  implements Serializable {
    private String wifiName;
    private int wifiIntensity;//信号强度
    private boolean wifiEncryption;//加密方式
    private String wifiLocation;
    private int wifiType;//0 以保存的  1 未保存的
    public int netId = -1;
    public String wifi_mac_address;
    public String wifi_psw;
    public String ip_address;
    public int maxspeed;
    public boolean isPj;

    public WiFiDetailEntity(){

    }

    public WiFiDetailEntity(String wifiname, int wifiintensity,boolean wifiencryption,String wifilocation,int type,int netId,String bssid,String psw,boolean pj) {
        this.wifiName = wifiname;
        this.wifiIntensity = wifiintensity;
        this.wifiEncryption = wifiencryption;
        this.wifiLocation = wifilocation;
        this.wifiType = type;
        this.netId = netId;
        this.wifi_mac_address = bssid;
        this.wifi_psw = psw;
        this.isPj = pj;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public int getWifiIntensity() {
        return wifiIntensity;
    }

    public void setWifiIntensity(int wifiIntensity) {
        this.wifiIntensity = wifiIntensity;
    }

    public boolean getWifiEncryption() {
        return wifiEncryption;
    }

    public void setWifiEncryption(boolean wifiEncryption) {
        this.wifiEncryption = wifiEncryption;
    }

    public String getWifiLocation() {
        return wifiLocation;
    }

    public void setWifiLocation(String wifiLocation) {
        this.wifiLocation = wifiLocation;
    }

    public int getWifiType() {
        return wifiType;
    }

    public void setWifiType(int wifiType) {
        this.wifiType = wifiType;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public String getWifi_mac_address() {
        return wifi_mac_address;
    }

    public void setWifi_mac_address(String wifi_mac_address) {
        this.wifi_mac_address = wifi_mac_address;
    }

    public String getWifi_psw() {
        return wifi_psw;
    }

    public void setWifi_psw(String wifi_psw) {
        this.wifi_psw = wifi_psw;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public int getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(int maxspeed) {
        this.maxspeed = maxspeed;
    }

    public boolean isPj() {
        return isPj;
    }

    public void setPj(boolean pj) {
        isPj = pj;
    }
}
