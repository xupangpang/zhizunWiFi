package com.zhizun.zhizunwifi.activity;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;

public class RegistAndLoginActivity extends BaseActivity {

	@InjectView(id = R.id.et_account)
	EditText et_account;
	@InjectView(id = R.id.et_password)
	EditText et_password;
	@InjectView(id = R.id.btn_reg_log, click = "click")
	Button btn_reg_log;
	@InjectView(id = R.id.tv_regist, click = "click")
	TextView tv_regist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_log);
		initView();
	}

	private void initView() {
		// 设置左侧自定义图标不可点击，出现系统自带的返回箭头，点击可finish
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// 设置左侧自定义图标可点击
//		getActionBar().setHomeButtonEnabled(true);

		// 隐藏logo和icon
		getActionBar().setDisplayShowHomeEnabled(false);

		tv_regist.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
		tv_regist.getPaint().setAntiAlias(true);//抗锯齿
	}

	public void click(View v){
		switch (v.getId()) {
			case R.id.tv_regist:
				btn_reg_log.setText("立即注册");
				tv_regist.setVisibility(View.INVISIBLE);
				break;

			case R.id.btn_reg_log:
				if(btn_reg_log.getText().toString().equals("立即注册")){

				}else if(btn_reg_log.getText().toString().equals("立即登录")){

				}
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(btn_reg_log.getText().toString().equals("立即注册")){
					btn_reg_log.setText("立即登录");
					tv_regist.setVisibility(View.VISIBLE);
				}else {
					finish();
				}

				return true;

		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(btn_reg_log.getText().toString().equals("立即注册")){
				btn_reg_log.setText("立即登录");
				tv_regist.setVisibility(View.VISIBLE);
			}else {
				finish();
			}
		}

		return false;
	}
}
