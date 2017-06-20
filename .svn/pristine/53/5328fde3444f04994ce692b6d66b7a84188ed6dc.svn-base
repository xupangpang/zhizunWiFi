package com.zhizun.zhizunwifi.activity;

import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.BaseUtils;

/**
 *
 */
public class WebInfoActivity extends BaseActivity {
	private ImageView back;
	private TextView btitle;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webinfo);
		initView();

	}
	private void initView() {
		back = (ImageView) findViewById(R.id.headerLeft);
		back.setOnClickListener(this);
		btitle = (TextView) findViewById(R.id.headerTitle);
		mWebView = (WebView) findViewById(R.id.webview_webinfo);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				String mValue = Uri.parse(url).getQueryParameter("id");
//				if (mValue == null) {
				mWebView.loadUrl(url);
//
//
//				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT >= 19) {
					mWebView.getSettings().setLoadsImagesAutomatically(true);
				} else {
					mWebView.getSettings().setLoadsImagesAutomatically(false);
				}

				return;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
					mWebView.getSettings().setLoadsImagesAutomatically(true);
				}
			}


		});
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				btitle.setText(title);
				super.onReceivedTitle(view, title);
			}
		});

		webSet();
		mWebView.loadUrl(getIntent().getStringExtra("url"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.headerLeft:
				finish();
				break;

			default:
				break;

		}

	}
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void webSet() {
		WebSettings webSetting = mWebView.getSettings();
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



	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack())
			mWebView.goBack();
		else
			finish();
	}
}
