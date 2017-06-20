package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.StoreMsg;
import com.zhizun.zhizunwifi.bean.WiFiDetailEntity;
import com.zhizun.zhizunwifi.dialog.PasswordConnectDialog;
import com.zhizun.zhizunwifi.utils.RxBus;
import com.zhizun.zhizunwifi.utils.WifiUtils;
import com.zhizun.zhizunwifi.widget.ImageViewRoundOval;

import java.util.ArrayList;

public class WiFiDetailsActivity extends BaseActivity{
	private TextView headerTitle;
	private TextView headerRightTextView;
	private TextView wifi_detail_name;//wifi名称
	private TextView wifi_detail_intensity;//信号强度
	private TextView wifi_detail_encryption;//加密方式
	private TextView wifi_detail_location;//wifi位置
	private RelativeLayout wifi_details_paw;//取消密码分享
	private WiFiDetailEntity wiFiDetailEntity;
	private ImageView headerLeft;
	private Button wifi_detail_forgotpassword;
	private Button wifi_detail_connect;
	private WifiUtils wifiUtils;
	private PasswordConnectDialog passwordConnectDialog;
	private TextView wifi_detail_addr;
	private TextView wifi_detail_type;
	private ImageView wifi_detail_addr_line;
	private LinearLayout wifi_detail_store_main;
	private ImageViewRoundOval store_signature;
	private ImageViewRoundOval store_environment;
	private ImageViewRoundOval store_product;
	private LinearLayout store_msg_audit;
	private LinearLayout store_msg_nothrough;
	private net.qiujuer.genius.ui.widget.Button store_msg_chagewifi;
	private StoreMsg storeMsg;
	private Context mcontext = this;
	private String swimage;
	private String snimage;
	private String cpimage;
	private Button wifi_detail_review;
	private LinearLayout store_signature_lay,store_environment_lay,store_product_lay,nobutton_lay;
	private ArrayList<String> Images = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_details_main);

		wifiUtils = new WifiUtils(this);

		Intent intent = this.getIntent();


		initView();


		if (intent.getStringExtra("audit")!= null){
			store_msg_audit.setVisibility(View.VISIBLE);
		}else if (intent.getStringExtra("nothrough")!= null){
			store_msg_nothrough.setVisibility(View.VISIBLE);
		}else {
			wiFiDetailEntity = (WiFiDetailEntity)intent.getSerializableExtra("WiFiDetail");
			storeMsg = (StoreMsg)intent.getSerializableExtra("storemsg");
			setView();
		}

		if (intent.getStringExtra("nobutton") != null){
			nobutton_lay.setVisibility(View.GONE);
			//2017-6-12 取消更改
			headerRightTextView.setVisibility(View.GONE);
			headerRightTextView.setText("更改");
			wifi_detail_review.setVisibility(View.VISIBLE);
		}

		if (intent.getStringExtra("homepage") != null){
			nobutton_lay.setVisibility(View.GONE);
			headerRightTextView.setVisibility(View.GONE);
			wifi_detail_review.setVisibility(View.GONE);
		}
	}

	private void initView() {
		headerTitle = (TextView)findViewById(R.id.headerTitle);
		headerTitle.setText("详情");

		headerLeft = (ImageView)findViewById(R.id.headerLeft);
		headerLeft.setOnClickListener(onClickListener);

		wifi_detail_name = (TextView)findViewById(R.id.wifi_detail_name);
		wifi_detail_intensity = (TextView)findViewById(R.id.wifi_detail_intensity);
		wifi_detail_encryption = (TextView)findViewById(R.id.wifi_detail_encryption);
		wifi_detail_location = (TextView)findViewById(R.id.wifi_detail_location);
		wifi_details_paw = (RelativeLayout)findViewById(R.id.wifi_details_paw);
		wifi_details_paw.setOnClickListener(onClickListener);
		wifi_detail_forgotpassword = (Button)findViewById(R.id.wifi_detail_forgotpassword);
		wifi_detail_forgotpassword.setOnClickListener(onClickListener);
		wifi_detail_connect = (Button)findViewById(R.id.wifi_detail_connect);
		wifi_detail_connect.setOnClickListener(onClickListener);

		//无数据需要隐藏的试图
		wifi_detail_addr = (TextView)findViewById(R.id.wifi_detail_addr);
		wifi_detail_addr_line = (ImageView)findViewById(R.id.wifi_detail_addr_line);
		wifi_detail_store_main = (LinearLayout)findViewById(R.id.wifi_detail_store_main);

		//有店铺信息需要数据的
		wifi_detail_type = (TextView)findViewById(R.id.wifi_detail_type);

		store_signature = (ImageViewRoundOval)findViewById(R.id.store_signature);
		store_signature.setRoundRadius(16);//矩形凹行大小
		store_signature.setType(ImageViewRoundOval.TYPE_ROUND);
		store_signature.setOnClickListener(onClickListener);

		store_environment = (ImageViewRoundOval)findViewById(R.id.store_environment);
		store_environment.setType(ImageViewRoundOval.TYPE_ROUND);
		store_environment.setRoundRadius(16);//矩形凹行大小
		store_environment.setOnClickListener(onClickListener);

		store_product = (ImageViewRoundOval)findViewById(R.id.store_product);
		store_product.setType(ImageViewRoundOval.TYPE_ROUND);
		store_product.setRoundRadius(16);//矩形凹行大小
		store_product.setOnClickListener(onClickListener);

		store_msg_audit = (LinearLayout)findViewById(R.id.store_msg_audit);
		store_msg_nothrough = (LinearLayout)findViewById(R.id.store_msg_nothrough);
		store_msg_chagewifi = (net.qiujuer.genius.ui.widget.Button)findViewById(R.id.store_msg_chagewifi);
		store_msg_chagewifi.setOnClickListener(onClickListener);

		store_signature_lay = (LinearLayout)findViewById(R.id.store_signature_lay);
		store_environment_lay = (LinearLayout)findViewById(R.id.store_environment_lay);
		store_product_lay = (LinearLayout)findViewById(R.id.store_product_lay);

		nobutton_lay = (LinearLayout)findViewById(R.id.nobutton_lay);

		headerRightTextView = (TextView)findViewById(R.id.headerRightTextView);
		headerRightTextView.setOnClickListener(onClickListener);

		wifi_detail_review = (Button)findViewById(R.id.wifi_detail_review);
		wifi_detail_review.setOnClickListener(onClickListener);
	}

	private void setView(){
		wifi_detail_name.setText(wiFiDetailEntity.getWifiName());
		int i = 200 - (wiFiDetailEntity.getWifiIntensity() * 2);
		if(i > 100){
			i = 100;
		}
		wifi_detail_intensity.setText((i) + "%");

		if (wiFiDetailEntity.getWifiEncryption())
		    wifi_detail_encryption.setText("无加密");
		else
			wifi_detail_encryption.setText("WPA/WPA2");

		wifi_detail_location.setText(wiFiDetailEntity.getWifiLocation());

		if (wiFiDetailEntity.getWifiType() == 0){
			wifi_detail_forgotpassword.setVisibility(View.VISIBLE);
		}else {
			wifi_detail_forgotpassword.setVisibility(View.GONE);
		}

		if (storeMsg != null){
			if (!TextUtils.isEmpty(storeMsg.detailAdress)){
				wifi_detail_addr.setVisibility(View.VISIBLE);
				wifi_detail_addr.setText(storeMsg.detailAdress);
			}
			if (!TextUtils.isEmpty(storeMsg.address)){
				wifi_detail_location.setText(storeMsg.address);
			}
			wifi_detail_type.setText(storeMsg.name+" • "+storeMsg.type);
			wifi_detail_addr_line.setVisibility(View.VISIBLE);
			wifi_detail_store_main.setVisibility(View.VISIBLE);


			if (storeMsg.pic != null){
				for (StoreMsg.pic pic : storeMsg.pic) {
					if (pic.type.equals("室外招牌")){
						store_signature_lay.setVisibility(View.VISIBLE);
						swimage = pic.link;
						Glide.with(this).load(pic.link).into(store_signature);
						Images.add(swimage);
					}else if (pic.type.equals("室内环境")){
						store_environment_lay.setVisibility(View.VISIBLE);
						snimage = pic.link;
						Glide.with(this).load(pic.link).into(store_environment);
						Images.add(snimage);
					}else if (pic.type.equals("特色产品")){
						store_product_lay.setVisibility(View.VISIBLE);
						cpimage = pic.link;
						Glide.with(this).load(pic.link).into(store_product);
						Images.add(cpimage);
					}
				}
			}

		}

	}


	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.wifi_details_paw:
					//取消WiFi分享

					break;
				case R.id.headerLeft:
					finish();
					break;
				case R.id.wifi_detail_forgotpassword:
					//忘记密码
					wifiUtils.removeNetwork(wifiUtils.IsConfiguration(wiFiDetailEntity.getWifiName()));
					finish();
					break;
				case R.id.wifi_detail_connect:
					//连接wifi
					if (wiFiDetailEntity.getWifiType() == 0){
						RxBus.getInstance().post(wiFiDetailEntity);
						finish();
					}else {
						if(!WiFiDetailsActivity.this.isFinishing())
						{
							showPasswordConnectDialog(wiFiDetailEntity.isPj());
						}

					}
					break;
				case R.id.store_signature:
					if(!TextUtils.isEmpty(swimage)) {
						Intent intent = new Intent(mcontext, PhotoViewActivity.class);
						intent.putExtra("photoTitle", "室外招牌");
						intent.putExtra("photoShow", false);
						intent.putExtra("photoUrl",swimage);
						startActivity(intent);
					}
					break;
				case R.id.store_environment:
					if(!TextUtils.isEmpty(snimage)) {
						Intent intent = new Intent(mcontext, PhotoViewActivity.class);
						intent.putExtra("photoTitle", "室内环境");
						intent.putExtra("photoShow", false);
						intent.putExtra("photoUrl", snimage);
						startActivity(intent);
					}
					break;
				case R.id.store_product:
					if(!TextUtils.isEmpty(cpimage)) {
						Intent intent = new Intent(mcontext, PhotoViewActivity.class);
						intent.putExtra("photoTitle", "特色产品");
						intent.putExtra("photoShow", false);
						intent.putExtra("photoUrl", cpimage);
						startActivity(intent);
					}
					break;
				case R.id.store_msg_chagewifi:
					finish();
					startActivity(new Intent(mcontext,AddWiFiMessageActivity.class));
					break;
				case R.id.headerRightTextView:
					finish();
					startActivity(new Intent(mcontext,AddWiFiMessageActivity.class));
					break;
				case R.id.wifi_detail_review:
					Intent intent = new Intent(mcontext,StoreReviewActivity.class);
					intent.putStringArrayListExtra("images",Images);
					intent.putExtra("storename",storeMsg.name);
					intent.putExtra("storetype",storeMsg.type);
					startActivity(intent);
					break;

			}
		}
	};

	private void showPasswordConnectDialog(final boolean isBj) {
		if(passwordConnectDialog != null){
			passwordConnectDialog.dismiss();
			passwordConnectDialog = null;
		}
		passwordConnectDialog = new PasswordConnectDialog(this, null,isBj).builder()
				.setTitle(wiFiDetailEntity.getWifiName())
				.setAlertText(null)
				.setPositiveButton("连接", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isBj){

							RxBus.getInstance().post(wiFiDetailEntity);
						}else {
							wiFiDetailEntity.setWifi_psw(passwordConnectDialog.getPassword());
							RxBus.getInstance().post(wiFiDetailEntity);
						}
					}
				})
				.setNegativeButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						passwordConnectDialog.dismiss();
					}
				});
		passwordConnectDialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
