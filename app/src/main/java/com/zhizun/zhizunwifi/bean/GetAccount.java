package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */

public class GetAccount {
    @SerializedName("match")
    public int match;

    @SerializedName("list")
    public List<String> list;

}
