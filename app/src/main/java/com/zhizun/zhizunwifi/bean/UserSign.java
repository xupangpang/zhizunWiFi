package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/30.
 */

public class UserSign {
    @SerializedName("result")
    public String result;
    @SerializedName("msg")
    public String msg;
    @SerializedName("points")
    public int points;
}
