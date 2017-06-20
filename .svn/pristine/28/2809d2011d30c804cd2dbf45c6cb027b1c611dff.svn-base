package com.zhizun.zhizunwifi.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/10/9 09:35
 * Des：设备工具类
 */
public class DeviceUtil {

    // 获取版本号
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    // 获取版本名称
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
