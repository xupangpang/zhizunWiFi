package com.zhizun.zhizunwifi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zzad.media.InterFace.SplashListener;
import com.zzad.media.StartSDK;

/**
 * Created by 开屏广告 on 2016/12/14.
 */

public class OpeningADActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //9500广告初始化
        StartSDK.initSDK(this);

        final String isGuide = BaseUtils.getSharedPreferences("isGuide", this);
        /*
         开屏广告，必须在初始化之后调用
        */
        StartSDK.ShowAplash(this, new SplashListener() {
            @Override
            public void Finish() {
                //判断是否是第一次打开应用
                if(isGuide != null && isGuide.equals("true")){
                    Intent intent = new Intent(OpeningADActivity.this, MainTabActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(OpeningADActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    // 防止用户返回键退出APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

