package com.zhizun.zhizunwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.view.SwitchButtonBlue;

/**
 * @项目名: ZhizunWIFI
 * @包名: com.zhizun.zhizunwifi.activity
 * @类名: WiFiSettingActivity
 * @创建者: 梁辉
 * @创建时间: 2016-3-22 上午9:41:15
 * @描述: TODO
 *
 * @SVN版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class WiFiSettingActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back;
	private TextView title;

	// 三个滑动开关
	private SwitchButtonBlue floatWindow; // 桌面悬浮窗
	private SwitchButtonBlue closeFlow; // 连上wifi后关闭数据流量
	private SwitchButtonBlue autoConnect; // 掉线后自动连接
	private RelativeLayout blackList; // wifi黑名单

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_wifi);

		back = (ImageView) findViewById(R.id.headerLeft);
		title = (TextView) findViewById(R.id.headerTitle);
		// 三个滑动开关
		floatWindow = (SwitchButtonBlue) findViewById(R.id.switch_float);
		closeFlow = (SwitchButtonBlue) findViewById(R.id.switch_closeflow);
		autoConnect = (SwitchButtonBlue) findViewById(R.id.switch_autoconnect);
		blackList = (RelativeLayout) findViewById(R.id.blacklist);

		back.setOnClickListener(this);
		blackList.setOnClickListener(this);
		title.setText("WiFi设置");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.headerLeft:
				finish();
				break;
			case R.id.blacklist:
				Intent intent3 = new Intent(this, BlacklistDataEmptyActivity.class);
				startActivity(intent3);
				break;

			default:
				break;
		}
	}

}
