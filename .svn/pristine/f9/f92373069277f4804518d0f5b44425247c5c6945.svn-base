package com.zhizun.zhizunwifi.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xupp
 * @date 2017/4/20
 */

public class WifiApUtils {
    private static final String TAG = "wifiHelper";
    private WifiManager mWifiManager = null;

    public WifiApUtils(Context context){
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    //判断WLAN状态是否开启
    public boolean isWifiApOn() {
        Method method = null;
        int i = 0;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApState");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            i = (Integer) method.invoke(mWifiManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "wifi sharing state -> " + i);
        // 10---正在关闭；11---已关闭；12---正在开启；13---已开启
        return i == 13;
    }
    //设置WLAN状态
    public boolean setWifiApEnabled(boolean enabled) {
        Method method = null, configMethod = null;
        boolean result = false;
        if (mWifiManager == null) {
            Log.i(TAG, "mWifiManager is null  -> " + result);
            return result;
        }
        try {
            configMethod = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) configMethod.invoke(mWifiManager);
            result = (boolean) method.invoke(mWifiManager, new Object[]{apConfig, enabled});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "setWifiApEnabled -> " + result);
        return result;
    }

    //获取WLAN ＳＳＩＤ
    public String getWifiApSSID() {
        Method method = null;
        String SSID = null;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) method.invoke(mWifiManager);
            SSID = apConfig.SSID;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getWifiApSSID -> " + SSID);
        return SSID;
    }

    //获取WLAN　密码
    public String getWifiApSharedKey() {
        Method method = null;
        String SharedKey = null;
        try {
            method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            WifiConfiguration apConfig = (WifiConfiguration) method.invoke(mWifiManager);
            SharedKey = apConfig.preSharedKey;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SharedKey;
    }

    public void createWifiHotspot(String ssid,String psw,Handler handler) {
        if (mWifiManager.isWifiEnabled()) {
            //如果wifi处于打开状态，则关闭wifi,
            mWifiManager.setWifiEnabled(false);
        }
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = ssid;
        config.preSharedKey = psw;
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

        //通过反射调用设置热点
        try {
            Method method = mWifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            boolean enable = (Boolean) method.invoke(mWifiManager, config, true);
            if (enable) {
                 handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(1);
        }
    }
}
