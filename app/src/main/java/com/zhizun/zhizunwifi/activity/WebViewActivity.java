package com.zhizun.zhizunwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.BaseUtils;

import net.duohuo.dhroid.ioc.annotation.InjectView;

/**
 * @author xupp
 * @date 2017/5/24
 */

public class WebViewActivity extends BaseActivity{
    @InjectView(id = R.id.headerLeft)
    ImageView headleft;
    @InjectView(id = R.id.headerTitle)
    TextView headerTitle;
    @InjectView(id = R.id.webView_main)
    WebView webView_main;
    private Intent intent0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_main_layout);
        initView();
        intent0 = this.getIntent();
        webView_main.loadUrl(intent0.getStringExtra("Url"));
    }

    private void initView(){
        headleft.setOnClickListener(onClickListener);
        webSet();
        webView_main.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView_main.loadUrl(url);
                return true;
            }
        });
        webView_main.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                headerTitle.setText(title);
            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.headerLeft:
                    finish();
                    break;

            }
        }
    };

    private void webSet() {
        WebSettings webSetting = webView_main.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(BaseUtils.isConnect(this) ? WebSettings.LOAD_DEFAULT :WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }


    @JavascriptInterface
    public void startFunction(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });
    }

}
