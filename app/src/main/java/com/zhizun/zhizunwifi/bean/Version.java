package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/10.
 */

public class Version {
    @SerializedName("app_name")
    public String app_name;

    @SerializedName("app_version")
    public String app_version;

    @SerializedName("app_url")
    public String app_url;

    @SerializedName("update")
    public String update;

    @SerializedName("desc")
    public String desc;
}
