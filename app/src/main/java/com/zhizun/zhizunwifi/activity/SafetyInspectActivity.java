package com.zhizun.zhizunwifi.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.view.FlipImageView;


/**
 * @项目名: ZhizunWIFI
 * @包名: com.zhizun.zhizunwifi.activity
 * @类名: SafetyInspectActivity
 * @创建者: 梁辉
 * @创建时间: 2016-4-7 上午10:30:04
 * @描述: 安全检查
 *
 * @SVN版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class SafetyInspectActivity extends BaseActivity{


	/*
	 * 获取IP对应的地址
	 * http://ip.taobao.com/service/getIpInfo.php?ip=192.167.8.8
	 * "country_id":"IANA", 为内网--8.8.8.8不需测试，正常DNS
	 * {
    "code":0,
    "data":{
        "country":"未分配或者内网IP",
        "country_id":"IANA",
        "area":"",
        "area_id":"",
        "region":"",
        "region_id":"",
        "city":"",
        "city_id":"",
        "county":"",
        "county_id":"",
        "isp":"",
        "isp_id":"",
        "ip":"172.16.8.8"
    }
    }
	 */

	private Handler mHandler;

	private ProgressBar progressBar;

	private List<String> listData;
	private ListView lv;
	private LinearLayout ll;
	private FlipImageView ImageStatus;

	private TextView tv_ssid;

	private LayoutInflater inflater;
	private Animation anim;

	private Spinner mSpinner;

	String wifiName;

	private static final Interpolator[] fInterpolators = new Interpolator[]{
			new DecelerateInterpolator(),
			new AccelerateInterpolator(),
			new AccelerateDecelerateInterpolator(),
			new BounceInterpolator(),
			new OvershootInterpolator(),
			new AnticipateOvershootInterpolator()
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety_inspect);

		initData();
		initListData();
		initView();
		initHandler();
		addListener();

		findViewById(R.id.headerLeft).setOnClickListener(this);
		findViewById(R.id.btnStartNet).setOnClickListener(this);
	}

	private void initData(){
		wifiName = getIntent().getStringExtra("wifiName");
	}

	private void initListData() {
		listData = new ArrayList<String>();
		listData.add("是否连接WiFi");
		listData.add("是否能上网");
		listData.add("检测DNS是否正常");
		listData.add("检测是否收到ARP攻击");
		listData.add("检测虚假WiFi");
		listData.add("检测WiFi是否加密");
	}

	private void initView() {

		tv_ssid = (TextView) findViewById(R.id.tv_ssid);
		tv_ssid.setText("所连接的WIFI " + wifiName);

		progressBar = (ProgressBar) findViewById(R.id.pb_checking);
		lv = (ListView) findViewById(R.id.lv_checking);
		ll = (LinearLayout) findViewById(R.id.inspect_past);
		ImageStatus = (FlipImageView) findViewById(R.id.imageview);
		MyAdapter mAdapter = new MyAdapter();
		lv.setAdapter(mAdapter);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	private void initHandler() {
		mHandler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {

				System.out.println(msg.what);

				//第一个
				if(msg.what == 0){
					View childAt = lv.getChildAt(msg.what);
					ImageView imgwait = (ImageView) childAt.findViewById(R.id.iv_wait);
					ImageView proceed = (ImageView) childAt.findViewById(R.id.iv_proceed);
					ImageView accomplish = (ImageView) childAt.findViewById(R.id.iv_accomplish);
					proceed.setAnimation(anim);
					imgwait.setVisibility(View.GONE);
					proceed.setVisibility(View.VISIBLE);
					accomplish.setVisibility(View.GONE);

					childAt = null;
				}else if(msg.what <= 5){
					//中间
					View childAt = lv.getChildAt(msg.what);
					ImageView proceed = (ImageView) childAt.findViewById(R.id.iv_proceed);
					ImageView accomplish = (ImageView) childAt.findViewById(R.id.iv_accomplish);
					proceed.setAnimation(anim);
					proceed.setVisibility(View.VISIBLE);
					accomplish.setVisibility(View.GONE);

					View prevChildAt = lv.getChildAt(msg.what - 1);
					ImageView prevWait = (ImageView) prevChildAt.findViewById(R.id.iv_wait);
					ImageView prevProceed = (ImageView) prevChildAt.findViewById(R.id.iv_proceed);
					ImageView prevAccomplish = (ImageView) prevChildAt.findViewById(R.id.iv_accomplish);
					prevWait.setVisibility(View.GONE);
					prevProceed.setVisibility(View.GONE);
					prevProceed.clearAnimation();
					progressBar.setProgress(msg.what);
					prevAccomplish.setVisibility(View.VISIBLE);

					prevChildAt = null;

				}else if(msg.what > 5){
					View childAt = lv.getChildAt(msg.what - 1);
					ImageView imgwait = (ImageView) childAt.findViewById(R.id.iv_wait);
					ImageView proceed = (ImageView) childAt.findViewById(R.id.iv_proceed);
					ImageView accomplish = (ImageView) childAt.findViewById(R.id.iv_accomplish);
					imgwait.setVisibility(View.GONE);
					proceed.setVisibility(View.GONE);
					proceed.clearAnimation();
					progressBar.setProgress(msg.what);
					accomplish.setVisibility(View.VISIBLE);


					//隐藏listview 显示开始上网按钮
					lv.setVisibility(View.GONE);
					ll.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);

					ImageStatus.toggleFlip();
					ImageStatus.setInterpolator(new DecelerateInterpolator());
					ImageStatus.setDuration(500);//动画持续时间
					ImageStatus.setRotationXEnabled(false);
					ImageStatus.setRotationYEnabled(true);
					ImageStatus.setRotationZEnabled(false);
					ImageStatus.setRotationReversed(false);

				}
				return false;


			}
		});

	}

	private void addListener() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				for (int i = 0; i <= 6; i++) {
					//进度中
					mHandler.sendEmptyMessage(i);

//					msg.what = START_INSPECT;
//					mHandler.sendMessage(msg);
					try {
						int random = (int) (Math.random() * 3000 );
						Thread.sleep(random);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					//进度后
//					mHandler.sendMessage(msg);
//					mHandler.sendEmptyMessage(i);
				}
				//检查完毕

			}

		}).start();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
			case R.id.headerLeft:
				finish();
				break;
			case R.id.btnStartNet:
				Intent intent = new Intent(MainTabActivity.SafetyInspectActionNet);
				setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				break;
		}
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(anim==null){
				anim = AnimationUtils.loadAnimation(SafetyInspectActivity.this, R.anim.rotate);
			}
			View view = inflater.inflate(R.layout.item_safety_inspect, null);
			TextView text = (TextView) view.findViewById(R.id.tv_text);
//			ImageView imageWait = (ImageView) view.findViewById(R.id.iv_wait);
			ImageView imageProceed = (ImageView) view.findViewById(R.id.iv_proceed);
//			ImageView imageAccomplish = (ImageView) view.findViewById(R.id.iv_accomplish);

//			imageProceed.setAnimation(anim);
			text.setText(listData.get(position));
			return view;
		}



	}
}
