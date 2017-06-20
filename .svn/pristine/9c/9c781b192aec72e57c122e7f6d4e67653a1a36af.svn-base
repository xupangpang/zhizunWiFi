package com.zhizun.zhizunwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.DeviceUtil;


public class FunctionInActivity extends BaseActivity {
	private ImageView headerLeft;
    private TextView headerTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.functionin_main);

		headerLeft = (ImageView)findViewById(R.id.headerLeft);
		headerLeft.setOnClickListener(this);

		headerTitle = (TextView)findViewById(R.id.headerTitle);
		headerTitle.setText("功能介绍");

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.headerLeft:
				finish();
				break;
		}

	}
}
