package com.zhizun.zhizunwifi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;


/**
 *
 * @项目名:	ZhizunWIFI
 * @包名:	com.zhizun.zhizunwifi.activity
 * @类名:	TimeCardActivity
 * @创建者:	梁辉
 * @创建时间:	2016年3月16日 上午11:06:23
 * @描述:	TODO
 *
 * @SVN版本:	$Rev: 537 $
 * @更新人:	$Author: liangzi $
 * @更新时间:	$Date: 2016-03-16 18:34:45 +0800 (Wed, 16 Mar 2016) $
 * @更新描述:	TODO
 */
public class TimeCardActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timecard);

		back = (ImageView) findViewById(R.id.headerLeft);
		title = (TextView) findViewById(R.id.headerTitle);

		back.setOnClickListener(this);

		title.setText("我的时长");
	}

	@Override
	public void onClick(View v) {
		if (v == back) {
			finish();
		}

	}
}
