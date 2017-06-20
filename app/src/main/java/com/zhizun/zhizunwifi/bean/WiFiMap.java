package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author xupp
 * @date 2017/5/31
 */

public class WiFiMap {
    @SerializedName("lat")
    public double lat;
    @SerializedName("lon")
    public double lon;
    @SerializedName("ssid")
    public String ssid;
}
