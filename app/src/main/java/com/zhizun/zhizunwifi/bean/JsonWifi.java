package com.zhizun.zhizunwifi.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/8.
 */

public class JsonWifi extends DataSupport implements Serializable {
    @Column(unique=true)
    private String router;
    private String ssid;
    private String passwd;
    private String lon;
    private String lat;
    private int level;

    public String getRoute() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
