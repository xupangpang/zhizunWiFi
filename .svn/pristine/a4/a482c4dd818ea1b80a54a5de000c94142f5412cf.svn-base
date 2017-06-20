package com.zhizun.zhizunwifi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.NetWorkSpeedInfo;
import com.zhizun.zhizunwifi.bean.WiFiDetailEntity;
import com.zhizun.zhizunwifi.utils.BaseUtils;
import com.zhizun.zhizunwifi.utils.ReadFile;
import com.zhizun.zhizunwifi.utils.ScanDeviceTool;
import com.zhizun.zhizunwifi.utils.WifiUtils;
import com.zhizun.zhizunwifi.utils.router.ActivityDiscovery;
import com.zhizun.zhizunwifi.utils.router.DefaultDiscovery;
import com.zhizun.zhizunwifi.utils.router.DnsDiscovery;
import com.zhizun.zhizunwifi.utils.router.Prefs;
import com.zhizun.zhizunwifi.widget.CircleViewPhy;
import com.zhizun.zhizunwifi.widget.NumberScrollTextView;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 体检测速
 */
public class PhysicalExaminationSpeedActivity extends BaseActivity implements View.OnClickListener {
	private ImageView back;
	private TextView title;
	private Context mContext = this;
	private Animation operatingAnim;
	private Timer timer;
	private Timer timer1;
	private Timer timer2;
	private Timer timer3;
	private Timer timer4;
	private Timer timer5;
	private Timer timer6;

	private ImageView speed_rotating;
	private CircleViewPhy circle_safe;
	private ImageView speed_icon_safe;
	private ImageView network_details_arrow;
	private LinearLayout network_details_item;
	private TextView wifi_detail_name;//wifi 名称
	private TextView signal_strength_name;//信号强度
	private TextView encryption_name;//加密方式
	private TextView max_speed_tv;//最大连接速度
	private TextView ip_address_name;//IP地址
	private TextView mac_address_name;//mac地址
	private WifiUtils wifiUtils;
	private WiFiDetailEntity mDetailEntity;
	private Animation anim;

	private LinearLayout network_security_item;
	private CircleViewPhy circle_speed;
	private ImageView speed_speed;
	private ImageView speed_icon_speed;
	private ImageView network_security_arrow;

	private ImageView network_speed_arrow;
	private int testTime = 10 *1000; // 测试时间
	ReadFile mReadFile;
	boolean isStart = false;
	byte[] imageData = null;
	private String url = "http://dp.anzhuo.com/zzwifi/zzwifi.apk ";
	NetWorkSpeedInfo netWorkSpeedInfo = null;
	private TextView network_speed_text;
	private LinearLayout network_speed_item;
	private LinearLayout network_speed_chat,network_speed_online,network_speed_game,network_speed_video;
	private TextView network_speed_txt;
	private Button to_test_btn;

	private CircleViewPhy circle_equipment;
	private ImageView speed_icon_equipment;
	private ImageView speed_equipment;
	private ImageView online_equipment_arrow;
	private LinearLayout network_equipment_item;
	private TextView network_equipment_size;
	private Button check_the_equipment;
	private int finshNum;

	private ScanDeviceTool scanDeviceTool;

	private LinearLayout discovery_loading_lay;
	private RelativeLayout discovery_finsh_lay;
	private TextView discovery_loading_tv;
	private JumpingBeans jumpingBeans1;
	private NumberScrollTextView phy_exa_num;

	private final int ONEIMGROTATING = 0;
	private final int NEYWORKDETAILS = 1;
	private final int NETWORKSECURTY = 2;
	private final int NETWORKSPEED = 3;
	private final int UPDATE_DNOE = 4;// 完成下载
	private final int NETWORKSPEEDONE = 5;
	private final int NETWORKEQUIPMENTONE = 6;
	private final int NETWORKEQUIPMENT = 7;
	private final int NETWORKEQUIPMENTSIZE = 8;
	private final int NETWORKEFINSH = 9;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phy_exa_speed_main);


		back = (ImageView) findViewById(R.id.headerLeft);
		title = (TextView) findViewById(R.id.headerTitle);
		title.setText("体检测速");
		back.setOnClickListener(this);

		operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_fast);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);

		anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate);

		wifiUtils = new WifiUtils(mContext);

		initNetworkDetails();
		initNetworkSecurity();
		initNetworkSpeed();
		initNetworkEquipment();

		discovery_loading_lay = (LinearLayout)findViewById(R.id.discovery_loading_lay);
		discovery_finsh_lay = (RelativeLayout)findViewById(R.id.discovery_finsh_lay);
		discovery_loading_tv = (TextView)findViewById(R.id.discovery_loading_tv);
		jumpingBeans1 = JumpingBeans.with(discovery_loading_tv)
				.appendJumpingDots()
				.build();

		phy_exa_num = (NumberScrollTextView)findViewById(R.id.phy_exa_num);



		network_details.start();
	}
	private void initNetworkDetails(){
		network_details_arrow = (ImageView)findViewById(R.id.network_details_arrow);
		network_details_arrow.setAnimation(anim);
		network_details_arrow.setVisibility(View.VISIBLE);

		speed_icon_safe = (ImageView)findViewById(R.id.speed_icon_safe);


		speed_rotating = (ImageView)findViewById(R.id.speed_rotating);
		circle_safe = (CircleViewPhy)findViewById(R.id.circle_safe);

		network_details_item = (LinearLayout)findViewById(R.id.network_details_item);
		wifi_detail_name = (TextView)findViewById(R.id.wifi_detail_name);
		signal_strength_name = (TextView)findViewById(R.id.signal_strength_name);
		encryption_name = (TextView)findViewById(R.id.encryption_name);
		max_speed_tv = (TextView)findViewById(R.id.max_speed_name);
		ip_address_name = (TextView)findViewById(R.id.ip_address_name_one);
		mac_address_name = (TextView)findViewById(R.id.mac_address_name);


	}


	private void initNetworkSpeed(){
		circle_speed = (CircleViewPhy)findViewById(R.id.circle_speed);
		network_speed_arrow = (ImageView)findViewById(R.id.network_speed_arrow);
		speed_speed = (ImageView)findViewById(R.id.speed_speed);
		network_speed_text = (TextView)findViewById(R.id.network_speed_text);
		network_speed_item = (LinearLayout)findViewById(R.id.network_speed_item);
		netWorkSpeedInfo = new NetWorkSpeedInfo();
		network_speed_chat = (LinearLayout)findViewById(R.id.network_speed_chat);
		network_speed_online = (LinearLayout)findViewById(R.id.network_speed_online);
		network_speed_game = (LinearLayout)findViewById(R.id.network_speed_game);
		network_speed_video = (LinearLayout)findViewById(R.id.network_speed_video);
		network_speed_txt = (TextView)findViewById(R.id.network_speed_txt);
		to_test_btn = (Button)findViewById(R.id.to_test_btn);
		to_test_btn.setOnClickListener(this);
	}

	private void initNetworkSecurity(){
		network_security_item = (LinearLayout)findViewById(R.id.network_security_item);
		speed_icon_speed = (ImageView)findViewById(R.id.speed_icon_speed);
		network_security_arrow = (ImageView)findViewById(R.id.network_security_arrow);

	}


	private void initNetworkEquipment(){
		circle_equipment = (CircleViewPhy)findViewById(R.id.circle_equipment);
		speed_icon_equipment = (ImageView)findViewById(R.id.speed_icon_equipment);
		speed_equipment = (ImageView)findViewById(R.id.speed_equipment);
		online_equipment_arrow = (ImageView)findViewById(R.id.online_equipment_arrow);
		scanDeviceTool = new ScanDeviceTool();
		network_equipment_item = (LinearLayout)findViewById(R.id.network_equipment_item);
		network_equipment_size = (TextView)findViewById(R.id.network_equipment_size);
		check_the_equipment = (Button)findViewById(R.id.check_the_equipment);
		check_the_equipment.setOnClickListener(this);
	}

	private Thread network_Security = new Thread(){
		@Override
		public void run() {
			  Message message = new Message();
			  message.what = NETWORKSECURTY;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			handler.sendMessage(message);

		}
	};

	private Thread network_details = new Thread(){
		@Override
		public void run() {
			Message message = new Message();
			WiFiDetailEntity detailEntity = new WiFiDetailEntity();
			detailEntity.setWifiName(wifiUtils.getConnectedSSID(wifiUtils.getConnectionInfo()));
			detailEntity.setWifiIntensity(Math.abs( wifiUtils.getConnectedIntensity(wifiUtils.getConnectionInfo())));
			detailEntity.setWifiEncryption(wifiUtils.getConnectedEncryption());
			detailEntity.setMaxspeed(wifiUtils.getConnectedMaxSpeed(wifiUtils.getConnectionInfo()));
			detailEntity.setIp_address(intToIp(wifiUtils.getConnectedIPAddr(wifiUtils.getConnectionInfo())));
			detailEntity.setWifi_mac_address(wifiUtils.getConnectedMacAddr());
			message.obj = detailEntity;
			message.what = NEYWORKDETAILS;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			handler.sendMessage(message);

		}
	};


	private Thread network_equipment = new Thread(){
		@Override
		public void run() {
			Message message = new Message();
			message.obj = scanDeviceTool.scan();
			message.what = NETWORKEQUIPMENTSIZE;

			handler.sendMessage(message);

		}
	};


	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ONEIMGROTATING) {
				speed_rotating.setAnimation(operatingAnim);
				speed_rotating.setVisibility(View.VISIBLE);
			}else if (msg.what == NEYWORKDETAILS){
				if (msg.obj != null){
					mDetailEntity = (WiFiDetailEntity)msg.obj;

					wifi_detail_name.setText(mDetailEntity.getWifiName());

					int i = 200 - (mDetailEntity.getWifiIntensity() * 2);
					if(i > 100){
						i = 100;
					}
					signal_strength_name.setText((i) + "%");

					if (mDetailEntity.getWifiEncryption())
						encryption_name.setText("无加密");
					else
						encryption_name.setText("WPA/WPA2");

					max_speed_tv.setText(mDetailEntity.getMaxspeed()+"Mbps");
					ip_address_name.setText(mDetailEntity.getIp_address()+"");
					mac_address_name.setText(mDetailEntity.getWifi_mac_address());

					Animation wifidetail_one = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top);
					network_details_item.setAnimation(wifidetail_one);
					network_details_item.setVisibility(View.VISIBLE);

					network_details_arrow.setVisibility(View.GONE);
					network_details_arrow.clearAnimation();

					//开启第一个动画
					circle_safe.setautoRun(true);
					circle_safe.setVisibility(View.VISIBLE);

					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							handler.sendEmptyMessage(ONEIMGROTATING);

						}
					},1000);

					network_security_arrow.setAnimation(anim);
					network_security_arrow.setVisibility(View.VISIBLE);

                    //展开网络安全监测
					network_Security.start();

				}

			}else if (msg.what == NETWORKSECURTY){
				circle_safe.setfinsh(true);
				Animation wifidetail = AnimationUtils.loadAnimation(mContext, R.anim.push_in);
				speed_icon_safe.setAnimation(wifidetail);
				speed_icon_safe.setImageResource(R.drawable.icon_safe_shi);

				Animation wifidetail_one = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top);
				network_security_item.setAnimation(wifidetail_one);
				network_security_item.setVisibility(View.VISIBLE);

				speed_rotating.setVisibility(View.GONE);
				speed_rotating.clearAnimation();

				network_security_arrow.setVisibility(View.GONE);
				network_security_arrow.clearAnimation();

				//开启第二个动画
				timer2 = new Timer();
				timer2.schedule(new TimerTask() {
					@Override
					public void run() {
						startTest();
						handler.sendEmptyMessage(NETWORKSPEEDONE);

					}
				},1000);

			}else if (msg.what == NETWORKSPEED){
				speed_speed.setAnimation(operatingAnim);
				speed_speed.setVisibility(View.VISIBLE);
			}else if (msg.what == UPDATE_DNOE){
				isStart = false;
				setNetSpeedDes(netWorkSpeedInfo.speed / 1024);

				circle_speed.setfinsh(true);
				Animation wifidetail = AnimationUtils.loadAnimation(mContext, R.anim.push_in);
				speed_icon_speed.setAnimation(wifidetail);
				speed_icon_speed.setImageResource(R.drawable.icon_speed_shi);

				Animation wifidetail_one = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top);
				network_speed_item.setAnimation(wifidetail_one);
				network_speed_item.setVisibility(View.VISIBLE);

				speed_speed.setVisibility(View.GONE);
				speed_speed.clearAnimation();

				network_speed_arrow.setVisibility(View.GONE);
				network_speed_arrow.clearAnimation();

				int speedK = (int)netWorkSpeedInfo.speed / 1024;
				String speedValue = speedK < 1024 ? speedK +" Kb/s" : (speedK / 1024) +" Mb/s";
				network_speed_txt.setVisibility(View.VISIBLE);
				network_speed_txt.setText(speedValue);

				//开启第三个动画
				timer4 = new Timer();
				timer4.schedule(new TimerTask() {
					@Override
					public void run() {
						handler.sendEmptyMessage(NETWORKEQUIPMENTONE);

					}
				},1000);


			}else if (msg.what == NETWORKSPEEDONE){
                 //第二个动画开启
				discovery_loading_tv.setText("正在检测网络速度");
				jumpingBeans1 = JumpingBeans.with(discovery_loading_tv)
						.appendJumpingDots()
						.build();
				circle_speed.setautoRun(true);
				circle_speed.setVisibility(View.VISIBLE);

				timer1 = new Timer();
				timer1.schedule(new TimerTask() {
					@Override
					public void run() {
						handler.sendEmptyMessage(NETWORKSPEED);
					}
				},1000);

				network_speed_arrow.setAnimation(anim);
				network_speed_arrow.setVisibility(View.VISIBLE);
			}else if (msg.what == NETWORKEQUIPMENTONE){
				//第三个动画开启
				discovery_loading_tv.setText("正在检测在线设备");
				jumpingBeans1 = JumpingBeans.with(discovery_loading_tv)
						.appendJumpingDots()
						.build();
				circle_equipment.setautoRun(true);
				circle_equipment.setVisibility(View.VISIBLE);

				timer5 = new Timer();
				timer5.schedule(new TimerTask() {
					@Override
					public void run() {
						handler.sendEmptyMessage(NETWORKEQUIPMENT);
					}
				},2000);

				online_equipment_arrow.setAnimation(anim);
				online_equipment_arrow.setVisibility(View.VISIBLE);

				network_equipment.start();


			}else if (msg.what == NETWORKEQUIPMENT){
				speed_equipment.setAnimation(operatingAnim);
				speed_equipment.setVisibility(View.VISIBLE);
			}else if (msg.what == NETWORKEQUIPMENTSIZE){
				String size = "0";
				if (msg.obj != null){
					size = (String)msg.obj;
				}

				circle_equipment.setfinsh(true);
				Animation wifidetail = AnimationUtils.loadAnimation(mContext, R.anim.push_in);
				speed_icon_equipment.setAnimation(wifidetail);
				speed_icon_equipment.setImageResource(R.drawable.icon_safe_shi);

				network_equipment_size.setText("发现"+size+"台设备连接次WiFi");

				Animation wifidetail_one = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top);
				network_equipment_item.setAnimation(wifidetail_one);
				network_equipment_item.setVisibility(View.VISIBLE);

				speed_equipment.setVisibility(View.GONE);
				speed_equipment.clearAnimation();

				online_equipment_arrow.setVisibility(View.GONE);
				online_equipment_arrow.clearAnimation();

				//动画播放完毕 开启分数动画
				timer6 = new Timer();
				timer6.schedule(new TimerTask() {
					@Override
					public void run() {
						handler.sendEmptyMessage(NETWORKEFINSH);
					}
				},500);


			}else if (msg.what == NETWORKEFINSH){
				//动画播放完毕 开启分数动画
				discovery_loading_lay.setVisibility(View.GONE);
				discovery_finsh_lay.setVisibility(View.VISIBLE);
				if (finshNum == 100){
					discovery_loading_tv.setText("网络安全，网络适合视频");
				}else if (finshNum == 90){
					discovery_loading_tv.setText("网络安全，网络适合游戏");
				}else if (finshNum == 80){
					discovery_loading_tv.setText("网络安全，网络适合上网");
				}else if (finshNum == 70){
					discovery_loading_tv.setText("网络安全，网络适合上网");
				}else if (finshNum == 60){
					discovery_loading_tv.setText("网络安全，网络适合聊天");
				}
				jumpingBeans1.stopJumping();
				phy_exa_num.setFromAndEndNumber(0, finshNum);
				phy_exa_num.setDuration(3000);
				phy_exa_num.start();
			}
		}
	};

	public void setNetSpeedDes(float speedK){
		if(speedK > 500) {
			network_speed_text.setText("网速雕堡了,能够流畅跑各种app应用");
			finshNum = 100;
		}else if(speedK > 200) {
			network_speed_text.setText("网速挺快,能够流畅跑大多数app应用");
			network_speed_video.setVisibility(View.GONE);
			finshNum = 90;
		}else if(speedK > 50) {
			network_speed_text.setText("网速一般,当前网速可正常打开网页");
			network_speed_game.setVisibility(View.GONE);
			network_speed_video.setVisibility(View.GONE);
			finshNum = 80;
		}else if(speedK > 20) {
			network_speed_text.setText("网速挺低，当前网速可正常打开网页");
			network_speed_game.setVisibility(View.GONE);
			network_speed_video.setVisibility(View.GONE);
			finshNum = 70;
		}else {
			network_speed_text.setText("当前网速龟速，比较影响上网访问");
			network_speed_online.setVisibility(View.GONE);
			network_speed_game.setVisibility(View.GONE);
			network_speed_video.setVisibility(View.GONE);
			finshNum = 60;
		}
	}


	private String intToIp(int i) {

		return (i & 0xFF ) + "." +
				((i >> 8 ) & 0xFF) + "." +
				((i >> 16 ) & 0xFF) + "." +
				( i >> 24 & 0xFF) ;
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.headerLeft:
				finish();
				break;
			case R.id.to_test_btn:
				Intent intent = new Intent(mContext, NetSpeedActivity.class);
				intent.putExtra("wifiname",mDetailEntity.getWifiName());
				startActivity(intent);
				break;
			case R.id.check_the_equipment:
				if (BaseUtils.isWifiConnected(mContext)) {
					Intent intent1 = new Intent(mContext, ActivityDiscovery.class);
					startActivity(intent1);
				}else {
					ShowToast("连接上WiFi才能使用");
				}
				break;


			default:
				break;
		}
	}

	private void startTest() {
		//测速相关
		final long clickTime = System.currentTimeMillis();

		new Thread() {
			@Override
			public void run() {
				Log.i("开始", "**********开始  ReadFile*******");
				mReadFile = new ReadFile();
				imageData = mReadFile.getFileFromUrl(url, netWorkSpeedInfo, testTime);
				handler.sendEmptyMessage(UPDATE_DNOE);
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
