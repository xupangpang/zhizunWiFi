package com.zhizun.zhizunwifi.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author xupp
 * @date 2017/5/9
 */

public class InstallManger {
    @SerializedName("img_url")
    private String APPicon;
    @SerializedName("app_name")
    private String APPname;
    @SerializedName("app_size")
    private String APPsize;
    private int APPstag;//0 下载 1 安装  2 暂停  3继续
    @SerializedName("app_url")
    private String APPurl;
    private int Maxprogress;
    private int Progress;

    public String getAPPicon() {
        return APPicon;
    }

    public void setAPPicon(String APPicon) {
        this.APPicon = APPicon;
    }

    public String getAPPname() {
        return APPname;
    }

    public void setAPPname(String APPname) {
        this.APPname = APPname;
    }

    public String getAPPsize() {
        return APPsize;
    }

    public void setAPPsize(String APPsize) {
        this.APPsize = APPsize;
    }

    public int getAPPstag() {
        return APPstag;
    }

    public void setAPPstag(int APPstag) {
        this.APPstag = APPstag;
    }

    public String getAPPurl() {
        return APPurl;
    }

    public void setAPPurl(String APPurl) {
        this.APPurl = APPurl;
    }

    public int getMaxprogress() {
        return Maxprogress;
    }

    public void setMaxprogress(int maxprogress) {
        Maxprogress = maxprogress;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }
}
