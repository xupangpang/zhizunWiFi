package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.utils.WifiApUtils;

/**
 * 个人热点页面
 */
public class PersonalHotspotActivity extends BaseActivity implements OnClickListener {
     private Context mContext = this;
	 private ImageView headerLeft;
	 private TextView headerTitle;
	 private TextView hotspot_name;
	 private TextView hotspot_psw;
	 private CheckBox hotspot_check;
	private WifiApUtils wifiApUtils;
	private RelativeLayout layout_head;
	private RelativeLayout ll_nickarraw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_hotspot_main);
		wifiApUtils = new WifiApUtils(mContext);
		initView();


	}

	private void initView() {
		headerLeft = (ImageView) findViewById(R.id.headerLeft);
		headerTitle = (TextView) findViewById(R.id.headerTitle);
		headerLeft.setOnClickListener(this);
		headerTitle.setText("个人热点");

		hotspot_name = (TextView)findViewById(R.id.hotspot_name);
		hotspot_name.setText(wifiApUtils.getWifiApSSID());

		hotspot_psw = (TextView)findViewById(R.id.hotspot_psw);
		hotspot_psw.setText(wifiApUtils.getWifiApSharedKey());

		layout_head = (RelativeLayout)findViewById(R.id.layout_head);
		layout_head.setOnClickListener(this);
		ll_nickarraw = (RelativeLayout)findViewById(R.id.ll_nickarraw);
		ll_nickarraw.setOnClickListener(this);

		hotspot_check = (CheckBox)findViewById(R.id.hotspot_check);
		if (wifiApUtils.isWifiApOn()){
			hotspot_check.setChecked(true);
		}else {
			hotspot_check.setChecked(false);
		}

		hotspot_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b){
					baseShowProgressDialog("请您稍等片刻",true);
					wifiApUtils.createWifiHotspot(hotspot_name.getText().toString(),hotspot_psw.getText().toString(),handler);
				}else {
					wifiApUtils.setWifiApEnabled(false);
				}
			}
		});

	}


private Handler handler = new Handler(){
	@Override
	public void handleMessage(Message msg) {
		if (msg.what == 0){
			basehideProgressDialog();
			ShowToast("个人热点创建成功");
		}else {
			basehideProgressDialog();
			ShowToast("个人热点创建失败");
			hotspot_check.setChecked(false);
		}
	}
};



	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.headerLeft:// 返回
			finish();
			break;
			case R.id.layout_head://ssid
              Intent intent = new Intent(mContext,PersonalHotspotEnditActivity.class);
				intent.putExtra("titlename","修改热点名称");
				intent.putExtra("hint","请修改热点名称");
				intent.putExtra("value",hotspot_name.getText().toString());
				startActivityForResult(intent,0);
				break;
			case R.id.ll_nickarraw:// 密码
				Intent intent0 = new Intent(mContext,PersonalHotspotEnditActivity.class);
				intent0.putExtra("titlename","修改热点密码");
				intent0.putExtra("hint","请修改热点密码");
				intent0.putExtra("value",hotspot_psw.getText().toString());
				startActivityForResult(intent0,1);
				break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0){
			if (data != null){
				hotspot_name.setText(data.getStringExtra("value"));
			}
		}else if (requestCode == 1){
			if (data != null){
				hotspot_psw.setText(data.getStringExtra("value"));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
