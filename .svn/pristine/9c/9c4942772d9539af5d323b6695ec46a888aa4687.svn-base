package com.zhizun.zhizunwifi.activity;

import com.zhizun.zhizunwifi.R;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @项目名:	ZhizunWIFI
 * @包名:	com.zhizun.zhizunwifi.activity
 * @类名:	WebFAQActivity
 * @创建者:	梁辉
 * @创建时间:	2016-3-29 下午4:15:20
 * @描述:	TODO
 *
 * @SVN版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class WebFAQActivity extends BaseActivity  {

	private ImageView headerLeft;
	private TextView headerTitle;
	private WebView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_faq);

		headerLeft = (ImageView) findViewById(R.id.headerLeft);
		headerTitle = (TextView) findViewById(R.id.headerTitle);
		content = (WebView) findViewById(R.id.web_FAQ);

		headerLeft.setOnClickListener(this);
		headerTitle.setText("常见问题");
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
}
