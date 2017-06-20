package com.zhizun.zhizunwifi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;


public class ApplicationIntroducedActivity extends BaseActivity {
	private ImageView headerLeft;
    private TextView headerTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appintroduct_main);

		headerLeft = (ImageView)findViewById(R.id.headerLeft);
		headerLeft.setOnClickListener(this);

		headerTitle = (TextView)findViewById(R.id.headerTitle);
		headerTitle.setText("应用简介");

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
