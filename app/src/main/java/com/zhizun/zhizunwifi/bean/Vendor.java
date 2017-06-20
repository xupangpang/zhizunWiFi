package com.zhizun.zhizunwifi.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author xupp
 * @date 2017/4/19
 */

public class Vendor implements Serializable {
    private String macAddress;
    private String netCardName;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getNetCardName() {
        return netCardName;
    }

    public void setNetCardName(String netCardName) {
        this.netCardName = netCardName;
    }
}
