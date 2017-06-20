package com.zhizun.zhizunwifi.activity;

import com.zhizun.zhizunwifi.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @项目名: ZhizunWIFI
 * @包名: com.zhizun.zhizunwifi.activity
 * @类名: BlacklistDataEmpty
 * @创建者: 梁辉
 * @创建时间: 2016-3-23 上午9:26:09
 * @描述: 暂时使用 以后有了真正的黑名单数据后 使用fragment来做状态切换
 *
 * @SVN版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class BlacklistDataEmptyActivity extends BaseActivity implements OnClickListener{
	private ImageView back;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blacklist_data_empty);

		back = (ImageView) findViewById(R.id.headerLeft);
		title = (TextView) findViewById(R.id.headerTitle);

		back.setOnClickListener(this);
		title.setText("WiFi黑名单");
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
