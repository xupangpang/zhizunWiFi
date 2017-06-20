package com.zhizun.zhizunwifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.DeviceUtil;


public class AboutMeActivity extends BaseActivity {
	private ImageView headerLeft;
    private TextView headerTitle;
	private TextView about_appvasion;
	private RelativeLayout about_functionin_gn;
	private RelativeLayout about_functionin_app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me_main);

		headerLeft = (ImageView)findViewById(R.id.headerLeft);
		headerLeft.setOnClickListener(this);

		headerTitle = (TextView)findViewById(R.id.headerTitle);
		headerTitle.setText("关于我们");

		about_appvasion = (TextView)findViewById(R.id.about_appvasion);
		about_appvasion.setText("版本 "+DeviceUtil.getVersionName(this));

		about_functionin_gn = (RelativeLayout)findViewById(R.id.about_functionin_gn);
		about_functionin_gn.setOnClickListener(this);

		about_functionin_app = (RelativeLayout)findViewById(R.id.about_functionin_app);
		about_functionin_app.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.headerLeft:
				finish();
				break;
			case R.id.about_functionin_gn:
				Intent intent = new Intent(this,FunctionInActivity.class);
				startActivity(intent);
				break;
			case R.id.about_functionin_app:
				Intent intent1 = new Intent(this,ApplicationIntroducedActivity.class);
				startActivity(intent1);
				break;
		}

	}
}
