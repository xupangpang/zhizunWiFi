package com.zhizun.zhizunwifi.utils;


import com.zhizun.zhizunwifi.service.QiangHongBaoService;

import android.view.accessibility.AccessibilityEvent;

public interface AccessbilityJob {
    String getTargetPackageName();
    void onCreateJob(QiangHongBaoService qiangHongBaoService);
    void onReceiveJob(AccessibilityEvent event);
    void onStopJob();
    void onNotificationPosted(IStatusBarNotification service);
    boolean isEnable();
}
