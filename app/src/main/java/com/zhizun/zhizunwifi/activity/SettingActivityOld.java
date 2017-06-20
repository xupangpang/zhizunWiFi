package com.zhizun.zhizunwifi.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.MainApplication;
import com.zhizun.zhizunwifi.R;

import net.duohuo.dhroid.ioc.annotation.InjectView;

import java.util.ArrayList;
import java.util.List;

public class SettingActivityOld extends BaseActivity {
	@InjectView(id = R.id.act_wifi_back_rl, click = "click")
	RelativeLayout act_wifi_back_rl;
	@InjectView(id = R.id.act_wifi_back_wiperswitch)
	CheckBox act_wifi_back_wiperswitch;

	@InjectView(id = R.id.act_wifi_show_info_rl, click = "click")
	RelativeLayout act_wifi_show_info_rl;
	@InjectView(id = R.id.act_wifi_show_info_wiperswitch)
	CheckBox act_wifi_show_info_wiperswitch;

	@InjectView(id = R.id.act_wifi_update_rl, click = "click")
	RelativeLayout act_wifi_update_rl;
	@InjectView(id = R.id.act_wifi_update_wiperswitch)
	CheckBox act_wifi_update_wiperswitch;

	@InjectView(id = R.id.act_wifi_exit_rl, click = "click")
	RelativeLayout act_wifi_exit_rl;
	@InjectView(id = R.id.act_wifi_exit_wiperswitch)
	CheckBox act_wifi_exit_wiperswitch;

	@InjectView(id = R.id.act_wifi_server_rl, click = "click")
	RelativeLayout act_wifi_server_rl;
	@InjectView(id = R.id.act_wifi_server_wiperswitch)
	CheckBox act_wifi_server_wiperswitch;

	@InjectView(id = R.id.act_wifi_tool_rl, click = "click")
	RelativeLayout act_wifi_tool_rl;
	@InjectView(id = R.id.act_wifi_tool_wiperswitch)
	CheckBox act_wifi_tool_wiperswitch;
	@InjectView(id = R.id.headerLeft, click = "click")
	ImageView headleft;
	@InjectView(id = R.id.headerTitle)
	TextView headerTitle;

	String[] configName = new String[]
			{"act_wifi_back_wiperswitch","act_wifi_show_info_wiperswitch","act_wifi_update_wiperswitch",
					"act_wifi_exit_wiperswitch","act_wifi_server_wiperswitch","act_wifi_tool_wiperswitch"};
	List<CheckBox> checkBoxs = new ArrayList<CheckBox>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_old);

		initView();
		readConfigure();
	}

	private void initView() {
		// 设置左侧自定义图标不可点击，出现系统自带的返回箭头，点击可finish
//		getActionBar().setDisplayHomeAsUpEnabled(true);

		// 设置左侧自定义图标可点击
//		getActionBar().setHomeButtonEnabled(true);

		// 隐藏logo和icon
//		getActionBar().setDisplayShowHomeEnabled(false);

		checkBoxs.add(act_wifi_back_wiperswitch);
		checkBoxs.add(act_wifi_show_info_wiperswitch);
		checkBoxs.add(act_wifi_update_wiperswitch);
		checkBoxs.add(act_wifi_exit_wiperswitch);
		checkBoxs.add(act_wifi_server_wiperswitch);
		checkBoxs.add(act_wifi_tool_wiperswitch);


		headerTitle.setText("应用设置");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				saveConfigure();
				finish();
				return true;

		}
		return false;
	}

	public void click(View v){
		switch (v.getId()) {
			case R.id.act_wifi_back_rl:
				act_wifi_back_wiperswitch.setChecked(!act_wifi_back_wiperswitch.isChecked());
				break;

			case R.id.act_wifi_show_info_rl:
				act_wifi_show_info_wiperswitch.setChecked(!act_wifi_show_info_wiperswitch.isChecked());
				break;
			case R.id.act_wifi_update_rl:
				act_wifi_update_wiperswitch.setChecked(!act_wifi_update_wiperswitch.isChecked());
				break;

			case R.id.act_wifi_exit_rl:
				act_wifi_exit_wiperswitch.setChecked(!act_wifi_exit_wiperswitch.isChecked());
				break;

			case R.id.act_wifi_server_rl:
				act_wifi_server_wiperswitch.setChecked(!act_wifi_server_wiperswitch.isChecked());
				break;

			case R.id.act_wifi_tool_rl:
				act_wifi_tool_wiperswitch.setChecked(!act_wifi_tool_wiperswitch.isChecked());
				break;
			case R.id.headerLeft:
				finish();
				break;
		}
	}

	private void saveConfigure(/*int index, boolean isOpen*/) {
		Editor sharedata = getSharedPreferences(MainApplication.SHAREDNAME, 0).edit();
//		sharedata.putBoolean(configName[index], isOpen);
//		sharedata.commit();
		for(int i = 0; i< configName.length; i++){
			sharedata.putBoolean(configName[i], checkBoxs.get(i).isChecked());
		}
		sharedata.commit();
	}


	private void readConfigure() {
		SharedPreferences sharedata = getSharedPreferences(MainApplication.SHAREDNAME,0);
		for(int i = 0; i< configName.length; i++){
			boolean isOpen = sharedata.getBoolean(configName[i], true);
			checkBoxs.get(i).setChecked(isOpen);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			saveConfigure();
			finish();
		}

		return false;
	}
}
