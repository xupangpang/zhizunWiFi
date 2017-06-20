package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author xupp
 * @date 2017/5/27
 */

public class StoreMsg implements Serializable {
    @SerializedName("ssid")
    public String ssid;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("detailAdress")
    public String detailAdress;
    @SerializedName("type")
    public String type;
    @SerializedName("pic")
    public List<pic> pic;

    public class pic implements Serializable{
        @SerializedName("type")
        public String type;
        @SerializedName("link")
        public String link;
    }
}
