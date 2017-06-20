package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author xupp
 * @date 2017/5/25
 */

public class BussCooper {
    @SerializedName("ssid")
    private String ssid;
    @SerializedName("status")
    private String status;
    @SerializedName("business")
    private StoreMsg business;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StoreMsg getBusiness() {
        return business;
    }

    public void setBusiness(StoreMsg business) {
        this.business = business;
    }
}
