package com.zhizun.zhizunwifi.utils;

import android.widget.TextView;

import com.zhizun.zhizunwifi.widget.DownloadProgressButton;

/**
 * @author xupp
 * @date 2017/5/9
 */

public interface OnProgressListener {
    void updateProgress(int max, int progress,int pos);
}
