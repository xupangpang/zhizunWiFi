package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author xupp
 * @date 2017/6/12
 */

public class JsonWifiMain {
    @SerializedName("all")
    public List<JsonWifiImp> all;
}
